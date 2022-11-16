<%@ page pageEncoding="Windows-31J" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="/WEB-INF/ctag-message.tld" prefix="gsmsg" %>
<% String key = jp.groupsession.v2.cmn.GSConst.SESSION_KEY; %>
<%@ page import="java.util.*"%>
<%@ page import="jp.groupsession.v2.cmn.GSConst" %>

<html:html>
<head>
	<title>[GroupSession] Knt401</title>
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
<html:form action="/kintai/Knt401">

<input type="hidden" name="CMD" value="">
<html:hidden name="Knt401Form" property="delWrkId" />

<%
	// �ҏW�Ώۏ��
	int isEdit = (int)request.getAttribute("isEdit");
	int wrkId = (int)request.getAttribute("wrkId");
	String wrkName = (String)request.getAttribute("wrkName");
	String cmdReg = "";
	String cmdBtn = "";
	if (isEdit==0){
		cmdReg = "insert";
		cmdBtn = "�@�o�@�^�@";
	} else {
		cmdReg = "update";
		cmdBtn = "�@�X�@�V�@";
	}
	String comM1 = "";
	String comM2 = "";
	if (isEdit==0){
		comM1 = "<!--";
		comM2 = "-->";
	}
%>

<table border="0">
	<tr>
	<tr>
		<td>
			<h1>��ƕҏW</h1>
		</td>
		<td colspan=1>
			<input type="button" value="<%=cmdBtn%>" onClick="buttonPush('<%=cmdReg%>');">
			<%=comM1%>
			<input type="button" value="�@��@���@" onClick="buttonPush('delete');">
			<%=comM2%>
			<button type="button"><html:link action="/kintai/Knt400">�@�߁@��@</html:link></button>
		</td>
	</tr>

	<tr>
		<td>
			��Ɩ��F
		</td>
		<td>
			<input type="text" name="wrkName" size="50" maxlength="100" value="<%=wrkName%>" placeholder="�K�{���ڂł��B">
			<logic:messagesPresent property="wrkName">
				<p><font color="red">����Ɩ��͕̂K�{���ڂł��B</font></p>
			</logic:messagesPresent>
		</td>
	</tr>

	<tr>
		<td>
		</td>
		<td>
			���u�L���v�Ɠ��͂��Ă��L�������ɂ͂Ȃ�܂���B
		</td>
	</tr>

	<tr>
		<td>
		</td>
		<td>
			<div class="invisibleFrame">
			<input type="text" name="wrkId" size="8" maxlength="4" value="<%=wrkId%>" readonly placeholder="�K�{���ڂł��B">
			</div>
			<logic:messagesPresent property="wrkId">
				<p><font color="red">�����ID�͕K�{���ڂł��B</font></p>
			</logic:messagesPresent>
		</td>
	</tr>

</table>

</html:form>
</body>
</html:html>
