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

	<!-- ページ読み込み時に実行 -->
	window.onload = function(){
		var total = 0;
		for (i=0;i<10;i++){
			var tv = document.getElementById("time" + String(i)).value;
			total += Number(tv);
		}
		document.getElementById("time_total").value = total;
		var seisu = Math.floor(total);
		var shosu = parseFloat("0."+(String(total)).split(".")[1]);
		document.getElementById("time_remark").value = seisu + " 時間 " + shosu * 60 + " 分です。";
	}
</script>

<input type="hidden" name="CMD" value="">

<%
	// 表示するユーザー名
	String usrName = (String)request.getAttribute("usrName");
	// プロジェクト一覧を取得
	ArrayList<Knt002Model> prjModels = (ArrayList<Knt002Model>) request.getAttribute("prjList");
	// 作業内容一覧を取得
	ArrayList<Knt002Model> wrkList = (ArrayList<Knt002Model>) request.getAttribute("wrkList");
	// 作業実績を取得
	ArrayList<Knt002Model> wrks = (ArrayList<Knt002Model>) request.getAttribute("wrks");
	// 実績時間
	double stpTime = (double)request.getAttribute("stpTime");
	String stpTimeF2 = String.format("%.02f",stpTime);
	BigDecimal bd = new BigDecimal(stpTime);
	int hStpTime = bd.intValue();
	BigDecimal mStpTime = bd.remainder(BigDecimal.ONE);
	mStpTime = mStpTime.multiply(new BigDecimal(60));
	mStpTime = mStpTime.setScale(2, BigDecimal.ROUND_HALF_UP);
	String mStpTimeF2 = String.format("%.00f",mStpTime);

	// 打刻時間
	String firstInTime = (String)request.getAttribute("firstInTime");
	String lastOutTime = (String)request.getAttribute("lastOutTime");
	String inTime = (String)request.getAttribute("inTime");
	String outTime = (String)request.getAttribute("outTime");
	// 期間の取得（総務確認へ戻る用）
	int selUsrSid = (Integer)request.getAttribute("selUsrSid");
	int startYear = (Integer)request.getAttribute("startYear");
	int startMonth = (Integer)request.getAttribute("startMonth");
	int startDay = (Integer)request.getAttribute("startDay");
	int endYear = (Integer)request.getAttribute("endYear");
	int endMonth = (Integer)request.getAttribute("endMonth");
	int endDay = (Integer)request.getAttribute("endDay");
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
	// 総務確認からの遷移状態
	int smuChk = (Integer)request.getAttribute("smuChk");
	// 有給時間（申請済）
	double uqTime = (double)request.getAttribute("uqTime");
	String uqTimeF2 = String.format("%.02f",uqTime);
	// 36時間（申請済）
	double totalTime36 = (double)request.getAttribute("totalTime36");
	String time36F2 = String.format("%.02f",totalTime36);
	double time45 = 45 - totalTime36;
	String time45F2 = String.format("%.02f",time45);
	// 合計時間
	double totalTime = stpTime + uqTime;
	String totalTimeF2 = String.format("%.02f",totalTime);
	// 事由
	String reason = (String)request.getAttribute("reason");
	// 代休
	int daiQ = (Integer)request.getAttribute("daiQ");
	// 登録確認時
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
			さんの実績
			</p>
		</td>
		<td align="right" colspan="2">
		<font size=2>
			※下記時間は休憩時間となります。移動はそのまま登録して問題ありません。<br>
			（～8:30、12:00～13:00、17:30～18:00、22:00～22:30）
		</font>
		</td>
	</tr>
	<tr>
		<td align="left" colspan="1">
			<%=intY%>年<%=intM%>月<%=intD%>日（<%=dw%>）実績 <%=hStpTime%>時間<%=mStpTimeF2%>分（<%=stpTimeF2%>）
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
				<font color="red">登録しました。</font>
			</logic:messagesPresent>
			<logic:messagesPresent property="mismatch">
				<font color="red">プロジェクトと作業（間接以外）は必須です。</font>
			</logic:messagesPresent>
			<logic:messagesPresent property="over30">
				<font color="red">申請時間が実績時間より少ないです。画面下部の備考に理由を入力してください。</font>
			</logic:messagesPresent>
			<logic:messagesPresent property="diffTime">
				<font color="red">申請時間と実績時間が異なります。よろしければ「確定」で登録します。</font>
			</logic:messagesPresent>
			<logic:messagesPresent property="sChecked">
				<font color="red">承認済です。</font>
			</logic:messagesPresent>
			<logic:messagesPresent property="salesRmk">
				<font color="red">備考は必須です。</font>
			</logic:messagesPresent>
			<logic:messagesPresent property="cantEditTime">
				<font color="red">承認済は作業時間の変更は行えません。</font>
			</logic:messagesPresent>

			<!-- メイン画面からの遷移時 -->
			<%=com1%>

			<logic:messagesNotPresent property="diffTime">
			<input type="button" value="　登　録　" onClick="buttonPush('registConfirm');" />
			</logic:messagesNotPresent>

			<logic:messagesPresent property="diffTime">
			<input type="button" value="　確　定　" onClick="buttonPush('regist');" />
			</logic:messagesPresent>

			<input type="button" value="　戻　る　" onClick="buttonPush('backMain');" />
			<%=com2%>
			<!-- 総務確認画面からの遷移時 -->
			<%=com3%>
			<input type="button" value="　戻　る　" onClick="buttonPush('back200');" />
			<%=com4%>
		</td>
	</tr>
</table>

<table border=1 width="100%">
	<tr>
		<th bgcolor="#bfefdf" width="50%">プロジェクト</th>
		<th bgcolor="#bfefdf" width="20%">作業内容</th>
		<th bgcolor="#bfefdf" width="10%">作業時間</th>
		<th bgcolor="#bfefdf" width="20%">備　　考</th>
	</tr>
	<%for(int i=0;i<10;i++){%>
	<tr>

		<!-- 作業実績取得 -->
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
	
		<!-- プロジェクト 一覧-->
		<td align="center">
			<p>
			<select name="prjList<%=i%>" style="width:100%;">
			<option value="-1">選択してください</option>
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
		<!-- 作業一覧 -->
		<td align="center">
			<p>
			<select name="wrkList<%=i%>" style="width:100%;">
			<option value="-1">選択してください</option>
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
		<!-- 作業時間 -->
		<td align="center">
			<input type="number" name="time<%=i%>" id="time<%=i%>" onblur="fillZere(this)" style="text-align:right;" onChange="sumTime()" min="0.00" max="999.99" step="0.25" value="<%=selTimeF2%>" >
		</td>
		<!-- 備考 -->
		<td>
			<input type="text" name="jskRmk<%=i%>" value="<%=jskRmk%>" maxlength="100" style="width:100%;">
		</td>
	</tr>
	<%}%>
	<tr>
		<td align="center" colspan="1">入力作業時間の合計</td>
		<td align="center" colspan="1">
			<input type="checkbox" name="daiQ" value="1" <%= daiQ==1 ? " checked" : "" %> >&nbsp;代休（仮実装）
		</td>
		<td width="10%" align="center">
			<input type="number" name="time_total" id="time_total" size="4" value="0" min="0.00" max="999.99" style="text-align:right;" readonly>
		</td>
		<td width="20%">
			<input name="time_remark" id="time_remark" style="width:100%;text-align:left;" readonly>
		</td>
	</tr>
	<tr>
		<td align="center" colspan="1">備　考</td>
		<td colspan="3">
			<input type="text" name="reason" id="reason" value="<%=reason%>" style="width:100%;" <%=conf%>>
		</td>
	</tr>
</table>

<!-- 総務からの遷移のみ -->
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
				出　社　　　　　<%=firstInTime%>
			</td>
			<td width="30%" align="right">
				36協定：上限45.00 - 残業<%=time36F2%> = <%=time45F2%> 時間
			</td>
		</tr>
		<tr>
			<td>
				時間内（退勤）　<%=outTime%>
			</td>
			<td align="right">
<%=comSmu1%>
				作業時間：<%=stpTimeF2%> 時間
<%=comSmu2%>
			</td>
		</tr>
		<tr>
			<td>
				時間内（出勤）　<%=inTime%>
			</td>
			<td align="right">
<%=comSmu1%>
				有給時間：<%=uqTimeF2%> 時間
<%=comSmu2%>
			</td>
		</tr>
		<tr>
			<td>
				退　社　　　　　<%=lastOutTime%>
			</td>
			<td align="right">
<%=comSmu1%>
				合　　計：<%=totalTimeF2%> 時間
<%=comSmu2%>
			</td>
		</tr>
	</table>

</html:form>
</body>
</html:html>