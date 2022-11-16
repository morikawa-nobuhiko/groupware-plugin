package jp.groupsession.v2.kintai.Knt100;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
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
import jp.groupsession.v2.kintai.Knt001.Knt001Model;
import jp.groupsession.v2.kintai.Knt001.Knt001Dao;

/**
 * <br>[機  能] Knt100のアクションクラス
 * <br>[解  説]
 * <br>[備  考]
 *
 * @author JTS
 */
public class Knt100Action extends AbstractGsAction {

	/** Logging インスタンス */
	private static Log log__ = LogFactory.getLog(Knt100Action.class);

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
		Knt100Form knt100Form = (Knt100Form) form;
		Knt100Dao dao = new Knt100Dao();

		// コマンド
		String cmd = NullDefault.getString(req.getParameter("CMD"), "");

		//HttpServletRequestからリクエストモデルを取得します
		RequestModel reqMdl = getRequestModel(req);
		//リクエストモデルからログインユーザの情報を取得します。
		BaseUserModel usModel = reqMdl.getSmodel();
		// 表示するユーザーID
		int loginSid = usModel.getUsrsid();

		// 選択された日付
		int y = 9999;
		int m = 12;
		int d = 31;
		if (cmd.startsWith("approval")) {
			String[] ymd = cmd.split(",");
			y= Integer.parseInt(ymd[1]);
			m = Integer.parseInt(ymd[2]);
			d = Integer.parseInt(ymd[3]);
		} else {
			// 選択された日付
			y = Integer.parseInt(req.getParameter("yyyy"));
			m = Integer.parseInt(req.getParameter("mm"));
			d = Integer.parseInt(req.getParameter("dd"));
		}
		req.setAttribute("yyyy", y);
		req.setAttribute("mm", m);
		req.setAttribute("dd", d);

		log__.debug("CMD=" + cmd);
		log__.debug("loginSid=" + loginSid);
		log__.debug("100Y=" + y);
		log__.debug("100M=" + m);
		log__.debug("100D=" + d);

		// グループ一覧を取得
		ArrayList<Knt100GroupModel> grpList = dao.selectGroups(con);
		// グループ一覧をセット
		req.setAttribute("grpList", grpList);

		// 指定されたグループID
		int grpId = knt100Form.getGrpId();

		log__.debug("grpId=" + grpId);

		// 選択グループ
		int grpIndex = -1;
		for(int i=0; i<grpList.size(); i++) {
			Knt100GroupModel gm = (Knt100GroupModel)grpList.get(i);
			if (grpId == gm.getGrpId()) {
				grpIndex = i;
				break;
			}
		}
		req.setAttribute("grpIndex", grpIndex);

		// グループに所属するユーザー情報を取得
		ArrayList<Knt100UserModel> grpUsers = dao.selectGroupUsersInfo(grpId, loginSid, y, m, d, con);
		req.setAttribute("grpUsers", grpUsers);

		log__.debug("grpUsers_count=" + grpUsers.size());

		if (cmd.equals("backMain")) {

			req.setAttribute("dispYear", y);
			req.setAttribute("dispMonth", m);
			req.setAttribute("dispDay", d);

			// メインへ戻る
			forward =  map.findForward("backMain");

		} else if (cmd.equals("init") || grpIndex == -1) {

			// 初期表示
			forward = map.getInputForward();

		} else if (cmd.equals("regist")) {

			for (Knt100UserModel grpUsr : grpUsers){

				// ユーザーID
				int usrSid = grpUsr.getUsrSid();
				log__.debug("usrSid=" + usrSid);

				// チェックされたユーザー
				String checkedUsrSid =  NullDefault.getString(req.getParameter("app" + usrSid), "");
				log__.debug("checkedUsrSid=" + checkedUsrSid);

				// 承認テーブル存在チェック
				boolean approved = dao.containsApprovedUser(usrSid, y, m, d, con);
				log__.debug("approved=" + approved);

				boolean commit = false;
				try {

					// 承認テーブルに存在する場合は承認済みとする
					// 1.チェックされたユーザー＝承認テーブルで存在チェックを行い存在する場合	：何もしない
					// 2.チェックされたユーザー＝承認テーブルで存在チェックを行い存在しない場合	：追加する
					// 3.非チェックのユーザー＝承認テーブルで存在チェックを行い存在する場合		：削除する
					// 4.非チェックのユーザー＝承認テーブルで存在チェックを行い存在しない場合	：何もしない
					if (checkedUsrSid.equals("")){

						// 未承認（3.削除）
						if (approved){
							dao.deleteApprovedUser(usrSid, y, m, d, con);
							log__.debug("未承認（3.削除）");
						}

					} else {

						// 承認（2.追加）
						if (!approved){
							dao.insertApprovedUser(usrSid, y, m, d, con);
							log__.debug("承認（2.追加）");
						}
						log__.debug("usrSid=" + usrSid);
						log__.debug("y=" + y);
						log__.debug("m=" + m);
						log__.debug("d=" + d);

					}

					// コミット
					con.commit();
					commit = true;

				} catch (Exception e) {
					log__.error("承認登録に失敗しました。", e);
					throw e;
				} finally {
					if (!commit) {
						con.rollback();
						log__.debug("rollback）");
					}
				}

				// 登録後メッセージを表示
				ActionErrors errors = new ActionErrors();
				errors.add("registed", new ActionMessage("registed"));
				addErrors(req, errors);

				// 再表示
				forward = map.findForward("registed");

			}

			// 以下は「Knt001.jsp」の表示に必要な値

			// 作業実績時間取得（月）
//			Knt001Dao ui = new Knt001Dao();
//			ArrayList<Knt001Model> uiList = ui.selectDayTotalTime(usModel.getUsrsid(), y, m, con);
//			HashMap<Integer, Double> uiMap = new HashMap<Integer, Double>();
//			for(int i = 0; i < uiList.size(); i++)
//			{
//				Knt001Model model = uiList.get(i);
//				uiMap.put(model.getWrkDay(), model.getWrkTime());
//			}
//			// 未登録の日の実績時間を追加
//			for(int i = 0; i <= 31; i++)
//			{
//				if (uiMap.containsKey(i)){
//				} else {
//					uiMap.put(i, 0.0);
//				}
//			}
//			req.setAttribute("dispYear", y);
//			req.setAttribute("dispMonth", m);
//			req.setAttribute("dispDay", d);
//			req.setAttribute("uiMap", uiMap);

//			// 登録後カレンダーに戻る
//			forward = map.findForward("backMain");

		} else {

			// 初期表示
			forward = map.getInputForward();

		}

		return forward;

	}

}
