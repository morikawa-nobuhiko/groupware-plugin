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
<%@ page import="jp.groupsession.v2.kintai.Knt900.Knt900Model" %>

<html:html>
<head>
	<title>[GroupSession] Knt900</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<script language="JavaScript" src="../common/js/cmd.js?<%= GSConst.VERSION_PARAM %>"></script>
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
<html:form action="/kintai/Knt900">

<input type="hidden" name="CMD" value="">

<%
	// �v���W�F�N�g�ꗗ���擾
	ArrayList<Knt900Model> prjModels = (ArrayList<Knt900Model>) request.getAttribute("prjList");
	String prjId = (String)request.getAttribute("prjId");
	String prjName = (String)request.getAttribute("prjName");
	int startY = (int)request.getAttribute("startY");
	int startM =  (int)request.getAttribute("startM");
	int startD =  (int)request.getAttribute("startD");
	int endY = (int)request.getAttribute("endY");
	int endM = (int)request.getAttribute("endM");
	int endD = (int)request.getAttribute("endD");
	int ptn = (int)request.getAttribute("searched");
	int unit = (int)request.getAttribute("unit");
%>

<table width="1000px" border="0">
	<tr>
		<td align="left" width="30%" colspan=1>
			<h1>�o�c��񌟍�</h1>
		</td>
		<td align="left" colspan=1>
			<logic:messagesPresent property="fromTo">
				<font color="red">���I�����Ԃ͊J�n���Ԃ��Ȍ��I�����Ă��������B</font>
			</logic:messagesPresent>
		</td>
	</tr>
	<tr>
		<td>
			<input type="radio" name="search" value="0" <%= ptn==0 ? " checked" : "" %> >&nbsp;�v���W�F�N�gID�F
		</td>
		<td>
			<input type="text" name="prjId" size="8" maxlength="4" value="<%=prjId%>" placeholder="��F0001">
		</td>
	</tr>
	<tr>
		<td>
			<input type="radio" name="search" value="1" <%= ptn==1 ? " checked" : "" %>>&nbsp;�v���W�F�N�g���F
		</td>
		<td>
			<input type="text" name="prjName" size="30" maxlength="100" value="<%=prjName%>">
		</td>
	</tr>
	<tr>
		<td>
			<input type="radio" name="search" value="2" <%= ptn==2 ? " checked" : "" %>>&nbsp;���ԁF
		</td>
		<td align="left">
			<select name="startYear">
			<%for(int i=2021; i<=2100; i++) {%>
				<option value=<%=i%> <%= i==startY ? " selected=\"selected\"" : "" %>><%=i%></option>
			<%}%>
			</select>
			<select name="startMonth">
			<%for(int i=1; i<=12; i++) {%>
				<option value=<%=i%> <%= i==startM ? " selected=\"selected\"" : "" %>><%=i%></option>
			<%}%>
			</select>
			<select name="startDay">
			<%for(int i=1; i<=31; i++) {%>
				<option value=<%=i%> <%= i==startD ? " selected=\"selected\"" : "" %>><%=i%></option>
			<%}%>
			</select>
			�`
			<select name="endYear">
			<%for(int i=2021; i<=2100; i++) {%>
				<option value=<%=i%> <%= i==endY ? " selected=\"selected\"" : "" %>><%=i%></option>
			<%}%>
			</select>
			<select name="endMonth">
			<%for(int i=1; i<=12; i++) {%>
				<option value=<%=i%> <%= i==endM ? " selected=\"selected\"" : "" %>><%=i%></option>
			<%}%>
			</select>
			<select name="endDay">
			<%for(int i=1; i<=31; i++) {%>
				<option value=<%=i%> <%= i==endD ? " selected=\"selected\"" : "" %>><%=i%></option>
			<%}%>
			</select>
		</td>

		</td>
	</tr>
	<tr>
		<td valign="top">
			�@�o�͒P�ʁF
		</td>
		<td align="left">
			<input type="radio" name="unit" value="0" <%= unit==0 ? " checked" : "" %> >&nbsp;�v���W�F�N�g
			<br>
			<input type="radio" name="unit" value="1" <%= unit==1 ? " checked" : "" %> >&nbsp;�Ј�
		</td>
	</tr>
	<tr>
		<td>
		</td>
		<td colspan=2>
			<input type="button" value="�@���@���@" onClick="buttonPush('select');" />
			<input type="button" value="�G�N�X�|�[�g" onClick="buttonPush('export_csv');">
			<button type="button"><html:link action="/kintai/Knt001">�@�߁@��@</html:link></button>
		</td>
	</tr>
</table>

<br>

<%
	String com1 = "�v���W�F�N�g����";
	String com2 = "�J�n����";
	String com3 = "�I������";
	// �Ј�
	if (unit == 1) {
		com1 = "�v���W�F�N�g����";
		com2 = "���[�U�[ID";
		com3 = "�N/��";
	}
%>

<table width="1000px" border="2">
	<th bgcolor="#bfefdf">�v���W�F�N�gID</th>
	<th bgcolor="#bfefdf"><%=com1%></th>
	<th bgcolor="#bfefdf"><%=com2%></th>
	<th bgcolor="#bfefdf"><%=com3%></th>
	<th bgcolor="#bfefdf">�\�����ԁi�L�����O�j</th>

	<%for(int i=0; i<prjModels.size(); i++) {%>
		<tr>
			<%Knt900Model model = (Knt900Model)prjModels.get(i);%>
			<%
				int psid = model.getPrjSid();
				int usrSid = model.getUsrSid();
				String pid = model.getPrjId();
				String pn = model.getPrjName();
				int sy = model.getStartYear();
				int sm = model.getStartMonth();
				int sd = model.getStartDay();
				int ey = model.getEndYear();
				int em = model.getEndMonth();
				int ed = model.getEndDay();
				double pt = model.getPrjTime();
				String sdate = sy + "/" + sm + "/" + sd;
				String edate = ey + "/" + em + "/" + ed;
				// �Ј��P��
				if (unit == 1) {
					sdate = usrSid + "";
					edate = model.getUsrYear() + "/" + model.getUsrMonth();
				}
			%>
			<!-- �v���W�F�N�g�ꗗ -->
			<td align="center">
				<!-- �v���W�F�N�gID -->
				<%=pid%>
			</td>
			<td>
				<!-- �v���W�F�N�g���� -->
				<%=pn%>
			</td>
			<td align="center">
				<!-- �J�n���� or ���[�U�[ID -->
				<%=sdate%>
			</td>
			<td align="center">
				<!-- �I������ or �N��-->
				<%=edate%>
			</td>
			<td align="right">
				<!-- ���� -->
				<fmt:formatNumber value="<%=pt%>" pattern="0.00" />
			</td>
		</tr>
	<%}%>
</table>

</html:form>
</body>
</html:html>