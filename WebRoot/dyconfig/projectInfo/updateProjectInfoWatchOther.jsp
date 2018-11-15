<%@ page language="java" contentType="text/html;charset=gb2312"%>
<%@ page import = "com.godoing.rose.http.common.*" %>
<%@ page import = "com.godoing.rose.lang.*" %>
<%@ page import="com.care.common.lang.*"%>
<%@ page import="com.care.common.config.Config"%>
<%@ page import="com.care.app.LoginUser"%>
<%@ taglib uri="/WEB-INF/struts-bean" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic"%>

<jsp:useBean id = "projectInfo" scope = "request" class = "com.godoing.rose.lang.DataMap"/>
<%@ page autoFlush="true" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link href="<%=request.getContextPath()%>/css/tbls.css" rel="stylesheet" type="text/css">
<script language="JavaScript" src="<%=request.getContextPath()%>/public/public.js"></script>
<script language="JavaScript" src="<%=request.getContextPath()%>/js/jquery-1.8.2.js"></script>
<title>无标题文档</title>
</head>
<script language="javascript">
function selectProject(obj){
    var a=frmGo.projectName1.value;
    var b=frmGo.projectName.value;
	$.ajax({
		type:"get",
		url:"doProjectInfo.do?method=queryProjectName",
		data:"projectName="+obj,
		success:function(msg){
			 if(msg.trim() != ""&&b!=a){	
				alert("项目名已经存在,请重新输入！");
				 document.all.projectName.value=""; 
			} 
			}
	});

}
function onUpdate(){
/* 	if(frmGo.projectName.value.trim() == ''){
		alert("项目名称不能为空");
		frmGo.ProjectName.focus();
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
<form name="frmGo" method="post" action="doProjectInfo.do?method=updateProjectWatchInfo" encType="multipart/form-data" onsubmit="return onUpdate()">
<input name="id" type="hidden" value="<%=projectInfo.getAt("id")%>" >
<table width="100%" border="0"cellpadding="0" cellspacing="1"  class="tbl_11">
  <tr>
     <th colspan="3" nowrap="nowrap" align="left">
                           表盘配置
     </th>
   </tr>
  <tr class="tr_11">
   <%--  <td width="7%" align="left">&nbsp;&nbsp;客户名</td>
    <td align="left" >
      <input type="text" size="50"  name="project_no" id="project_no" value=<%=projectInfo.getAt("project_no")%>
       > --%>
 <!--      onchange="selectProject(this.value)"> -->
       <%-- <td align="left" >
      <input type="text" name="project_name" id="projectName1" value=<%=projectInfo.getAt("project_name")%>
      style="display:none"> 
    </td>
    <td></td>--%>
  </tr>
  <tr class="tr_11">
    <td width="7%" align="left">&nbsp;&nbsp;name id</td>
    <td align="left" >
      <input type="text" size="50" name="project_name" id="project_name" value=<%=projectInfo.getAt("project_name")%>
      >
    </td>
    <td></td>
  </tr>
 
  <tr class="tr_11">
    <td width="7%" align="left">&nbsp;&nbsp;type id</td>
     <td width="20%" align="left">
    	<select name="company_id"  id="company_id">     
  <option value="R-400" <%=projectInfo.getAt("company_id").equals("R-400")? "selected":"" %>>R-400</option>     
  <option value="R-360" <%=projectInfo.getAt("company_id").equals("R-360")? "selected":"" %>>R-360</option>  
  <option value="S-320" <%=projectInfo.getAt("company_id").equals("S-320")? "selected":"" %>>S-320</option>  
 </select>  
 
    <td></td>
  </tr>
  
 
   <tr class="tr_11">
    <td width="7%" align="left">&nbsp;&nbsp;文件file </td>
    <td>
     <a href="<%=projectInfo.getAt("adTitle")%>" title="zip" style="color:#0000FF">【下载】</a>
<%--     <a herf="<%=projectInfo.getAt("adTitle")%>"   style="color:#0000FF">下载zip</a> --%>
    	<%--  <img src="<%=projectInfo.getAt("channel_id")%>"  style="vertical-align:bottom"  width = "100%" height = "30%"></img>  --%>
						</td>
						
    <td align="left" >
     
    					<input type="file" name="channelId" id="channelId" multiple="multiple" class="imagePath"/>
    					<img alt="" src="" id="image1" class="showImage">
    				</td>
 
   
  </tr>
    <tr class="tr_11">
    <td width="7%" align="left">&nbsp;&nbsp;预览 </td>
    <td>
    	 <img src="<%=projectInfo.getAt("adDetail")%>"  style="vertical-align:bottom"  width = "65%" height = "65%"></img> 
						</td>
						
    <td align="left" >
     
    					<input type="file" name="adDetail" id="adDetail" multiple="multiple" class="imagePath"/>
    					<img alt="" src="" id="image2" class="showImage">
    				</td>
 
   
  </tr>
  <%--  <tr class="tr_11">
    <td width="7%" align="left">&nbsp;&nbsp;advertisingUrl</td>
    <td align="left">
    	<input name="status" type="radio" class="radio_1" value="1" <%if("1".equals("" + projectInfo.getAt("status"))){%>checked<%}%>>正常
	    <input name="status" type="radio" class="radio_1" value="0" <%if("0".equals("" + projectInfo.getAt("status"))){%>checked<%}%>>禁用
    </td>
  </tr> --%>
    <%-- <tr class="tr_11">
    <td width="7%" align="left">&nbsp;&nbsp;备注</td>
    <td width="20%" align="left">
      <textarea name="remark" id="remark" rows="5" cols="50" class="txt_1" maxlength="30"><%=projectInfo.getAt("remark")%></textarea>
    </td>
    <td align="left">(字数不能超过30字)</font></td>
  </tr> --%>
  
  <tr  class="tr_11">
    <td></td>
    <td  align="left">&nbsp;&nbsp;&nbsp;<input type="button" name="ok" accesskey="y" tabindex="y"  value="确 定" class="but_1" onclick="onUpdate()">
	
      <input type="button" name="back" accesskey="b" tabindex="b" value="返 回" class="but_1" onclick="location='doProjectInfo.do?method=queryWatchInfo'">
      <input type="reset" name="back" accesskey="b" tabindex="b" value="重置" class="but_1" >
    </td>
  </tr>
</table>
</form>
</body>
</html>