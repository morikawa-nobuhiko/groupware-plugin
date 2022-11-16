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
<%@ page import="jp.groupsession.v2.kintai.Knt301.Knt301Model" %>

<html:html>
<head>
	<title>[GroupSession] Knt301</title>
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
<html:form action="/kintai/Knt301">

<input type="hidden" name="CMD" value="">

<%
	// プロジェクト一覧を取得
	ArrayList<Knt301Model> prjModels = (ArrayList<Knt301Model>) request.getAttribute("prjList");
	// 管理者グループ
	boolean manager = (Boolean)request.getAttribute("manager");
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

<table border=0 width="750px">
	<tr>
		<td align="left" width="150px">
			<h1>管理情報一覧</h1>
		</td>
		<td colspan=1>
			<%=comM1%>
			<button type="button"><html:link action="/kintai/Knt302">　新規登録　</html:link></button>
			<%=comM2%>
			<button type="button"><html:link action="/kintai/Knt300">　再検索　</html:link></button>
			<button type="button"><html:link action="/kintai/Knt001">　戻　る　</html:link></button>
		</td>
	</tr>
</table>

<br>

<table border="2" width="100%">

	<tr>
		<th bgcolor="#bfefdf">ID</th>
		<th bgcolor="#bfefdf">名称</th>
		<th bgcolor="#bfefdf" width="200">発注者</th>
		<th bgcolor="#bfefdf">対象</th>
		<th bgcolor="#bfefdf">開始期間</th>
		<th bgcolor="#bfefdf">終了期間</th>
		<th bgcolor="#bfefdf">主任担当者</th>
		<th bgcolor="#bfefdf">担当者</th>
		<th bgcolor="#bfefdf" width="300">備考</th>
<%=comM1%>
		<th bgcolor="#bfefdf">編集</th>
<%=comM2%>
	</tr>
	<%for(int i=0; i<prjModels.size(); i++) {%>
		<tr>
			<%Knt301Model model = (Knt301Model)prjModels.get(i);%>
			<%
				int psid = model.getPrjSid();
				String pid = model.getPrjId();
				String pn = model.getPrjName();
				int sy = model.getStartYear();
				int sm = model.getStartMonth();
				int sd = model.getStartDay();
				int ey = model.getEndYear();
				int em = model.getEndMonth();
				int ed = model.getEndDay();
				String sdate = sy + "/" + sm + "/" + sd;
				String edate = ey + "/" + em + "/" + ed;
				String orderName = model.getOrderName();
				String chiefName = model.getChiefName();
				String managerName = model.getManagerName();
				String remark = model.getRemark();
				int targetB = model.getTargetB();
				int targetS = model.getTargetS();
				int targetH = model.getTargetH();
				int targetJ = model.getTargetJ();
				String target="";
				if (targetB==1){target=target + "物件,";}
				if (targetS==1){target=target + "測量,";}
				if (targetH==1){target=target + "補償説明,";}
				if (targetJ==1){target=target + "事業認定";}
			%>
			<!-- プロジェクト一覧 -->
			<td align="center">
				<%=pid%>
			</td>
			<td>
				<%=pn%>
			</td>
			<td align="center">
				<%=orderName%>
			</td>
			<td>
				<%=target%>
			</td>
			<td align="center">
				<%=sdate%>
			</td>
			<td align="center">
				<%=edate%>
			</td>
			<td align="center">
				<%=chiefName%>
			</td>
			<td align="center">
				<%=managerName%>
			</td>
			<td>
				<%=remark%>
			</td>
			<%=comM1%>
			<td align="center">
				<input type="button" value="編集" onClick="buttonPush('edit<%=psid%>');">
			</td>
			<%=comM2%>
		</tr>
	<%}%>
</table>

<h1>
<br>
</html:form>
</body>
</html:html>