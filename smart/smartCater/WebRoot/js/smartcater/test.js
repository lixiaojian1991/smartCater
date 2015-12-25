reqTest=function(){
	var req={};
	req["page"]=getQueryString("page");
    if(req["page"]==null){
    	req["page"]=1;
    }
    req["pageSize"]=15;
    req["op"]="queryOnFood";
    cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
};

resTest=function(){
	var retData =cVe.XHR.responseText;
	var retDataObj=JSON.parse(retData);
	var retData=retDataObj[cVe.cEioVeDataId];
	alert(JSON.stringify(retData));
};