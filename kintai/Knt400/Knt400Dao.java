package jp.groupsession.v2.kintai.Knt400;

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

public class Knt400Dao extends AbstractDao {

	/** Logging インスタンス */
	private static Log log__ = LogFactory.getLog(Knt400Dao.class);

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
	 * <p>パラメーターによる作業一覧の取得（有給除外）
	 * @param con    DB Connection
	 * @throws SQLException SQL実行例外
	 * @return ArrayList<Knt400Model>
	 */
	public ArrayList<Knt400Model> selectWorkList(Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Knt400Model> ret = new ArrayList<Knt400Model>();

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   *");
			sql.addSql(" from");
			sql.addSql("   KNT_WRK");
			sql.addSql(" where");
			sql.addSql("   WRK_ID != ?");
			sql.addSql(" ORDER BY WRK_ID");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(99);
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Knt400Model model = new Knt400Model();
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

}
