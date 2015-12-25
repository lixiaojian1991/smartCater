package scater.Handle;

import org.json.JSONObject;

import csAsc.EIO.MsgEngine.CEIOMsgRouter.CParam;

public class MaterialHandle extends CMsgBaseHandle {

	@Override
	public int handleMsg(CParam param) throws Exception {
        JSONObject data=getReqMessage(param);
		
		String op=data.getString("op");
		JSONObject result=null;
		
		switch(op){
		
		}
		param.respData.append(result.toString());
		return super.handleMsg(param);
	}
}
