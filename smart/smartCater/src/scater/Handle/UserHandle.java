package scater.Handle;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import scater.Service.UserService;
import csAsc.EIO.MsgEngine.CEIOMsgRouter.CParam;

public class UserHandle extends CMsgBaseHandle {

	@Override
	public int handleMsg(CParam param) throws Exception {
		JSONObject data=getReqMessage(param);
		
		String op=data.getString("op");
		JSONObject result=null;
		UserService service=new UserService(param);
		
		switch(op){
		case "login":
			result=service.login(data);
			if(result.getString("msg").equals("登录成功!")){
				HttpSession session=param.getMsgReq().getSession(true);
			    session.setAttribute("user",result.getJSONObject("user"));
			    result.put("JSESSIONID",session.getId());
			}
			break;
		case "findPassword":
			result=service.findPassword(data);
			break;
		case "isLogin":
			HttpSession session=getSession(param);
			result=service.isLogin(session);
			break;
		case "querySeller":
			result=service.querySeller(data);
			break;
		case "logout":
			param.getMsgReq().getSession().invalidate();
			result=new JSONObject();
			result.put("msg", "退出成功!");
			break;
		}
		param.respData.append(result.toString());
		return 0;
	}
}
