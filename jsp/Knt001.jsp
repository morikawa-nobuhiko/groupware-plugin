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
	// 表示する日付の取得
	Calendar cal=Calendar.getInstance();
	// 本日の年月日
	int nowY = cal.get(Calendar.YEAR);
	int nowM = cal.get(Calendar.MONTH)+1;
	int nowD = cal.get(Calendar.DATE);
	// 表示する日付の取得
	int dispY = (int)request.getAttribute("dispYear");
	int dispM = (int)request.getAttribute("dispMonth");
	int toDay = cal.get(Calendar.DATE);
	// Calendar.MONTHは1月＝0
	cal.set(dispY, dispM - 1, 1);
	// 曜日
	int k=cal.get(Calendar.DAY_OF_WEEK)-1;
	// 申請（KEY=日 VALUE=実績時間）
	HashMap<Integer,Double> uiMap = (HashMap<Integer,Double>)request.getAttribute("uiMap");
	// 打刻（KEY=日 VALUE=実績時間）
	HashMap<Integer,Double> stpMap = (HashMap<Integer,Double>)request.getAttribute("stpMap");
	// 打刻合計時間
	double stpTotalTime = (Double)request.getAttribute("stpTotalTime");
	String stpTotalTimeF2 = String.format("%.02f",stpTotalTime);
	// 申請合計時間
	double reqTotalTime = (Double)request.getAttribute("reqTotalTime");
	String reqTotalTimeF2 = String.format("%.02f",reqTotalTime);
	// 有給合計時間
	double uqt = (Double)request.getAttribute("uqt");
	String uqtF2 = String.format("%.02f",uqt);
	// 管理者グループ
	boolean manager = (Boolean)request.getAttribute("manager");
	// 承認グループ
	boolean authorizer = (Boolean)request.getAttribute("authorizer");
	// 総務グループ
	boolean soumu = (Boolean)request.getAttribute("soumu");
	// 登録後の日付
	int regY = (int)request.getAttribute("yyyy");
	int regM = (int)request.getAttribute("mm");
	int regD = (int)request.getAttribute("dd");
	// 祝日
	ArrayList<String> holiday = (ArrayList<String>)request.getAttribute("holiday");
	// 権限
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
			<!-- ユーザ名：姓 -->
			<bean:write name="<%= key %>" scope="session" property="usisei" />
			<!-- ユーザ名：名 -->
			<bean:write name="<%= key %>" scope="session" property="usimei" />
			</b>
			さんの実績
			</p>
		</td>
		<td align="right">
			勤怠実績 <%=stpTotalTimeF2%> 時間
			<logic:messagesPresent property="registed">
				<p><font>※<%=regY%>/<%=regM%>/<%=regD%> の実績を登録しました。</font></p>
			</logic:messagesPresent>
		</td>
	</tr>
	<tr>
		<td align="left" colspan=1>
			<!--表示中の年月を表示-->
			<input type="button" value=" ＜ " onClick="buttonPush('MoveMonthBefore,<%=dispY%>,<%=dispM%>');">
			<span class="title"><%=dispY%>年<%=dispM%>月</span>
			<input type="button" value=" ＞ " onClick="buttonPush('MoveMonthAfter,<%=dispY%>,<%=dispM%>');">
<!--		<input type="button" value="ロールバック" onClick="buttonPush('debug_rollback');">-->
		</td>
		<td align="right">
			<%=comS1%>
				<button type="button"><html:link action="/kintai/Knt200">総務</html:link></button>
				<button type="button"><html:link action="/kintai/Knt500">データ移行</html:link></button>
			<%=comS2%>				
			<%=comM1%>
				<button type="button"><html:link action="/kintai/Knt400">作業管理</html:link></button>
			<%=comM2%>
				<button type="button"><html:link action="/kintai/Knt300">管理情報検索</html:link></button>
			<%=comM1%>
				<button type="button"><html:link action="/kintai/Knt302">管理情報登録</html:link></button>
				<button type="button"><html:link action="/kintai/Knt900">経営情報検索</html:link></button>
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
							<html:link action="/kintai/Knt200">総務</html:link>
						</li>
						<li>
							<html:link action="/kintai/Knt500">データ移行</html:link>
						</li>
						<li>
							<html:link action="/kintai/Knt400">作業管理</html:link>
						</li>
						<li>
							<html:link action="/kintai/Knt300">管理情報検索</html:link>
						</li>
						<li>
							<html:link action="/kintai/Knt302">管理情報登録</html:link>
						</li>
						<li>
							<html:link action="/kintai/Knt900">経営情報検索</html:link>
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
		<th class="holiday"  width="10%">日</th>
		<th class="weekday"  width="10%" >月</th>
		<th class="weekday"  width="10%" >火</th>
		<th class="weekday"  width="10%" >水</th>
		<th class="weekday"  width="10%" >木</th>
		<th class="weekday"  width="10%" >金</th>
		<th class="saturday" width="10%">土</th>
	</tr>
	<%int d=1;
		//月内の間はループ
		while(cal.get(Calendar.MONTH)==dispM-1){%>
	<tr height="100px">
		<!--必ず日曜～土曜の一週間分は処理をするループ-->
		<%for(int j=0;j<7;j++){%>
			<%
			//各曜日のクラス決定
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

			<%}//■終了■各曜日のクラス決定%>

			<%
			//もしkが0（＝日曜）でなければkから1を引く
			//【k】： 日曜が0、土曜が6
			//★★★	はじめの1日目を書き込む曜日の確定用	★★★
			//★★★	＝前月分の空枠	★★★
			if(k!=0){
				k--;
			//もしkが0（＝日曜）なら、もし月内の間なら
			//★★★	月が不一致の場合は空白出力	★★★
			}else if (cal.get(Calendar.MONTH)==dispM-1){%>

				<!--変数dを1増やす前の日付保持-->
				<%int md=d;%>
				<!--変数dを1増やして日付表示-->
				<%=d++%><br>

				<!--クリックした時に次のJSPへ年月日のHashMapを渡す-->
				<%
					Map ymdMap = new HashMap();
					ymdMap.put("dd",md);
					ymdMap.put("mm",dispM);
					ymdMap.put("yyyy",dispY);
					request.setAttribute("ymdMap",ymdMap);
				%>
				<!--HashMapより日付の実績時間を取得-->

				<%
					// 本日より前日のみ編集可能にする
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

				<!-- 書式 -->
				<%
					String realTime = String.format("%.02f",stpMap.get(md)) ;
					String reqTime = String.format("%.02f",uiMap.get(md)) ;
				%>

				<%=com1%>

				<input type="button" value="申請 <%=reqTime%>" style="font-size:110%;border:none;background-color:transparent;color:blue;" onClick="buttonPush('detail001,<%=dispY%>,<%=dispM%>,<%=md%>');">
				<br>
				<font style="font-size:100%;">実績 <%=realTime%></font>

				<%=com3%>
				<input type="button" value="承認" style="font-size:110%;border:none;background-color:transparent;color:blue;" onClick="buttonPush('approval,<%=dispY%>,<%=dispM%>,<%=md%>');">
				<%=com4%>
				<%=com2%>

				<!--1日先の日付にする-->
				<%cal.add(Calendar.DATE,1);%>
			<%}%>				<!--/***if(k!=0)-->
			</td>				<!--/***td(holiday,saturday,weekday)-->
		<%}%>					<!--/***for-->
	</tr>						<!--/***tr(外側-->
	<%}%>						<!--/***while-->
</table>

<table id="table-knt001" width="100px" border="0">
	<tr>
		<td align="right" width="75%">
			申請合計（有給）:
		</td>
		<td align="right">
			<%=reqTotalTimeF2%>（<%=uqtF2%>） 時間
		</td>
	</tr>
</table>

</html:form>
</body>
</html:html>