package jp.groupsession.v2.kintai.Knt001;

/**
 * <br>[機 能] ユーザーの作業実績情報を保持するModelクラス
 * <br>[解 説]
 * <br>[備 考]
 *
 * @author JTS
 */
public class Knt001Model {

	/** USR_SID */
	private int usrSid__ = -1;
	/** USR_PRJ_SID */
	private int prjSid__ = -1;
	/** USR_PRJ_ID */
	private int prjId__ = -1;
	/** USR_WRK_ID */
	private int wrkId__ = -1;
	/** USR_YYYY */
	private int wrkYear__ = -1;
	/** USR_MM */
	private int wrkMonth__ = -1;
	/** USR_DD */
	private int wrkDay__ = -1;
	/** WRK_TIME */
	private double wrkTime__ = -1;
	/** STP_TIME */
	private double stpTime__ = -1;

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
	 * <p>PrjSid を取得します。
	 * @return PrjSid
	 */
	public int getPrjSid() {
		return prjSid__;
	}

	/**
	 * <p>PrjSid をセットします。
	 * @param PrjSid PrjSid
	 */
	public void setPrjSid(int PrjSid) {
		prjSid__ = PrjSid;
	}

	/**
	 * <p>PrjId を取得します。
	 * @return PrjId
	 */
	public int getPrjId() {
		return prjId__;
	}

	/**
	 * <p>PrjId をセットします。
	 * @param PrjId PrjId
	 */
	public void setPrjId(int PrjId) {
		prjId__ = PrjId;
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
	 * <p>year を取得します。
	 * @return year
	 */
	public int getWrkYear() {
		return wrkYear__;
	}

	/**
	 * <p>year をセットします。
	 * @param year year
	 */
	public void setWrkYear(int year) {
		wrkYear__ = year;
	}

	/**
	 * <p>month を取得します。
	 * @return month
	 */
	public int getWrkMonth() {
		return wrkMonth__;
	}

	/**
	 * <p>month をセットします。
	 * @param month month
	 */
	public void setWrkMonth(int month) {
		wrkMonth__ = month;
	}

	/**
	 * <p>day を取得します。
	 * @return day
	 */
	public int getWrkDay() {
		return wrkDay__;
	}

	/**
	 * <p>day をセットします。
	 * @param day day
	 */
	public void setWrkDay(int day) {
		wrkDay__ = day;
	}

	/**
	 * <p>time を取得します。
	 * @return time
	 */
	public double getWrkTime() {
		return wrkTime__;
	}

	/**
	 * <p>time をセットします。
	 * @param time time
	 */
	public void setWrkTime(double time) {
		wrkTime__ = time;
	}

	/**
	 * <p>sptTime を取得します。
	 * @return time
	 */
	public double getStpTime() {
		return stpTime__;
	}

	/**
	 * <p>sptTime をセットします。
	 * @param double time
	 */
	public void setStpTime(double time) {
		stpTime__ = time;
	}

}
