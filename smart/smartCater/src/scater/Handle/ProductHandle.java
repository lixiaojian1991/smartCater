package scater.Handle;

import org.json.JSONObject;

import scater.Service.ProductService;
import csAsc.EIO.MsgEngine.CEIOMsgRouter.CParam;

public class ProductHandle extends CMsgBaseHandle {

	@Override
	public int handleMsg(CParam param) throws Exception {
		JSONObject data=getReqMessage(param);
		
		String op=data.getString("op");
		JSONObject result=null;
		ProductService service=new ProductService(param);
		
		switch(op){
		case "queryAllProduct":   //查询所有产品
			result=service.queryAllProduct(data);
			break;
		case "queryProById":  //查找单个产品的详细信息
			result=service.queryProById(data);
			break;
		case "querySelfPro":  //查看某个商家自己出售的所有产品
			result=service.querySelfPro(data);
			break;
		}
		param.respData.append(result.toString());
		return 0;
	}
}
