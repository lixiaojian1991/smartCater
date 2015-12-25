
//********bid.html********begin**********//
reqBidsInit=function(){
	       var json={};
	       json["page"]=getQueryString("page");
	       if(json["page"]==null){
	          json["page"]=1;
	       }
	       json["pageSize"]=15;
	       json["operator"]="queryBid";
	       cVe.setConn(cServerUri,"POST", true, JSON.stringify(json));
};
resBidsInit=function(){
	var retData =cVe.XHR.responseText;
	var retDataObj=JSON.parse(retData);
	var retData=retDataObj[cVe.cEioVeDataId];
	var items=new Array("id","number","name","type","proposalCount","pubDate","username","beginDate","endDate");
	$("#mytable").append(gene_bid_content(retData["array"],items));
	$("#myPage").append(gene_page_content("bid/bid.html",retData["page"]));
};

function gene_bid_content(array,items){
	var content="";
	$.each(array, function(i, item) {
		var beginDate=item["beginDate"];
		var endDate=item["endDate"];
		content+="<tr><td><input name='checkedItem' type='checkbox' value='"+item.id+"'></check></td>";
		for(var i=1;i<items.length-2;i++){
			 content+="<td>"+item[items[i]]+"</td>";
		}
		content+="<td>"+getStatus(beginDate,endDate)+"</td>"
		content+="</tr>";
	});
	return content;
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

$("#add").click(function(){
	location.href="bid/publicBid.html";
});

$("#detail").click(function(){
	var length=$("input[type='checkbox'][name='checkedItem']:checked").length;
	if(length==0){
		alert("请至少选择一个标书!");
	}else if(length>1){
		alert("只能选择一个标书!");
	}else{
		var bidId=$("input[type='checkbox'][name='checkedItem']:checked").val();
		location.href="bid/bidDetail.html?bidId="+bidId;
	}
});

$("#queryProposal").click(function(){
	var length=$("input[type='checkbox'][name='checkedItem']:checked").length;
	if(length==0){
		alert("请至少选择一个标书!");
	}else if(length>1){
		alert("只能选择一个标书!");
	}else{
		var bidId=$("input[type='checkbox'][name='checkedItem']:checked").val();
		location.href="bid/bidProposal.html?bidId="+bidId;
	}
});

//********************bid.html*******end****************//

//************publicBid.html********begin***********//
var result;
var standRow="<tr><td><input type='text'/></td>"+
             "<td><input type='text'/></td>"+
             "<td><input type='text'/></td>"+
             "<td><button onclick='addStand()'>增加</button><button onclick='delRow(this)'>删除</button></td></tr>";
var materialRow="";
function gene_materialRow(){
  return "<tr><td><select onChange='materialChange(this)'><option value='食材'>食材</option><option value='调料'>调料</option></select></td>"+
                "<td><select>"+gene_option_string_byId(result["materialArray"])+"</select></td><td><input type='text'/></td><td><select>"+gene_option_string_byId(result["unitArray"])+"</select></td>"+
                "<td><button onclick='addMaterial()'>增加</button><button onclick='delRow(this)'>删除</button></td></tr>";
}
function typeChange(type){
     if($(type).val()=="短期标"){
         $("#content1").css("display","block");
         $("#content2").css("display","none");
     }else if($(type).val()=="服务标"){
     	$("#content1").css("display","none");
        $("#content2").css("display","block");
     }
}

function gene_option_string_byId(array){
	var val="";
	$.each(array, function(i, item) {
			  val+="<option value='"+item.id+"'>"+item.name+"</option>";
    });
    return val;
}

function materialChange(td){
      var val="<select>";
      if($(td).val()=='食材'){
         $.each(result["materialArray"], function(i, item) {
          val+="<option value='"+item.id+"'>"+item.name+"</option>";
        });
      }else{
	      $.each(result["relishArray"], function(i, item) {
	          val+="<option value='"+item.id+"'>"+item.name+"</option>";
	      });
      }
	  
	val+="</select>";
   $(td).parent().next().html(val);
}
function addStand(){
    $("#sale_bidstandard").append(standRow);
}

function addMaterial(){
	$("#sale_bidmaterial").append(materialRow);
}

function delRow(td){
    $(td).parent().parent().remove(); 
}

function reqPublicInit(){
	   var json={};
	   json["operator"]="publicInit";
	   cVe.setConn(cServerUri, "POST", true,JSON.stringify(json));
	}
	
function resPublicInit(){
    var retData = cVe.XHR.responseText;
	var retDataObj = JSON.parse(retData);
	var retData = retDataObj[cVe.cEioVeDataId];
	result=retData;
	materialRow=gene_materialRow();
}

$("#publicBid").click(function(){
    var bidPath="";  //附件上传后的文件路径
    var bidName="";  //附件的名字
		$.ajaxFileUpload({
		url : 'servlet/UploadFileServlet',
		secureuri : false,
		fileElementId : 'uploadFile',
		dataType : 'json',
		data : {},
		success : function(data, status) {
			bidPath=data.picUrl;  //上传后的文件路径
			bidName=data.fileName;
			cVe.startReqByMsgHandle(cVeName,'','','reqPublicBid','resPublicBid','csAsc.purchase.Handle.BidHandle.handleMsg');
		},
		error : function(data, status, e) {
			alert('上传出错');
		}
	});
		
  reqPublicBid=function(){
	  var json = {};
	  var array=new Array("number","name","description","type","startDate","finishDate","beginDate","endDate",
	            "contact","tel","sellerInfo","materials");
	  for(var i=0;i<array.length;i++){
	     json[array[i]]=$("#"+array[i]).val();
	  }
	  json["bidPath"]=bidPath;
	  json["bidName"]=bidName;
	  if($("#type").val=="服务标"){
	       json["content"]=$("#content").val();
	  }else{
	       json["content"]="";
	       if($("#sale_bidmaterial tr").length>1){
	          var temp={};
	          var bidmaterial=[];
	          $("#sale_bidmaterial tr").each(function(i,item){
	              if(i>0){
	              var tds=$(this).children();
	              temp["bidId"]=0;
	              temp["type"]=tds.eq(0).children().eq(0).find("option:selected").val();
	              temp["id"]=tds.eq(1).children().eq(0).find("option:selected").val();
	              temp["name"]=tds.eq(1).children().eq(0).find("option:selected").text();
	              temp["mount"]=tds.eq(2).children().eq(0).val();
	              temp["unit"]=tds.eq(3).children().eq(0).find("option:selected").text();
	              bidmaterial[i-1]=temp;
	              temp={};
	              }
	          });
	          json["material"]=bidmaterial;
	       }else{
	          json["material"]=[];
	       }
	  }
	  if($("#sale_bidstandard tr").length>1){
	       var temp={};
	       var standards=[];
	       $("#sale_bidstandard tr").each(function(i,item){
	             if(i>0){
	                var tds=$(this).children();
	                temp["bidId"]=0;
	                temp["number"]=tds.eq(0).children().eq(0).val();
	                temp["name"]=tds.eq(1).children().eq(0).val();
	                temp["weight"]=tds.eq(2).children().eq(0).val();
	                standards[i-1]=temp;
	                temp={};
	             }
	       });
	       json["standards"]=standards;
	  }else{
	      json["standards"]=[];
	  }
	  json["operator"]="saveBid";
	  cVe.setConn(cServerUri,"POST",true,JSON.stringify(json));
  };
  resPublicBid=function(){
	  var retData = cVe.XHR.responseText;
	  var retDataObj = JSON.parse(retData);
	  var retData = retDataObj[cVe.cEioVeDataId];
	  alert(retData["msg"]);
  };
});



//*******************publicBid.html*********end*********//


//*******************bidDetail.html*******begin********//
reqDetailInit=function(){
    var json={};
    json["bidId"]=getQueryString("bidId");
    json["operator"]="searchBidById";
    cVe.setConn(cServerUri,"POST", true, JSON.stringify(json));
};
resDetailInit=function(){
	var retData =cVe.XHR.responseText;
	var retDataObj=JSON.parse(retData);
	var retData=retDataObj[cVe.cEioVeDataId];
	var array=new Array("startDate","pubDate","tel","sellerInfo","finishDate","endDate","number",
	           "beginDate","type","contact","content","username","materials","description","name");
	for(var i=0;i<array.length;i++){
		$("#"+array[i]).text(retData["bid"][array[i]]);
	}
	$("#status").text(getStatus(retData["bid"]["beginDate"],retData["bid"]["endDate"]));
	if(retData["bid"]["type"]=="短期标"){
	    $("#content2").css("display","block");
		var material="";
		$.each(retData["material"], function(i, item) {
			  material+="<tr><td>"+item.type+"</td><td>"+item.name+"</td><td>"+item.mount+"</td><td>"+item.unit+"</td></tr>";
		});
		$("#material").append(material);
	}
	var stands="";
	$.each(retData["stands"], function(i, item) {
			  stands+="<tr><td>"+item.number+"</td><td>"+item.name+"</td><td>"+item.weight+"</td></tr>";
		});
	$("#stands").append(stands);
};

//********************bidDetail.html*****end***************//

//********************bidProposal.html*******begin*********//
function reqProposalInit(){
    var json={};
    json["bidId"]=getQueryString("bidId");
    json["page"]=getQueryString("page");
      if(json["page"]==null){
         json["page"]=1;
      }
    json["pageSize"]=15;
    json["operator"]="queryProposalByBidId";
    cVe.setConn(cServerUri,"POST", true, JSON.stringify(json));
}
function getStatusByWin(isWin){
	return isWin==1?"中标":isWin==0?"未中标":"待定";
}

function getComment(isComment){
	return isComment==1?"已评标":"未评标";
}
function resProposalInit(){
	var retData =cVe.XHR.responseText;
	var retDataObj=JSON.parse(retData);
	var retData=retDataObj[cVe.cEioVeDataId];
	var items=new Array("id","username","name","uploadDate","isComment","totalScore","isWin");
	var content="";
	 $.each(retData["proposalArray"], function(i, item) {
		 content+="<tr><td><input name='checkedItem' type='checkbox' value='"+item.id+"'></check></td>";
			for(var i=1;i<items.length;i++){
				if(items[i]=="isWin"){
					content+="<td>"+getStatusByWin(item[items[i]])+"</td>";
				}else if(items[i]=="isComment"){
					content+="<td>"+getComment(item[items[i]])+"</td>";
				}else{
				    content+="<td>"+item[items[i]]+"</td>";
				}
			}
			content+="</tr>";
	}); 
	$("#mytable").append(content);
	$("#myPage").append(gene_page_content("bid/bidProposal.html",retData["page"]));
}

$("#proposalDetail").click(function(){
	var length=$("input[type='checkbox'][name='checkedItem']:checked").length;
	if(length==0){
		alert("请至少选择一个竞标书!");
	}else if(length>1){
		alert("只能选择一个竞标书!");
	}else{
		var proposalId=$("input[type='checkbox'][name='checkedItem']:checked").val();
		location.href="bid/proposalDetail.html?proposalId="+proposalId;
	}
});

$("#commentProposal").click(function(){
	var length=$("input[type='checkbox'][name='checkedItem']:checked").length;
	if(length==0){
		alert("请至少选择一个竞标书!");
	}else if(length>1){
		alert("只能选择一个竞标书!");
	}else{
		var proposalId=$("input[type='checkbox'][name='checkedItem']:checked").val();
		location.href="bid/commentBid.html?proposalId="+proposalId;
	}
});

$("#winProposal").click(function(){
	var length=$("input[type='checkbox'][name='checkedItem']:checked").length;
	if(length==0){
		alert("请至少选择一个竞标书!");
	}else{
		var ids=getValueFromCheckBox("checkedItem");
		if(confirm("是否确认继续操作？")){
			reqWin=function(){
				 var req={};
				 req["operator"]="decideProposal";
				 req["ids"]=ids;
				 req["go"]="win";
				 cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
			 };
			 resWin=function(){
				 var retData =cVe.XHR.responseText;
			 	 var retDataObj=JSON.parse(retData);
			 	 var retData=retDataObj[cVe.cEioVeDataId];
			 	 alert(retData["msg"]);
			 	 window.location.reload();
			 };
			 cVe.startReqByMsgHandle(cVeName,'','','reqWin','resWin','csAsc.purchase.Handle.BidHandle.handleMsg');
		}
	}
});

$("#loseProposal").click(function(){
	var length=$("input[type='checkbox'][name='checkedItem']:checked").length;
	if(length==0){
		alert("请至少选择一个竞标书!");
	}else{
		var ids=getValueFromCheckBox("checkedItem");
		if(confirm("是否确认继续操作？")){
			reqWin=function(){
				 var req={};
				 req["operator"]="decideProposal";
				 req["ids"]=ids;
				 req["go"]="lose";
				 cVe.setConn(cServerUri,"POST", true, JSON.stringify(req));
			 };
			 resWin=function(){
				 var retData =cVe.XHR.responseText;
			 	 var retDataObj=JSON.parse(retData);
			 	 var retData=retDataObj[cVe.cEioVeDataId];
			 	 alert(retData["msg"]);
			 	 window.location.reload();
			 };
			 cVe.startReqByMsgHandle(cVeName,'','','reqWin','resWin','csAsc.purchase.Handle.BidHandle.handleMsg');
		}
	}
});
//******************bidProposal.html*****end**************//


//**********commentBid.html*******proposalDetail.html****begin**********//

reqCommentInit=function(){
    var json={};
    json["proposalId"]=getQueryString("proposalId");
    json["operator"]="searchProById";
    cVe.setConn(cServerUri,"POST", true, JSON.stringify(json));
};
resCommentInit=function(){
	var retData =cVe.XHR.responseText;
	var retDataObj=JSON.parse(retData);
	var retData=retDataObj[cVe.cEioVeDataId];
	var array=new Array("username","uploadDate","registerdMoney","sellerInfo","tel","contact");
	for(var i=0;i<array.length;i++){
		$("#"+array[i]).text(retData["proposal"][array[i]]);
	}
	array=new Array("proposalPath","successPath","certificatePath");
	for(var i=0;i<array.length;i++){
		$("#"+array[i]).attr("href",$("#"+array[i]).attr("href")+retData["proposal"][array[i]]);
	}
	var stands="";
	if(retData["proposal"]["isComment"]==0){
		$("#status").text("待评标");
		$.each(retData["stands"], function(i, item) {
			  stands+="<tr><td>"+item.number+"</td><td>"+item.name+"</td><td>"+item.weight+"%</td><td><input type='text'></td></tr>";
		});
	}else{
	    $("#status").text("已评标");
	    $("#score").text(retData["proposal"]["totalScore"]);
	    $.each(retData["score"], function(i, item) {
			  stands+="<tr><td>"+item.number+"</td><td>"+item.name+"</td><td>"+item.weight+"%</td><td>"+item.score+"</td></tr>";
		});
	    $("#commentPro").css("display","none");
	}
	$("#mytable").append(stands);
};

$("#commentPro").click(function(){
	  reqComment=function(){
	  	   var json={};
	       var proposalId=getQueryString("proposalId");
	       json["proposalId"]=proposalId;
	       var temp={};
	       var array=[];
	        $("#mytable tr").each(function(i,item){
		              var tds=$(this).children();
		              temp["proposalId"]=proposalId;
		              temp["number"]=tds.eq(0).html();
		              temp["score"]=tds.eq(3).children().eq(0).val();
		              array[i]=temp;
		              temp={};
		     });
		     json["comment"]=array;
	       json["operator"]="commentProposal";
	       alert(JSON.stringify(json));
	       cVe.setConn(cServerUri,"POST", true, JSON.stringify(json));
	  };
	  
	  resComment=function(){
	      var retData = cVe.XHR.responseText;
		  var retDataObj = JSON.parse(retData);
		  var retData = retDataObj[cVe.cEioVeDataId];
		  alert(retData["msg"]);
	  };
	  cVe.startReqByMsgHandle(cVeName,'','','reqComment','resComment','csAsc.purchase.Handle.BidHandle.handleMsg');
	});

