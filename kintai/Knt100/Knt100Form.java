package jp.groupsession.v2.kintai.Knt100;

import jp.groupsession.v2.struts.AbstractGsForm;

/**
* <br>[機  能] Monthlyのフォームクラス
* <br>[解  説]
* <br>[備  考]
*
* @author JTS
*/
public class Knt100Form extends AbstractGsForm {

	/** プロジェクトID */
	private int grpId__ = -1;
	/** プロジェクト名称 */
	private String grpName__ = null;

	/**
	 * <p>grpId を取得します。
	 * @return grpId
	 */
	public int getGrpId() {
		return grpId__;
	}

	/**
	 * <p>grpId をセットします。
	 * @param grpId grpId
	 */
	public void setGrpId(int grpId) {
		grpId__ = grpId;
	}

}