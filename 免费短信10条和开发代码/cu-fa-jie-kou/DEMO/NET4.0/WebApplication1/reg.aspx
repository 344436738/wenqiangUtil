<%@ Page Title="主页" Language="C#" MasterPageFile="~/Site.master" AutoEventWireup="true"
    CodeBehind="Default.aspx.cs" Inherits="WebApplication1._Default" %>

<asp:Content ID="HeaderContent" runat="server" ContentPlaceHolderID="HeadContent">
</asp:Content>
<asp:Content ID="BodyContent" runat="server" ContentPlaceHolderID="MainContent">

<script type="text/javascript" src="jquery.js"></script>
<script language="javascript">
	function get_mobile_code(){
        $.get('Post.aspx', {mobile:jQuery.trim($('#mobile').val())}, function(msg) {
            alert(jQuery.trim(unescape(msg)));
			if(msg=='提交成功'){
				RemainTime();
			}
        });
	};
	var iTime = 59;
	var Account;
	function RemainTime(){
		document.getElementById('zphone').disabled = true;
		var iSecond,sSecond="",sTime="";
		if (iTime >= 0){
			iSecond = parseInt(iTime%60);
			iMinute = parseInt(iTime/60)
			if (iSecond >= 0){
				if(iMinute>0){
					sSecond = iMinute + "分" + iSecond + "秒";
				}else{
					sSecond = iSecond + "秒";
				}
			}
			sTime=sSecond;
			if(iTime==0){
				clearTimeout(Account);
				sTime='获取手机验证码';
				iTime = 59;
				document.getElementById('zphone').disabled = false;
			}else{
				Account = setTimeout("RemainTime()",1000);
				iTime=iTime-1;
			}
		}else{
			sTime='没有倒计时';
		}
		document.getElementById('zphone').value = sTime;
	}	
</script>
<div>
<form action="reg.aspx" method="post" name="formUser" onSubmit="return register();">
	<table width="100%"  border="0" align="left" cellpadding="5" cellspacing="3">
		<tr>
			<td align="right">手机<td>
		<input id="mobile" name="mobile" type="text" size="25" class="inputBg" /><span style="color:#FF0000"> *</span> 
        <input id="zphone" type="button" value=" 发送手机验证码 " onclick="get_mobile_code();"></td>
        </tr>
		<tr>
			<td align="right">验证码</td>
			<td><input type="text" size="8" name="captcha" class="inputBg" /></td>
		</tr>
	</table>
</form>
</div>

</asp:Content>