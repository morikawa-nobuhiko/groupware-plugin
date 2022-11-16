package jp.groupsession.v2.kintai.Knt302;

/**
 * <br>[機 能] 情報を保持するModelクラス
 * <br>[解 説]
 * <br>[備 考]
 *
 * @author JTS
 */
public class Knt302UserModel {

	/** GRP_ID */
	private int grpId__ = -1;
	/** USR_ID */
	private int usrSid__ = -1;
	/** USR_NAME */
	private String usrName__ = "";

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
	 * <p>usrSid を取得します。
	 * @return usrSid
	 */
	public int getUsrSid() {
		return usrSid__;
	}

	/**
	 * <p>usrSid をセットします。
	 * @param usrSid usrSid
	 */
	public void setUsrSid(int usrSid) {
		usrSid__ = usrSid;
	}

	/**
	 * <p>usrName を取得します。
	 * @return usrName
	 */
	public String getUsrName() {
		return usrName__;
	}

	/**
	 * <p>usrName をセットします。
	 * @param usrName usrName
	 */
	public void setUsrName(String usrName) {
		usrName__ = usrName;
	}

}
