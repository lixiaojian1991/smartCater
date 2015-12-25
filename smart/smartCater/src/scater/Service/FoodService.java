package scater.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import scater.DAO.FoodDAO;
import scater.POJO.Food;
import scater.common.Constant;
import csAsc.EIO.MsgEngine.CEIOMsgRouter.CParam;

public class FoodService {

	private CParam param;
	private JSONObject result;
	private FoodDAO dao;
	
	public FoodService(CParam param){
		this.param=param;
		result=new JSONObject();
		dao=new FoodDAO(param);
	}
	
	public JSONObject queryOnFood(JSONObject data) throws JSONException, SQLException{
		int page=data.getInt("page");
		int pageSize=data.getInt("pageSize");
		String sql=dao.searchByPage("food", Food.onFood, true, page, pageSize,Food.onFood_join, Food.onFood_where, null, null);
		ResultSet set=dao.executeQuerySql(sql);
		JSONArray array=Constant.getJsonArrayFromSet(set, Food.onFood_int, Food.onFood_string, Food.onFood_float,null);
		result.put("page", page);
		result.put("array", array);
		return result;
	}
	
	public JSONObject querySelfFood(JSONObject data) throws JSONException, SQLException{
		int page=data.getInt("page");
		int pageSize=data.getInt("pageSize");
		String companyId=data.getString("companyId");
		String[][] where=new String[][]{{"food.isSell","=","1"},{"food.companyId","=",companyId}};
		String sql=dao.searchByPage("food", Food.onFood, true, page, pageSize,Food.onFood_join, where, null, null);
		ResultSet set=dao.executeQuerySql(sql);
		JSONArray array=Constant.getJsonArrayFromSet(set, Food.onFood_int, Food.onFood_string, Food.onFood_float,null);
		result.put("companyId", companyId);
		result.put("page", page);
		result.put("array", array);
		return result;
	}
	
	public JSONObject givenFood(JSONObject data) throws JSONException{
		int orderId=data.getInt("orderId");
		String foodIds=data.getString("foodIds");
		int flag=dao.givenFood(orderId, foodIds);
		if(flag>0){
			result.put("msg", "操作成功!");
		}else{
			result.put("msg", "操作失败,请重试!");
		}
		return result;
	}
}
