//**********stock.html*******begin********//
var result;
reqStock=function(){
	var req={};
	req["operator"]="queryStock";
	req["page"]=getQueryString("page");
    if(req["page"]==null){
    	req["page"]=1;
    }
    req["pageSize"]=15;
	cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
};

resStock=function(){
	var retData =cVe.XHR.responseText;
	var retDataObj=JSON.parse(retData);
	var retData=retDataObj[cVe.cEioVeDataId];
	result=retData;
	var items=new Array("id","category","name","mount","unit");
	$("#mytable").append(gene_table_content(retData["array"], items));
	$("#myPage").append(gene_page_content("stock/stock.html",retData["page"]));
};

$("#StockIn").click(function(){
	var length=$("input[type='checkbox'][name='checkedItem']:checked").length;
	if(length==0){
		alert("请至少选择一中食材!");
	}else if(length>1){
		alert("只能选择一个食材!");
	}else{
		var id=$("input[type='checkbox'][name='checkedItem']:checked").val();
		var category;
		var materialId;
		$.each(result["array"], function(i, item) {
			if(item.id==id){
				category=item.category;
				materialId=item.materialId;
			}
		});
		location.href="stock/inRecord.html?category="+encodeURIComponent(category)+"&materialId="+materialId;
	}
});

$("#StockOut").click(function(){
	var length=$("input[type='checkbox'][name='checkedItem']:checked").length;
	if(length==0){
		alert("请至少选择一中食材!");
	}else if(length>1){
		alert("只能选择一个食材!");
	}else{
		var id=$("input[type='checkbox'][name='checkedItem']:checked").val();
		var category;
		var materialId;
		$.each(result["array"], function(i, item) {
			if(item.id==id){
				category=item.category;
				materialId=item.materialId;
			}
		});
		location.href="stock/outRecord.html?category="+category+"&materialId="+materialId;
	}
});

$("#StockWaste").click(function(){
	var length=$("input[type='checkbox'][name='checkedItem']:checked").length;
	if(length==0){
		alert("请至少选择一中食材!");
	}else if(length>1){
		alert("只能选择一个食材!");
	}else{
		var id=$("input[type='checkbox'][name='checkedItem']:checked").val();
		var category;
		var materialId;
		$.each(result["array"], function(i, item) {
			if(item.id==id){
				category=item.category;
				materialId=item.materialId;
			}
		});
		location.href="stock/wasteRecord.html?category="+category+"&materialId="+materialId;
	}
});


//*********stock/stockhtml********end*********88//


//*********stock/inRecord.html*****begin*******//

reqStockIn=function(){
	var req={};
	req["operator"]="queryStockIn";
	req["page"]=getQueryString("page");
	req["category"]=decodeURIComponent(getQueryString("category"));
	req["materialId"]=getQueryString("materialId");
    if(req["page"]==null){
    	req["page"]=1;
    }
    req["pageSize"]=15;
	cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
};

resStockIn=function(){
	var retData =cVe.XHR.responseText;
	var retDataObj=JSON.parse(retData);
	var retData=retDataObj[cVe.cEioVeDataId];
	var items=new Array("id","name","mount","unit","price","actualname","inDate","inWay","wayname");
	$("#mytable").append(gene_table_content(retData["array"], items));
	$("#myPage").append(gene_page_content("stock/stock.html",retData["page"]));
};

//**********stock/inRecord.html*******end*****//

//**********stock/outRecord.html****begin******//
reqStockOut=function(){
	var req={};
	req["operator"]="queryStockOut";
	req["page"]=getQueryString("page");
	req["category"]=decodeURIComponent(getQueryString("category"));
	req["materialId"]=getQueryString("materialId");
    if(req["page"]==null){
    	req["page"]=1;
    }
    req["pageSize"]=15;
	cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
};

resStockOut=function(){
	var retData =cVe.XHR.responseText;
	var retDataObj=JSON.parse(retData);
	var retData=retDataObj[cVe.cEioVeDataId];
	var items=new Array("id","name","mount","unit","actualname","outDate","receiver");
	$("#mytable").append(gene_table_content(retData["array"], items));
	$("#myPage").append(gene_page_content("stock/stock.html",retData["page"]));
};

//************stock/outRecord.html********end*//

//********stock/wasteRecord.html*****begin*****//
reqStockWaste=function(){
	var req={};
	req["operator"]="queryStockWaste";
	req["page"]=getQueryString("page");
	req["category"]=decodeURIComponent(getQueryString("category"));
	req["materialId"]=getQueryString("materialId");
    if(req["page"]==null){
    	req["page"]=1;
    }
    req["pageSize"]=15;
	cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
};

resStockWaste=function(){
	var retData =cVe.XHR.responseText;
	var retDataObj=JSON.parse(retData);
	var retData=retDataObj[cVe.cEioVeDataId];
	var items=new Array("id","name","mount","unit","actualname","wasteDate","sender");
	$("#mytable").append(gene_table_content(retData["array"], items));
	$("#myPage").append(gene_page_content("stock/stock.html",retData["page"]));
};
//************stock/wasteRecord.html********end*//

//************stock/stockIn.html********begin*****//

reqStockInInit=function(){
	var req={};
	req["operator"]="stockInInit";
	cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
};

resStockInInit=function(){
	var retData =cVe.XHR.responseText;
	var retDataObj=JSON.parse(retData);
	var retData=retDataObj[cVe.cEioVeDataId];
	result=retData;
	geneSelect("wayname", retData["bids"]);
};

$("#inWay").change(function(){
	if($("#inWay").val()=="标书"){
		geneSelect("wayname", result["bids"]);
	}else if($("#inWay").val()=="订单"){
		geneSelect("wayname",result["materialorder"]);
	}
});

$("#queryMaterial").click(function(){
	reqQueryDetail=function(){
		var req={};
		req["inWay"]=$("#inWay").val();
		req["wayname"]=$("#wayname").val();
		req["operator"]="queryMaterial";
		cVe.setConn(cServerUri,"POST",true,JSON.stringify(req));
	};
	resQueryDetail=function(){
		var retData =cVe.XHR.responseText;
		var retDataObj=JSON.parse(retData);
		var retData=retDataObj[cVe.cEioVeDataId];
		var items=new Array("type","name","mount","unit","price");
		$("#mytable").html(gene_stock_table(retData["array"], items));
	};
	cVe.startReqByMsgHandle(cVeName,'','','reqQueryDetail','resQueryDetail','csAsc.purchase.Handle.StockHandle.handleMsg');
});

function gene_stock_table(array,items){
	var content="";
	$.each(array, function(i, item) {
		content+="<tr>";
		for(var i=0;i<items.length;i++){
			if(item[items[i]]==undefined){
				content+="<td><input type='text'></td>"
			}else{
				content+="<td><input type='text' value='"+item[items[i]]+"'></td>";
			}
		}
		content+="</tr>";
	});
	return content;
}

$("#submitStockIn").click(function(){
	reqSubmit=function(){
		var req={};
		req["inWay"]=$("#inWay").val();
		req["wayId"]=$("#wayname").val();
		req["wayname"]=$("#wayname option:selected").text();
	};
	
	resSubmit=function(){
		
	};
	cVe.startReqByMsgHandle(cVeName,'','','reqSubmit','resSubmit','csAsc.purchase.Handle.StockHandle.handleMsg');
});