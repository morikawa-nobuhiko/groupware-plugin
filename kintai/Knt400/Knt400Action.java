package jp.groupsession.v2.kintai.Knt400;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jp.co.sjts.util.NullDefault;
import jp.co.sjts.util.date.UDate;
import jp.co.sjts.util.jdbc.JDBCUtil;
import jp.co.sjts.util.jdbc.SqlBuffer;
import jp.groupsession.v2.struts.AbstractGsAction;
import jp.groupsession.v2.cmn.GSConst;
import jp.groupsession.v2.cmn.GSConstSchedule;
import jp.groupsession.v2.cmn.config.PluginConfig;
import jp.groupsession.v2.cmn.dao.GroupModel;
import jp.groupsession.v2.cmn.dao.BaseUserModel;
import jp.groupsession.v2.cmn.dao.UsidSelectGrpNameDao;
import jp.groupsession.v2.cmn.dao.BaseUserModel;
import jp.groupsession.v2.cmn.model.RequestModel;
import jp.groupsession.v2.kintai.Knt400.Knt400Dao;
import jp.groupsession.v2.kintai.Knt401.Knt401Dao;
import jp.groupsession.v2.kintai.Knt401.Knt401Model;
import jp.groupsession.v2.cmn.cmn999.Cmn999Form;

/**
 * <br>[機  能] Knt400のアクションクラス
 * <br>[解  説]
 * <br>[備  考]
 *
 * @author JTS
 */
public class Knt400Action extends AbstractGsAction {

	/** Logging インスタンス */
	private static Log log__ = LogFactory.getLog(Knt400Action.class);

	/**
	* <br>[機  能] アクションを実行する
	* <br>[解  説]
	* <br>[備  考]
	* @param map ActionMapping
	* @param form ActionForm
	* @param req HttpServletRequest
	* @param res HttpServletResponse
	* @param con DB Connection
	* @return ActionForward
	* @throws Exception 実行時例外
	*/
	public ActionForward executeAction(ActionMapping map,
										ActionForm form,
										HttpServletRequest req,
										HttpServletResponse res,
										Connection con)
	throws Exception {

		ActionForward forward = null;
		Knt400Form Knt400Form = (Knt400Form) form;
		Knt400Dao dao400 = new Knt400Dao();
		Knt401Dao dao401 = new Knt401Dao();

		// コマンド
		String cmd = NullDefault.getString(req.getParameter("CMD"), "");

		if(cmd.startsWith("edit")) {

			// 400へ遷移
			return map.findForward("edit");

		} else {

			// 作業一覧取得
			ArrayList<Knt400Model> wrkList = dao400.selectWorkList(con);
			req.setAttribute("wrkList", wrkList);

			// 初期表示
			forward = map.getInputForward();

		}

//		return map.getInputForward();
		return forward;

	}

}
