//********food/onFood.html*****begin*****//
var result;
reqOnFood=function(){
	var req={};
	req["page"]=getQueryString("page");
    if(req["page"]==null){
       req["page"]=1;
    }
    req["operator"]="queryOnFood";
	req["isSell"]=getQueryString("isSell");
	if(req["isSell"]==null){
		req["isSell"]=1;
	}
	req["pageSize"]=15;
	cVe.setConn(cServerUri,"POST",true,JSON.stringify(req));
};

resOnFood=function(){
	var retData =cVe.XHR.responseText;
	var retDataObj=JSON.parse(retData);
	var retData=retDataObj[cVe.cEioVeDataId];
	result=retData;
	var items=new Array("id","name","category","price","plannumber","actualnumber");
	$("#mytable").html(gene_table_content(retData["array"], items));
	$("#myPage").html(gene_food_page("food/onFood.html?isSell="+retData["isSell"]+"&", retData["page"]));
};

$("#onFood").click(function(){
	var length=$("input[type='checkbox'][name='checkedItem']:checked").length;
	if(length==0){
		alert("请至少选择一个菜品!");
	}else if(length>1){
		alert("只能选择一个菜品!");
	}else{
		var foodId=$("input[type='checkbox'][name='checkedItem']:checked").val();
		$.each(result["array"], function(i, item) {
			if(item["id"]==foodId){
				$("#name").text(item["name"]);
				$("#category").text(item["category"]);
				$("#price").val("");
				$("#plannumber").val("");
			}
		});
		$("#content").css("display","block");
	}
});

$("#cancelFood").click(function(){
	$("#content").css("display","none");
});

//菜品上架
$("#submitFood").click(function(){
	var length=$("input[type='checkbox'][name='checkedItem']:checked").length;
	if(length==0){
		alert("请至少选择一个菜品!");
	}else if(length>1){
		alert("只能选择一个菜品!");
	}else{
		var foodId=$("input[type='checkbox'][name='checkedItem']:checked").val();
		reqPutFood=function(){
			var req={};
			req["foodId"]=foodId;
			req["plannumber"]=$("#plannumber").val();
			req["price"]=$("#price").val();
			req["isSell"]=1;
			req["operator"]="putOnFood";
			cVe.setConn(cServerUri,"POST",true,JSON.stringify(req));
		};
		resPutFood=function(){
			var retData =cVe.XHR.responseText;
			var retDataObj=JSON.parse(retData);
			var retData=retDataObj[cVe.cEioVeDataId];
			alert(retData["msg"]);
			location.reload();
		};
		cVe.startReqByMsgHandle(cVeName,'','','reqPutFood','resPutFood','csAsc.purchase.Handle.FoodHandle.handleMsg');
	}
});

//菜品下架
$("#downFood").click(function(){
	var length=$("input[type='checkbox'][name='checkedItem']:checked").length;
	if(length==0){
		alert("请至少选择一个菜品!");
	}else if(length>1){
		alert("只能选择一个菜品!");
	}else{
		var foodId=$("input[type='checkbox'][name='checkedItem']:checked").val();
		reqPutFood=function(){
			var req={};
			req["foodId"]=foodId;
			req["operator"]="putDownFood";
			cVe.setConn(cServerUri,"POST",true,JSON.stringify(req));
		};
		resPutFood=function(){
			var retData =cVe.XHR.responseText;
			var retDataObj=JSON.parse(retData);
			var retData=retDataObj[cVe.cEioVeDataId];
			alert(retData["msg"]);
			location.href="food/onFood.html?isSell=0";
		};
		cVe.startReqByMsgHandle(cVeName,'','','reqPutFood','resPutFood','csAsc.purchase.Handle.FoodHandle.handleMsg');
	}
});

$("#notSell").click(function(){
	reqNotSellFood=function(){
		var req={};
		req["page"]=getQueryString("page");
	    if(req["page"]==null){
	       req["page"]=1;
	    }
	    req["operator"]="queryOnFood";
		req["isSell"]=0;
		req["pageSize"]=15;
		cVe.setConn(cServerUri,"POST",true,JSON.stringify(req));
	};

	resNotSellFood=function(){
		var retData =cVe.XHR.responseText;
		var retDataObj=JSON.parse(retData);
		var retData=retDataObj[cVe.cEioVeDataId];
		result=retData;
		var items=new Array("id","name","category","price","plannumber","actualnumber");
		$("#mytable").html(gene_table_content(retData["array"], items));
		$("#myPage").html(gene_food_page("food/onFood.html?isSell="+retData["isSell"]+"&", retData["page"]));
	};
	cVe.startReqByMsgHandle(cVeName,'','','reqNotSellFood','resNotSellFood','csAsc.purchase.Handle.FoodHandle.handleMsg');
});
$("#isSell").click(function(){
	location.href="food/onFood.html?isSell=1";
});