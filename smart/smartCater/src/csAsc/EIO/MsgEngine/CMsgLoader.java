package csAsc.EIO.MsgEngine;
import java.io.IOException;

import csAsc.EIO.MsgEngine.CEIOMsgRouter.CParam;
import csAsc.com.*;

import javax.servlet.ServletException;

public class CMsgLoader 
{//
 public static int  callMsgHandle(CParam param)
    throws ServletException,IOException
 {//信息执行引擎
  int retData=0;
    
  String msgHandleId = param.getMsgHandle();
  if (msgHandleId=="") return -1;
  int k=msgHandleId.lastIndexOf(".");
  String className =msgHandleId.substring(0,k);
  String methodName =msgHandleId.substring(k+1);
  CMethodCaller mCaller= new CMethodCaller();
  Object[] params = {param};
  //System.out.println("反射调用的返回对象=="+className+methodName); 
  retData = (int )mCaller.loadExec(className, methodName, params);
  
  
  //throw new CMyException("程序错误");
  //return (StringBuffer)retData;	
  return retData;
	   
 }
 
  
  /*
  MsgGetJsonData
  MsgGetGetData
  MsgGetJSArrayData
  MsgGetTextData
  MsgGetBlockData
  MsgFormSubmit
  MsgNewPageFile
  MsgNewPageInner 
 */
 
}
