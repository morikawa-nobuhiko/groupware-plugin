package jp.groupsession.v2.kintai.Knt001;

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

public class Knt001Dao extends AbstractDao {

	/** Logging インスタンス */
	private static Log log__ = LogFactory.getLog(Knt001Dao.class);

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
	 * <p>パラメーターによる作業実績合計時間の取得
	 * @param sid ユーザSID
	 * @param yyyy 年
	 * @param mm   月
	 * @param dd   日
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return 作業実績合計時間
	 */
	public Knt001Model selectTotalTime(int sid, int yyyy, int mm, int dd, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Knt001Model ret = null;

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
			sql.addIntValue(sid);
			sql.addStrValue(STPD);
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				ret = new Knt001Model();
				ret.setWrkTime(rs.getDouble("sum(JSK_WRK_TIME)"));
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
	 * @param sid ユーザSID
	 * @param yyyy  西暦
	 * @param mm    月
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return 作業実績合計時間
	 */
	public ArrayList<Knt001Model> selectDayTotalTime(int sid, int yyyy, int mm, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Knt001Model> ret = new ArrayList<Knt001Model>();

		try {

			Calendar cal=Calendar.getInstance();
			cal.clear();
			cal.set(Calendar.YEAR, yyyy);
			cal.set(Calendar.MONTH, mm - 1);
			int last = cal.getActualMaximum(Calendar.DATE);

			log__.debug("selectDayTotalTime-yyyy=" + yyyy);
			log__.debug("selectDayTotalTime-mm=" + mm);
			log__.debug("selectDayTotalTime-last=" + last);

			// TIMESTAMPの書式
			String START = yyyy + "-" + mm + "-" + 01 + " " + "00:00:00";
			String END = yyyy + "-" + mm + "-" + last + " " + "23:59:59";

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
			sql.addSql(" group by JSK_USR_SID, JSK_YMD");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(sid);
			sql.addStrValue(START);
			sql.addStrValue(END);
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				int ty = toTimestampYmd(rs.getTimestamp("JSK_YMD"), "yyyy");
				int tm = toTimestampYmd(rs.getTimestamp("JSK_YMD"), "MM");
				int td = toTimestampYmd(rs.getTimestamp("JSK_YMD"), "dd");
				Knt001Model model = new Knt001Model();
				model.setUsrSid(rs.getInt("JSK_USR_SID"));
				model.setWrkYear(ty);
				model.setWrkMonth(tm);
				model.setWrkDay(td);
				model.setWrkTime(rs.getDouble("sum(JSK_WRK_TIME)"));
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
	 * @param sid ユーザSID
	 * @param yyyy  西暦
	 * @param mm    月
	 * @param dd    日
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return 作業実績合計時間
	 */
	public ArrayList<Knt001Model> selectDayTotalTime(int sid, int yyyy, int mm, int dd, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Knt001Model> ret = new ArrayList<Knt001Model>();

		try {

			// TIMESTAMPの書式
			String STPD = yyyy + "-" + mm + "-" + dd + " " + "00:00:00";

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
			sql.addSql("   and");
			sql.addSql("   JSK_YMD = ?");

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
				Knt001Model model = new Knt001Model();
				model.setUsrSid(rs.getInt("JSK_USR_SID"));
				model.setWrkYear(ty);
				model.setWrkMonth(tm);
				model.setWrkDay(td);
				model.setWrkTime(rs.getDouble("sum(JSK_WRK_TIME)"));
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
	 * <p>指定日の勤務時間の取得
	 * @param sid   ユーザSID
	 * @param yyyy  年
	 * @param mm    月
	 * @param dd    日
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return 作業実績合計時間
	 */
	public Double selectDayWrkTime(int usrSid, int yyyy, int mm, int dd, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Double ret = 0.0;

		try {

			// TIMESTAMPの書式
			String IN = yyyy + "-" + mm + "-" + dd + "-" + "06:00:00";
			String OUT = yyyy + "-" + mm + "-" + dd + "-" + "23:59:59";

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" SELECT");
			sql.addSql("   TIMESTAMPDIFF(MINUTE, IN, OUT) - 60 AS TOTAL");
			sql.addSql(" FROM");
			sql.addSql("   (SELECT MIN(STP_TIME) as IN, MAX(STP_TIME) as OUT FROM KNT_STP");
			sql.addSql(" WHERE");
			sql.addSql("   STP_USR_SID = ?");
			sql.addSql("   AND");
			sql.addSql("   STP_TIME >= ?");
			sql.addSql("   AND ");
			sql.addSql("   STP_TIME <= ?");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(usrSid);
			sql.addStrValue(IN);
			sql.addStrValue(OUT);
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				ret = rs.getDouble("TOTAL");
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
	 * @param uq    有給ID
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return 作業実績合計時間
	 */
	public Double selectMonthPayTotalTime(int sid, int yyyy, int mm, int uq, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Double ret = 0.0;

		try {

			Calendar cal=Calendar.getInstance();
			cal.clear();
			cal.set(Calendar.YEAR, yyyy);
			cal.set(Calendar.MONTH, mm - 1);
			int last = cal.getActualMaximum(Calendar.DATE);

			log__.debug("selectMonthPayTotalTime-yyyy=" + yyyy);
			log__.debug("selectMonthPayTotalTime-mm=" + mm);

			// TIMESTAMPの書式
			String START = yyyy + "-" + mm + "-" + 01 + " " + "00:00:00";
			String END = yyyy + "-" + mm + "-" + last + " " + "23:59:59";

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   sum(JSK_WRK_TIME) as JSK_WRK_TIME");
			sql.addSql(" from");
			sql.addSql("   KNT_JSK");
			sql.addSql(" where");
			sql.addSql("   JSK_USR_SID = ?");
			sql.addSql(" and");
			sql.addSql("   JSK_YMD >= ?");
			sql.addSql(" and");
			sql.addSql("   JSK_YMD <= ?");
			sql.addSql(" and");
			sql.addSql("   JSK_WRK_ID = ?");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(sid);
			sql.addStrValue(START);
			sql.addStrValue(END);
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
	 * <p>ユーザーが承認グループに所属するかを返す
	 * @param grpId   グループSID
	 * @param usrSid   ユーザSID
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return 作業実績合計時間
	 */
	public Boolean containsManageGroup(int grpId, int usrSid, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Boolean ret = false;

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" SELECT");
			sql.addSql("   *");
			sql.addSql(" FROM");
			sql.addSql("   CMN_BELONGM");
			sql.addSql(" WHERE");
			sql.addSql("   GRP_SID = ?");
			sql.addSql("   AND");
			sql.addSql("   USR_SID = ?");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(grpId);
			sql.addIntValue(usrSid);
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

}
