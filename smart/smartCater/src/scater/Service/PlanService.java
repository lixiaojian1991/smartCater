package scater.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import scater.DAO.PlanDAO;
import scater.POJO.Plan;
import csAsc.EIO.MsgEngine.CEIOMsgRouter.CParam;

public class PlanService {

	private CParam param;
	private JSONObject result;
	private PlanDAO dao;
	
	public PlanService(CParam param){
		this.param=param;
		result=new JSONObject();
		dao=new PlanDAO(param);
	}
	
	
	/**
	 * 采购计划生成
	 * @param data
	 * @return
	 * @throws JSONException 
	 * @throws SQLException 
	 */
	public JSONObject SalePlan(JSONObject data) throws JSONException, SQLException{
		int companyId=data.getJSONObject("user").getInt("companyId");
		int days=data.getInt("days");   //得到当前的天数
		//1、得到当前库存
		ResultSet set=dao.getCurrentStock(companyId);
		JSONObject stock=getCurrentStock(set);
		
		//2、得到近一周的菜品的每天食材的消耗量
		set=dao.getMaterialByWeek(companyId);
		JSONObject totalMaterial=getMaterialAvg(set);
		
		//3、未来几天的食材需求量
		JSONObject need=getNeedMaterial(days, stock, totalMaterial);
		//从数据库连接得出完整信息封装
		
		return result;
	}
	
	/**
	 * 得到菜品销售数据，什么菜，多少份，多少钱
	 * @param data
	 * @return
	 * @throws JSONException 
	 * @throws SQLException 
	 */
	public JSONObject foodPlan(JSONObject data) throws SQLException, JSONException{
		//1、先得出平均的近一周的菜品的平均销售量和价格
		int companyId=data.getJSONObject("user").getInt("companyId");
		ResultSet set=dao.getfoodAvg(companyId);
		JSONArray array=getfoodAvg(set);
		result.put("foodAvg",array);
		
		//2、得到当前库存
		set=dao.getCurrentStock(companyId);
		JSONObject stock=getCurrentStock(set);
		result.put("stock",stock);
		return result;
	}
	
	
	private JSONObject getCurrentStock(ResultSet set) throws JSONException, SQLException{
		HashMap<Integer,Float> material=new HashMap<Integer,Float>();
		HashMap<Integer,Float> relish=new HashMap<Integer,Float>();
		JSONObject object=new JSONObject();
		if(set!=null){
			while(set.next()){
				if(set.getString("category").equals("调料")){
					relish.put(set.getInt("materialId"),set.getFloat("mount"));
				}else{
					material.put(set.getInt("materialId"),set.getFloat("mount"));
				}
			}
		}
		object.put("material", material);
		object.put("relish", relish);
		return object;
	}
	private JSONArray getfoodAvg(ResultSet set) throws SQLException, JSONException{
		if(set==null){
			return new JSONArray();
		}else{
			JSONArray array=new JSONArray();
			while(set.next()){
				JSONObject temp=new JSONObject();
				temp.put("foodId", set.getInt("foodId"));
				temp.put("name", set.getString("name"));
				temp.put("avgCount", Plan.getMin(set.getFloat("avgCount")));
				temp.put("price", set.getFloat("price"));
				array.put(temp);
			}
			return array;
		}
	}
	
	private JSONObject getMaterialAvg(ResultSet set) throws SQLException, JSONException{
		HashMap<Integer,Float> material=new HashMap<Integer,Float>();
		HashMap<Integer,Float> relish=new HashMap<Integer,Float>();
		JSONObject object=new JSONObject();
		if(set!=null){
			while(set.next()){
				if(set.getString("type").equals("调料")){
					relish.put(set.getInt("id"),set.getFloat("totalCount")/7);
				}else{
					material.put(set.getInt("id"),set.getFloat("totalCount")/7);
				}
			}
		}
		object.put("material", material);
		object.put("relish", relish);
		return object;
	}
	
	private JSONObject getNeedMaterial(int days,JSONObject stock,JSONObject totalMaterial){
		JSONObject object=new JSONObject();
		
		return object;
	}
}
