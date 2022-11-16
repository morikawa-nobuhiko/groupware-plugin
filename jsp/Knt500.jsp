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
			<h1>�f�[�^�ڍs</h1>
		</td>
		<td align="center" width="50%">
			<logic:messagesPresent property="init">
				<font color="blue">�C���|�[�g�t�@�C���͊ԈႦ�Ȃ��l�ɒ��ӂ��đI�����Ă��������B</font>
			</logic:messagesPresent>
			<logic:messagesPresent property="registed">
				<font color="black">�o�^���܂����B</font>
			</logic:messagesPresent>
			<logic:messagesPresent property="nofile">
				<font color="red">�C���|�[�gCSV�t�@�C�����I������Ă��܂���B</font>
			</logic:messagesPresent>
		</td>
		<td align="right" width="20%">
			<button type="button"><html:link action="/kintai/Knt001">�@�߁@��@</html:link></button>
		</td>
	</tr>
</table>

<table width="80%" border="1">
	<tr>
		<th bgcolor="#CCFFFF" width="20%">���@��</th>
		<th bgcolor="#CCFFFF" width="20%">�ו���</th>
		<th bgcolor="#CCFFFF" width="auto">�{�Зp</th>
		<th bgcolor="#CCFFFF" width="auto">�x�Зp</th>
	</tr>
	<tr>
		<td align="center" rowspan="1">
			�t�@�C���I��
		</td>
		<td align="center" rowspan="1">
			�C���|�[�g�p
		</td>
		<td align="center" rowspan="1" colspan="2">
			<html:form action="/kintai/Knt500" method="POST">
				<html:file property="fileUp"/>
			</html:form>
		</td>
	</tr>
	<tr>
		<td align="center" rowspan="3">
			�v���W�F�N�g
		</td>
		<td align="center" rowspan="1">
			���C��
		</td>
		<td align="center" rowspan="1">
			<input type="button" value="  �G�N�X�|�[�g  " onClick="buttonPush('exportProject');">
		</td>
		<td align="center" rowspan="1">
			<input type="button" value="    �C���|�[�g    " onClick="buttonPush('importProject');">
		</td>
	</tr>
	<tr>
		<td align="center" rowspan="1">
			�S����
		</td>
		<td align="center" rowspan="1">
			<input type="button" value="  �G�N�X�|�[�g  " onClick="buttonPush('exportProjectTanto');">
		</td>
		<td align="center" rowspan="1">
			<input type="button" value="    �C���|�[�g    " onClick="buttonPush('importProjectTanto');">
		</td>
	</tr>
	<tr>
		<td align="center" rowspan="1">
			���
		</td>
		<td align="center" rowspan="1">
			<input type="button" value="  �G�N�X�|�[�g  " onClick="buttonPush('exportWork');">
		</td>
		<td align="center" rowspan="1">
			<input type="button" value="    �C���|�[�g    " onClick="buttonPush('importWork');">
		</td>
	</tr>
	<tr>
		<td align="center" rowspan="2">
			�ΑӃf�[�^
		</td>
		<td align="center" rowspan="1">
			����
		</td>
		<td align="center" rowspan="1">
			<input type="submit" value="    �C���|�[�g    " onClick="buttonPush('importStamp');">
			<input type="button" value="  �G�N�X�|�[�g  " onClick="buttonPush('exportStamp');">
		</td>
		<td align="center" rowspan="1">
			<input type="button" value="  �G�N�X�|�[�g  " onClick="buttonPush('exportStamp');">
		</td>
	</tr>
	<tr>
		<td align="center" rowspan="1">
			�\��
		</td>
		<td align="center" rowspan="1">
			<input type="submit" value="    �C���|�[�g    " onClick="buttonPush('importUserStamp');">
		</td>
		<td align="center" rowspan="1">
			<input type="button" value="  �G�N�X�|�[�g  " onClick="buttonPush('exportUserStamp');">
		</td>
	</tr>
</table>

<!--
<table width="100%" border="1">
	<tr>
		<th bgcolor="#CCFFFF" width="10%">���@��</th>
		<th bgcolor="#CCFFFF" width="60%">�t�@�C���I���Ə���</th>
		<th bgcolor="#CCFFFF" width="30%">���@�l</th>
	</tr>
	<tr>
		<td align="center" rowspan="2">
			�{�Зp
		</td>
		<td rowspan="1">
			<input type="button" value="�v���W�F�N�g�̃G�N�X�|�[�g" onClick="buttonPush('prjExport');">
		</td>
		<td align="left" style="word-wrap:break-word;">
			�x�Ђ֓n���v���W�F�N�g�f�[�^���G�N�X�|�[�g���܂��B
		</td>
	</tr>
	<tr>
		<td rowspan="1">
			<html:form action="/kintai/Knt500" method="POST">
				<html:file property="fileUp"/>
			</html:form>
			<input type="submit" value="���x�Ђ���C���|�[�g" onClick="buttonPush('importSendai');">
			<input type="submit" value="���؂���C���|�[�g" onClick="buttonPush('importNamiki');">
		</td>
		<td align="left" style="word-wrap:break-word;">
			�x�Ђ���󂯎�����ΑӃf�[�^��{�ЂփC���|�[�g���܂��B
		</td>
	</tr>
	<tr>
		<td align="center" rowspan="2">
			�x�ЁE�o���җp
		</td>
		<td align="left">
			<input type="button" value="�v���W�F�N�g�C���|�[�g" onClick="buttonPush('importProject');">
		</td>
		<td align="left" style="word-wrap:break-word;">
			�{�Ђ���̃v���W�F�N�g���C���|�[�g���܂��B
		</td>
	</tr>
	<tr>
		<td align="left">
			<input type="button" value="�ΑӃG�N�X�|�[�g" onClick="buttonPush('export');">
		</td>
		<td align="left" style="word-wrap:break-word;">
			�{�Ђ֓n���ΑӃf�[�^���G�N�X�|�[�g���܂��B
		</td>
	</tr>
</table>
-->

</html:form>
</body>
</html:html>