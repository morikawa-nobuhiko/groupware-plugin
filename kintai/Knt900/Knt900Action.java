package jp.groupsession.v2.kintai.Knt900;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.IOException;
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
import jp.co.sjts.util.Encoding;
import jp.co.sjts.util.NullDefault;
import jp.co.sjts.util.date.UDate;
import jp.co.sjts.util.jdbc.JDBCUtil;
import jp.co.sjts.util.jdbc.SqlBuffer;
import jp.co.sjts.util.http.TempFileUtil;
import jp.groupsession.v2.struts.AbstractGsAction;
import jp.groupsession.v2.cmn.config.PluginConfig;
import jp.groupsession.v2.cmn.dao.GroupModel;
import jp.groupsession.v2.cmn.dao.BaseUserModel;
import jp.groupsession.v2.cmn.dao.UsidSelectGrpNameDao;
import jp.groupsession.v2.cmn.dao.BaseUserModel;
import jp.groupsession.v2.cmn.model.RequestModel;

/**
 * <br>[機能] Knt900のアクションクラス
 * <br>[解説]
 * <br>[備考]
 *
 * @author JTS
 */
public class Knt900Action extends AbstractGsAction {

	/** Logging インスタンス */
	private static Log log__ = LogFactory.getLog(Knt900Action.class);

	/**
	* <br>[機能] アクションを実行する
	* <br>[解説]
	* <br>[備考]
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
		Knt900Form knt900Form = (Knt900Form) form;

		// buttonPushでセットされているコマンド
		String cmd = NullDefault.getString(req.getParameter("CMD"), "");

		log__.debug("CMD=" + cmd);

		//HttpServletRequestからリクエストモデルを取得します
		RequestModel reqMdl = getRequestModel(req);

		//リクエストモデルからログインユーザの情報を取得します。
		BaseUserModel usModel = reqMdl.getSmodel();

		int searched = 0;
		int ptn = 0;
		String prjId = NullDefault.getString(knt900Form.getPrjId(), "");
		String prjName = NullDefault.getString(knt900Form.getPrjName(), "");
		int sy = knt900Form.getStartYear();
		int sm = knt900Form.getStartMonth();
		int sd = knt900Form.getStartDay();
		int ey = knt900Form.getEndYear();
		int em = knt900Form.getEndMonth();
		int ed = knt900Form.getEndDay();
		int unit = knt900Form.getUnit();

		// 開始日が月の日数より大きい時
		Calendar scal=Calendar.getInstance();
		scal.clear();
		scal.set(Calendar.YEAR, sy);
		scal.set(Calendar.MONTH, sm -1);
		int slast = scal.getActualMaximum(Calendar.DATE);
		if (slast < sd) {
			sd = slast;
		}

		// 終了日が月の日数より大きい時
		Calendar ecal=Calendar.getInstance();
		ecal.clear();
		ecal.set(Calendar.YEAR, ey);
		ecal.set(Calendar.MONTH, em -1);
		int elast = ecal.getActualMaximum(Calendar.DATE);
		if (elast < ed) {
			ed = elast;
		}

		log__.debug("prjId=" + prjId);
		log__.debug("prjName=" + prjName);
		log__.debug("sy=" + sy);
		log__.debug("sm=" + sm);
		log__.debug("sd=" + sd);
		log__.debug("ey=" + ey);
		log__.debug("em=" + em);
		log__.debug("ed=" + ed);
		log__.debug("unit=" + unit);

		ArrayList<Knt900Model> prjList = new ArrayList<Knt900Model>();
		ArrayList<Knt900Model> usrList = new ArrayList<Knt900Model>();

		if(cmd.equals("")) {

			// 初期チェック
			unit = 0;

		} else {

			Knt900Dao dao = new Knt900Dao();

			// 検索条件の取得
			searched = Integer.parseInt(req.getParameter("search"));

			// IDが空の時は全件検索
			ptn = searched;
			if (ptn==0 && prjId.equals("")) {
				ptn = 99;
			}

			log__.debug("ptn=" + ptn);

			// プロジェクト一覧検索
			switch (ptn) {
			case 0:
				// IDで検索
				prjList = dao.selectByIdProjectTotalTime(prjId, 99, con);
				break;
			case 1:
				// 名称で検索
				prjList = dao.selectByNameProjectTotalTime(prjName, 99, con);
				break;
			case 2:
				// 期間で検索
				// チェック
				ActionErrors errors = new ActionErrors();
				Calendar calS = Calendar.getInstance();
				Calendar calE = Calendar.getInstance();
				calS.set(sy, sm - 1, sd);
				calE.set(ey, em - 1, ed);
				if (calE.before(calS)) {
					errors.add("fromTo", new ActionMessage("終了期間が開始期間より以前です。"));
				}
				if (!errors.isEmpty()) {
					addErrors(req, errors);
				} else {
					if (unit == 1) {
						// 社員単位（開始年月から終了年月まで回す）
						while (calE.after(calS)){
							int y = calS.get(Calendar.YEAR);
							int m = calS.get(Calendar.MONTH) + 1;
							int d = calS.get(Calendar.DATE);
							int last = calS.getActualMaximum(Calendar.DATE);
							log__.debug("user_fym=" + String.valueOf(y) + String.valueOf(m));
							prjList.addAll(dao.selectProjectUserList(y, m, d, y, m, last, 99, con));
							calS.add(Calendar.MONTH, 1);
						}
					} else {
						// プロジェクト単位
						prjList = dao.selectByStartEndProjectList(sy, sm, sd, ey, em, ed, 99, con);
					}
				}
				break;
			default:
				// 全件検索
				prjList = dao.selectProjectTotalTime(99, con);
				break;
			}

			// 社員単位（プロジェクトID(4桁)、ユーザID(Gsessionのもの)、年月、時間）
//			log__.debug("prjList_size=" + prjList.size());
//			if (unit == 1) {
//				ArrayList<Knt900Model> prjUsrList = new ArrayList<Knt900Model>();
//				for(int i=0; i < prjList.size(); i++) {
//					// キャスト
//					Knt900Model model = (Knt900Model)prjList.get(i);
//					// モデルからデータを取得
//					log__.debug("getStartYear1=" + model.getStartYear());
//					if (model.getStartYear() != 0) { 
//						prjUsrList.addAll(dao.selectProjectUserList(model, 99, con));
//					}
//				}
//				prjList = prjUsrList;
//			}

			// エクスポート処理の場合
			if (cmd.startsWith("export_csv")) {
				log__.debug("エクスポート処理");
				//テンポラリディレクトリパスを取得
				String tempDir = System.getProperty("java.io.tmpdir");
				// 出力名
				String fileName =  "経営情報.csv";
				String fullPath = tempDir + fileName;
				log__.debug("fullPath=" + fullPath);
				// エクスポート処理
				if (unit == 0) {
					exportProject(prjList, fullPath, con);
				} else if (unit == 1){
					exportProjectUsers(prjList, fullPath, con);
				}
				// ファイルがダウンロードされる処理っぽい
				TempFileUtil.downloadAtachment(req, res, fullPath, fileName, Encoding.UTF_8);
			}

		}

		req.setAttribute("searched", searched);
		req.setAttribute("prjId", prjId);
		req.setAttribute("prjName", prjName);
		req.setAttribute("startY", sy);
		req.setAttribute("startM", sm);
		req.setAttribute("startD", sd);
		req.setAttribute("endY", ey);
		req.setAttribute("endM", em);
		req.setAttribute("endD", ed);
		req.setAttribute("unit", unit);
		req.setAttribute("prjList", prjList);

		forward = map.getInputForward();

		return forward;

	}

	/**
	 * <br>[機能] エクスポート処理を実行
	 * <br>[解説]
	 * <br>[備考]
	 * @param prjList  プロジェクト一覧
	 * @param filePath 出力ファイルパス
	 * @param con コネクション
	 * @param outDir 出力先ディレクトリ
	 * @return ActionForward
	 * @throws Exception 実行例外
	 */
	private ActionForward exportProject(ArrayList<Knt900Model> prjList, String filePath, Connection con) throws Exception {

		// ファイルパス
		File file = new File(filePath);

		try {

			// ファイルの存在を確認
			Boolean fileExists = file.exists();

			// ファイルを生成
			if (!fileExists) {
				Boolean createNewFile = file.createNewFile();
			}

			// 初期化
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));

			if (checkBeforeWritefile(file)){

				for(int i=0; i<prjList.size(); i++) {
					// キャスト
					Knt900Model model = (Knt900Model)prjList.get(i);
					// モデルからデータを取得
					int psid = model.getPrjSid();
					String pid = model.getPrjId();
					String pn = model.getPrjName();
					int sy = model.getStartYear();
					int sm = model.getStartMonth();
					int sd = model.getStartDay();
					int ey = model.getEndYear();
					int em = model.getEndMonth();
					int ed = model.getEndDay();
					double pt = model.getPrjTime();
					String sdate = sy + "/" + sm + "/" + sd;
					String edate = ey + "/" + em + "/" + ed;
					// 1行レコード作成
					List<String> outList = new ArrayList<String>();
					outList.add("'" + pid + "'");
					outList.add(pn);
					outList.add(sdate);
					outList.add(edate);
					outList.add(String.format("%02f", pt));
					String data = String.join(",", outList);
					// 1行書き込み
					pw.println(data);
				}

				pw.close();

			}else{
				log__.error("ファイルに書き込めません" + filePath);
			}

		}catch(IOException e){
			log__.error("エクスポート処理に失敗しました。");
		}

		return null;

	}

	/**
	 * <br>[機能] エクスポート処理を実行
	 * <br>[解説]
	 * <br>[備考]
	 * @param prjList  プロジェクト一覧
	 * @param filePath 出力ファイルパス
	 * @param con コネクション
	 * @param outDir 出力先ディレクトリ
	 * @return ActionForward
	 * @throws Exception 実行例外
	 */
	private ActionForward exportProjectUsers(ArrayList<Knt900Model> prjList, String filePath, Connection con) throws Exception {

		// ファイルパス
		File file = new File(filePath);

		try {

			// ファイルの存在を確認
			Boolean fileExists = file.exists();

			// ファイルを生成
			if (!fileExists) {
				Boolean createNewFile = file.createNewFile();
			}

			// 初期化
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));

			if (checkBeforeWritefile(file)){

				for(int i=0; i<prjList.size(); i++) {
					// キャスト
					Knt900Model model = (Knt900Model)prjList.get(i);
					// モデルからデータを取得
					int psid = model.getPrjSid();
					int usrSid = model.getUsrSid();
					String pid = model.getPrjId();
					double pt = model.getPrjTime();
					String ym = model.getUsrYear() + "/" + model.getUsrMonth();
					// 1行レコード作成
					List<String> outList = new ArrayList<String>();
					outList.add("'" + pid + "'");
					outList.add(usrSid + "");
					outList.add(ym);
					outList.add(String.format("%02f", pt));
					String data = String.join(",", outList);
					// 1行書き込み
					pw.println(data);
				}

				pw.close();

			}else{
				log__.error("ファイルに書き込めません" + filePath);
			}

		}catch(IOException e){
			log__.error("エクスポート処理に失敗しました。");
		}

		return null;

	}

	/**
	 * <br>[機能] ファイルが書き込み可能かを判断
	 * <br>[解説]
	 * <br>[備考]
	 * @param  file ファイルパス
	 * @return ActionForward
	 * @throws Exception 実行例外
	 */
	private static boolean checkBeforeWritefile(File file){

		if (file.exists()){
			if (file.isFile() && file.canWrite()){
				return true;
			}
		}

		return false;

	}

}
