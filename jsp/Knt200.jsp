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
	// �J�����_�[�̏�����
	Calendar cal=Calendar.getInstance();
	// ���[�U�[�ꗗ���擾
	ArrayList<Knt302UserModel> users = (ArrayList<Knt302UserModel>) request.getAttribute("usrList");
	// ���шꗗ���擾
	ArrayList<Knt200Model> fromToTimes = (ArrayList<Knt200Model>) request.getAttribute("fromToTimes");
	// �ҏW�Ώۏ��
	int selUsrSid = (int)request.getAttribute("selUsrSid");
	int startY = (int)request.getAttribute("startYear");
	int startM = (int)request.getAttribute("startMonth");
	int startD = (int)request.getAttribute("startDay");
	int endY = (int)request.getAttribute("endYear");
	int endM = (int)request.getAttribute("endMonth");
	int endD = (int)request.getAttribute("endDay");
	// �j��
	ArrayList<String> holiday = (ArrayList<String>)request.getAttribute("holiday");
%>

<table width="100%" border="0">
	<tr>
		<td align="left">
			<h1>�������m�F</h1>
		</td>
	</tr>
</table>

<table width="100%" border="0">
	<tr>
		<td>
			�Ј����F
		</td>
		<td>
			<select name="selUsrSid";>
			<option value="-1">�I�����Ă�������</option>
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
			���ԁF
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

			�`

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
			<input type="button" value="�@���@���@" onClick="buttonPush('search');">
			<input type="button" value="�Ζ��\�o��" onClick="buttonPush('xlsx');">
			<logic:messagesPresent property="fromTo">
				<font color="red">�I�����Ԃ͊J�n���Ԃ��Ȍ��I�����Ă��������B</font>
			</logic:messagesPresent>
		</td>
		<td align="right">
			<logic:messagesPresent property="registed">
				<font color="black">�o�^���܂����B</font>
			</logic:messagesPresent>
			<input type="button" value=" �ꊇ�`�F�b�N " onclick="allcheck(true);">
			<input type="button" value=" �ꊇ�`�F�b�N���� " onclick="allcheck(false);">
			<input type="button" value="�@�@�o�@�^�@�@" onClick="buttonPush('regist');">
			<button type="button"><html:link action="/kintai/Knt001">�@�߁@��@</html:link></button>
		</td>
	</tr>
</table>

<br>

<table width="100%" border=1>
	<th bgcolor="#bfefdf" width="12%">���t</th>
	<th bgcolor="#bfefdf" width="6%">�o��</th>
	<th bgcolor="#bfefdf" width="6%">�ދ�<br>�i���ԓ��j</th>
	<th bgcolor="#bfefdf" width="6%">�o��<br>�i���ԓ��j</th>
	<th bgcolor="#bfefdf" width="6%">�ދ�</th>
	<th bgcolor="#bfefdf" width="6%">�\������<br>���v</th>
	<th bgcolor="#bfefdf" width="6%">�\������<br>���L��</th>
	<th bgcolor="#bfefdf" width="5%">�ʏ�<br>�c��</th>
	<th bgcolor="#bfefdf" width="5%">�[��<br>�c��</th>
	<th bgcolor="#bfefdf" width="25%">���l</th>
	<th bgcolor="#bfefdf" width="4%">��x<br>�\��</th>
	<th bgcolor="#bfefdf" width="4%">���F</th>
	<th bgcolor="#bfefdf" width="4%">�m�F</th>

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
				// �N����
				cal.set(ftt.getWrkYear(), ftt.getWrkMonth() - 1, ftt.getWrkDay());
				int rowY = cal.get(Calendar.YEAR);
				int rowM = cal.get(Calendar.MONTH) + 1;
				int rowD = cal.get(Calendar.DATE);
				// �j��
				int k = cal.get(Calendar.DAY_OF_WEEK);
				String dw = "";
				// �y���j��
				String holiday1 = "<font color='black'>";
				String holiday2 = "</font>";
				switch (k){
				case 1:
					dw = "��";
					holiday1 = "<font color='red'>";
					holiday2 = "</font>";
					break;
				case 2:
					dw = "��";
					break;
				case 3:
					dw = "��";
					break;
				case 4:
					dw = "��";
					break;
				case 5:
					dw = "��";
					break;
				case 6:
					dw = "��";
					break;
				case 7:
					dw = "�y";
					holiday1 = "<font color='red'>";
					holiday2 = "</font>";
					break;
				}
				// �j�����f
				String ymd = String.valueOf(rowY) + String.format("%02d",rowM) + String.format("%02d", rowD);
				boolean holi = holiday.contains(ymd);
				if (holi){
					holiday1 = "<font color='red'>";
					holiday2 = "</font>";
				}
				// ��x
				if (ftt.getDaiQChk()==1) {
					daiQ = "����";
				} else {
					daiQ = "";
				}
			%>
			<%=holiday1%>
			<%=rowM%>��<%=rowD%>���i<%=dw%>�j
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
			<input type="button" style="font-size:110%;border:none;background-color:transparent;color:blue;"  value="�m�F" onClick="buttonPush('detail002,<%=rowY%>,<%=rowM%>,<%=rowD%>');">
		</td>
	</tr>
	<%}%>

	<tr>
		<td align="center" colspan="5">
			�x�����v
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
			���@�@�v
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