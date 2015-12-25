package csAsc.EIO.SysMsgHandle;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;

import csAsc.EIO.MsgEngine.CEIOMsgRouter.CParam;

public class CMsgInit
{//处理初始化消息
 	 public int handleMsg(CParam param) throws ServletException,IOException,JSONException
	 {//请求消息数据格式：{"MsgInitName1" : Init初始化名称1, "MsgInitName2" : Init初始化名称21, ...}
 	  //回送消息数据格式：{Init初始化名称1 : 回送数据1, Init初始化名称2 : 回送数据2, ...}	 
	  System.out.println("==进入初始化消息处理器CMsgInit的handleMsg");
	  HttpServletRequest msgReq;
	  msgReq = param.getMsgReq();
	  msgReq.setCharacterEncoding("UTF-8");

	  StringBuffer reqMsgData = new StringBuffer();
	  reqMsgData.append(msgReq.getParameter(param.getReqMsgDataId())); //从消息中获取消息数据体（JSON字符串)

	  JSONObject msgDataObj = new JSONObject(reqMsgData.toString()); 
	  
	  String initName = msgDataObj.getString("MsgInitName1"); //从消息中取出消息内容
	  
	  switch (initName)
	  {
	  case "GetMsgMap":
		String msgMap = param.eioConfigObj.getXml2Json("消息包2");	
		//System.out.println("JSON="+msgMap);
		
		param.respData.append( "{\"GetMsgMap\":" + msgMap + "}");
		break;
	  }
	  
		 
	  return 0;  
	 }
}
