package scater.Service;

import org.json.JSONObject;

import scater.DAO.MaterialDAO;
import csAsc.EIO.MsgEngine.CEIOMsgRouter.CParam;

public class MaterialService {

	private CParam param;
	private JSONObject result;
	private MaterialDAO dao;
	
	
	public MaterialService(CParam param) {
		this.param=param;
		result=new JSONObject();
		dao=new MaterialDAO(param);
	}
	
	public JSONObject InitMaterial(JSONObject data){
		
		return result;
	}
}
