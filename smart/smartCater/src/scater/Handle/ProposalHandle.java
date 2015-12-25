package scater.Handle;

import org.json.JSONObject;

import scater.Service.ProposalService;
import csAsc.EIO.MsgEngine.CEIOMsgRouter.CParam;

public class ProposalHandle extends CMsgBaseHandle {

	@Override
	public int handleMsg(CParam param) throws Exception {
		JSONObject data=getReqMessage(param);
		JSONObject user=getCurrentUser(param);
		String op=data.getString("op");
		JSONObject result=null;
		ProposalService service=new ProposalService(param);
		
		switch(op){
		case "pubProposal":     //发布竞标书
			data.put("user", user);
			result=service.pubProposal(data);
			break;
		case "queryProposal":   //查看自己以往的竞标书
			data.put("user", user);
			result=service.queryProposal(data);
			break;
		case "giveupProposal":  //放弃竞标
			result=service.giveupProposal(data);
			break;
		case "queryProById":   //查看竞标书具体信息
			result=service.queryProById(data);
			break;
		case "updatePro":    //修改竞标书
			result=service.updatePro(data);
			break;
		case "deletePro":    //删除竞标书
			result=service.deletePro(data);
			break;
		case "commentPro":   //评标
			result=service.commentPro(data);
			break;
		case "decidePro":   //定标
			result=service.decidePro(data);
			break;
		}
		param.respData.append(result.toString());
		return 0;
	}
}
