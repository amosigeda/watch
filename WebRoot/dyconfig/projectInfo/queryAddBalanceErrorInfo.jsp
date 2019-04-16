<%@ page language="java" contentType="text/html;charset=gb2312"%>
<%@ page import="com.godoing.rose.http.common.*"%>
<%@ page import="com.care.common.lang.*"%>
<%@ page import="com.care.common.config.Config"%>
<%@ page import="com.care.app.LoginUser"%>
<%@ taglib uri="/WEB-INF/struts-bean" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic"%>
<%@ page autoFlush="true"%>
<%
	/*页面属性*/
	PagePys pys = (PagePys) request.getAttribute("PagePys");
	LoginUser loginUser = (LoginUser)request.getSession().getAttribute(Config.SystemConfig.LOGINUSER); 
%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<title>无标题文档</title>
		<link href="<%=request.getContextPath()%>/css/tbls.css"
			rel="stylesheet" type="text/css">
		<script language="JavaScript"
			src="<%=request.getContextPath()%>/public/public.js"></script>   <!-- 调用此方法 -->
		<script language="JavaScript" src="../../js/jquery-1.8.2.js"></script>
		<script language="JavaScript"
			src="<%=request.getContextPath()%>/public/My97DatePicker/WdatePicker.js"></script>
	</head>
	<script language="javascript">
function finds(){
  /*   var st = new Date(frmGo.startTime.value.replace(/-/g,'/'));
	var et = new Date(frmGo.endTime.value.replace(/-/g,'/'));
	if(Date.parse(st) - Date.parse(et)>0){
		alert("开始时间不能大于结束时间!");
		return false;
	} */
	   frmGo.submit();
}
function add(){
	frmGo.action = "doProjectInfo.do?method=initInsertWatch";
	frmGo.submit();
}
function c(){
    document.all.project_no.value="";
    document.all.remark.value="";
    
  /*   document.all.userId.options[0].selected=true;
    document.all.projectId.options[0].selected=true; */
} 

function changeCompany(obj){
	$.ajax({
		type:"get",
		url:"doProjectInfo.do?method=getProjectByCompanyId",
		data:"companyId="+obj,
		success:function(msg){
			var projectSelect = document.getElementById("projectId");
			projectSelect.length=1;
			if(msg.trim() != ""){				
				var projects = msg.split("&");
				for(i=0;i<projects.length; i++){
					var project = projects[i].split(",");
					if(project.length == 2){
						projectSelect.options[projectSelect.length] = new Option(project[1],project[0]);
						
					}
					
				}
			}
			
		}
	});
}
function update(id){
	frmGo.action="doProjectInfo.do?method=initUpdateWatch&id="+id;
	frmGo.submit();
}
function updateStatus(id,status){
	frmGo.action="doProjectInfo.do?method=initUpdateWatchStatus&id="+id+"&s="+status;
	frmGo.submit();
}
function deletee(id){
	frmGo.action="doProjectInfo.do?method=deletewatch&id="+id;
	frmGo.submit();
}
function ofuncs(projectId){
	window.open("projectRoleFuncFrame.jsp?projectId=" + i);
	//window.showModalDialog("roleFuncFrame.jsp?roleCode=" + rcode,		"","help:0;resizable:0;status=0;scrollbars=0;dialogWidth=25;dialogHeight=35;center=true");;
}
</script>
	<body>
		<span class="title_1"></span>
		<form name="frmGo" method="post" action="doProjectInfo.do?method=addBalanceErrorInfo">
			
			<table width="100%" class="table" border=0 cellpadding="0" cellspacing="1">
               <tr>
                <th colspan="12" nowrap="nowrap" align="left">
                                               充值失败记录
                    <!--  <input type="button" class="but_1" accesskey="a"
							tabindex="a" value="添 加" onclick="add()"> -->
                </th>
                </tr>
                 <tr class="title_3">
                        <td colspan="13">
				<%-- 	  创建时间
                     <input name="startTime" type="text" class="txt_1"  id="startTime" style="cursor:text"
								value="<%CommUtils.printReqByAtt(request,response,"fNow_date");%>" onclick="WdatePicker()"
								size="9" readonly> -
							<input name="endTime" type="text" class="txt_1" id="endTime" style="cursor:text"
								value="<%CommUtils.printReqByAtt(request,response,"now_date");%>" onclick="WdatePicker()"
								size="9" readonly>		 --%>				
							商户
						    <input id="project_no" name="project_no" type="text" class="txt_1" 
						    value="<%CommUtils.printReqByAtt(request,response,"project_no");%>" size="20">
						  <%--   skin id
						    <input id="remark" name="remark" type="text" class="txt_1" 
						    value="<%CommUtils.printReqByAtt(request,response,"remark");%>" size="20"> --%>
					<%-- 	项目名		
						<%String projectId = (String)request.getAttribute("projectId"); %>			
							<select id="projectId" name="projectId" >
								<option value="">全部</option>
								<logic:iterate id="pro" name="project">
									<bean:define id="project" name="pro" property="id" type="java.lang.Integer" />																	
									<option value='<%=project %>' <%=String.valueOf(project).equals(projectId)? "selected" : "" %>><bean:write name="pro" property="project_name"/></option>
								</logic:iterate>
							</select>	 --%>						
						<input name="find1" type="button" class="but_1" accesskey="f"
							tabindex="f" value="搜 索" onclick="javascript:finds()">
					     <input name="clear" type="button" class="but_1" accesskey="c"
						    tabindex="c"  value="清除搜索" onclick="c()"> 
				</tr> 
				<%int i=1; %>
                  <tr class="title_2">
                 	 <td width="10%">
						商户
					</td>
					<td width="8%">
						订单id
					</td>
					<td width="8%">
						充值号
					</td>
					<td width="6%">
						充值金额
					</td>	
					<td width="6%">
						时间
					</td>					
					<td width="6%">
						状态码
					</td>
					
				</tr>
 
				<logic:iterate id="element" name="pageList">
					<tr class="tr_5" onmouseover='this.className="tr_4"'
						onmouseout='this.className="tr_5"'>
						
						<td>							
							<bean:write name="element" property="username" />
						</td>
						<td>							
							<bean:write name="element" property="order_id" />
						</td>
						<td>							
							<bean:write name="element" property="charge_acct" />
						</td>
						<td>							
							<bean:write name="element" property="charge_cash" />
						</td>
					<td>								
							<bean:write name="element" property="createtime" format="yyyy-MM-dd HH:mm:ss"/>
						</td>
						<td>							
							<bean:write name="element" property="error_code" />
						</td>
						
						<%--  <td>
    	                 <img src="<bean:write name="element" property="adDetail"/>"  style="vertical-align:bottom"  width = "150px" height = "150px"></img> 
						</td> --%>
						
					</tr>
				</logic:iterate>

			  	<tr class="title_3">
					<td colspan="12" align="left" >  
						<%
							pys.printGoPage(response, "frmGo");
						%>
					</td>
				</tr>  
			</table>
		</form>
	</body>
</html>