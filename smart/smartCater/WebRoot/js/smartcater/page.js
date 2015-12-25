
function gene_page_content(url,obj){
	var content="";
	var page=obj["page"];
	var totalPage=obj["totalPage"];
	content="<li><a href=\""+url+"?page="+page+"\">"+page+"</a></li>";
		if(page>1){
		    content="<li><a href=\""+url+"?page=1\">首页</a></li><li><a href=\""+url+"?page="+(page-1)+"\">&laquo;</a></li>"+content;
		}else if(page==1){
			content="<li><a href=\""+url+"?page=1\">首页</a></li>"+content;
		}
		if(page<totalPage){
		   content+="<li><a href=\""+url+"?page="+(page+1)+"\">&raquo;</a></li><li><a href=\""+url+"?page="+totalPage+"\">尾页</a></li>";
		}else if(page==totalPage){
			content+="<li><a href=\""+url+"?page="+totalPage+"\">尾页</a></li>";
		}
		return content;
}


function gene_food_page(url,obj){
	var content="";
	var page=obj["page"];
	var totalPage=obj["totalPage"];
	content="<li><a href=\""+url+"page="+page+"\">"+page+"</a></li>";
		if(page>1){
		    content="<li><a href=\""+url+"page=1\">首页</a></li><li><a href=\""+url+"page="+(page-1)+"\">&laquo;</a></li>"+content;
		}else if(page==1){
			content="<li><a href=\""+url+"page=1\">首页</a></li>"+content;
		}
		if(page<totalPage){
		   content+="<li><a href=\""+url+"page="+(page+1)+"\">&raquo;</a></li><li><a href=\""+url+"page="+totalPage+"\">尾页</a></li>";
		}else if(page==totalPage){
			content+="<li><a href=\""+url+"page="+totalPage+"\">尾页</a></li>";
		}
		return content;
}



function gene_table_content(array,items){
	var content="";
	$.each(array, function(i, item) {
		content+="<tr><td><input name='checkedItem' type='checkbox' value='"+item.id+"'></check></td>";
		for(var i=1;i<items.length;i++){
			content+="<td>"+item[items[i]]+"</td>";
		}
		content+="</tr>";
	});
	return content;
}

function getQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return r[2];
	return null;
}

function getValueFromCheckBox(name){ 
	var obj=document.getElementsByName(name); 
	var s=''; 
	for(var i=0; i<obj.length; i++){ 
		if(obj[i].checked) s+=obj[i].value+',';
	} 
	//那么现在来检测s的值就知道选中的复选框的值了 
	return s.substring(0,s.length-1);
}

//下面这个函数就是jsonarray生成select的函数
function geneSelect(id,array){
	var val="";
	$.each(array, function(i, item) {
          val+="<option value='"+item.id+"'>"+item.name+"</option>";
    });
	$("#"+id).html(val);
}