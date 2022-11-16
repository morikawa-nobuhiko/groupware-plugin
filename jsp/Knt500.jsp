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
	<title>[GroupSession] Knt500</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<script language="JavaScript" src="../common/js/cmd.js?<%= GSConst.VERSION_PARAM %>"></script>
	<script language="JavaScript" src="../kintai/js/knt500.js?<%= GSConst.VERSION_PARAM %>"></script>
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

<html:form action="/kintai/Knt500" enctype="multipart/form-data">

<input type="hidden" name="CMD" value="">

<%
%>


<table width="80%" border="0">
	<tr>
		<td align="left" width="20%">
			<h1>データ移行</h1>
		</td>
		<td align="center" width="50%">
			<logic:messagesPresent property="init">
				<font color="blue">インポートファイルは間違えない様に注意して選択してください。</font>
			</logic:messagesPresent>
			<logic:messagesPresent property="registed">
				<font color="black">登録しました。</font>
			</logic:messagesPresent>
			<logic:messagesPresent property="nofile">
				<font color="red">インポートCSVファイルが選択されていません。</font>
			</logic:messagesPresent>
		</td>
		<td align="right" width="20%">
			<button type="button"><html:link action="/kintai/Knt001">　戻　る　</html:link></button>
		</td>
	</tr>
</table>

<table width="80%" border="1">
	<tr>
		<th bgcolor="#CCFFFF" width="20%">分　類</th>
		<th bgcolor="#CCFFFF" width="20%">細分類</th>
		<th bgcolor="#CCFFFF" width="auto">本社用</th>
		<th bgcolor="#CCFFFF" width="auto">支社用</th>
	</tr>
	<tr>
		<td align="center" rowspan="1">
			ファイル選択
		</td>
		<td align="center" rowspan="1">
			インポート用
		</td>
		<td align="center" rowspan="1" colspan="2">
			<html:form action="/kintai/Knt500" method="POST">
				<html:file property="fileUp"/>
			</html:form>
		</td>
	</tr>
	<tr>
		<td align="center" rowspan="3">
			プロジェクト
		</td>
		<td align="center" rowspan="1">
			メイン
		</td>
		<td align="center" rowspan="1">
			<input type="button" value="  エクスポート  " onClick="buttonPush('exportProject');">
		</td>
		<td align="center" rowspan="1">
			<input type="button" value="    インポート    " onClick="buttonPush('importProject');">
		</td>
	</tr>
	<tr>
		<td align="center" rowspan="1">
			担当者
		</td>
		<td align="center" rowspan="1">
			<input type="button" value="  エクスポート  " onClick="buttonPush('exportProjectTanto');">
		</td>
		<td align="center" rowspan="1">
			<input type="button" value="    インポート    " onClick="buttonPush('importProjectTanto');">
		</td>
	</tr>
	<tr>
		<td align="center" rowspan="1">
			作業
		</td>
		<td align="center" rowspan="1">
			<input type="button" value="  エクスポート  " onClick="buttonPush('exportWork');">
		</td>
		<td align="center" rowspan="1">
			<input type="button" value="    インポート    " onClick="buttonPush('importWork');">
		</td>
	</tr>
	<tr>
		<td align="center" rowspan="2">
			勤怠データ
		</td>
		<td align="center" rowspan="1">
			実績
		</td>
		<td align="center" rowspan="1">
			<input type="submit" value="    インポート    " onClick="buttonPush('importStamp');">
			<input type="button" value="  エクスポート  " onClick="buttonPush('exportStamp');">
		</td>
		<td align="center" rowspan="1">
			<input type="button" value="  エクスポート  " onClick="buttonPush('exportStamp');">
		</td>
	</tr>
	<tr>
		<td align="center" rowspan="1">
			申請
		</td>
		<td align="center" rowspan="1">
			<input type="submit" value="    インポート    " onClick="buttonPush('importUserStamp');">
		</td>
		<td align="center" rowspan="1">
			<input type="button" value="  エクスポート  " onClick="buttonPush('exportUserStamp');">
		</td>
	</tr>
</table>

<!--
<table width="100%" border="1">
	<tr>
		<th bgcolor="#CCFFFF" width="10%">分　類</th>
		<th bgcolor="#CCFFFF" width="60%">ファイル選択と処理</th>
		<th bgcolor="#CCFFFF" width="30%">備　考</th>
	</tr>
	<tr>
		<td align="center" rowspan="2">
			本社用
		</td>
		<td rowspan="1">
			<input type="button" value="プロジェクトのエクスポート" onClick="buttonPush('prjExport');">
		</td>
		<td align="left" style="word-wrap:break-word;">
			支社へ渡すプロジェクトデータをエクスポートします。
		</td>
	</tr>
	<tr>
		<td rowspan="1">
			<html:form action="/kintai/Knt500" method="POST">
				<html:file property="fileUp"/>
			</html:form>
			<input type="submit" value="仙台支社からインポート" onClick="buttonPush('importSendai');">
			<input type="submit" value="並木からインポート" onClick="buttonPush('importNamiki');">
		</td>
		<td align="left" style="word-wrap:break-word;">
			支社から受け取った勤怠データを本社へインポートします。
		</td>
	</tr>
	<tr>
		<td align="center" rowspan="2">
			支社・出向者用
		</td>
		<td align="left">
			<input type="button" value="プロジェクトインポート" onClick="buttonPush('importProject');">
		</td>
		<td align="left" style="word-wrap:break-word;">
			本社からのプロジェクトをインポートします。
		</td>
	</tr>
	<tr>
		<td align="left">
			<input type="button" value="勤怠エクスポート" onClick="buttonPush('export');">
		</td>
		<td align="left" style="word-wrap:break-word;">
			本社へ渡す勤怠データをエクスポートします。
		</td>
	</tr>
</table>
-->

</html:form>
</body>
</html:html>