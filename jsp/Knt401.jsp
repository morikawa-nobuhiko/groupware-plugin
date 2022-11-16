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
	// 編集対象情報
	int isEdit = (int)request.getAttribute("isEdit");
	int wrkId = (int)request.getAttribute("wrkId");
	String wrkName = (String)request.getAttribute("wrkName");
	String cmdReg = "";
	String cmdBtn = "";
	if (isEdit==0){
		cmdReg = "insert";
		cmdBtn = "　登　録　";
	} else {
		cmdReg = "update";
		cmdBtn = "　更　新　";
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
			<h1>作業編集</h1>
		</td>
		<td colspan=1>
			<input type="button" value="<%=cmdBtn%>" onClick="buttonPush('<%=cmdReg%>');">
			<%=comM1%>
			<input type="button" value="　削　除　" onClick="buttonPush('delete');">
			<%=comM2%>
			<button type="button"><html:link action="/kintai/Knt400">　戻　る　</html:link></button>
		</td>
	</tr>

	<tr>
		<td>
			作業名：
		</td>
		<td>
			<input type="text" name="wrkName" size="50" maxlength="100" value="<%=wrkName%>" placeholder="必須項目です。">
			<logic:messagesPresent property="wrkName">
				<p><font color="red">※作業名称は必須項目です。</font></p>
			</logic:messagesPresent>
		</td>
	</tr>

	<tr>
		<td>
		</td>
		<td>
			※「有給」と入力しても有給扱いにはなりません。
		</td>
	</tr>

	<tr>
		<td>
		</td>
		<td>
			<div class="invisibleFrame">
			<input type="text" name="wrkId" size="8" maxlength="4" value="<%=wrkId%>" readonly placeholder="必須項目です。">
			</div>
			<logic:messagesPresent property="wrkId">
				<p><font color="red">※作業IDは必須項目です。</font></p>
			</logic:messagesPresent>
		</td>
	</tr>

</table>

</html:form>
</body>
</html:html>
