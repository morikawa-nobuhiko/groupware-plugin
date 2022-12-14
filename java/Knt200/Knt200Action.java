package jp.groupsession.v2.kintai.Knt200;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.Calendar;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.model.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import jp.co.sjts.util.NullDefault;
import jp.co.sjts.util.date.UDate;
import jp.co.sjts.util.jdbc.JDBCUtil;
import jp.co.sjts.util.jdbc.SqlBuffer;
import jp.co.sjts.util.Encoding;
import jp.co.sjts.util.http.TempFileUtil;
import jp.groupsession.v2.struts.AbstractGsAction;
import jp.groupsession.v2.cmn.GSConst;
import jp.groupsession.v2.cmn.GSConstSchedule;
import jp.groupsession.v2.cmn.config.PluginConfig;
import jp.groupsession.v2.cmn.dao.GroupModel;
import jp.groupsession.v2.cmn.dao.BaseUserModel;
import jp.groupsession.v2.cmn.dao.UsidSelectGrpNameDao;
import jp.groupsession.v2.cmn.dao.BaseUserModel;
import jp.groupsession.v2.cmn.model.RequestModel;
import jp.groupsession.v2.kintai.Knt002.Knt002Dao;
import jp.groupsession.v2.kintai.Knt002.Knt002Model;
import jp.groupsession.v2.kintai.Knt302.Knt302Dao;
import jp.groupsession.v2.kintai.Knt302.Knt302UserModel;
import jp.groupsession.v2.cmn.dao.base.CmnHolidayDao;
import jp.groupsession.v2.cmn.model.base.CmnHolidayModel;

/**
 * <br>[???  ???] Knt200???????????????????????????
 * <br>[???  ???]
 * <br>[???  ???]
 *
 * @author JTS
 */
public class Knt200Action extends AbstractGsAction {

	/** Logging ?????????????????? */
	private static Log log__ = LogFactory.getLog(Knt200Action.class);

	/**
	* <br>[???  ???] ??????????????????????????????
	* <br>[???  ???]
	* <br>[???  ???]
	* @param map ActionMapping
	* @param form ActionForm
	* @param req HttpServletRequest
	* @param res HttpServletResponse
	* @param con DB Connection
	* @return ActionForward
	* @throws Exception ???????????????
	*/
	public ActionForward executeAction(ActionMapping map,
										ActionForm form,
										HttpServletRequest req,
										HttpServletResponse res,
										Connection con)
	throws Exception {

		ActionForward forward = null;
		Knt200Form knt200Form = (Knt200Form) form;
		Knt002Dao dao002 = new Knt002Dao();
		Knt200Dao dao200 = new Knt200Dao();
		Knt302Dao dao302 = new Knt302Dao();

		// buttonPush???????????????????????????????????????
		String cmd = NullDefault.getString(req.getParameter("CMD"), "");

		log__.debug("CMD=" + cmd);

		//HttpServletRequest????????????????????????????????????????????????
		RequestModel reqMdl = getRequestModel(req);
		//?????????????????????????????????????????????????????????????????????????????????
		BaseUserModel usModel = reqMdl.getSmodel();

		// ???????????????????????????
		ArrayList<Knt302UserModel> usrList = dao302.selectUsers(con);
		// ??????????????????????????????
		req.setAttribute("usrList", usrList);

		// JSP?????????????????????
		int selUsrSid = -1;
		int startYear=2000;
		int startMonth=12;
		int startDay=31;
		int endYear=9999;
		int endMonth=12;
		int endDay=31;
		int intDays=31;

		if (cmd.equals("")) {

			// ????????????

			// ????????????????????????
			Calendar cal=Calendar.getInstance();
			cal.set(Calendar.DATE, 1);					// 1????????????
			startYear=cal.get(Calendar.YEAR);
			startMonth=cal.get(Calendar.MONTH) + 1;
			startDay=cal.get(Calendar.DATE);
			endYear=startYear;
			endMonth=startMonth;
			endDay=cal.getActualMaximum(Calendar.DATE);

		} else if (cmd.equals("search") || cmd.equals("back200") || cmd.equals("xlsx")) {

			// ??????????????????
			selUsrSid = knt200Form.getSelUsrSid();
			startYear=knt200Form.getStartYear();
			startMonth=knt200Form.getStartMonth();
			startDay=knt200Form.getStartDay();
			endYear=knt200Form.getEndYear();
			endMonth=knt200Form.getEndMonth();
			endDay=knt200Form.getEndDay();

			// ????????????
			ActionErrors errors = new ActionErrors();
			Calendar calS = Calendar.getInstance();
			Calendar calE = Calendar.getInstance();
			calS.set(startYear, startMonth, startDay);
			calE.set(endYear, endMonth, endDay);
			if (calE.before(calS)) {
				errors.add("fromTo", new ActionMessage("????????????????????????????????????????????????"));
			}
			if (!errors.isEmpty()) {
				addErrors(req, errors);
			}

			log__.debug("??????????????????");

		} else if (cmd.startsWith("regist")) {

//			log__.debug("smuChk=" + knt200Form.getSmuChk().length);
//			log__.debug("smuRmk=" + knt200Form.getSmuRmk().length);

			// ????????????????????????
			selUsrSid = knt200Form.getSelUsrSid();
			startYear=knt200Form.getStartYear();
			startMonth=knt200Form.getStartMonth();
			startDay=knt200Form.getStartDay();
			endYear=knt200Form.getEndYear();
			endMonth=knt200Form.getEndMonth();
			endDay=knt200Form.getEndDay();

			// ??????????????????
			Calendar calS=Calendar.getInstance();
			Calendar calE=Calendar.getInstance();
			calS.set(startYear, startMonth -1, startDay);
			calE.set(endYear, endMonth -1, endDay);

			log__.debug("calS=" + calS);
			log__.debug("calE=" + calE);

			boolean commit = false;
			try {

				// ??????????????????????????????????????????????????????
//				int idx = 0;
				while(!calS.equals(calE)){

					// ???????????????
					int smuY = calS.get(Calendar.YEAR);
					int smuM = calS.get(Calendar.MONTH) + 1;
					int smuD = calS.get(Calendar.DATE);
					log__.debug("smuY=" + smuY);
					log__.debug("smuM=" + smuM);
					log__.debug("smuD=" + smuD);

					// ???????????????????????????????????????
//					boolean userJsk =dao200.containsUserJsk(selUsrSid, smuY, smuM, smuD, con);
//					if (!userJsk) {
//						// ??????????????????????????????
//						calS.add(Calendar.DATE, 1);
//						continue;
//					}

					// ??????????????????
					int intSmuChk = 0;
					String checkedDate =  NullDefault.getString(req.getParameter("smuChk" + smuY + smuM + smuD), "");
					log__.debug("checkedDate=" + checkedDate);
					if (!checkedDate.equals("")) {
						intSmuChk = 1;
					}
					log__.debug("intSmuChk=" + intSmuChk);

					// ??????
//					String smuRmk = knt200Form.getSmuRmk()[idx];
					String smuRmk =  NullDefault.getString(req.getParameter("smuRmk" + smuY + smuM + smuD), "");
//					log__.debug("idx=" + idx);
					log__.debug("smuRmk=" + smuRmk);

					// ??????????????????
					boolean isNew = dao200.isNewSmuCheck(selUsrSid, smuY, smuM, smuD, con);
					if (isNew){
						// ??????
						dao200.insertSmuCheck(selUsrSid, smuY, smuM, smuD, intSmuChk, smuRmk, con);
					} else {
						// ??????
						dao200.updateSmuCheck(selUsrSid, smuY, smuM, smuD, intSmuChk, smuRmk, con);
					}

					// ??????????????????????????????
					calS.add(Calendar.DATE, 1);

					// ??????????????????????????????????????????????????????
//					idx = idx + 1;

				}

				// ????????????
				con.commit();
				commit = true;

			} catch (Exception e) {
				throw e;
			} finally {
				if (!commit) {
					con.rollback();
				}
			}

			// ?????????????????????
			ActionErrors errors = new ActionErrors();
			errors.add("registed", new ActionMessage("?????????????????????"));
			addErrors(req, errors);

		} else if (cmd.startsWith("detail002")) {

			// ???????????????????????????
			req.setAttribute("smuChk", 1);

			return map.findForward("detail002");

		}

		// ??????????????????????????????????????????
		Calendar scal=Calendar.getInstance();
		scal.clear();
		scal.set(Calendar.YEAR, startYear);
		scal.set(Calendar.MONTH, startMonth -1);
		int slast = scal.getActualMaximum(Calendar.DATE);
		if (slast < startDay) {
			startDay = slast;
		}
		scal.set(Calendar.DATE, startDay);
		log__.debug("startYear=" + startYear);
		log__.debug("startMonth=" + startMonth);
		log__.debug("startDay=" + startDay);
		log__.debug("slast=" + slast);

		// ??????????????????????????????????????????
		Calendar ecal=Calendar.getInstance();
		ecal.clear();
		ecal.set(Calendar.YEAR, endYear);
		ecal.set(Calendar.MONTH, endMonth -1);
		int elast = ecal.getActualMaximum(Calendar.DATE);
		if (elast < endDay) {
			endDay = elast;
		}
		ecal.set(Calendar.DATE, endDay + 1); // before????????????1?????????
		log__.debug("endYear=" + endYear);
		log__.debug("endMonth=" + endMonth);
		log__.debug("endDay=" + endDay);
		log__.debug("elast=" + elast);

		// ????????????????????????????????????
//		ArrayList<Knt200Model> fromToTimes = dao200.selectFromToTotalTime(selUsrSid, startYear, startMonth, startDay, endYear, endMonth, endDay, con);

		// ????????????????????????????????????????????????????????????????????????
		ArrayList<Knt200Model> fromToTimes = new ArrayList<Knt200Model>();

		// ?????????????????????
//		if (!cmd.equals("")) {

		// ????????????????????????????????????
		if (cmd.equals("search") || cmd.equals("regist") || cmd.equals("back200") || cmd.equals("xlsx")) {

			while (scal.before(ecal)) {

				// ??????
				int sYear=scal.get(Calendar.YEAR);
				int sMonth=scal.get(Calendar.MONTH) + 1;
				int sDay=scal.get(Calendar.DATE);

				// ????????????????????????????????????1??????
				Knt200Model model = dao200.selectFromToTotalTime(selUsrSid, sYear, sMonth, sDay, con);

				// ???????????????
				Timestamp firstInTime = dao002.selectInOutTime(selUsrSid, sYear, sMonth, sDay, 0, con);
				Timestamp lastOutTime = dao002.selectInOutTime(selUsrSid, sYear, sMonth, sDay, 1, con);
				Timestamp outTime = dao002.selectInOutTime(selUsrSid, sYear, sMonth, sDay, 2, con);
				Timestamp inTime = dao002.selectInOutTime(selUsrSid, sYear, sMonth, sDay, 3, con);
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
				String strFirstInTime = "";
				String strLastOutTime = "";
				String strOutTime = "";
				String strInTime = "";
				if (Objects.nonNull(firstInTime)) {
					strFirstInTime = sdf.format(firstInTime);
				}
				if (Objects.nonNull(lastOutTime)) {
					strLastOutTime = sdf.format(lastOutTime);
				}
				if (Objects.nonNull(outTime)) {
					strOutTime = sdf.format(outTime);
				}
				if (Objects.nonNull(inTime)) {
					strInTime = sdf.format(inTime);
				}
				model.setFirstInTime(strFirstInTime);
				model.setLastOutTime(strLastOutTime);
				model.setOutTime(strOutTime);
				model.setInTime(strInTime);

//				// ???????????????????????????
//				double overTime = dao200.selectDayOverTime(selUsrSid, sYear, sMonth, sDay, con);
//				// ????????????????????????????????????????????????
//				double hollowOverTime = dao200.selectDayOverHollowTime(selUsrSid, sYear, sMonth, sDay, con);
//				model.setOverTime(overTime - hollowOverTime);
//
//				// ???????????????????????????
//				double midnightTime = dao200.selectDayMidnightTime(selUsrSid, sYear, sMonth, sDay, con);
//				// ????????????????????????????????????????????????
//				double hollowMidnightTime = dao200.selectDayMidnightHollowTime(selUsrSid, sYear, sMonth, sDay, con);
//				model.setMidnightTime(midnightTime - hollowMidnightTime);

				// ???????????????model.getWrkTime???

				// ???????????????????????????
				double ot = model.getWrkTime() - 8;
				if (0 < ot) {
					model.setOverTime(ot);
				} else {
					model.setOverTime(0);
				}

				// ??????????????????????????????4????????????????????????
				double mt = model.getWrkTime() - 8 -4;
				if (0 < mt) {
					model.setMidnightTime(ot);
				} else {
					model.setMidnightTime(0);
				}

				// ??????
				Knt002Model modelDaiQ = dao002.selectWork(selUsrSid, sYear, sMonth, sDay, 71, con);
				int daiQ = 0;
				if (modelDaiQ.getWrkId()==711) {
					daiQ = 1;
				} else {
					daiQ = 0;
				}
				model.setDaiQChk(daiQ);

				// ???????????????????????????
				fromToTimes.add(model);

				// ????????????
				scal.add(Calendar.DAY_OF_MONTH, 1);

				log__.debug("ASY=" + sYear);
				log__.debug("ASM=" + sMonth);
				log__.debug("ASD=" + sDay);
//				log__.debug("overTime=" + overTime);
//				log__.debug("hollowOverTime=" + hollowOverTime);
//				log__.debug("midnightTime=" + midnightTime);
//				log__.debug("hollowMidnightTime=" + hollowMidnightTime);

			}

			// ???????????????????????????
			for(int i=0; i<fromToTimes.size(); i++) {
				Knt200Model ftt = (Knt200Model)fromToTimes.get(i);
				Knt200Model smu = dao200.selectSmuCheck(selUsrSid, ftt.getWrkYear(), ftt.getWrkMonth(), ftt.getWrkDay(), con);
				ftt.setSmuChk(smu.getSmuChk());
				ftt.setSmuRmk(smu.getSmuRmk());
			}

		}

		if (cmd.startsWith("xlsx")) {

			// ???????????????
			log__.debug("???????????????");
			//?????????????????????????????????????????????
			String appRootPath = getAppRootPath();
			// ??????????????????
			String tempWpDir = "WEB-INF/plugin/kintai/template/";
//			String tempWpFileName = "workReportFormat.xls";
			String tempWpFileName = "workReportFormat.xlsx";
			String tempWpFullPath = appRootPath + tempWpDir + tempWpFileName;
			//????????????????????????????????????????????????
			String tempDir = System.getProperty("java.io.tmpdir");
			// ?????????
//			String fileName =  "/?????????.xls";
			String fileName =  "/?????????.xlsx";
			String fullPath = tempDir + fileName;
			// ????????????
			String usrName = "";
			for(int i=0; i<usrList.size(); i++) {
				Knt302UserModel usr = (Knt302UserModel)usrList.get(i);
				int usrSid = usr.getUsrSid();
				if (usrSid == selUsrSid) {
					usrName = usr.getUsrName();
					break;
				}
			}
			// ????????????
			exportWorkReport(fromToTimes, tempWpFullPath, fullPath, usrName, startYear, startMonth, startDay, endYear, endMonth, endDay, con);
			// ?????????????????????????????????????????????????????????
			String downFileName =  "?????????_" + usrName + ".xlsx";
			TempFileUtil.downloadAtachment(req, res, fullPath, downFileName, Encoding.UTF_8);

		}

		log__.debug("fromToTimes_count=" + fromToTimes.size());
		log__.debug("DISP_USR_ID=" + selUsrSid);
		log__.debug("SY=" + startYear);
		log__.debug("SM=" + startMonth);
		log__.debug("SD=" + startDay);
		log__.debug("EY=" + endYear);
		log__.debug("EM=" + endMonth);
		log__.debug("ED=" + endDay);

		// JSP???????????????????????????
		req.setAttribute("fromToTimes", fromToTimes);
		req.setAttribute("selUsrSid", selUsrSid);
		req.setAttribute("startYear", startYear);
		req.setAttribute("startMonth", startMonth);
		req.setAttribute("startDay", startDay);
		req.setAttribute("endYear", endYear);
		req.setAttribute("endMonth", endMonth);
		req.setAttribute("endDay", endDay);
		// --------------------------------------------------------------

		// ???????????????
		CmnHolidayDao holDao = new CmnHolidayDao(con);
		List<CmnHolidayModel> holidayModels = holDao.select();
		ArrayList<String> holiday = new ArrayList<String>();
		for(int i = 0; i < holidayModels.size(); i++)
		{
			CmnHolidayModel hm = holidayModels.get(i);
			holiday.add(hm.getHolDate().getDateString());
		}
		req.setAttribute("holiday", holiday);

		forward = map.getInputForward();

		return forward;

	}

	/**
	 * <br>[??????] ????????????????????????
	 * <br>[??????]
	 * <br>[??????]
	 * @param wrkList      ????????????????????????
	 * @param tempFilePath ????????????????????????????????????
	 * @param outFilePath  ????????????????????????
	 * @param con          ??????????????????
	 * @param outDir       ???????????????????????????
	 * @return ActionForward
	 * @throws Exception ????????????
	 */
	private ActionForward exportWorkReport(ArrayList<Knt200Model> wrkList,
											String tempFilePath,
											String outFilePath,
											String usrName,
											int sy, int sm, int sd,
											int ey, int em, int ed,
											Connection con) throws Exception {

		Workbook tempbook = null;

		try {

			log__.debug("exportWorkReport_start");
			log__.debug("?????????????????????" + tempFilePath);
			log__.debug("????????????" + outFilePath);

			// ????????????????????????????????????????????????????????????
			Path tempPath = Paths.get(tempFilePath);
			InputStream inSt = Files.newInputStream(tempPath);
			tempbook = new XSSFWorkbook(inSt);
			Sheet sheet = tempbook.getSheet("work_report");

			log__.debug("exportWorkReport_tempPath_OK");

			// ????????????
			Row r = null;
			Cell c = null;
			int idxRow = 0;

			// ??????
			c = sheet.getRow(idxRow).getCell(1, Row.CREATE_NULL_AS_BLANK);
			c.setCellValue(usrName);

			idxRow = idxRow + 1;

			// ??????
			c = sheet.getRow(idxRow).getCell(1, Row.CREATE_NULL_AS_BLANK);
			c.setCellValue(sy + "/" + sm + "/" + sd + "???" + ey + "/" + em + "/" + ed);

			idxRow = 3;

			String switchYm = "";
			String printYm = "";
			Double totalWrkTime = 0.0;
			Double totalPayTime = 0.0;
			for(int i=0; i<wrkList.size(); i++) {

				// ??????????????????
				Knt200Model ftt = (Knt200Model)wrkList.get(i);

				// ????????????
				printYm = String.valueOf(ftt.getWrkYear()) + String.valueOf(ftt.getWrkMonth());
				log__.debug("printYm:" + printYm);

				// ???????????????????????????
				if (!switchYm.equals("") && !switchYm.equals(printYm)) {
					c = sheet.getRow(idxRow).getCell(0, Row.CREATE_NULL_AS_BLANK);
					c.setCellValue("??????");
					c = sheet.getRow(idxRow).getCell(6, Row.CREATE_NULL_AS_BLANK);
					c.setCellValue(totalWrkTime);
					c = sheet.getRow(idxRow).getCell(7, Row.CREATE_NULL_AS_BLANK);
					c.setCellValue(totalPayTime);
					// ?????????
					totalWrkTime = 0.0;
					totalPayTime = 0.0;
					// ?????????
					idxRow += 1;
				}

				// ???????????????
				switchYm = String.valueOf(ftt.getWrkYear()) + String.valueOf(ftt.getWrkMonth());
				log__.debug("switchYm:" + switchYm);

				// ????????????
				Calendar cal=Calendar.getInstance();
				cal.clear();
				cal.set(Calendar.YEAR, ftt.getWrkYear());
				cal.set(Calendar.MONTH, ftt.getWrkMonth() -1);
				cal.set(Calendar.DATE, ftt.getWrkDay());
				int k = cal.get(Calendar.DAY_OF_WEEK);
				String dw = "";
				switch (k){
				case 1:
					dw = "???";
					break;
				case 2:
					dw = "???";
					break;
				case 3:
					dw = "???";
					break;
				case 4:
					dw = "???";
					break;
				case 5:
					dw = "???";
					break;
				case 6:
					dw = "???";
					break;
				case 7:
					dw = "???";
					break;
				}

				// ??????
				c = sheet.getRow(idxRow).getCell(0, Row.CREATE_NULL_AS_BLANK);
				c.setCellValue(ftt.getWrkMonth() + "/" + ftt.getWrkDay());

				// ??????
				c = sheet.getRow(idxRow).getCell(1, Row.CREATE_NULL_AS_BLANK);
				c.setCellValue(dw);

				// ??????
				c = sheet.getRow(idxRow).getCell(2, Row.CREATE_NULL_AS_BLANK);
				c.setCellValue(ftt.getFirstInTime());

				// ?????????????????????
				c = sheet.getRow(idxRow).getCell(3, Row.CREATE_NULL_AS_BLANK);
				c.setCellValue(ftt.getOutTime());

				// ?????????????????????
				c = sheet.getRow(idxRow).getCell(4, Row.CREATE_NULL_AS_BLANK);
				c.setCellValue(ftt.getInTime());

				// ??????
				c = sheet.getRow(idxRow).getCell(5, Row.CREATE_NULL_AS_BLANK);
				c.setCellValue(ftt.getLastOutTime());

				// ????????????????????????????????????
				c = sheet.getRow(idxRow).getCell(6, Row.CREATE_NULL_AS_BLANK);
				double total = ftt.getWrkTime() + ftt.getPaidTime();
				c.setCellValue(total);

				// ??????????????????
				c = sheet.getRow(idxRow).getCell(7, Row.CREATE_NULL_AS_BLANK);
				c.setCellValue(ftt.getPaidTime());

				// ????????????
				c = sheet.getRow(idxRow).getCell(8, Row.CREATE_NULL_AS_BLANK);
				c.setCellValue(ftt.getOverTime());

				// ????????????
				c = sheet.getRow(idxRow).getCell(9, Row.CREATE_NULL_AS_BLANK);
				c.setCellValue(ftt.getMidnightTime());

				// ??????????????????
				totalWrkTime = totalWrkTime + total;
				totalPayTime = totalPayTime + ftt.getPaidTime();

				// ?????????
				idxRow += 1;

			}

			// ???????????????
			c = sheet.getRow(idxRow).getCell(0, Row.CREATE_NULL_AS_BLANK);
			c.setCellValue("??????");
			c = sheet.getRow(idxRow).getCell(6, Row.CREATE_NULL_AS_BLANK);
			c.setCellValue(totalWrkTime);
			c = sheet.getRow(idxRow).getCell(7, Row.CREATE_NULL_AS_BLANK);
			c.setCellValue(totalPayTime);

			// ?????????
			idxRow += 1;

			// ??????????????????????????????
			CellStyle styleBorderNone = tempbook.createCellStyle();
			styleBorderNone.setBorderTop(CellStyle.BORDER_NONE);
			styleBorderNone.setBorderBottom(CellStyle.BORDER_NONE);
			styleBorderNone.setBorderLeft(CellStyle.BORDER_NONE);
			styleBorderNone.setBorderRight(CellStyle.BORDER_NONE);
			for(int i=idxRow; i<1000; i++) {
				for(int j=0; j<10; j++) {
					r = sheet.getRow(i);
					if (r == null) {
						break;
					}
					c = r.getCell(j, Row.CREATE_NULL_AS_BLANK);
					if (c == null) {
						break;
					}
					c.setCellStyle(styleBorderNone);
				}
			}

			// ??????????????????
			Path outPath = Paths.get(outFilePath);
			OutputStream outSt = Files.newOutputStream(outPath);
			tempbook.write(outSt);

			log__.debug("exportWorkReport_end");

		} catch (IOException e) {
			log__.error("????????????????????????????????????????????????");
		} finally {
			// ?????????????????????????????????
			try {
				if (tempbook != null) {
					tempbook.close();
				}
			} catch (IOException e) {
				log__.error("??????????????????????????????????????????????????????");
			}
		}

		return null;

	}

	/**
	 * <br>[??????] ?????????????????????????????????1???????????????
	 * <br>[??????]
	 * <br>[??????]
	 * @param prjList  ????????????????????????
	 * @param filePath ????????????????????????????????????
	 * @param con ??????????????????
	 * @param outDir ???????????????????????????
	 * @return ActionForward
	 * @throws Exception ????????????
	 */
	private ActionForward exportWorkReport2(ArrayList<Knt200Model> wrkList, String filePath, Connection con) throws Exception {

		//Excel???????????????
		Workbook wb = new XSSFWorkbook();	// xlsx????????????????????????
		Sheet sh = wb.createSheet();		// ??????????????????

		// ????????????
		Row row = null;
		Cell cell = null;

		// ?????????????????????
		CellStyle styleTitle = wb.createCellStyle();
		Font fontTitle = wb.createFont();
		fontTitle.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontTitle.setFontHeightInPoints((short)14);
		styleTitle.setFont(fontTitle);

		int idxRow = 0;
		row = sh.createRow(idxRow);

		// ????????????
		cell = row.createCell(0);
		cell.setCellStyle(styleTitle);
		cell.setCellValue("?????????");

		idxRow = idxRow + 1;
		row = sh.createRow(idxRow);

		// ???????????????
		cell = row.createCell(0);
		cell.setCellValue("?????????");

		// ??????
		cell = row.createCell(1);
		cell.setCellValue("?????? ??????");

		idxRow = idxRow + 1;
		row = sh.createRow(idxRow);

		// ???????????????
		cell = row.createCell(0);
		cell.setCellValue("?????????");

		// ??????
		cell = row.createCell(1);
		cell.setCellValue("2021/04/01???2021/04/30");

		//Excel??????????????????
		FileOutputStream out = null;
		String path = filePath;

		try {

			//Excel??????
			out = new FileOutputStream(path);
			wb.write(out);

		}catch(IOException e){
			log__.error("????????????????????????????????????????????????");
		} finally {
			try {
				wb.close();
				out.close();
			} catch (Exception ex2) {
				ex2.printStackTrace();
			}
		}

		return null;

	}

}
