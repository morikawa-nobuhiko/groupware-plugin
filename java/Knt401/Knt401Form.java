package jp.groupsession.v2.kintai.Knt401;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMapping;
import jp.co.sjts.util.struts.StrutsUtil;
import jp.groupsession.v2.struts.AbstractGsForm;

	/**
	* <br>[機  能] Monthlyのフォームクラス
	* <br>[解  説]
	* <br>[備  考]
	*
	* @author JTS
	*/
	public class Knt401Form extends AbstractGsForm {

	/** 作業ID */
	private int wrkId__ = 0;
	/** 作業ID */
	private int delWrkId__ = -1;
	/** 作業名称 */
	private String wrkName__ = null;

	/**
	 * <br>[機  能] 入力チェックを行う
	 * <br>[解  説]
	 * <br>[備  考]
	 * @return エラー
	 * @throws SQLException SQL実行時例外
	 */
	public ActionErrors validateCheck()
	throws SQLException {

		ActionErrors errors = new ActionErrors();

		String pn = this.getWrkName();
		pn = pn.replaceAll(" ","");
		pn = pn.replaceAll("　","");
		if (pn == "") {
			errors.add("wrkName", new ActionMessage("作業名称は必須項目です。"));
		}

		return errors;

	}

	/**
	 * <p>WrkId を取得します。
	 * @return WrkId
	 */
	public int getWrkId() {
		return wrkId__;
	}

	/**
	 * <p>WrkId をセットします。
	 * @param WrkId WrkId
	 */
	public void setWrkId(int wrkId) {
		wrkId__ = wrkId;
	}

	/**
	 * <p>delWrkId を取得します。
	 * @return delWrkId
	 */
	public int getDelWrkId() {
		return delWrkId__;
	}

	/**
	 * <p>delWrkId をセットします。
	 * @param delWrkId delWrkId
	 */
	public void setDelWrkId(int delWrkId) {
		delWrkId__ = delWrkId;
	}

	/**
	 * <p>wrkName を取得します。
	 * @return wrkName
	 */
	public String getWrkName() {
		return wrkName__;
	}

	/**
	 * <p>wrkName をセットします。
	 * @param wrkName wrkName
	 */
	public void setWrkName(String wrkName) {
		wrkName__ = wrkName;
	}

}