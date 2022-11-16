package jp.groupsession.v2.kintai.Knt100;

/**
 * <br>[機 能] 情報を保持するModelクラス
 * <br>[解 説]
 * <br>[備 考]
 *
 * @author JTS
 */
public class Knt100UserModel {

	/** USR_ID */
	private int usrSid__ = -1;
	/** USR_NAME */
	private String usrName__ = "";
	/** STP_TIME */
	private double stpTime__ = 0;
	/** STP_JSK */
	private String stpJsk__ = "";
	/** USR_TIME */
	private double usrTime__ = 0;
	/** USR_SYN 承認 */
	private boolean usrSyn__ = false;

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

	/**
	 * <p>stpTime を取得します。
	 * @return stpTime
	 */
	public double getStpTime() {
		return stpTime__;
	}

	/**
	 * <p>stpTime をセットします。
	 * @param stpTime stpTime
	 */
	public void setStpTime(double stpTime) {
		stpTime__ = stpTime;
	}

	/**
	 * <p>usrTime を取得します。
	 * @return usrTime
	 */
	public double getUsrTime() {
		return usrTime__;
	}

	/**
	 * <p>usrTime をセットします。
	 * @param usrTime usrTime
	 */
	public void setUsrTime(double usrTime) {
		usrTime__ = usrTime;
	}

	/**
	 * <p>stpJsk を取得します。
	 * @return stpJsk
	 */
	public String getStpJsk() {
		return stpJsk__;
	}

	/**
	 * <p>stpJsk をセットします。
	 * @param stpJsk stpJsk
	 */
	public void setStpJsk(String stpJsk) {
		stpJsk__ = stpJsk;
	}

	/**
	 * <p>usrSyn を取得します。
	 * @return usrSyn
	 */
	public boolean getUsrSyn() {
		return usrSyn__;
	}

	/**
	 * <p>usrSyn をセットします。
	 * @param usrSyn usrSyn
	 */
	public void setUsrSyn(boolean usrSyn) {
		usrSyn__ = usrSyn;
	}

}
