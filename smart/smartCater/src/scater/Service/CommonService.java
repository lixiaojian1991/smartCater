package scater.Service;

import org.json.JSONObject;
import scater.DAO.CommonDAO;
import csAsc.EIO.MsgEngine.CEIOMsgRouter.CParam;

public class CommonService {

	private CParam param;
	private JSONObject result;
	private CommonDAO dao;
	
	public CommonService(CParam param) {
		this.param=param;
		this.result=new JSONObject();
		this.dao=new CommonDAO(param);
	}
	
	
}
