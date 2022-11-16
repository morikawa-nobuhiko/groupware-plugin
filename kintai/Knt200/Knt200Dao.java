package jp.groupsession.v2.kintai.Knt200;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import jp.co.sjts.util.NullDefault;
import jp.co.sjts.util.date.UDate;
import jp.co.sjts.util.jdbc.JDBCUtil;
import jp.co.sjts.util.jdbc.SqlBuffer;
import jp.co.sjts.util.dao.AbstractDao;
import jp.groupsession.v2.struts.AbstractGsAction;
import jp.groupsession.v2.cmn.GSConst;
import jp.groupsession.v2.cmn.GSConstSchedule;
import jp.groupsession.v2.cmn.config.PluginConfig;
import jp.groupsession.v2.cmn.dao.GroupModel;
import jp.groupsession.v2.cmn.dao.BaseUserModel;
import jp.groupsession.v2.cmn.dao.UsidSelectGrpNameDao;
import jp.groupsession.v2.cmn.dao.BaseUserModel;
import jp.groupsession.v2.cmn.model.RequestModel;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Knt200Dao extends AbstractDao {

	/** Logging インスタンス */
	private static Log log__ = LogFactory.getLog(Knt200Dao.class);

	/**
	 * <p>Timestampから指定した書式を返す
	 * @param ts Timestamp
	 * @return int
	 */
	public int toTimestampYmd(Timestamp ts, String fmt){
		SimpleDateFormat sdf = new SimpleDateFormat(fmt);
		String str = sdf.format(ts);
		int ret = Integer.parseInt(str);
		return ret;
	}

	/**
	 * <p>パラメーターによる作業実績が存在するかの取得
	 * @param usrSid ユーザSID
	 * @param sy    開始年
	 * @param sm    開始月
	 * @param sd    開始月
	 * @param ey    終了年
	 * @param em    終了月
	 * @param ed    終了月
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return 作業実績合計時間
	 */
	public Boolean containsUserJsk(int usrSid, int y, int m, int d, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Boolean ret = false;
		Boolean isJsk = false;

		try {

			// TIMESTAMPの書式
			String STPD = y + "-" + m + "-" + d + " " + "00:00:00";

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   * ");
			sql.addSql(" from");
			sql.addSql("   KNT_JSK");
			sql.addSql(" where");
			sql.addSql("   JSK_USR_SID = ?");
			sql.addSql("   and");
			sql.addSql("   JSK_YMD = ?");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(usrSid);
			sql.addStrValue(STPD);
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();
			ret = rs.next();

			if (ret){
				// 存在する
				isJsk = true;
			} else {
				// 存在しない
				isJsk = false;
			}

		} catch (SQLException e) {
			throw e;
		} finally {
			JDBCUtil.closeResultSet(rs);
			JDBCUtil.closeStatement(pstmt);
		}
		return ret;
	}

	/**
	 * <p>パラメーターによる作業実績合計時間（有給除外）の取得（1件）
	 * @param usrSid ユーザSID
	 * @param sy    開始年
	 * @param sm    開始月
	 * @param sd    開始月
	 * @param ey    終了年
	 * @param em    終了月
	 * @param ed    終了月
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return 作業実績合計時間
	 */
	public Knt200Model selectFromToTotalTime(int usrSid,
											int sy, int sm, int sd,
											Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Knt200Model ret = new Knt200Model();

		try {

			// TIMESTAMPの書式
			String START = sy + "-" + sm + "-" + sd + " " + "00:00:00";

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   JSK_USR_SID, ");
			sql.addSql("   JSK_YMD, ");
			sql.addSql("   sum(JSK_WRK_TIME)");
			sql.addSql(" from");
			sql.addSql("   KNT_JSK");
			sql.addSql(" where");
			sql.addSql("   JSK_USR_SID = ?");
			sql.addSql(" and");
			sql.addSql("   JSK_YMD = ?");
			sql.addSql("   group by JSK_USR_SID, JSK_YMD");
			sql.addSql("   order by JSK_YMD");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(usrSid);
			sql.addStrValue(START);
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			ret.setUsrSid(usrSid);
			ret.setWrkYear(sy);
			ret.setWrkMonth(sm);
			ret.setWrkDay(sd);
			ret.setPaidTime(0);
			ret.setWrkTime(0);
			ret.setSmuChk(0);
			ret.setSmuRmk("");

			// 有給合計時間を取得
			while (rs.next()) {
				double paidTime = selectFromToPaidTotalTime(usrSid, sy, sm, sd, 99, con);
				ret.setPaidTime(paidTime);
				ret.setWrkTime(rs.getDouble("sum(JSK_WRK_TIME)")-paidTime);
			}

		} catch (SQLException e) {
			throw e;
		} finally {
			JDBCUtil.closeResultSet(rs);
			JDBCUtil.closeStatement(pstmt);
		}
		return ret;
	}

	/**
	 * <p>パラメーターによる作業実績合計時間（有給除外）の取得
	 * @param usrSid ユーザSID
	 * @param sy    開始年
	 * @param sm    開始月
	 * @param sd    開始月
	 * @param ey    終了年
	 * @param em    終了月
	 * @param ed    終了月
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return 作業実績合計時間
	 */
	public ArrayList<Knt200Model> selectFromToTotalTime(int usrSid,
														int sy, int sm, int sd,
														int ey, int em, int ed,
														Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Knt200Model> ret = new ArrayList<Knt200Model>();

		try {

			// TIMESTAMPの書式
			String START = sy + "-" + sm + "-" + sd + " " + "00:00:00";
			String END = ey + "-" + em + "-" + ed + " " + "23:59:59";

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   JSK_USR_SID, ");
			sql.addSql("   JSK_YMD, ");
			sql.addSql("   sum(JSK_WRK_TIME)");
			sql.addSql(" from");
			sql.addSql("   KNT_JSK");
			sql.addSql(" where");
			sql.addSql("   JSK_USR_SID = ?");
			sql.addSql(" and");
			sql.addSql("   JSK_YMD >= ?");
			sql.addSql(" and");
			sql.addSql("   JSK_YMD <= ?");
			sql.addSql("   group by JSK_USR_SID, JSK_YMD");
			sql.addSql("   order by JSK_YMD");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(usrSid);
			sql.addStrValue(START);
			sql.addStrValue(END);
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// 有給合計時間を取得
				int ty = toTimestampYmd(rs.getTimestamp("JSK_YMD"), "yyyy");
				int tm = toTimestampYmd(rs.getTimestamp("JSK_YMD"), "MM");
				int td = toTimestampYmd(rs.getTimestamp("JSK_YMD"), "dd");
				double paidTime = selectFromToPaidTotalTime(usrSid, ty, tm, td, 99, con);
				Knt200Model model = new Knt200Model();
				model.setUsrSid(usrSid);
				model.setWrkYear(ty);
				model.setWrkMonth(tm);
				model.setWrkDay(td);
				model.setPaidTime(paidTime);
				model.setWrkTime(rs.getDouble("sum(JSK_WRK_TIME)")-paidTime);
				model.setSmuChk(0);
				model.setSmuRmk("");
				ret.add(model);
			}

		} catch (SQLException e) {
			throw e;
		} finally {
			JDBCUtil.closeResultSet(rs);
			JDBCUtil.closeStatement(pstmt);
		}
		return ret;
	}

	/**
	 * <p>パラメーターによる有給合計時間の取得
	 * @param usrSid ユーザSID
	 * @param y     年
	 * @param m     月
	 * @param d     月
	 * @param uq    有給ID
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return 作業実績合計時間
	 */
	public Double selectFromToPaidTotalTime(int usrSid, int y, int m, int d, int uq, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Double ret = 0.0;

		try {

			// TIMESTAMPの書式
			String STPD = y + "-" + m + "-" + d + " " + "00:00:00";

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   JSK_USR_SID, ");
			sql.addSql("   JSK_YMD, ");
			sql.addSql("   sum(JSK_WRK_TIME)");
			sql.addSql(" from");
			sql.addSql("   KNT_JSK");
			sql.addSql(" where");
			sql.addSql("   JSK_USR_SID = ?");
			sql.addSql(" and");
			sql.addSql("   JSK_YMD = ?");
			sql.addSql(" and");
			sql.addSql("   JSK_WRK_ID = ?");
			sql.addSql("   group by JSK_USR_SID");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(usrSid);
			sql.addStrValue(STPD);
			sql.addIntValue(uq);
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				ret = rs.getDouble("sum(JSK_WRK_TIME)");
				break;
			}

		} catch (SQLException e) {
			throw e;
		} finally {
			JDBCUtil.closeResultSet(rs);
			JDBCUtil.closeStatement(pstmt);
		}
		return ret;
	}

	/**
	 * <p>総務チェックの存在チェック
	 * @param usrSid  int
	 * @param yyyy  int
	 * @param mm    int
	 * @param dd    int
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return 作業実績合計時間
	 */
	public Boolean isNewSmuCheck(int usrSid, int yyyy, int mm, int dd, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Boolean ret = false;
		Boolean isNew = false;

		try {

			// TIMESTAMPの書式
			String STPD = yyyy + "-" + mm + "-" + dd + " " + "00:00:00";

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   *");
			sql.addSql(" from");
			sql.addSql("   KNT_SMU");
			sql.addSql(" where");
			sql.addSql("   SMU_USR_SID = ?");
			sql.addSql(" and");
			sql.addSql("   SMU_YMD = ?");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(usrSid);
			sql.addStrValue(STPD);
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();
			ret = rs.next();

			if (ret){
				// 存在する
				isNew = false;
			} else {
				// 存在しない
				isNew = true;
			}

		} catch (SQLException e) {
			throw e;
		} finally {
			JDBCUtil.closeResultSet(rs);
			JDBCUtil.closeStatement(pstmt);
		}

		return isNew;

	}

	/**
	 * 総務確認の取得
	 * @param usrSid ユーザSID
	 * @param sy    開始年
	 * @param sm    開始月
	 * @param sd    開始月
	 * @param ey    終了年
	 * @param em    終了月
	 * @param ed    終了月
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return 作業実績合計時間
	 */
	public Knt200Model selectSmuCheck(int usrSid, int y, int m, int d, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Knt200Model ret = new Knt200Model();

		try {

			// TIMESTAMPの書式
			String STPD = y + "-" + m + "-" + d + " " + "00:00:00";

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   * ");
			sql.addSql(" from");
			sql.addSql("   KNT_SMU");
			sql.addSql(" where");
			sql.addSql("   SMU_USR_SID = ?");
			sql.addSql("   and");
			sql.addSql("   SMU_YMD = ?");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(usrSid);
			sql.addStrValue(STPD);
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				int ty = toTimestampYmd(rs.getTimestamp("SMU_YMD"), "yyyy");
				int tm = toTimestampYmd(rs.getTimestamp("SMU_YMD"), "MM");
				int td = toTimestampYmd(rs.getTimestamp("SMU_YMD"), "dd");
				Knt200Model model = new Knt200Model();
				model.setUsrSid(usrSid);
				model.setWrkYear(ty);
				model.setWrkMonth(tm);
				model.setWrkDay(td);
				model.setSmuChk(rs.getInt("SMU_CHK"));
				model.setSmuRmk(rs.getString("SMU_RMK"));
				ret = model;
				break;
			}

		} catch (SQLException e) {
			throw e;
		} finally {
			JDBCUtil.closeResultSet(rs);
			JDBCUtil.closeStatement(pstmt);
		}
		return ret;
	}

	/**
	* <br>[機  能] 総務確認を登録する
	* <br>[解  説]
	* <br>[備  考]
	 * @param usrSid int
	 * @param yyyy  int
	 * @param mm    int
	 * @param dd    int
	 * @param chk   boolean
	 * @param rmk   String
	* @param con   コネクション
	* @throws SQLException SQL実行時例外
	*/
	public void insertSmuCheck(int usrSid, int yyyy, int mm, int dd, int chk, String rmk, Connection con) throws SQLException {

		PreparedStatement pstmt = null;

		try {

			// TIMESTAMPの書式
			String STPD = yyyy + "-" + mm + "-" + dd + " " + "00:00:00";

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" insert ");
			sql.addSql(" into ");
			sql.addSql(" KNT_SMU(");
			sql.addSql("   SMU_USR_SID,");
			sql.addSql("   SMU_YMD,");
			sql.addSql("   SMU_CHK,");
			sql.addSql("   SMU_RMK");
			sql.addSql(" )");
			sql.addSql(" values");
			sql.addSql(" (");
			sql.addSql("   ?,");
			sql.addSql("   ?,");
			sql.addSql("   ?,");
			sql.addSql("   ?");
			sql.addSql(" )");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(usrSid);
			sql.addStrValue(STPD);
			sql.addIntValue(chk);
			sql.addStrValue(rmk);
			log__.info(sql.toLogString());
			sql.setParameter(pstmt);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} finally {
			JDBCUtil.closeStatement(pstmt);
		}

	}

	/**
	* <br>[機  能] 総務確認を更新する
	* <br>[解  説]
	* <br>[備  考]
	 * @param usrSid int
	 * @param yyyy  int
	 * @param mm    int
	 * @param dd    int
	 * @param chk   int
	 * @param rmk   String
	* @param con   コネクション
	* @throws SQLException SQL実行時例外
	*/
	public void updateSmuCheck(int usrSid, int yyyy, int mm, int dd, int chk, String rmk, Connection con) throws SQLException {

		PreparedStatement pstmt = null;

		try {

			// TIMESTAMPの書式
			String STPD = yyyy + "-" + mm + "-" + dd + " " + "00:00:00";

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" update");
			sql.addSql("   KNT_SMU");
			sql.addSql(" set ");
			sql.addSql("   SMU_CHK=?,");
			sql.addSql("   SMU_RMK=?");
			sql.addSql(" where");
			sql.addSql("   SMU_USR_SID = ?");
			sql.addSql(" and");
			sql.addSql("   SMU_YMD = ?");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(chk);
			sql.addStrValue(rmk);

			sql.addIntValue(usrSid);
			sql.addStrValue(STPD);

			log__.info(sql.toLogString());
			sql.setParameter(pstmt);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} finally {
			JDBCUtil.closeStatement(pstmt);
		}

	}

	/**
	 * <p>通常残業時間（日別）を取得（通常残業時間帯 18:00-22:00）
	 * @param userId ユーザーID
	 * @param yyyy   年
	 * @param mm     月
	 * @param dd     日
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return 作業実績合計時間
	 */
	public double selectDayOverTime(int usrSid, int yyyy, int mm, int dd, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Boolean ret = false;
		double res = 0;
		SqlBuffer sql = new SqlBuffer();

		try {

			// 次の日付を取得する
			Calendar cal=Calendar.getInstance();
			cal.clear();
			cal.set(yyyy, mm -1, dd);
			cal.add(Calendar.DAY_OF_MONTH, 1);
			int toY = cal.get(Calendar.YEAR);
			int toM = cal.get(Calendar.MONTH) + 1;
			int toD = cal.get(Calendar.DATE);

			// TIMESTAMPの書式
			String START = yyyy + "-" + mm  + "-" + dd  + " 06:00:00";
			String END   = toY  + "-" + toM + "-" + toD + " 04:00:00";
			String OVER_START = yyyy + "-" + mm + "-" + dd + " 18:00:00";
			String OVER_END   = yyyy + "-" + mm + "-" + dd + " 22:00:00";

			//退勤時間が通常残業時間帯かの確認
			sql.addSql(" SELECT OUT FROM");
			sql.addSql(" (SELECT STP_TIME AS OUT FROM KNT_STP WHERE");
			sql.addSql("  STP_USR_SID = ?");
			sql.addSql("   AND");
			sql.addSql("  STP_TIME >= ?");
			sql.addSql("   AND");
			sql.addSql("  STP_TIME <= ?");
			sql.addSql("   AND");
			sql.addSql("  STP_IOM_ID = 1");
			sql.addSql(" )");
			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(usrSid);
			sql.addStrValue(OVER_START);
			sql.addStrValue(OVER_END);
			// SQL実行
			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();
			ret = rs.next();
			log__.info(sql.toLogString());
			log__.debug("selectDayOverTime_ret=" + ret);

			if (!ret){
				log__.debug("退勤時間が通常残業時間帯を超える");

				// 退勤時間が通常残業時間帯を超える
				sql = new SqlBuffer();
				sql.addSql(" SELECT");
				sql.addSql("   TIMESTAMPDIFF(MINUTE,");
				sql.addSql("   ?,");
				sql.addSql("   ?)");
				sql.addSql("   AS TIME");
				sql.addSql(" FROM");
				sql.addSql("   (SELECT * FROM KNT_STP");
				sql.addSql("    WHERE");
				sql.addSql("     STP_USR_SID = ?");
				sql.addSql("    AND");
				sql.addSql("     STP_TIME >= ?");
				sql.addSql("    AND");
				sql.addSql("     STP_TIME <= ?");
				sql.addSql("    AND");
				sql.addSql("     STP_IOM_ID = 1)");
				// SQL実行
				pstmt = con.prepareStatement(sql.toSqlString());
				sql.addStrValue(OVER_START);
				sql.addStrValue(OVER_END);
				sql.addIntValue(usrSid);
				sql.addStrValue(START);
				sql.addStrValue(END);
				sql.setParameter(pstmt);
				rs = pstmt.executeQuery();
				log__.info(sql.toLogString());
				while (rs.next()) {
					res = rs.getDouble("TIME");
					res = res / 60;
					break;
				}

			} else {
				log__.debug("通常残業時間帯");

				// 通常残業時間帯
				sql = new SqlBuffer();
				sql.addSql(" SELECT");
				sql.addSql("   TIMESTAMPDIFF(MINUTE, ?, OUT) AS TIME");
				sql.addSql(" FROM");
				sql.addSql("   (SELECT MAX(STP_TIME) AS OUT");
				sql.addSql("    FROM");
				sql.addSql("     KNT_STP");
				sql.addSql("    WHERE");
				sql.addSql("     STP_USR_SID = ?");
				sql.addSql("    AND");
				sql.addSql("     STP_TIME >= ?");
				sql.addSql("    AND");
				sql.addSql("     STP_TIME <= ?");
				sql.addSql("    AND");
				sql.addSql("     STP_IOM_ID = 1)");
				// SQL実行
				pstmt = con.prepareStatement(sql.toSqlString());
				sql.addStrValue(OVER_START);
				sql.addIntValue(usrSid);
				sql.addStrValue(START);
				sql.addStrValue(END);
				sql.setParameter(pstmt);
				rs = pstmt.executeQuery();
				log__.info(sql.toLogString());
				while (rs.next()) {
					res = rs.getDouble("TIME");
					res = res / 60;
					break;
				}

			}

			// 残業時間帯に中抜けがある場合は考慮

		} catch (SQLException e) {
			throw e;
		} finally {
			JDBCUtil.closeResultSet(rs);
			JDBCUtil.closeStatement(pstmt);
		}

		return res;

	}

	/**
	 * <p>深夜残業時間（日別）を取得（深夜残業時間帯 22:30-04:00）
	 * @param userId ユーザーID
	 * @param yyyy   年
	 * @param mm     月
	 * @param dd     日
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return 作業実績合計時間
	 */
	public double selectDayMidnightTime(int usrSid, int yyyy, int mm, int dd, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Boolean ret = false;
		double res = 0;
		SqlBuffer sql = new SqlBuffer();

		try {

			// 次の日付を取得する
			Calendar cal=Calendar.getInstance();
			cal.clear();
			cal.set(yyyy, mm -1, dd);
			cal.add(Calendar.DAY_OF_MONTH, 1);
			int toY = cal.get(Calendar.YEAR);
			int toM = cal.get(Calendar.MONTH) + 1;
			int toD = cal.get(Calendar.DATE);

			// TIMESTAMPの書式
			String START = yyyy + "-" + mm  + "-" + dd  + " 06:00:00";
			String END   = toY  + "-" + toM + "-" + toD + " 04:00:00";
			String MIDNIGHT_START = yyyy + "-" + mm + "-" + dd       + " 22:30:00";
			String MIDNIGHT_END   = yyyy + "-" + mm + "-" + (dd + 1) + " 04:00:00";

			// 深夜残業時間帯
			sql = new SqlBuffer();
			sql.addSql(" SELECT");
			sql.addSql("   TIMESTAMPDIFF(MINUTE, ?, OUT) AS TIME");
			sql.addSql(" FROM");
			sql.addSql("   (SELECT MAX(STP_TIME) AS OUT");
			sql.addSql("    FROM");
			sql.addSql("     KNT_STP");
			sql.addSql("    WHERE");
			sql.addSql("     STP_USR_SID = ?");
			sql.addSql("    AND");
			sql.addSql("     STP_TIME >= ?");
			sql.addSql("    AND");
			sql.addSql("     STP_TIME <= ?");
			sql.addSql("    AND");
			sql.addSql("     STP_IOM_ID = 1)");
			// SQL実行
			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addStrValue(MIDNIGHT_START);
			sql.addIntValue(usrSid);
			sql.addStrValue(START);
			sql.addStrValue(END);
			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();
			log__.info(sql.toLogString());
			while (rs.next()) {
				res = rs.getDouble("TIME");
				res = res / 60;
				break;
			}

			// 退勤時間が深夜残業時間帯前の時は0をセット
			if (res <= 0 ) {
				res = 0;
			}

			// 残業時間帯に中抜けがある場合は考慮

		} catch (SQLException e) {
			throw e;
		} finally {
			JDBCUtil.closeResultSet(rs);
			JDBCUtil.closeStatement(pstmt);
		}

		return res;

	}

	/**
	 * <p>通常残業時間（日別）の中抜け時間を取得（通常残業時間帯 18:00-22:00）
	 * @param userId ユーザーID
	 * @param yyyy   年
	 * @param mm     月
	 * @param dd     日
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return 作業実績合計時間
	 */
	public double selectDayOverHollowTime(int usrSid, int yyyy, int mm, int dd, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Boolean ret = false;
		double res = 0;
		SqlBuffer sql = new SqlBuffer();

		try {

			// 次の日付を取得する
			Calendar cal=Calendar.getInstance();
			cal.clear();
			cal.set(yyyy, mm -1, dd);
			cal.add(Calendar.DAY_OF_MONTH, 1);
			int toY = cal.get(Calendar.YEAR);
			int toM = cal.get(Calendar.MONTH) + 1;
			int toD = cal.get(Calendar.DATE);

			// TIMESTAMPの書式
			String START = yyyy + "-"  + mm  + "-" + dd  + " 06:00:00";
			String END   = toY   + "-" + toM + "-" + toD + " 04:00:00";
			String OVER_START = yyyy + "-" + mm + "-" + dd + " 18:00:00";
			String OVER_END   = yyyy + "-" + mm + "-" + dd + " 22:00:00";

			// 時間内（出社）が通常残業時間帯かの確認
			sql.addSql(" SELECT IN FROM");
			sql.addSql(" (SELECT STP_TIME AS IN FROM KNT_STP");
			sql.addSql("   WHERE");
			sql.addSql("    STP_USR_SID = ?");
			sql.addSql("   AND");
			sql.addSql("    STP_TIME >= ?");
			sql.addSql("   AND");
			sql.addSql("    STP_TIME <= ?");
			sql.addSql("   AND");
			sql.addSql("    STP_IOM_ID = 3");
			sql.addSql(" )");
			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(usrSid);
			sql.addStrValue(OVER_START);
			sql.addStrValue(OVER_END);
			// SQL実行
			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();
			ret = rs.next();
			log__.info(sql.toLogString());
			log__.debug("selectDayOverHollowTime_ret=" + ret);

			if (!ret){
				log__.debug("時間内（出社）時間が通常残業時間帯を超える");

				// 退勤時間が通常残業時間帯を超える
				sql = new SqlBuffer();
				sql.addSql(" SELECT");
				sql.addSql("   TIMESTAMPDIFF(MINUTE,");
				sql.addSql("   ?,");
				sql.addSql("   ?)");
				sql.addSql("   AS TIME");
				sql.addSql(" FROM");
				sql.addSql("   (SELECT * FROM KNT_STP");
				sql.addSql("    WHERE");
				sql.addSql("     STP_USR_SID = ?");
				sql.addSql("    AND");
				sql.addSql("     STP_TIME >= ?");
				sql.addSql("    AND");
				sql.addSql("     STP_TIME <= ?");
				sql.addSql("    AND");
				sql.addSql("     STP_IOM_ID = 3)");
				// SQL実行
				pstmt = con.prepareStatement(sql.toSqlString());
				sql.addStrValue(OVER_START);
				sql.addStrValue(OVER_END);
				sql.addIntValue(usrSid);
				sql.addStrValue(START);
				sql.addStrValue(END);
				sql.setParameter(pstmt);
				rs = pstmt.executeQuery();
				log__.info(sql.toLogString());
				while (rs.next()) {
					res = rs.getDouble("TIME");
					res = res / 60;
					break;
				}

			} else {
				log__.debug("通常残業時間帯");

				// 通常残業時間帯の中抜け
				sql = new SqlBuffer();
				sql.addSql(" SELECT");
				sql.addSql("   TIMESTAMPDIFF(MINUTE, OUT, IN) AS TIME");
				sql.addSql(" FROM");
				sql.addSql("   (SELECT MIN(STP_TIME) AS OUT, MAX(STP_TIME) AS IN");
				sql.addSql("    FROM");
				sql.addSql("     KNT_STP");
				sql.addSql("    WHERE");
				sql.addSql("     STP_USR_SID = ?");
				sql.addSql("    AND");
				sql.addSql("     STP_TIME >= ?");
				sql.addSql("    AND");
				sql.addSql("     STP_TIME <= ?");
				sql.addSql("    AND");
				sql.addSql("     (STP_IOM_ID = 2 OR STP_IOM_ID = 3)");
				sql.addSql("   )");
				// SQL実行
				pstmt = con.prepareStatement(sql.toSqlString());
				sql.addIntValue(usrSid);
				sql.addStrValue(START);
				sql.addStrValue(END);
				sql.setParameter(pstmt);
				rs = pstmt.executeQuery();
				log__.info(sql.toLogString());
				while (rs.next()) {
					res = rs.getDouble("TIME");
					res = res / 60;
					break;
				}

			}

		} catch (SQLException e) {
			throw e;
		} finally {
			JDBCUtil.closeResultSet(rs);
			JDBCUtil.closeStatement(pstmt);
		}

		return res;

	}

	/**
	 * <p>深夜残業時間（日別）の中抜け時間を取得（深夜残業時間帯 22:30-04:00）
	 * @param userId ユーザーID
	 * @param yyyy   年
	 * @param mm     月
	 * @param dd     日
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return 作業実績合計時間
	 */
	public double selectDayMidnightHollowTime(int usrSid, int yyyy, int mm, int dd, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Boolean ret = false;
		double res = 0;
		SqlBuffer sql = new SqlBuffer();

		try {

			// 次の日付を取得する
			Calendar cal=Calendar.getInstance();
			cal.clear();
			cal.set(yyyy, mm -1, dd);
			cal.add(Calendar.DAY_OF_MONTH, 1);
			int toY = cal.get(Calendar.YEAR);
			int toM = cal.get(Calendar.MONTH) + 1;
			int toD = cal.get(Calendar.DATE);

			// TIMESTAMPの書式
			String START = yyyy + "-" + mm  + "-" + dd  + " 06:00:00";
			String END   = toY  + "-" + toM + "-" + toD + " 04:00:00";
			String MIDNIGHT_START = yyyy + "-" + mm + "-" + dd  + " 22:30:00";
			String MIDNIGHT_END   = yyyy + "-" + mm + "-" + toD + " 04:00:00";

			// 時間内（出社）が通常残業時間帯かの確認
			sql.addSql(" SELECT IN FROM");
			sql.addSql(" (SELECT STP_TIME AS IN FROM KNT_STP");
			sql.addSql("   WHERE");
			sql.addSql("    STP_USR_SID = ?");
			sql.addSql("   AND");
			sql.addSql("    STP_TIME >= ?");
			sql.addSql("   AND");
			sql.addSql("    STP_TIME <= ?");
			sql.addSql("   AND");
			sql.addSql("    STP_IOM_ID = 3");
			sql.addSql(" )");
			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(usrSid);
			sql.addStrValue(MIDNIGHT_START);
			sql.addStrValue(MIDNIGHT_END);
			// SQL実行
			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();
			ret = rs.next();
			log__.info(sql.toLogString());
			log__.debug("selectDayMidnightHollowTime_ret=" + ret);

			if (ret){
				log__.debug("深夜残業時間帯");

				// 深夜残業時間帯の中抜け
				sql = new SqlBuffer();
				sql.addSql(" SELECT");
				sql.addSql("   TIMESTAMPDIFF(MINUTE, OUT, IN) AS TIME");
				sql.addSql(" FROM");
				sql.addSql("   (SELECT MIN(STP_TIME) AS OUT, MAX(STP_TIME) AS IN");
				sql.addSql("    FROM");
				sql.addSql("     KNT_STP");
				sql.addSql("    WHERE");
				sql.addSql("     STP_USR_SID = ?");
				sql.addSql("    AND");
				sql.addSql("     STP_TIME >= ?");
				sql.addSql("    AND");
				sql.addSql("     STP_TIME <= ?");
				sql.addSql("    AND");
				sql.addSql("     (STP_IOM_ID = 2 OR STP_IOM_ID = 3)");
				sql.addSql("   )");
				// SQL実行
				pstmt = con.prepareStatement(sql.toSqlString());
				sql.addIntValue(usrSid);
				sql.addStrValue(START);
				sql.addStrValue(END);
				sql.setParameter(pstmt);
				rs = pstmt.executeQuery();
				log__.info(sql.toLogString());
				while (rs.next()) {
					res = rs.getDouble("TIME");
					res = res / 60;
					break;
				}

			}

		} catch (SQLException e) {
			throw e;
		} finally {
			JDBCUtil.closeResultSet(rs);
			JDBCUtil.closeStatement(pstmt);
		}

		return res;

	}

}
