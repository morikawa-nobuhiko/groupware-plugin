package jp.groupsession.v2.kintai.Knt002;

import java.sql.SQLException;
import jp.groupsession.v2.struts.AbstractGsForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMapping;

	/**
	 * <br>[機	能] Monthlyのフォームクラス
	 * <br>[解	説]
	 * <br>[備	考]
	 *
	 * @author JTS
	 */
	public class Knt002Form extends AbstractGsForm {

	/** USR_ID */
	private int usrSid__ = -1;
	/** SEL_USR_ID */
	private int selUsrSid__ = -1;
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
	private double wrkTime__ = -1;

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
	private int smuChk__ = 0;

	/** REASON */
	private String reason__ = "";
	/** DAIQ */
	private int daiQ__ = 0;

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
	 * <p>reason を取得します。
	 * @return reason
	 */
	public String getReason() {
		return reason__;
	}

	/**
	 * <p>reason をセットします。
	 * @param String reason
	 */
	public void setReason(String reason) {
		reason__ = reason;
	}

	/**
	 * <p>daiQ を取得します。
	 * @return jskId
	 */
	public int getDaiQ() {
		return daiQ__;
	}

	/**
	 * <p>daiQ をセットします。
	 * @param id id
	 */
	public void setDaiQ(int dq) {
		daiQ__ = dq;
	}

}