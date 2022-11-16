<%@ page pageEncoding="Windows-31J" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<% String key = jp.groupsession.v2.cmn.GSConst.SESSION_KEY; %>
<%@ page import="java.util.*"%>
<%@ page import="jp.groupsession.v2.cmn.GSConst" %>
<%@ page import="jp.groupsession.v2.kintai.Knt001.Knt001Model" %>
<%@ page import="jp.groupsession.v2.cmn.cmn200.Cmn200HolidayMdl" %>

<html:html>
<head>
	<title>[GroupSession] Knt001</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<script language="JavaScript" src="../common/js/cmd.js?<%= GSConst.VERSION_PARAM %>"></script>
	<link rel=stylesheet type='text/css' href='../common/css/default.css'>
	<link rel=stylesheet type='text/css' href='../kintai/css/knt001.css'>
	<style type="text/css">
	<!--
		*{padding:5px; margin:0px;}
		br{line-height:1em;}
	-->
	.holiday2   { color: black; background: #e4d2d8;}
	.saturday2  { color: black; background: #e4d2d8;}
	</style>
</head>

<%
	// �\��������t�̎擾
	Calendar cal=Calendar.getInstance();
	// �{���̔N����
	int nowY = cal.get(Calendar.YEAR);
	int nowM = cal.get(Calendar.MONTH)+1;
	int nowD = cal.get(Calendar.DATE);
	// �\��������t�̎擾
	int dispY = (int)request.getAttribute("dispYear");
	int dispM = (int)request.getAttribute("dispMonth");
	int toDay = cal.get(Calendar.DATE);
	// Calendar.MONTH��1����0
	cal.set(dispY, dispM - 1, 1);
	// �j��
	int k=cal.get(Calendar.DAY_OF_WEEK)-1;
	// �\���iKEY=�� VALUE=���ю��ԁj
	HashMap<Integer,Double> uiMap = (HashMap<Integer,Double>)request.getAttribute("uiMap");
	// �ō��iKEY=�� VALUE=���ю��ԁj
	HashMap<Integer,Double> stpMap = (HashMap<Integer,Double>)request.getAttribute("stpMap");
	// �ō����v����
	double stpTotalTime = (Double)request.getAttribute("stpTotalTime");
	String stpTotalTimeF2 = String.format("%.02f",stpTotalTime);
	// �\�����v����
	double reqTotalTime = (Double)request.getAttribute("reqTotalTime");
	String reqTotalTimeF2 = String.format("%.02f",reqTotalTime);
	// �L�����v����
	double uqt = (Double)request.getAttribute("uqt");
	String uqtF2 = String.format("%.02f",uqt);
	// �Ǘ��҃O���[�v
	boolean manager = (Boolean)request.getAttribute("manager");
	// ���F�O���[�v
	boolean authorizer = (Boolean)request.getAttribute("authorizer");
	// �����O���[�v
	boolean soumu = (Boolean)request.getAttribute("soumu");
	// �o�^��̓��t
	int regY = (int)request.getAttribute("yyyy");
	int regM = (int)request.getAttribute("mm");
	int regD = (int)request.getAttribute("dd");
	// �j��
	ArrayList<String> holiday = (ArrayList<String>)request.getAttribute("holiday");
	// ����
	String comM1 = "";
	String comM2 = "";
	if (!manager){
		comM1 = "<!--";
		comM2 = "-->";
	}
	String comS1 = "";
	String comS2 = "";
	if (!manager && !soumu){
		comS1 = "<!--";
		comS2 = "-->";
	}
	if (manager){
		comS1 = "";
		comS2 = "";
		comM1 = "";
		comM2 = "";
	}
%>

<body class="body_03">
<html:form action="/kintai/Knt001">

<input type="hidden" name="CMD" value="">
<table id="table-knt001" border="0">
	<tr>
		<td align="left" colspan="1">
			<p><b>
			<!-- ���[�U���F�� -->
			<bean:write name="<%= key %>" scope="session" property="usisei" />
			<!-- ���[�U���F�� -->
			<bean:write name="<%= key %>" scope="session" property="usimei" />
			</b>
			����̎���
			</p>
		</td>
		<td align="right">
			�Αӎ��� <%=stpTotalTimeF2%> ����
			<logic:messagesPresent property="registed">
				<p><font>��<%=regY%>/<%=regM%>/<%=regD%> �̎��т�o�^���܂����B</font></p>
			</logic:messagesPresent>
		</td>
	</tr>
	<tr>
		<td align="left" colspan=1>
			<!--�\�����̔N����\��-->
			<input type="button" value=" �� " onClick="buttonPush('MoveMonthBefore,<%=dispY%>,<%=dispM%>');">
			<span class="title"><%=dispY%>�N<%=dispM%>��</span>
			<input type="button" value=" �� " onClick="buttonPush('MoveMonthAfter,<%=dispY%>,<%=dispM%>');">
<!--		<input type="button" value="���[���o�b�N" onClick="buttonPush('debug_rollback');">-->
		</td>
		<td align="right">
			<%=comS1%>
				<button type="button"><html:link action="/kintai/Knt200">����</html:link></button>
				<button type="button"><html:link action="/kintai/Knt500">�f�[�^�ڍs</html:link></button>
			<%=comS2%>				
			<%=comM1%>
				<button type="button"><html:link action="/kintai/Knt400">��ƊǗ�</html:link></button>
			<%=comM2%>
				<button type="button"><html:link action="/kintai/Knt300">�Ǘ���񌟍�</html:link></button>
			<%=comM1%>
				<button type="button"><html:link action="/kintai/Knt302">�Ǘ����o�^</html:link></button>
				<button type="button"><html:link action="/kintai/Knt900">�o�c��񌟍�</html:link></button>
			<%=comM2%>
		</td>
<!--
	</tr>
		<td>
			<div class="hamburger-menu">
				<input type="checkbox" id="menu-btn-check">
				<label for="menu-btn-check" class="menu-btn"><span></span></label>
				<div class="menu-content">
					<ul>
						<li>
							<html:link action="/kintai/Knt200">����</html:link>
						</li>
						<li>
							<html:link action="/kintai/Knt500">�f�[�^�ڍs</html:link>
						</li>
						<li>
							<html:link action="/kintai/Knt400">��ƊǗ�</html:link>
						</li>
						<li>
							<html:link action="/kintai/Knt300">�Ǘ���񌟍�</html:link>
						</li>
						<li>
							<html:link action="/kintai/Knt302">�Ǘ����o�^</html:link>
						</li>
						<li>
							<html:link action="/kintai/Knt900">�o�c��񌟍�</html:link>
						</li>
					</ul>
				</div>
			</div>
		</td>
-->
</table>

<table id="table-knt001" border="2px" width="70%">
	<tr bgcolor=":#CCFFFF">
		<!--TH : Table Header-->
		<th class="holiday"  width="10%">��</th>
		<th class="weekday"  width="10%" >��</th>
		<th class="weekday"  width="10%" >��</th>
		<th class="weekday"  width="10%" >��</th>
		<th class="weekday"  width="10%" >��</th>
		<th class="weekday"  width="10%" >��</th>
		<th class="saturday" width="10%">�y</th>
	</tr>
	<%int d=1;
		//�����̊Ԃ̓��[�v
		while(cal.get(Calendar.MONTH)==dispM-1){%>
	<tr height="100px">
		<!--�K�����j�`�y�j�̈�T�ԕ��͏��������郋�[�v-->
		<%for(int j=0;j<7;j++){%>
			<%
			//�e�j���̃N���X����
			if(j==0){%>
				<td class="holiday2" align="center" valign="top">
			<%}else if(j==6){%>
				<td class="saturday2" align="center" valign="top">
			<%}else{%>

				<%
					String ymd = String.valueOf(dispY) + String.format("%02d",dispM) + String.format("%02d", d);
					boolean holi = holiday.contains(ymd);
					String clsHol = "weekday";
					if (holi){ 
						clsHol = "holiday2";
					}
				%>
<!--			<td class="weekday" align="center" valign="top">-->
				<td class="<%=clsHol%>" align="center" valign="top">

			<%}//���I�����e�j���̃N���X����%>

			<%
			//����k��0�i�����j�j�łȂ����k����1������
			//�yk�z�F ���j��0�A�y�j��6
			//������	�͂��߂�1���ڂ��������ޗj���̊m��p	������
			//������	���O�����̋�g	������
			if(k!=0){
				k--;
			//����k��0�i�����j�j�Ȃ�A���������̊ԂȂ�
			//������	�����s��v�̏ꍇ�͋󔒏o��	������
			}else if (cal.get(Calendar.MONTH)==dispM-1){%>

				<!--�ϐ�d��1���₷�O�̓��t�ێ�-->
				<%int md=d;%>
				<!--�ϐ�d��1���₵�ē��t�\��-->
				<%=d++%><br>

				<!--�N���b�N�������Ɏ���JSP�֔N������HashMap��n��-->
				<%
					Map ymdMap = new HashMap();
					ymdMap.put("dd",md);
					ymdMap.put("mm",dispM);
					ymdMap.put("yyyy",dispY);
					request.setAttribute("ymdMap",ymdMap);
				%>
				<!--HashMap�����t�̎��ю��Ԃ��擾-->

				<%
					// �{�����O���̂ݕҏW�\�ɂ���
					String com1 = "";
					String com2 = "";
					String com3 = "";
					String com4 = "";
					if (nowY < dispY) {
						com1 = "<!--";
						com2 = "--><br>";
					} else if (dispY < nowY || dispM < nowM) {
					} else if (nowY < dispY || nowM < dispM) {
						com1 = "<!--";
						com2 = "--><br>";
					} else if (toDay < md) {
						com1 = "<!--";
						com2 = "--><br>";
					} else {
						if (!authorizer){
							com3 = "<!--";
							com4 = "-->";
						}
					}
					if (com1.equals("")){
						if (!authorizer){
							com3 = "<!--";
							com4 = "-->";
						}
					}
				%>

				<!-- ���� -->
				<%
					String realTime = String.format("%.02f",stpMap.get(md)) ;
					String reqTime = String.format("%.02f",uiMap.get(md)) ;
				%>

				<%=com1%>

				<input type="button" value="�\�� <%=reqTime%>" style="font-size:110%;border:none;background-color:transparent;color:blue;" onClick="buttonPush('detail001,<%=dispY%>,<%=dispM%>,<%=md%>');">
				<br>
				<font style="font-size:100%;">���� <%=realTime%></font>

				<%=com3%>
				<input type="button" value="���F" style="font-size:110%;border:none;background-color:transparent;color:blue;" onClick="buttonPush('approval,<%=dispY%>,<%=dispM%>,<%=md%>');">
				<%=com4%>
				<%=com2%>

				<!--1����̓��t�ɂ���-->
				<%cal.add(Calendar.DATE,1);%>
			<%}%>				<!--/***if(k!=0)-->
			</td>				<!--/***td(holiday,saturday,weekday)-->
		<%}%>					<!--/***for-->
	</tr>						<!--/***tr(�O��-->
	<%}%>						<!--/***while-->
</table>

<table id="table-knt001" width="100px" border="0">
	<tr>
		<td align="right" width="75%">
			�\�����v�i�L���j:
		</td>
		<td align="right">
			<%=reqTotalTimeF2%>�i<%=uqtF2%>�j ����
		</td>
	</tr>
</table>

</html:form>
</body>
</html:html>