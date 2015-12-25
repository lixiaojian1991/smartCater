package scater.Service;

import org.json.JSONObject;

import scater.DAO.CategoryDAO;
import csAsc.EIO.MsgEngine.CEIOMsgRouter.CParam;

public class CategoryService {

	private CParam param;
	private JSONObject result;
	private CategoryDAO dao;
	
	public CategoryService(CParam param) {
		this.param=param;
		this.result=new JSONObject();
		this.dao=new CategoryDAO(param);
	}
}
