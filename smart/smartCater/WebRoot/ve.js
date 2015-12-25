/*
document.write('<script type="text/javascript" src="js/jquery/jquery.min.js"></script>');
document.write('<script src="js/jqGrid/grid.locale-cn.js" type="text/javascript"></script>');
document.write('<script src="js/jqGrid/jquery.jqGrid.min.js" type="text/javascript"></script>');
document.write('<script type="text/javascript" src="js/dtree.js"></script>');*/


(function ()
{  
 window['EIO'] = {}  //注册命名空间 '@' 到window对象上
 
 ////////////////////////////组件初始化（目前，尚不可用）======================
 //初始化表格
  
 ////////////////////////////客户端消息收发器==================
 function JsVeMsgDispatcher()
 {
  var father = this; //私有成员方法中访问属性时，使用”father.属性名"的方式

  this.serverUri;
  this.isAsync=true;
  this.method="POST";
  this.respFunc;
  this.sendData;
  this.msgType;
  this.msgName;
  this.XHR;
 	
  this.reqMsgHead;
  this.cEioVeDataId="EIOVEDATA";
  this.cVeNameId="VeName";
  this.cMsgTypeId="VEReqMsgType";
  this.cMsgNameId="VEReqMsgName";
  	  
  //alert("进入ve");
  this.setConn=function(serverUri,method,isAsyn,sendData)
   {//alert("从消息处理函数 进入ve的setConn");
 	this.serverUri=serverUri;
 	this.isAsync=isAsyn;
 	this.method=method;
 	
 	/*if (dataformat==1) this.sendData=jsonToGet(sendData)
 	else this.sendData=sendData; */
 	this.sendData=this.reqMsgHead + sendData;
 	this.XHR=createXHR();
 	//this.respFunc=respFunc; 
   };
  
   //this.test=function test(){alert("Test方法");};
  
   this.sendMsg = function()
   {
 	
    if (!this.XHR) return 1;
 	if (this.serverUri==null) return 2;
 		         
 	this.XHR.open(this.method,this.serverUri, this.isAsync); 
 	this.XHR.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
 	if (this.respFunc!="")
 	  this.XHR.onreadystatechange= function(){getServerResp(this.respFunc)}; 
 	 //XHR.onreadystatechange = new Function(respFunc); //设置事件触发器
 	 //y语法：xmlhttp.onreadystatechange= new Function("FuncName(param)");
 	 //xmlhttp.onreadystatechange= function(){FuncName(param)};// para为变量名的字面值   
 	//alert("发送到服务端的数据(即将执行send): "+this.sendData);
 	this.XHR.send(this.sendData); //发送请求
 	return 0;//成功，返回
   };     

   this.startReqByMsgMap = function(veName,msgType, msgName,msgConfig)
   {//消息处理的总人口（通过消息映射表启动消息请求）  
 	var reqFunc =  msgConfig[msgType][msgName].ReqFunc; //获取请求服务端的函数
    var resFunc =  msgConfig[msgType][msgName].ResFunc; //获取处理服务端的回复消息的函数
    var msgHandle = msgConfig[msgType][msgName].MsgHandle; //获取信息处理器
        
    this.startReqByMsgHandle(veName,msgType,msgName,reqFunc, resFunc,msgHandle);
    
    };
    
   this.startReqByMsgHandle = function(veName,msgType,msgName,reqFunc, resFunc,msgHandle)
   {//消息处理的总人口（不通过消息映射表启动消息请求）  
	
	this.reqMsgHead = genMsgHead(veName,msgType,msgName,msgHandle) ;
	    
	if ( resFunc=="") resFunc=null;//不指定相应处理函数
	this.msgType=msgType;
	this.msgName=msgName;
	this.respFunc = resFunc;
	
	      //eval(reqFunc+"("+resFunc+")"); //此种方式，传入reqFunc的参数为对象，表示原来的字符串
	eval(reqFunc+"()"); //调用具体的消息处理函数，以生成信息数据，设置个性化的通信方式
	this.sendMsg();//发送消息 
   };
   
   function genMsgHead(cVeName,msgType,msgName,msgHandle)
   {//生成VE的标准的消息头	
    return father.cVeNameId+"="+cVeName+"&"+ father.cMsgTypeId+"="+msgType+"&"+father.cMsgNameId+"="+msgName 
       +"&MsgHandle="+msgHandle +"&"+ father.cEioVeDataId +"=" ;
   }

  function createXHR() 
  {//创建一XML 个HTTP请求对象并返回其
   try
   {//检查能否用activexobject
    var XHR = new ActiveXObject("msxml2.XMLHTTP");
   }catch(e1)
    {
 	try
 	{//检查能否用activexobject
 	 XHR = new ActiveXObject("microsoft.XMLHTTP");
 	} catch(e2)
 	  {
 	   try
 	   {//检查能否用本地javascript对象
 		XHR = new XMLHttpRequest();
 	   }catch(e3)
 	    { //创建失败
 		 XHR = false;
 		}//catch(e3)
       }//catch(e2)
      }//catch(e1)
   return XHR;
  } //XHR  
 	 
 function getServerResp(respFunc)
 {//事件响应受理程序	 
	 
  if (father.XHR.readyState == 4)
   if( father.XHR.status == 200)
   {//alert("进入事件响应受理程序getServerResp");
    eval(father.respFunc+"()");
   }
 }//getServerResp(respFunc)
 	 
}//JsVeMsgDispatcher()
 //function ve() { alert(200);}
 //var ve = 1000;
 
 /////////////////////交互组件的抽注函数/////////////////////////
function ExtInj()
{
 
 this.injectSelect = function injSelect(vcId, selData)
 {//数据注入Vc:用数组数据selData刷新SELECT元素selId
  //selData的格式,即数组：[s1,s2,...]
 			 
  var str1="";
  for (var i=0; i<selData.length;i++)
  {
   str1 += '<option value=' +'"'+ selData[i]+'">'+selData[i] + '</option>'; 
  } 	 
 	  
  document.getElementById(vcId).innerHTML = str1;
  //document.getElementById(vcId).location.reload();
 	
 }//this.injectSelect
 
 this.injectGrid=function(id,data)
 {
  var divID='serverReturn';
// window[divID].injectData({
//			gData: retDataObj[cEioVeDataId],
//			records: 1003,
//			total: 12,
//			page:1
//			});
 window[id].injectData(data);
}//injectGrid()	

//初始化表格，目前作为通用函数，后期应进行整合
	this.initGrid = function(divId, buffer, options) {
		//为表格主体、翻页器与全选器编写通用id号
		var gridId = "g_" + divId;
		var pagerId = "p_" + divId;
		var selectAllId = "a_" + divId;
		//在div里插入表格主体、翻页器与全选器
		if (typeof options.multiselect != 'undefined' && options.multiselect == false) {
			document.getElementById(divId).innerHTML = '<table id="' + gridId + '"></table>' + '<div id="' + pagerId + '"></div>';
		} else {
			document.getElementById(divId).innerHTML = '<table id="' + gridId + '"></table>' + '<div id="' + pagerId + '"></div>' + '<div><span><input type="checkbox" id="' + selectAllId + '">选择全部</span></div>';
		}
		var gridTable = new Grid2('#' + gridId, buffer, options);
		//把grid对象全局化，以便于外部调用
		window[divId] = gridTable;
		//设置表格格式
		gridTable.setGridCols({
			colNames: options.colNames,
			widths: options.widths
		});
	}//initGrid()
 
 this.extractCheckbox = function extCheckbox(myId)
 {//'EIO-HTML-ID-rank'
  //复选框的数据抽取函数，将区域ID值为myId的区域内的复选框的当前选择的值读出返回到一个数组中
  var eioDiv = document.getElementById(myId);
  var boxes = eioDiv.getElementsByTagName('input');
  var retVal = [];
  for (var i = 0; i < boxes.length; i++)
  {
   if (boxes[i].type == 'checkbox') 
     if (boxes[i].checked) retVal.push(boxes[i].value);
 		
  }
   alert(retVal);
   return retVal;
 }//this.extractCheckbox
 
 this.extractGrid=function(id)
 {
 	var gridTable=window[id];
 	return gridTable.extractGrid();
 }//this.extractGrid
 
 this.extractElements = function extElem(id)
 {
 	var divs=document.getElementsByTagName('div');
 	var retObj={};
 	//收集所有写在div里的checkbox与radio
 	for(var i=0;i<divs.length;i++)
 	{
 		var div=divs[i];
 		var childs=div.childNodes;
 		for(var j=0;j<childs.length;j++)
 		{
 			var child=childs[j];
 			if (child.type=='checkbox'&&child.checked)
 			{
 				if (typeof retObj[div.id]=='undefined')
 				{
 				   retObj[div.id]=[];
 				   retObj[div.id].push(child.value);
 				}
 				else
 				{
 					retObj[div.id].push(child.value);
 				}
 			}
 			//由于radio是多选一的关系，这里只设为元素！
 			else if(child.type=='radio'&&child.checked)
 			{
 				retObj[div.id]=child.value;
 			}
 		}
 	}
 	//收集select
 	var selects=document.getElementsByTagName('select');
 	for(var i=0;i<selects.length;i++)
 	{
 		var select=selects[i];
 		retObj[select.id]=select.options[select.selectedIndex].value;
 	}
 	//收集其他input，现在只有text
 	var inputs=document.getElementsByTagName('input');
 	for(var i=0;i<inputs.length;i++)
 	{
 		var input=inputs[i];
 		if (input.type=='text') {
 			retObj[input.id]=input.value;
 		}
 	}
 	//调试用
 	//alert(JSON.stringify(retObj).toString());
 	return JSON.stringify(retObj).toString();
 }//this.extractElements

 this.injectElement = function injElement(id, data)
 {//HTML输入输出元素的数据注入， id==要注入数据的元素的id属性值， val--注入的数据值
  //val的形式：单值型元素： {id:值}；集合型值的元素：{id:[值1,z值2, ...]}
		var obj = document.getElementById(id);
		if (typeof obj == 'undefined')
		{
			//id不存在
			return;
		}
		//文本框
		if (obj.type == 'text')
		{	//alert(data);
			obj.value = data;
		} 
		else if (obj.getElementsByTagName('input').length > 0)
		{
			var inputs = obj.getElementsByTagName('input');
			var type = inputs[0].type;
			//checkbox与radio的情况
			if (type == 'checkbox' || type == 'radio')
			{
				//checkbox比较特殊，是从页面已存在中的value里注入？
				//alert("value[data]:"+values[data]);
				var values ={};
				for (var i = 0; i < inputs.length; i++)
				{
					var input = inputs[i];
					values[input.value] = i;
				}
				if (type == 'checkbox')
				{
					for (var i = 0; i < data.length; i++)
					{
						if (typeof (values[data[i]]) != "undefined")
						{
							inputs[values[data[i]]].checked = true;
						}
					}
				} 
				else if (type == 'radio')
				{
					if (typeof (values[data]) != "undefined")
					{	
						//alert("value[data]:"+values[data]);
						inputs[values[data]].checked = true;
					}
				}
			}

		}
		//select
		else if (obj.type == 'select-one' || obj.type == 'select-multiple')
		{
			//默认注入的是数组
			var str1 = "";
			for (var i = 0; i < data.length; i++)
			{
				str1 += '<option value=' + '"' + data[i] + '">' + data[i]
						+ '</option > ';
			}
			obj.innerHTML = str1;
		}
	}
 
 ///////////////////////树形组件抽注///////////////////////////
 this.initTree = function initTreeX(TreeName, RootName)
 {   
 	//alert(TreeName);
 	Tree = new dTree(TreeName);
 	window[TreeName]= Tree;
 	Tree.add(0,-1,RootName);
 	return Tree;
 	
 }

 this.injectTree = function injectTreeX(divID,data, Tree)
 {   
    
     //alert(Number(data[0][0]));
 	for(var i=0;i<data.length;i++)
 	{ 
 		var id=Number(data[i][0]);
 		var parentid=Number(data[i][2]);
 		Tree.add(id,parentid,data[i][1],'ADRS00.html');
 	}
 	document.getElementById(divID).innerHTML = Tree;

 }
 
} //ExtInj()

///////////////////Buffer///////////////////////
function Buffer()
{
	//定义数据源接口
	function Source(){
		//获取数据源的总大小
        this.getSize = arguments[0] || function() {
            return -1;
        }
        //从数据源获取数据
        //@param {number} start 起始位置
        //@param {number} num 获取的数据量
        this.getData = arguments[1] || function(start, num){
        	return undefined;
        }
        //更新数据源数据
        //@param {number} start 起始位置
        //@param {number} end 结束位置
        //@param {string} str 需要更新到数据源的数据
        //@result {boolean} 写入数据是否成功 todo
        //该方法内部应采用会阻塞当前线程 todo
        this.setData = arguments[2] || function(start,end,str){
        	return true; //todo
        }
	}
	//事件分发类
	function EventEmitter(){
		var handles={};
		function addEventListener(type,event,listener){
			if(!handles[event]){
				handles[event]=new Array();
			}
			var obj = {};
			obj.type=type;
			obj.listener = listener;
			handles[event].push(obj);
		}
		this.on = function(event,listener){
			addEventListener("on",event,listener);
		}
		this.once = function(event,listener){
			addEventListener("once",event,listener);
		}
		this.emit = function(event){
			var listeners = handles[event];
			if(listeners){
				for(var i=0;i<listeners.length;i++){
					var obj = listeners[i];
					obj.listener.apply(null,Array.prototype.slice.call(arguments,1));
					if(obj.type=="once"){
						listeners.splice(i,1);
					}
				}
			}
		}
	}

	var source = undefined,
		buffer = "",
		bufferStart = undefined,
		eventEmitter = undefined;

	var dirtyStart = -1,
		dirtyCharEnd = -1,
		dirtyBufferEnd = -1;

	function init(){
		eventEmitter = new EventEmitter();
	}

	//输出当前缓冲区状态
    function debug(){
        console.log(">------"+new Date().toString()+"------<");
        console.dir(source);
        console.log("buffer:"+buffer);
        console.log("bufferStart:"+bufferStart);
        console.log("dirtyStart:"+dirtyStart+" dirtyCharEnd:"+dirtyCharEnd+" dirtyBufferEnd:"+dirtyBufferEnd);
        console.log("-------------------------------------");
    }

    //检查参数合法性
    //@param {number} start 起始位置
    //@param {number} num 需要获取数据的大小
    function checkParamters(start,num){
        if (source === undefined) {
            alert("尚未设置源");
            return false;
        }
        if(parseInt(start)!=start){
            alert("参数start必须为整数");
            return false;
        }
        if (start < 0) {
            alert("参数start必须为正数");
            return false;
        }
        if(parseInt(num)!=num){
            alert("参数num必须为整数");
            return false;
        }
        if (num < 0) {
            alert("参数num必须为正数");
            return false;
        }
        return true;
    }

    //根据块位置更新dirtyStart和dirtyEnd
    function updateDirty(s){
        if(s<=-1)
            return;
        if(dirtyStart!==-1){
            dirtyStart = Math.min(s,dirtyStart);
        }else{
            dirtyStart = s;
            dirtyBufferEnd = buffer.split("/;").length + bufferStart;
            dirtyCharEnd = buffer.length + bufferStart;
        }
    }

    this.addReadListener = function(fn){
    	eventEmitter.once("read",fn);
    }

    this.setSource = function(a,b,c){
        source = new Source(a,b,c);
    }

    //以字符为单位读
    //@param {number} 起始字符位置
    //@param {number} 读取的字符个数
    //@return {string} 获取到的数据 undefined表示未获取到数据
    this.readChar = function(start, num) {
    	if(!checkParamters(start,num)){
    		return;
    	}
        if(num === 0){
            return;
        }

        var result = undefined;
		var size = buffer.length;

        if (bufferStart === undefined || !(bufferStart <= start && start < bufferStart + size)) {
            //一开始就要重新获取数据
            source.getData(start, num);
        } else {
            var offset = start-bufferStart;

            if( num <= size-offset ){ //可以全部使用缓冲区数据
                result = buffer.substring(offset,offset+num);
            }else{
                result = buffer.substring(offset);
                //除了缓冲区数据外，还需要从源获取数据
                source.getData(bufferStart+size, num-size+offset);
            }
        }
        debug();
        return result;
    }

    //以数据块为单位读
    //@param {number} 起始数据块位置
    //@param {number} 读取的数据块数
    //@param {string} 获取到的数据 undefined表示未获取到数据
    this.readBuf = function(start,num){
    	if(!checkParamters(start,num)){
    		return;
    	}
        if (num === 0) {
            return;
        }

        var result = undefined; //保存最终需要得到的字符串
        var size = buffer.split("/;").length; //当前buffer里面块的数目

        if (bufferStart === undefined || !(bufferStart <= start && start < bufferStart + size)) {
            //一开始就要重新获取数据
            source.getData(start, num);
        } else {
            var offset = start-bufferStart;

            if( num <= size-offset ){ //可以全部使用缓冲区数据
                result = buffer.split("/;").slice(offset,offset+num).join("/;");
            }else{ //除了缓冲区数据外，还需要从源获取数据
                result = buffer.split("/;").slice(offset).join("/;");
                source.getData(bufferStart+size, num-size+offset);
            }
        }
        debug();
        return result;
    }

    //以数据块为单位读
    //与readBuf相同，返回数组格式的数据
    this.readBufToArray = function(start,num){
    	if(!checkParamters(start,num)){
    		return;
    	}
        if (num === 0) {
            return "";
        }

        var result = undefined; //保存最终需要得到的字符串
        var size = buffer.split("/;").length; //当前buffer里面块的数目

        if (bufferStart === undefined || !(bufferStart <= start && start < bufferStart + size)) {
            //一开始就要重新获取数据
            source.getData(start, num);
        } else {
            var offset = start-bufferStart;

            if( num <= size-offset ){ //可以全部使用缓冲区数据
                result = buffer.split("/;").slice(offset,offset+num);
            }else{ //除了缓冲区数据外，还需要从源获取数据
                result = buffer.split("/;").slice(offset);
                source.getData(bufferStart+size, num-size+offset);
            }
        }
        debug();
        return result;
    }

    //向缓冲区中以覆盖的方式写入字符串
    //@param {number} start 起始字符位置
    //@param {string} str 待写入的字符串
    //@result {boolean}
    this.writeChar = function(start,str){
        if(start<0||start>=buffer.length)
            return false;

        updateDirty(start);

        buffer = buffer.substring(0,start)+str+buffer.substring(start+str.length);
        debug();
        return true;
    }

    //向缓冲区中以覆盖的方式写入数据块
    //@param {number} start 起始数据块位置
    //@param 0到多个参数 待写入的多个数据块
    //@result {boolean}
    this.writeBuf = function(start){
        if(start<0)
            return false;
        var n = arguments.length;
        if(n<2)
            return false;

        var arr = [];
        if(buffer.length === 0){
            for(var j = 1;j<arguments.length;j++)
                arr.push(arguments[j]);
        }else{
            arr = buffer.split("/;");
            if(start>=arr.length)
                return false;
            updateDirty(start);
            var i = 1;
            while(i<n){
                arr[start++] = arguments[i++];
            }
        }

        buffer = arr.join("/;")
        debug();
        return true;
    }

    //向缓冲区中以覆盖的方式写入字符串
    //@param {number} start 起始数据块块位置
    //@param {array} arr2 待写入的多个数据块组成的数组
    //@result {boolean}
    this.writeBufByArray = function(start,arr2){
        var argu = [];
        argu.push(start);
        for(var i = 0; i<arr2.length;i++){
            argu.push(arr2[i]);
        }
        return this.writeBuf.apply(this,argu);
    }

    //向缓冲区中以插入的方式写入字符串
    //@param {number} start 起始字符位置，有效范围[0,buffer.length]
    //@param {string} str 待写入的字符串
    //@result {boolean}
    this.insertChar = function(start,str){
        if(start<0 || start > buffer.length)
            return false;

        updateDirty(start);

        buffer = buffer.substring(0,start)+str+buffer.substring(start);
        debug();
        return true;
    }

    //向缓冲区中以插入的方式写入数据块
    //@param {number} start 起始数据块位置，有效范围[0,buffer.length]
    //@param 0到多个参数 待写入的多个数据块
    //@result {boolean}
    this.insertBuf = function(start){
        if(start<0)
            return false;
        if(buffer.length===0){
            return writeBuf.apply(this,arguments);
        }
        var arr = buffer.split("/;");
        if(start>arr.length)
            return false;
        var n = arguments.length;
        if(n<2)
            return false;

        updateDirty(start);

        var argu = [];
        argu.push(start);
        argu.push(0);
        for(var i = 1;i<arguments.length;i++){
            argu.push(arguments[i]);
        }
        Array.prototype.splice.apply(arr,argu);

        buffer = arr.join("/;")
        debug();
        return true;
    }

    //向缓冲区中以插入的方式写入数据块
    //@param {number} start 起始数据块位置，有效范围[0,buffer.length]
    //@param {array} arr2 待写入的多个数据块组成的数组
    //@result {boolean}
    this.insertBufByArray = function(start,arr2){
        var argu = [];
        argu.push(start);
        for(var i = 0; i<arr2.length;i++){
            argu.push(arr2[i]);
        }
        return this.insertBuf.apply(this,argu);
    }

    //向缓冲区中末尾以追加的方式写入数据块
    //@param {array} arr 待写入的多个数据块组成的数组
    //@result {boolean}
    this.appendByArray = function(arr){
        if(buffer.length === 0)
            return this.writeBufByArray(0,arr);
        else{
             return this.insertBufByArray(buffer.split("/;").length,arr);
        }
    }

    //向缓冲区中末尾以追加的方式写入数据块,供resInjectXXX回调使用
    //@param {array} arr 待写入的多个数据块组成的数组
    //@result {boolean}
    this.append = function(start,arr){
        if(buffer.length === 0){
            bufferStart = start;
            this.writeBufByArray(0,arr);
        }
        else{
             this.insertBufByArray(buffer.split("/;").length,arr);
        }
        eventEmitter.emit("read");
    }

    //删除指定范围的数据块
    //@param {number} start 数据块在缓冲区中的位置，不小于0
    //@param {number} num 删除的数据块数量，小于等于0表示不删除
    //@result {boolean}
    this.delete = function(start,num){
        if(start<0)
            return false;
        if(buffer.length===0)
            return true;

        updateDirty(start);
        var arr = buffer.split("/;");
        arr.splice(start,num);

        buffer = arr.join("/;");
        debug();
        return true;
    }

    //与数据源同步
    this.dump = function(type){
        var result;
        if(dirtyStart!==-1){
            var s = dirtyStart+bufferStart,
                e = type=="char"?dirtyCharEnd:dirtyBufferEnd;
            result = source.setData(s,e,buffer.split("/;").slice(dirtyStart).join("/;"));
            if(result){
                dirtyStart = dirtyEnd = -1;
            }else{
                alert("无法更新数据到源");
            }
            return result;
        }
        return true;
    }

    //获取源数据总数
    this.getSize = function(){
        return source.getSize();
    }

    init();	

}//Buffer()

////////////////Utilities//////////////
function Utilities()
{
//	//localStorage+userData
//	this.Ls = (function(window) {
//		var LS, noop = function() {},
//			document = window.document,
//			notSupport = {
//				set: noop,
//				get: noop,
//				remove: noop,
//				clear: noop,
//				each: noop,
//				obj: noop,
//				length: 0
//			};
//		(function() {
//			if ("localStorage" in window) {
//				try {
//					LS = window.localStorage;
//					return;
//				} catch (e) {}
//			}
//			var o = document.getElementsByTagName("head")[0],
//				hostKey = window.location.hostname || "localStorage",
//				d = new Date(),
//				doc, agent;
//			if (!o.addBehavior) {
//				try {
//					LS = window.localStorage;
//				} catch (e) {
//					LS = null;
//				}
//				return;
//			}
//
//			try {
//				agent = new ActiveXObject('htmlfile');
//				agent.open();
//				agent.write('<s' + 'cript>document.w=window;</s' + 'cript><iframe src="/favicon.ico"></iframe>');
//				agent.close();
//				doc = agent.w.frames[0].document;
//				o = doc.createElement('head');
//				doc.appendChild(o);
//			} catch (e) {
//				o = document.getElementsByTagName("head")[0];
//			}
//
//			//初始化userData
//			try {
//				d.setDate(d.getDate() + 36500);
//				o.addBehavior("#default#userData");
//				o.expires = d.toUTCString();
//				o.load(hostKey);
//				o.save(hostKey);
//			} catch (e) {
//				//防止部分外壳浏览器的bug出现导致后续js无法运行
//				//如果有错，放弃本地存储
//				//2013-04-23 马超 增加
//				return;
//			}
//			//开始处理userData
//			//以下代码感谢瑞星的刘瑞明友情支持，做了大量的兼容测试和修改
//			//并处理了userData设置的key不能以数字开头的问题
//			var root, attrs;
//			try {
//				root = o.XMLDocument.documentElement;
//				attrs = root.attributes;
//			} catch (e) {
//				//防止部分外壳浏览器的bug出现导致后续js无法运行
//				//如果有错，放弃本地存储
//				//2013-04-23 马超 增加
//				return;
//			}
//			var prefix = "p__hack_",
//				spfix = "m-_-c",
//				reg1 = new RegExp("^" + prefix),
//				reg2 = new RegExp(spfix, "g"),
//				encode = function(key) {
//					return encodeURIComponent(prefix + key).replace(/%/g, spfix);
//				},
//				decode = function(key) {
//					return decodeURIComponent(key.replace(reg2, "%")).replace(reg1, "");
//				};
//			//创建模拟对象
//			LS = {
//				length: attrs.length,
//				isVirtualObject: true,
//				getItem: function(key) {
//					//IE9中 通过o.getAttribute(name);取不到值，所以才用了下面比较复杂的方法。
//					return (attrs.getNamedItem(encode(key)) || {
//						nodeValue: null
//					}).nodeValue || root.getAttribute(encode(key));
//				},
//				setItem: function(key, value) {
//					//IE9中无法通过 o.setAttribute(name, value); 设置#userData值，而用下面的方法却可以。
//					try {
//						root.setAttribute(encode(key), value);
//						o.save(hostKey);
//						this.length = attrs.length;
//					} catch (e) { //这里IE9经常报没权限错误,但是不影响数据存储
//					}
//				},
//				removeItem: function(key) {
//					//IE9中无法通过 o.removeAttribute(name); 删除#userData值，而用下面的方法却可以。
//					try {
//						root.removeAttribute(encode(key));
//						o.save(hostKey);
//						this.length = attrs.length;
//					} catch (e) { //这里IE9经常报没权限错误,但是不影响数据存储
//					}
//				},
//				clear: function() {
//					while (attrs.length) {
//						this.removeItem(attrs[0].nodeName);
//					}
//					this.length = 0;
//				},
//				key: function(i) {
//					return attrs[i] ? decode(attrs[i].nodeName) : undefined;
//				}
//			};
//			//提供模拟的"localStorage"接口
//			if (!("localStorage" in window))
//				window.localStorage = LS;
//		})();
//
//		//二次包装接口
//		return !LS ? notSupport : {
//			set: function(key, value) {
//				//fixed iPhone/iPad 'QUOTA_EXCEEDED_ERR' bug
//				if (this.get(key) !== undefined)
//					this.remove(key);
//				LS.setItem(key, value);
//				this.length = LS.length;
//			},
//			//查询不存在的key时，有的浏览器返回null，这里统一返回undefined
//			get: function(key) {
//				var v = LS.getItem(key);
//				return v === null ? undefined : v;
//			},
//			remove: function(key) {
//				LS.removeItem(key);
//				this.length = LS.length;
//			},
//			clear: function() {
//				LS.clear();
//				this.length = 0;
//			},
//			//本地存储数据遍历，callback接受两个参数 key 和 value，如果返回false则终止遍历
//			each: function(callback) {
//				var list = this.obj(),
//					fn = callback || function() {},
//					key;
//				for (key in list)
//					if (fn.call(this, key, this.get(key)) === false)
//						break;
//			},
//			//返回一个对象描述的localStorage副本
//			obj: function() {
//				var list = {},
//					i = 0,
//					n, key;
//				if (LS.isVirtualObject) {
//					list = LS.key(-1);
//				} else {
//					n = LS.length;
//					for (; i < n; i++) {
//						key = LS.key(i);
//						list[key] = this.get(key);
//					}
//				}
//				return list;
//			},
//			length: LS.length
//		};
//		//如果有jQuery，则同样扩展到jQuery
//		if (window.jQuery) window.jQuery.LS = window.LS;
//	})(window);
	
	
	
	
	
  //cookie	
 this.Cookie = {
			//保存cookie  cName:cookie名,cValue:cookie值,cDays:保存期限(天数)
			setCookie: function(cName, cValue, cDays) {
				var date = new Date();
				//如果保存天数未设置，则默认保存一天
				if (cDays==undefined||Number(cDays)!=cDays) {
					cDays=1;
				}
				date.setTime(date.getTime() + cDays * 24 * 3600 * 1000);
				//var cookieStr = escape(cName) + "=" + escape(cValue) + "; expires=" + date.toGMTString() + ";";
				var cookieStr = escape(cName) + "=" + escape(cValue) + "; expires=" + date.toGMTString() + "; path=/;";

				document.cookie = cookieStr;
			},
			//根据cookie名取得其值
			getCookie: function(cName) {
				var cookieStr = document.cookie;
				var cookies = cookieStr.split("; ");
				for (var i = 0; i < cookies.length; i++) {
					var cookie = cookies[i];
					var theCookie = cookie.split("=");
					var theValue = unescape(theCookie["1"]);
					if (theValue == "undefined" || theValue == null) {
						theValue = "";
					}
					if (theCookie["0"] == escape(cName)) {
						return theValue;
						break;
					}
				}
				return "";
			},
			//根据cookie名删除
			delCookie: function(cName) {
				var date = new Date();
				date.setTime(date.getTime() - 1000);
				var cookieStr = escape(cName) + "=; expires=" + date.toGMTString() + ";";
				document.cookie = cookieStr;
			}
		}	
	
	this.gotoLocalMsgHandle = function gotoLocalMsgHandleX(msgName, msgData)
	{
	 switch (msgName)
	 {
	  case "OpenNewPage": 
	   window.location.href=msgData; //成功，IE支持，Chrome支持
	   //window.navigate("in_new.html"); //路径相对于工程下的WebContent目录, IE支持，Chrome不支持
	   //self.location("in_new.html"); //, IE支持，Chrome不支持
	   break;
	  case "BackPage":	  
	   window.history.back(msgData);
	   break;	  
	 }//switch
	}
	
	this.switchToNewPage = function switchToNewPageX(htmlStr)
	{//从原网页上打开一个新网页，关闭原网页。新网页的数据htmlStr，是完整的HTML页面串
		alert(htmlStr);
	 document.write(htmlStr);
	 document.close();	
	}	
	
	this.readMsgMapFromCookie=function()
	{//从Cookie读取消息映射表
	 var msgMap = this.Cookie.getCookie('VeMsgMap');
	 var msgMapObj=eval("("+msgMap+")");	
	 
	 return  msgMapObj[cVe.cEioVeDataId].GetMsgMap;
	  
	} 
	
	this.saveMsgMapToCookie=function(msgData)
	{//从Cookie读取消息映射表
	 this.Cookie.setCookie("VeMsgMap",msgData);//将消息映射保存到Cookies
	 	 	  
	} 
	
	
	
	this.readLoginPermit=function()
	{//从Cookie读取消息映射表
		
	 var msgMap = this.Cookie.getCookie('VeLoginPermit');
	 if(msgMap!=""){
		 var msgMapObj=eval("("+msgMap+")");
		 return  msgMapObj[cVe.cEioVeDataId];
	 }else {
		 return null;
	 }	
	} 
	
	this.saveLoginPermit=function(msgData)
	{//从Cookie读取消息映射表
	 this.Cookie.setCookie("VeLoginPermit",msgData);//将消息映射保存到Cookies
	 	 	  
	} 
}//utilities()

window['EIO']['ve']=JsVeMsgDispatcher;
window['EIO']['vei']=ExtInj;
window['EIO']['veBuffer']=Buffer;
window['EIO']['veUti']=Utilities;
}//function ()
)();

/////////////表格控件////////////////
//表格控件
function Grid2(selector, buffer, options) {
	var def = {
		colNames: [],
		colModel: [],
		formatters: {},
		widths: {},
		rows: 10,
		styles: {},
		//		total: 10,
		//		records: 1000,
		//		page: 1,
		caption: "",
		height: 'auto',
		width: 'auto',
		shrinkToFit: true,
		rowClick: undefined,
	}; //默认配置项
	var options = $.extend({}, def, options || {}); //配置总配置项
	var checks = [];
	var buffer = buffer;
	var that = this;
	var showData = {
		gData: [],
		page: 1,
		records: 0,
		total: 0
	};
	//由于新版buffer特殊的回调特性，start与num需要找公共变量存储！默认是获取第一页的情况
	var start = 0;
	var num = options.rows;
	var init = function() {
			var jqObj = {
				datatype: "local",
				autowidth: true,
				shrinkToFit: options.shrinkToFit,
				autoScroll: true,
				height: options.height,
				width: options.width,
				viewrecords: false,
				caption: options.caption,
				rowNum: options.rows,
				onSelectRow: function(rowid, status) {
					var p = (showData.page - 1) * (options.rows) + Number(rowid);
					checks[p] = status;
					if (options.rowClick != undefined) {
						options.rowClick(rowid);
					}
				},
				onSelectAll: function(aRowids, status) {
					var p = (showData.page - 1) * (options.rows);
					for (var i = 1; i <= aRowids.length; i++) {
						checks[p + i] = status;
					}
				},
				loadComplete: function() {
					//先删再插入数据，保证无重复
					var rowIds = $(selector).jqGrid('getDataIDs');
					for (var i = 0; i < rowIds.length; i++) {
						$(selector).jqGrid("delRowData", rowIds[i]);
					}
					$('#p_' + selector.substring(3) + '_right').html('共' + showData.records + '条记录')
						//数组模式
					var gData = showData.gData;
					var first = (showData.page - 1) * options.rows + 1;
					console.log("gData.length:" + gData.length);
					for (var i = 0; i < gData.length; i++) {
						var rowData = {};
						for (var j = 0; j < options.colNames.length; j++) {
							rowData[j + ''] = gData[i][j];
						}
						$(selector).jqGrid('addRowData', i + 1, rowData);
						if (checks[Number(first) + Number(i)]) {
							$(selector).jqGrid("setSelection", i + 1);
						}
					}
					//注：列样式需要在这里生效！
					for (var i = 0; i < options.colNames.length; i++) {
						var m = {};
						m.name = i + '';
						m.align = "center";
						//注入formatter
						if (typeof options.styles[i] != 'undefined') {
							$('td[aria-describedby="gridTable_' + i + '"]').css(options.styles[i]);
						}
					}
				},
				onPaging: function(pgButton) {
					var pagerId = 'p_' + selector.substring(3);
					switch (pgButton) {
						//首页事件
						case 'first_' + pagerId:
							showData.page = 1;
							break;
							//最后一页事件
						case 'last_' + pagerId:
							showData.page = showData.total;
							break;
							//前一页事件
						case 'prev_' + pagerId:
							if (showData.page > 1) {
								showData.page = showData.page - 1;
							}
							break;
							//下一页事件
						case 'next_' + pagerId:
							if (showData.page < showData.total) {
								showData.page = Number(showData.page) + 1;
							}
							break;
					}
					start = (showData.page - 1) * options.rows;
					console.log("start:" + start);
					var data = buffer.readBufToArray(start, num);
					if (data != undefined) {
						var gData = [];
						for (var i = 0; i < data.length; i++) {
							gData.push(data[i].split(','));
						}
						that.injectData({
							gData: gData,
							records: buffer.getSize(),
							total: Math.ceil(buffer.getSize() / options.rows),
						});
					}

				},
				localReader: { //  
					page: function(obj) {
						return showData.page;
					},
					total: function(obj) {
						return showData.total;
					},
					records: function(obj) {
						return showData.records;
					},
					repeatitems: true,
					id: "no_id"
				},
				pager: '#p_' + selector.substring(3),
				hidegrid: false,
				multiselect: true,
			};
			//是否拥有多选功能
			if (typeof options.multiselect != 'undefined' && options.multiselect == false) {
				jqObj.multiselect = false;
			}
			jqObj.colNames = options.colNames;
			jqObj.colModel = [];
			for (var i = 0; i < options.colNames.length; i++) {
				var m = {};
				m.name = i + '';
				m.align = "center";
				//注入formatter
				if (typeof options.formatters[i] != 'undefined') {
					m.formatter = eval(options.formatters[i])
				}
				//注入width
				if (typeof options.widths[i] != 'undefined') {
					m.width = options.widths[i]
				}
				jqObj.colModel.push(m)
			}
			$(selector).jqGrid(jqObj);
			//执行某些数据清空操作
//			for (var i = 0; i <= showData.records; i++) {
//				checks[i] = false;
//			}
			$(selector).closest(".ui-jqgrid-bdiv").css({
				'overflow-x': 'scroll'
			});
			$(selector).closest(".ui-jqgrid-bdiv").css({
				'overflow-y': 'scroll'
			});
			$(selector).closest(".ui-jqgrid-bdiv").css({
				'width': '100%'
			});
			//初始化跳页事件
			//处理jqgrid页码框失去焦点事件
			$('.ui-pg-input').blur(function() {
					var v = $(this).val();
					var p = parseInt(v);
					if (p == v) {
						if (p >= 1 && p <= options.total) {
							//						console.log("p:" + p);
							showData.page = p;
							showData.gData = buffer.read(1, 10);
							$(selector).trigger("reloadGrid")
						}
					} else {
						$(this).val(showData.page);
					}
				})
				//处理全选事件
			if (typeof options.multiselect != 'undefined' && options.multiselect == false) {

			} else {
				//注：现在checkbox体系变得不可用，因为如何记录尚未知！
				$('#a_' + selector.substring(3)).click(function() {
					var flag = $(this).prop('checked');
					for (var i = 0; i <= showData.records; i++) {
						checks[i] = flag;
					}
					$(selector).trigger("reloadGrid");
				});
				
			}
			//初始化buffer，由于总数未知，records与total均为假数据
			//			buffer.setCallBack(
			//					function(data) {
			//						var gData = [];
			//						for (var i = 0; i < data.length; i++) {
			//							gData.push(data[i].split(','));
			//						}
			//						that.injectData({
			//							gData: gData,
			//							records: 1003,
			//							total: 12,
			//						});
			//					})
			////				注入第一批数据
			//			buffer.readBufToArray(0, options.rows - 1);
//			buffer.addReadListener(function() {
//				var data = buffer.readBufToArray(start, num);
//				if (data != undefined) {
//					//					alert(data);
//					var gData = [];
//					for (var i = 0; i < data.length; i++) {
//						gData.push(data[i].split(','));
//					}
//					that.injectData({
//						gData: gData,
//						records: buffer.getSize(),
//						total: Math.ceil(buffer.getSize() / options.rows),
//					});
//				}
//			});
//
//			var data = buffer.readBufToArray(start, num);
//			if (data != undefined) {
//				var gData = [];
//				for (var i = 0; i < data.length; i++) {
//					gData.push(data[i].split(','));
//				}
//				that.injectData({
//					gData: gData,
//					records: that.buffer.getSize(),
//					total: Math.ceil(buffer.getSize() / options.rows),
//				});
//			}
		}
		//	init();
	this.injectData = function(data, flag) {
		//让gData清空
		showData.gData = [];
		showData = $.extend({}, showData, data || {});
		//对于表格的总记录数改变的专门处理
		if (typeof data.records != 'undefined' && showData.records != data.records) {
			//每当设置record，checks需重置
			checks = [];
		}
		if (flag == true) {} else {
			$(selector).trigger("reloadGrid");
		}
	};
	this.setGridCols = function(newOptions) {
		options = $.extend({}, options, newOptions || {});
		$(selector).jqGrid('GridUnload');
		init();
		//TODO  由于buffer未有，这里注释！
		//		this.injectData({
		//			gData: buffer.read(1, 10)
		//		})
	};
	this.extGrid = function() {
		//checkbox版本
		//		var exData = {};
		//		exData.cols = options.colNames;
		//		exData.recs = [];
		//		for (var i = 1; i <= showData.gData.length; i++) {
		//			if (checks[i]) {
		//				exData.recs.push(showData.gData[i - 1])
		//			}
		//		}
		var exData = [];
		for (var i = 1; i <= checks.length; i++) {
			if (checks[i]) {
				exData.push(i);
			}
		}
		return JSON.stringify(exData).toString();
		//tSel版本
		//		var tSel = "5:16,9:9";
		//		var sels = tSel.split(',');
		//		var a = [];
		//		for (var i = 0; i < sels.length; i++) {
		//			var fl = sels[i].split(':');
		//			if ($.isNumeric(fl[0]) && $.isNumeric(fl[1])) {
		//				var tFirst = parseInt(fl[0]);
		//				var tLast = parseInt(fl[1]);
		//				//如果tFirst > tLast,则抛弃之
		//				if (tFirst <= tLast) {
		//					for (var j = tFirst; j <= tLast; j++) {
		//						a.push(Number(j));
		//					}
		//				}
		//			}
		//		}
		//		if (a.length == 0) {
		//			return a;
		//		}
		//		a.sort(function(a, b) {
		//			return a - b;
		//		});
		//		var returnArray = [];
		//		var max = a[0];
		//		returnArray.push(max);
		//		for (var i = 1; i < a.length; i++) {
		//			if (a[i] > max) {
		//				max = a[i];
		//				returnArray.push(max);
		//			}
		//		}
		//		return returnArray;
	};
	//显示等待信息，用于buffer返回等待信息时使用
	var showWaitMessage = function() {
			$('#g_' + selector.substring(3)).prev().html("<span style='font-size: 20px;'><center>网络故障，请耐心等待！</center></span>");
		}
		//获取当前是表格的第几页，用于计算行数据的相对路径
	this.getPage = function() {
			return showData.page;
		}
		//获取当前一页显示多少条记录
	this.getRows = function() {
			return showData.rows;
		}
		//获取当前点击行的rowData数据，需要知道rowId
	this.extDataById = function(rowId) {
			return showData.gData[rowId - 1];
		}
		//设置总页数
	this.setTotal = function(total) {
		showData.total = total;
		$(selector).trigger("reloadGrid");
	};
	//设置总条数，注意，cb将被重建
	this.setRecords = function(records) {
			showData.records = records;
			//每当设置record，checks需重置
			checks = [];
			for(var i=0;i<=records;i++)
			{
				checks[i]=false;
			}
			$(selector).trigger("reloadGrid");
		}
		//添加一条新记录
	this.addOneRow = function(rowObject) {
			//		showData=
			$(selector).trigger("reloadGrid");
		}
		//删除记录
	this.delRows = function(rowids) {
			$(selector).trigger("reloadGrid");
		}
		//修改一条表格记录
	this.editOneRow = function(rowid, rowObject) {

			$(selector).trigger("reloadGrid");
		}
		//刷新
	this.refresh = function() {
			$(selector).trigger("reloadGrid");
		}
		//让选中的页面行在表格消失，当用户执行翻页等操作此删除将失效，目前只是页面上的操作
	this.delSelectedRows = function() {
			var selectedRowIds =
				$(selector).jqGrid("getGridParam", "selarrrow");
			var len = selectedRowIds.length;
			for (var i = 0; i < len; i++) {
				$(selector).jqGrid("delRowData", selectedRowIds[0]);
			}
		}
		//让选中行的某一列数据内容发生变化，目前只是页面上的操作，colId是列索引值，从1开始算，content是修改后的内容
	this.editSelectedRows = function(colId, content) {
		var selectedRowIds =
			$(selector).jqGrid("getGridParam", "selarrrow");
		var len = selectedRowIds.length;
		for (var i = 0; i < len; i++) {
			$(selector).jqGrid('setCell', selectedRowIds[i], colId,content);;
		}
	}
	//获取表的选中id数组
	this.getSelectedIds=function()
	{
		//checkbox版本
//		var exData = [];
//		for (var i = 1; i <checks.length; i++) {
//			if (checks[i]) {
//				exData.push(i);
//			}
//		}
//		return JSON.stringify(exData).toString();
//       tsel版本
        var exData=[];
        var first=-1;
        var right=-1;
        for(var i=1;i<checks.length;i++)
        {
        	if (checks[i]) {
        		if (first==-1) {
        			first=i;
        			right=i;
        		}
        		else
        		{
        			if (right==i-1) {
        				right=i;
        			}
        			else
        			{
        				exData.push(first+"-"+right);
        				first=i;
        				right=i;
        			}
        		}
        	}
        }
        if (first!=-1) {
        	exData.push(first+"-"+right);
        }
        return JSON.stringify(exData).toString();
	}
}//Grids()

/////////////////////////jqTable/////////////
jQuery.fn.extend({

	  jqTable:function(options){

	        //一些内部使用的常量
	        var thisDom = this[0],
	            contentTable, //显示内容table的jQuery对象
	            contentTableDom, //显示内容table的javascript对象
	            dragDiv, //拖动列时显示的div
	            sortbyDefault=function(a,b){ //默认排序函数
	              return a<b?-1:(a==b?0:1);
	            };

	        //参数检验
	        var colNamesLength = options.colNames.length,
	            colModelsLength = options.colModels.length;
	        if( !(colNamesLength && colModelsLength && colNamesLength === colModelsLength) ){
	          alert("colNames和colModels长度不想等")
	          return;
	        }
	        /*if(options.pageSize>options.onceSize){
	          alert("pageSize参数不能比onceSize参数大");
	          return;
	        }
	        if( options.onceSize % options.pageSize !==0 ){
	          alert("onceSize参数必须能被pageSize参数整除");
	          return;
	        }*/

	        //参数合并
	        options.height = options.height || 100;

	        //获取滚动条宽度
	        var scrollBarWidth = (function(){
	          var div = document.createElement("div");
	          div.style.width = "100px";
	          div.style.overflow = "scroll";
	          document.body.appendChild(div);
	          var result = div.offsetWidth - div.clientWidth;
	          document.body.removeChild(div);
	          return result;
	        })();

	       //获取每列宽度和总宽度
	       if(options.width || (  !options.width && !options.colModels[0].width ) ){
	          options.width = options.width || thisDom.offsetWidth;
	          (function(colNames,colModels){
	              var table = document.createElement("table");
	              $(table).width(options.width);
	              table.className = "jqt-table";
	              thisDom.appendChild(table);
	              var tr = document.createElement("tr");
	              var th,th_text;
	              jQuery.each(colNames,function(i,data){
	                th = document.createElement("th");
	                th_text = document.createTextNode(data);
	                th.appendChild(th_text);
	                tr.appendChild(th);
	              })
	              table.appendChild(tr);
	              $(table).find("th").each(function(i,data){
	                colModels[i].width = $(this).width();
	              })
	              thisDom.removeChild(table);
	          })(options.colNames,options.colModels);
	       }else if(!options.width && options.colModels[0].width){
	          options.width = 0;
	          for(var i=0;i<colModelsLength;i++){
	            var w = options.colModels[0].width;
	            if(w){
	              options.width += w;
	            }
	          }
	       }
	       console.dir(options);


	       //生成表头
	      var head = (function(colNames,colModels){

	        var lastOrderCanvas = undefined;

	        //显示表头的table
	        var table = document.createElement("table");
	        table.className = "jqt-table jqt-head-table";
	        $(table).width(options.width);
	        //tr
	        var tr = document.createElement("tr");
	        var th,th_text,th_resizeDiv,th_sortDiv;
	        jQuery.each(colNames,function(i,data){
	          th = document.createElement("th");
	          th.style.width = colModels[i].width+"px";
	          if(colModels[i].sortable){
	            th.style.cursor = "pointer";
	            th.onselectstart = function(){return false;};
	            th.onclick = (function(i){
	              var order = 0;
	              var sortFunc = [function(a,b){
	                if(colModels[i].sortby){
	                  return colModels[i].sortby(a[i],b[i]);
	                }else{
	                  return sortbyDefault(a[i].toString(),b[i].toString());
	                }
	              },function(a,b){
	                if(colModels[i].sortby){
	                  return -colModels[i].sortby(a[i],b[i]);
	                }else{
	                  return -sortbyDefault(a[i].toString(),b[i].toString());
	                }
	              }];
	              return function(){
	                      localData.rows.sort(sortFunc[order]);
	                      //显示相应图标
	                      if(lastOrderCanvas){
	                        $(lastOrderCanvas).hide();
	                      }
	                      lastOrderCanvas = this.lastChild.childNodes[order];
	                      $(lastOrderCanvas).show();

	                      order = (++order)%2;
	                      gotoPageFunc(localPage);
	                    }
	            })(i);
	          }
	          th_resizeDiv = document.createElement("span");
	          th_resizeDiv.className = "jqt-resizeDiv";
	          th_resizeDiv.innerHTML = " ";
	          //th_resizeDiv.id="s"+i;
	          th.appendChild(th_resizeDiv);

	          th_text = document.createTextNode(data);
	          th.appendChild(th_text);

	          //添加排序标志
	          th_sortDiv = document.createElement("div");
	          th_sortDiv.className = "jqt-sortDiv";
	          th_sortDiv.innerHTML = "<canvas class='jqt-sort jqt-sortUp' width='10' height='7'></canvas><canvas class='jqt-sort jqt-sortDown' width='10' height='7'></canvas>";
	          th.appendChild(th_sortDiv);

	          tr.appendChild(th);
	        })
	        table.appendChild(tr);

	        var divInner = document.createElement("div");
	        divInner.appendChild(table);

	        //外层包裹的div
	        var div = document.createElement("div");
	        div.className = "jqt-head-div";
	        div.appendChild(divInner);

	        return {"self":div,"divInner":divInner,"table":table};
	      })(options.colNames,options.colModels);


	        //创建分页栏函数
	        var createPager = function(box){
	          var aPrePage = document.createElement("a");
	          aPrePage.href = "#";
	          aPrePage.className = "jqt-link";
	          aPrePage.innerHTML = "上一页";
	          aPrePage.onclick = function(){
	            prePageFunc();
	            return false;
	          }
	          box.appendChild(aPrePage);

	          var aNextPage = document.createElement("a");
	          aNextPage.href = "#";
	          aNextPage.className = "jqt-link";
	          aNextPage.innerHTML = "下一页";
	          aNextPage.onclick = function(){
	            nextPageFunc();
	            return false;
	          }
	          box.appendChild(aNextPage);

	          var span = document.createElement("span");
	          span.style.margin="5px";
	          span.innerHTML = "<span id='localPage'></span>/<span id='totalPage'></span>";
	          box.appendChild(span);

	          var input = document.createElement("input");
	          input.type="text";
	          input.id="gotoPage";
	          input.style.width="30px";
	          box.appendChild(input);

	          var aGotoPage = document.createElement("a");
	          aGotoPage.href = "#";
	          aGotoPage.className = "jqt-link";
	          aGotoPage.innerHTML = "跳转";
	          aGotoPage.onclick = function(){
	            gotoPageFunc($("#gotoPage").val());
	            return false;
	          }
	          box.appendChild(aGotoPage);
	        };


	        var localPage, //当前页
	            totalPage, //总页数
	            curItem, //当前显示的条目，第一行为0
	            curMaxItem, //当前显示的最大数目
	            localData; //本地数据
	        init();

	        return {
	          refresh:function(){
	            fillData(localPage*options.pageSize);
	          },
	          nextItem:function(){
	            if(curItem===(curMaxItem-1)){
	              nextPageFunc(function(){
	                console.log("curItem:"+curItem);
	                $("#jqTable_0").click();
	              });
	            }else{
	              $("#jqTable_"+(curItem+1)).click();
	            }
	          },
	          preItem:function(){
	            if(curItem === 0){
	              prePageFunc(function(){
	                console.log("curMaxItem"+curMaxItem);
	                $("#jqTable_"+(curMaxItem-1)).click();
	              });
	            }else{
	              $("#jqTable_"+(curItem-1)).click();
	            }
	          }
	        }

	        //初始化表格和分页栏
	        function init(){

	          //增加表头
	          thisDom.appendChild(head.self);

	          setTimeout(function(){
	            $(".jqt-sortUp").each(function(){
	            var cxt = this.getContext("2d");
	            cxt.moveTo(5,0);
	            cxt.lineTo(0,7);
	            cxt.lineTo(10,7);
	            cxt.lineTo(5,0);
	            cxt.fillStyle = "#6CAAD4";
	            cxt.fill();
	          })
	          $(".jqt-sortDown").each(function(){
	            var cxt = this.getContext("2d");
	            cxt.moveTo(0,0);
	            cxt.lineTo(5,7);
	            cxt.lineTo(10,0);
	            cxt.lineTo(0,0);
	            cxt.fillStyle = "#6CAAD4";
	            cxt.fill();
	          })
	        },0);

	          //增加内容表格,读取中提示框和包裹div
	          var div = document.createElement("div");
	          div.className = "jqt-content-div";
	          $(div).height(options.height);

	          var readingDiv = document.createElement("div");
	          readingDiv.id = "reading";
	          readingDiv.className = "jqt-reading-div";
	          readingDiv.innerHTML = "读取中...";
	          div.appendChild(readingDiv);
	          setTimeout(function(){
	            $("#reading").css({"top":(div.offsetHeight-readingDiv.offsetHeight)/2,"left":(div.offsetWidth-readingDiv.offsetWidth)/2});
	          },0);


	          contentTableDom = document.createElement("table");
	          contentTableDom.className = "jqt-table jqt-content-table";
	          contentTable = $(contentTableDom);
	          contentTable.width(options.width);
	          div.appendChild(contentTableDom);
	          thisDom.appendChild(div);

	          //设置表头里层div的宽度
	          head.divInner.style.width = $(head.table).width()+scrollBarWidth+"px";

	          //增加拖动可以改变列宽度的元素
	          $(".jqt-resizeDiv").each(function(){
	            drag(this);
	          })

	          //同步滚动
	          $(div).scroll(function(){
	            $(head.self).scrollLeft($(this).scrollLeft());
	          })

	          //初始化dragDiv
	          dragDiv = document.createElement("div");
	          dragDiv.className = "jqt-dragDiv";
	          dragDiv.style.height = thisDom.clientHeight+"px";
	          document.body.appendChild(dragDiv);

	          //生成分页栏
	          createPager(jQuery(options.pager)[0]);

	          document.getElementById("localPage").innerHTML = localPage = 1;
	          document.getElementById("totalPage").innerHTML = totalPage = Math.ceil(options.buffer.getSize()/options.pageSize);
	          if(!fillData(0)){
	            document.getElementById("reading").style.display = "inline";
	            options.buffer.addReadListener(function(){
	              fillData(0);
	              document.getElementById("reading").style.display = "none";
	              $("#jqTable_0").click();
	            })
	          }else{
	            $("#jqTable_0").click();
	          }
	        }

	        //填充数据,参数：数据在localData中的起始位置
	        function fillData(startIndex){
	          var data = options.buffer.readBufToArray(startIndex,options.pageSize);
	          if(data!=undefined&&data.length===options.pageSize){
	            contentTable.empty();
	            curMaxItem = data.length;
	            curItem = -1;
	            var tr,td;
	            jQuery.each(data,function(i,items){
	              items = items.split(",");
	              tr = document.createElement("tr");
	              tr.id = "jqTable_"+i;
	              tr.onclick = (function(index,data){
	                return function(){
	                  if(curItem !== index){
	                    if(curItem>=0){
	                      $("#jqTable_"+curItem).removeClass("jqt-trFocus");
	                    }
	                    $("#jqTable_"+index).addClass("jqt-trFocus");
	                    curItem = index;
	                    if(options.trClick){
	                      options.trClick(data);
	                    }
	                  }
	                }
	              })(i,items);
	              for(var j=0,item; j<colModelsLength;j++){
	                item = items[j];
	                td = document.createElement("td");
	                td.style.width = options.colModels[j].width+"px";
	                if(options.colModels[j].formate){
	                  td.innerHTML = options.colModels[j].formate(item);
	                }else{
	                  td.innerHTML = item;
	                }
	                tr.appendChild(td);
	              }
	              contentTableDom.appendChild(tr);
	            })
	            return true;
	          }
	          return false;
	        }

	        //获取起始位置并填充数据,参数callback:填充数据后的回调函数
	        function getStartIndexAndFillData(callback){
	          var t = fillData((localPage-1)*options.pageSize);
	          if(t){
	            if(callback!==undefined){
	              callback();
	            }
	          }else{
	            document.getElementById("reading").style.display = "inline";
	            options.buffer.addReadListener(function(){
	              fillData((localPage-1)*options.pageSize);
	              document.getElementById("reading").style.display = "none";
	              if(callback!==undefined){
	                callback();
	              }
	            })
	          }
	        }

	        function prePageFunc(callback){
	          if(localPage>1){
	            document.getElementById("localPage").innerHTML = --localPage;
	            getStartIndexAndFillData(callback);
	            return true;
	          }else{
	            alert("已经是首页");
	            return false;
	          }
	        }
	        function nextPageFunc(callback){
	          if(totalPage!=localPage){
	            document.getElementById("localPage").innerHTML = ++localPage;
	            getStartIndexAndFillData(callback);
	            return true;
	          }else{
	            alert("已经到最后一页");
	            return false;
	          }
	        }
	        function gotoPageFunc(i,callback){
	          if(i>0&&i<=totalPage){
	            document.getElementById("localPage").innerHTML = localPage = i;
	            getStartIndexAndFillData(callback);
	            return true;
	          }else{
	            alert("无效页码")
	            return false;
	          }
	        }

	        //拖动改变列宽度函数
	        function drag(t){
	          t.index = t.parentNode.cellIndex;
	          t.getWidth = function(){
	            return this.parentNode.parentNode.cells[this.index].offsetWidth;
	          }
	          t.changeWidth = function(move){
	            //head.divInner.style.width = $(head.table).width()+scrollBarWidth+"px";
	            //contentTableDom.style.width = (contentTableDom.offsetWidth+move)+"px";
	            this.parentNode.style.width = (this.getWidth()+move)+"px";
	            for(var i = 0 ;i<contentTableDom.rows.length;i++){
	              var cell = contentTableDom.rows[i].cells[this.index];
	              cell.style.width = (cell.offsetWidth+move)+"px";
	            }
	            options.colModels[this.index].width = parseInt(this.parentNode.style.width);
	            head.table.style.width = (head.table.offsetWidth+move)+"px";
	            contentTableDom.style.width = head.table.style.width;
	            head.divInner.style.width = $(head.table).width()+scrollBarWidth+"px";
	          }
	          t.firstChild.onmousedown = function(){
	            return false;
	          }
	          t.onmousedown = function(e){
	            var d = document;
	            e = e || window.event;
	            var lastX = e.clientX;
	            //console.dir(e);
	            $(dragDiv).css({"top":$(t).offset().top,"left":e.pageX,"display":"block"});
	            if(t.setCapture)
	              t.setCapture();
	            else if(window.captureEvents){
	              window.captureEvents(Event.MOUSEMOVE | Event.MOUSEUP);
	            }
	            //防止拖动时选中文字
	            d.onselectstart = function(){return false;};//ie,chrome
	            $(t).css("-moz-user-select","none"); //firefox

	            d.onmousemove = function(e){
	                e = e || window.event;
	                $(dragDiv).css("left",e.pageX);
	            }
	            d.onmouseup = function(e){
	              if (t.releaseCapture)
	                t.releaseCapture();
	              else if (window.captureEvents)
	                window.captureEvents(Event.MOUSEMOVE | Event.MOUSEUP);
	              d.onmousemove = null;
	              d.onmouseup = null;
	              d.onselectstart = null;
	              $(t).css("-moz-user-select","inherit");
	              $(dragDiv).hide();
	              e = e || window.event;
	              var move = e.clientX - lastX;
	              if(move>0){ //right
	                t.changeWidth(move);
	              }else{ //left
	                if(t.getWidth() + move < 20)
	                  return;
	                t.changeWidth(move);
	              }
	              lastX = e.clientX;
	            }
	          }
	        }
	      }
	})
