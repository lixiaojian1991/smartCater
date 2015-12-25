package scater.Handle;

import org.json.JSONObject;
import scater.Service.FoodService;
import csAsc.EIO.MsgEngine.CEIOMsgRouter.CParam;

public class FoodHandle extends CMsgBaseHandle {

	@Override
	public int handleMsg(CParam param) throws Exception {
		JSONObject data=getReqMessage(param);
		
		String op=data.getString("op");
		JSONObject result=null;
		FoodService service=new FoodService(param);
		switch(op){
		case "queryOnFood":  //查看所有上架的菜品
			result=service.queryOnFood(data);
			break;
		case "querySelfFood":  //查看某个商家的上架的菜品
			result=service.querySelfFood(data);
			break;
		case "givenFood":  //发放菜品
			result=service.givenFood(data);
			break;
		}
		param.respData.append(result.toString());
		return 0;
	}
}
