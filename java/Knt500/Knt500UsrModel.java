package jp.groupsession.v2.kintai.Knt500;

/**
 * <br>[機 能] 実績を保持するModelクラス
 * <br>[解 説]
 * <br>[備 考]
 *
 * @author JTS
 */
public class Knt500UsrModel {

	/** ユーザSID */
	private int usrSid__ = -1;
	/** 作業年月日 */
	private String jskYmd__ = "";
	/** 識別ID */
	private int jskId__ = -1;
	/** プロジェクトSID */
	private int prjSid__ = -1;
	/** 作業ID */
	private int wrkId__ = -1;
	/** 作業時間 */
	private double wrkTime__ = 0;
	/** 備考 */
	private String remark__ = "";

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
	 * <p>JskYmd を取得します。
	 * @return JskYmd
	 */
	public String getJskYmd() {
		return jskYmd__;
	}

	/**
	 * <p>JskYmd をセットします。
	 * @param JskYmd JskYmd
	 */
	public void setJskYmd(String jskYmd) {
		jskYmd__ = jskYmd;
	}

	/**
	 * <p>jskId を取得します。
	 * @return jskId
	 */
	public int getJskId() {
		return jskId__;
	}

	/**
	 * <p>jskId をセットします。
	 * @param jskId jskId
	 */
	public void setJskId(int jskId) {
		jskId__ = jskId;
	}

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
	 * <p>wrkId を取得します。
	 * @return wrkId
	 */
	public int getWrkId() {
		return wrkId__;
	}

	/**
	 * <p>wrkId をセットします。
	 * @param wrkId wrkId
	 */
	public void setWrkId(int wrkId) {
		wrkId__ = wrkId;
	}

	/**
	 * <p>wrkTime を取得します。
	 * @return wrkTime
	 */
	public double getWrkTime() {
		return wrkTime__;
	}

	/**
	 * <p>wrkTime をセットします。
	 * @param wrkTime wrkTime
	 */
	public void setWrkTime(double wrkTime) {
		wrkTime__ = wrkTime;
	}

	/**
	 * <p>remark を取得します。
	 * @return remark
	 */
	public String getRemark() {
		return remark__;
	}

	/**
	 * <p>remark をセットします。
	 * @param remark remark
	 */
	public void setRemark(String remark) {
		remark__ = remark;
	}

}
