package jp.groupsession.v2.kintai.Knt401;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.Calendar;
import java.lang.String;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.util.MessageResources;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import jp.co.sjts.util.NullDefault;
import jp.co.sjts.util.jdbc.JDBCUtil;
import jp.co.sjts.util.jdbc.SqlBuffer;
import jp.co.sjts.util.struts.StrutsUtil;
import jp.co.sjts.util.StringUtil;
import jp.co.sjts.util.ValidateUtil;
import jp.groupsession.v2.struts.AbstractGsAction;
import jp.groupsession.v2.cmn.GSConst;
import jp.groupsession.v2.cmn.GSConstSchedule;
import jp.groupsession.v2.cmn.config.PluginConfig;
import jp.groupsession.v2.cmn.dao.GroupModel;
import jp.groupsession.v2.cmn.dao.BaseUserModel;
import jp.groupsession.v2.cmn.dao.UsidSelectGrpNameDao;
import jp.groupsession.v2.cmn.dao.BaseUserModel;
import jp.groupsession.v2.cmn.model.RequestModel;
import jp.groupsession.v2.kintai.Knt401.Knt401Model;
import jp.groupsession.v2.kintai.Knt401.Knt401Dao;
import jp.groupsession.v2.cmn.cmn999.Cmn999Form;

/**
 * <br>[機  能] Knt401のアクションクラス
 * <br>[解  説]
 * <br>[備  考]
 *
 * @author JTS
 */
public class Knt401Action extends AbstractGsAction {

	/** Logging インスタンス */
	private static Log log__ = LogFactory.getLog(Knt401Action.class);

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
		ActionErrors errors = new ActionErrors();
		Knt401Form Knt401Form = (Knt401Form) form;
		Knt401Dao dao = new Knt401Dao();

		// コマンド
		String cmd = NullDefault.getString(req.getParameter("CMD"), "");

		log__.debug("CMD=" + cmd);

		// 更新フラグ
		int isEdit = 0;

		if (cmd.equals("insert") || cmd.equals("update")) {

			// 入力チェック
			if (!cmd.equals("")){

				errors = Knt401Form.validateCheck();

				// 編集フラグ
				if (cmd.equals("update")) {
					isEdit = 1;
				}

				if (!errors.isEmpty()) {
					addErrors(req, errors);
					req.setAttribute("isEdit", isEdit);
					req.setAttribute("wrkId", Knt401Form.getWrkId());
					req.setAttribute("wrkName", Knt401Form.getWrkName());
					return map.getInputForward();
				}
			}

			// 登録ボタンクリック
			if (cmd.equals("insert")) {
				forward = doInsert(map, Knt401Form , req, res, con);
			} else if (cmd.equals("update")) {
				forward = doUpdate(map, Knt401Form , req, res, con);
			}

		} else if (cmd.equals("inserted") || cmd.equals("updated")) {

			// 登録・更新完了メッセージ
			forward = __doRegistCompDsp(map, Knt401Form, req, res, con);

		} else if (cmd.startsWith("edit")) {

			// 編集ボタンからの遷移
			String editWrkSid = cmd.replace("edit","");
			int wrkId = Integer.parseInt(editWrkSid);

			// 編集フラグON
			isEdit = 1;

			log__.debug("WrkId=" + editWrkSid);

			// 401へ渡す
			Knt401Model model = dao.selectWork(wrkId, con);
			req.setAttribute("wrkId", model.getWrkId());
			req.setAttribute("wrkName", model.getWrkName());

			// debug log
			log__.debug("wrkId=" + model.getWrkId());
			log__.debug("wrkName=" + model.getWrkName());

			// 編集表示
			forward = map.getInputForward();

		} else if (cmd.startsWith("delete")) {

			log__.debug("ConfWrkId=" + Knt401Form.getWrkId());
			log__.debug("ConfDelWrkId=" + Knt401Form.getDelWrkId());

			// 削除ボタンからの確認画面表示
			forward = __setDeleteConfirmMsgPageParam(map, req, Knt401Form, con);

		} else if (cmd.equals("delWrkDecision")) {

			// 削除ボタンからの遷移
			int wrkId = Knt401Form.getWrkId();

			log__.debug("DelWrkId=" + wrkId);

			boolean commit = false;
			try {

				// プロジェクトを削除
				Knt401Dao dao401 = new Knt401Dao();
				dao401.deleteProject(wrkId, con);

				// コミット
				con.commit();
				commit = true;

			} catch (Exception e) {
				log__.error("プロジェクトの削除に失敗", e);
				throw e;
			} finally {
				if (!commit) {
					con.rollback();
				}
			}

			// 400へ遷移
			forward = map.findForward("back400");

		} else {

			// プロジェクトを検索
			Knt401Dao dao401 = new Knt401Dao();
			int emptyId = dao401.emptyWorkId(con);

			// 初期値
			req.setAttribute("wrkId", emptyId);
			req.setAttribute("wrkName", "");

			// 新規登録入力
			forward = map.getInputForward();

		}

		// 編集フラグセット
		req.setAttribute("isEdit", isEdit);

		return forward;

	}

	/**
	 * <br>[機  能] 追加処理を行う
	 * <br>[解  説]
	 * <br>[備  考]
	 * @param map				マップ
	 * @param form				フォーム
	 * @param usrList			ユーザー一覧
	 * @param req				リクエスト
	 * @param res				レスポンス
	 * @param con				コネクション
	 * @return ActionForward	フォワード
	 * @throws Exception		実行例外
	 */
	private ActionForward doInsert(ActionMapping map,
									Knt401Form form,
									HttpServletRequest req,
									HttpServletResponse res,
									Connection con)
	throws Exception {

		// インスタンス
		Knt401Dao dao = new Knt401Dao();

		boolean commit = false;
		try {

			// 追加
			dao.insertProject(form, con);

			// コミット
			con.commit();
			commit = true;

		} catch (Exception e) {
			log__.error("プロジェクトの登録に失敗", e);
			throw e;
		} finally {
			if (!commit) {
				con.rollback();
			}
		}

		// 追加
		return map.findForward("inserted");

	}

	/**
	 * <br>[機  能] 更新処理を行う
	 * <br>[解  説]
	 * <br>[備  考]
	 * @param map				マップ
	 * @param form				フォーム
	 * @param usrList			ユーザー一覧
	 * @param req				リクエスト
	 * @param res				レスポンス
	 * @param con				コネクション
	 * @return ActionForward	フォワード
	 * @throws Exception 実行例外
	 */
	private ActionForward doUpdate(ActionMapping map,
									Knt401Form form,
									HttpServletRequest req,
									HttpServletResponse res,
									Connection con)
	throws Exception {

		// インスタンス
		Knt401Dao dao = new Knt401Dao();

		boolean commit = false;
		try {

			// 更新
			dao.updateProject(form, con);

			// コミット
			con.commit();
			commit = true;

		} catch (Exception e) {
			log__.error("プロジェクトの更新に失敗", e);
			throw e;
		} finally {
			if (!commit) {
				con.rollback();
			}
		}

		// 更新
		return map.findForward("updated");

	}

	/**
	 * <br>登録完了画面設定
	 * @param map アクションマッピング
	 * @param form アクションフォーム
	 * @param req リクエスト
	 * @param res レスポンス
	 * @param con コネクション
	 * @return ActionForward
	 */
	private ActionForward __doRegistCompDsp(ActionMapping map, 
											Knt401Form form,
											HttpServletRequest req,
											HttpServletResponse res,
											Connection con)
	{

		ActionForward forward = null;
		Cmn999Form cmn999Form = new Cmn999Form();
		ActionForward urlForward = null;

		//スケジュール登録完了画面パラメータの設定
		MessageResources msgRes = getResources(req);
		cmn999Form.setType(Cmn999Form.TYPE_OK);
		cmn999Form.setIcon(Cmn999Form.ICON_INFO);
		cmn999Form.setWtarget(Cmn999Form.WTARGET_BODY);

		//メッセージセット
		String msgState = "touroku.kakunin.once";
		List<String> msgs = new ArrayList<String>();
//		msgs.add("ＩＤ:" + form.getWrkId());
		msgs.add("名称:" + form.getWrkName());
		String msg = String.join("<br>", msgs);
		cmn999Form.setMessage(msgRes.getMessage("touroku.kanryo.object", msg));
		req.setAttribute("cmn999Form", cmn999Form);

		// 遷移先
		urlForward = map.findForward("back400");
		cmn999Form.setUrlOK(urlForward.getPath() + "?CMD=inserted");

		forward = map.findForward("gf_msg");
		return forward;
	}

	/**
	 * <br>[機  能] 登録確認の共通メッセージ画面遷移時の設定
	 * <br>[解  説]
	 * <br>[備  考]
	 * @param map マッピング
	 * @param req リクエスト
	 * @param form アクションフォーム
	 * @param con コネクション
	 * @return ActionForward フォワード
	 * @throws SQLException SQL例外発生
	 */
	private ActionForward __setRegistConfirmMsgPageParam(
		ActionMapping map,
		HttpServletRequest req,
		Knt401Form form,
		Connection con) throws SQLException {

		Cmn999Form cmn999Form = new Cmn999Form();
		ActionForward urlForward = null;

		MessageResources msgRes = getResources(req);
		cmn999Form.setIcon(Cmn999Form.ICON_INFO);
		cmn999Form.setWtarget(Cmn999Form.WTARGET_BODY);

		urlForward = map.findForward("back401");
		cmn999Form.setType(Cmn999Form.TYPE_OKCANCEL);
		cmn999Form.setUrlCancel(urlForward.getPath());

		cmn999Form.setUrlOK(urlForward.getPath() + "?CMD=regWrkDecision");

		//メッセージセット
		String msgState = "touroku.kakunin.once";
		List<String> msgs = new ArrayList<String>();
//		msgs.add("ＩＤ:" + form.getWrkId());
		msgs.add("名称:" + form.getWrkName());
		String msg = String.join("<br>", msgs);
		cmn999Form.setMessage(msgRes.getMessage(msgState, msg));

		cmn999Form.addHiddenParam("wrkId", form.getWrkId());
		cmn999Form.addHiddenParam("delWrkId", form.getWrkId());

		req.setAttribute("cmn999Form", cmn999Form);

		return map.findForward("gf_msg");
	}

	/**
	 * <br>[機  能] 削除確認の共通メッセージ画面遷移時の設定
	 * <br>[解  説]
	 * <br>[備  考]
	 * @param map マッピング
	 * @param req リクエスト
	 * @param form アクションフォーム
	 * @param con コネクション
	 * @return ActionForward フォワード
	 * @throws SQLException SQL例外発生
	 */
	private ActionForward __setDeleteConfirmMsgPageParam(
		ActionMapping map,
		HttpServletRequest req,
		Knt401Form form,
		Connection con) throws SQLException {

		Cmn999Form cmn999Form = new Cmn999Form();
		ActionForward urlForward = null;

		MessageResources msgRes = getResources(req);
		cmn999Form.setIcon(Cmn999Form.ICON_INFO);
		cmn999Form.setWtarget(Cmn999Form.WTARGET_BODY);

		urlForward = map.findForward("back401");
		cmn999Form.setType(Cmn999Form.TYPE_OKCANCEL);
		cmn999Form.setUrlCancel(urlForward.getPath());

		cmn999Form.setUrlOK(urlForward.getPath() + "?CMD=delWrkDecision");

		//メッセージセット
		String msgState = "sakujo.kakunin.once";
		List<String> msgs = new ArrayList<String>();
//		msgs.add("ＩＤ:" + form.getWrkId());
		msgs.add("名称:" + form.getWrkName());
		String msg = String.join("<br>", msgs);
		cmn999Form.setMessage(msgRes.getMessage(msgState, msg));

		cmn999Form.addHiddenParam("wrkId", form.getWrkId());
		cmn999Form.addHiddenParam("delWrkId", form.getWrkId());

		req.setAttribute("cmn999Form", cmn999Form);

		return map.findForward("gf_msg");
	}

}
