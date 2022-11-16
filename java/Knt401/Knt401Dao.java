package jp.groupsession.v2.kintai.Knt401;

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

public class Knt401Dao extends AbstractDao {

	/** Logging インスタンス */
	private static Log log__ = LogFactory.getLog(Knt401Dao.class);

	/**
	 * <p>空き作業IDの取得
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return ArrayList<Knt301Model>
	 */
	public Integer emptyWorkId(Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Integer ret = -1;

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   max(WRK_ID) as MAX_WRK_ID");
			sql.addSql(" from");
			sql.addSql("   KNT_WRK");

			pstmt = con.prepareStatement(sql.toSqlString());
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				ret = rs.getInt("MAX_WRK_ID") + 1;
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
	 * <p>パラメーターによる作業の取得
	 * @param wrkId int
	 * @param con    DB Connection
	 * @throws SQLException SQL実行例外
	 * @return ArrayList<Knt401Model>
	 */
	public Knt401Model selectWork(int wrkId, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Knt401Model ret = new Knt401Model();

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   *");
			sql.addSql(" from");
			sql.addSql("   KNT_WRK");
			sql.addSql(" where");
			sql.addSql("   WRK_ID = ?");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(wrkId);
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Knt401Model model = new Knt401Model();
				model.setWrkId(rs.getInt("WRK_ID"));
				model.setWrkName(rs.getString("WRK_NAME"));
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
	* <br>[機  能] 作業情報を登録する
	* <br>[解  説]
	* <br>[備  考]
	* @param form フォーム
	* @param con  コネクション
	* @throws SQLException SQL実行時例外
	*/
	public void insertProject(Knt401Form form, Connection con) throws SQLException {

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
			sql.addSql("   ?,");
			sql.addSql(" )");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(form.getWrkId());
			sql.addStrValue(form.getWrkName());
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
	* <br>[機  能] 作業情報を更新する
	* <br>[解  説]
	* <br>[備  考]
	* @param form フォーム
	* @param con  コネクション
	* @throws SQLException SQL実行時例外
	*/
	public void updateProject(Knt401Form form, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		int count = 0;

		try {
			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" update");
			sql.addSql("   KNT_WRK");
			sql.addSql(" set ");
			sql.addSql("   WRK_NAME=?");
			sql.addSql(" where");
			sql.addSql("   WRK_ID = ?");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addStrValue(form.getWrkName());
			sql.addIntValue(form.getWrkId());

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
	* <br>[機  能] 作業を削除する
	* <br>[解  説]
	* <br>[備  考]
	* @param wrkId  作業ID
	* @param con  コネクション
	* @throws SQLException SQL実行時例外
	*/
	public void deleteProject(int wrkId, Connection con) throws SQLException {

		PreparedStatement pstmt = null;

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" delete");
			sql.addSql("   KNT_WRK");
			sql.addSql(" where");
			sql.addSql("   WRK_ID = ?");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(wrkId);
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
