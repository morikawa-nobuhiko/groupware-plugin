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
	<title>[GroupSession] Knt305</title>
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
<html:form action="/kintai/Knt305">

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
%>

<table width="100%" border="0">
	<tr>
		<td align="left">
			<h1>タイム編集</h1>
		</td>
	</tr>
</table>

<table width="100%" border="0">
	<tr>
		<td width="10%">
			社員名：
		</td>
		<td width="20%">
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
		<td>
		</td>
	</tr>
	<tr>
		<td>
			日付：
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
			<input type="button" value="　表　示　" onClick="buttonPush('search');">
		</td>
		<td align="right">
			<logic:messagesPresent property="registed">
				<font color="black">登録しました。</font>
			</logic:messagesPresent>
			<input type="button" value="　　登　録　　" onClick="buttonPush('regist');">
			<button type="button"><html:link action="/kintai/Knt001">　戻　る　</html:link></button>
		</td>
	</tr>
</table>

<br>

<table width="100%" border=1>
	<th bgcolor="#bfefdf" width="15%">日付</th>
	<th bgcolor="#bfefdf" width="15%">出勤</th>
	<th bgcolor="#bfefdf" width="15%">退勤</th>
	<th bgcolor="#bfefdf" width="15%">時間内（退勤）</th>
	<th bgcolor="#bfefdf" width="15%">時間内（出勤）</th>
	<th bgcolor="#bfefdf" width="25%">備考</th>

	<%for(int i=0; i<fromToTimes.size(); i++) {%>
	<%Knt200Model ftt = (Knt200Model)fromToTimes.get(i);%>
	<tr>
		<td>
			<%
				// 年月日
				cal.set(ftt.getWrkYear(), ftt.getWrkMonth(), ftt.getWrkDay());
				int rowY = cal.get(Calendar.YEAR);
				int rowM = cal.get(Calendar.MONTH);
				int rowD = cal.get(Calendar.DATE);
				// 曜日
				int k = cal.get(Calendar.DAY_OF_WEEK);
				String dw = "";
				switch (k){
				case 1:
					dw = "日";
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
					break;
				}
			%>
			<%=rowM%>月<%=rowD%>日（<%=dw%>）
		</td>
		<td align="right">
			<fmt:formatNumber value="<%=ftt.getWrkTime()%>" pattern="0.00" />
		</td>
		<td align="right">
			<fmt:formatNumber value="<%=ftt.getPaidTime()%>" pattern="0.00" />
		</td>
		<td align="right">
			<fmt:formatNumber value="<%=ftt.getWrkTime() + ftt.getPaidTime()%>" pattern="0.00" />
		</td>
		<td>
			<input type="text" name="smuRmk" value="<%=ftt.getSmuRmk()%>" maxlength="100" style="width:100%;">
		</td>
		<td>
			<input type="text" name="remark" value="" maxlength="100" style="width:100%;">
		</td>
	</tr>
	<%}%>

</table>

</html:form>
</body>
</html:html>