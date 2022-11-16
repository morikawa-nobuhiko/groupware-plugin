package jp.groupsession.v2.kintai.Knt500;

/**
 * <br>[機 能] プロジェクト担当者情報を保持するModelクラス
 * <br>[解 説]
 * <br>[備 考]
 *
 * @author JTS
 */
public class Knt500TntModel {

	/** プロジェクトSID */
	private int prjSid__ = -1;
	/** ユーザーSID */
	private int usrSid__ = -1;

	/**
	 * <p>prjSid を取得します。
	 * @return prjSid
	 */
	public int getPrjSid() {
		return prjSid__;
	}

	/**
	 * <p>prjSid をセットします。
	 * @param prjSid prjSid
	 */
	public void setPrjSid(int prjSid) {
		prjSid__ = prjSid;
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

}
