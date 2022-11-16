package jp.groupsession.v2.kintai.Knt200;

  import jp.groupsession.v2.struts.AbstractGsForm;

  /**
   * <br>[機  能] 経営情報検索のフォームクラス
   * <br>[解  説]
   * <br>[備  考]
   *
   * @author JTS
   */
  public class Knt200Form extends AbstractGsForm {

	/** SEL_USR_ID */
	private int selUsrSid__ = -1;
	/** START_YEAR */
	private int startYear__ = -1;
	/** START_MONTH */
	private int startMonth__ = -1;
	/** START_DAY */
	private int startDay__ = -1;
	/** START_YEAR */
	private int endYear__ = -1;
	/** START_MONTH */
	private int endMonth__ = -1;
	/** START_DAY */
	private int endDay__ = -1;
	/** SMU_CHK */
	private boolean[] smuChk__ = null;
	/** SMU_RMK */
	private String[] smuRmk__ = null;

	/**
	 * <p>selUsrSid を取得します。
	 * @return usrSid
	 */
	public int getSelUsrSid() {
		return selUsrSid__;
	}

	/**
	 * <p>selUsrSid をセットします。
	 * @param usrSid usrSid
	 */
	public void setSelUsrSid(int selUsrSid) {
		selUsrSid__ = selUsrSid;
	}

	/**
	 * <p>year を取得します。
	 * @return year
	 */
	public int getStartYear() {
		return startYear__;
	}

	/**
	 * <p>year をセットします。
	 * @param year year
	 */
	public void setStartYear(int year) {
		startYear__ = year;
	}

	/**
	 * <p>month を取得します。
	 * @return month
	 */
	public int getStartMonth() {
		return startMonth__;
	}

	/**
	 * <p>month をセットします。
	 * @param month month
	 */
	public void setStartMonth(int month) {
		startMonth__ = month;
	}

	/**
	 * <p>day を取得します。
	 * @return day
	 */
	public int getStartDay() {
		return startDay__;
	}

	/**
	 * <p>day をセットします。
	 * @param day day
	 */
	public void setStartDay(int day) {
		startDay__ = day;
	}

	/**
	 * <p>year を取得します。
	 * @return year
	 */
	public int getEndYear() {
		return endYear__;
	}

	/**
	 * <p>year をセットします。
	 * @param year year
	 */
	public void setEndYear(int year) {
		endYear__ = year;
	}

	/**
	 * <p>month を取得します。
	 * @return month
	 */
	public int getEndMonth() {
		return endMonth__;
	}

	/**
	 * <p>month をセットします。
	 * @param month month
	 */
	public void setEndMonth(int month) {
		endMonth__ = month;
	}

	/**
	 * <p>day を取得します。
	 * @return day
	 */
	public int getEndDay() {
		return endDay__;
	}

	/**
	 * <p>day をセットします。
	 * @param day day
	 */
	public void setEndDay(int day) {
		endDay__ = day;
	}

	/**
	 * <p>smuChk を取得します。
	 * @return smuChk
	 */
	public boolean[] getSmuChk() {
		return smuChk__;
	}

	/**
	 * <p>smuChk をセットします。
	 * @param smuChk smuChk
	 */
	public void setSmuChk(boolean[] smuChk) {
		smuChk__ = smuChk;
	}

	/**
	 * <p>smuRmk を取得します。
	 * @return smuRmk
	 */
	public String[] getSmuRmk() {
		return smuRmk__;
	}

	/**
	 * <p>smuRmk をセットします。
	 * @param smuRmk smuRmk
	 */
	public void setSmuRmk(String[] smuRmk) {
		smuRmk__ = smuRmk;
	}

  }