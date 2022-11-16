package jp.groupsession.v2.kintai.Knt100;

/**
 * <br>[機 能] 情報を保持するModelクラス
 * <br>[解 説]
 * <br>[備 考]
 *
 * @author JTS
 */
public class Knt100GroupModel {

	/** GRP_ID */
	private int grpId__ = -1;
	/** GRP_NAME */
	private String grpName__ = "";

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

	/**
	 * <p>grpName を取得します。
	 * @return grpName
	 */
	public String getGrpName() {
		return grpName__;
	}

	/**
	 * <p>grpName をセットします。
	 * @param grpName grpName
	 */
	public void setGrpName(String grpName) {
		grpName__ = grpName;
	}

}
