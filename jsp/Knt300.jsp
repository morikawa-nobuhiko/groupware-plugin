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

<html:html>
<head>
	<title>[GroupSession] Knt300</title>
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
<html:form action="/kintai/Knt300">

<input type="hidden" name="CMD" value="">

<%
	String prjId = "";
	String prjName = "";
	int startY = 2021;
	int startM = 2;
	int startD = 1;
	int endY = 2021;
	int endM = 2;
	int endD = 28;
	String orderName = "";
	String chiefName = "";
	String managerName = "";
	int targetB = 0;
	int targetS = 0;
	int targetH = 0;
	int targetJ = 0;
	// 管理者グループ
	boolean manager = false;
%>

<!-- 権限 -->
<%
	String comM1 = "";
	String comM2 = "";
	if (!manager){
		comM1 = "<!--";
		comM2 = "-->";
	}
%>

<table width="750px" border=0>
	<tr>
		<td align="left">
			<h1>管理情報検索</h1>
		</td>
	</tr>
	<tr>
		<td>
			<input type="radio" name="search" value="0" checked>&nbsp;プロジェクトID
		</td>
		<td>
			<input type="text" name="prjId" size="8" maxlength="4" value="<%=prjId%>" placeholder="例：0001">
		</td>
		<td>
		</td>
	</tr>
	<tr>
		<td>
			<input type="radio" name="search" value="1">&nbsp;プロジェクト名
		</td>
		<td>
			<input type="text" name="prjName" size="30" maxlength="100" value="<%=prjName%>">
		</td>
		<td align="left">
		</td>
	</tr>
	<tr>
		<td>
			<input type="radio" name="search" value="2">&nbsp;開始期間：
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
		</td>
		<td>
			
		</td>
	</tr>
	<tr>
		<td>
			<input type="radio" name="search" value="3">&nbsp;終了期間：
		</td>
		<td align="left">
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
		<td>
		</td>
	</tr>
	<tr>
		<td>
			<input type="radio" name="search" value="4">&nbsp;発注者
		</td>
		<td>
			<input type="text" name="orderName" size="30" maxlength="100" value="">
		</td>
		<td align="left">
		</td>
	</tr>
	<tr>
		<td valign="top">
			<input type="radio" name="search" value="5">&nbsp;対象
		</td>
		<td>
			<input type="checkbox" name="targetB" value="1" <%= targetB==1 ? " checked" : "" %> >&nbsp;物件
			<br>
			<input type="checkbox" name="targetS" value="1" <%= targetS==1 ? " checked" : "" %> >&nbsp;測量
			<br>
			<input type="checkbox" name="targetH" value="1" <%= targetH==1 ? " checked" : "" %> >&nbsp;補償説明
			<br>
			<input type="checkbox" name="targetJ" value="1" <%= targetJ==1 ? " checked" : "" %> >&nbsp;事業認定
		</td>
		<td align="left">
		</td>
	</tr>
	<tr>
		<td>
			<input type="radio" name="search" value="6">&nbsp;主任担当者
		</td>
		<td>
			<input type="text" name="chiefName" size="30" maxlength="100" value="<%=chiefName%>">
		</td>
		<td align="left">
		</td>
	</tr>
	<tr>
		<td>
			<input type="radio" name="search" value="7">&nbsp;担当者
		</td>
		<td>
			<input type="text" name="managerName" size="30" maxlength="100" value="<%=managerName%>">
		</td>
		<td align="left">
		</td>
	</tr>
	<tr>
		<td>
		</td>
		<td colspan=2>
			<input type="button" value="　検　索　" onClick="buttonPush('select');" />
			<input type="button" value="　全件検索　" onClick="buttonPush('selectAll');" />
<%=comM1%>
			<input type="button" value="　新規登録　" onClick="buttonPush('newProject');" />
<%=comM2%>
			<button type="button"><html:link action="/kintai/Knt001">　戻　る　</html:link></button>
		</td>
		</td>
	</tr>
</table>

</html:form>
</body>
</html:html>