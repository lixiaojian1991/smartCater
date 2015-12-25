package scater.Handle;

import org.json.JSONObject;

import scater.Service.BidService;
import csAsc.EIO.MsgEngine.CEIOMsgRouter.CParam;

public class BidHandle extends CMsgBaseHandle {

	@Override
	public int handleMsg(CParam param) throws Exception {
		JSONObject data=getReqMessage(param);
		JSONObject user=getCurrentUser(param);
		String op=data.getString("op");
		JSONObject result=null;
		BidService service=new BidService(param);
		
		switch(op){
		case "queryBids":  //查询所有企业发布的标书,用户查看的
			result=service.queryBids(data);
			break;
		case "pubBid":  //发布新标书
			result=service.publicBid(data);
			break;
		case "pubBidInit":  //发布标书需要初始化的数据
			data.put("user", user);
			result=service.pubBidInit(data);
			break;
		case "queryBidById":  //查看招标书详情
			result=service.queryBidById(data);
			break;
		case "queryProByBidId":  //根据招标书查看竞标书
			result=service.queryProByBidId(data);
			break;
		}
		param.respData.append(result.toString());
		return 0;
	}
}
