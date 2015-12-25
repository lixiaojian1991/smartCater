package scater.Handle;

import javax.servlet.http.HttpSession;
import org.json.JSONObject;

import scater.Service.FoodOrderService;
import csAsc.EIO.MsgEngine.CEIOMsgRouter.CParam;

public class FoodOrderHandle extends CMsgBaseHandle {

	@Override
	public int handleMsg(CParam param) throws Exception {
		JSONObject data=getReqMessage(param);
		JSONObject user=getCurrentUser(param);
		String op=data.getString("op");
		JSONObject result=null;
		FoodOrderService service=new FoodOrderService(param);
		
		switch(op){
		case "newOrder":   //下订单
			data.put("user", user);
			result=service.newOrder(data);
			break;
		case "payOrder":  //支付功能，目前只是一个模拟接口
			result=service.payOrder(data);
			break;
		case "cancelOrder":  //取消订单
			result=service.cancelOrder(data);
			break;
		case "queryOrder": //查询自己的以往订单信息
			data.put("userId", user.getInt("id"));
			result=service.queryOrder(data);
			break;
		}
		param.respData.append(result.toString());
		return 0;
	}
	
}
