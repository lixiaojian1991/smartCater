package scater.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import scater.DAO.ProductDAO;
import scater.POJO.Product;
import scater.common.Constant;
import csAsc.EIO.MsgEngine.CEIOMsgRouter.CParam;

public class ProductService {

	private CParam param;
	private ProductDAO dao;
	private JSONObject result;
	
	public ProductService(CParam param){
		this.param=param;
		dao=new ProductDAO(param);
		result=new JSONObject();
	}
	
	public JSONObject queryAllProduct(JSONObject data) throws SQLException, JSONException{
		int page=data.getInt("page");
		int pageSize=data.getInt("pageSize");
		ResultSet set=dao.queryAllProduct(page, pageSize);
		JSONArray array=Constant.getJsonArrayFromSet(set, Product.market_int_col, Product.market_string_col, null, null);
		result.put("page", page);
		result.put("pageSize",pageSize);
		result.put("array", array);
		return result;
	}
	
	public JSONObject queryProById(JSONObject data) throws JSONException, SQLException{
		String productId=data.getString("productId");
		ResultSet set=dao.queryProById(productId);
		if(set!=null){
			set.next();
			JSONObject product=Constant.setValue(set, Product.product_int, Product.product_string, null, null);
			result.put("product", product);
		}
		set=dao.queryPriceById(productId);
		JSONArray array=Constant.getJsonArrayFromSet(set, Product.price_int, null, Product.price_float, null);
		result.put("price", array);
		return result;
	}
	
	public JSONObject querySelfPro(JSONObject data) throws JSONException, SQLException{
		int page=data.getInt("page");
		int pageSize=data.getInt("pageSize");
		String companyId=data.getString("companyId");
		ResultSet set=dao.querySelfPro(page, pageSize, companyId);
		JSONArray array=Constant.getJsonArrayFromSet(set, Product.market_int_col, Product.market_string_col, null, null);
		result.put("page", page);
		result.put("companyId", companyId);
		result.put("pageSize",pageSize);
		result.put("array", array);
		return result;
	}
	
}
