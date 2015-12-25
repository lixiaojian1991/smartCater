var result;

//********proposal.html********begin**********//
reqProposalInit=function(){
	       var json={};
	       json["page"]=getQueryString("page");
	       if(json["page"]==null){
	          json["page"]=1;
	       }
	       json["pageSize"]=15;
	       json["operator"]="queryProposal";
	       cVe.setConn(cServerUri,"POST", true, JSON.stringify(json));
};
resProposalInit=function(){
	var retData =cVe.XHR.responseText;
	var retDataObj=JSON.parse(retData);
	var retData=retDataObj[cVe.cEioVeDataId];
	result=retData;
	var items=new Array("id","name","beginDate","endDate","username","uploadDate","isComment","totalScore","isWin","isGive");
	$("#mytable").append(gene_proposal_content(retData["array"],items));
	$("#myPage").append(gene_page_content("proposal/proposal.html",retData["page"]));
};

function gene_proposal_content(array,items){
	var content="";
	$.each(array, function(i, item) {
		var beginDate=item["beginDate"];
		var endDate=item["endDate"];
		content+="<tr><td><input name='checkedItem' type='checkbox' value='"+item.id+"'></check></td>"+
		         "<td>"+item["name"]+"</td>"+"<td>"+getStatus(item["beginDate"],item["endDate"])+"</td>";
		for(var i=4;i<items.length;i++){
			 if(items[i]=="isWin"){
				 content+="<td>"+getStatusByWin(item[items[i]])+"</td>";
			 }else if(items[i]=="isComment"){
				 content+="<td>"+getComment(item[items[i]])+"</td>";
			 }else if(items[i]=="isGive"){
				 content+="<td>"+getGive(item[items[i]])+"</td>";
			 }else{
				 content+="<td>"+item[items[i]]+"</td>";
			 }
		}
		content+="</tr>";
	});
	return content;
}


function getGive(isGive){
	return isGive==1?"放弃竞标":"正常";
}
function getStatus(beginDate,endDate){
    var now=new Date().toLocaleDateString().replace("/","-").replace("/","-");
    if(beginDate>now){
          return "未开始";
    }else if(endDate<now){
          return "已结束";
    }else{
          return "进行中";
    }
}

function getStatusByWin(isWin){
	return isWin==1?"中标":isWin==0?"未中标":"待定";
}

function getComment(isComment){
	return isComment==1?"已评标":"未评标";
}

$("#queryBid").click(function(){
	var length=$("input[type='checkbox'][name='checkedItem']:checked").length;
	if(length==0){
		alert("请至少选择一个标书!");
	}else if(length>1){
		alert("只能选择一个标书!");
	}else{
		var proposalId=$("input[type='checkbox'][name='checkedItem']:checked").val();
		var bidId;
		$.each(result["array"], function(i, item) {
			if(item.id==proposalId){
				bidId=item.bidsId;
			}
		});
		location.href="bid/bidDetail.html?bidId="+bidId;
	}
});

$("#giveup").click(function(){
	var length=$("input[type='checkbox'][name='checkedItem']:checked").length;
	if(length==0){
		alert("请至少选择一个标书!");
	}else{
		var ids=getValueFromCheckBox("checkedItem");
		if(confirm("是否确认放弃竞标？")){
			reqGiveup=function(){
				 var req={};
				 req["operator"]="giveupProposal";
				 req["ids"]=ids;
				 req["go"]="giveup";
				 cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
			 };
			 resGiveup=function(){
				 var retData =cVe.XHR.responseText;
			 	 var retDataObj=JSON.parse(retData);
			 	 var retData=retDataObj[cVe.cEioVeDataId];
			 	 alert(retData["msg"]);
			 	 window.location.reload();
			 };
			 cVe.startReqByMsgHandle(cVeName,'','','reqGiveup','resGiveup','csAsc.purchase.Handle.ProposalHandle.handleMsg');
		}
	}
});

$("#updatePro").click(function(){  //修改竞标书，，，后面记得要加上判断条件
	var length=$("input[type='checkbox'][name='checkedItem']:checked").length;
	if(length==0){
		alert("请至少选择一个标书!");
	}else if(length>1){
		alert("只能选择一个标书!");
	}else{
		var proposalId=$("input[type='checkbox'][name='checkedItem']:checked").val();
		location.href="proposal/updateProposal.html?proposalId="+proposalId;
	}
});

$("#neverGiveup").click(function(){
	var length=$("input[type='checkbox'][name='checkedItem']:checked").length;
	if(length==0){
		alert("请至少选择一个标书!");
	}else{
		var ids=getValueFromCheckBox("checkedItem");
		if(confirm("是否确认重新竞标？")){
			reqGiveup=function(){
				 var req={};
				 req["operator"]="giveupProposal";
				 req["ids"]=ids;
				 req["go"]="neverGiveUp";
				 cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
			 };
			 resGiveup=function(){
				 var retData =cVe.XHR.responseText;
			 	 var retDataObj=JSON.parse(retData);
			 	 var retData=retDataObj[cVe.cEioVeDataId];
			 	 alert(retData["msg"]);
			 	 window.location.reload();
			 };
			 cVe.startReqByMsgHandle(cVeName,'','','reqGiveup','resGiveup','csAsc.purchase.Handle.ProposalHandle.handleMsg');
		}
	}
});

$("#deletePro").click(function(){
	var length=$("input[type='checkbox'][name='checkedItem']:checked").length;
	if(length==0){
		alert("请至少选择一个标书!");
	}else{
		var ids=getValueFromCheckBox("checkedItem");
		if(confirm("是否确认删除竞标书？")){
			reqDeletePro=function(){
				 var req={};
				 req["operator"]="deletePro";
				 req["ids"]=ids;
				 cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
			 };
			 resDeletePro=function(){
				 var retData =cVe.XHR.responseText;
			 	 var retDataObj=JSON.parse(retData);
			 	 var retData=retDataObj[cVe.cEioVeDataId];
			 	 alert(retData["msg"]);
			 	 window.location.reload();
			 };
			 cVe.startReqByMsgHandle(cVeName,'','','reqDeletePro','resDeletePro','csAsc.purchase.Handle.ProposalHandle.handleMsg');
		}
	}
});


//**********proposal.html*******end******//



//**********updateProposal.html*******begin********//
reqUpdateInit=function(){
	var req={};
	req["proposalId"]=getQueryString("proposalId");
	req["operator"]="queryProById";
	cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
};

resUpdateInit=function(){
	 var retData =cVe.XHR.responseText;
	 var retDataObj=JSON.parse(retData);
	 var retData=retDataObj[cVe.cEioVeDataId];
	 var array=new Array("registerdMoney","tel","contact","sellerInfo");
	 for(var i=0;i<array.length;i++){
		$("#"+array[i]).val(retData["pro"][array[i]]);
	 }
	 var pathArray=new Array("proposalPath","successPath","certificatePath");
	 for(var i=0;i<pathArray.length;i++){
		 $("#old"+pathArray[i]).attr("href",retData["pro"][pathArray[i]]);
		 if(retData["pro"][pathArray[i]]!=""){
		     $("#old"+pathArray[i]).text("原文件下载");
		 }else{
			 $("#old"+pathArray[i]).text("无原文件");
		 }
	 }
	 
};

$("#updateProposal").click(function(){
	reqUpdatePro=function(){
		var req={};
		req["proposalId"]=getQueryString("proposalId");
		req["operator"]="updatePro";
		var array=new Array("registerdMoney","tel","contact","sellerInfo");
	    for(var i=0;i<array.length;i++){
		    req[array[i]]=$("#"+array[i]).val();
	    }
	    var pathArray=new Array("proposalPath","successPath","certificatePath");
		for(var i=0;i<pathArray.length;i++){
			 if($("#"+pathArray[i]).val()!=""){
				 req[pathArray[i]]=$("#"+pathArray[i]).val();
			 }else{
				 req[pathArray[i]]=$("#old"+pathArray[i]).attr("href");
			 }
			 
		}
		cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
	};
	resUpdatePro=function(){
		var retData =cVe.XHR.responseText;
		var retDataObj=JSON.parse(retData);
		var retData=retDataObj[cVe.cEioVeDataId];
		alert(retData["msg"]);
		location.href="proposal/proposal.html";
	};
	cVe.startReqByMsgHandle(cVeName,'','','reqUpdatePro','resUpdatePro','csAsc.purchase.Handle.ProposalHandle.handleMsg');
});


//*************updateProposal.html******end*************//