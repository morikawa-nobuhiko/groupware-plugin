package jp.groupsession.v2.kintai.Knt302;

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
import jp.groupsession.v2.kintai.Knt301.Knt301Model;
import jp.groupsession.v2.kintai.Knt301.Knt301Dao;
import jp.groupsession.v2.cmn.cmn999.Cmn999Form;

/**
 * <br>[機  能] Knt302のアクションクラス
 * <br>[解  説]
 * <br>[備  考]
 *
 * @author JTS
 */
public class Knt302Action extends AbstractGsAction {

	/** Logging インスタンス */
	private static Log log__ = LogFactory.getLog(Knt302Action.class);

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
		Knt302Form knt302Form = (Knt302Form) form;
		Knt302Dao dao = new Knt302Dao();

		// コマンド
		String cmd = NullDefault.getString(req.getParameter("CMD"), "");

		log__.debug("CMD=" + cmd);

		// ユーザー一覧を取得
		ArrayList<Knt302UserModel> usrList = dao.selectUsers(con);
		// ユーザー一覧をセット
		req.setAttribute("usrList", usrList);
		// プロジェクト担当者一覧
		ArrayList<Integer> prjUsrList = new ArrayList<Integer>();

		// 更新フラグ
		int isEdit = 0;

		if (cmd.equals("insert") || cmd.equals("update")) {

			// 入力チェック
			if (!cmd.equals("")){
				errors = knt302Form.validateCheck();
				// 担当者チェック
				boolean isUser = false;
				for (Knt302UserModel usr : usrList){
					// チェックされた担当者
					String checkedUsrSid =  NullDefault.getString(req.getParameter("user" + usr.getUsrSid()), "");
					if (checkedUsrSid != ""){
						isUser = true;
//						break;
						prjUsrList.add(Integer.parseInt(checkedUsrSid));
					}
				}
				if (prjUsrList.size() == 0) {
					errors.add("users", new ActionMessage("担当者を選択してください。"));
				}

				log__.debug("PrjId=" + knt302Form.getPrjId());

				// プロジェクトID存在チェック
				if (cmd.equals("insert")) { 
					boolean isNewProject = dao.containsProject(knt302Form.getPrjId(), con);
					if (!isNewProject) {
						errors.add("errPrjId", new ActionMessage("既に存在するプロジェクトIDです。"));
					}
				}

				// 日付チェック
				Calendar cal=Calendar.getInstance();
				cal.set(knt302Form.getStartYear(),knt302Form.getStartMonth()-1,1);
				int intDays=cal.getActualMaximum(Calendar.DATE);
				if (intDays < knt302Form.getStartDay()) {
					errors.add("errStartDate", new ActionMessage("日付が正しくありません。"));
				}
				cal.clear();
				cal.set(knt302Form.getEndYear(),knt302Form.getEndMonth()-1,1);
				intDays=cal.getActualMaximum(Calendar.DATE);
				if (intDays < knt302Form.getEndDay()) {
					errors.add("errEndDate", new ActionMessage("日付が正しくありません。"));
				}

				// 編集フラグ
				if (cmd.equals("update")) {
					isEdit = 1;
				}

				if (!errors.isEmpty()) {
					addErrors(req, errors);
					req.setAttribute("isEdit", isEdit);
					req.setAttribute("prjSid", knt302Form.getPrjSid());
					req.setAttribute("prjId", knt302Form.getPrjId());
					req.setAttribute("prjName", knt302Form.getPrjName());
					req.setAttribute("errPrjId", knt302Form.getPrjName());
					req.setAttribute("startY", knt302Form.getStartYear());
					req.setAttribute("startM", knt302Form.getStartMonth());
					req.setAttribute("startD", knt302Form.getStartDay());
					req.setAttribute("endY", knt302Form.getEndYear());
					req.setAttribute("endM", knt302Form.getEndMonth());
					req.setAttribute("endD", knt302Form.getEndDay());
					req.setAttribute("indirect", knt302Form.getIndirect());
					req.setAttribute("usrList", usrList);
					req.setAttribute("projectUsrList", prjUsrList);
					req.setAttribute("orderName", knt302Form.getOrderName());
					req.setAttribute("chiefName", knt302Form.getChiefName());
					req.setAttribute("managerName", knt302Form.getManagerName());
					req.setAttribute("remark", knt302Form.getRemark());
					req.setAttribute("targetB", knt302Form.getTargetB());
					req.setAttribute("targetS", knt302Form.getTargetS());
					req.setAttribute("targetH", knt302Form.getTargetH());
					req.setAttribute("targetJ", knt302Form.getTargetJ());
					log__.debug("登録ボタン=エラーあり");
					return map.getInputForward();
				}
			}

			log__.debug("indirect=" + knt302Form.getIndirect());
			log__.debug("登録ボタン=エラーなし");

			// 登録ボタンクリック
			if (cmd.equals("insert")) {
				forward = doInsert(map, knt302Form , usrList, req, res, con);
			} else if (cmd.equals("update")) {
				forward = doUpdate(map, knt302Form , usrList, req, res, con);
			}

		} else if (cmd.equals("inserted") || cmd.equals("updated")) {

			// 登録・更新完了メッセージ
			forward = __doRegistCompDsp(map, knt302Form, req, res, con);

		} else if (cmd.startsWith("edit")) {

			// 編集ボタンからの遷移
			String editPrjSid = cmd.replace("edit","");
			int prjSid = Integer.parseInt(editPrjSid);

			log__.debug("PrjSid=" + editPrjSid);

			// プロジェクト一覧を検索
			Knt301Dao dao301 = new Knt301Dao();
			ArrayList<Knt301Model> prjList = dao301.selectProjectList(prjSid, con);

			if (0 < prjList.size()){

				// 編集フラグON
				isEdit = 1;

				// プロジェクト担当者一覧を取得
				prjUsrList = dao.selectProjectUsers(prjSid, con);

				// 検索値を302へ渡す
				Knt301Model model = prjList.get(0);
				String prjId = model.getPrjId();
				int sy = model.getStartYear();
				int sm = model.getStartMonth();
				int sd = model.getStartDay();
				int ey = model.getEndYear();
				int em = model.getEndMonth();
				int ed = model.getEndDay();
				int id = model.getIndirect();
				req.setAttribute("prjSid", prjSid);
				req.setAttribute("prjId", prjId);
				req.setAttribute("prjName", model.getPrjName());
				req.setAttribute("startY", sy);
				req.setAttribute("startM", sm);
				req.setAttribute("startD", sd);
				req.setAttribute("endY", ey);
				req.setAttribute("endM", em);
				req.setAttribute("endD", ed);
				req.setAttribute("indirect", id);
				req.setAttribute("projectUsrList", prjUsrList);
				req.setAttribute("orderName", model.getOrderName());
				req.setAttribute("chiefName", model.getChiefName());
				req.setAttribute("managerName", model.getManagerName());
				req.setAttribute("remark", model.getRemark());
				req.setAttribute("targetB", model.getTargetB());
				req.setAttribute("targetS", model.getTargetS());
				req.setAttribute("targetH", model.getTargetH());
				req.setAttribute("targetJ", model.getTargetJ());

				// debug log
				log__.debug("prjSid=" + prjSid);
				log__.debug("prjId=" + prjId);
				log__.debug("prjName=" + model.getPrjName());
				log__.debug("startY=" + sy);
				log__.debug("startY=" + sm);
				log__.debug("startY=" + sd);
				log__.debug("endY=" + ey);
				log__.debug("endM=" + em);
				log__.debug("endD=" + ed);
				log__.debug("indirect=" + id);
				log__.debug("targetB=" + model.getTargetB());
				log__.debug("targetS=" + model.getTargetS());
				log__.debug("targetH=" + model.getTargetH());
				log__.debug("targetJ=" + model.getTargetJ());
				log__.debug("usrList=" + usrList.size());
				log__.debug("projectUsrList=" + prjUsrList.size());
				for(int i=0; i<prjUsrList.size(); i++) {
					log__.debug("projectUsr=" + prjUsrList.get(i));
				}

			} else {

				// デバッグ用
				req.setAttribute("prjSid", 1);
				req.setAttribute("prjName", "");
				req.setAttribute("startY", 2021);
				req.setAttribute("startM", 1);
				req.setAttribute("startD", 1);
				req.setAttribute("endY", 2021);
				req.setAttribute("endM", 12);
				req.setAttribute("endD", 31);
				req.setAttribute("indirect", 0);

			}

			// 編集表示
			forward = map.getInputForward();

		} else if (cmd.startsWith("delete")) {

			log__.debug("ConfPrjSid=" + knt302Form.getPrjSid());
			log__.debug("ConfDelPrjSid=" + knt302Form.getDelPrjSid());

			// 削除ボタンからの確認画面表示
			forward = __setDeleteConfirmMsgPageParam(map, req, knt302Form, con);

		} else if (cmd.equals("delPrjDecision")) {

			// 削除ボタンからの遷移
			int prjSid = knt302Form.getPrjSid();

			log__.debug("DelPrjSid=" + prjSid);

			boolean commit = false;
			try {

				// プロジェクトを削除
				Knt302Dao dao302 = new Knt302Dao();
				dao302.deleteProject(prjSid, con);

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

			// 300へ遷移
			forward = map.findForward("back300");

		} else {

			// プロジェクトを検索
			Knt301Dao dao301 = new Knt301Dao();
			int emptySid = dao301.emptyProjectId(con);
			String emptyId = String.format("%04d", emptySid);

			// 現在の日付を取得
			Calendar cal=Calendar.getInstance();
			int intYear=cal.get(Calendar.YEAR);
			int intMonth=cal.get(Calendar.MONTH) + 1;
			int intDay=cal.get(Calendar.DATE);
			int intDays=cal.getActualMaximum(Calendar.DATE);

			// 初期値
			req.setAttribute("prjSid", emptySid);
			req.setAttribute("prjId", "");
			req.setAttribute("prjName", "");
			req.setAttribute("startY", intYear);
			req.setAttribute("startM", intMonth);
			req.setAttribute("startD", intDay);
			req.setAttribute("endY", intYear);
			req.setAttribute("endM", intMonth);
			req.setAttribute("endD", intDays);
			req.setAttribute("indirect", 0);
			req.setAttribute("projectUsrList", prjUsrList);

			// 2021.07.29 追加
			req.setAttribute("orderName", "");
			req.setAttribute("chiefName", "");
			req.setAttribute("managerName", "");
			req.setAttribute("remark", "");
			req.setAttribute("targetB", 0);
			req.setAttribute("targetS", 0);
			req.setAttribute("targetH", 0);
			req.setAttribute("targetJ", 0);

			log__.debug("startY=" + intYear);
			log__.debug("startM=" + intMonth);
			log__.debug("startD=" + intDays);
			log__.debug("endY=" + intYear);
			log__.debug("endM=" + intMonth);
			log__.debug("endD=" + intDays);

			// 新規登録
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
									Knt302Form form,
									ArrayList<Knt302UserModel> usrList,
									HttpServletRequest req,
									HttpServletResponse res,
									Connection con)
	throws Exception {

		// インスタンス
		Knt302Dao dao = new Knt302Dao();

		// プロジェクト存在チェック
		boolean isNewProject = dao.containsProject(form.getPrjId(), con);
//		boolean isNewProject = dao.containsProject(form.getPrjSid(),form.getPrjId(), con);

		boolean commit = false;
		try {

			// 追加
			dao.insertProject(form, con);

			// プロジェクトの担当者を全て削除する
			dao.deleteAllProjectUser(form.getPrjSid(), con);

			for (Knt302UserModel usr : usrList){
				// チェックされた担当者
				String checkedUsrSid =  NullDefault.getString(req.getParameter("user" + usr.getUsrSid()), "");
				if (checkedUsrSid != ""){
					// プロジェクト担当者存在チェック
					boolean isNewUser = dao.containsProjectUser(form.getPrjSid(), usr.getUsrSid() , con);
					// 追加
					if (isNewUser){
						dao.insertProjectUser(form.getPrjSid(), usr.getUsrSid(), con);
					}
				}
			}

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
									Knt302Form form,
									ArrayList<Knt302UserModel> usrList,
									HttpServletRequest req,
									HttpServletResponse res,
									Connection con)
	throws Exception {

		// インスタンス
		Knt302Dao dao = new Knt302Dao();

		boolean commit = false;
		try {

			// 更新
			dao.updateProject(form, con);

			// プロジェクトの担当者を全て削除する
			dao.deleteAllProjectUser(form.getPrjSid(), con);

			for (Knt302UserModel usr : usrList){

				// チェックされた担当者
				String checkedUsrSid =  NullDefault.getString(req.getParameter("user" + usr.getUsrSid()), "");

				if (checkedUsrSid != ""){

					// プロジェクト担当者存在チェック
					boolean isNewUser = dao.containsProjectUser(form.getPrjSid(), usr.getUsrSid() , con);

					// 追加
					if (isNewUser){
						dao.insertProjectUser(form.getPrjSid(), usr.getUsrSid(), con);
					}

				}

			}

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
											Knt302Form form,
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
		msgs.add("ＩＤ:" + form.getPrjId());
		msgs.add("名称:" + form.getPrjName());
		msgs.add("開始:" + form.getStartYear() + "/" +  + form.getStartMonth() + "/" +  + form.getStartDay());
		msgs.add("終了:" + form.getEndYear() + "/" +  + form.getEndMonth() + "/" +  + form.getEndDay());
		String msg = String.join("<br>", msgs);
		cmn999Form.setMessage(msgRes.getMessage("touroku.kanryo.object", msg));
		req.setAttribute("cmn999Form", cmn999Form);

		// 遷移先
		urlForward = map.findForward("back300");
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
		Knt302Form form,
		Connection con) throws SQLException {

		Cmn999Form cmn999Form = new Cmn999Form();
		ActionForward urlForward = null;

		MessageResources msgRes = getResources(req);
		cmn999Form.setIcon(Cmn999Form.ICON_INFO);
		cmn999Form.setWtarget(Cmn999Form.WTARGET_BODY);

		urlForward = map.findForward("back302");
		cmn999Form.setType(Cmn999Form.TYPE_OKCANCEL);
		cmn999Form.setUrlCancel(urlForward.getPath());

		cmn999Form.setUrlOK(urlForward.getPath() + "?CMD=regPrjDecision");

		//メッセージセット
		String msgState = "touroku.kakunin.once";
		List<String> msgs = new ArrayList<String>();
		msgs.add("ＩＤ:" + form.getPrjId());
		msgs.add("名称:" + form.getPrjName());
		msgs.add("開始:" + form.getStartYear() + "/" +  + form.getStartMonth() + "/" +  + form.getStartDay());
		msgs.add("終了:" + form.getEndYear() + "/" +  + form.getEndMonth() + "/" +  + form.getEndDay());
		String msg = String.join("<br>", msgs);
		cmn999Form.setMessage(msgRes.getMessage(msgState, msg));

		cmn999Form.addHiddenParam("prjSid", form.getPrjSid());
		cmn999Form.addHiddenParam("prjId", form.getPrjId());
		cmn999Form.addHiddenParam("prjName", form.getPrjName());
		cmn999Form.addHiddenParam("startYear", form.getStartYear());
		cmn999Form.addHiddenParam("startMonth", form.getStartMonth());
		cmn999Form.addHiddenParam("startDay", form.getStartDay());
		cmn999Form.addHiddenParam("endYear", form.getEndYear());
		cmn999Form.addHiddenParam("endMonth", form.getEndMonth());
		cmn999Form.addHiddenParam("endDay", form.getEndDay());
		cmn999Form.addHiddenParam("indirect", form.getIndirect());

		cmn999Form.addHiddenParam("startY", form.getStartYear());
		cmn999Form.addHiddenParam("startM", form.getStartMonth());
		cmn999Form.addHiddenParam("startD", form.getStartDay());
		cmn999Form.addHiddenParam("endY", form.getEndYear());
		cmn999Form.addHiddenParam("endM", form.getEndMonth());
		cmn999Form.addHiddenParam("endD", form.getEndDay());

		cmn999Form.addHiddenParam("orderName", form.getOrderName());
		cmn999Form.addHiddenParam("chiefName", form.getChiefName());
		cmn999Form.addHiddenParam("managerName", form.getManagerName());
		cmn999Form.addHiddenParam("remark", form.getRemark());
		cmn999Form.addHiddenParam("targetB", form.getTargetB());
		cmn999Form.addHiddenParam("targetS", form.getTargetS());
		cmn999Form.addHiddenParam("targetH", form.getTargetH());
		cmn999Form.addHiddenParam("targetJ", form.getTargetJ());

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
		Knt302Form form,
		Connection con) throws SQLException {

		Cmn999Form cmn999Form = new Cmn999Form();
		ActionForward urlForward = null;

		MessageResources msgRes = getResources(req);
		cmn999Form.setIcon(Cmn999Form.ICON_INFO);
		cmn999Form.setWtarget(Cmn999Form.WTARGET_BODY);

		urlForward = map.findForward("back302");
		cmn999Form.setType(Cmn999Form.TYPE_OKCANCEL);
		cmn999Form.setUrlCancel(urlForward.getPath());

		cmn999Form.setUrlOK(urlForward.getPath() + "?CMD=delPrjDecision");

		//メッセージセット
		String msgState = "sakujo.kakunin.once";
		List<String> msgs = new ArrayList<String>();
		msgs.add("ＩＤ:" + form.getPrjId());
		msgs.add("名称:" + form.getPrjName());
		msgs.add("開始:" + form.getStartYear() + "/" +  + form.getStartMonth() + "/" +  + form.getStartDay());
		msgs.add("終了:" + form.getEndYear() + "/" +  + form.getEndMonth() + "/" +  + form.getEndDay());
		String msg = String.join("<br>", msgs);
		cmn999Form.setMessage(msgRes.getMessage(msgState, msg));

		cmn999Form.addHiddenParam("prjSid", form.getPrjSid());
		cmn999Form.addHiddenParam("delPrjSid", form.getPrjSid());

		req.setAttribute("cmn999Form", cmn999Form);

		return map.findForward("gf_msg");
	}

}
