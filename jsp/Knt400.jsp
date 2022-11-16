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
<%@ page import="jp.groupsession.v2.kintai.Knt400.Knt400Model" %>

<html:html>
<head>
	<title>[GroupSession] Knt400</title>
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
<html:form action="/kintai/Knt400">

<input type="hidden" name="CMD" value="">

<%
	// ��ƈꗗ���擾
	ArrayList<Knt400Model> wrkModels = (ArrayList<Knt400Model>) request.getAttribute("wrkList");
%>

<table border="0" width="500px">
	<tr>
		<td align="left" width="150px">
			<h1>��ƊǗ�</h1>
		</td>
		<td colspan=1>
			<button type="button"><html:link action="/kintai/Knt401">�@�V�@�K�@</html:link></button>
			<button type="button"><html:link action="/kintai/Knt001">�@�߁@��@</html:link></button>
		</td>
	</tr>
</table>

<br>

<table border="1" width="500px">
	<tr>
		<th bgcolor="#bfefdf">��Ɩ�</th>
		<th bgcolor="#bfefdf">�ҏW</th>
	</tr>
	<%for(int i=0; i<wrkModels.size(); i++) {%>
		<tr>
			<%Knt400Model model = (Knt400Model)wrkModels.get(i);%>
			<%
				int wrkId = model.getWrkId();
				String wrkName = model.getWrkName();
			%>
			<!-- ��ƈꗗ -->
			<td align="center">
				<%=wrkName%>
			</td>
			<td align="center">
				<input type="button" value="�@�ҁ@�W�@" onClick="buttonPush('edit<%=wrkId%>');">
			</td>
		</tr>
	<%}%>
</table>

<h1>
<br>
</html:form>
</body>
</html:html>