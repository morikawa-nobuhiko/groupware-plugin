package jp.groupsession.v2.kintai.Knt500;
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
import java.nio.charset.StandardCharsets;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import jp.co.sjts.util.Encoding;
import jp.co.sjts.util.NullDefault;
import jp.co.sjts.util.date.UDate;
import jp.co.sjts.util.jdbc.JDBCUtil;
import jp.co.sjts.util.jdbc.SqlBuffer;
import jp.co.sjts.util.http.TempFileUtil;
import jp.groupsession.v2.struts.AbstractGsAction;
import jp.groupsession.v2.cmn.GSConst;
import jp.groupsession.v2.cmn.GSConstSchedule;
import jp.groupsession.v2.cmn.config.PluginConfig;
import jp.groupsession.v2.cmn.dao.GroupModel;
import jp.groupsession.v2.cmn.dao.BaseUserModel;
import jp.groupsession.v2.cmn.dao.UsidSelectGrpNameDao;
import jp.groupsession.v2.cmn.dao.BaseUserModel;
import jp.groupsession.v2.cmn.model.RequestModel;
import jp.groupsession.v2.kintai.Knt302.Knt302Dao;
import jp.groupsession.v2.kintai.Knt302.Knt302UserModel;

import javax.servlet.http.*;
import org.apache.struts.action.*;
//(1)FormFileインタフェースのインポート
import org.apache.struts.upload.FormFile;
import java.io.*;

/**
 * <br>[機  能] Knt500のアクションクラス
 * <br>[解  説]
 * <br>[備  考]
 *
 * @author JTS
 */
public class Knt500Action extends AbstractGsAction {

	/** Logging インスタンス */
	private static Log log__ = LogFactory.getLog(Knt500Action.class);

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

		log__.debug("executeAction_start");

		ActionForward forward = null;
		ActionErrors errors = new ActionErrors();
		Knt500Form knt500Form = (Knt500Form) form;
		Knt500Dao dao500 = new Knt500Dao();

		//HttpServletRequestからリクエストモデルを取得します
		RequestModel reqMdl = getRequestModel(req);
		//リクエストモデルからログインユーザの情報を取得します。
		BaseUserModel usModel = reqMdl.getSmodel();

		// buttonPushでセットされているコマンド
		String cmd = NullDefault.getString(req.getParameter("CMD"), "");

		log__.debug("CMD=" + cmd);

		boolean commit = false;

		if (cmd.equals("")) {

			errors.add("init", new ActionMessage("初回メッセージ"));
			addErrors(req, errors);

		} else if (cmd.equals("exportProject")) {

			log__.debug("プロジェクトエクスポート");

			ArrayList<Knt500Model> prjList = new ArrayList<Knt500Model>();
			prjList = dao500.selectProject(con);
			//テンポラリディレクトリパスを取得
			String tempDir = System.getProperty("java.io.tmpdir");
			// 出力名
			String fileName =  "プロジェクト（メイン）.csv";
			String fullPath = tempDir + fileName;
			// エクスポート処理
			exportProject(prjList, fullPath, con);
			// ファイルダウンロード
			TempFileUtil.downloadAtachment(req, res, fullPath, fileName, Encoding.SHIFT_JIS);

		} else if (cmd.equals("importProject")) {

			log__.debug("プロジェクトインポート");

			// アクセスメソッドを使用してFormFileオブジェクトの取得
			FormFile fileUp = knt500Form.getFileUp();
			// getInputStreamメソッドを使用し入力ストリームを取得
			InputStream is = fileUp.getInputStream();
			// 入力ストリームをバッファリング
			BufferedInputStream inBuffer = new BufferedInputStream(is);
			// BufferedReaderに変換
			BufferedReader br = new BufferedReader(new InputStreamReader(inBuffer, Encoding.SHIFT_JIS));

			try {

				// ファイルを読み込む
				byte[] buf = new byte[256];
				// ファイルを読み込む
				String line = null;
				while((line = br.readLine()) != null) {
					// 1行読み込み
					log__.debug("line=" + line);
					// lineをカンマで分割し、配列dataに設定
					String[] data = line.trim().split(",");
					// 登録 or 更新
					if (dao500.containsProject(Integer.parseInt(data[0].trim()), con)) {
						// 更新
						dao500.updateProject(data, con);
					} else {
						// 新規
						dao500.insertProject(data, con);
					}
//					dao500.executeProject(line, con);
				}

				// コミット
				con.commit();

			} catch (Exception e) {
				log__.error("プロジェクト担当者の更新に失敗", e);
				throw e;
			} finally {
				inBuffer.close();
				br.close();
				if (!commit) {
					con.rollback();
				}
			}

			// 一時領域のアップロードデータを削除
			fileUp.destroy();

			// ファイル未選択
			if (fileUp.getFileName().equals("")) {
				errors.add("nofile", new ActionMessage("インポートCSVファイルが選択されていません。"));
				addErrors(req, errors);
			} else {
				errors.add("registed", new ActionMessage("登録しました。"));
				addErrors(req, errors);
			}

		} else if (cmd.equals("exportProjectTanto")) {

			log__.debug("プロジェクト担当者エクスポート");

			ArrayList<Knt500TntModel> tntList = new ArrayList<Knt500TntModel>();
			tntList = dao500.selectProjectTanto(con);
			//テンポラリディレクトリパスを取得
			String tempDir = System.getProperty("java.io.tmpdir");
			// 出力名
			String fileName =  "プロジェクト（担当者）.csv";
			String fullPath = tempDir + fileName;
			// エクスポート処理
			exportProjectTanto(tntList, fullPath, con);
			// ファイルダウンロード
			TempFileUtil.downloadAtachment(req, res, fullPath, fileName, Encoding.SHIFT_JIS);

		} else if (cmd.equals("importProjectTanto")) {

			log__.debug("プロジェクト担当者インポート");

			// アクセスメソッドを使用してFormFileオブジェクトの取得
			FormFile fileUp = knt500Form.getFileUp();
			// getInputStreamメソッドを使用し入力ストリームを取得
			InputStream is = fileUp.getInputStream();
			// 入力ストリームをバッファリング
			BufferedInputStream inBuffer = new BufferedInputStream(is);
			// BufferedReaderに変換
			BufferedReader br = new BufferedReader(new InputStreamReader(inBuffer, Encoding.SHIFT_JIS));

			try {

				// プロジェクト担当者全削除
				dao500.deleteAllProjectTanto(con);
				// ファイルを読み込む
				String line = null;
				while((line = br.readLine()) != null) {
					// 1行読み込み
					log__.debug("line=" + line);
					// lineをカンマで分割し、配列dataに設定
					String[] data = line.trim().split(",");
					// 新規
					dao500.insertProjectTanto(data, con);
				}

				// コミット
				con.commit();

			} catch (Exception e) {
				log__.error("プロジェクト担当者の更新に失敗", e);
				throw e;
			} finally {
				inBuffer.close();
				br.close();
				if (!commit) {
					con.rollback();
				}
			}

			// 一時領域のアップロードデータを削除
			fileUp.destroy();

			// ファイル未選択
			if (fileUp.getFileName().equals("")) {
				errors.add("nofile", new ActionMessage("インポートCSVファイルが選択されていません。"));
				addErrors(req, errors);
			} else {
				errors.add("registed", new ActionMessage("登録しました。"));
				addErrors(req, errors);
			}

		} else if (cmd.equals("exportWork")) {

			log__.debug("作業エクスポート");

			ArrayList<Knt500WrkModel> wrkList = new ArrayList<Knt500WrkModel>();
			wrkList = dao500.selectWorkList(con);
			//テンポラリディレクトリパスを取得
			String tempDir = System.getProperty("java.io.tmpdir");
			// 出力名
			String fileName =  "プロジェクト（作業）.csv";
			String fullPath = tempDir + fileName;
			// エクスポート処理
			exportWork(wrkList, fullPath, con);
			// ファイルダウンロード
			TempFileUtil.downloadAtachment(req, res, fullPath, fileName, Encoding.SHIFT_JIS);

		} else if (cmd.equals("importWork")) {

			log__.debug("作業インポート");

			// アクセスメソッドを使用してFormFileオブジェクトの取得
			FormFile fileUp = knt500Form.getFileUp();
			// getInputStreamメソッドを使用し入力ストリームを取得
			InputStream is = fileUp.getInputStream();
			// 入力ストリームをバッファリング
			BufferedInputStream inBuffer = new BufferedInputStream(is);
			// BufferedReaderに変換
			BufferedReader br = new BufferedReader(new InputStreamReader(inBuffer, Encoding.SHIFT_JIS));

			try {

				// 作業全削除
				dao500.deleteAllWork(con);
				// ファイルを読み込む
				String line = null;
				while((line = br.readLine()) != null) {
					// 1行読み込み
					log__.debug("line=" + line);
					// lineをカンマで分割し、配列dataに設定
					String[] data = line.trim().split(",");
					// 新規
					dao500.insertWork(data, con);
				}

				// コミット
				con.commit();

			} catch (Exception e) {
				log__.error("作業の更新に失敗", e);
				throw e;
			} finally {
				inBuffer.close();
				br.close();
				if (!commit) {
					con.rollback();
				}
			}

			// 一時領域のアップロードデータを削除
			fileUp.destroy();

			// ファイル未選択
			if (fileUp.getFileName().equals("")) {
				errors.add("nofile", new ActionMessage("インポートCSVファイルが選択されていません。"));
				addErrors(req, errors);
			} else {
				errors.add("registed", new ActionMessage("登録しました。"));
				addErrors(req, errors);
			}

		} else if (cmd.equals("exportStamp")) {

			log__.debug("実績エクスポート");

			ArrayList<Knt500JskModel> jskList = new ArrayList<Knt500JskModel>();
			jskList = dao500.selectJskList(con);
			//テンポラリディレクトリパスを取得
			String tempDir = System.getProperty("java.io.tmpdir");
			// 出力名
			String fileName =  "実績.csv";
			String fullPath = tempDir + fileName;
			// エクスポート処理
			exportJsk(jskList, fullPath, con);
			// ファイルダウンロード
			TempFileUtil.downloadAtachment(req, res, fullPath, fileName, Encoding.SHIFT_JIS);

		} else if (cmd.equals("importStamp")) {

			log__.debug("実績インポート");

			// アクセスメソッドを使用してFormFileオブジェクトの取得
			FormFile fileUp = knt500Form.getFileUp();
			// getInputStreamメソッドを使用し入力ストリームを取得
			InputStream is = fileUp.getInputStream();
			// 入力ストリームをバッファリング
			BufferedInputStream inBuffer = new BufferedInputStream(is);
			// BufferedReaderに変換
			BufferedReader br = new BufferedReader(new InputStreamReader(inBuffer, Encoding.SHIFT_JIS));

			try {

				// ファイルを読み込む
				String line = null;
				while((line = br.readLine()) != null) {
					// 1行読み込み
					log__.debug("line=" + line);
					// lineをカンマで分割し、配列dataに設定
					String[] data = line.trim().split(",");
					// 実績削除
					dao500.deleteJsk(data, con);
					// 実績登録
					dao500.insertJsk(data, con);
				}

				// コミット
				con.commit();

			} catch (Exception e) {
				log__.error("実績の更新に失敗", e);
				throw e;
			} finally {
				inBuffer.close();
				br.close();
				if (!commit) {
					con.rollback();
				}
			}

			// 一時領域のアップロードデータを削除
			fileUp.destroy();

			// ファイル未選択
			if (fileUp.getFileName().equals("")) {
				errors.add("nofile", new ActionMessage("インポートCSVファイルが選択されていません。"));
				addErrors(req, errors);
			} else {
				errors.add("registed", new ActionMessage("登録しました。"));
				addErrors(req, errors);
			}

		} else if (cmd.equals("exportUserStamp")) {

			log__.debug("申請エクスポート");

			ArrayList<Knt500UsrModel> usrList = new ArrayList<Knt500UsrModel>();
			usrList = dao500.selectUsrWork(con);
			//テンポラリディレクトリパスを取得
			String tempDir = System.getProperty("java.io.tmpdir");
			// 出力名
			String fileName =  "申請.csv";
			String fullPath = tempDir + fileName;
			// エクスポート処理
			exportUsrWork(usrList, fullPath, con);
			// ファイルダウンロード
			TempFileUtil.downloadAtachment(req, res, fullPath, fileName, Encoding.SHIFT_JIS);

		} else if (cmd.equals("importUserStamp")) {

			log__.debug("実績インポート");

			// アクセスメソッドを使用してFormFileオブジェクトの取得
			FormFile fileUp = knt500Form.getFileUp();
			// getInputStreamメソッドを使用し入力ストリームを取得
			InputStream is = fileUp.getInputStream();
			// 入力ストリームをバッファリング
			BufferedInputStream inBuffer = new BufferedInputStream(is);
			// BufferedReaderに変換
			BufferedReader br = new BufferedReader(new InputStreamReader(inBuffer, Encoding.SHIFT_JIS));

			try {

				// ファイルを読み込む
				String line = null;
				while((line = br.readLine()) != null) {
					// 1行読み込み
					log__.debug("line=" + line);
					// lineをカンマで分割し、配列dataに設定
					String[] data = line.trim().split(",");
					// 申請削除
					dao500.deleteUsrJsk(data, con);
					// 申請登録
					dao500.insertUsrJsk(data, con);
				}

				// コミット
				con.commit();

			} catch (Exception e) {
				log__.error("申請の更新に失敗", e);
				throw e;
			} finally {
				inBuffer.close();
				br.close();
				if (!commit) {
					con.rollback();
				}
			}

			// 一時領域のアップロードデータを削除
			fileUp.destroy();

			// ファイル未選択
			if (fileUp.getFileName().equals("")) {
				errors.add("nofile", new ActionMessage("インポートCSVファイルが選択されていません。"));
				addErrors(req, errors);
			} else {
				errors.add("registed", new ActionMessage("登録しました。"));
				addErrors(req, errors);
			}

		}

		forward = map.getInputForward();

		return forward;

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

	/**
	 * <br>[機能] プロジェクトエクスポート処理を実行
	 * <br>[解説]
	 * <br>[備考]
	 * @param prjList  プロジェクト一覧
	 * @param filePath 出力ファイルパス
	 * @param con コネクション
	 * @param outDir 出力先ディレクトリ
	 * @return ActionForward
	 * @throws Exception 実行例外
	 */
	private ActionForward exportProject(ArrayList<Knt500Model> prjList, String filePath, Connection con) throws Exception {

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
					Knt500Model model = (Knt500Model)prjList.get(i);
					// モデルからデータを取得
					int sy = model.getStartYear();
					int sm = model.getStartMonth();
					int sd = model.getStartDay();
					int ey = model.getEndYear();
					int em = model.getEndMonth();
					int ed = model.getEndDay();
					String prjSid = String.valueOf(model.getPrjSid());
					String prjId = String.valueOf(model.getPrjId());
					String prjName = model.getPrjName();
					String ind = String.valueOf(model.getIndirect());
					String sdate = sy + "-" + sm + "-" + sd + " 00:00:00";
					String edate = ey + "-" + em + "-" + ed + " 23:59:59";

					String orderName = model.getOrderName();
					String targetB =  String.valueOf(model.getTargetB());
					String targetS =  String.valueOf(model.getTargetS());
					String targetH =  String.valueOf(model.getTargetH());
					String targetJ =  String.valueOf(model.getTargetJ());
					String chiefName = model.getChiefName();
					String managerName = model.getManagerName();
					String remark = model.getRemark();

					// 1行レコード作成
					List<String> outList = new ArrayList<String>();

//					outList.add("insert into KNT_PRJ");
//					outList.add("(PRJ_SID, PRJ_ID, PRJ_NAME, PRJ_START, PRJ_END, PRJ_INDIRECT)");
//					outList.add("VALUES(");
//					outList.add(prjSid);
//					outList.add(",");
//					outList.add("'" + prjId + "'");
//					outList.add(",");
//					outList.add("'" + prjName + "'");
//					outList.add(",");
//					outList.add("'" + sdate + "'");
//					outList.add(",");
//					outList.add("'" + edate + "'");
//					outList.add(",");
//					outList.add(ind);
//					outList.add(")");

					outList.add(prjSid);
					outList.add(",");
					outList.add(prjId);
					outList.add(",'");
					outList.add(prjName);
					outList.add("',");
					outList.add(sdate);
					outList.add(",");
					outList.add(edate);
					outList.add(",");
					outList.add(ind);
					outList.add(",'");
					outList.add(orderName);
					outList.add("',");
					outList.add(targetB);
					outList.add(",");
					outList.add(targetS);
					outList.add(",");
					outList.add(targetH);
					outList.add(",");
					outList.add(targetJ);
					outList.add(",'");
					outList.add(chiefName);
					outList.add("','");
					outList.add(managerName);
					outList.add("','");
					outList.add(remark);
					outList.add("'");

					String data = String.join("", outList);
					// 1行書き込み
					pw.println(data);
				}

				pw.close();

			}else{
				log__.error("ファイルに書き込めません" + filePath);
			}

		}catch(IOException e){
			log__.error("プロジェクトエクスポート処理に失敗しました。");
		}

		return null;

	}

	/**
	 * <br>[機能] 担当者エクスポート処理を実行
	 * <br>[解説]
	 * <br>[備考]
	 * @param tntList  プロジェクト担当者一覧
	 * @param filePath 出力ファイルパス
	 * @param con コネクション
	 * @param outDir 出力先ディレクトリ
	 * @return ActionForward
	 * @throws Exception 実行例外
	 */
	private ActionForward exportProjectTanto(ArrayList<Knt500TntModel> tntList, String filePath, Connection con) throws Exception {

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

				for(int i=0; i<tntList.size(); i++) {
					// キャスト
					Knt500TntModel model = (Knt500TntModel)tntList.get(i);
					// モデルからデータを取得
					String prjSid = String.valueOf(model.getPrjSid());
					String usrSid = String.valueOf(model.getUsrSid());
					// 1行レコード作成
					List<String> outList = new ArrayList<String>();
					outList.add(prjSid);
					outList.add(",");
					outList.add(usrSid);
					String data = String.join("", outList);
					// 1行書き込み
					pw.println(data);
				}

				pw.close();

			}else{
				log__.error("ファイルに書き込めません" + filePath);
			}

		}catch(IOException e){
			log__.error("プロジェクト担当者のエクスポート処理に失敗しました。");
		}

		return null;

	}

	/**
	 * <br>[機能] 作業エクスポート処理を実行
	 * <br>[解説]
	 * <br>[備考]
	 * @param wrkList  作業一覧
	 * @param filePath 出力ファイルパス
	 * @param con コネクション
	 * @param outDir 出力先ディレクトリ
	 * @return ActionForward
	 * @throws Exception 実行例外
	 */
	private ActionForward exportWork(ArrayList<Knt500WrkModel> wrkList, String filePath, Connection con) throws Exception {

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

				for(int i=0; i<wrkList.size(); i++) {
					// キャスト
					Knt500WrkModel model = (Knt500WrkModel)wrkList.get(i);
					// モデルからデータを取得
					String wrkId = String.valueOf(model.getWrkId());
					String wrkName = String.valueOf(model.getWrkName());
					// 1行レコード作成
					List<String> outList = new ArrayList<String>();
					outList.add(wrkId);
					outList.add(",'");
					outList.add(wrkName);
					outList.add("'");
					String data = String.join("", outList);
					// 1行書き込み
					pw.println(data);
				}

				pw.close();

			}else{
				log__.error("ファイルに書き込めません" + filePath);
			}

		}catch(IOException e){
			log__.error("作業のエクスポート処理に失敗しました。");
		}

		return null;

	}

	/**
	 * <br>[機能] 実績エクスポート処理を実行
	 * <br>[解説]
	 * <br>[備考]
	 * @param prjList  プロジェクト一覧
	 * @param filePath 出力ファイルパス
	 * @param con コネクション
	 * @param outDir 出力先ディレクトリ
	 * @return ActionForward
	 * @throws Exception 実行例外
	 */
	private ActionForward exportJsk(ArrayList<Knt500JskModel> jskList, String filePath, Connection con) throws Exception {

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

				for(int i=0; i<jskList.size(); i++) {
					// キャスト
					Knt500JskModel model = (Knt500JskModel)jskList.get(i);
					// 1行レコード作成
					List<String> outList = new ArrayList<String>();
					outList.add(String.valueOf(model.getUsrSid()));
					outList.add(",");
					outList.add(model.getStpTime());
					outList.add(",");
					outList.add(String.valueOf(model.getIomId()));
					String data = String.join("", outList);
					// 1行書き込み
					pw.println(data);
				}

				pw.close();

			}else{
				log__.error("ファイルに書き込めません" + filePath);
			}

		}catch(IOException e){
			log__.error("実績エクスポート処理に失敗しました。");
		}

		return null;

	}

	/**
	 * <br>[機能] 申請エクスポート処理を実行
	 * <br>[解説]
	 * <br>[備考]
	 * @param prjList  プロジェクト一覧
	 * @param filePath 出力ファイルパス
	 * @param con コネクション
	 * @param outDir 出力先ディレクトリ
	 * @return ActionForward
	 * @throws Exception 実行例外
	 */
	private ActionForward exportUsrWork(ArrayList<Knt500UsrModel> usrList, String filePath, Connection con) throws Exception {

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

				for(int i=0; i<usrList.size(); i++) {
					// キャスト
					Knt500UsrModel model = (Knt500UsrModel)usrList.get(i);
					// 1行レコード作成
					List<String> outList = new ArrayList<String>();
					outList.add(String.valueOf(model.getUsrSid()));
					outList.add(",");
					outList.add(model.getJskYmd());
					outList.add(",");
					outList.add(String.valueOf(model.getJskId()));
					outList.add(",");
					outList.add(String.valueOf(model.getPrjSid()));
					outList.add(",");
					outList.add(String.valueOf(model.getWrkId()));
					outList.add(",");
					outList.add(String.valueOf(model.getWrkTime()));
					outList.add(",");
					outList.add(model.getRemark());
					String data = String.join("", outList);
					// 1行書き込み
					pw.println(data);
				}

				pw.close();

			}else{
				log__.error("ファイルに書き込めません" + filePath);
			}

		}catch(IOException e){
			log__.error("実績エクスポート処理に失敗しました。");
		}

		return null;

	}

}
