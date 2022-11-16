package jp.groupsession.v2.kintai.Knt500;

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

public class Knt500Dao extends AbstractDao {

	/** Logging インスタンス */
	private static Log log__ = LogFactory.getLog(Knt500Dao.class);

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
	* <br>[機  能] SQLを実行する
	* <br>[解  説]
	* <br>[備  考]
	* @param insertSql sql
	* @param con  コネクション
	* @throws SQLException SQL実行時例外
	*/
	public void executeSql(String exeSql, Connection con) throws SQLException {

		PreparedStatement pstmt = null;

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(exeSql);
			pstmt = con.prepareStatement(sql.toSqlString());
			sql.setParameter(pstmt);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} finally {
			JDBCUtil.closeStatement(pstmt);
		}
	}

	/**
	 * <p>プロジェクト一覧取得
	 * @param con    DB Connection
	 * @throws SQLException SQL実行例外
	 * @return ArrayList<Knt500Model>
	 */
	public ArrayList<Knt500Model> selectProject(Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Knt500Model> ret = new ArrayList<Knt500Model>();

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" SELECT");
			sql.addSql("   *");
			sql.addSql(" FROM");
			sql.addSql("   KNT_PRJ");

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
				Knt500Model model = new Knt500Model();
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
				model.setTargetB(rs.getInt("PRJ_TARGET_B"));
				model.setTargetS(rs.getInt("PRJ_TARGET_S"));
				model.setTargetH(rs.getInt("PRJ_TARGET_H"));
				model.setTargetJ(rs.getInt("PRJ_TARGET_J"));
				model.setChiefName(rs.getString("PRJ_CHIEF"));
				model.setManagerName(rs.getString("PRJ_MANAGER"));
				model.setRemark(rs.getString("PRJ_REMARK"));
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
	 * <p>パラメーターによるプロジェクトの存在チェック
	 * @param prjId   プロジェクトID
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return 作業実績合計時間
	 */
	public Boolean containsProject(int prjSid, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Boolean ret = false;
		Boolean isUpdate = false;

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
			ret = rs.next();

			if (ret){
				// 存在する
				isUpdate = true;
			} else {
				// 存在しない
				isUpdate = false;
			}

		} catch (SQLException e) {
			throw e;
		} finally {
			JDBCUtil.closeResultSet(rs);
			JDBCUtil.closeStatement(pstmt);
		}

		return isUpdate;

	}

	/**
	* <br>[機  能] プロジェクト情報を登録する
	* <br>[解  説]
	* <br>[備  考]
	* @param data カンマデータ配列
	* @param con  コネクション
	* @throws SQLException SQL実行時例外
	*/
	public void insertProject(String[] data, Connection con) throws SQLException {

		PreparedStatement pstmt = null;

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" insert ");
			sql.addSql(" into ");
			sql.addSql(" KNT_PRJ(");
			sql.addSql("   PRJ_SID,");
			sql.addSql("   PRJ_ID,");
			sql.addSql("   PRJ_NAME,");
			sql.addSql("   PRJ_START,");
			sql.addSql("   PRJ_END,");
			sql.addSql("   PRJ_INDIRECT,");
			sql.addSql("   PRJ_ORDER_NAME,");
			sql.addSql("   PRJ_TARGET_B,");
			sql.addSql("   PRJ_TARGET_S,");
			sql.addSql("   PRJ_TARGET_H,");
			sql.addSql("   PRJ_TARGET_J,");
			sql.addSql("   PRJ_CHIEF,");
			sql.addSql("   PRJ_MANAGER,");
			sql.addSql("   PRJ_REMARK");

			sql.addSql(" )");
			sql.addSql(" values");
			sql.addSql(" (");
			sql.addSql("   ?,");
			sql.addSql("   ?,");
			sql.addSql("   ?,");
			sql.addSql("   ?,");
			sql.addSql("   ?,");
			sql.addSql("   ?,");
			sql.addSql("   ?,");
			sql.addSql("   ?,");
			sql.addSql("   ?,");
			sql.addSql("   ?,");
			sql.addSql("   ?,");
			sql.addSql("   ?,");
			sql.addSql("   ?,");
			sql.addSql("   ?");
			sql.addSql(" )");

			// lineをカンマで分割し、配列dataに設定
//			String[] data = line.split(",");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(Integer.parseInt(data[0].trim()));
			sql.addStrValue(data[1]);
			sql.addStrValue(data[2]);
			sql.addStrValue(data[3]);
			sql.addStrValue(data[4]);
			sql.addIntValue(Integer.parseInt(data[5].trim()));
			sql.addStrValue(data[6]);
			sql.addIntValue(Integer.parseInt(data[7].trim()));
			sql.addIntValue(Integer.parseInt(data[8].trim()));
			sql.addIntValue(Integer.parseInt(data[9].trim()));
			sql.addIntValue(Integer.parseInt(data[10].trim()));
			sql.addStrValue(data[11]);
			sql.addStrValue(data[12]);
			sql.addStrValue(data[13]);
			sql.setParameter(pstmt);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} finally {
			JDBCUtil.closeStatement(pstmt);
		}
	}

	/**
	* <br>[機  能] プロジェクト情報を更新する
	* <br>[解  説]
	* <br>[備  考]
	* @param data カンマデータ配列
	* @param con  コネクション
	* @throws SQLException SQL実行時例外
	*/
	public void updateProject(String[] data, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		int count = 0;

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" update");
			sql.addSql("   KNT_PRJ");
			sql.addSql(" set ");
			sql.addSql("   PRJ_ID=?,");
			sql.addSql("   PRJ_NAME=?,");
			sql.addSql("   PRJ_START=?,");
			sql.addSql("   PRJ_END=?,");
			sql.addSql("   PRJ_INDIRECT=?,");
			sql.addSql("   PRJ_ORDER_NAME=?,");
			sql.addSql("   PRJ_TARGET_B=?,");
			sql.addSql("   PRJ_TARGET_S=?,");
			sql.addSql("   PRJ_TARGET_H=?,");
			sql.addSql("   PRJ_TARGET_J=?,");
			sql.addSql("   PRJ_CHIEF=?,");
			sql.addSql("   PRJ_MANAGER=?,");
			sql.addSql("   PRJ_REMARK=?");
			sql.addSql(" where");
			sql.addSql("   PRJ_SID = ?");

			// lineをカンマで分割し、配列dataに設定
//			String[] data = line.split(",");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addStrValue(data[1]);
			sql.addStrValue(data[2]);
			sql.addStrValue(data[3]);
			sql.addStrValue(data[4]);
			sql.addIntValue(Integer.parseInt(data[5].trim()));
			sql.addStrValue(data[6]);
			sql.addIntValue(Integer.parseInt(data[7].trim()));
			sql.addIntValue(Integer.parseInt(data[8].trim()));
			sql.addIntValue(Integer.parseInt(data[9].trim()));
			sql.addIntValue(Integer.parseInt(data[10].trim()));
			sql.addStrValue(data[11]);
			sql.addStrValue(data[12]);
			sql.addStrValue(data[13]);
			sql.addIntValue(Integer.parseInt(data[0].trim()));
			sql.setParameter(pstmt);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} finally {
			JDBCUtil.closeStatement(pstmt);
		}

	}

	/**
	 * <p>プロジェクト担当者一覧取得
	 * @param con    DB Connection
	 * @throws SQLException SQL実行例外
	 * @return ArrayList<Knt500Model>
	 */
	public ArrayList<Knt500TntModel> selectProjectTanto(Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Knt500TntModel> ret = new ArrayList<Knt500TntModel>();

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" SELECT");
			sql.addSql("   *");
			sql.addSql(" FROM");
			sql.addSql("   KNT_TNT");

			pstmt = con.prepareStatement(sql.toSqlString());
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Knt500TntModel model = new Knt500TntModel();
				model.setPrjSid(rs.getInt("TNT_PRJ_SID"));
				model.setUsrSid(rs.getInt("TNT_USR_SID"));
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
	* <br>[機  能] プロジェクト担当者情報を登録する
	* <br>[解  説]
	* <br>[備  考]
	* @param data カンマデータ配列
	* @param con  コネクション
	* @throws SQLException SQL実行時例外
	*/
	public void insertProjectTanto(String[] data, Connection con) throws SQLException {

		PreparedStatement pstmt = null;

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" insert ");
			sql.addSql(" into ");
			sql.addSql(" KNT_TNT(");
			sql.addSql("   TNT_PRJ_SID,");
			sql.addSql("   TNT_USR_SID");
			sql.addSql(" )");
			sql.addSql(" values");
			sql.addSql(" (");
			sql.addSql("   ?,");
			sql.addSql("   ?");
			sql.addSql(" )");

			// lineをカンマで分割し、配列dataに設定
//			String[] data = line.split(",");

			log__.debug("data[0]=" + data[0]);
			log__.debug("data[1]=" + data[1]);

			int prjSid = Integer.parseInt(data[0].trim());
			int usrSid = Integer.parseInt(data[1].trim());

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(prjSid);
			sql.addIntValue(usrSid);
			sql.setParameter(pstmt);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} finally {
			JDBCUtil.closeStatement(pstmt);
		}
	}

	/**
	 * <p>プロジェクト担当者全削除
	 * @param con    DB Connection
	 * @throws SQLException SQL実行例外
	 * @return ArrayList<Knt500Model>
	 */
	public void deleteAllProjectTanto(Connection con) throws SQLException {

		PreparedStatement pstmt = null;

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" DELETE");
			sql.addSql(" FROM");
			sql.addSql("   KNT_TNT");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.setParameter(pstmt);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} finally {
			JDBCUtil.closeStatement(pstmt);
		}

	}

	/**
	 * <p>作業一覧取得
	 * @param con    DB Connection
	 * @throws SQLException SQL実行例外
	 * @return ArrayList<Knt500Model>
	 */
	public ArrayList<Knt500WrkModel> selectWorkList(Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Knt500WrkModel> ret = new ArrayList<Knt500WrkModel>();

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" SELECT");
			sql.addSql("   *");
			sql.addSql(" FROM");
			sql.addSql("   KNT_WRK");

			pstmt = con.prepareStatement(sql.toSqlString());
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Knt500WrkModel model = new Knt500WrkModel();
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
	* <br>[機  能] 作業を登録する
	* <br>[解  説]
	* <br>[備  考]
	* @param data カンマデータ配列
	* @param con  コネクション
	* @throws SQLException SQL実行時例外
	*/
	public void insertWork(String[] data, Connection con) throws SQLException {

		PreparedStatement pstmt = null;

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" insert ");
			sql.addSql(" into ");
			sql.addSql(" KNT_WRK(");
			sql.addSql("   WRK_ID,");
			sql.addSql("   WRK_NAME");
			sql.addSql(" )");
			sql.addSql(" values");
			sql.addSql(" (");
			sql.addSql("   ?,");
			sql.addSql("   ?");
			sql.addSql(" )");

			// lineをカンマで分割し、配列dataに設定
//			String[] data = line.split(",");

			log__.debug("data[0]=" + data[0]);
			log__.debug("data[1]=" + data[1]);

			int wrkId = Integer.parseInt(data[0].trim());
			String wrkName = data[1].trim();

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(wrkId);
			sql.addStrValue(wrkName);
			sql.setParameter(pstmt);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} finally {
			JDBCUtil.closeStatement(pstmt);
		}
	}

	/**
	 * <p>作業全削除
	 * @param con    DB Connection
	 * @throws SQLException SQL実行例外
	 * @return ArrayList<Knt500Model>
	 */
	public void deleteAllWork(Connection con) throws SQLException {

		PreparedStatement pstmt = null;

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" DELETE");
			sql.addSql(" FROM");
			sql.addSql("   KNT_WRK");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.setParameter(pstmt);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} finally {
			JDBCUtil.closeStatement(pstmt);
		}

	}

	/**
	 * <p>実績一覧取得
	 * @param con    DB Connection
	 * @throws SQLException SQL実行例外
	 * @return ArrayList<Knt500Model>
	 */
	public ArrayList<Knt500JskModel> selectJskList(Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Knt500JskModel> ret = new ArrayList<Knt500JskModel>();

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" SELECT");
			sql.addSql("   *");
			sql.addSql(" FROM");
			sql.addSql("   KNT_STP");

			pstmt = con.prepareStatement(sql.toSqlString());
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				int tY = toTimestampYmd(rs.getTimestamp("STP_TIME"), "yyyy");
				int tM = toTimestampYmd(rs.getTimestamp("STP_TIME"), "MM");
				int tD = toTimestampYmd(rs.getTimestamp("STP_TIME"), "dd");
				int th = toTimestampYmd(rs.getTimestamp("STP_TIME"), "HH");
				int tm = toTimestampYmd(rs.getTimestamp("STP_TIME"), "mm");
				int ts = toTimestampYmd(rs.getTimestamp("STP_TIME"), "ss");
				Knt500JskModel model = new Knt500JskModel();
				model.setUsrSid(rs.getInt("STP_USR_SID"));
				model.setStpTime(tY + "-" + tM + "-" + tD + " " + th + ":" + tm + ":" + ts);
				model.setIomId(rs.getInt("STP_IOM_ID"));
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
	* <br>[機  能] 実績を登録する
	* <br>[解  説]
	* <br>[備  考]
	* @param data カンマデータ配列
	* @param con  コネクション
	* @throws SQLException SQL実行時例外
	*/
	public void insertJsk(String[] data, Connection con) throws SQLException {

		PreparedStatement pstmt = null;

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" insert ");
			sql.addSql(" into ");
			sql.addSql(" KNT_STP(");
			sql.addSql("   STP_USR_SID,");
			sql.addSql("   STP_TIME,");
			sql.addSql("   STP_IOM_ID");
			sql.addSql(" )");
			sql.addSql(" values");
			sql.addSql(" (");
			sql.addSql("   ?,");
			sql.addSql("   ?,");
			sql.addSql("   ?");
			sql.addSql(" )");

			// lineをカンマで分割し、配列dataに設定
//			String[] data = line.split(",");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(Integer.parseInt(data[0].trim()));
			sql.addStrValue(data[1]);
			sql.addIntValue(Integer.parseInt(data[2].trim()));
			sql.setParameter(pstmt);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} finally {
			JDBCUtil.closeStatement(pstmt);
		}
	}

	/**
	 * <p>実績削除
	 * @param con    DB Connection
	 * @throws SQLException SQL実行例外
	 * @return ArrayList<Knt500Model>
	 */
	public void deleteJsk(String[] data, Connection con) throws SQLException {

		PreparedStatement pstmt = null;

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" DELETE");
			sql.addSql(" FROM");
			sql.addSql("   KNT_STP");
			sql.addSql(" WHERE");
			sql.addSql("   STP_USR_SID = ?");
			sql.addSql(" AND");
			sql.addSql("   STP_TIME = ?");
			sql.addSql(" AND");
			sql.addSql("   STP_IOM_ID = ?");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(Integer.parseInt(data[0].trim()));
			sql.addStrValue(data[1]);
			sql.addIntValue(Integer.parseInt(data[2].trim()));
			sql.setParameter(pstmt);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} finally {
			JDBCUtil.closeStatement(pstmt);
		}

	}

	/**
	 * <p>申請一覧取得
	 * @param con    DB Connection
	 * @throws SQLException SQL実行例外
	 * @return ArrayList<Knt500Model>
	 */
	public ArrayList<Knt500UsrModel> selectUsrWork(Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Knt500UsrModel> ret = new ArrayList<Knt500UsrModel>();

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" SELECT");
			sql.addSql("   *");
			sql.addSql(" FROM");
			sql.addSql("   KNT_JSK");

			pstmt = con.prepareStatement(sql.toSqlString());
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				int tY = toTimestampYmd(rs.getTimestamp("JSK_YMD"), "yyyy");
				int tM = toTimestampYmd(rs.getTimestamp("JSK_YMD"), "MM");
				int tD = toTimestampYmd(rs.getTimestamp("JSK_YMD"), "dd");
				int th = toTimestampYmd(rs.getTimestamp("JSK_YMD"), "hh");
				int tm = toTimestampYmd(rs.getTimestamp("JSK_YMD"), "mm");
				int ts = toTimestampYmd(rs.getTimestamp("JSK_YMD"), "ss");
				Knt500UsrModel model = new Knt500UsrModel();
				model.setUsrSid(rs.getInt("JSK_USR_SID"));
				model.setJskYmd(tY + "-" + tM + "-" + tD + " " + th + ":" + tm + ":" + ts);
				model.setJskId(rs.getInt("JSK_ID"));
				model.setPrjSid(rs.getInt("JSK_PRJ_SID"));
				model.setWrkId(rs.getInt("JSK_WRK_ID"));
				model.setWrkTime(rs.getDouble("JSK_WRK_TIME"));
				model.setRemark(rs.getString("JSK_RMK"));
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
	* <br>[機  能] 申請を登録する
	* <br>[解  説]
	* <br>[備  考]
	* @param data カンマデータ配列
	* @param con  コネクション
	* @throws SQLException SQL実行時例外
	*/
	public void insertUsrJsk(String[] data, Connection con) throws SQLException {

		PreparedStatement pstmt = null;

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" insert ");
			sql.addSql(" into ");
			sql.addSql(" KNT_JSK(");
			sql.addSql("   JSK_USR_SID,");
			sql.addSql("   JSK_YMD,");
			sql.addSql("   JSK_ID,");
			sql.addSql("   JSK_PRJ_SID,");
			sql.addSql("   JSK_WRK_ID,");
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

			// lineをカンマで分割し、配列dataに設定
//			String[] data = line.split(",");

			// 
			String rmk = "";
			if (6 < data.length){
				rmk = NullDefault.getString(data[6], "");
			}

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(Integer.parseInt(data[0].trim()));
			sql.addStrValue(data[1]);
			sql.addIntValue(Integer.parseInt(data[2].trim()));
			sql.addIntValue(Integer.parseInt(data[3].trim()));
			sql.addIntValue(Integer.parseInt(data[4].trim()));
			sql.addDoubleValue(Double.parseDouble(data[5].trim()));
			sql.addStrValue(rmk);
			sql.setParameter(pstmt);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} finally {
			JDBCUtil.closeStatement(pstmt);
		}
	}

	/**
	 * <p>申請削除
	 * @param con    DB Connection
	 * @throws SQLException SQL実行例外
	 * @return ArrayList<Knt500Model>
	 */
	public void deleteUsrJsk(String[] data, Connection con) throws SQLException {

		PreparedStatement pstmt = null;

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" DELETE");
			sql.addSql(" FROM");
			sql.addSql("   KNT_JSK");
			sql.addSql(" WHERE");
			sql.addSql("   JSK_USR_SID = ?");
			sql.addSql(" AND");
			sql.addSql("   JSK_YMD = ?");
			sql.addSql(" AND");
			sql.addSql("   JSK_ID = ?");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(Integer.parseInt(data[0].trim()));
			sql.addStrValue(data[1]);
			sql.addIntValue(Integer.parseInt(data[2].trim()));
			sql.setParameter(pstmt);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} finally {
			JDBCUtil.closeStatement(pstmt);
		}

	}

}
