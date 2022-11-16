package jp.groupsession.v2.kintai.Knt900;

/**
 * <br>[機 能] 管理情報検索情報を保持するModelクラス
 * <br>[解 説]
 * <br>[備 考]
 *
 * @author JTS
 */
public class Knt900Model {

	/** プロジェクトSID */
	private int prjSid__ = -1;
	/** プロジェクトID */
	private String prjId__ = "";
	/** プロジェクト名称 */
	private String prjName__ = "";
	/** 開始年 */
	private int startYear__;
	/** 開始月 */
	private int startMonth__;
	/** 開始日 */
	private int startDay__;
	/** 終了年 */
	private int endYear__;
	/** 終了月 */
	private int endMonth__;
	/** 終了日 */
	private int endDay__;
	/** 作業時間 */
	private double prjTime__ = 0;

	/** ユーザーSID */
	private int usrSid__;
	/** 年 */
	private int usrYear__;
	/** 月 */
	private int usrMonth__;

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
	 * <p>prjId を取得します。
	 * @return prjId
	 */
	public String getPrjId() {
		return prjId__;
	}

	/**
	 * <p>prjId をセットします。
	 * @param prjId prjId
	 */
	public void setPrjId(String prjId) {
		prjId__ = prjId;
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
	 * <p>startYear を取得します。
	 * @return StartYear
	 */
	public int getStartYear() {
		return startYear__;
	}

	/**
	 * <p>startYear をセットします。
	 * @param StartYear StartYear
	 */
	public void setStartYear(int startYear) {
		startYear__ = startYear;
	}

	/**
	 * <p>startMonth を取得します。
	 * @return StartMonth
	 */
	public int getStartMonth() {
		return startMonth__;
	}

	/**
	 * <p>startMonth をセットします。
	 * @param StartMonth StartMonth
	 */
	public void setStartMonth(int startMonth) {
		startMonth__ = startMonth;
	}

	/**
	 * <p>startDay を取得します。
	 * @return StartDay
	 */
	public int getStartDay() {
		return startDay__;
	}

	/**
	 * <p>startDay をセットします。
	 * @param StartDay StartDay
	 */
	public void setStartDay(int startDay) {
		startDay__ = startDay;
	}

	/**
	 * <p>endYear を取得します。
	 * @return EndYear
	 */
	public int getEndYear() {
		return endYear__;
	}

	/**
	 * <p>endYear をセットします。
	 * @param endYear endYear
	 */
	public void setEndYear(int endYear) {
		endYear__ = endYear;
	}

	/**
	 * <p>endMonth を取得します。
	 * @return endMonth
	 */
	public int getEndMonth() {
		return endMonth__;
	}

	/**
	 * <p>endMonth をセットします。
	 * @param endMonth endMonth
	 */
	public void setEndMonth(int endMonth) {
		endMonth__ = endMonth;
	}

	/**
	 * <p>endDay を取得します。
	 * @return endDay
	 */
	public int getEndDay() {
		return endDay__;
	}

	/**
	 * <p>endDay をセットします。
	 * @param endDay endDay
	 */
	public void setEndDay(int endDay) {
		endDay__ = endDay;
	}

	/**
	 * <p>prjTime を取得します。
	 * @return prjTime
	 */
	public double getPrjTime() {
		return prjTime__;
	}

	/**
	 * <p>prjTime をセットします。
	 * @param prjTime prjTime
	 */
	public void setPrjTime(double prjTime) {
		prjTime__ = prjTime;
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
	 * <p>usrYear をセットします。
	 * @param usrYear usrYear
	 */
	public void setUsrYear(int usrYear) {
		usrYear__ = usrYear;
	}

	/**
	 * <p>usrSid を取得します。
	 * @return usrSid
	 */
	public int getUsrYear() {
		return usrYear__;
	}

	/**
	 * <p>usrMonth を取得します。
	 * @return usrMonth
	 */
	public int getUsrMonth() {
		return usrMonth__;
	}

	/**
	 * <p>usrYear をセットします。
	 * @param usrYear usrYear
	 */
	public void setUsrMonth(int usrMonth) {
		usrMonth__ = usrMonth;
	}

}
