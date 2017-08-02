<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/ajaxfileupload.js"></script>


<style type="text/css">
	tr{
	    display:block; /*将tr设置为块体元素*/
	    margin:10px 0;  /*设置tr间距为2px*/
	}
</style>
<title>Insert title here</title>
</head>
<body>
<div style="margin-left:470px;margin-top:260px;">
<form method="post" id="resultForm" action="<%=request.getContextPath() %>/automation/uploadFile.do" enctype="multipart/form-data">
<table>
	<tr>
		<td>
			<span>请选择需要创建的客户文件:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
			<input type="file" id="customer" name="customer">
		</td>
		<td><input type="button" id="customerUpload" value="执行" style="width:60px;"></td>
	</tr>
	<tr></tr>
	
	<tr>
		<td>
			<span>请选择需要完善客户的文件:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
			<input type="file" id="updateCustomer" name="updateCustomer">
			<input type="hidden"  name="fileType" value="update">
		</td>
		<td><input type="button" value="执行" id="customerupdate" style="width:60px;"></td>
	</tr>
	<tr>
		<td>
			<span>请选择需要创建的客户事件的文件:</span>
			<input type="file" id="event" name="event">
			<input type="hidden"  name="fileType" value="event">
		</td> 
		<td><input type="button" value="执行" id="eventUpload" style="width:60px;"></td>
	</tr>
	<tr></tr>
</table>
</form>
</div>
<script type="text/javascript">
$(function(){
	//$("#eventUpload").attr("disabled","true");
});
$("#customerUpload").click(function(){
	var customer = $("#customer").val();
	if(customer != null && customer != ""){
		FileUpload("customer");
	}else{
		alert("请选择要上传的文件");
	}
});

$("#eventUpload").click(function(){
	var event = $("#event").val();
	if(event != null && event != ""){
		FileUpload("event");
	}else{
		alert("请选择要上传的文件");
	}
	
});

function FileUpload(fileId){
	alert(fileId);
	$.ajaxFileUpload({
		type : 'POST',
		async : false,
		url : '<%=request.getContextPath() %>/automation/upload.do',
		secureuri:true,// 安全提交，默认为false
		fileElementId: fileId, // 文件类型的id ['file']
		data:{"type":fileId},
		dataType : "json",
		success:function (data){
			$("#eventUpload").removeAttr("disabled");
			$("#customer").val("");
		},
		error : function(data){
			alert("提示","文件上传失败");
		}
	})
}
</script>
</body>
</html>