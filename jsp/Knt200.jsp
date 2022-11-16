<%@ page pageEncoding="Windows-31J" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<% String key = jp.groupsession.v2.cmn.GSConst.SESSION_KEY; %>
<%@ page import="java.util.*"%>
<%@ page import="jp.groupsession.v2.cmn.GSConst" %>
<%@ page import="jp.groupsession.v2.kintai.Knt302.Knt302UserModel" %>
<%@ page import="jp.groupsession.v2.kintai.Knt200.Knt200Model" %>

<html:html>
<head>
	<title>[GroupSession] Knt200</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<script language="JavaScript" src="../common/js/cmd.js?<%= GSConst.VERSION_PARAM %>"></script>
	<script language="JavaScript" src="../kintai/js/knt100.js"></script>
	<script language="JavaScript" src="../kintai/js/knt200.js?<%= GSConst.VERSION_PARAM %>"></script>
	<link rel=stylesheet type='text/css' href='../common/css/default.css'>
	<style type="text/css">
	<!--
		*{padding:5px; margin:0px;}
		body{text-align:center;}
		table{background:white; border-collapse:collapse;margin-left: auto;margin-right: auto;}
	-->
	</style>
</head>

<body class="body_03">
<html:form action="/kintai/Knt200">

<input type="hidden" name="CMD" value="">

<%
	// カレンダーの初期化
	Calendar cal=Calendar.getInstance();
	// ユーザー一覧を取得
	ArrayList<Knt302UserModel> users = (ArrayList<Knt302UserModel>) request.getAttribute("usrList");
	// 実績一覧を取得
	ArrayList<Knt200Model> fromToTimes = (ArrayList<Knt200Model>) request.getAttribute("fromToTimes");
	// 編集対象情報
	int selUsrSid = (int)request.getAttribute("selUsrSid");
	int startY = (int)request.getAttribute("startYear");
	int startM = (int)request.getAttribute("startMonth");
	int startD = (int)request.getAttribute("startDay");
	int endY = (int)request.getAttribute("endYear");
	int endM = (int)request.getAttribute("endMonth");
	int endD = (int)request.getAttribute("endDay");
	// 祝日
	ArrayList<String> holiday = (ArrayList<String>)request.getAttribute("holiday");
%>

<table width="100%" border="0">
	<tr>
		<td align="left">
			<h1>総務部確認</h1>
		</td>
	</tr>
</table>

<table width="100%" border="0">
	<tr>
		<td>
			社員名：
		</td>
		<td>
			<select name="selUsrSid";>
			<option value="-1">選択してください</option>
			<%for(int i=0; i<users.size(); i++) {%>
				<%Knt302UserModel usr = (Knt302UserModel)users.get(i);%>
				<%
					int usrSid = usr.getUsrSid();
					String usrName = usr.getUsrName();
					String strChecked = "";
				%>
				<option value=<%=usrSid%> <%= usrSid==selUsrSid ? " selected=\"selected\"" : "" %> ><%=usrName%></option>
			<%}%>
			</select>
		</td>
	</tr>
	<tr>
		<td>
			期間：
		</td>
		<td align="left">

			<select name="startYear";>
			<%for(int i=2021; i<=2100; i++) {%>
				<option value=<%=i%> <%= i==startY ? " selected=\"selected\"" : "" %>><%=i%></option>
			<%}%>
			</select>
			<select name="startMonth";>
			<%for(int i=1; i<=12; i++) {%>
				<option value=<%=i%> <%= i==startM ? " selected=\"selected\"" : "" %>><%=i%></option>
			<%}%>
			</select>
			<select name="startDay";>
			<%for(int i=1; i<=31; i++) {%>
				<option value=<%=i%> <%= i==startD ? " selected=\"selected\"" : "" %>><%=i%></option>
			<%}%>
			</select>

			～

			<select name="endYear";>
			<%for(int i=2021; i<=2100; i++) {%>
				<option value=<%=i%> <%= i==endY ? " selected=\"selected\"" : "" %>><%=i%></option>
			<%}%>
			</select>
			<select name="endMonth";>
			<%for(int i=1; i<=12; i++) {%>
				<option value=<%=i%> <%= i==endM ? " selected=\"selected\"" : "" %>><%=i%></option>
			<%}%>
			</select>
			<select name="endDay";>
			<%for(int i=1; i<=31; i++) {%>
				<option value=<%=i%> <%= i==endD ? " selected=\"selected\"" : "" %>><%=i%></option>
			<%}%>
			</select>

		</td>
	</tr>
	<tr>
		<td>
		</td>
		<td>
			<input type="button" value="　検　索　" onClick="buttonPush('search');">
			<input type="button" value="勤務表出力" onClick="buttonPush('xlsx');">
			<logic:messagesPresent property="fromTo">
				<font color="red">終了期間は開始期間より以後を選択してください。</font>
			</logic:messagesPresent>
		</td>
		<td align="right">
			<logic:messagesPresent property="registed">
				<font color="black">登録しました。</font>
			</logic:messagesPresent>
			<input type="button" value=" 一括チェック " onclick="allcheck(true);">
			<input type="button" value=" 一括チェック解除 " onclick="allcheck(false);">
			<input type="button" value="　　登　録　　" onClick="buttonPush('regist');">
			<button type="button"><html:link action="/kintai/Knt001">　戻　る　</html:link></button>
		</td>
	</tr>
</table>

<br>

<table width="100%" border=1>
	<th bgcolor="#bfefdf" width="12%">日付</th>
	<th bgcolor="#bfefdf" width="6%">出勤</th>
	<th bgcolor="#bfefdf" width="6%">退勤<br>（時間内）</th>
	<th bgcolor="#bfefdf" width="6%">出勤<br>（時間内）</th>
	<th bgcolor="#bfefdf" width="6%">退勤</th>
	<th bgcolor="#bfefdf" width="6%">申請時間<br>合計</th>
	<th bgcolor="#bfefdf" width="6%">申請時間<br>内有給</th>
	<th bgcolor="#bfefdf" width="5%">通常<br>残業</th>
	<th bgcolor="#bfefdf" width="5%">深夜<br>残業</th>
	<th bgcolor="#bfefdf" width="25%">備考</th>
	<th bgcolor="#bfefdf" width="4%">代休<br>予定</th>
	<th bgcolor="#bfefdf" width="4%">承認</th>
	<th bgcolor="#bfefdf" width="4%">確認</th>

	<%
		double holidayTotalWrkTime = 0;
		double holidayTotalOverTime = 0;
		double holidayTotalPaidTime = 0;
		double holidayTotalMidnightTime = 0;
		double totalWrkTime = 0;
		double totalOverTime = 0;
		double totalPaidTime = 0;
		double totalMidnightTime = 0;
		String daiQ = "";
	%>

	<%for(int i=0; i<fromToTimes.size(); i++) {%>
	<%Knt200Model ftt = (Knt200Model)fromToTimes.get(i);%>
	<tr>
		<td>
			<%
				// 年月日
				cal.set(ftt.getWrkYear(), ftt.getWrkMonth() - 1, ftt.getWrkDay());
				int rowY = cal.get(Calendar.YEAR);
				int rowM = cal.get(Calendar.MONTH) + 1;
				int rowD = cal.get(Calendar.DATE);
				// 曜日
				int k = cal.get(Calendar.DAY_OF_WEEK);
				String dw = "";
				// 土日祝日
				String holiday1 = "<font color='black'>";
				String holiday2 = "</font>";
				switch (k){
				case 1:
					dw = "日";
					holiday1 = "<font color='red'>";
					holiday2 = "</font>";
					break;
				case 2:
					dw = "月";
					break;
				case 3:
					dw = "火";
					break;
				case 4:
					dw = "水";
					break;
				case 5:
					dw = "木";
					break;
				case 6:
					dw = "金";
					break;
				case 7:
					dw = "土";
					holiday1 = "<font color='red'>";
					holiday2 = "</font>";
					break;
				}
				// 祝日判断
				String ymd = String.valueOf(rowY) + String.format("%02d",rowM) + String.format("%02d", rowD);
				boolean holi = holiday.contains(ymd);
				if (holi){
					holiday1 = "<font color='red'>";
					holiday2 = "</font>";
				}
				// 代休
				if (ftt.getDaiQChk()==1) {
					daiQ = "あり";
				} else {
					daiQ = "";
				}
			%>
			<%=holiday1%>
			<%=rowM%>月<%=rowD%>日（<%=dw%>）
			<%=holiday2%>
			<%
				if (!holiday1.equals("<font color='black'>")) {
					holidayTotalWrkTime += ftt.getWrkTime() + ftt.getPaidTime();
					holidayTotalPaidTime += ftt.getPaidTime();
					holidayTotalOverTime += ftt.getOverTime();
					holidayTotalMidnightTime += ftt.getMidnightTime();
				}
				totalWrkTime += ftt.getWrkTime() + ftt.getPaidTime();
				totalPaidTime += ftt.getPaidTime();
				totalOverTime += ftt.getOverTime();
				totalMidnightTime += ftt.getMidnightTime();
			%>
		</td>
		<td align="center">
			<%=ftt.getFirstInTime()%>
		</td>
		<td align="center">
			<%=ftt.getOutTime()%>
		</td>
		<td align="center">
			<%=ftt.getInTime()%>
		</td>
		<td align="center">
			<%=ftt.getLastOutTime()%>
		</td>
		<td align="right">
			<fmt:formatNumber value="<%=ftt.getWrkTime() + ftt.getPaidTime()%>" pattern="0.00" />
		</td>
		<td align="right">
			<fmt:formatNumber value="<%=ftt.getPaidTime()%>" pattern="0.00" />
		</td>
		<td align="right">
			<fmt:formatNumber value="<%=ftt.getOverTime()%>" pattern="0.00" />
		</td>
		<td align="right">
			<fmt:formatNumber value="<%=ftt.getMidnightTime()%>" pattern="0.00" />
		</td>
		<td>
			<input type="text" name="smuRmk<%=rowY%><%=rowM%><%=rowD%>" value="<%=ftt.getSmuRmk()%>" maxlength="100" style="width:100%;">
		</td>
		<td align="center">
			<%=daiQ%>
		</td>
		<td align="center">
			<input type="checkbox" name="smuChk<%=rowY%><%=rowM%><%=rowD%>" value=<%=rowY%>,<%=rowM%>,<%=rowD%> <%= ftt.getSmuChk()==1 ? " checked=\"checked\"" : "" %>>
		</td>
		<td align="center">
			<input type="button" style="font-size:110%;border:none;background-color:transparent;color:blue;"  value="確認" onClick="buttonPush('detail002,<%=rowY%>,<%=rowM%>,<%=rowD%>');">
		</td>
	</tr>
	<%}%>

	<tr>
		<td align="center" colspan="5">
			休日合計
		</td>
		<td align="right">
			<fmt:formatNumber value="<%=holidayTotalWrkTime%>" pattern="0.00" />
		</td>
		<td align="right">
			<fmt:formatNumber value="<%=holidayTotalPaidTime%>" pattern="0.00" />
		</td>
		<td align="right">
			<fmt:formatNumber value="<%=holidayTotalOverTime%>" pattern="0.00" />
		</td>
		<td align="right">
			<fmt:formatNumber value="<%=holidayTotalMidnightTime%>" pattern="0.00" />
		</td>
		<td>
		</td>
		<td>
		</td>
		<td>
		</td>
		<td>
		</td>
	</tr>

	<tr>
		<td align="center" colspan="5">
			合　　計
		</td>
		<td align="right">
			<fmt:formatNumber value="<%=totalWrkTime%>" pattern="0.00" />
		</td>
		<td align="right">
			<fmt:formatNumber value="<%=totalPaidTime%>" pattern="0.00" />
		</td>
		<td align="right">
			<fmt:formatNumber value="<%=totalOverTime%>" pattern="0.00" />
		</td>
		<td align="right">
			<fmt:formatNumber value="<%=totalMidnightTime%>" pattern="0.00" />
		</td>
		<td>
		</td>
		<td>
		</td>
		<td>
		</td>
		<td>
		</td>
	</tr>

</table>

</html:form>
</body>
</html:html>