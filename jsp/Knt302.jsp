<%@ page pageEncoding="Windows-31J" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="/WEB-INF/ctag-message.tld" prefix="gsmsg" %>
<% String key = jp.groupsession.v2.cmn.GSConst.SESSION_KEY; %>
<%@ page import="java.util.*"%>
<%@ page import="jp.groupsession.v2.cmn.GSConst" %>
<%@ page import="jp.groupsession.v2.kintai.Knt302.Knt302UserModel" %>

<html:html>
<head>
	<title>[GroupSession] Knt302</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<script language="JavaScript" src="../common/js/cmd.js?<%= GSConst.VERSION_PARAM %>"></script>
	<link rel=stylesheet type='text/css' href='../common/css/default.css'>
	<link rel=stylesheet href='../kintai/css/knt.css?<%= GSConst.VERSION_PARAM %>' type='text/css'>
	<style type="text/css">
	<!--
		*{padding:5px; margin:0px;}
		table{background:white; border-collapse:collapse;margin-left: auto;margin-right: auto;}
	-->
	</style>
</head>

<body class="body_03">
<html:form action="/kintai/Knt302">

<input type="hidden" name="CMD" value="">
<html:hidden name="Knt302Form" property="delPrjSid" />

<%
	// �S���҈ꗗ���擾
	ArrayList<Knt302UserModel> usrModels = (ArrayList<Knt302UserModel>) request.getAttribute("usrList");
	// �v���W�F�N�g�S���҈ꗗ���擾
	ArrayList<Integer> prjUsrs = (ArrayList<Integer>) request.getAttribute("projectUsrList");
	// �ҏW�Ώۏ��
	int isEdit = (int)request.getAttribute("isEdit");
	int prjSid = (int)request.getAttribute("prjSid");
	String prjId = (String)request.getAttribute("prjId");
	String prjName = (String)request.getAttribute("prjName");
	int startY = (int)request.getAttribute("startY");
	int startM = (int)request.getAttribute("startM");
	int startD = (int)request.getAttribute("startD");
	int endY = (int)request.getAttribute("endY");
	int endM = (int)request.getAttribute("endM");
	int endD = (int)request.getAttribute("endD");
	int indirect = (int)request.getAttribute("indirect");
	String cmdReg = "";
	String cmdBtn = "";
	if (isEdit==0){
		cmdReg = "insert";
		cmdBtn = "�@�o�@�^�@";
	} else {
		cmdReg = "update";
		cmdBtn = "�@�X�@�V�@";
	}
	// 2021.07.29 �ǉ�
	String orderName = (String)request.getAttribute("orderName");
	String chiefName = (String)request.getAttribute("chiefName");
	String managerName = (String)request.getAttribute("managerName");
	String remark = (String)request.getAttribute("remark");
	int targetB = (int)request.getAttribute("targetB");
	int targetS = (int)request.getAttribute("targetS");
	int targetH = (int)request.getAttribute("targetH");
	int targetJ = (int)request.getAttribute("targetJ");

%>

<table border="0">
	<tr>
	<tr>
		<td>
			<h1>�Ǘ����o�^</h1>
		</td>
		<td colspan=1>
			<input type="button" value="<%=cmdBtn%>" onClick="buttonPush('<%=cmdReg%>');">
			<input type="button" value="�@��@���@" onClick="buttonPush('delete');">
			<button type="button"><html:link action="/kintai/Knt001">�@�߁@��@</html:link></button>
			<button type="button"><html:link action="/kintai/Knt300">�@�Ǘ���񌟍��@</html:link></button>
		</td>
	</tr>
	<tr>
		<td>
			<logic:messagesPresent property="infoInserted">
				<p><font color="black">���o�^���܂����B</font></p>
			</logic:messagesPresent>
			<logic:messagesPresent property="infoUpdated">
				<p><font color="black">���X�V���܂����B</font></p>
			</logic:messagesPresent>
		</td>
	</tr>
	<tr>
		<td>
			�v���W�F�N�gID�F
		</td>
		<td>
<!--		<input type="text" name="prjId" size="8" maxlength="4" value="<%=prjId%>" <%= isEdit==1 ? "readonly" : "" %> placeholder="�K�{���ڂł��B">-->
			<input type="text" name="prjId" size="8" maxlength="4" value="<%=prjId%>" placeholder="�K�{���ڂł��B">
			<logic:messagesPresent property="prjId">
				<p><font color="red">���v���W�F�N�gID�͕K�{���ڂł��B</font></p>
			</logic:messagesPresent>
			<logic:messagesPresent property="errPrjId">
				<p><font color="red">�����ɑ��݂���v���W�F�N�gID�ł��B</font></p>
			</logic:messagesPresent>
		</td>
	</tr>
	<tr>
		<td>
			�v���W�F�N�g���F
		</td>
		<td>
			<input type="text" name="prjName" size="50" maxlength="100" value="<%=prjName%>" placeholder="�K�{���ڂł��B">
			<logic:messagesPresent property="prjName">
				<p><font color="red">���v���W�F�N�g���͕̂K�{���ڂł��B</font></p>
			</logic:messagesPresent>
		</td>
	</tr>
	<tr>
		<td>
			�����ҁF
		</td>
		<td>
			<input type="text" name="orderName" size="50" maxlength="100" value="<%=orderName%>">
		</td>
	</tr>
	<tr>
		<td valign="top">
			�ΏہF
		</td>
		<td>
			<input type="checkbox" name="targetB" value="1" <%= targetB==1 ? " checked" : "" %> >&nbsp;����
			<br>
			<input type="checkbox" name="targetS" value="1" <%= targetS==1 ? " checked" : "" %> >&nbsp;����
			<br>
			<input type="checkbox" name="targetH" value="1" <%= targetH==1 ? " checked" : "" %> >&nbsp;�⏞����
			<br>
			<input type="checkbox" name="targetJ" value="1" <%= targetJ==1 ? " checked" : "" %> >&nbsp;���ƔF��
		</td>
	</tr>
	<tr>
		<td>
			�J�n���ԁF
		</td>
		<td align="left">
			<select name="startYear">
			<%for(int i=2021; i<=2100; i++) {%>
				<option value=<%=i%> <%= i==startY ? " selected=\"selected\"" : "" %> ><%=i%></option>
			<%}%>
			</select>
			<select name="startMonth">
			<%for(int i=1; i<=12; i++) {%>
				<option value=<%=i%> <%= i==startM ? " selected=\"selected\"" : "" %> ><%=i%></option>
			<%}%>
			</select>
			<select name="startDay">
			<%for(int i=1; i<=31 ; i++) {%>
				<option value=<%=i%> <%= i==startD ? " selected=\"selected\"" : "" %> ><%=i%></option>
			<%}%>
			</select>
			<logic:messagesPresent property="errStartDate">
				<p><font color="red">�����t������������܂���B</font></p>
			</logic:messagesPresent>
		</td>
	</tr>
	<tr>
		<td>
			�I�����ԁF
		</td>
		<td align="left">
			<select name="endYear">
			<%for(int i=2021; i<=2100; i++) {%>
				<option value=<%=i%> <%= i==endY ? " selected=\"selected\"" : "" %> ><%=i%></option>
			<%}%>
			</select>
			<select name="endMonth">
			<%for(int i=1; i<=12; i++) {%>
				<option value=<%=i%> <%= i==endM ? " selected=\"selected\"" : "" %> ><%=i%></option>
			<%}%>
			</select>
			<select name="endDay">
			<%for(int i=1; i<=31; i++) {%>
				<option value=<%=i%> <%= i==endD ? " selected=\"selected\"" : "" %> ><%=i%></option>
			<%}%>
			</select>
			<logic:messagesPresent property="fromTo">
				<p><font color="red">���I�����Ԃ͊J�n���Ԃ��Ȍ��I�����Ă��������B</font></p>
			</logic:messagesPresent>
			<logic:messagesPresent property="errEndDate">
				<p><font color="red">�����t������������܂���B</font></p>
			</logic:messagesPresent>
		</td>
	</tr>
	<tr>
		<td>
			��C�S���ҁF
		</td>
		<td>
			<input type="text" name="chiefName" size="50" maxlength="100" value="<%=chiefName%>">
		</td>
	</tr>
	<tr>
		<td>
			�S���ҁF
		</td>
		<td>
			<input type="text" name="managerName" size="50" maxlength="100" value="<%=managerName%>">
		</td>
	</tr>
	<tr>
		<td>
			���l�F
		</td>
		<td>
			<input type="text" name="remark" size="50" maxlength="100" value="<%=remark%>">
		</td>
	</tr>
	<tr>
		<td valign="top">
			��Ƌ敪�F
		</td>
		<td>
			<input type="checkbox" name="indirect" value="1" <%= indirect==1 ? " checked=\"checked\"" : "" %> >&nbsp�Ԑڍ��<br>
		</td>
	</tr>
	<tr>
		<td valign="top">
			�S���ҁF
		</td>
		<td>
			<logic:messagesPresent property="users">
				<p><font color="red">���S���҂�I�����Ă��������B</font></p>
			</logic:messagesPresent>
			<form action="/Servlet/ServletCheckboxes" method="request">
			<%for(int i=0; i<usrModels.size(); i++) {%>
				<%Knt302UserModel model = (Knt302UserModel)usrModels.get(i);%>
				<%
					int usrSid = model.getUsrSid();
					String usrName = model.getUsrName();
					String strChecked = "";
				%>
				<input type="checkbox" name="user<%=usrSid%>" value="<%=usrSid%>" <%= prjUsrs.contains(usrSid) ? " checked=\"checked\"" : "" %> >&nbsp<%=usrName%><br>
			<%}%>
			</form>
		</td>
	</tr>
	<tr>
		<td>
		<div class="invisibleFrame">
			<input type="text" name="prjSid" size="8" maxlength="4" value="<%=prjSid%>" readonly placeholder="�f�o�b�O�p">
		</div>
		</td>
		<td>
		</td>
	</tr>

</table>

</html:form>
</body>
</html:html>
