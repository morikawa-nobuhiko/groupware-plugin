package jp.groupsession.v2.kintai.Knt300;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
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
import jp.groupsession.v2.struts.AbstractGsAction;
import jp.groupsession.v2.cmn.GSConst;
import jp.groupsession.v2.cmn.GSConstSchedule;
import jp.groupsession.v2.cmn.config.PluginConfig;
import jp.groupsession.v2.cmn.dao.GroupModel;
import jp.groupsession.v2.cmn.dao.BaseUserModel;
import jp.groupsession.v2.cmn.dao.UsidSelectGrpNameDao;
import jp.groupsession.v2.cmn.dao.BaseUserModel;
import jp.groupsession.v2.cmn.model.RequestModel;

/**
 * <br>[機  能] Knt300のアクションクラス
 * <br>[解  説]
 * <br>[備  考]
 *
 * @author JTS
 */
public class Knt300Action extends AbstractGsAction {

	/** Logging インスタンス */
	private static Log log__ = LogFactory.getLog(Knt300Action.class);

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
		Knt300Form knt300Form = (Knt300Form) form;

		// コマンド
		String cmd = NullDefault.getString(req.getParameter("CMD"), "");

		log__.debug("CMD=" + cmd);

		if (cmd.equals("select")) {

			// 300→301

			// 検索値
			int ptn = Integer.parseInt(req.getParameter("search"));
			int prjSid = knt300Form.getPrjSid();
			String prjId = knt300Form.getPrjId();
			String prjName = NullDefault.getString(knt300Form.getPrjName(), "");
			int sy = knt300Form.getStartYear();
			int sm = knt300Form.getStartMonth();
			int sd = knt300Form.getStartDay();
			int ey = knt300Form.getEndYear();
			int em = knt300Form.getEndMonth();
			int ed = knt300Form.getEndDay();
			String orderName = knt300Form.getOrderName();

			// 検索値を301へ渡す
			req.setAttribute("prjSid", prjSid);
			req.setAttribute("prjId", prjId);
			req.setAttribute("prjName", prjName);
			req.setAttribute("startY", sy);
			req.setAttribute("startM", sm);
			req.setAttribute("startD", sd);
			req.setAttribute("endY", ey);
			req.setAttribute("endM", em);
			req.setAttribute("endD", ed);
			req.setAttribute("orderName", orderName);

			// 301へ遷移
			forward = map.findForward("select");

		} else if (cmd.equals("selectAll")) {

			// 300→301（全件検索）
			req.setAttribute("all", "all");

			// 301へ遷移
			forward = map.findForward("selectAll");

		} else if (cmd.equals("newProject")) {

			// 302へ遷移
			forward = map.findForward("regist");

		} else {

			// 初期表示
			forward = map.getInputForward();

		}

		return forward;

	}

}
