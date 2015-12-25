package csAsc.EIO.RE;
import java.io.IOException;

import csAsc.com.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import csAsc.EIO.MsgEngine.IMsgHandle;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class CResourceEngine 
{//
 public static String callMsgHandle(String msgHandleId,HttpServletRequest req)
    throws ServletException,IOException
 {//资源执行引擎
  IMsgHandle msgHandle;	 
  Object retData=null;
    
  int k=msgHandleId.lastIndexOf(".");
  String className =msgHandleId.substring(0,k);
  String methodName =msgHandleId.substring(k+1);
  CMethodCaller mCaller= new CMethodCaller();
  Object[] params = {req};
    
  retData = mCaller.loadExec(className, methodName, params);
  //System.out.println("反射调用的返回对象=="+ retData); 
  
  //throw new CMyException("程序错误");
  return (String)retData;	
	   
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
