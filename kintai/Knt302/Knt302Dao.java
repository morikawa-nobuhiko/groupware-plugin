package jp.groupsession.v2.kintai.Knt302;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Date;
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

public class Knt302Dao extends AbstractDao {

	/** Logging インスタンス */
	private static Log log__ = LogFactory.getLog(Knt302Dao.class);

	/**
	 * <p>パラメーターによるプロジェクトの存在チェック
	 * @param prjSid  プロジェクトSID
	 * @param prjId   プロジェクトID
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return 作業実績合計時間
	 */
	public Boolean containsProject(int prjSid,String prjId, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Boolean ret = false;
		Boolean isNew = false;

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   *");
			sql.addSql(" from");
			sql.addSql("   KNT_PRJ");
			sql.addSql(" where");
			sql.addSql("   PRJ_SID = ?");
			sql.addSql(" and");
			sql.addSql("   PRJ_ID = ?");
			
			// 2021.02.21時点では同じプロジェクトIDは入力できないようにしている
			// 同じプロジェクトIDで登録可能にするには「PRJ_ID = ?」をコメント化する

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(prjSid);
			sql.addStrValue(prjId);
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
	 * <p>パラメーターによるプロジェクトの存在チェック
	 * @param prjId   プロジェクトID
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return 作業実績合計時間
	 */
	public Boolean containsProject(String prjId, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Boolean ret = false;
		Boolean isNew = false;

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   *");
			sql.addSql(" from");
			sql.addSql("   KNT_PRJ");
			sql.addSql(" where");
			sql.addSql("   PRJ_ID = ?");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addStrValue(prjId);
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
	 * <p>パラメーターによるユーザ一覧の取得
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return ArrayList<Knt302UserModel>
	 */
	public ArrayList<Knt302UserModel> selectUsers(Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Knt302UserModel> ret = new ArrayList<Knt302UserModel>();

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   distinct");
			sql.addSql("   T1.USR_SID,");
			sql.addSql("   T1.USI_SEI,");
			sql.addSql("   T1.USI_MEI,");
			sql.addSql("   T2.GRP_SID,");
			sql.addSql("   T3.POS_SORT");
			sql.addSql(" from");
			sql.addSql("   CMN_USRM_INF AS T1");
			sql.addSql(" join");
			sql.addSql("   CMN_BELONGM AS T2");
			sql.addSql(" on");
			sql.addSql("   T1.USR_SID = T2.USR_SID");

//			sql.addSql(" and");
//			sql.addSql("   T2.GRP_SID = 7");
//			sql.addSql(" and");

			sql.addSql(" and");
			sql.addSql("   T2.GRP_SID >= 2");
			sql.addSql(" and");
//			sql.addSql("   T2.GRP_SID <= 6");
//			sql.addSql(" and");

			sql.addSql("   T1.USR_SID != 101");
			sql.addSql(" join");
			sql.addSql("   CMN_POSITION AS T3");
			sql.addSql(" on");
			sql.addSql("   T1.POS_SID = T3.POS_SID");
			sql.addSql(" order by T2.GRP_SID, T3.POS_SORT, T1.USI_SEI");

			pstmt = con.prepareStatement(sql.toSqlString());
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			List<Integer> lst = new ArrayList<Integer>();
			while (rs.next()) {
				Knt302UserModel model = new Knt302UserModel();
				int sid = rs.getInt("USR_SID");
				model.setGrpId(rs.getInt("GRP_SID"));
				model.setUsrSid(sid);
				model.setUsrName(rs.getString("USI_SEI") + " " + rs.getString("USI_MEI"));
				if (!lst.contains(sid)) {
					ret.add(model);
				}
				lst.add(sid);
			}

			// 雅人直入れ
			if (!lst.contains(101)) {
				Knt302UserModel model = new Knt302UserModel();
				model.setGrpId(1);
				model.setUsrSid(101);
				model.setUsrName("林 雅人");
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
	 * <p>パラメーターによるプロジェクト担当者一覧の取得
	 * @param prjSid  プロジェクトID
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return ArrayList<Knt302UserModel>
	 */
	public ArrayList<Integer> selectProjectUsers(int prjSid, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Integer> ret = new ArrayList<Integer>();

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   *");
			sql.addSql(" from");
			sql.addSql("   KNT_TNT");
			sql.addSql(" where");
			sql.addSql("   TNT_PRJ_SID = ?");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(prjSid);
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				ret.add(rs.getInt("TNT_USR_SID"));
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
	* <br>[機  能] プロジェクト情報を登録する
	* <br>[解  説]
	* <br>[備  考]
	* @param form フォーム
	* @param con  コネクション
	* @throws SQLException SQL実行時例外
	*/
	public void insertProject(Knt302Form form, Connection con) throws SQLException {

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
			sql.addSql("   PRJ_CHIEF,");
			sql.addSql("   PRJ_MANAGER,");
			sql.addSql("   PRJ_REMARK,");
			sql.addSql("   PRJ_TARGET_B,");
			sql.addSql("   PRJ_TARGET_S,");
			sql.addSql("   PRJ_TARGET_H,");
			sql.addSql("   PRJ_TARGET_J");
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

			// TIMESTAMPの書式
			String START = form.getStartYear() + "-" + form.getStartMonth() + "-" + form.getStartDay() + " " + "00:00:00";
			String END = form.getEndYear() + "-" + form.getEndMonth() + "-" + form.getEndDay() + " " + "23:59:59";

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(form.getPrjSid());
			sql.addStrValue(form.getPrjId());
			sql.addStrValue(form.getPrjName());
			sql.addStrValue(START);
			sql.addStrValue(END);
			sql.addIntValue(form.getIndirect());
			// 2021.07.29 追加
			sql.addStrValue(form.getOrderName());
			sql.addStrValue(form.getChiefName());
			sql.addStrValue(form.getManagerName());
			sql.addStrValue(form.getRemark());
			sql.addIntValue(form.getTargetB());
			sql.addIntValue(form.getTargetS());
			sql.addIntValue(form.getTargetH());
			sql.addIntValue(form.getTargetJ());

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
	* <br>[機  能] プロジェクト情報を更新する
	* <br>[解  説]
	* <br>[備  考]
	* @param form フォーム
	* @param con  コネクション
	* @throws SQLException SQL実行時例外
	*/
	public void updateProject(Knt302Form form, Connection con) throws SQLException {

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
			sql.addSql("   PRJ_CHIEF=?,");
			sql.addSql("   PRJ_MANAGER=?,");
			sql.addSql("   PRJ_REMARK=?,");
			sql.addSql("   PRJ_TARGET_B=?,");
			sql.addSql("   PRJ_TARGET_S=?,");
			sql.addSql("   PRJ_TARGET_H=?,");
			sql.addSql("   PRJ_TARGET_J=?");
			sql.addSql(" where");
			sql.addSql("   PRJ_SID = ?");

			// TIMESTAMPの書式
			String START = form.getStartYear() + "-" + form.getStartMonth() + "-" + form.getStartDay() + " " + "00:00:00";
			String END = form.getEndYear() + "-" + form.getEndMonth() + "-" + form.getEndDay() + " " + "23:59:59";

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addStrValue(form.getPrjId());
			sql.addStrValue(form.getPrjName());
			sql.addStrValue(START);
			sql.addStrValue(END);
			sql.addIntValue(form.getIndirect());
			// 2021.07.29 追加
			sql.addStrValue(form.getOrderName());
			sql.addStrValue(form.getChiefName());
			sql.addStrValue(form.getManagerName());
			sql.addStrValue(form.getRemark());
			sql.addIntValue(form.getTargetB());
			sql.addIntValue(form.getTargetS());
			sql.addIntValue(form.getTargetH());
			sql.addIntValue(form.getTargetJ());
			sql.addIntValue(form.getPrjSid());

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
	* <br>[機  能] プロジェクトを削除する
	* <br>[解  説]
	* <br>[備  考]
	* @param prjSid  プロジェクトID
	* @param con  コネクション
	* @throws SQLException SQL実行時例外
	*/
	public void deleteProject(int prjSid, Connection con) throws SQLException {

		PreparedStatement pstmt = null;

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" delete");
			sql.addSql("   KNT_PRJ");
			sql.addSql(" where");
			sql.addSql("   PRJ_SID = ?");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(prjSid);
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
	 * <p>パラメーターによるグループに所属するユーザーかのチェック
	 * @param grpId  グループID
	 * @param userId ユーザーID
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return 作業実績合計時間
	 */
	public Boolean containsGroupProjectUser(int grpId, int userId, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Boolean ret = false;
		Boolean isGroupUser = false;

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   *");
			sql.addSql(" from");
			sql.addSql("   CMN_BELONGM");
			sql.addSql(" where");
			sql.addSql("   GRP_SID > ?");
			sql.addSql(" and");
			sql.addSql("   USR_SID = ?");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(grpId);
			sql.addIntValue(userId);
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();
			ret = rs.next();

			if (ret){
				// 存在する
				isGroupUser = true;
			} else {
				// 存在しない
				isGroupUser = false;
			}

		} catch (SQLException e) {
			throw e;
		} finally {
			JDBCUtil.closeResultSet(rs);
			JDBCUtil.closeStatement(pstmt);
		}

		return isGroupUser;

	}

	/**
	 * <p>パラメーターによるプロジェクト担当者の存在チェック
	 * @param prjSid  プロジェクトID
	 * @param userId ユーザーID
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return 作業実績合計時間
	 */
	public Boolean containsProjectUser(int prjSid, int userId, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Boolean ret = false;
		Boolean isNew = false;

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   *");
			sql.addSql(" from");
			sql.addSql("   KNT_TNT");
			sql.addSql(" where");
			sql.addSql("   TNT_PRJ_SID = ?");
			sql.addSql(" and");
			sql.addSql("   TNT_USR_SID = ?");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(prjSid);
			sql.addIntValue(userId);
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
	* <br>[機  能] プロジェクト担当者情報を登録する
	* <br>[解  説]
	* <br>[備  考]
	* @param prjSid  プロジェクトID
	* @param userId ユーザーID
	* @param con  コネクション
	* @throws SQLException SQL実行時例外
	*/
	public void insertProjectUser(int prjSid, int userId, Connection con) throws SQLException {

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

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(prjSid);
			sql.addIntValue(userId);
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
	* <br>[機  能] プロジェクト担当者情報を全て削除する
	* <br>[解  説]
	* <br>[備  考]
	* @param prjSid  プロジェクトID
	* @param userId ユーザーID
	* @param con  コネクション
	* @throws SQLException SQL実行時例外
	*/
	public void deleteAllProjectUser(int prjSid, Connection con) throws SQLException {

		PreparedStatement pstmt = null;

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" delete");
			sql.addSql("   KNT_TNT");
			sql.addSql(" where");
			sql.addSql("   TNT_PRJ_SID = ?");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(prjSid);
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
	* <br>[機  能] プロジェクト担当者情報を削除する
	* <br>[解  説]
	* <br>[備  考]
	* @param prjSid  プロジェクトID
	* @param userId ユーザーID
	* @param con  コネクション
	* @throws SQLException SQL実行時例外
	*/
	public void deleteProjectUser(int prjSid, int userId, Connection con) throws SQLException {

		PreparedStatement pstmt = null;

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" delete");
			sql.addSql("   KNT_TNT");
			sql.addSql(" where");
			sql.addSql("   TNT_PRJ_SID = ?");
			sql.addSql(" and");
			sql.addSql("   TNT_USR_SID = ?");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(prjSid);
			sql.addIntValue(userId);
			log__.info(sql.toLogString());
			sql.setParameter(pstmt);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} finally {
			JDBCUtil.closeStatement(pstmt);
		}
	}

}
