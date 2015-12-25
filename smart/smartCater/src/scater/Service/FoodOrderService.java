package scater.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import scater.DAO.FoodOrderDAO;
import scater.POJO.FoodOrder;
import scater.common.Constant;
import csAsc.EIO.MsgEngine.CEIOMsgRouter.CParam;

public class FoodOrderService {

	private CParam param;
	private JSONObject result;
	private FoodOrderDAO dao;
	
	public FoodOrderService(CParam param){
		this.param=param;
		result=new JSONObject();
		dao=new FoodOrderDAO(param);
	}
	
	public JSONObject newOrder(JSONObject data) throws JSONException, SQLException{
		data.put("userId", data.getJSONObject("user").getInt("id"));
		data.put("username", data.getJSONObject("user").getString("username"));
		data.put("orderStatus", 1);
		data.put("orderDate",new java.sql.Date(System.currentTimeMillis()));
		String[] foodOrder=Constant.setValueToArray(FoodOrder.foodOrder,data);
		JSONArray array=data.getJSONArray("orderDetail");
		for(int i=0;i<array.length();i++){
			array.getJSONObject(i).put("orderId",0);
			array.getJSONObject(i).put("foodStatus",0);
		}
		String[][] orderDetail=Constant.setArrayToArray(FoodOrder.orderDetail, array);
		int flag=dao.newOrder(foodOrder, orderDetail);
		if(flag>0){
			result.put("msg","下单成功!");
		}else{
			result.put("msg", "下单失败，请重试!");
		}
		return result;
	}
	
	public JSONObject payOrder(JSONObject data) throws JSONException{
		int orderId=data.getInt("orderId");
		int status=1;
		if(status==1){  //支付宝返回来的数据
			int flag=dao.payOrder(orderId);
			if(flag>0){
				result.put("msg", "支付成功!");
			}else{
				result.put("msg", "支付失败!");
			}
		}else{
			result.put("msg", "支付失败!");
		}
		return result;
	}
	
	public JSONObject cancelOrder(JSONObject data) throws SQLException, JSONException{
		int orderId=data.getInt("orderId");
		int status=dao.cancelOrder(orderId);
		switch(status){
		case -1:
			result.put("msg", "取消失败,请重试!");
			break;
		case 1:
			result.put("msg", "订单已取消!");
			break;
		case 2:
			result.put("msg", "订单不存在!");
			break;
		case 3:
			result.put("msg", "订单已支付，不能取消!");
			break;
		case 4:
			result.put("msg", "订单已取消");  //不用重复操作
			break;
		}
		return result;
	}
	
	public JSONObject queryOrder(JSONObject data) throws JSONException, SQLException{
		int page=data.getInt("page");
		int pageSize=data.getInt("pageSize");
		int userId=data.getInt("userId");
		ResultSet set=dao.queryOrder(page, pageSize, userId);
		if(set==null){
			result.put("order", "暂时没有订单哦!");
		}else{
			JSONArray array=new JSONArray();
			JSONArray orderArray=Constant.getJsonArrayFromSet(set, FoodOrder.queryOrder_int, FoodOrder.queryOrder_string, 
					             FoodOrder.queryOrder_float, FoodOrder.queryOrder_date);
		    for(int i=0;i<orderArray.length();i++){
		    	JSONObject order=orderArray.getJSONObject(i);
		    	JSONArray temp=queryOrderDetail(order.getInt("id"));
		    	order.put("orderDetail", temp);
		    	array.put(order);
		    }
		    result.put("order",array);
		}
		result.put("page",page);
		return result;
	}
	
	public JSONArray queryOrderDetail(int orderId) throws SQLException, JSONException{
		ResultSet set=dao.orderDetail(orderId);
		if(set==null){
			return new JSONArray();
		}else{
			JSONArray array=Constant.getJsonArrayFromSet(set, FoodOrder.orderDetail_int, FoodOrder.orderDetail_string, 
					        FoodOrder.orderDetail_float, FoodOrder.orderDetail_date);
			return array;
		}
	}
	
}
