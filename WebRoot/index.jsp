<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html;charset=gb2312"%>
<%
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script language="JavaScript" src="public/g_js.js"></script>
<title>�ڶ౦��̨����ϵͳ</title>
<link href="css/styles.css" rel="stylesheet" type="text/css">
</head>
<script language="javascript">
function loadimage(){ 
document.getElementById("randImage").src = "image.jsp?"+Math.random(); 
altet(Math.random());
} 
function checkSpace(str){
	for (var i = 0; i < str.length; i++) {
		if (str.charAt(i) == ' ') {
			return false;
		}
	}
	return true;
}
function check(){
	if (frmLogon.code.value ==""){
		alert("�û�������Ϊ�գ�");
		frmLogon.code.focus();
		return false;
	}
	if (frmLogon.paw.value ==""){
		alert("���벻��Ϊ�գ�");
		frmLogon.paw.focus();
		return false;
	}
	if (frmLogon.paw.value !=""){
	  if (!checkSpace(frmLogon.paw.value)){
		alert("���벻���пո�");
		frmLogon.paw.focus();
	  return false;
	  }
	}
	return true;
}
	
function submit(){  
	if(!check()){
	   return false;
	}
	frmLogon.submit();
}

function clears(){
	frmLogon.code.value = "";
	frmLogon.paw.value = "";	
	frmLogon.rand.value = "";
}
</script>
<body>
<form name="frmLogon" method="post" action="validate.jsp" onSubmit="return check()">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" background="images/login/blue.jpg">
  
  <tr>
    <td height="699"><table width="862" border="0" align="center" cellpadding="0" cellspacing="0">
      <tr>
        <td height="198">&nbsp;</td>
      </tr>
      <tr>
        <td height="245"><table width="861" border="0" cellspacing="0">
            <tr>
              <td width="236" height="244">&nbsp;</td>
              <td width="373"><table width="378" height="240" border="0" cellspacing="0" background="images/bg/desktop3.jpg">
                <tr>
                  <td width="90" height="56">&nbsp;</td>
                  <td width="192">&nbsp;</td>
                  <td width="90">&nbsp;</td>
                </tr>
                <tr>
                  <td height="125">&nbsp;</td>
                  <td><table width="100%" height="128" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                      <td width="230" height="118"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                          <tr>
                            <td width="28%" height="30"><div align="center"><span class="STYLE3">�û�</span></div></td>
                            <td width="72%" height="30"><input type="text" name="code" value="" style="height:18px; width:130px; border:solid 1px #cadcb2; font-size:12px;" ></td>
                          </tr>
                          <tr>
                            <td height="30"><div align="center"><span class="STYLE3">����</span></div></td>
                            <td height="30"><input type="password" name="paw" value="" style="height:18px; width:130px; border:solid 1px #cadcb2; font-size:12px;"></td>
                          </tr>
                         <!--  <tr>
                            <td height="30"><div align="center"><span class="STYLE3">У����</span></div></td>
                            <td width="72%" height="30"><input type="text" name="rand" value="" style="height:18px; width:130px; border:solid 1px #cadcb2; font-size:12px;" ></td>
                          </tr>
                          <tr>
                           </tr>
                            <tr>
                            <td height="30"><div align="center"><span class="STYLE3"><a href="javascript:loadimage();">��һ��</a></span></div></td>
                            <td width="72%" height="30"><img alt="code..." name="randImage" id="randImage" src="image.jsp" style="height:18px; width:130px; border:solid 1px #cadcb2; font-size:12px;" ></td>
                            
                          </tr> -->
                          <tr>
                            <td height="30">&nbsp;</td>
                            <td height="30"><input type="submit" name="b1" value="ȷ��">
                                <input type="button" name="b2" value="���" onClick="javascript:clears()"></td>
                          </tr>
                      </table></td>
            </tr>
                  </table></td>
                  <td>&nbsp;</td>
                </tr>
                <tr>
                  <td height="53">&nbsp;</td>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                </tr>
              </table></td>
              <td width="246">&nbsp;</td>
            </tr>
            
            
          </table></td>
      </tr>
      <tr>
        <td height="250" valign="top">&nbsp;</td>
      </tr>
    </table></td>
  </tr>
</table>
</form>
</body>
<script language="javascript" type="text/javascript">
frmLogon.code.focus();
</script>
</html>
