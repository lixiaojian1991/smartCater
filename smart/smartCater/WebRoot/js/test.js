$("#login").click(function(){
	reqLogin=function(){
		var req={};
		req["username"]="test";
		req["password"]="test";
		req["op"]="login";
	    cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
	};
	resLogin=function(){
		var retData =cVe.XHR.responseText;
		var retDataObj=JSON.parse(retData);
		var retData=retDataObj[cVe.cEioVeDataId];
		alert(JSON.stringify(retData));
	};
	cVe.startReqByMsgHandle(cVeName,'','','reqLogin','resLogin','csAsc.Handle.UserHandle.handleMsg'); 
});

$("#logout").click(function(){
	reqLogin=function(){
		var req={};
		req["op"]="logout";
	    cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
	};
	resLogin=function(){
		var retData =cVe.XHR.responseText;
		var retDataObj=JSON.parse(retData);
		var retData=retDataObj[cVe.cEioVeDataId];
		alert(JSON.stringify(retData));
	};
	cVe.startReqByMsgHandle(cVeName,'','','reqLogin','resLogin','csAsc.Handle.UserHandle.handleMsg'); 
});

$("#isLogin").click(function(){
	reqLogin=function(){
		var req={};
		req["op"]="isLogin";
	    cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
	};
	resLogin=function(){
		var retData =cVe.XHR.responseText;
		var retDataObj=JSON.parse(retData);
		var retData=retDataObj[cVe.cEioVeDataId];
		alert(JSON.stringify(retData));
	};
	cVe.startReqByMsgHandle(cVeName,'','','reqLogin','resLogin','csAsc.Handle.UserHandle.handleMsg'); 
});

$("#test").click(function(){
	reqTest=function(){
		var req={};
		req["username"]="test";
		req["email"]="905033683@qq.com";
		req["op"]="findPassword";
	    cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
	};

	resTest=function(){
		var retData =cVe.XHR.responseText;
		var retDataObj=JSON.parse(retData);
		var retData=retDataObj[cVe.cEioVeDataId];
	};
	cVe.startReqByMsgHandle(cVeName,'','','reqTest','resTest','csAsc.Handle.UserHandle.handleMsg');
});

/*$("#login").click(function(){
reqLogin=function(){
	var req={};
	req["username"]="fangli_song";
	req["password"]="123";
	req["op"]="login";
    cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
};
resLogin=function(){
	var retData =cVe.XHR.responseText;
	var retDataObj=JSON.parse(retData);
	var retData=retDataObj[cVe.cEioVeDataId];
	alert(JSON.stringify(retData));
};
cVe.startReqByMsgHandle(cVeName,'','','reqLogin','resLogin','csAsc.Handle.UserHandle.handleMsg'); 
});

$("#logout").click(function(){
reqLogin=function(){
	var req={};
	req["op"]="logout";
    cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
};
resLogin=function(){
	var retData =cVe.XHR.responseText;
	var retDataObj=JSON.parse(retData);
	var retData=retDataObj[cVe.cEioVeDataId];
	alert(JSON.stringify(retData));
};
cVe.startReqByMsgHandle(cVeName,'','','reqLogin','resLogin','csAsc.Handle.UserHandle.handleMsg'); 
});

$("#isLogin").click(function(){
reqLogin=function(){
	var req={};
	req["op"]="isLogin";
    cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
};
resLogin=function(){
	var retData =cVe.XHR.responseText;
	var retDataObj=JSON.parse(retData);
	var retData=retDataObj[cVe.cEioVeDataId];
	alert(JSON.stringify(retData));
};
cVe.startReqByMsgHandle(cVeName,'','','reqLogin','resLogin','csAsc.Handle.UserHandle.handleMsg'); 
});

//查看招标书的所有竞标书简况
$("#test").click(function(){
reqTest=function(){
	var req={};
	req["proposalId"]=6;
	req["op"]="queryProById";
    cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
};

resTest=function(){
	var retData =cVe.XHR.responseText;
	var retDataObj=JSON.parse(retData);
	var retData=retDataObj[cVe.cEioVeDataId];
};
cVe.startReqByMsgHandle(cVeName,'','','reqTest','resTest','csAsc.Handle.ProposalHandle.handleMsg');
});*/

/*//查看招标书的所有竞标书简况
$("#test").click(function(){
reqTest=function(){
	var req={};
	req["page"]=1;
	req["pageSize"]=5;
	req["op"]="queryProposal";
    cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
};

resTest=function(){
	var retData =cVe.XHR.responseText;
	var retDataObj=JSON.parse(retData);
	var retData=retDataObj[cVe.cEioVeDataId];
};
cVe.startReqByMsgHandle(cVeName,'','','reqTest','resTest','csAsc.Handle.ProposalHandle.handleMsg');
});*/

/*//查看招标书的所有竞标书简况
$("#test").click(function(){
reqTest=function(){
	var req={};
	req["page"]=1;
	req["pageSize"]=5;
	req["bidId"]=7;
	req["op"]="queryProByBidId";
    cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
};

resTest=function(){
	var retData =cVe.XHR.responseText;
	var retDataObj=JSON.parse(retData);
	var retData=retDataObj[cVe.cEioVeDataId];
};
cVe.startReqByMsgHandle(cVeName,'','','reqTest','resTest','csAsc.Handle.BidHandle.handleMsg');
});*/

/*//根据招标书的id查询其具体信息
$("#test").click(function(){
reqTest=function(){
	var req={};
	req["bidId"]=7;
	req["op"]="queryBidById";
    cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
};

resTest=function(){
	var retData =cVe.XHR.responseText;
	var retDataObj=JSON.parse(retData);
	var retData=retDataObj[cVe.cEioVeDataId];
};
cVe.startReqByMsgHandle(cVeName,'','','reqTest','resTest','csAsc.Handle.BidHandle.handleMsg');
});*/

/*//发布标书需要的数据
$("#test").click(function(){
reqTest=function(){
	var req={};
	req["op"]="pubBidInit";
    cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
};

resTest=function(){
	var retData =cVe.XHR.responseText;
	var retDataObj=JSON.parse(retData);
	var retData=retDataObj[cVe.cEioVeDataId];
};
cVe.startReqByMsgHandle(cVeName,'','','reqTest','resTest','csAsc.Handle.BidHandle.handleMsg');
});*/

/*//标书市场的主页标书查询
$("#test").click(function(){
reqTest=function(){
	var req={};
	req["page"]=1;
	req["pageSize"]=15;
	req["status"]="all";  // all   will on done
	req["sort"]="date";    //date count
	req["keyWord"]="";
	req["op"]="queryBids";
    cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
};

resTest=function(){
	var retData =cVe.XHR.responseText;
	var retDataObj=JSON.parse(retData);
	var retData=retDataObj[cVe.cEioVeDataId];
};
cVe.startReqByMsgHandle(cVeName,'','','reqTest','resTest','csAsc.Handle.BidHandle.handleMsg');
});*/


/*//下食材订单
$("#test").click(function(){
reqTest=function(){
	var req={};
	req["cartIds"]=4;
	req["op"]="newOrder";
    cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
};

resTest=function(){
	var retData =cVe.XHR.responseText;
	var retDataObj=JSON.parse(retData);
	var retData=retDataObj[cVe.cEioVeDataId];
};
cVe.startReqByMsgHandle(cVeName,'','','reqTest','resTest','csAsc.Handle.OrderHandle.handleMsg');
});*/

/*//查询以往订单信息
$("#test").click(function(){
reqTest=function(){
	var req={};
	req["page"]=1;
	req["pageSize"]=15;
	req["op"]="querySelfOrder";
    cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
};

resTest=function(){
	var retData =cVe.XHR.responseText;
	var retDataObj=JSON.parse(retData);
	var retData=retDataObj[cVe.cEioVeDataId];
};
cVe.startReqByMsgHandle(cVeName,'','','reqTest','resTest','csAsc.Handle.OrderHandle.handleMsg');
});*/

/*//订单支付
$("#test").click(function(){
reqTest=function(){
	var req={};
	req["orderId"]=1;
	req["op"]="payOrder";
    cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
};

resTest=function(){
	var retData =cVe.XHR.responseText;
	var retDataObj=JSON.parse(retData);
	var retData=retDataObj[cVe.cEioVeDataId];
};
cVe.startReqByMsgHandle(cVeName,'','','reqTest','resTest','csAsc.Handle.OrderHandle.handleMsg');
});*/

/*//查看购物车里的商品
$("#test").click(function(){
reqTest=function(){
	var req={};
	req["op"]="queryCart";
    cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
};

resTest=function(){
	var retData =cVe.XHR.responseText;
	var retDataObj=JSON.parse(retData);
	var retData=retDataObj[cVe.cEioVeDataId];
};
cVe.startReqByMsgHandle(cVeName,'','','reqTest','resTest','csAsc.Handle.OrderHandle.handleMsg');
});*/


/*//从购物车里移除商品
$("#test").click(function(){
reqTest=function(){
	var req={};
	req["ids"]="2,3";
	req["op"]="delCart";
    cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
};

resTest=function(){
	var retData =cVe.XHR.responseText;
	var retDataObj=JSON.parse(retData);
	var retData=retDataObj[cVe.cEioVeDataId];
};
cVe.startReqByMsgHandle(cVeName,'','','reqTest','resTest','csAsc.Handle.OrderHandle.handleMsg');
});*/

/*//添加商品到购物车
$("#test").click(function(){
reqTest=function(){
	var req={};
	req["productId"]=1;
	req["mount"]=10;
	req["price"]=16;
	req["unit"]="袋";
	req["op"]="addCart";
    cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
};

resTest=function(){
	var retData =cVe.XHR.responseText;
	var retDataObj=JSON.parse(retData);
	var retData=retDataObj[cVe.cEioVeDataId];
};
cVe.startReqByMsgHandle(cVeName,'','','reqTest','resTest','csAsc.Handle.OrderHandle.handleMsg');
});*/

/*找回密码数据测试
* $("#test").click(function(){
reqTest=function(){
	var req={};
	req["username"]="fangli_song";
	req["email"]="905033683@qq.com";
	req["op"]="findPassword";
    cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
};

resTest=function(){
	var retData =cVe.XHR.responseText;
	var retDataObj=JSON.parse(retData);
	var retData=retDataObj[cVe.cEioVeDataId];
};
cVe.startReqByMsgHandle(cVeName,'','','reqTest','resTest','csAsc.Handle.UserHandle.handleMsg');
});*/

/*查询所有在卖商品
$("#test").click(function(){
reqTest=function(){
	var req={};
	req["page"]=1;
	req["pageSize"]=15;
	req["op"]="queryAllProduct";
    cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
};

resTest=function(){
	var retData =cVe.XHR.responseText;
	var retDataObj=JSON.parse(retData);
	var retData=retDataObj[cVe.cEioVeDataId];
};
cVe.startReqByMsgHandle(cVeName,'','','reqTest','resTest','csAsc.Handle.ProductHandle.handleMsg');
});*/

/*查找单个产品的详细信息
$("#test").click(function(){
reqTest=function(){
	var req={};
	req["productId"]=1;
	req["op"]="queryProById";
    cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
};

resTest=function(){
	var retData =cVe.XHR.responseText;
	var retDataObj=JSON.parse(retData);
	var retData=retDataObj[cVe.cEioVeDataId];
};
cVe.startReqByMsgHandle(cVeName,'','','reqTest','resTest','csAsc.Handle.ProductHandle.handleMsg');
});*/

/*查看某个商家自己出售的所有产品
$("#test").click(function(){
reqTest=function(){
	var req={};
	req["companyId"]=1;
	req["page"]=1;
	req["pageSize"]=15;
	req["op"]="querySelfPro";
    cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
};

resTest=function(){
	var retData =cVe.XHR.responseText;
	var retDataObj=JSON.parse(retData);
	var retData=retDataObj[cVe.cEioVeDataId];
};
cVe.startReqByMsgHandle(cVeName,'','','reqTest','resTest','csAsc.Handle.ProductHandle.handleMsg');
});*/



/*注册测试数据
reqTest=function(){
	var req={};
	req["username"]="test";
	req["password"]="password";
	req["email"]="905033683@qq.com";
	req["op"]="register";
    cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
};*/
/*查看以往订单信息
reqTest=function(){
	var req={};
	req["page"]=1;
	req["pageSize"]=5;
	req["op"]="queryOrder";
    cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
};*/
/*支付订单测试数据
reqTest=function(){
	var req={};
	req["orderId"]=6;
	req["op"]="payOrder";
    cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
};*/
/*取消订单测试数据
reqTest=function(){
	var req={};
	req["orderId"]=6;
	req["op"]="cancelOrder";
    cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
};*/
/*销售终端发放菜品测试
reqTest=function(){
	var req={};
	req["orderId"]=4;
	req["foodIds"]=11;
	req["op"]="givenFood";
    cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
};*/
/*查看某个商家所有的上架菜品
reqTest=function(){
	var req={};
	req["page"]=1;
	req["pageSize"]=5;
	req["companyId"]=2;
	req["op"]="querySelfFood";
    cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
};*/
/*查看所有上架菜品数据
reqTest=function(){
	var req={};
	req["page"]=1;
	req["pageSize"]=15;
	req["op"]="queryOnFood";
    cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
};*/
/*下订单测试数据
reqTest=function(){
	var req={};
	req["totalprice"]=26;
	req["tel"]="18814116672";
	var orderDetail=[];
	var detail={};
	detail["foodId"]=11;
	detail["number"]=2;
	detail["price"]=13;
	orderDetail.push(detail);
	req["orderDetail"]=orderDetail;
	req["op"]="newOrder";
    cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
};*/