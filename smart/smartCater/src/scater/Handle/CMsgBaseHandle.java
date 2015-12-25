package scater.Handle;

import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import csAsc.EIO.MsgEngine.CEIOMsgRouter.CParam;

public class CMsgBaseHandle {

	public JSONObject getReqMessage(CParam param) throws Exception{
		System.out.println("进入消息处理器");
		HttpServletRequest msgReq;
		msgReq = param.getMsgReq();
		msgReq.setCharacterEncoding("UTF-8");
		StringBuffer reqMsgData = new StringBuffer();
		reqMsgData.append(msgReq.getParameter(param.getReqMsgDataId())); //从消息中获取消息数据体（JSON字符串)
		JSONObject msgDataObj = new JSONObject(reqMsgData.toString());   //请求参数JSON格式
		return msgDataObj;
	}
	
	public HttpSession getSession(CParam param){
		HttpSession session=param.getMsgReq().getSession(false);  //true的时候，没有的时候不创建
		return session;
	}
	
	public JSONObject getCurrentUser(CParam param){
		HttpSession session=getSession(param);
		if(session==null){
			return null;
		}
		return (JSONObject) session.getAttribute("user");
	}
	
	public int handleMsg(CParam param) throws Exception {
		return 0;
	}
}
