package jp.groupsession.v2.kintai.Knt900;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.Scanner;
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

public class Knt900Dao extends AbstractDao {

	/** Logging インスタンス */
	private static Log log__ = LogFactory.getLog(Knt900Dao.class);

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
	 * <p>プロジェクトの作業合計時間取得（有給除外）
	 * @param uq int
	 * @param con    DB Connection
	 * @throws SQLException SQL実行例外
	 * @return ArrayList<Knt900Model>
	 */
	public ArrayList<Knt900Model> selectProjectTotalTime(int uq, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Knt900Model> ret = new ArrayList<Knt900Model>();

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" SELECT");
			sql.addSql("   T1.PRJ_SID,");
			sql.addSql("   T1.PRJ_ID,");
			sql.addSql("   T1.PRJ_NAME,");
			sql.addSql("   T1.PRJ_START,");
			sql.addSql("   T1.PRJ_END,");
			sql.addSql("   SUM(T2.JSK_WRK_TIME) AS TIME");
			sql.addSql(" FROM");
			sql.addSql("   KNT_PRJ AS T1");
			sql.addSql(" JOIN");
			sql.addSql("   KNT_JSK AS T2");
			sql.addSql(" ON");
			sql.addSql("   T1.PRJ_SID = T2.JSK_PRJ_SID");
			sql.addSql(" WHERE");
			sql.addSql("   T2.JSK_WRK_ID != ?");
			sql.addSql(" GROUP BY T1.PRJ_SID");
			sql.addSql(" ORDER BY T1.PRJ_INDIRECT, T1.PRJ_ID");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(uq);
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				int tsy = toTimestampYmd(rs.getTimestamp("PRJ_START"), "yyyy");
				int tsm = toTimestampYmd(rs.getTimestamp("PRJ_START"), "MM");
				int tsd = toTimestampYmd(rs.getTimestamp("PRJ_START"), "dd");
				int tey = toTimestampYmd(rs.getTimestamp("PRJ_END"), "yyyy");
				int tem = toTimestampYmd(rs.getTimestamp("PRJ_END"), "MM");
				int ted = toTimestampYmd(rs.getTimestamp("PRJ_END"), "dd");
				Knt900Model model = new Knt900Model();
				model.setPrjSid(rs.getInt("PRJ_SID"));
				model.setPrjId(rs.getString("PRJ_ID"));
				model.setPrjName(rs.getString("PRJ_NAME"));
				model.setStartYear(tsy);
				model.setStartMonth(tsm);
				model.setStartDay(tsd);
				model.setEndYear(tey);
				model.setEndMonth(tem);
				model.setEndDay(ted);
				model.setPrjTime(rs.getDouble("TIME"));
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
	 * <p>プロジェクトの作業合計時間取得（有給除外）
	 * @param prjId int
	 * @param uq int
	 * @param con    DB Connection
	 * @throws SQLException SQL実行例外
	 * @return ArrayList<Knt900Model>
	 */
	public ArrayList<Knt900Model> selectByIdProjectTotalTime(String prjId, int uq, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Knt900Model> ret = new ArrayList<Knt900Model>();

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" SELECT");
			sql.addSql("   T1.PRJ_SID,");
			sql.addSql("   T1.PRJ_ID,");
			sql.addSql("   T1.PRJ_NAME,");
			sql.addSql("   T1.PRJ_START,");
			sql.addSql("   T1.PRJ_END,");
			sql.addSql("   SUM(T2.JSK_WRK_TIME) AS TIME");
			sql.addSql(" FROM");
			sql.addSql("   KNT_PRJ AS T1");
			sql.addSql(" JOIN");
			sql.addSql("   KNT_JSK AS T2");
			sql.addSql(" WHERE");
			sql.addSql("   T1.PRJ_ID = ?");
			sql.addSql(" AND");
			sql.addSql("   T2.JSK_WRK_ID != ?");
			sql.addSql(" ORDER BY T1.PRJ_INDIRECT, T1.PRJ_ID");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addStrValue(prjId);
			sql.addIntValue(uq);
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				int tsy = toTimestampYmd(rs.getTimestamp("PRJ_START"), "yyyy");
				int tsm = toTimestampYmd(rs.getTimestamp("PRJ_START"), "MM");
				int tsd = toTimestampYmd(rs.getTimestamp("PRJ_START"), "dd");
				int tey = toTimestampYmd(rs.getTimestamp("PRJ_END"), "yyyy");
				int tem = toTimestampYmd(rs.getTimestamp("PRJ_END"), "MM");
				int ted = toTimestampYmd(rs.getTimestamp("PRJ_END"), "dd");
				Knt900Model model = new Knt900Model();
				model.setPrjSid(rs.getInt("PRJ_SID"));
				model.setPrjId(rs.getString("PRJ_ID"));
				model.setPrjName(rs.getString("PRJ_NAME"));
				model.setStartYear(tsy);
				model.setStartMonth(tsm);
				model.setStartDay(tsd);
				model.setEndYear(tey);
				model.setEndMonth(tem);
				model.setEndDay(ted);
				model.setPrjTime(rs.getDouble("TIME"));
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
	 * <p>プロジェクトの作業合計時間取得（有給除外）
	 * @param prjId int
	 * @param uq int
	 * @param con    DB Connection
	 * @throws SQLException SQL実行例外
	 * @return ArrayList<Knt900Model>
	 */
	public ArrayList<Knt900Model> selectByNameProjectTotalTime(String prjName, int uq, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Knt900Model> ret = new ArrayList<Knt900Model>();

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" SELECT");
			sql.addSql("   T1.PRJ_SID,");
			sql.addSql("   T1.PRJ_ID,");
			sql.addSql("   T1.PRJ_NAME,");
			sql.addSql("   T1.PRJ_START,");
			sql.addSql("   T1.PRJ_END,");
			sql.addSql("   SUM(T2.JSK_WRK_TIME) AS TIME");
			sql.addSql(" FROM");
			sql.addSql("   KNT_PRJ AS T1");
			sql.addSql(" JOIN");
			sql.addSql("   KNT_JSK AS T2");
			sql.addSql(" WHERE");
			sql.addSql("   T1.PRJ_NAME LIKE '%" + prjName + "%'");
			sql.addSql(" AND");
			sql.addSql("   T2.JSK_WRK_ID != ?");
			sql.addSql(" GROUP BY T1.PRJ_ID");
			sql.addSql(" ORDER BY T1.PRJ_INDIRECT, T1.PRJ_ID");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(uq);
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				int tsy = toTimestampYmd(rs.getTimestamp("PRJ_START"), "yyyy");
				int tsm = toTimestampYmd(rs.getTimestamp("PRJ_START"), "MM");
				int tsd = toTimestampYmd(rs.getTimestamp("PRJ_START"), "dd");
				int tey = toTimestampYmd(rs.getTimestamp("PRJ_END"), "yyyy");
				int tem = toTimestampYmd(rs.getTimestamp("PRJ_END"), "MM");
				int ted = toTimestampYmd(rs.getTimestamp("PRJ_END"), "dd");
				Knt900Model model = new Knt900Model();
				model.setPrjSid(rs.getInt("PRJ_SID"));
				model.setPrjId(rs.getString("PRJ_ID"));
				model.setPrjName(rs.getString("PRJ_NAME"));
				model.setStartYear(tsy);
				model.setStartMonth(tsm);
				model.setStartDay(tsd);
				model.setEndYear(tey);
				model.setEndMonth(tem);
				model.setEndDay(ted);
				model.setPrjTime(rs.getDouble("TIME"));
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
	 * <p>プロジェクトの作業合計時間取得（有給除外）
	 * @param prjId int
	 * @param uq int
	 * @param con    DB Connection
	 * @throws SQLException SQL実行例外
	 * @return ArrayList<Knt900Model>
	 */
	public ArrayList<Knt900Model> selectByStartDateProjectList(int sy, int sm, int sd, int uq, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Knt900Model> ret = new ArrayList<Knt900Model>();

		try {

			// TIMESTAMPの書式
			String START = sy + "-" + sm + "-" + sd + " " + "00:00:00";

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" SELECT");
			sql.addSql("   T1.PRJ_SID,");
			sql.addSql("   T1.PRJ_ID,");
			sql.addSql("   T1.PRJ_NAME,");
			sql.addSql("   T1.PRJ_START,");
			sql.addSql("   T1.PRJ_END,");
			sql.addSql("   SUM(T2.JSK_WRK_TIME) AS TIME");
			sql.addSql(" FROM");
			sql.addSql("   KNT_PRJ AS T1");
			sql.addSql(" JOIN");
			sql.addSql("   KNT_JSK AS T2");
			sql.addSql(" WHERE");
			sql.addSql("   T1.PRJ_START >= ?");
			sql.addSql(" AND");
			sql.addSql("   T2.JSK_WRK_ID != ?");
			sql.addSql(" GROUP BY T1.PRJ_SID");
			sql.addSql(" ORDER BY T1.PRJ_INDIRECT, T1.PRJ_ID");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addStrValue(START);
			sql.addIntValue(uq);
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				int tsy = toTimestampYmd(rs.getTimestamp("PRJ_START"), "yyyy");
				int tsm = toTimestampYmd(rs.getTimestamp("PRJ_START"), "MM");
				int tsd = toTimestampYmd(rs.getTimestamp("PRJ_START"), "dd");
				int tey = toTimestampYmd(rs.getTimestamp("PRJ_END"), "yyyy");
				int tem = toTimestampYmd(rs.getTimestamp("PRJ_END"), "MM");
				int ted = toTimestampYmd(rs.getTimestamp("PRJ_END"), "dd");
				Knt900Model model = new Knt900Model();
				model.setPrjSid(rs.getInt("PRJ_SID"));
				model.setPrjId(rs.getString("PRJ_ID"));
				model.setPrjName(rs.getString("PRJ_NAME"));
				model.setStartYear(tsy);
				model.setStartMonth(tsm);
				model.setStartDay(tsd);
				model.setEndYear(tey);
				model.setEndMonth(tem);
				model.setEndDay(ted);
				model.setPrjTime(rs.getDouble("TIME"));
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
	 * <p>プロジェクトの作業合計時間取得（有給除外）
	 * @param prjId int
	 * @param uq int
	 * @param con    DB Connection
	 * @throws SQLException SQL実行例外
	 * @return ArrayList<Knt900Model>
	 */
	public ArrayList<Knt900Model> selectByEndDateProjectList(int ey, int em, int ed, int uq, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Knt900Model> ret = new ArrayList<Knt900Model>();

		try {

			// TIMESTAMPの書式
			String END = ey + "-" + em + "-" + ed + " " + "23:59:59";

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" SELECT");
			sql.addSql("   T1.PRJ_SID,");
			sql.addSql("   T1.PRJ_ID,");
			sql.addSql("   T1.PRJ_NAME,");
			sql.addSql("   T1.PRJ_START,");
			sql.addSql("   T1.PRJ_END,");
			sql.addSql("   SUM(T2.JSK_WRK_TIME) AS TIME");
			sql.addSql(" FROM");
			sql.addSql("   KNT_PRJ AS T1");
			sql.addSql(" JOIN");
			sql.addSql("   KNT_JSK AS T2");
			sql.addSql(" WHERE");
			sql.addSql("   T1.PRJ_END <= ?");
			sql.addSql(" AND");
			sql.addSql("   T2.JSK_WRK_ID != ?");
			sql.addSql(" GROUP BY T1.PRJ_SID");
			sql.addSql(" ORDER BY T1.PRJ_INDIRECT, T1.PRJ_ID");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addStrValue(END);
			sql.addIntValue(uq);
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				int tsy = toTimestampYmd(rs.getTimestamp("PRJ_START"), "yyyy");
				int tsm = toTimestampYmd(rs.getTimestamp("PRJ_START"), "MM");
				int tsd = toTimestampYmd(rs.getTimestamp("PRJ_START"), "dd");
				int tey = toTimestampYmd(rs.getTimestamp("PRJ_END"), "yyyy");
				int tem = toTimestampYmd(rs.getTimestamp("PRJ_END"), "MM");
				int ted = toTimestampYmd(rs.getTimestamp("PRJ_END"), "dd");
				Knt900Model model = new Knt900Model();
				model.setPrjSid(rs.getInt("PRJ_SID"));
				model.setPrjId(rs.getString("PRJ_ID"));
				model.setPrjName(rs.getString("PRJ_NAME"));
				model.setStartYear(tsy);
				model.setStartMonth(tsm);
				model.setStartDay(tsd);
				model.setEndYear(tey);
				model.setEndMonth(tem);
				model.setEndDay(ted);
				model.setPrjTime(rs.getDouble("TIME"));
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
	 * <p>プロジェクトの作業合計時間取得（有給除外）
	 * @param sy int
	 * @param sm int
	 * @param sd int
	 * @param sy int
	 * @param sm int
	 * @param sd int
	 * @param uq int
	 * @param con    DB Connection
	 * @throws SQLException SQL実行例外
	 * @return ArrayList<Knt900Model>
	 */
	public ArrayList<Knt900Model> selectByStartEndProjectList(int sy, int sm, int sd,int ey, int em, int ed, int uq, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Knt900Model> ret = new ArrayList<Knt900Model>();

		try {

			// TIMESTAMPの書式
			String START = sy + "-" + sm + "-" + sd + " " + "00:00:00";
			String END = ey + "-" + em + "-" + ed + " " + "23:59:59";

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" SELECT");
			sql.addSql("   T1.PRJ_SID,");
			sql.addSql("   T1.PRJ_ID,");
			sql.addSql("   T1.PRJ_NAME,");
			sql.addSql("   T1.PRJ_START,");
			sql.addSql("   T1.PRJ_END,");
			sql.addSql("   SUM(T2.JSK_WRK_TIME) AS TIME");
			sql.addSql(" FROM");
			sql.addSql("   KNT_PRJ AS T1");
			sql.addSql(" JOIN");
			sql.addSql("   KNT_JSK AS T2");
			sql.addSql(" ON");
			sql.addSql("   T1.PRJ_SID = T2.JSK_PRJ_SID");
			sql.addSql(" WHERE");
			sql.addSql(" T1.PRJ_START >= ?");
			sql.addSql(" AND");
			sql.addSql(" T1.PRJ_END <= ?");
			sql.addSql(" AND");
			sql.addSql("   T2.JSK_WRK_ID != ?");
			sql.addSql(" GROUP BY T1.PRJ_SID");
			sql.addSql(" ORDER BY T1.PRJ_INDIRECT, T1.PRJ_ID");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addStrValue(START);
			sql.addStrValue(END);
			sql.addIntValue(uq);
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				int tsy = toTimestampYmd(rs.getTimestamp("PRJ_START"), "yyyy");
				int tsm = toTimestampYmd(rs.getTimestamp("PRJ_START"), "MM");
				int tsd = toTimestampYmd(rs.getTimestamp("PRJ_START"), "dd");
				int tey = toTimestampYmd(rs.getTimestamp("PRJ_END"), "yyyy");
				int tem = toTimestampYmd(rs.getTimestamp("PRJ_END"), "MM");
				int ted = toTimestampYmd(rs.getTimestamp("PRJ_END"), "dd");
				Knt900Model model = new Knt900Model();
				model.setPrjSid(rs.getInt("PRJ_SID"));
				model.setPrjId(rs.getString("PRJ_ID"));
				model.setPrjName(rs.getString("PRJ_NAME"));
				model.setStartYear(tsy);
				model.setStartMonth(tsm);
				model.setStartDay(tsd);
				model.setEndYear(tey);
				model.setEndMonth(tem);
				model.setEndDay(ted);
				model.setPrjTime(rs.getDouble("TIME"));
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
	 * <p>社員毎（プロジェクト別）の作業合計時間取得（有給除外）
	 * @param sy int
	 * @param sm int
	 * @param sd int
	 * @param sy int
	 * @param sm int
	 * @param sd int
	 * @param uq int
	 * @param con    DB Connection
	 * @throws SQLException SQL実行例外
	 * @return ArrayList<Knt900Model>
	 */
	public ArrayList<Knt900Model> selectProjectUserList(int sy, int sm, int sd,int ey, int em, int ed, int uq, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Knt900Model> ret = new ArrayList<Knt900Model>();

		try {

			// TIMESTAMPの書式
			String START = sy + "-" + sm + "-" + sd + " " + "00:00:00";
			String END = ey + "-" + em + "-" + ed + " " + "23:59:59";

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" SELECT");
			sql.addSql("   T1.PRJ_SID,");
			sql.addSql("   T1.PRJ_ID,");
			sql.addSql("   T1.PRJ_NAME,");
			sql.addSql("   T2.JSK_USR_SID,");
			sql.addSql("   SUM(T2.JSK_WRK_TIME) AS TIME");
			sql.addSql(" FROM");
			sql.addSql("   KNT_PRJ AS T1");
			sql.addSql(" JOIN");
			sql.addSql("   KNT_JSK AS T2");
			sql.addSql(" ON");
			sql.addSql("   T1.PRJ_SID = T2.JSK_PRJ_SID");
			sql.addSql(" WHERE");
			sql.addSql("   T2.JSK_YMD >= ?");
			sql.addSql(" AND");
			sql.addSql("   T2.JSK_YMD <= ?");
			sql.addSql(" AND");
			sql.addSql("   T2.JSK_WRK_ID != ?");
			sql.addSql(" GROUP BY T1.PRJ_SID, T1.PRJ_ID, T2.JSK_USR_SID");
			sql.addSql(" ORDER BY T1.PRJ_ID, T2.JSK_USR_SID");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addStrValue(START);
			sql.addStrValue(END);
			sql.addIntValue(uq);
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Knt900Model model = new Knt900Model();
				model.setPrjSid(rs.getInt("PRJ_SID"));
				model.setPrjId(rs.getString("PRJ_ID"));
				model.setPrjName(rs.getString("PRJ_NAME"));
				model.setUsrSid(rs.getInt("JSK_USR_SID"));
				model.setUsrYear(sy);
				model.setUsrMonth(sm);
				model.setPrjTime(rs.getDouble("TIME"));
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
	 * <p>（没）プロジェクトに所属するユーザー毎の作業合計時間取得（有給除外）
	 * @param sy int
	 * @param sm int
	 * @param sd int
	 * @param sy int
	 * @param sm int
	 * @param sd int
	 * @param uq int
	 * @param con    DB Connection
	 * @throws SQLException SQL実行例外
	 * @return ArrayList<Knt900Model>
	 */
	public ArrayList<Knt900Model> selectProjectUserList(Knt900Model prj, int uq, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Knt900Model> ret = new ArrayList<Knt900Model>();

		try {

			int prjSid = prj.getPrjSid();

			// TIMESTAMPの書式
			String START = prj.getStartYear() + "-" + prj.getStartMonth() + "-" + prj.getStartDay() + " " + "00:00:00";
			String END = prj.getEndYear() + "-" + prj.getEndMonth() + "-" + prj.getEndDay() + " " + "23:59:59";

			log__.debug("START=" + START);
			log__.debug("END=" +END);

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" SELECT");
			sql.addSql("   JSK_USR_SID,");
			sql.addSql("   JSK_YMD,");
			sql.addSql("   SUM(JSK_WRK_TIME) AS TIME");
			sql.addSql(" FROM");
			sql.addSql("   KNT_JSK");
			sql.addSql(" WHERE");
			sql.addSql("   JSK_PRJ_SID = ?");
			sql.addSql(" AND");
			sql.addSql("   JSK_YMD >= ?");
			sql.addSql(" AND");
			sql.addSql("   JSK_YMD <= ?");
			sql.addSql(" AND");
			sql.addSql("   JSK_WRK_ID != ?");
			sql.addSql(" GROUP BY JSK_USR_SID,JSK_YMD");
			sql.addSql(" ORDER BY JSK_PRJ_SID, JSK_USR_SID, JSK_YMD");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(prjSid);
			sql.addStrValue(START);
			sql.addStrValue(END);
			sql.addIntValue(uq);
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			// key:SID,ID,NAME,USRSID,Y,M val:作業時間
			HashMap<String, Double> map = new HashMap<String, Double>();
			while (rs.next()) {
				int ty = toTimestampYmd(rs.getTimestamp("JSK_YMD"), "yyyy");
				int tm = toTimestampYmd(rs.getTimestamp("JSK_YMD"), "MM");
				int td = toTimestampYmd(rs.getTimestamp("JSK_YMD"), "dd");

				// 時間をセット
				String key = prj.getPrjSid() + "," + prj.getPrjId() + "," + prj.getPrjName() + "," + rs.getInt("JSK_USR_SID") + "," + ty + "," + tm;
				log__.debug("key=" + key);
				if (map.containsKey(key)){
					double time = map.get(key);
					time += rs.getDouble("TIME");
					map.replace(key, time);
				} else {
					map.put(key, rs.getDouble("TIME"));
				}

			}

			for (String key : map.keySet()) {

				double val = map.get(key);
				String[] keys = key.split(",", -1);
				String sPrjSid = keys[0];
				String sPrjId = keys[1];
				String sPrjName = keys[2];
				String sUsrSid = keys[3];
				String sY = keys[4];
				String sM = keys[5];

				Knt900Model model = new Knt900Model();
				model.setPrjSid(Integer.parseInt(sPrjSid));
				model.setPrjId(sPrjId);
				model.setPrjName(sPrjName);
				model.setPrjTime(val);
				model.setUsrSid(Integer.parseInt(sUsrSid));
				model.setUsrYear(Integer.parseInt(sY));
				model.setUsrMonth(Integer.parseInt(sM));
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
	 * 没
	 * <p>プロジェクトに所属するユーザー毎の作業合計時間取得（有給除外）
	 * @param sy int
	 * @param sm int
	 * @param sd int
	 * @param sy int
	 * @param sm int
	 * @param sd int
	 * @param uq int
	 * @param con    DB Connection
	 * @throws SQLException SQL実行例外
	 * @return ArrayList<Knt900Model>
	 */
	public ArrayList<Knt900Model> selectProjectUserList(int prjSid, int uq, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Knt900Model> ret = new ArrayList<Knt900Model>();

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" SELECT");
			sql.addSql("   T1.PRJ_SID,");
			sql.addSql("   T1.PRJ_ID,");
			sql.addSql("   T1.PRJ_NAME,");
			sql.addSql("   T2.JSK_USR_SID,");
			sql.addSql("   T2.JSK_YMD,");
			sql.addSql("   SUM(T2.JSK_WRK_TIME) AS TIME");
			sql.addSql(" FROM");
			sql.addSql("   KNT_PRJ AS T1");
			sql.addSql(" JOIN");
			sql.addSql("   KNT_JSK AS T2");
			sql.addSql(" ON");
			sql.addSql("   T1.PRJ_SID = T2.JSK_PRJ_SID");
			sql.addSql(" WHERE");
			sql.addSql("   T1.PRJ_SID = ?");
			sql.addSql(" AND");
			sql.addSql("   T2.JSK_WRK_ID != ?");
			sql.addSql(" GROUP BY T2.JSK_USR_SID, T2.JSK_YMD");
			sql.addSql(" ORDER BY T1.PRJ_INDIRECT, T1.PRJ_ID");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(prjSid);
			sql.addIntValue(uq);
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				int ty = toTimestampYmd(rs.getTimestamp("JSK_YMD"), "yyyy");
				int tm = toTimestampYmd(rs.getTimestamp("JSK_YMD"), "MM");
				int td = toTimestampYmd(rs.getTimestamp("JSK_YMD"), "dd");
				Knt900Model model = new Knt900Model();
				model.setPrjSid(rs.getInt("PRJ_SID"));
				model.setPrjId(rs.getString("PRJ_ID"));
				model.setPrjName(rs.getString("PRJ_NAME"));
				model.setPrjTime(rs.getDouble("TIME"));
				model.setUsrSid(rs.getInt("JSK_USR_SID"));
				model.setUsrYear(ty);
				model.setUsrMonth(tm);
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

}
