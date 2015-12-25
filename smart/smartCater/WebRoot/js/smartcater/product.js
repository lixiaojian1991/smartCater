
//*************product.html********begin*****//
var result;
reqProduct=function(){
	var req={};
	req["page"]=getQueryString("page");
    if(req["page"]==null){
    	req["page"]=1;
    }
    req["pageSize"]=4;
    req["operator"]="queryProduct";
    cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
};

resProduct=function(){
	var retData =cVe.XHR.responseText;
	var retDataObj=JSON.parse(retData);
	var retData=retDataObj[cVe.cEioVeDataId];
	var length=retData["array"].length;
	var content="";
	$.each(retData["array"], function(i, item) {
		content+="<td><div><img src=\""+item.imagePath+"\" onclick='productDetail("+item.id+")'></div><div>"+item.showPrice+"</div><div class='product'>"+item.name+" "+item.briefInfo+
                 "</div><div><a href='"+item.companyId+"'>"+item.companyName+"</a></div></td>";
	    if(i%4==0){
	       content="<tr>"+content;
	    }else if(i%4==3){
	       content=content+"</tr>";
	    }
	});
	if(length%4==1){
		content+="<td></td><td></td><td></td></tr>";
	}else if(length%4==2){
		content+="<td></td><td></td></tr>";
	}else if(length%4==3){
		content+="<td></td></tr>";
	}
	$("#mytable").append(content);
	$("#myPage").append(gene_page_content("foodMarket.html", retData["page"]));
};


function productDetail(id){
	location.href="productDetail.html?productId="+id;
}


//**********product.html***********end***********//


//**************productDetail.html*******begin**********//
var map={"spefication":"包装规格","payWay":"支付方式","transSupport":"交易支持","brand":"品牌","category":"产品类别",
		"import":"是否进口","area":"原产地","saleWay":"售卖方式","material":"原料与配料","class":"等级","special":"特产",
		"garantee":"保质期","batchDate":"生产日期","manufacture":"生产厂家"};
reqProDetail=function(){
	var req={};
	req["productId"]=getQueryString("productId");
    req["operator"]="queryProById";
    cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
};

resProDetail=function(){
	var retData =cVe.XHR.responseText;
	var retDataObj=JSON.parse(retData);
	var retData=retDataObj[cVe.cEioVeDataId];
	var items=new Array("name","briefInfo");
	$("#imagePath").attr("src",retData["product"]["imagePath"]);
	for(var i=0;i<items.length;i++){
		$("#"+items[i]).text(retData["product"][items[i]]);
	}
	var priceShow="";
	$.each(retData["price"], function(i, item) {
		priceShow+=item.minNumber+retData["product"]["sellerUnit"]+"~"+item.maxNumber+retData["product"]["sellerUnit"]+"   ￥" +item.price+";";
	});
	$("#price").text(priceShow);
	var content="";
	for(var prop in map){
		if(retData["product"][prop]!=undefined){
			content+="<div class='item'>"+map[prop]+":<span>"+retData["product"][prop]+"</span></div>";
		}
	}
	$("#content_right").append(content);
};


//****************productDetail.html*********end*********//

$("#addCart").click(function(){
	var temp={};
	temp["productId"]=getQueryString("productId");
	temp["mount"]=$("#number").val();
	if($.cookie("cart")=="null"){
		var products=[];
	}else{
		var products=JSON.parse($.cookie("cart"));
	}
	products.push(temp);
	$.cookie("cart",JSON.stringify(products));
});

