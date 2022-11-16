package jp.groupsession.v2.kintai.Knt002;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.Calendar;
import java.util.Objects;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
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
import jp.groupsession.v2.kintai.Knt001.Knt001Model;
import jp.groupsession.v2.kintai.Knt001.Knt001Dao;
import jp.groupsession.v2.kintai.Knt100.Knt100Dao;

/**
 * <br>[機  能] Knt002のアクションクラス
 * <br>[解  説]
 * <br>[備  考]
 *
 * @author JTS
 */
public class Knt002Action extends AbstractGsAction {

	/** Logging インスタンス */
	private static Log log__ = LogFactory.getLog(Knt002Action.class);

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
		Knt002Form knt002Form = (Knt002Form) form;
		Knt001Dao dao001 = new Knt001Dao();
		Knt002Dao dao002 = new Knt002Dao();
		Knt100Dao dao100 = new Knt100Dao();

		//HttpServletRequestからリクエストモデルを取得します
		RequestModel reqMdl = getRequestModel(req);
		//リクエストモデルからログインユーザの情報を取得します。
		BaseUserModel usModel = reqMdl.getSmodel();
		// ユーザー名
		String usrName = usModel.getUsisei() + " " + usModel.getUsimei();

		// buttonPushでセットされているコマンド
		String cmd = NullDefault.getString(req.getParameter("CMD"), "");

		log__.debug("CMD=" + cmd);

		// 選択された日付
		int y = 9999;
		int m = 12;
		int d = 31;
		if (cmd.startsWith("detail")) {
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

		log__.debug("002Y=" + y);
		log__.debug("002M=" + m);
		log__.debug("002D=" + d);

		// 表示するユーザーID
		int loginSid = usModel.getUsrsid();
		// 総務からの遷移のユーザーID
		if (cmd.startsWith("detail002")) {
			loginSid = knt002Form.getSelUsrSid();
		}

		// プロジェクト一覧（担当者所属かつ期間内）
		ArrayList<Knt002Model> prjList = dao002.selectFromToProjectList(loginSid, y, m, d, con);
		req.setAttribute("prjList", prjList);
		// 作業内容一覧
		ArrayList<Knt002Model> wrkList = dao002.selectWorkList(con);
		req.setAttribute("wrkList", wrkList);
		// 作業実績時間取得（指定日）
		ArrayList<Knt002Model> wrks = dao002.selectWorks(loginSid, y, m, d, con);
		req.setAttribute("wrks", wrks);
		// 事由
		Knt002Model modelReason = dao002.selectWork(loginSid, y, m, d, 70, con);
		String reason = NullDefault.getString(modelReason.getJskRmk(),"");
		// 代休
		Knt002Model modelDaiQ = dao002.selectWork(loginSid, y, m, d, 71, con);
		int daiQ = 0;
		if (modelDaiQ.getWrkId()==711) {
			daiQ = 1;
		} else {
			daiQ = 0;
		}

		// 登録済みの実績時間
		double regSumTime = dao002.selectTotalTimeDay(loginSid, y, m, d ,con);

		// 勤怠実績時間
		double stpTime = dao100.selectDayUserStampTotalTimeEx(loginSid, y, m, d, con);

		// 勤務時間
		req.setAttribute("stpTime", stpTime);
		log__.debug("stpTime：" + stpTime);

		// 有給時間（申請）
		double uqTime = dao002.selectDayPayTotalTime(loginSid, y, m, d, 99, con);
		req.setAttribute("uqTime", uqTime);
		// 36協定用の月別残業合計時間（申請時間）※移動時間を除外
		Calendar cal=Calendar.getInstance();
		cal.clear();
		cal.set(Calendar.YEAR, y);
		cal.set(Calendar.MONTH, m - 1);
		int lastDay = cal.getActualMaximum(Calendar.DATE);
		double totalTime36 = 0;
		for (int i=1;i<=lastDay;i++) {
			double dayTime36 = dao002.selectDay36TotalTime(loginSid, y, m, i, 0, con);
			if (0 < dayTime36){
				totalTime36 = totalTime36 + dayTime36;
			}
		}
		req.setAttribute("totalTime36", totalTime36);

		// 出退勤時間
		Timestamp firstInTime = dao002.selectInOutTime(loginSid, y, m, d, 0, con);
		Timestamp lastOutTime = dao002.selectInOutTime(loginSid, y, m, d, 1, con);
		Timestamp outTime = dao002.selectInOutTime(loginSid, y, m, d, 2, con);
		Timestamp inTime = dao002.selectInOutTime(loginSid, y, m, d, 3, con);

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String strFirstInTime = "";
		String strLastOutTime = "";
		String strOutTime = "";
		String strInTime = "";
		if (Objects.nonNull(firstInTime)) {
			strFirstInTime = sdf.format(firstInTime);
		}
		if (Objects.nonNull(lastOutTime)) {
			strLastOutTime = sdf.format(lastOutTime);
		}
		if (Objects.nonNull(outTime)) {
			strOutTime = sdf.format(outTime);
		}
		if (Objects.nonNull(inTime)) {
			strInTime = sdf.format(inTime);
		}

		req.setAttribute("firstInTime", strFirstInTime);
		req.setAttribute("lastOutTime", strLastOutTime);
		req.setAttribute("outTime", strOutTime);
		req.setAttribute("inTime", strInTime);
		req.setAttribute("uqTime", uqTime);

		// 総務確認からの遷移情報をクリア
		int smuChk = knt002Form.getSmuChk();
		int selUsrSid = loginSid;
		int startYear=2000;
		int startMonth=12;
		int startDay=31;
		int endYear=9999;
		int endMonth=12;
		int endDay=31;
		int intDays=31;
		int confirm = 0;

//		log__.debug("smuChk=" + smuChk);
//		log__.debug("reason=" + reason);

		// 承認済みユーザーかの取得
		boolean synChk = dao002.selectShouninUser(selUsrSid, y, m, d ,con);
		// 総務確認済みユーザーかの取得
		boolean smuChecked = dao002.selectSoumuCheckUser(selUsrSid, y, m, d ,con);
		// 承認済みまたは総務確認済みユーザーかの取得
		boolean sChecked = false;
		if (synChk) {
			sChecked = true;
		}
		if (smuChecked) {
			sChecked = true;
		}

		if (cmd.startsWith("detail001")){

			// 承認済
			if(sChecked) {
				errors.add("sChecked", new ActionMessage("※承認済または総務確認済です。"));
				addErrors(req, errors);
			}

			// 初期表示
			forward = map.getInputForward();

		} else if (cmd.startsWith("regist")) {

			// ユーザーが営業グループに所属するか
			boolean sales = dao001.containsManageGroup(6, usModel.getUsrsid(), con);

			// 登録内容を配列化
			boolean errMismatch = false;
			boolean requiredSalesRemark = false;
			double sumTime = 0;
			ArrayList<Knt002Model> params = new ArrayList<Knt002Model>();
			for(int i=0; i<10; i++) {
				Knt002Model model = new Knt002Model();
				int prjSid = Integer.parseInt(req.getParameter("prjList" + i));
				int wrkId = Integer.parseInt(req.getParameter("wrkList" + i));
				double time = 0;
				if (!NullDefault.getString(req.getParameter("time" + i),"").equals("")){
					time = Double.parseDouble(req.getParameter("time" + i));
				}
//				String jskRmk = req.getParameter("jskRmk" + i);
				String jskRmk = NullDefault.getString(req.getParameter("jskRmk" + i), "");
//				log__.debug("prjSid=" + prjSid);
//				log__.debug("wrkId=" + wrkId);
//				log__.debug("time=" + time);
//				log__.debug("jskRmk=" + jskRmk);
				model.setJskId(i);
				model.setUsrSid(loginSid);
				model.setPrjSid(prjSid);
				model.setWrkId(wrkId);
				model.setWrkYear(y);
				model.setWrkMonth(m);
				model.setWrkDay(d);
				model.setWrkTime(time);
				model.setJskRmk(jskRmk);
				params.add(model);
				sumTime = sumTime + time;
				// 時間有＋プロジェクト未選択
//				log__.debug("errMismatch=" + errMismatch);
				if (errMismatch == false && 0 < time) {
					// 間接判断
					boolean idp = dao002.BooIndirectProject(prjSid, con);
					log__.debug("idp=" + idp);
					if (prjSid == -1) {
						// プロジェクト未選択
						errMismatch = true;
					} else if (idp == false) {
						// 通常プロジェクト未選択＋作業未選択
						if ( prjSid == -1 && wrkId == -1 ) {
							errMismatch = true;
						} else if ( wrkId == -1 ) {
							// 通常プロジェクト選択＋作業未選択
							errMismatch = true;
						}
					}
					// 営業備考必須
					if (sales && jskRmk.equals("")) {
						requiredSalesRemark = true;
					}
				}
			}

			// 事由
			reason = NullDefault.getString(knt002Form.getReason(), "");
			modelReason = new Knt002Model();
			modelReason.setUsrSid(loginSid);
			modelReason.setWrkYear(y);
			modelReason.setWrkMonth(m);
			modelReason.setWrkDay(d);
			modelReason.setJskId(70);
			modelReason.setJskRmk(reason);
			params.add(modelReason);

			// 代休意思
			daiQ = knt002Form.getDaiQ();
			modelDaiQ = new Knt002Model();
			modelDaiQ.setUsrSid(loginSid);
			modelDaiQ.setWrkYear(y);
			modelDaiQ.setWrkMonth(m);
			modelDaiQ.setWrkDay(d);
			modelDaiQ.setJskId(71);
			if (daiQ==0){
				modelDaiQ.setWrkId(710);
			} else {
				modelDaiQ.setWrkId(711);
			}
			params.add(modelDaiQ);

//			log__.debug("sChecked=" + sChecked);
//			log__.debug("sumTime=" + sumTime);
//			log__.debug("stpTime=" + stpTime);
//			log__.debug("regSumTime=" + regSumTime);
//			log__.debug("reason=" + reason);
//			log__.debug("daiQ=" + daiQ);
//			log__.debug("params.size=" + params.size());

			// 時間有＋未選択エラー
			if (errMismatch) {
				errors.add("mismatch", new ActionMessage("※プロジェクトと作業（間接以外）は必須です。"));
				addErrors(req, errors);
				req.setAttribute("wrks", params);
				forward = map.getInputForward();
			} else if (requiredSalesRemark) {
				errors.add("salesRmk", new ActionMessage("※備考は必須です。"));
				addErrors(req, errors);
				req.setAttribute("wrks", params);
				forward = map.getInputForward();
			} else if ((sChecked) && (sumTime != regSumTime)) {
				// プロジェクト変更は可能とするが、申請時間が異なる場合は登録不可
				errors.add("cantEditTime", new ActionMessage("※承認済は作業時間の変更は行えません。プロジェクトのみ変更可能です。"));
				addErrors(req, errors);
				req.setAttribute("wrks", wrks);
				forward = map.getInputForward();
			} else {
				// 登録確認チェック
				if (cmd.startsWith("registConfirm") && sChecked == false) {
					double diff = stpTime - (sumTime - uqTime); // 有給は除外
					log__.debug("diff=" + diff);
					if(diff != 0 && diff > 0.5 && reason.equals("")){
						// 0.5（30分）以上は事由必須
						errors.add("over30", new ActionMessage("※申請時間が実績時間より少ない事由を入力してください。"));
						addErrors(req, errors);
						req.setAttribute("wrks", params);
						forward = map.getInputForward();
						log__.debug("reason");
					} else if(diff != 0 && diff < 0.25) {
						// 0.25（15分）未満は確認
						errors.add("diffTime", new ActionMessage("※申請時間と実績時間が異なります。よろしいですか？登録ボタン再クリックで確定します。"));
						addErrors(req, errors);
						req.setAttribute("wrks", params);
						confirm = 1;
						forward = map.getInputForward();
						log__.debug("TimeError");
					}
					
				}
			}

			// エラーなし
			if (errors.isEmpty()) {

				log__.debug("エラーなし");

				// 以下は「Knt001.jsp」の表示に必要な値

				// 作業実績時間取得（月）
				ArrayList<Knt001Model> uiList = dao001.selectDayTotalTime(loginSid, y, m, con);
				HashMap<Integer, Double> uiMap = new HashMap<Integer, Double>();
				for(int i = 0; i < uiList.size(); i++)
				{
					Knt001Model model = uiList.get(i);
					uiMap.put(model.getWrkDay(), model.getWrkTime());
				}
				// 未登録の日の実績時間を追加
				for(int i = 0; i <= 31; i++)
				{
					if (uiMap.containsKey(i)){
					} else {
						uiMap.put(i, 0.0);
					}
				}

				req.setAttribute("dispYear", y);
				req.setAttribute("dispMonth", m);
				req.setAttribute("dispDay", d);
				req.setAttribute("uiMap", uiMap);

				log__.debug("reason=" + reason);
				log__.debug("params.size=" + params.size());

				//登録ボタンクリック（登録後メインに遷移）
				forward = __doRegist(map, params, req, res, con);

				// 登録後、メイン画面にメッセージを表示
//				errors.add("registed", new ActionMessage("registed"));
//				addErrors(req, errors);

				// 登録後、遷移せずメッセージ表示
				errors.add("registed", new ActionMessage("登録しました。"));
				addErrors(req, errors);
				req.setAttribute("wrks", params);
				forward = map.getInputForward();

			}

		} else if (cmd.startsWith("detail002")) {

			// 総務確認からの遷移情報
			selUsrSid = knt002Form.getSelUsrSid();
			usrName = dao002.selectUserName(selUsrSid, con);
			smuChk = (Integer)req.getAttribute("smuChk");

			// 事由
			modelReason = dao002.selectWork(selUsrSid, y, m, d, 70, con);
			reason = NullDefault.getString(modelReason.getJskRmk(),"");

			log__.debug("smuChk1=" + knt002Form.getSmuChk());
			log__.debug("smuChk2=" + (Integer)req.getAttribute("smuChk"));
			log__.debug("selUsrSid=" + selUsrSid);
			log__.debug("y=" + y);
			log__.debug("m=" + m);
			log__.debug("d=" + d);
			log__.debug("reason=" + reason);

			startYear=knt002Form.getStartYear();
			startMonth=knt002Form.getStartMonth();
			startDay=knt002Form.getStartDay();
			endYear=knt002Form.getEndYear();
			endMonth=knt002Form.getEndMonth();
			endDay=knt002Form.getEndDay();

			req.setAttribute("selUsrSid", selUsrSid);
			req.setAttribute("startYear", startYear);
			req.setAttribute("startMonth", startMonth);
			req.setAttribute("startDay", startDay);
			req.setAttribute("endYear", endYear);
			req.setAttribute("endMonth", endMonth);
			req.setAttribute("endDay", endDay);

			log__.debug("DISP_USR_ID_002=" + selUsrSid);
			log__.debug("SY_002=" + startYear);
			log__.debug("SM_002=" + startMonth);
			log__.debug("SD_002=" + startDay);
			log__.debug("EY_002=" + endYear);
			log__.debug("EM_002=" + endMonth);
			log__.debug("ED_002=" + endDay);

			// 総務確認から表示
			forward = map.getInputForward();

		} else if (cmd.equals("backMain")) {

			req.setAttribute("dispYear", y);
			req.setAttribute("dispMonth", m);
			req.setAttribute("dispDay", d);

			// メインへ戻る
			forward =  map.findForward("backMain");

		} else if (cmd.equals("back200")) {

//			selUsrSid = knt002Form.getSelUsrSid();
			startYear=knt002Form.getStartYear();
			startMonth=knt002Form.getStartMonth();
			startDay=knt002Form.getStartDay();
			endYear=knt002Form.getEndYear();
			endMonth=knt002Form.getEndMonth();
			endDay=knt002Form.getEndDay();

			log__.debug("SEL_USR_ID_002=" + selUsrSid);
			log__.debug("SY_002=" + startYear);
			log__.debug("SM_002=" + startMonth);
			log__.debug("SD_002=" + startDay);
			log__.debug("EY_002=" + endYear);
			log__.debug("EM_002=" + endMonth);
			log__.debug("ED_002=" + endDay);

			// 総務確認へ戻る
			forward =  map.findForward("back200");

		}

		// 総務確認からの遷移情報をセット
		req.setAttribute("usrName", usrName);
		req.setAttribute("smuChk", smuChk);
		req.setAttribute("selUsrSid", selUsrSid);
		req.setAttribute("startYear", startYear);
		req.setAttribute("startMonth", startMonth);
		req.setAttribute("startDay", startDay);
		req.setAttribute("endYear", endYear);
		req.setAttribute("endMonth", endMonth);
		req.setAttribute("endDay", endDay);
		req.setAttribute("reason", reason);
		req.setAttribute("daiQ", daiQ);
		req.setAttribute("confirm", confirm);

		return forward;

	}

	/**
	 * <br>[機  能] 指定した時間の期間内判定
	 * <br>[解  説]
	 * <br>[備  考]
	 * @param  ts      判定する時間
	 * @param  ts1     開始時間
	 * @param  ts2     終了時間
	 * @return boolean 判定
	 */
	private boolean between(Timestamp ts, Timestamp ts1, Timestamp ts2) {
		return !(ts1.after(ts) || ts2.before(ts));
	}

	/**
	 * <p>Timestampから指定した書式を返す
	 * @param ts Timestamp
	 * @return int
	 */
	public int toTimestampFormat(Timestamp ts, String fmt){
		SimpleDateFormat sdf = new SimpleDateFormat(fmt);
		String str = sdf.format(ts);
		int ret = Integer.parseInt(str);
		return ret;
	}

	/**
	 * <br>[機  能] 登録ボタンクリック時処理を行う
	 * <br>[解  説]
	 * <br>[備  考]
	 * @param map    マップ
	 * @param params Knt002Model配列
	 * @param reason 事由
	 * @param dq     代休意思
	 * @param req    リクエスト
	 * @param res    レスポンス
	 * @param con    コネクション
	 * @return ActionForward フォワード
	 * @throws Exception 実行例外
	 */
	private ActionForward __doRegist(ActionMapping map,
										ArrayList<Knt002Model> params,
										HttpServletRequest req,
										HttpServletResponse res,
										Connection con)
		throws Exception {

		// インスタンス
		Knt002Dao dao = new Knt002Dao();

		boolean commit = false;
		try {

			for(int i=0; i < params.size(); i++) {

				// 配列から取り出し
				Knt002Model param = params.get(i);

				// 存在チェック
				boolean isNew = dao.isNewPerformance(param, con);

				if (isNew){
					// 追加
					dao.insertPerformance(param, con);
					log__.debug("追加");
				} else {
					// 更新
					dao.updatePerformance(param, con);
					log__.debug("更新");
				}
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

		// メインに遷移
		return map.findForward("backMain");

	}

}
