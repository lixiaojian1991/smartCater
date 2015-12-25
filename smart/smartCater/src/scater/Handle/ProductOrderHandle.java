package scater.Handle;
import org.json.JSONObject;

import scater.Service.ProductOrderService;
import csAsc.EIO.MsgEngine.CEIOMsgRouter.CParam;

public class ProductOrderHandle extends CMsgBaseHandle {

	@Override
	public int handleMsg(CParam param) throws Exception {
		JSONObject data=getReqMessage(param);
		JSONObject user=getCurrentUser(param);
		String op=data.getString("op");
		JSONObject result=null;
		ProductOrderService service=new ProductOrderService(param);
		
		switch(op){
		case "newOrder":  //下食材订单
			data.put("user", user);
			result=service.newOrder(data);
			break;
		case "querySelfOrder":  //查询自己以往订单
			data.put("user",user);
			result=service.querySelfOrder(data);
			break;
		case "payOrder":  //订单支付，暂时只是一个模拟接口
			result=service.payOrder(data);
			break;
		case "queryCart"://查询购物车里的商品
			data.put("user",user);
			result=service.queryCart(data);
			break;
		case "addCart":  //向购物车里添加产品
			data.put("user", user);
			result=service.addCart(data);
			break;
		case "delCart":  //从购物车里删除产品
			result=service.delCart(data);
			break;
		}
		param.respData.append(result.toString());
		return 0;
	}
}
