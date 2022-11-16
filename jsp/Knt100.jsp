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
<%@ page import="jp.groupsession.v2.kintai.Knt100.Knt100GroupModel" %>
<%@ page import="jp.groupsession.v2.kintai.Knt100.Knt100UserModel" %>

<html:html>
<head>
	<title>[GroupSession] Knt100</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<script language="JavaScript" src="../common/js/cmd.js?<%= GSConst.VERSION_PARAM %>"></script>
	<script language="JavaScript" src="../kintai/js/knt100.js"></script>
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
<html:form action="/kintai/Knt100">

<input type="hidden" name="CMD" value="">

<%
	// グループ一覧を取得
	ArrayList<Knt100GroupModel> groups = (ArrayList<Knt100GroupModel>) request.getAttribute("grpList");
	// グループのユーザー一覧を取得
	ArrayList<Knt100UserModel> grpUsers = (ArrayList<Knt100UserModel>) request.getAttribute("grpUsers");
	// 選択グループ
	int grpIndex = (int)request.getAttribute("grpIndex");
	// 日付の取得
	int intY = (Integer)request.getAttribute("yyyy");
	int intM = (Integer)request.getAttribute("mm");
	int intD = (Integer)request.getAttribute("dd");
	// DAY_OF_WEEK ： 日曜が1で始まる1～7 の数字
	Calendar cal = Calendar.getInstance();
	cal.set(intY, intM - 1 ,intD);
	int k = cal.get(Calendar.DAY_OF_WEEK);
	String dw = "";
	switch (k){
	case 1:
		dw = "日";
		break;
	case 2:
		dw = "月";
		break;
	case 3:
		dw = "火";
		break;
	case 4:
		dw = "水";
		break;
	case 5:
		dw = "木";
		break;
	case 6:
		dw = "金";
		break;
	case 7:
		dw = "土";
		break;
	}
%>

<table width="750px" border="0">
	<tr>
		<td align="left">
			<h1>実績承認</h1>
		</td>
		<td>
<!--
			<select name="group" style="valign=center;">
			<option value="-1">選択してください</option>
			<option value="0">物件部門</option>
			<option value="1">補償調査部門</option>
			<option value="2">測量部門</option>
			<option value="3">営業</option>
			<option value="4">経理</option>
			<option value="5">鑑定</option>
			</select>
-->
			<select name="grpId" onchange="changeGroupCombo()";>
				<option value="-1">選択してください</option>
				<%for(int i=0; i<groups.size(); i++) {%>
					<%Knt100GroupModel grp = (Knt100GroupModel)groups.get(i);%>
					<%
						int grpId = grp.getGrpId();
						String grpName = grp.getGrpName();
						String strChecked = "";
					%>
					<option value=<%=grpId%> <%= i==grpIndex ? " selected=\"selected\"" : "" %> ><%=grpName%></option>
				<%}%>
			</select>

			<input type="button" value="　登　録　" onClick="buttonPush('regist');">
			<input type="button" value="　戻　る　" onClick="buttonPush('backMain');" />
			<input type="button" value=" 一括承認 " onclick="allcheck(true);">
			<input type="button" value=" 一括解除 " onclick="allcheck(false);">
		</td>
	</tr>
	<tr>
		<td colspan="1">
			<%=intY%>年<%=intM%>月<%=intD%>日（<%=dw%>）
			<%
				request.setAttribute("yyyy",intY);
				request.setAttribute("mm",intM);
				request.setAttribute("dd",intD);
			%>
			<input type="hidden" name="yyyy" value=<%=intY%>>
			<input type="hidden" name="mm"   value=<%=intM%>>
			<input type="hidden" name="dd"   value=<%=intD%>>
		</td>
		<td align="left" colspan="1"">
			<logic:messagesPresent property="registed">
				<p><font>承認を登録しました。</font></p>
			</logic:messagesPresent>
		</td>
	</tr>
</table>

<!--一覧-->
<table width="750px" border="1">

	<tr>
		<th bgcolor="#bfefdf">氏名</th>
		<th bgcolor="#bfefdf">申請時間</th>
		<th bgcolor="#bfefdf">承認</th>
		<th bgcolor="#bfefdf" colspan="2">勤怠実績</th>
	</tr>

	<%for(int i=0; i<grpUsers.size(); i++) {%>
		<%Knt100UserModel model = (Knt100UserModel)grpUsers.get(i);%>
		<tr>
			<td>
				<%=model.getUsrName()%>
			</td>
			<td align="center">
				<%
					double usrTime = model.getUsrTime();
					String usrTimeF2 = String.format("%.02f", usrTime);
				%>
				<%=usrTimeF2%>
			</td>
			<td align="center">
				<input type="checkbox" name="app<%=model.getUsrSid()%>" value=<%=model.getUsrSid()%> <%= model.getUsrSyn() ? " checked=\"checked\"" : "" %> >
			</td>
			<td align="center">
				<%=model.getStpJsk()%>
			</td>
			<td align="center">
				<%
					double stpTime = model.getStpTime();
					String stpTimeF2 = String.format("%.02f", stpTime);
				%>
				実働 <%=stpTimeF2%> 時間
			</td>
		</tr>
	<%}%>

</table>

</html:form>
</body>
</html:html>