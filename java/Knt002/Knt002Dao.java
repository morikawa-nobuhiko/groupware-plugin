package jp.groupsession.v2.kintai.Knt002;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

public class Knt002Dao extends AbstractDao {

	/** Logging インスタンス */
	private static Log log__ = LogFactory.getLog(Knt002Dao.class);

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
	 * <p>パラメーターによるプロジェクト一覧の取得
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return ArrayList<Knt002Model>
	 */
	public ArrayList<Knt002Model> selectProjectList(Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Knt002Model> ret = new ArrayList<Knt002Model>();

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   *");
			sql.addSql(" from");
			sql.addSql("   KNT_PRJ");

			pstmt = con.prepareStatement(sql.toSqlString());
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Knt002Model model = new Knt002Model();
				model.setPrjSid(rs.getInt("PRJ_SID"));
				model.setPrjName(rs.getString("PRJ_NAME"));
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
	 * <p>間接作業プロジェクトの判定
	 * @param prjSid int
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return boolean
	 */
	public boolean BooIndirectProject(int prjSid, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean ret = false;

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   *");
			sql.addSql(" from");
			sql.addSql("   KNT_PRJ");
			sql.addSql(" where");
			sql.addSql("   PRJ_SID = ?");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(prjSid);
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				if (rs.getInt("PRJ_INDIRECT") == 0) {
					ret = false;
				} else {
					ret = true;
				}
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
	 * <p>パラメーターによるプロジェクト一覧の取得
	 * @param usrSid ユーザーID
	 * @param y 年
	 * @param m 月
	 * @param d 日
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return ArrayList<Knt002Model>
	 */
	public ArrayList<Knt002Model> selectProjectList(int usrSid, int y, int m, int d, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Knt002Model> ret = new ArrayList<Knt002Model>();

		// TIMESTAMPの書式
		String END = y + "-" + m + "-" + d + " " + "23:59:59";

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   T1.PRJ_SID,");
			sql.addSql("   T1.PRJ_NAME,");
			sql.addSql("   T1.PRJ_INDIRECT");
			sql.addSql(" from");
			sql.addSql("   KNT_PRJ AS T1");
			sql.addSql(" join");
			sql.addSql("   KNT_TNT AS T2");
			sql.addSql(" on");
			sql.addSql("   T1.PRJ_SID = T2.TNT_PRJ_SID");
			sql.addSql(" where");
			sql.addSql("   T2.TNT_USR_SID = ?");
			sql.addSql(" and");
			sql.addSql("   T1.PRJ_END >= ?");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(usrSid);
			sql.addStrValue(END);
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Knt002Model model = new Knt002Model();
				model.setPrjSid(rs.getInt("PRJ_SID"));
				String prjName = rs.getString("PRJ_NAME");
				if (rs.getInt("PRJ_INDIRECT") == 1) {
					prjName = prjName + "（間接作業）";
				}
				model.setPrjName(prjName);
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
	 * <p>パラメーターによるプロジェクト一覧の取得
	 * @param usrSid ユーザーID
	 * @param y 年
	 * @param m 月
	 * @param d 日
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return ArrayList<Knt002Model>
	 */
	public ArrayList<Knt002Model> selectFromToProjectList(int usrSid, int y, int m, int d, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Knt002Model> ret = new ArrayList<Knt002Model>();

		// TIMESTAMPの書式
		String START = y + "-" + m + "-" + d + " " + "00:00:00";
		String END   = y + "-" + m + "-" + d + " " + "23:59:59";

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   T1.PRJ_SID,");
			sql.addSql("   T1.PRJ_NAME,");
			sql.addSql("   T1.PRJ_INDIRECT");
			sql.addSql(" from");
			sql.addSql("   KNT_PRJ AS T1");
			sql.addSql(" join");
			sql.addSql("   KNT_TNT AS T2");
			sql.addSql(" on");
			sql.addSql("   T1.PRJ_SID = T2.TNT_PRJ_SID");
			sql.addSql(" where");
			sql.addSql("   T2.TNT_USR_SID = ?");
			sql.addSql(" and");
			sql.addSql("   T1.PRJ_START <= ?");
			sql.addSql(" and");
			sql.addSql("   T1.PRJ_END >= ?");
			sql.addSql(" ORDER BY T1.PRJ_INDIRECT, T1.PRJ_ID");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(usrSid);
			sql.addStrValue(START);
			sql.addStrValue(END);
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Knt002Model model = new Knt002Model();
				model.setPrjSid(rs.getInt("PRJ_SID"));
				String prjName = rs.getString("PRJ_NAME");
				if (rs.getInt("PRJ_INDIRECT") == 1) {
					prjName = prjName + "（間接作業）";
				}
				model.setPrjName(prjName);
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
	 * <p>パラメーターによる作業内容一覧の取得
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return ArrayList<Knt002Model>
	 */
	public ArrayList<Knt002Model> selectWorkList(Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Knt002Model> ret = new ArrayList<Knt002Model>();

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   *");
			sql.addSql(" from");
			sql.addSql("   KNT_WRK");

			pstmt = con.prepareStatement(sql.toSqlString());
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Knt002Model model = new Knt002Model();
				model.setWrkId(rs.getInt("WRK_ID"));
				model.setWrkName(rs.getString("WRK_NAME"));
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
	 * <p>パラメーターによる作業実績合計時間の取得
	 * @param usrSid  ユーザSID
	 * @param yyyy 年
	 * @param mm   月
	 * @param dd   日
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return 作業実績合計時間
	 */
	public double selectTotalTimeDay(int usrSid, int yyyy, int mm, int dd, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		double ret = 0;

		try {

			// TIMESTAMPの書式
			String STPD = yyyy + "-" + mm + "-" + dd + " " + "00:00:00";

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   sum(JSK_WRK_TIME)");
			sql.addSql(" from");
			sql.addSql("   KNT_JSK");
			sql.addSql(" where");
			sql.addSql("   JSK_USR_SID = ?");
			sql.addSql(" and");
			sql.addSql("   JSK_YMD = ?");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(usrSid);
			sql.addStrValue(STPD);
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				ret = rs.getDouble("sum(JSK_WRK_TIME)");
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
	 * <p>パラメーターによる作業実績の取得
	 * @param sid  ユーザSID
	 * @param yyyy 年
	 * @param mm   月
	 * @param dd   日
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return 作業実績合計時間
	 */
	public ArrayList<Knt002Model> selectWorks(int sid, int yyyy, int mm, int dd, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Knt002Model> ret = new ArrayList<Knt002Model>();

		try {

			// TIMESTAMPの書式
			String STPD = yyyy + "-" + mm + "-" + dd + " " + "00:00:00";

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   *");
			sql.addSql(" from");
			sql.addSql("   KNT_JSK");
			sql.addSql(" where");
			sql.addSql("   JSK_USR_SID = ?");
			sql.addSql(" and");
			sql.addSql("   JSK_YMD = ?");
			sql.addSql(" order by JSK_ID");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(sid);
			sql.addStrValue(STPD);
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				int ty = toTimestampYmd(rs.getTimestamp("JSK_YMD"), "yyyy");
				int tm = toTimestampYmd(rs.getTimestamp("JSK_YMD"), "MM");
				int td = toTimestampYmd(rs.getTimestamp("JSK_YMD"), "dd");
				Knt002Model model = new Knt002Model();
				model.setUsrSid(rs.getInt("JSK_USR_SID"));
				model.setPrjSid(rs.getInt("JSK_PRJ_SID"));
				model.setWrkId(rs.getInt("JSK_WRK_ID"));
				model.setWrkYear(ty);
				model.setWrkMonth(tm);
				model.setWrkDay(td);
				model.setWrkTime(rs.getDouble("JSK_WRK_TIME"));
				model.setJskRmk(rs.getString("JSK_RMK"));
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
	 * <p>パラメーターによる作業実績の取得
	 * @param sid  ユーザSID
	 * @param yyyy 年
	 * @param mm   月
	 * @param dd   日
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return 作業実績合計時間
	 */
	public Knt002Model selectWork(int sid, int yyyy, int mm, int dd, int jskId, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Knt002Model ret = new Knt002Model();

		try {

			// TIMESTAMPの書式
			String STPD = yyyy + "-" + mm + "-" + dd + " " + "00:00:00";

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   *");
			sql.addSql(" from");
			sql.addSql("   KNT_JSK");
			sql.addSql(" where");
			sql.addSql("   JSK_USR_SID = ?");
			sql.addSql(" and");
			sql.addSql("   JSK_YMD = ?");
			sql.addSql(" and");
			sql.addSql("   JSK_ID = ?");
			sql.addSql(" order by JSK_ID");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(sid);
			sql.addStrValue(STPD);
			sql.addIntValue(jskId);
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				int ty = toTimestampYmd(rs.getTimestamp("JSK_YMD"), "yyyy");
				int tm = toTimestampYmd(rs.getTimestamp("JSK_YMD"), "MM");
				int td = toTimestampYmd(rs.getTimestamp("JSK_YMD"), "dd");
				ret.setUsrSid(rs.getInt("JSK_USR_SID"));
				ret.setPrjSid(rs.getInt("JSK_PRJ_SID"));
				ret.setWrkId(rs.getInt("JSK_WRK_ID"));
				ret.setWrkYear(ty);
				ret.setWrkMonth(tm);
				ret.setWrkDay(td);
				ret.setWrkTime(rs.getDouble("JSK_WRK_TIME"));
				ret.setJskRmk(rs.getString("JSK_RMK"));
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
	 * <p>パラメーターによる実績の存在チェック
	 * @param param  Knt002Model
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return 作業実績合計時間
	 */
	public Boolean isNewPerformance(Knt002Model param, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Boolean ret = false;
		Boolean isNew = false;

		try {

			// TIMESTAMPの書式
			String STPD = param.getWrkYear() + "-" + param.getWrkMonth() + "-" + param.getWrkDay() + " " + "00:00:00";

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   *");
			sql.addSql(" from");
			sql.addSql("   KNT_JSK");
			sql.addSql(" where");
			sql.addSql("   JSK_USR_SID = ?");
			sql.addSql(" and");
			sql.addSql("   JSK_YMD = ?");
			sql.addSql(" and");
			sql.addSql("   JSK_ID = ?");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(param.getUsrSid());
			sql.addStrValue(STPD);
			sql.addIntValue(param.getJskId());
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
	* <br>[機  能] 実績情報を登録する
	* <br>[解  説]
	* <br>[備  考]
	* @param param Knt002Model
	* @param con   コネクション
	* @throws SQLException SQL実行時例外
	*/
	public void insertPerformance(Knt002Model param, Connection con) throws SQLException {

		// プロジェクトID、作業IDが未選択時は追加しない
//		if (param.getPrjSid()==-1 || param.getWrkId()==-1){
//			return;
//		}

		PreparedStatement pstmt = null;

		try {

			// TIMESTAMPの書式
			String STPD = param.getWrkYear() + "-" + param.getWrkMonth() + "-" + param.getWrkDay() + " " + "00:00:00";

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" insert ");
			sql.addSql(" into ");
			sql.addSql(" KNT_JSK(");
			sql.addSql("   JSK_ID,");
			sql.addSql("   JSK_USR_SID,");
			sql.addSql("   JSK_PRJ_SID,");
			sql.addSql("   JSK_WRK_ID,");
			sql.addSql("   JSK_YMD,");
			sql.addSql("   JSK_WRK_TIME,");
			sql.addSql("   JSK_RMK");
			sql.addSql(" )");
			sql.addSql(" values");
			sql.addSql(" (");
			sql.addSql("   ?,");
			sql.addSql("   ?,");
			sql.addSql("   ?,");
			sql.addSql("   ?,");
			sql.addSql("   ?,");
			sql.addSql("   ?,");
			sql.addSql("   ?");
			sql.addSql(" )");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(param.getJskId());
			sql.addIntValue(param.getUsrSid());
			sql.addIntValue(param.getPrjSid());
			sql.addIntValue(param.getWrkId());
			sql.addStrValue(STPD);
			sql.addDoubleValue(param.getWrkTime());
			sql.addStrValue(param.getJskRmk());
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
	* <br>[機  能] 実績情報を更新する
	* <br>[解  説]
	* <br>[備  考]
	* @param param Knt002Model
	* @param con   コネクション
	* @throws SQLException SQL実行時例外
	*/
	public void updatePerformance(Knt002Model param, Connection con) throws SQLException {

		PreparedStatement pstmt = null;

		try {

			// TIMESTAMPの書式
			String STPD = param.getWrkYear() + "-" + param.getWrkMonth() + "-" + param.getWrkDay() + " " + "00:00:00";

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" update");
			sql.addSql("   KNT_JSK");
			sql.addSql(" set ");
			sql.addSql("   JSK_PRJ_SID=?,");
			sql.addSql("   JSK_WRK_ID=?,");
			sql.addSql("   JSK_WRK_TIME=?,");
			sql.addSql("   JSK_RMK=?");
			sql.addSql(" where");
			sql.addSql("   JSK_USR_SID = ?");
			sql.addSql(" and");
			sql.addSql("   JSK_YMD = ?");
			sql.addSql(" and");
			sql.addSql("   JSK_ID = ?");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(param.getPrjSid());
			sql.addIntValue(param.getWrkId());
			sql.addDoubleValue(param.getWrkTime());
			sql.addStrValue(param.getJskRmk());
			sql.addIntValue(param.getUsrSid());
			sql.addStrValue(STPD);
			sql.addIntValue(param.getJskId());

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
	 * <p>指定日の打刻日時の取得
	 * @param sid   ユーザSID
	 * @param yyyy  年
	 * @param mm    月
	 * @param dd    日
	 * @param io    出退勤フラグ
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return 作業実績合計時間
	 */
	public Timestamp selectInOutTime(int usrSid, int yyyy, int mm, int dd, int io, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Timestamp ret = null;

		try {

			// 次の日付を取得する
			Calendar cal = Calendar.getInstance();
			cal.set(yyyy, mm - 1, dd);
			cal.add(Calendar.DAY_OF_MONTH, 1);
			int toY = cal.get(Calendar.YEAR);
			int toM = cal.get(Calendar.MONTH) + 1;
			int toD = cal.get(Calendar.DATE);

			// TIMESTAMPの書式
			String IN  = yyyy + "-" + mm  + "-" + dd  + " 06:00:00";
			String OUT = toY  + "-" + toM + "-" + toD + " 04:00:00";
			log__.debug("IN=" + IN);
			log__.debug("OUT=" + OUT);

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" SELECT");
			sql.addSql("   STP_TIME");
			sql.addSql(" FROM");
			sql.addSql("   KNT_STP");
			sql.addSql(" WHERE");
			sql.addSql("   STP_USR_SID = ?");
			sql.addSql("   AND");
			sql.addSql("   STP_TIME >= ?");
			sql.addSql("   AND");
			sql.addSql("   STP_TIME <= ?");
			sql.addSql("   AND");
			sql.addSql("   STP_IOM_ID = ?");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(usrSid);
			sql.addStrValue(IN);
			sql.addStrValue(OUT);
			sql.addIntValue(io);
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				ret = rs.getTimestamp("STP_TIME");
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
	 * <p>パラメーターによる有給合計時間の取得
	 * @param sid ユーザSID
	 * @param yyyy  西暦
	 * @param mm    月
	 * @param mm    日
	 * @param uq    有給ID
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return 作業実績合計時間
	 */
	public Double selectDayPayTotalTime(int sid, int yyyy, int mm, int dd, int uq, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Double ret = 0.0;

		try {

			// TIMESTAMPの書式
			String STPD = yyyy + "-" + mm + "-" + dd + " " + "00:00:00";

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   sum(JSK_WRK_TIME) as JSK_WRK_TIME");
			sql.addSql(" from");
			sql.addSql("   KNT_JSK");
			sql.addSql(" where");
			sql.addSql("   JSK_USR_SID = ?");
			sql.addSql(" and");
			sql.addSql("   JSK_YMD = ?");
			sql.addSql(" and");
			sql.addSql("   JSK_WRK_ID = ?");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(sid);
			sql.addStrValue(STPD);
			sql.addIntValue(uq);
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				ret = rs.getDouble("JSK_WRK_TIME");
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
	 * <p>パラメーターによる申請時間合計（36協定）時間の取得
	 * @param sid ユーザSID
	 * @param yyyy  西暦
	 * @param mm    月
	 * @param dd    日
	 * @param mv    移動ID
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return 作業実績合計時間
	 * @remark 移動時間は除外
	 */
	public Double selectDay36TotalTime(int sid, int yyyy, int mm, int dd, int mv, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Double ret = 0.0;

		try {

			Calendar cal=Calendar.getInstance();
			cal.clear();
			cal.set(Calendar.YEAR, yyyy);
			cal.set(Calendar.MONTH, mm - 1);
			int last = cal.getActualMaximum(Calendar.DATE);

			// TIMESTAMPの書式
			String STPD = yyyy + "-" + mm + "-" + dd + " " + "00:00:00";

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   sum(JSK_WRK_TIME) as JSK_WRK_TIME");
			sql.addSql(" from");
			sql.addSql("   KNT_JSK");
			sql.addSql(" where");
			sql.addSql("   JSK_USR_SID = ?");
			sql.addSql(" and");
			sql.addSql("   JSK_YMD = ?");
			sql.addSql(" and");
			sql.addSql("   JSK_WRK_ID != ?");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(sid);
			sql.addStrValue(STPD);
			sql.addIntValue(mv);
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				ret = rs.getDouble("JSK_WRK_TIME") - 8;
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
	 * <p>パラメーターによるユーザー氏名の取得
	 * @param usrSid int
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return ArrayList<Knt002Model>
	 */
	public String selectUserName(int usrSid, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String ret = "";

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   USI_SEI,");
			sql.addSql("   USI_MEI");
			sql.addSql(" from");
			sql.addSql("   CMN_USRM_INF");
			sql.addSql(" where");
			sql.addSql("   USR_SID = ?");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(usrSid);
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				ret = rs.getString("USI_SEI") + " " + rs.getString("USI_MEI");
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
	 * <p>承認済かの取得
	 * @param usrSid ユーザーID
	 * @param y 年
	 * @param m 月
	 * @param d 日
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return 0:未承認 1:承認済
	 */
	public boolean selectShouninUser(int usrSid, int y, int m, int d, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean ret = false;

		try {

			// TIMESTAMPの書式
			String STPD = y + "-" + m + "-" + d + " " + "00:00:00";

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   *");
			sql.addSql(" from");
			sql.addSql("   KNT_SYN");
			sql.addSql(" where");
			sql.addSql("   SYN_USR_SID = ?");
			sql.addSql(" and");
			sql.addSql("   SYN_YMD = ?");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(usrSid);
			sql.addStrValue(STPD);
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				ret = true;
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
	 * <p>総務チェック済かの取得
	 * @param usrSid ユーザーID
	 * @param y 年
	 * @param m 月
	 * @param d 日
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return 0:未承認 1:承認済
	 */
	public boolean selectSoumuCheckUser(int usrSid, int y, int m, int d, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean ret = false;

		try {

			// TIMESTAMPの書式
			String STPD = y + "-" + m + "-" + d + " " + "00:00:00";

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

			while (rs.next()) {
				if (rs.getInt("SMU_CHK") == 0) {
					// 未チェック
					ret = false;
				} else {
					// チェック済
					ret = true;
				}
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

}
