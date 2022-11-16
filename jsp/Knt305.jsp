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
%>

<table width="100%" border="0">
	<tr>
		<td align="left">
			<h1>�^�C���ҏW</h1>
		</td>
	</tr>
</table>

<table width="100%" border="0">
	<tr>
		<td width="10%">
			�Ј����F
		</td>
		<td width="20%">
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
		<td>
		</td>
	</tr>
	<tr>
		<td>
			���t�F
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
			<input type="button" value="�@�\�@���@" onClick="buttonPush('search');">
		</td>
		<td align="right">
			<logic:messagesPresent property="registed">
				<font color="black">�o�^���܂����B</font>
			</logic:messagesPresent>
			<input type="button" value="�@�@�o�@�^�@�@" onClick="buttonPush('regist');">
			<button type="button"><html:link action="/kintai/Knt001">�@�߁@��@</html:link></button>
		</td>
	</tr>
</table>

<br>

<table width="100%" border=1>
	<th bgcolor="#bfefdf" width="15%">���t</th>
	<th bgcolor="#bfefdf" width="15%">�o��</th>
	<th bgcolor="#bfefdf" width="15%">�ދ�</th>
	<th bgcolor="#bfefdf" width="15%">���ԓ��i�ދ΁j</th>
	<th bgcolor="#bfefdf" width="15%">���ԓ��i�o�΁j</th>
	<th bgcolor="#bfefdf" width="25%">���l</th>

	<%for(int i=0; i<fromToTimes.size(); i++) {%>
	<%Knt200Model ftt = (Knt200Model)fromToTimes.get(i);%>
	<tr>
		<td>
			<%
				// �N����
				cal.set(ftt.getWrkYear(), ftt.getWrkMonth(), ftt.getWrkDay());
				int rowY = cal.get(Calendar.YEAR);
				int rowM = cal.get(Calendar.MONTH);
				int rowD = cal.get(Calendar.DATE);
				// �j��
				int k = cal.get(Calendar.DAY_OF_WEEK);
				String dw = "";
				switch (k){
				case 1:
					dw = "��";
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
					break;
				}
			%>
			<%=rowM%>��<%=rowD%>���i<%=dw%>�j
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