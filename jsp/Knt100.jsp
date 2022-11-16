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
	// �O���[�v�ꗗ���擾
	ArrayList<Knt100GroupModel> groups = (ArrayList<Knt100GroupModel>) request.getAttribute("grpList");
	// �O���[�v�̃��[�U�[�ꗗ���擾
	ArrayList<Knt100UserModel> grpUsers = (ArrayList<Knt100UserModel>) request.getAttribute("grpUsers");
	// �I���O���[�v
	int grpIndex = (int)request.getAttribute("grpIndex");
	// ���t�̎擾
	int intY = (Integer)request.getAttribute("yyyy");
	int intM = (Integer)request.getAttribute("mm");
	int intD = (Integer)request.getAttribute("dd");
	// DAY_OF_WEEK �F ���j��1�Ŏn�܂�1�`7 �̐���
	Calendar cal = Calendar.getInstance();
	cal.set(intY, intM - 1 ,intD);
	int k = cal.get(Calendar.DAY_OF_WEEK);
	String dw = "";
	switch (k){
	case 1:
		dw = "��";
		break;
	case 2:
		dw = "��";
		break;
	case 3:
		dw = "��";
		break;
	case 4:
		dw = "��";
		break;
	case 5:
		dw = "��";
		break;
	case 6:
		dw = "��";
		break;
	case 7:
		dw = "�y";
		break;
	}
%>

<table width="750px" border="0">
	<tr>
		<td align="left">
			<h1>���я��F</h1>
		</td>
		<td>
<!--
			<select name="group" style="valign=center;">
			<option value="-1">�I�����Ă�������</option>
			<option value="0">��������</option>
			<option value="1">�⏞��������</option>
			<option value="2">���ʕ���</option>
			<option value="3">�c��</option>
			<option value="4">�o��</option>
			<option value="5">�Ӓ�</option>
			</select>
-->
			<select name="grpId" onchange="changeGroupCombo()";>
				<option value="-1">�I�����Ă�������</option>
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

			<input type="button" value="�@�o�@�^�@" onClick="buttonPush('regist');">
			<input type="button" value="�@�߁@��@" onClick="buttonPush('backMain');" />
			<input type="button" value=" �ꊇ���F " onclick="allcheck(true);">
			<input type="button" value=" �ꊇ���� " onclick="allcheck(false);">
		</td>
	</tr>
	<tr>
		<td colspan="1">
			<%=intY%>�N<%=intM%>��<%=intD%>���i<%=dw%>�j
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
				<p><font>���F��o�^���܂����B</font></p>
			</logic:messagesPresent>
		</td>
	</tr>
</table>

<!--�ꗗ-->
<table width="750px" border="1">

	<tr>
		<th bgcolor="#bfefdf">����</th>
		<th bgcolor="#bfefdf">�\������</th>
		<th bgcolor="#bfefdf">���F</th>
		<th bgcolor="#bfefdf" colspan="2">�Αӎ���</th>
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
				���� <%=stpTimeF2%> ����
			</td>
		</tr>
	<%}%>

</table>

</html:form>
</body>
</html:html>