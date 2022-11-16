package jp.groupsession.v2.kintai.Knt900;

  import jp.groupsession.v2.struts.AbstractGsForm;

  /**
   * <br>[機  能] 経営情報検索のフォームクラス
   * <br>[解  説]
   * <br>[備  考]
   *
   * @author JTS
   */
  public class Knt900Form extends AbstractGsForm {

	/** プロジェクトSID */
	private int prjSid__ = -1;
	/** プロジェクトID */
	private String prjId__ = "";
	/** プロジェクト名称 */
	private String prjName__ = null;
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
	/** 出力単位 */
	private int unit__;

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
	 * <p>unit を取得します。
	 * @return unit
	 */
	public int getUnit() {
		return unit__;
	}

	/**
	 * <p>unit をセットします。
	 * @param unit unit
	 */
	public void setUnit(int unit) {
		unit__ = unit;
	}

  }