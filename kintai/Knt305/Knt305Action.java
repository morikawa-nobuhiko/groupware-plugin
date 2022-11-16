package jp.groupsession.v2.kintai.Knt305;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
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
import jp.groupsession.v2.kintai.Knt200.Knt200Dao;
import jp.groupsession.v2.kintai.Knt200.Knt200Model;
import jp.groupsession.v2.kintai.Knt302.Knt302Dao;
import jp.groupsession.v2.kintai.Knt302.Knt302UserModel;

/**
 * <br>[機  能] Knt305のアクションクラス
 * <br>[解  説]
 * <br>[備  考]
 *
 * @author JTS
 */
public class Knt305Action extends AbstractGsAction {

	/** Logging インスタンス */
	private static Log log__ = LogFactory.getLog(Knt305Action.class);

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
		Knt305Form Knt305Form = (Knt305Form) form;
		Knt302Dao dao302 = new Knt302Dao();
		Knt200Dao dao200 = new Knt200Dao();

		// buttonPushでセットされているコマンド
		String cmd = NullDefault.getString(req.getParameter("CMD"), "");

		log__.debug("CMD=" + cmd);

		//HttpServletRequestからリクエストモデルを取得します
		RequestModel reqMdl = getRequestModel(req);
		//リクエストモデルからログインユーザの情報を取得します。
		BaseUserModel usModel = reqMdl.getSmodel();

		// ユーザー一覧を取得
		ArrayList<Knt302UserModel> usrList = dao302.selectUsers(con);
		// ユーザー一覧をセット
		req.setAttribute("usrList", usrList);

		// JSPに反映する変数
		int selUsrSid = -1;
		int startYear=2000;
		int startMonth=12;
		int startDay=31;
		int endYear=9999;
		int endMonth=12;
		int endDay=31;
		int intDays=31;

		if (cmd.equals("")) {

			// 初期表示

			// 現在の日付を反映
			Calendar cal=Calendar.getInstance();
			cal.set(Calendar.DATE, 1);					// 1日を指定
			startYear=cal.get(Calendar.YEAR);
			startMonth=cal.get(Calendar.MONTH) + 1;
			startDay=cal.get(Calendar.DATE);
			endYear=startYear;
			endMonth=startMonth;
			endDay=cal.getActualMaximum(Calendar.DATE);

		} else if (cmd.equals("search") || cmd.equals("back200")) {

			// 表示条件変更
			selUsrSid = Knt305Form.getSelUsrSid();
			startYear=Knt305Form.getStartYear();
			startMonth=Knt305Form.getStartMonth();
			startDay=Knt305Form.getStartDay();

			log__.debug("表示条件変更");

		} else if (cmd.startsWith("regist")) {

			// パラメーター取得
			selUsrSid = Knt305Form.getSelUsrSid();
			startYear=Knt305Form.getStartYear();
			startMonth=Knt305Form.getStartMonth();
			startDay=Knt305Form.getStartDay();

			// 期間日数取得
			Calendar calS=Calendar.getInstance();
			Calendar calE=Calendar.getInstance();
			calS.set(startYear, startMonth -1, startDay);
			calE.set(endYear, endMonth -1, endDay);

			log__.debug("calS=" + calS);
			log__.debug("calE=" + calE);

			boolean commit = false;
			try {

				// 開始期間と終了期間が異なる間処理する
				int idx = 0;
				while(!calS.equals(calE)){

					// 日付の取得
					int smuY = calS.get(Calendar.YEAR);
					int smuM = calS.get(Calendar.MONTH) + 1;
					int smuD = calS.get(Calendar.DATE);
					log__.debug("smuY=" + smuY);
					log__.debug("smuM=" + smuM);
					log__.debug("smuD=" + smuD);

					// 実績がない日付は処理しない
					boolean userJsk =dao200.containsUserJsk(selUsrSid, smuY, smuM, smuD, con);
					if (!userJsk) {
						// 開始期間の日数を加算
						calS.add(Calendar.DATE, 1);
						continue;
					}

					// チェックされた日付
					int intSmuChk = 0;
					String checkedDate =  NullDefault.getString(req.getParameter("smuChk" + smuY + smuM + smuD), "");
					log__.debug("checkedDate=" + checkedDate);
					if (!checkedDate.equals("")) {
						intSmuChk = 1;
					}
					log__.debug("intSmuChk=" + intSmuChk);

					// 備考
					log__.debug("idx=" + idx);
					String smuRmk = Knt305Form.getSmuRmk()[idx];

					// 存在チェック
					boolean isNew = dao200.isNewSmuCheck(selUsrSid, smuY, smuM, smuD, con);
					if (isNew){
						// 追加
						dao200.insertSmuCheck(selUsrSid, smuY, smuM, smuD, intSmuChk, smuRmk, con);
					} else {
						// 更新
						dao200.updateSmuCheck(selUsrSid, smuY, smuM, smuD, intSmuChk, smuRmk, con);
					}

					// 開始期間の日数を加算
					calS.add(Calendar.DATE, 1);

					// チェック状態、備考配列のインデックス
					idx = idx + 1;

				}

				// コミット
				con.commit();
				commit = true;

			} catch (Exception e) {
				throw e;
			} finally {
				if (!commit) {
					con.rollback();
				}
			}

			// 登録メッセージ
			ActionErrors errors = new ActionErrors();
			errors.add("registed", new ActionMessage("登録しました。"));
			addErrors(req, errors);

		} else if (cmd.startsWith("detail002")) {

			// 総務確認からの遷移
			req.setAttribute("smuChk", 1);

			return map.findForward("detail002");

		}

		// 作業実績時間取得（期間）
		ArrayList<Knt200Model> fromToTimes = dao200.selectFromToTotalTime(selUsrSid, startYear, startMonth, startDay, endYear, endMonth, endDay, con);

		// 総務確認情報の取得
		for(int i=0; i<fromToTimes.size(); i++) {
			Knt200Model ftt = (Knt200Model)fromToTimes.get(i);
			Knt200Model smu = dao200.selectSmuCheck(selUsrSid, ftt.getWrkYear(), ftt.getWrkMonth(), ftt.getWrkDay(), con);
			ftt.setSmuChk(smu.getSmuChk());
			ftt.setSmuRmk(smu.getSmuRmk());
		}

		log__.debug("fromToTimes_count=" + fromToTimes.size());
		log__.debug("DISP_USR_ID=" + selUsrSid);
		log__.debug("SY=" + startYear);
		log__.debug("SM=" + startMonth);
		log__.debug("SD=" + startDay);

		// JSPで必要な値をセット
		req.setAttribute("fromToTimes", fromToTimes);
		req.setAttribute("selUsrSid", selUsrSid);
		req.setAttribute("startYear", startYear);
		req.setAttribute("startMonth", startMonth);
		req.setAttribute("startDay", startDay);
		// --------------------------------------------------------------

		forward = map.getInputForward();

		return forward;

	}

}
