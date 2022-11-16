package jp.groupsession.v2.kintai.Knt301;

/**
 * <br>[機 能] 管理情報検索情報を保持するModelクラス
 * <br>[解 説]
 * <br>[備 考]
 *
 * @author JTS
 */
public class Knt301Model {

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
	/** 間接作業フラグ */
	private int indirect__ = 0;
	/** 発注者名 */
	private String orderName__ = null;
	/** 主任担当者名 */
	private String chiefName__ = null;
	/** 担当者名 */
	private String managerName__ = null;
	/** 備考 */
	private String remark__ = null;
	/** 対象（物件） */
	private int targetB__ = 0;
	/** 対象（測量） */
	private int targetS__ = 0;
	/** 対象（補償説明） */
	private int targetH__ = 0;
	/** 対象（事業認定） */
	private int targetJ__ = 0;

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
	 * <p>indirect を取得します。
	 * @return indirect
	 */
	public int getIndirect() {
		return indirect__;
	}

	/**
	 * <p>indirect をセットします。
	 * @param indirect indirect
	 */
	public void setIndirect(int indirect) {
		indirect__ = indirect;
	}

	/**
	 * <p>orderName を取得します。
	 * @return orderName
	 */
	public String getOrderName() {
		return orderName__;
	}

	/**
	 * <p>orderName をセットします。
	 * @param orderName orderName
	 */
	public void setOrderName(String orderName) {
		orderName__ = orderName;
	}

	/**
	 * <p>chiefName を取得します。
	 * @return chiefName
	 */
	public String getChiefName() {
		return chiefName__;
	}

	/**
	 * <p>chiefName をセットします。
	 * @param chiefName chiefName
	 */
	public void setChiefName(String chiefName) {
		chiefName__ = chiefName;
	}

	/**
	 * <p>managerName を取得します。
	 * @return managerName
	 */
	public String getManagerName() {
		return managerName__;
	}

	/**
	 * <p>managerName をセットします。
	 * @param managerName managerName
	 */
	public void setManagerName(String managerName) {
		managerName__ = managerName;
	}

	/**
	 * <p>remarkName を取得します。
	 * @return remarkName
	 */
	public String getRemark() {
		return remark__;
	}

	/**
	 * <p>remarkName をセットします。
	 * @param remarkName remarkName
	 */
	public void setRemark(String remark) {
		remark__ = remark;
	}

	/**
	 * <p>targetB を取得します。
	 * @return target
	 */
	public int getTargetB() {
		return targetB__;
	}

	/**
	 * <p>targetB をセットします。
	 * @param target target
	 */
	public void setTargetB(int targetB) {
		targetB__ = targetB;
	}

	/**
	 * <p>targetS を取得します。
	 * @return target
	 */
	public int getTargetS() {
		return targetS__;
	}

	/**
	 * <p>targetS をセットします。
	 * @param target target
	 */
	public void setTargetS(int targetS) {
		targetS__ = targetS;
	}

	/**
	 * <p>targetH を取得します。
	 * @return target
	 */
	public int getTargetH() {
		return targetH__;
	}

	/**
	 * <p>targetH をセットします。
	 * @param target target
	 */
	public void setTargetH(int targetH) {
		targetH__ = targetH;
	}

	/**
	 * <p>targetJ を取得します。
	 * @return target
	 */
	public int getTargetJ() {
		return targetJ__;
	}

	/**
	 * <p>targetJ をセットします。
	 * @param target target
	 */
	public void setTargetJ(int targetJ) {
		targetJ__ = targetJ;
	}

}
