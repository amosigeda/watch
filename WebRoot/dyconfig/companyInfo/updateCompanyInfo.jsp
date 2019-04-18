<%@ page language="java" contentType="text/html;charset=gb2312"%>
<%@ page import = "com.godoing.rose.http.common.*" %>
<%@ page import = "com.godoing.rose.lang.*" %>

<jsp:useBean id = "companyInfo" scope = "request"  class = "com.godoing.rose.lang.DataMap"/>
<%@ page autoFlush="true" %>
<html> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link href="<%=request.getContextPath()%>/css/tbls.css" rel="stylesheet" type="text/css">
<script language="JavaScript" src="<%=request.getContextPath()%>/public/public.js"></script>
<title>无标题文档</title>
</head>
<script language="javascript">
function onUpdate(){
/* 	if(frmGo.companyName.value.trim() == ''){
		alert("公司名称不能为空");
		frmGo.ProjectName.focus();
		return false;
	}
	if(frmGo.companyName.value.trim().length > 10){
		alert("字数不能超过10字");
		frmGo.remark.focus();
		return false;
	}
	if(frmGo.remark.value.trim().length > 30){
		alert("字数不能超过30字");
		frmGo.remark.focus();
		return false;
	} */
	frmGo.submit();
}


</script>
<body>
<span class="title_1"></span>
<form name="frmGo" method="post" action="doCompanyInfo.do?method=updateCompanyInfo" onsubmit="return onUpdate()">
<input name="id" type="hidden" value="<%=companyInfo.getAt("id")%>" >
<table width="100%" border="0"cellpadding="0" cellspacing="1"  class="tbl_11"> 
  <tr>
     <th colspan="13" nowrap="nowrap" align="left">
                         上游配置
     </th>
   </tr>
  <tr class="tr_11">
    <td width="7%" align="left">&nbsp;&nbsp;公司账号</td>
    <td width="20%" align="left">
    	<input name="user_name" id="user_name" type="text" class="txt_1" maxlength="20" value="<%=companyInfo.getAt("user_name")==null?" ":companyInfo.getAt("user_name")%>"><font color="red">*</font>
    </td>
    <td align="left"><font></font></td>
  </tr>
    <tr class="tr_11">
    <td width="7%" align="left">&nbsp;&nbsp;秘钥</td>
    <td width="20%" align="left">
    	<input name="secret_key" id="secret_key" type="text" class="txt_1" maxlength="20" value="<%=companyInfo.getAt("secret_key")==null?" ":companyInfo.getAt("secret_key")%>"><font color="red">*</font>
    </td>
    <td align="left"><font></font></td>
  </tr>
      <tr class="tr_11">
    <td width="7%" align="left">&nbsp;&nbsp;公司名称</td>
    <td width="20%" align="left">
    	<input name="company_name" id="company_name" type="text" class="txt_1" maxlength="20" value="<%=companyInfo.getAt("company_name")==null?" ":companyInfo.getAt("company_name")%>"><font color="red">*</font>
    </td>
    <td align="left"><font></font></td>
  </tr>
  
      <tr class="tr_11">
    <td width="7%" align="left">&nbsp;&nbsp;运营商类型</td>
    <td width="20%" align="left">
    	<input name="contain_type" style="border:none;" readonly= "true" id="contain_type" type="text" class="txt_1" maxlength="20" value="<%=companyInfo.getAt("contain_type")==null?" ":companyInfo.getAt("contain_type")%>"><font color="red">*</font>
    </td>
    <td align="left"><font></font></td>
  </tr>
  
    <tr class="tr_11">
    <td align="left" width="7%">&nbsp;&nbsp;运营商修改为</td>
     <td align="left" width="10%">&nbsp; 
					<input type="checkbox" name="type" value="1" />移动  <input type="checkbox" name="type" value="2" />联通<input type="checkbox" name="type" value="3" />电信 
					</td>
					 <td align="left"><font color="red">不选表示不修改</font></td>
  </tr>
  
  
      <tr class="tr_11">
    <td width="7%" align="left">&nbsp;&nbsp;回调地址</td>
    <td width="20%" align="left">
    	<input name="returl" id="returl" type="text" class="txt_1" maxlength="20" value="<%=companyInfo.getAt("returl")==null?" ":companyInfo.getAt("returl")%>"><font color="red">*</font>
    </td>
    <td align="left"><font></font></td>
  </tr>
  
  <tr class="tr_11">
    <td width="7%" align="left">&nbsp;&nbsp;运营状态</td>
    <td width="20%" align="left">
    	<input name="status" type="radio" class="txt_1" value="0" <%=companyInfo.getAt("status").equals("0")? "checked":"" %>>暂停
    	<input name="status" type="radio" class="txt_1" value="1" <%=companyInfo.getAt("status").equals("1")? "checked":"" %> >正常
    </td>
    <td align="left"></td>
  </tr>
  <tr class="tr_11">
    <td width="7%" align="left">&nbsp;&nbsp;备注</td>
    <td width="20%" align="left">
      <textarea name="remark" id="remark" rows="5" cols="30" class="txt_1" maxlength="30"><%=companyInfo.getAt("remark")==null?" ":companyInfo.getAt("remark")%></textarea>
    </td>
    <td align="left"><font color="red">(字数不能超过30字)</font></td>
  </tr>
  <tr  class="tr_11"> 
    <td></td>
    <td  align="left">&nbsp;&nbsp;&nbsp;<input type="button" name="ok" accesskey="y" tabindex="y"  value="确 定" class="but_1" onclick="onUpdate()">
	
      <input type="button" name="back" accesskey="b" tabindex="b" value="返 回" class="but_1" onclick="location='doCompanyInfo.do?method=queryCompanyInfo'">
       <input type="reset" name="back" accesskey="b" tabindex="b" value="重置" class="but_1" >
    </td>	     
    </td>
    <td></td>
  </tr>
</table>
</form>
</body>
</html>