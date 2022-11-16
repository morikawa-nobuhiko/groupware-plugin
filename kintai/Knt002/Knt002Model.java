package jp.groupsession.v2.kintai.Knt002;

/**
 * <br>[機 能] ユーザー実績情報を保持するModelクラス
 * <br>[解 説]
 * <br>[備 考]
 *
 * @author JTS
 */
public class Knt002Model {

	/**JSK_ID */
	private int jskId__ = -1;
	/** USR_SID */
	private int usrSid__ = -1;
	/** PRJ_SID */
	private int prjSid__ = -1;
	/** PRJ_NAME */
	private String prjName__ = "";
	/** WRK_ID */
	private int wrkId__ = -1;
	/** WRK_NAME */
	private String wrkName__ = "";
	/** USR_YYYY */
	private int wrkYear__ = -1;
	/** USR_MM */
	private int wrkMonth__ = -1;
	/** USR_DD */
	private int wrkDay__ = -1;
	/** USR_WRK_TIME */
	private double wrkTime__ = 0;
	/** JSK_RMK */
	private String jskRmk__ = "";

	/**
	 * <p>jskId を取得します。
	 * @return jskId
	 */
	public int getJskId() {
		return jskId__;
	}

	/**
	 * <p>jskId をセットします。
	 * @param id id
	 */
	public void setJskId(int jskId) {
		jskId__ = jskId;
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
	 * <p>prjName を取得します。
	 * @return prjName
	 */
	public String getPrjName() {
		return prjName__;
	}

	/**
	 * <p>prjName をセットします。
	 * @param prjName prjName
	 */
	public void setPrjName(String prjName) {
		prjName__ = prjName;
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
	 * <p>day を取得します。
	 * @return day
	 */
	public String getJskRmk() {
		return jskRmk__;
	}

	/**
	 * <p>day をセットします。
	 * @param day day
	 */
	public void setJskRmk(String jskRmk) {
		jskRmk__ = jskRmk;
	}

}
