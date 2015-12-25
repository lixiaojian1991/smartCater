
reqInit=function(){
	       var json={};
	       json["page"]=getQueryString("page");
	       if(json["page"]==null){
	          json["page"]=1;
	       }
	       json["pageSize"]=15;
	       json["operator"]="queryUser";
	       cVe.setConn(cServerUri,"POST", true, JSON.stringify(json));
	};
	
resInit=function(){
	var retData =cVe.XHR.responseText;
	var retDataObj=JSON.parse(retData);
	var retData=retDataObj[cVe.cEioVeDataId];
	var items=new Array("username","actualname","email","type","status","createDate","createUser");
	$("#mytable").append(gene_user_content(retData["user"],items));
	$("#myPage").append(gene_page_content("user/user.html",retData["page"]));
};

function gene_user_content(array,items){
	var content="";
	$.each(array, function(i, item) {
		content+="<tr><td><input name='checkedItem' type='checkbox' value='"+item.id+"'></check></td>";
		for(var i=0;i<items.length;i++){
			if(items[i]=="status"){
				content+="<td>"+getStatus(item[items[i]])+"</td>";
			}else if(items[i]=="type"){
				content+="<td>"+getType(item[items[i]])+"</td>";
			}else{
			    content+="<td>"+item[items[i]]+"</td>";
			}
		}
		content+="</tr>";
	});
	return content;
}

function getType(type){
	return type==10?"管理员":type==1?"菜品设计员":"普通员工";
}

function getStatus(status){
	return status=="1"?"正常":"暂停使用";
}

$("#add").click(function(){
	location.href="user/addUser.html";
});

$("#update").click(function(){
	var length=$("input[type='checkbox'][name='checkedItem']:checked").length;
	if(length==0){
		alert("请至少选择一个要修改的用户!");
	}else if(length>1){
		alert("只能选择一个被修改的用户!");
	}else{
		var userId=$("input[type='checkbox'][name='checkedItem']:checked").val();
		location.href="user/updateUser.html?userId="+userId;
	}
});

$("#stop").click(function(){
	 var length=$("input[type='checkbox'][name='checkedItem']:checked").length;
	 if(length==0){
		 alert("请至少选择一个要停用的用户!");
	 }else{
		 var ids=getValueFromCheckBox("checkedItem");
		 if(confirm("是否确认停用该用户？")){
			 reqStopUser=function(){
				 var req={};
				 req["operator"]="stopUser";
				 req["ids"]=ids;
				 req["go"]="stop";
				 cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
			 };
			 resStopUser=function(){
				 var retData =cVe.XHR.responseText;
			 	 var retDataObj=JSON.parse(retData);
			 	 var retData=retDataObj[cVe.cEioVeDataId];
			 	 alert(retData["msg"]);
			 	 window.location.reload();
			 };
			 cVe.startReqByMsgHandle(cVeName,'','','reqStopUser','resStopUser','csAsc.purchase.Handle.UserHandle.handleMsg');
		 }
	 }
});

$("#reuse").click(function(){
	 var length=$("input[type='checkbox'][name='checkedItem']:checked").length;
	 if(length==0){
		 alert("请至少选择一个要恢复使用的用户!");
	 }else{
		 var ids=getValueFromCheckBox("checkedItem");
		 if(confirm("是否确认恢复使用该用户？")){
			 reqStopUser=function(){
				 var req={};
				 req["operator"]="stopUser";
				 req["go"]="use";
				 req["ids"]=ids;
				 cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
			 };
			 resStopUser=function(){
				 var retData =cVe.XHR.responseText;
			 	 var retDataObj=JSON.parse(retData);
			 	 var retData=retDataObj[cVe.cEioVeDataId];
			 	 alert(retData["msg"]);
			 	 window.location.reload();
			 };
			 cVe.startReqByMsgHandle(cVeName,'','','reqStopUser','resStopUser','csAsc.purchase.Handle.UserHandle.handleMsg');
		 }
	 }
});
//新增用户调用的函数
$("#addUser").click(function(){
	reqAddUser=function(){
		var req={};
		var array=new Array("username","password","email","type","actualname");
		for(var i=0;i<array.length;i++){
			req[array[i]]=$("#"+array[i]).val();
		}
		req["operator"]="addUser";
		cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
	};
	resAddUser=function(){
		var retData =cVe.XHR.responseText;
 		var retDataObj=JSON.parse(retData);
 		var retData=retDataObj[cVe.cEioVeDataId];
 		alert(retData["msg"]);
 		location.href="user/user.html";
	};	
	if($("#password").val()!=$("#repassword").val()){
	        alert("二次输入密码不一致!");	
	}else{
		cVe.startReqByMsgHandle(cVeName,'','','reqAddUser','resAddUser','csAsc.purchase.Handle.UserHandle.handleMsg');
	}
});


//****************updateUser.html**************begin******//
function reqInitUser(){
	var req={};
	req["userId"]=getQueryString("userId");
	req["operator"]="queryUserById";
	cVe.setConn(cServerUri,"POST",true,JSON.stringify(req));
}

function resInitUser(){
	var retData =cVe.XHR.responseText;
	var retDataObj=JSON.parse(retData);
	var retData=retDataObj[cVe.cEioVeDataId];
	var array=new Array("username","password","email","actualname","type");
	for(var i=0;i<array.length;i++){
		$("#"+array[i]).val(retData["user"][array[i]]);
	}
	$("#repassword").val(retData["user"]["password"]);
}

$("#updateUser").click(function(){
	reqUpdateUser=function(){
		var req={};
		var array=new Array("username","password","email","type","actualname");
		for(var i=0;i<array.length;i++){
			req[array[i]]=$("#"+array[i]).val();
		}
		req["userId"]=getQueryString("userId");
		req["operator"]="updateUser";
		cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
	};
	
	resUpdateUser=function(){
		var retData =cVe.XHR.responseText;
 		var retDataObj=JSON.parse(retData);
 		var retData=retDataObj[cVe.cEioVeDataId];
 		alert(retData["msg"]);
 		location.href="user/user.html";
	};
	if($("#password").val()!=$("#repassword").val()){
        alert("二次输入密码不一致!");	
	}else{
		cVe.startReqByMsgHandle(cVeName,'','','reqUpdateUser','resUpdateUser','csAsc.purchase.Handle.UserHandle.handleMsg');
	}
});

//*************updateUser.html**************end*****************//