<%@ page language="java" contentType="text/html;charset=gb2312"%>
<%@ page import="com.godoing.rose.http.common.*"%>
<%@ page import="com.care.common.lang.*"%>
<%@ page import="com.care.common.config.Config"%>
<%@ page import="com.care.app.LoginUser"%>
<%@ taglib uri="/WEB-INF/struts-bean" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic"%>
<%@ page autoFlush="true"%>
<%
	/*ҳ������*/
	PagePys pys = (PagePys) request.getAttribute("PagePys");
	LoginUser loginUser = (LoginUser)request.getSession().getAttribute(Config.SystemConfig.LOGINUSER); 
%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<title>�ޱ����ĵ�</title>
		<link href="<%=request.getContextPath()%>/css/tbls.css"
			rel="stylesheet" type="text/css">
		<script language="JavaScript"
			src="<%=request.getContextPath()%>/public/public.js"></script>   <!-- ���ô˷��� -->
		<script language="JavaScript" src="../../js/jquery-1.8.2.js"></script>
		<script language="JavaScript"
			src="<%=request.getContextPath()%>/public/My97DatePicker/WdatePicker.js"></script>
	</head>
	<script language="javascript">
function finds(){
 /*    var st = new Date(frmGo.startTime.value.replace(/-/g,'/'));
	var et = new Date(frmGo.endTime.value.replace(/-/g,'/'));
	if(Date.parse(st) - Date.parse(et)>0){
		alert("��ʼʱ�䲻�ܴ��ڽ���ʱ��!");
		return false;
	} */
	   frmGo.submit();
}
function add(){
	frmGo.action = "doProjectInfo.do?method=initInsertBu";
	frmGo.submit();
}
function c(){
    document.all.imei.value="";
   /*  document.all.endTime.value="";
    document.all.userId.options[0].selected=true;
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
	if(confirm("ȷ���޸���?"))
	{
	frmGo.action="doProjectInfo.do?method=initUpdateBu&id="+id;
	frmGo.submit();
	}
}
function deletee(id){
	if(confirm("ȷ��ɾ����?"))
	{
	frmGo.action="doProjectInfo.do?method=deletexml&id="+id;
	frmGo.submit();
	}
}
function addBlanceByid(id){
	frmGo.action="doProjectInfo.do?method=initAddBalanceById&id="+id;
	frmGo.submit();
}

function resetScretById(imei){
	if(confirm("ȷ�������?"))
	{
		frmGo.action="doProjectInfo.do?method=resetScretById&imei="+imei;
		frmGo.submit();
	}
}
function ofuncs(projectId){
	window.open("projectRoleFuncFrame.jsp?projectId=" + i);
	//window.showModalDialog("roleFuncFrame.jsp?roleCode=" + rcode,		"","help:0;resizable:0;status=0;scrollbars=0;dialogWidth=25;dialogHeight=35;center=true");;
}
</script>
	<body>
		<span class="title_1"></span>
		<form name="frmGo" method="post" action="doProjectInfo.do?method=queryProjectInfoXml">
			
			<table width="100%" class="table" border=0 cellpadding="0" cellspacing="1">
               <tr>
                <th colspan="12" nowrap="nowrap" align="left">
                                               �豸��Ϣ
                    <!--  <input type="button" class="but_1" accesskey="a"
							tabindex="a" value="�� ��" onclick="add()"> -->
                </th>
                </tr>
                <tr class="title_3">
                        <td colspan="13">
                        IMEI <input id="imei" name="imei" type="text" class="txt_1" 
						    value="<%CommUtils.printReqByAtt(request,response,"imei");%>" size="20">
					<%--   ����ʱ��
                     <input name="startTime" type="text" class="txt_1"  id="startTime" style="cursor:text"
								value="<%CommUtils.printReqByAtt(request,response,"fNow_date");%>" onclick="WdatePicker()"
								size="9" readonly> -
							<input name="endTime" type="text" class="txt_1" id="endTime" style="cursor:text"
								value="<%CommUtils.printReqByAtt(request,response,"now_date");%>" onclick="WdatePicker()"
								size="9" readonly>						
						�ͻ�
							<%String userId = (String)request.getAttribute("userId"); %>
							<select id="userId" name="userId" onchange="changeCompany(this.value)">
								<option value="">ȫ��</option>
								<logic:iterate id="com" name="company">
									<bean:define id="company" name="com" property="id" type="java.lang.Integer" />																	
									<option value='<%=company %>' <%=String.valueOf(company).equals(userId)? "selected" : "" %>><bean:write name="com" property="company_name"/></option>
								</logic:iterate>
							</select> 
						IMEI	
						<%String projectId = (String)request.getAttribute("projectId"); %>			
							<select id="projectId" name="projectId" >
								<option value="">ȫ��</option>
								<logic:iterate id="pro" name="project">
									<bean:define id="project" name="pro" property="id" type="java.lang.Integer" />																	
									<option value='<%=project %>' <%=String.valueOf(project).equals(projectId)? "selected" : "" %>><bean:write name="pro" property="project_name"/></option>
								</logic:iterate>
							</select>		
							--%>					
						<input name="find1" type="button" class="but_1" accesskey="f"
							tabindex="f" value="�� ��" onclick="javascript:finds()">
					     <input name="clear" type="button" class="but_1" accesskey="c"
						    tabindex="c"  value="�������" onclick="c()"> 
				</tr>  
				<%int i=1; %>
                  <tr class="title_2">
                 	 <td width="3%">
						ID
					</td>
					<td width="8%">
						IMEI
					</td>
					<!-- <td width="10%">
						��Կ
					</td>
						<td width="6%">
						��˾����
					</td>
							
					<td width="6%">
						���
					</td>
				
					<td width="6%">
						��ע
					</td>
					 <td width="8%">
						�Ƿ���Ч
					</td> 
					 <td width="8%">
						��������
					</td> 
					 <td width="8%">
						�ص�URL
					</td>  -->
				
					<td width="10%">
						����ʱ��
					</td>
					
					<td width="10%">
						����
					</td>
				</tr>
 
				<logic:iterate id="element" name="pageList">
					<tr class="tr_5" onmouseover='this.className="tr_4"'
						onmouseout='this.className="tr_5"'>
						
						<td>							
							<bean:write name="element" property="id" />
						</td>
						
						<td>							
							<bean:write name="element" property="imei" />
						</td>
				<%-- 		<td>							
							<bean:write name="element" property="scret" />
						</td>
						<td>
							<bean:write name="element" property="nickname" />											
						</td>
						<td>
							<bean:write name="element" property="balance" />											
						</td>
						<td>
							<bean:write name="element" property="avatar" />											
						</td>
							<td>
							<logic:equal name="element" property="use_status" value="0"><font style="color:red;">��ͣ</font></logic:equal>							
							<logic:equal name="element" property="use_status" value="1"><font style="color:green;">����</font></logic:equal>
						</td>
						<td>								
							<bean:write name="element" property="shangyou_content" />					
						</td>
						<td>								
							<bean:write name="element" property="url" />					
						</td> --%>
						<td>								
							<bean:write name="element" property="createtime" format="yyyy-MM-dd HH:mm:ss"/>
						</td>
						<%-- <td>
							<bean:write name="element" property="heart_s" />											
						</td> --%>
						<%-- <td>
							<logic:empty name="element" property="count_device">0</logic:empty>
							<logic:notEmpty name="element" property="count_device">
								<a style="color:#0000FF" href="../deviceActiveInfo/doDeviceActiveInfo.do?method=queryDeviceActiveInfo&belongProject=
									<bean:write name="element" property="belong_project"/>">
									<bean:write name="element" property="count_device"/>
								</a>
							</logic:notEmpty>
						</td> --%>
						<!-- <td>							
							<bean:write name="element" property="channel_id" />
						</td> -->						
					<%-- 	<td>
							<logic:equal name="element" property="status" value="0"><font style="color:red;">��ͣ</font></logic:equal>							
							<logic:equal name="element" property="status" value="1"><font style="color:green;">����</font></logic:equal>
						</td> --%>
						
						<%-- <td>	
							<logic:empty name="element" property="remark">��</logic:empty>
							<logic:notEmpty name="element" property="remark">			
							<bean:write name="element" property="remark" />
							</logic:notEmpty>
						</td>	 --%>									 						
						<td>
							<%-- <a href=# onclick="update('<bean:write name="element" property="id" />')" style="color:#0000FF" > ���޸ġ�</a>
							<a href=# onclick="addBlanceByid('<bean:write name="element" property="id" />')" style="color:#0000FF" > ����ֵ��</a>  --%>
							<a href=# onclick="resetScretById('<bean:write name="element" property="imei" />')" style="color:#0000FF" > �����</a> 
							<%-- <a href=# onclick="deletee('<bean:write name="element" property="id" />')" style="color:#0000FF" > ��ɾ����</a>  --%>
							<%-- <a href="#"
								onclick="ofuncs('<bean:write name="element" property="id" />')"  class="tbl_A" >��Ȩ�����á�</a> --%>
						</td>
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