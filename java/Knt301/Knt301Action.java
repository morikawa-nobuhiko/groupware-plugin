package jp.groupsession.v2.kintai.Knt301;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
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
import jp.groupsession.v2.kintai.Knt001.Knt001Dao;

/**
 * <br>[機  能] Knt300のアクションクラス
 * <br>[解  説]
 * <br>[備  考]
 *
 * @author JTS
 */
public class Knt301Action extends AbstractGsAction {

	/** Logging インスタンス */
	private static Log log__ = LogFactory.getLog(Knt301Action.class);

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
		Knt301Form knt301Form = (Knt301Form) form;
		Knt001Dao dao001 = new Knt001Dao();

		//HttpServletRequestからリクエストモデルを取得します
		RequestModel reqMdl = getRequestModel(req);
		//リクエストモデルからログインユーザの情報を取得します。
		BaseUserModel usModel = reqMdl.getSmodel();

		// コマンド
		String cmd = NullDefault.getString(req.getParameter("CMD"), "");

		if(cmd.startsWith("edit")) {

			// 302へ遷移
			return map.findForward("edit");

		} else {

			// 300→301（300からの検索条件が渡される）
			int prjSid = knt301Form.getPrjSid();
			String prjName = NullDefault.getString(knt301Form.getPrjName(), "");
			int sy = knt301Form.getStartYear();
			int sm = knt301Form.getStartMonth();
			int sd = knt301Form.getStartDay();
			int ey = knt301Form.getEndYear();
			int em = knt301Form.getEndMonth();
			int ed = knt301Form.getEndDay();
			String orderName = NullDefault.getString(knt301Form.getOrderName(), "");

			// 300から検索条件の取得
			int ptn = Integer.parseInt(req.getParameter("search"));

			// 全件検索（終了期間が9999年12月31日）
			String all = NullDefault.getString((String)req.getAttribute("all"), "");
			if (all.equals("all")) {
				ey = 9999;
				em = 12;
				ed = 31;
				ptn = 3;
			}

			log__.debug("ptn=" + ptn);

			ArrayList<Knt301Model> prjList;
			Knt301Dao dao = new Knt301Dao();

			switch (ptn) {
			case 0:
				// IDで検索
				prjList = dao.selectProjectIdList(knt301Form.getPrjId(), con);
				break;
			case 1:
				// 名称で検索
				prjList = dao.selectProjectList(knt301Form.getPrjName(), con);
				break;
			case 2:
				// 開始期間で検索
				prjList = dao.selectStartDateProjectList(sy,sm,sd,con);
				break;
			case 3:
				// 終了期間で検索
				prjList = dao.selectEndDateProjectList(ey,em,ed, con);
				break;
			case 4:
				// 発注者で検索
				prjList = dao.selectOrderNameProjectList(knt301Form.getOrderName(), con);
				break;
			case 5:
				// 対象で検索
				prjList = dao.selectTargetProjectList(knt301Form.getTargetB(),knt301Form.getTargetS(),knt301Form.getTargetH(),knt301Form.getTargetJ(), con);
				break;
			case 6:
				// 主任担当者で検索
				prjList = dao.selectChiefNameProjectList(knt301Form.getChiefName(), con);
				break;
			case 7:
				// 担当者で検索
				prjList = dao.selectManagerNameProjectList(knt301Form.getManagerName(), con);
				break;
			default:
				// 全件検索
				prjList = dao.selectProjectList(con);
				break;
			}

			req.setAttribute("prjList", prjList);

		}

		// ユーザーが管理グループに所属するか
		boolean manager = dao001.containsManageGroup(1, usModel.getUsrsid(), con);
		req.setAttribute("manager", manager);

		return map.getInputForward();

	}

}
