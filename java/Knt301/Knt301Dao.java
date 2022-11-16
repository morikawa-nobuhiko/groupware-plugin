package jp.groupsession.v2.kintai.Knt301;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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

public class Knt301Dao extends AbstractDao {

	/** Logging インスタンス */
	private static Log log__ = LogFactory.getLog(Knt301Dao.class);

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
	 * <p>パラメーターによるプロジェクトの取得
	 * @param prjSid int
	 * @param con    DB Connection
	 * @throws SQLException SQL実行例外
	 * @return ArrayList<Knt301Model>
	 */
	public Knt301Model selectProject(int prjSid, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Knt301Model ret = new Knt301Model();

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   *");
			sql.addSql(" from");
			sql.addSql("   KNT_PRJ");
			sql.addSql(" where");
			sql.addSql(" PRJ_SID = ?");
			sql.addSql(" ORDER BY PRJ_INDIRECT, PRJ_ID");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(prjSid);
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
				Knt301Model model = new Knt301Model();
				model.setPrjSid(rs.getInt("PRJ_SID"));
				model.setPrjId(rs.getString("PRJ_ID"));
				model.setPrjName(rs.getString("PRJ_NAME"));
				model.setStartYear(tsy);
				model.setStartMonth(tsm);
				model.setStartDay(tsd);
				model.setEndYear(tey);
				model.setEndMonth(tem);
				model.setEndDay(ted);
				model.setOrderName(rs.getString("PRJ_ORDER_NAME"));
				model.setChiefName(rs.getString("PRJ_CHIEF"));
				model.setManagerName(rs.getString("PRJ_MANAGER"));
				model.setRemark(rs.getString("PRJ_REMARK"));
				model.setTargetB(rs.getInt("PRJ_TARGET_B"));
				model.setTargetS(rs.getInt("PRJ_TARGET_S"));
				model.setTargetH(rs.getInt("PRJ_TARGET_H"));
				model.setTargetJ(rs.getInt("PRJ_TARGET_J"));
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
	 * <p>パラメーターによるプロジェクト一覧の取得
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return ArrayList<Knt301Model>
	 */
	public ArrayList<Knt301Model> selectProjectList(Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Knt301Model> ret = new ArrayList<Knt301Model>();

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   *");
			sql.addSql(" from");
			sql.addSql("   KNT_PRJ");
			sql.addSql(" ORDER BY PRJ_INDIRECT, PRJ_ID");

			pstmt = con.prepareStatement(sql.toSqlString());
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
				Knt301Model model = new Knt301Model();
				model.setPrjSid(rs.getInt("PRJ_SID"));
				model.setPrjId(rs.getString("PRJ_ID"));
				model.setPrjName(rs.getString("PRJ_NAME"));
				model.setStartYear(tsy);
				model.setStartMonth(tsm);
				model.setStartDay(tsd);
				model.setEndYear(tey);
				model.setEndMonth(tem);
				model.setEndDay(ted);
				model.setOrderName(rs.getString("PRJ_ORDER_NAME"));
				model.setChiefName(rs.getString("PRJ_CHIEF"));
				model.setManagerName(rs.getString("PRJ_MANAGER"));
				model.setRemark(rs.getString("PRJ_REMARK"));
				model.setTargetB(rs.getInt("PRJ_TARGET_B"));
				model.setTargetS(rs.getInt("PRJ_TARGET_S"));
				model.setTargetH(rs.getInt("PRJ_TARGET_H"));
				model.setTargetJ(rs.getInt("PRJ_TARGET_J"));
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
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return ArrayList<Knt301Model>
	 */
	public Integer emptyProjectId(Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Integer ret = -1;

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   max(PRJ_SID) as MAX_PRJ_SID");
			sql.addSql(" from");
			sql.addSql("   KNT_PRJ");

			pstmt = con.prepareStatement(sql.toSqlString());
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				ret = rs.getInt("MAX_PRJ_SID") + 1;
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
	 * @param prjSid  プロジェクトID
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return ArrayList<Knt301Model>
	 */
	public ArrayList<Knt301Model> selectProjectList(int prjSid, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Knt301Model> ret = new ArrayList<Knt301Model>();

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   *");
			sql.addSql(" from");
			sql.addSql("   KNT_PRJ");
			sql.addSql(" where");
			sql.addSql("   PRJ_SID = ?");
			sql.addSql(" ORDER BY PRJ_INDIRECT, PRJ_ID");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(prjSid);
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
				Knt301Model model = new Knt301Model();
				model.setPrjSid(rs.getInt("PRJ_SID"));
				model.setPrjId(rs.getString("PRJ_ID"));
				model.setPrjName(rs.getString("PRJ_NAME"));
				model.setStartYear(tsy);
				model.setStartMonth(tsm);
				model.setStartDay(tsd);
				model.setEndYear(tey);
				model.setEndMonth(tem);
				model.setEndDay(ted);
				model.setIndirect(rs.getInt("PRJ_INDIRECT"));
				model.setOrderName(rs.getString("PRJ_ORDER_NAME"));
				model.setChiefName(rs.getString("PRJ_CHIEF"));
				model.setManagerName(rs.getString("PRJ_MANAGER"));
				model.setRemark(rs.getString("PRJ_REMARK"));
				model.setTargetB(rs.getInt("PRJ_TARGET_B"));
				model.setTargetS(rs.getInt("PRJ_TARGET_S"));
				model.setTargetH(rs.getInt("PRJ_TARGET_H"));
				model.setTargetJ(rs.getInt("PRJ_TARGET_J"));
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
	 * @param prjSid  プロジェクトID
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return ArrayList<Knt301Model>
	 */
	public ArrayList<Knt301Model> selectProjectIdList(String prjId, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Knt301Model> ret = new ArrayList<Knt301Model>();

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   *");
			sql.addSql(" from");
			sql.addSql("   KNT_PRJ");
			sql.addSql(" where");
			sql.addSql("   PRJ_ID = ?");
			sql.addSql(" ORDER BY PRJ_INDIRECT, PRJ_ID");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addStrValue(prjId);
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
				Knt301Model model = new Knt301Model();
				model.setPrjSid(rs.getInt("PRJ_SID"));
				model.setPrjId(rs.getString("PRJ_ID"));
				model.setPrjName(rs.getString("PRJ_NAME"));
				model.setStartYear(tsy);
				model.setStartMonth(tsm);
				model.setStartDay(tsd);
				model.setEndYear(tey);
				model.setEndMonth(tem);
				model.setEndDay(ted);
				model.setIndirect(rs.getInt("PRJ_INDIRECT"));
				model.setOrderName(rs.getString("PRJ_ORDER_NAME"));
				model.setChiefName(rs.getString("PRJ_CHIEF"));
				model.setManagerName(rs.getString("PRJ_MANAGER"));
				model.setRemark(rs.getString("PRJ_REMARK"));
				model.setTargetB(rs.getInt("PRJ_TARGET_B"));
				model.setTargetS(rs.getInt("PRJ_TARGET_S"));
				model.setTargetH(rs.getInt("PRJ_TARGET_H"));
				model.setTargetJ(rs.getInt("PRJ_TARGET_J"));
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
	 * @param prjName プロジェクト名称
	 * @param con DB  Connection
	 * @throws SQLException SQL実行例外
	 * @return ArrayList<Knt301Model>
	 */
	public ArrayList<Knt301Model> selectProjectList(String prjName, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Knt301Model> ret = new ArrayList<Knt301Model>();

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   *");
			sql.addSql(" from");
			sql.addSql("   KNT_PRJ");
			sql.addSql(" where");
			sql.addSql("   PRJ_NAME LIKE '%" + prjName + "%'");
			sql.addSql(" ORDER BY PRJ_INDIRECT, PRJ_ID");

			pstmt = con.prepareStatement(sql.toSqlString());
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
				Knt301Model model = new Knt301Model();
				model.setPrjSid(rs.getInt("PRJ_SID"));
				model.setPrjId(rs.getString("PRJ_ID"));
				model.setPrjName(rs.getString("PRJ_NAME"));
				model.setStartYear(tsy);
				model.setStartMonth(tsm);
				model.setStartDay(tsd);
				model.setEndYear(tey);
				model.setEndMonth(tem);
				model.setEndDay(ted);
				model.setIndirect(rs.getInt("PRJ_INDIRECT"));
				model.setOrderName(rs.getString("PRJ_ORDER_NAME"));
				model.setChiefName(rs.getString("PRJ_CHIEF"));
				model.setManagerName(rs.getString("PRJ_MANAGER"));
				model.setRemark(rs.getString("PRJ_REMARK"));
				model.setTargetB(rs.getInt("PRJ_TARGET_B"));
				model.setTargetS(rs.getInt("PRJ_TARGET_S"));
				model.setTargetH(rs.getInt("PRJ_TARGET_H"));
				model.setTargetJ(rs.getInt("PRJ_TARGET_J"));
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
	 * @param sy      開始年
	 * @param sm      開始月
	 * @param sd      開始日
	 * @param con DB  Connection
	 * @throws SQLException SQL実行例外
	 * @return ArrayList<Knt301Model>
	 */
	public ArrayList<Knt301Model> selectStartDateProjectList(int sy, int sm, int sd, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Knt301Model> ret = new ArrayList<Knt301Model>();

		// TIMESTAMPの書式
		String START = sy + "-" + sm + "-" + sd + " " + "00:00:00";

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   *");
			sql.addSql(" from");
			sql.addSql("   KNT_PRJ");
			sql.addSql(" where");
			sql.addSql("   PRJ_START >= ?");
			sql.addSql(" ORDER BY PRJ_INDIRECT, PRJ_ID");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addStrValue(START);
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
				Knt301Model model = new Knt301Model();
				model.setPrjSid(rs.getInt("PRJ_SID"));
				model.setPrjId(rs.getString("PRJ_ID"));
				model.setPrjName(rs.getString("PRJ_NAME"));
				model.setStartYear(tsy);
				model.setStartMonth(tsm);
				model.setStartDay(tsd);
				model.setEndYear(tey);
				model.setEndMonth(tem);
				model.setEndDay(ted);
				model.setIndirect(rs.getInt("PRJ_INDIRECT"));
				model.setOrderName(rs.getString("PRJ_ORDER_NAME"));
				model.setChiefName(rs.getString("PRJ_CHIEF"));
				model.setManagerName(rs.getString("PRJ_MANAGER"));
				model.setRemark(rs.getString("PRJ_REMARK"));
				model.setTargetB(rs.getInt("PRJ_TARGET_B"));
				model.setTargetS(rs.getInt("PRJ_TARGET_S"));
				model.setTargetH(rs.getInt("PRJ_TARGET_H"));
				model.setTargetJ(rs.getInt("PRJ_TARGET_J"));
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
	 * @param ey      開始年
	 * @param em      開始月
	 * @param ed      開始日
	 * @param con DB  Connection
	 * @throws SQLException SQL実行例外
	 * @return ArrayList<Knt301Model>
	 */
	public ArrayList<Knt301Model> selectEndDateProjectList(int ey, int em, int ed, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Knt301Model> ret = new ArrayList<Knt301Model>();

		// TIMESTAMPの書式
		String END = ey + "-" + em + "-" + ed + " " + "23:59:59";

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   *");
			sql.addSql(" from");
			sql.addSql("   KNT_PRJ");
			sql.addSql(" where");
			sql.addSql("   PRJ_END <= ?");
			sql.addSql(" ORDER BY PRJ_INDIRECT, PRJ_ID");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addStrValue(END);
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
				Knt301Model model = new Knt301Model();
				model.setPrjSid(rs.getInt("PRJ_SID"));
				model.setPrjId(rs.getString("PRJ_ID"));
				model.setPrjName(rs.getString("PRJ_NAME"));
				model.setStartYear(tsy);
				model.setStartMonth(tsm);
				model.setStartDay(tsd);
				model.setEndYear(tey);
				model.setEndMonth(tem);
				model.setEndDay(ted);
				model.setIndirect(rs.getInt("PRJ_INDIRECT"));
				model.setOrderName(rs.getString("PRJ_ORDER_NAME"));
				model.setChiefName(rs.getString("PRJ_CHIEF"));
				model.setManagerName(rs.getString("PRJ_MANAGER"));
				model.setRemark(rs.getString("PRJ_REMARK"));
				model.setTargetB(rs.getInt("PRJ_TARGET_B"));
				model.setTargetS(rs.getInt("PRJ_TARGET_S"));
				model.setTargetH(rs.getInt("PRJ_TARGET_H"));
				model.setTargetJ(rs.getInt("PRJ_TARGET_J"));
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
	 * <p>発注者によるプロジェクト一覧の取得
	 * @param prjName プロジェクト名称
	 * @param con DB  Connection
	 * @throws SQLException SQL実行例外
	 * @return ArrayList<Knt301Model>
	 */
	public ArrayList<Knt301Model> selectOrderNameProjectList(String orderName, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Knt301Model> ret = new ArrayList<Knt301Model>();

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   *");
			sql.addSql(" from");
			sql.addSql("   KNT_PRJ");
			sql.addSql(" where");
			sql.addSql("   PRJ_ORDER_NAME LIKE '%" + orderName + "%'");
			sql.addSql(" ORDER BY PRJ_INDIRECT, PRJ_ID");

			pstmt = con.prepareStatement(sql.toSqlString());
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
				Knt301Model model = new Knt301Model();
				model.setPrjSid(rs.getInt("PRJ_SID"));
				model.setPrjId(rs.getString("PRJ_ID"));
				model.setPrjName(rs.getString("PRJ_NAME"));
				model.setStartYear(tsy);
				model.setStartMonth(tsm);
				model.setStartDay(tsd);
				model.setEndYear(tey);
				model.setEndMonth(tem);
				model.setEndDay(ted);
				model.setIndirect(rs.getInt("PRJ_INDIRECT"));
				model.setOrderName(rs.getString("PRJ_ORDER_NAME"));
				model.setChiefName(rs.getString("PRJ_CHIEF"));
				model.setManagerName(rs.getString("PRJ_MANAGER"));
				model.setRemark(rs.getString("PRJ_REMARK"));
				model.setTargetB(rs.getInt("PRJ_TARGET_B"));
				model.setTargetS(rs.getInt("PRJ_TARGET_S"));
				model.setTargetH(rs.getInt("PRJ_TARGET_H"));
				model.setTargetJ(rs.getInt("PRJ_TARGET_J"));
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
	 * <p>主任担当者によるプロジェクト一覧の取得
	 * @param prjName プロジェクト名称
	 * @param con DB  Connection
	 * @throws SQLException SQL実行例外
	 * @return ArrayList<Knt301Model>
	 */
	public ArrayList<Knt301Model> selectChiefNameProjectList(String chiefName, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Knt301Model> ret = new ArrayList<Knt301Model>();

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   *");
			sql.addSql(" from");
			sql.addSql("   KNT_PRJ");
			sql.addSql(" where");
			sql.addSql("   PRJ_CHIEF LIKE '%" + chiefName + "%'");
			sql.addSql(" ORDER BY PRJ_INDIRECT, PRJ_ID");

			pstmt = con.prepareStatement(sql.toSqlString());
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
				Knt301Model model = new Knt301Model();
				model.setPrjSid(rs.getInt("PRJ_SID"));
				model.setPrjId(rs.getString("PRJ_ID"));
				model.setPrjName(rs.getString("PRJ_NAME"));
				model.setStartYear(tsy);
				model.setStartMonth(tsm);
				model.setStartDay(tsd);
				model.setEndYear(tey);
				model.setEndMonth(tem);
				model.setEndDay(ted);
				model.setIndirect(rs.getInt("PRJ_INDIRECT"));
				model.setOrderName(rs.getString("PRJ_ORDER_NAME"));
				model.setChiefName(rs.getString("PRJ_CHIEF"));
				model.setManagerName(rs.getString("PRJ_MANAGER"));
				model.setRemark(rs.getString("PRJ_REMARK"));
				model.setTargetB(rs.getInt("PRJ_TARGET_B"));
				model.setTargetS(rs.getInt("PRJ_TARGET_S"));
				model.setTargetH(rs.getInt("PRJ_TARGET_H"));
				model.setTargetJ(rs.getInt("PRJ_TARGET_J"));
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
	 * <p>担当者によるプロジェクト一覧の取得
	 * @param prjName プロジェクト名称
	 * @param con DB  Connection
	 * @throws SQLException SQL実行例外
	 * @return ArrayList<Knt301Model>
	 */
	public ArrayList<Knt301Model> selectManagerNameProjectList(String managerName, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Knt301Model> ret = new ArrayList<Knt301Model>();

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   *");
			sql.addSql(" from");
			sql.addSql("   KNT_PRJ");
			sql.addSql(" where");
			sql.addSql("   PRJ_MANAGER LIKE '%" + managerName + "%'");
			sql.addSql(" ORDER BY PRJ_INDIRECT, PRJ_ID");

			pstmt = con.prepareStatement(sql.toSqlString());
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
				Knt301Model model = new Knt301Model();
				model.setPrjSid(rs.getInt("PRJ_SID"));
				model.setPrjId(rs.getString("PRJ_ID"));
				model.setPrjName(rs.getString("PRJ_NAME"));
				model.setStartYear(tsy);
				model.setStartMonth(tsm);
				model.setStartDay(tsd);
				model.setEndYear(tey);
				model.setEndMonth(tem);
				model.setEndDay(ted);
				model.setIndirect(rs.getInt("PRJ_INDIRECT"));
				model.setOrderName(rs.getString("PRJ_ORDER_NAME"));
				model.setChiefName(rs.getString("PRJ_CHIEF"));
				model.setManagerName(rs.getString("PRJ_MANAGER"));
				model.setRemark(rs.getString("PRJ_REMARK"));
				model.setTargetB(rs.getInt("PRJ_TARGET_B"));
				model.setTargetS(rs.getInt("PRJ_TARGET_S"));
				model.setTargetH(rs.getInt("PRJ_TARGET_H"));
				model.setTargetJ(rs.getInt("PRJ_TARGET_J"));
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
	 * <p>対象によるプロジェクト一覧の取得
	 * @param targetB 物件
	 * @param targetS 測量
	 * @param targetH 補償説明
	 * @param targetJ 事業認定
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return ArrayList<Knt301Model>
	 */
	public ArrayList<Knt301Model> selectTargetProjectList(int targetB,int targetS,int targetH,int targetJ, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Knt301Model> ret = new ArrayList<Knt301Model>();

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   *");
			sql.addSql(" from");
			sql.addSql("   KNT_PRJ");
			sql.addSql(" where");
			sql.addSql("   PRJ_TARGET_B = ?");
			sql.addSql(" or");
			sql.addSql("   PRJ_TARGET_S = ?");
			sql.addSql(" or");
			sql.addSql("   PRJ_TARGET_H = ?");
			sql.addSql(" or");
			sql.addSql("   PRJ_TARGET_J = ?");
			sql.addSql(" ORDER BY PRJ_INDIRECT, PRJ_ID");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(targetB);
			sql.addIntValue(targetS);
			sql.addIntValue(targetH);
			sql.addIntValue(targetJ);
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
				Knt301Model model = new Knt301Model();
				model.setPrjSid(rs.getInt("PRJ_SID"));
				model.setPrjId(rs.getString("PRJ_ID"));
				model.setPrjName(rs.getString("PRJ_NAME"));
				model.setStartYear(tsy);
				model.setStartMonth(tsm);
				model.setStartDay(tsd);
				model.setEndYear(tey);
				model.setEndMonth(tem);
				model.setEndDay(ted);
				model.setIndirect(rs.getInt("PRJ_INDIRECT"));
				model.setOrderName(rs.getString("PRJ_ORDER_NAME"));
				model.setChiefName(rs.getString("PRJ_CHIEF"));
				model.setManagerName(rs.getString("PRJ_MANAGER"));
				model.setRemark(rs.getString("PRJ_REMARK"));
				model.setTargetB(rs.getInt("PRJ_TARGET_B"));
				model.setTargetS(rs.getInt("PRJ_TARGET_S"));
				model.setTargetH(rs.getInt("PRJ_TARGET_H"));
				model.setTargetJ(rs.getInt("PRJ_TARGET_J"));
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
