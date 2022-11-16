<%@ page pageEncoding="Windows-31J" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<% String key = jp.groupsession.v2.cmn.GSConst.SESSION_KEY; %>
<%@ page import="java.util.*"%>
<%@ page import="jp.groupsession.v2.cmn.GSConst" %>
<%@ page import="jp.groupsession.v2.kintai.Knt002.Knt002Model" %>
<%@ page import="java.math.BigDecimal" %>

<html:html>
<head>
	<title>[GroupSession] Knt002</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<script language="JavaScript" src="../common/js/cmd.js?<%= GSConst.VERSION_PARAM %>"></script>
	<script language="JavaScript" src="../kintai/js/knt002.js"></script>
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
<html:form action="/kintai/Knt002">

<script type="text/javascript">
	function fillZere(element) {
		var val = element.value;
		var num = parseFloat(val);
		num = num.toFixed(2);
		element.value = num;
	}

	<!-- �y�[�W�ǂݍ��ݎ��Ɏ��s -->
	window.onload = function(){
		var total = 0;
		for (i=0;i<10;i++){
			var tv = document.getElementById("time" + String(i)).value;
			total += Number(tv);
		}
		document.getElementById("time_total").value = total;
		var seisu = Math.floor(total);
		var shosu = parseFloat("0."+(String(total)).split(".")[1]);
		document.getElementById("time_remark").value = seisu + " ���� " + shosu * 60 + " ���ł��B";
	}
</script>

<input type="hidden" name="CMD" value="">

<%
	// �\�����郆�[�U�[��
	String usrName = (String)request.getAttribute("usrName");
	// �v���W�F�N�g�ꗗ���擾
	ArrayList<Knt002Model> prjModels = (ArrayList<Knt002Model>) request.getAttribute("prjList");
	// ��Ɠ��e�ꗗ���擾
	ArrayList<Knt002Model> wrkList = (ArrayList<Knt002Model>) request.getAttribute("wrkList");
	// ��Ǝ��т��擾
	ArrayList<Knt002Model> wrks = (ArrayList<Knt002Model>) request.getAttribute("wrks");
	// ���ю���
	double stpTime = (double)request.getAttribute("stpTime");
	String stpTimeF2 = String.format("%.02f",stpTime);
	BigDecimal bd = new BigDecimal(stpTime);
	int hStpTime = bd.intValue();
	BigDecimal mStpTime = bd.remainder(BigDecimal.ONE);
	mStpTime = mStpTime.multiply(new BigDecimal(60));
	mStpTime = mStpTime.setScale(2, BigDecimal.ROUND_HALF_UP);
	String mStpTimeF2 = String.format("%.00f",mStpTime);

	// �ō�����
	String firstInTime = (String)request.getAttribute("firstInTime");
	String lastOutTime = (String)request.getAttribute("lastOutTime");
	String inTime = (String)request.getAttribute("inTime");
	String outTime = (String)request.getAttribute("outTime");
	// ���Ԃ̎擾�i�����m�F�֖߂�p�j
	int selUsrSid = (Integer)request.getAttribute("selUsrSid");
	int startYear = (Integer)request.getAttribute("startYear");
	int startMonth = (Integer)request.getAttribute("startMonth");
	int startDay = (Integer)request.getAttribute("startDay");
	int endYear = (Integer)request.getAttribute("endYear");
	int endMonth = (Integer)request.getAttribute("endMonth");
	int endDay = (Integer)request.getAttribute("endDay");
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
	// �����m�F����̑J�ڏ��
	int smuChk = (Integer)request.getAttribute("smuChk");
	// �L�����ԁi�\���ρj
	double uqTime = (double)request.getAttribute("uqTime");
	String uqTimeF2 = String.format("%.02f",uqTime);
	// 36���ԁi�\���ρj
	double totalTime36 = (double)request.getAttribute("totalTime36");
	String time36F2 = String.format("%.02f",totalTime36);
	double time45 = 45 - totalTime36;
	String time45F2 = String.format("%.02f",time45);
	// ���v����
	double totalTime = stpTime + uqTime;
	String totalTimeF2 = String.format("%.02f",totalTime);
	// ���R
	String reason = (String)request.getAttribute("reason");
	// ��x
	int daiQ = (Integer)request.getAttribute("daiQ");
	// �o�^�m�F��
	int confirm = (Integer)request.getAttribute("confirm");
	String conf = "";
	if (confirm == 1) {
		conf="readonly";
	}
%>

<input type="hidden" name="DISP_USR_SID" value="">
<input type="hidden" name="selUsrSid" value="<%=selUsrSid%>">
<input type="hidden" name="startYear" value="<%=startYear%>">
<input type="hidden" name="startMonth" value="<%=startMonth%>">
<input type="hidden" name="startDay" value="<%=startDay%>">
<input type="hidden" name="endYear" value="<%=endYear%>">
<input type="hidden" name="endMonth" value="<%=endMonth%>">
<input type="hidden" name="endDay" value="<%=endDay%>">

<table width="100%" border="0">
	<tr>
		<td align="left" width="35%" colspan="1">
			<p><b>
			<%=usrName%>
			</b>
			����̎���
			</p>
		</td>
		<td align="right" colspan="2">
		<font size=2>
			�����L���Ԃ͋x�e���ԂƂȂ�܂��B�ړ��͂��̂܂ܓo�^���Ė�肠��܂���B<br>
			�i�`8:30�A12:00�`13:00�A17:30�`18:00�A22:00�`22:30�j
		</font>
		</td>
	</tr>
	<tr>
		<td align="left" colspan="1">
			<%=intY%>�N<%=intM%>��<%=intD%>���i<%=dw%>�j���� <%=hStpTime%>����<%=mStpTimeF2%>���i<%=stpTimeF2%>�j
			<%
				request.setAttribute("yyyy",intY);
				request.setAttribute("mm",intM);
				request.setAttribute("dd",intD);
			%>
			<input type="hidden" name="yyyy" value=<%=intY%>>
			<input type="hidden" name="mm"   value=<%=intM%>>
			<input type="hidden" name="dd"   value=<%=intD%>>
		</td>
		<td align="right" colspan="1">

			<%
				String com1 = "";
				String com2 = "";
				String com3 = "";
				String com4 = "";
				if (smuChk==0){
					com3 = "<!--";
					com4 = "-->";
				} else {
					com1 = "<!--";
					com2 = "-->";
				}
			%>

			<logic:messagesPresent property="registed">
				<font color="red">�o�^���܂����B</font>
			</logic:messagesPresent>
			<logic:messagesPresent property="mismatch">
				<font color="red">�v���W�F�N�g�ƍ�Ɓi�ԐڈȊO�j�͕K�{�ł��B</font>
			</logic:messagesPresent>
			<logic:messagesPresent property="over30">
				<font color="red">�\�����Ԃ����ю��Ԃ�菭�Ȃ��ł��B��ʉ����̔��l�ɗ��R����͂��Ă��������B</font>
			</logic:messagesPresent>
			<logic:messagesPresent property="diffTime">
				<font color="red">�\�����ԂƎ��ю��Ԃ��قȂ�܂��B��낵����΁u�m��v�œo�^���܂��B</font>
			</logic:messagesPresent>
			<logic:messagesPresent property="sChecked">
				<font color="red">���F�ςł��B</font>
			</logic:messagesPresent>
			<logic:messagesPresent property="salesRmk">
				<font color="red">���l�͕K�{�ł��B</font>
			</logic:messagesPresent>
			<logic:messagesPresent property="cantEditTime">
				<font color="red">���F�ς͍�Ǝ��Ԃ̕ύX�͍s���܂���B</font>
			</logic:messagesPresent>

			<!-- ���C����ʂ���̑J�ڎ� -->
			<%=com1%>

			<logic:messagesNotPresent property="diffTime">
			<input type="button" value="�@�o�@�^�@" onClick="buttonPush('registConfirm');" />
			</logic:messagesNotPresent>

			<logic:messagesPresent property="diffTime">
			<input type="button" value="�@�m�@��@" onClick="buttonPush('regist');" />
			</logic:messagesPresent>

			<input type="button" value="�@�߁@��@" onClick="buttonPush('backMain');" />
			<%=com2%>
			<!-- �����m�F��ʂ���̑J�ڎ� -->
			<%=com3%>
			<input type="button" value="�@�߁@��@" onClick="buttonPush('back200');" />
			<%=com4%>
		</td>
	</tr>
</table>

<table border=1 width="100%">
	<tr>
		<th bgcolor="#bfefdf" width="50%">�v���W�F�N�g</th>
		<th bgcolor="#bfefdf" width="20%">��Ɠ��e</th>
		<th bgcolor="#bfefdf" width="10%">��Ǝ���</th>
		<th bgcolor="#bfefdf" width="20%">���@�@�l</th>
	</tr>
	<%for(int i=0;i<10;i++){%>
	<tr>

		<!-- ��Ǝ��ю擾 -->
		<%
			int selPrjSid = -1;
			int selWrkId = -1;
			double selTime = 0;
			String jskRmk = "";
			String selTimeF2 = "";
			if (i < wrks.size()){
				Knt002Model model = (Knt002Model)wrks.get(i);
				selPrjSid = model.getPrjSid();
				selWrkId = model.getWrkId();
				selTime = model.getWrkTime();
				jskRmk = model.getJskRmk();
				selTimeF2 = String.format("%,.02f", selTime);
			}
		%>
	
		<!-- �v���W�F�N�g �ꗗ-->
		<td align="center">
			<p>
			<select name="prjList<%=i%>" style="width:100%;">
			<option value="-1">�I�����Ă�������</option>
			<%for(int j=0; j<prjModels.size(); j++) {%>
				<%Knt002Model model = (Knt002Model)prjModels.get(j);%>
				<%
					int psid = model.getPrjSid();
					String pnm = model.getPrjName();
				%>
				<option value=<%=psid%><%= psid==selPrjSid ? " selected=\"selected\"" : "" %>><%=pnm%></option>
			<%}%>
			</select>
			</p>
			</form>
		</td>
		<!-- ��ƈꗗ -->
		<td align="center">
			<p>
			<select name="wrkList<%=i%>" style="width:100%;">
			<option value="-1">�I�����Ă�������</option>
			<%for(int j=0; j<wrkList.size(); j++) {%>
				<%Knt002Model model = (Knt002Model)wrkList.get(j);%>
				<%
					int wid = model.getWrkId();
					String wnm = model.getWrkName();
				%>
				<option value=<%=wid%><%= wid==selWrkId ? " selected=\"selected\"" : "" %>><%=wnm%></option>
			<%}%>
			</select>
			</p>
			</form>
		</td>
		<!-- ��Ǝ��� -->
		<td align="center">
			<input type="number" name="time<%=i%>" id="time<%=i%>" onblur="fillZere(this)" style="text-align:right;" onChange="sumTime()" min="0.00" max="999.99" step="0.25" value="<%=selTimeF2%>" >
		</td>
		<!-- ���l -->
		<td>
			<input type="text" name="jskRmk<%=i%>" value="<%=jskRmk%>" maxlength="100" style="width:100%;">
		</td>
	</tr>
	<%}%>
	<tr>
		<td align="center" colspan="1">���͍�Ǝ��Ԃ̍��v</td>
		<td align="center" colspan="1">
			<input type="checkbox" name="daiQ" value="1" <%= daiQ==1 ? " checked" : "" %> >&nbsp;��x�i�������j
		</td>
		<td width="10%" align="center">
			<input type="number" name="time_total" id="time_total" size="4" value="0" min="0.00" max="999.99" style="text-align:right;" readonly>
		</td>
		<td width="20%">
			<input name="time_remark" id="time_remark" style="width:100%;text-align:left;" readonly>
		</td>
	</tr>
	<tr>
		<td align="center" colspan="1">���@�l</td>
		<td colspan="3">
			<input type="text" name="reason" id="reason" value="<%=reason%>" style="width:100%;" <%=conf%>>
		</td>
	</tr>
</table>

<!-- ��������̑J�ڂ̂� -->
<%
	String comSmu1 = "<!--";
	String comSmu2 = "-->";
	if (smuChk==1) {
		comSmu1 = "";
		comSmu2 = "";
	}
%>

	<table border="0" width="100%">
		<tr>
			<td width="60%">
				�o�@�Ё@�@�@�@�@<%=firstInTime%>
			</td>
			<td width="30%" align="right">
				36����F���45.00 - �c��<%=time36F2%> = <%=time45F2%> ����
			</td>
		</tr>
		<tr>
			<td>
				���ԓ��i�ދ΁j�@<%=outTime%>
			</td>
			<td align="right">
<%=comSmu1%>
				��Ǝ��ԁF<%=stpTimeF2%> ����
<%=comSmu2%>
			</td>
		</tr>
		<tr>
			<td>
				���ԓ��i�o�΁j�@<%=inTime%>
			</td>
			<td align="right">
<%=comSmu1%>
				�L�����ԁF<%=uqTimeF2%> ����
<%=comSmu2%>
			</td>
		</tr>
		<tr>
			<td>
				�ށ@�Ё@�@�@�@�@<%=lastOutTime%>
			</td>
			<td align="right">
<%=comSmu1%>
				���@�@�v�F<%=totalTimeF2%> ����
<%=comSmu2%>
			</td>
		</tr>
	</table>

</html:form>
</body>
</html:html>