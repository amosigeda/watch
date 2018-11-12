<%@ page language="java" contentType="text/html;charset=gb2312"%>
<%@ page import = "com.godoing.rose.http.common.*" %>
<%@ page import = "com.godoing.rose.lang.*" %>

<jsp:useBean id = "firmDevice" scope = "request"  class = "com.godoing.rose.lang.DataMap"/>
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
	
	frm.submit();
}

</script>
<body>
<span class="title_1"></span>
<form name="frm" method="post" action="doCheckInfo.do?method=updateFirmRemarks" onsubmit="return onUpdate()">
<input name="id" type="hidden" value="<%=firmDevice.getAt("id")%>" >
<table width="100%" border="0"cellpadding="0" cellspacing="1"  class="tbl_11"> 
  <tr>
     <th colspan="13" nowrap="nowrap" align="left">
                           添加备注-<font color="#FFFF00"><%=firmDevice.getAt("id") %></font>
     </th>
   </tr>
   
  <tr class="tr_11">
    <td width="7%" align="left">&nbsp;&nbsp;版本说明</td>
    <td width="80%" align="left">
      <textarea name="functionCap" id="functionCap" rows="5" cols="30" class="txt_1" maxlength="30"><%=firmDevice.getAt("function_cap")==null?"":firmDevice.getAt("function_cap")%></textarea>
   <!--  <input name="File1" id="File1" value="care.apk" style="display:none"></input> -->
    </td>
    <td align="left"></td>
  </tr>

  <tr class="tr_11">
    <td width="7%" align="left">&nbsp;&nbsp;备注</td>
    <td width="80%" align="left">
      <textarea name="remarks" id="remarks" rows="5" cols="30" class="txt_1" maxlength="30"><%=firmDevice.getAt("remarks")==null?"":firmDevice.getAt("remarks")%></textarea>
    </td>
    <td align="left"></td>
    </tr>
  <tr  class="tr_11"> 
    <td></td>
    <td  align="left">
    <input type="button" name="back" accesskey="b" tabindex="b" value="返 回" class="but_1" onclick="location='doCheckInfo.do?method=queryFirmDevice'"> 
    <input type="reset" name="ok" accesskey="y" tabindex="y"  value="重置" class="but_1">	     
    <input type="button" name="ok" accesskey="y" tabindex="y"  value="确 定" class="but_1" onclick="onUpdate()">	     
    </td>
    <td></td>
  </tr>
</table>
</form>
</body>
</html>