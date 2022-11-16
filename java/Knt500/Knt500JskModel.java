package jp.groupsession.v2.kintai.Knt500;

/**
 * <br>[機 能] 実績を保持するModelクラス
 * <br>[解 説]
 * <br>[備 考]
 *
 * @author JTS
 */
public class Knt500JskModel {

	/** ユーザSID */
	private int usrSid__ = -1;
	/** 日時 */
	private String stpTime__ = "";
	/** 出退勤 */
	private int iomId__ = -1;

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
	 * <p>StpTime を取得します。
	 * @return StpTime
	 */
	public String getStpTime() {
		return stpTime__;
	}

	/**
	 * <p>StpTime をセットします。
	 * @param StpTime StpTime
	 */
	public void setStpTime(String stpTime) {
		stpTime__ = stpTime;
	}

	/**
	 * <p>iomId を取得します。
	 * @return iomId
	 */
	public int getIomId() {
		return iomId__;
	}

	/**
	 * <p>iomId をセットします。
	 * @param iomId iomId
	 */
	public void setIomId(int iomId) {
		iomId__ = iomId;
	}

}
