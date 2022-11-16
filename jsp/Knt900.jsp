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
	// プロジェクト一覧を取得
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
			<h1>経営情報検索</h1>
		</td>
		<td align="left" colspan=1>
			<logic:messagesPresent property="fromTo">
				<font color="red">※終了期間は開始期間より以後を選択してください。</font>
			</logic:messagesPresent>
		</td>
	</tr>
	<tr>
		<td>
			<input type="radio" name="search" value="0" <%= ptn==0 ? " checked" : "" %> >&nbsp;プロジェクトID：
		</td>
		<td>
			<input type="text" name="prjId" size="8" maxlength="4" value="<%=prjId%>" placeholder="例：0001">
		</td>
	</tr>
	<tr>
		<td>
			<input type="radio" name="search" value="1" <%= ptn==1 ? " checked" : "" %>>&nbsp;プロジェクト名：
		</td>
		<td>
			<input type="text" name="prjName" size="30" maxlength="100" value="<%=prjName%>">
		</td>
	</tr>
	<tr>
		<td>
			<input type="radio" name="search" value="2" <%= ptn==2 ? " checked" : "" %>>&nbsp;期間：
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
			～
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
			　出力単位：
		</td>
		<td align="left">
			<input type="radio" name="unit" value="0" <%= unit==0 ? " checked" : "" %> >&nbsp;プロジェクト
			<br>
			<input type="radio" name="unit" value="1" <%= unit==1 ? " checked" : "" %> >&nbsp;社員
		</td>
	</tr>
	<tr>
		<td>
		</td>
		<td colspan=2>
			<input type="button" value="　検　索　" onClick="buttonPush('select');" />
			<input type="button" value="エクスポート" onClick="buttonPush('export_csv');">
			<button type="button"><html:link action="/kintai/Knt001">　戻　る　</html:link></button>
		</td>
	</tr>
</table>

<br>

<%
	String com1 = "プロジェクト名称";
	String com2 = "開始期間";
	String com3 = "終了期間";
	// 社員
	if (unit == 1) {
		com1 = "プロジェクト名称";
		com2 = "ユーザーID";
		com3 = "年/月";
	}
%>

<table width="1000px" border="2">
	<th bgcolor="#bfefdf">プロジェクトID</th>
	<th bgcolor="#bfefdf"><%=com1%></th>
	<th bgcolor="#bfefdf"><%=com2%></th>
	<th bgcolor="#bfefdf"><%=com3%></th>
	<th bgcolor="#bfefdf">申請時間（有給除外）</th>

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
				// 社員単位
				if (unit == 1) {
					sdate = usrSid + "";
					edate = model.getUsrYear() + "/" + model.getUsrMonth();
				}
			%>
			<!-- プロジェクト一覧 -->
			<td align="center">
				<!-- プロジェクトID -->
				<%=pid%>
			</td>
			<td>
				<!-- プロジェクト名称 -->
				<%=pn%>
			</td>
			<td align="center">
				<!-- 開始期間 or ユーザーID -->
				<%=sdate%>
			</td>
			<td align="center">
				<!-- 終了期間 or 年月-->
				<%=edate%>
			</td>
			<td align="right">
				<!-- 時間 -->
				<fmt:formatNumber value="<%=pt%>" pattern="0.00" />
			</td>
		</tr>
	<%}%>
</table>

</html:form>
</body>
</html:html>