package jp.groupsession.v2.kintai.Knt200;

/**
 * <br>[機 能] ユーザーの作業実績情報を保持するModelクラス
 * <br>[解 説]
 * <br>[備 考]
 *
 * @author JTS
 */
public class Knt200Model {

	/** USR_SID */
	private int usrSid__ = -1;
	/** USR_PRJ_SID */
	private int prjSid__ = -1;
	/** USR_WRK_ID */
	private int wrkId__ = -1;
	/** USR_YYYY */
	private int wrkYear__ = -1;
	/** USR_MM */
	private int wrkMonth__ = -1;
	/** USR_DD */
	private int wrkDay__ = -1;
	/** USR_WRK_TIME */
	private double wrkTime__ = -1;
	/** USR_PAID_TIME */
	private double paidTime__ = -1;
	/** SMU_RMK */
	private String smuRmk__ = "";
	/** SMU_CHK */
	private int smuChk__ = 0;
	/** DAIQ_CHK */
	private int daiQChk__ = 0;
	/** FIRST_INTIME */
	private String firstInTime__ = "";
	/** LAST_OUTTIME */
	private String lastOutTime__ = "";
	/** INTIME */
	private String inTime__ = "";
	/** OUTTIME */
	private String outTime__ = "";
	/** OVER_TIME */
	private double overTime__ = -1;
	/** MIDNIGHT_TIME */
	private double midnightTime__ = -1;
	/** HOLIDAY_TIME */
	private double holidayTime__ = -1;

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
	 * <p>paidTime を取得します。
	 * @return paidTime
	 */
	public double getPaidTime() {
		return paidTime__;
	}

	/**
	 * <p>paidTime をセットします。
	 * @param paidTime paidTime
	 */
	public void setPaidTime(double paidTime) {
		paidTime__ = paidTime;
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
	 * <p>smuRmk を取得します。
	 * @return smuRmk
	 */
	public String getSmuRmk() {
		return smuRmk__;
	}

	/**
	 * <p>smuRmk をセットします。
	 * @param smuRmk smuRmk
	 */
	public void setSmuRmk(String smuRmk) {
		smuRmk__ = smuRmk;
	}

	/**
	 * <p>smuChk を取得します。
	 * @return smuChk
	 */
	public int getSmuChk() {
		return smuChk__;
	}

	/**
	 * <p>smuChk をセットします。
	 * @param smuChk smuChk
	 */
	public void setSmuChk(int smuChk) {
		smuChk__ = smuChk;
	}

	/**
	 * <p>daiQChk を取得します。
	 * @return daiQChk
	 */
	public int getDaiQChk() {
		return daiQChk__;
	}

	/**
	 * <p>daiQChk をセットします。
	 * @param daiQChk daiQChk
	 */
	public void setDaiQChk(int daiQChk) {
		daiQChk__ = daiQChk;
	}

	/**
	 * <p>firstInTime を取得します。
	 * @return firstInTime
	 */
	public String getFirstInTime() {
		return firstInTime__;
	}

	/**
	 * <p>firstInTime をセットします。
	 * @param firstInTime firstInTime
	 */
	public void setFirstInTime(String firstInTime) {
		firstInTime__ = firstInTime;
	}

	/**
	 * <p>lastOutTime を取得します。
	 * @return lastOutTime
	 */
	public String getLastOutTime() {
		return lastOutTime__;
	}

	/**
	 * <p>lastOutTime をセットします。
	 * @param lastOutTime lastOutTime
	 */
	public void setLastOutTime(String lastOutTime) {
		lastOutTime__ = lastOutTime;
	}

	/**
	 * <p>inTime を取得します。
	 * @return inTime
	 */
	public String getInTime() {
		return inTime__;
	}

	/**
	 * <p>inTime をセットします。
	 * @param inTime inTime
	 */
	public void setInTime(String inTime) {
		inTime__ = inTime;
	}

	/**
	 * <p>outTime を取得します。
	 * @return outTime
	 */
	public String getOutTime() {
		return outTime__;
	}

	/**
	 * <p>outTime をセットします。
	 * @param outTime outTime
	 */
	public void setOutTime(String outTime) {
		outTime__ = outTime;
	}

	/**
	 * <p>overTime を取得します。
	 * @return time
	 */
	public double getOverTime() {
		return overTime__;
	}

	/**
	 * <p>overTime をセットします。
	 * @param time time
	 */
	public void setOverTime(double overTime) {
		overTime__ = overTime;
	}

	/**
	 * <p>midnightTime を取得します。
	 * @return time
	 */
	public double getMidnightTime() {
		return midnightTime__;
	}

	/**
	 * <p>midnightTime をセットします。
	 * @param time time
	 */
	public void setMidnightTime(double midnightTime) {
		midnightTime__ = midnightTime;
	}

	/**
	 * <p>holidayTime を取得します。
	 * @return time
	 */
	public double getHolidayTime() {
		return holidayTime__;
	}

	/**
	 * <p>holidayTime をセットします。
	 * @param time time
	 */
	public void setHolidayTime(double holidayTime) {
		holidayTime__ = holidayTime;
	}

}
