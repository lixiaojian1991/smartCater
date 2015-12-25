reqCart=function(){
	var req={};
	var products;
	if($.cookie("cart")=="null"){
		products=[];
	}else{
		products=JSON.parse($.cookie("cart"));
	}
	req["products"]=products;
	req["operator"]="queryCart";
	cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
};

resCart=function(){
	var retData =cVe.XHR.responseText;
	var retDataObj=JSON.parse(retData);
	var retData=retDataObj[cVe.cEioVeDataId];
	var content="";
	$.each(retData["products"], function(i, item) {
		content+="<tr><td>"+(i+1)+"</td><td>"+item.name+" "+item.briefInfo+"</td><td><img src='"+item.imagePath+"' onclick='productDetail("+item.id+")'></td><td>"+item.price
		         +"元/"+item.sellerUnit+"</td><td>"+getMount(item.id)+item.sellerUnit+"</td><td>"+item.price*getMount(item.id)+"元</td></tr>";
	});
	$("#mytable").html(content);
};

function productDetail(id){
	location.href="productDetail.html?productId="+id;
}
function getMount(productId){
	var mount;
	var products=JSON.parse($.cookie("cart"));
	$.each(products,function(i,item){
		if(item.productId==productId){
			mount= item.mount;
		}
	});
	return mount;
}