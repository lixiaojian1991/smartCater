package scater.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import scater.DAO.ProductOrderDAO;
import scater.POJO.ProductOrder;
import scater.common.Constant;

import com.google.gson.JsonObject;
import csAsc.EIO.MsgEngine.CEIOMsgRouter.CParam;

public class ProductOrderService {

	private CParam param;
	private JSONObject result;
	private ProductOrderDAO dao;
	
    public ProductOrderService(CParam param) {
		this.param=param;
		result=new JSONObject();
		dao=new ProductOrderDAO(param);
	}
    
    public JSONObject newOrder(JSONObject data) throws JSONException, SQLException{
    	data.put("number", System.currentTimeMillis());   //订单编号
    	data.put("orderStatus", 0);  //待付款
    	data.put("userId", data.getJSONObject("user").getInt("id"));
    	data.put("name", data.getJSONObject("user").getString("username"));
    	data.put("companyId", data.getJSONObject("user").getInt("companyId"));
    	data.put("date", new java.sql.Date(System.currentTimeMillis()));
    	String[] materialOrder=Constant.setValueToArray(ProductOrder.materialOrder, data);
    	JSONArray array=queryProsByCarts(data.getString("cartIds"));
    	for(int i=0;i<array.length();i++){
    		array.getJSONObject(i).put("orderId",0);
    	}
    	String[][] orderDetail=Constant.setArrayToArray(ProductOrder.orderDetail, array);
    	int flag=dao.newOrder(materialOrder,orderDetail);
    	if(flag>0){
    		result.put("msg", "下单成功!");
    	}else{
    		result.put("msg", "下单失败!");
    	}
    	return result;
    }
    
    public JSONObject querySelfOrder(JSONObject data) throws JSONException{
    	int userId=data.getJSONObject("user").getInt("id");
    	int page=data.getInt("page");
    	int pageSize=data.getInt("pageSize");
    	ResultSet set=dao.querySelfOrder(userId,page,pageSize);
    	JSONArray orderArray=Constant.getJsonArrayFromSet(set,ProductOrder.queryOrder_int, 
    			ProductOrder.queryOrder_string, ProductOrder.queryOrder_float, ProductOrder.queryOrder_date);
    	JSONArray array=new JSONArray();
    	for(int i=0;i<orderArray.length();i++){
    		JSONObject order=orderArray.getJSONObject(i);
    		JSONArray detail=queryOrderDetail(order.getInt("id"));
    		order.put("detail", detail);
    		array.put(order);
    	}
    	result.put("page", page);
    	result.put("pageSize", pageSize);
    	result.put("array", array);
    	return result;
    }
    
    public JSONArray queryProsByCarts(String ids){
    	ResultSet set=dao.queryProsByCarts(ids);
    	JSONArray array=Constant.getJsonArrayFromSet(set, ProductOrder.procart_int, 
    			ProductOrder.procart_string, ProductOrder.procart_float, ProductOrder.procart_date);
    	return array;
    }
    public JSONArray queryOrderDetail(int orderId){
    	ResultSet set=dao.queryOrderDetail(orderId);
    	JSONArray array=Constant.getJsonArrayFromSet(set,ProductOrder.orderDetail_int, 
    			ProductOrder.orderDetail_string, ProductOrder.orderDetail_float, ProductOrder.orderDetail_date);
    	return array;
    }
    
    public JSONObject payOrder(JSONObject data) throws JSONException{
    	int orderId=data.getInt("orderId");
    	int flag=dao.payOrder(orderId);
    	if(flag>0){
    		result.put("msg", "支付成功!");
    	}else{
    		result.put("msg", "支付失败!");
    	}
    	return result;
    }
    
    public JSONObject queryCart(JSONObject data) throws JSONException{
    	int userId=data.getJSONObject("user").getInt("id");
    	ResultSet set=dao.queryCart(userId);
    	JSONArray array=Constant.getJsonArrayFromSet(set,ProductOrder.cart_int, 
    			ProductOrder.cart_string, ProductOrder.cart_float, ProductOrder.cart_date);
    	result.put("cart",array);
    	return result;
    }
    
    public JSONObject addCart(JSONObject data) throws JSONException, SQLException{
    	data.put("userId", data.getJSONObject("user").getInt("id"));
    	int userId=data.getInt("userId");
    	int productId=data.getInt("productId");
    	ResultSet set=dao.existPro(userId, productId);
    	if(set!=null){
    		result.put("msg", "购物车里已有该商品!");
    		return result;
    	}
    	String[] productCart=Constant.setValueToArray(ProductOrder.productCart,data);
    	int flag=dao.addCart(productCart);
    	if(flag>0){
    		result.put("msg","加入购物车成功!");
    	}else{
    		result.put("msg","加入购物车失败!");
    	}
    	return result;
    }
    
    public JSONObject delCart(JSONObject data) throws JSONException{
    	String ids=data.getString("ids");
    	int flag=dao.delCart(ids);
    	if(flag>0){
    		result.put("msg","移除成功!");
    	}else{
    		result.put("msg","移除失败!");
    	}
    	return result;
    }
}
