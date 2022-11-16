package jp.groupsession.v2.kintai.Knt001;

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
import jp.groupsession.v2.kintai.Knt100.Knt100Dao;
import jp.groupsession.v2.cmn.dao.base.CmnHolidayDao;
import jp.groupsession.v2.cmn.model.base.CmnHolidayModel;

/**
 * <br>[機  能] Knt001のアクションクラス
 * <br>[解  説]
 * <br>[備  考]
 *
 * @author JTS
 */
public class Knt001Action extends AbstractGsAction {

	/** Logging インスタンス */
	private static Log log__ = LogFactory.getLog(Knt001Action.class);

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
		Knt001Dao dao001 = new Knt001Dao();
		Knt100Dao dao100 = new Knt100Dao();

		// buttonPushでセットされているコマンド
		String cmd = NullDefault.getString(req.getParameter("CMD"), "");

		log__.debug("CMD=" + cmd);

		//HttpServletRequestからリクエストモデルを取得します
		RequestModel reqMdl = getRequestModel(req);
		//リクエストモデルからログインユーザの情報を取得します。
		BaseUserModel usModel = reqMdl.getSmodel();

		Calendar cal=Calendar.getInstance();
		int intYear=9999;
		int intMonth=12;
		int intDay=31;

		if (cmd.equals("")){

			// 本日の日付を取得
			intYear=cal.get(Calendar.YEAR);
			intMonth=cal.get(Calendar.MONTH) + 1;
			intDay=cal.get(Calendar.DATE);

			// 月初の時は前月を表示する
			if (intDay == 1) {
				intMonth = intMonth - 1;
			}

			// 作業実績時間取得（指定日）
//			Knt001Dao kui = new Knt001Dao();
//			Knt001Model um = kui.selectTotalTimeDay(usModel.getUsrsid(), intYear, intMonth, intDay, con);
//			req.setAttribute("wrk001Model", um);

			// debug log
			log__.debug("dispYear=" + intYear);
			log__.debug("dispMonth=" + intMonth);

			forward = map.getInputForward();

		} else if (cmd.startsWith("backMain")) {

			intYear = (Integer)req.getAttribute("dispYear");
			intMonth = (Integer)req.getAttribute("dispMonth");
			intDay = (Integer)req.getAttribute("dispDay");

			forward = map.getInputForward();

		} else if (cmd.startsWith("regist")) {

			// 登録、承認画面からの遷移。元表示していた年月を表示
			intYear = (Integer)req.getAttribute("dispYear");
			intMonth = (Integer)req.getAttribute("dispMonth");
			intDay = (Integer)req.getAttribute("dispDay");

			forward = map.getInputForward();

		} else if (cmd.startsWith("MoveMonth")) {

			String[] cmds = cmd.split(",");

			log__.debug("NowYear=" + cmds[1]);
			log__.debug("NowMonth=" + cmds[2]);

			// 表示されていた年月を取得
			int dispYear = Integer.parseInt(cmds[1]);
			int dispMonth = Integer.parseInt(cmds[2]);
			cal.set(dispYear, dispMonth - 1 ,1);

			if (cmd.contains("Before")){
				// 前月
				cal.add(Calendar.MONTH, -1);
			} else {
				// 次月月
				cal.add(Calendar.MONTH, 1);
			}

			// 実績時間取得日付
			intYear = cal.get(Calendar.YEAR);
			intMonth = cal.get(Calendar.MONTH) + 1;

//			log__.debug("NowYear=" + dispYear);
//			log__.debug("NowMonth=" + dispMonth);

			forward = map.getInputForward();

		} else if (cmd.startsWith("detail001")) {

			// 実績登録画面へ遷移
			forward = map.findForward("detail001");

		} else if (cmd.startsWith("approval")) {

			// 承認画面へ遷移
			forward = map.findForward("approval");

		} else if (cmd.startsWith("debug_rollback")) {

			// デバッグ_ロールバック

			// 本日の日付を取得
			intYear=cal.get(Calendar.YEAR);
			intMonth=cal.get(Calendar.MONTH) + 1;
			intDay=cal.get(Calendar.DATE);

			// 月初の時は前月を表示する
			if (intDay == 1) {
				intMonth = intMonth - 1;
			}

			log__.debug("rollback_start");
			JDBCUtil.rollback(con);
			log__.debug("rollback_end");

			forward = map.getInputForward();

		}

		// 表示する年月を反映
		req.setAttribute("dispYear", intYear);
		req.setAttribute("dispMonth", intMonth);

		log__.debug("dispYear=" + intYear);
		log__.debug("dispMonth=" + intMonth);

		// 作業申請時間取得（月）
		double reqTotalTime = 0;
		ArrayList<Knt001Model> uiList = dao001.selectDayTotalTime(usModel.getUsrsid(), intYear, intMonth, con);
		HashMap<Integer, Double> uiMap = new HashMap<Integer, Double>();
		for(int i = 0; i < uiList.size(); i++)
		{
			Knt001Model model = uiList.get(i);
			uiMap.put(model.getWrkDay(), model.getWrkTime());
			reqTotalTime = reqTotalTime + model.getWrkTime();
		}
		req.setAttribute("reqTotalTime", reqTotalTime);

		// 未登録の日の申請時間を追加
		for(int i = 0; i <= 31; i++)
		{
			if (uiMap.containsKey(i)){
			} else {
				uiMap.put(i, 0.0);
			}
		}
		req.setAttribute("uiMap", uiMap);

		// 打刻実績時間取得（月）
		HashMap<Integer, Double> stpMap = new HashMap<Integer, Double>();
		double monthTotalTime = 0;
		cal.clear();
		cal.set(Calendar.YEAR, intYear);
		cal.set(Calendar.MONTH, intMonth -1);
		int last = cal.getActualMaximum(Calendar.DATE);
		for (int i=1; i<=last; i++){
			// 勤怠実績時間
			double stpTime = dao100.selectDayUserStampTotalTimeEx(usModel.getUsrsid(), intYear, intMonth, i, con);
			stpMap.put(i, stpTime);
			monthTotalTime = monthTotalTime + stpTime;
		}
		req.setAttribute("stpMap", stpMap);
		req.setAttribute("stpTotalTime", monthTotalTime);

		// 月別の有給合計時間を取得
		double uqt = dao001.selectMonthPayTotalTime(usModel.getUsrsid(), intYear, intMonth, 99, con);
		req.setAttribute("uqt", uqt);
		log__.debug("uqt=" + uqt);

		// ユーザーが管理グループに所属するか
		boolean manager = dao001.containsManageGroup(1, usModel.getUsrsid(), con);
		req.setAttribute("manager", manager);

		// ユーザーが承認グループに所属するか
		boolean authorizer = dao001.containsManageGroup(8, usModel.getUsrsid(), con);
		req.setAttribute("authorizer", authorizer);

		// ユーザーが総務グループに所属するか
		boolean soumu = dao001.containsManageGroup(2, usModel.getUsrsid(), con);
		req.setAttribute("soumu", soumu);

		// 登録日の表示用
		req.setAttribute("yyyy", intYear);
		req.setAttribute("mm", intMonth);
		req.setAttribute("dd", intDay);

		// 祝日の取得
		CmnHolidayDao holDao = new CmnHolidayDao(con);
		List<CmnHolidayModel> holidayModels = holDao.getHoliDayList(intYear);
		ArrayList<String> holiday = new ArrayList<String>();
//		HashMap <String, CmnHolidayModel> holMap =  new HashMap<String, CmnHolidayModel>();
		for(int i = 0; i < holidayModels.size(); i++)
		{
			CmnHolidayModel hm = holidayModels.get(i);
			holiday.add(hm.getHolDate().getDateString());
		}
		req.setAttribute("holiday", holiday);

		return forward;

	}

}
