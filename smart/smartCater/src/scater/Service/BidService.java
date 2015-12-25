package scater.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import scater.DAO.BidDAO;
import scater.POJO.Bid;
import scater.common.Constant;
import csAsc.EIO.MsgEngine.CEIOMsgRouter.CParam;

public class BidService {

	private CParam param;
	private JSONObject result;
	private BidDAO dao;
	
	public BidService(CParam param){
		this.param=param;
		result=new JSONObject();
		dao=new BidDAO(param);
	}
	
	public JSONObject queryBids(JSONObject data) throws JSONException{
		String status = data.getString("status").trim(); // 招标书的状态
		String sort = data.getString("sort").trim(); // 排序的规则
		String keyWord = data.getString("keyWord").trim(); // 关键字
		int pageSize = data.getInt("pageSize"); // 每页的条数
		int page = data.getInt("page"); // 第几页
		ResultSet set=dao.queryBids(Bid.querybid_col, true, page, pageSize, status, sort, keyWord);
		JSONArray array=Constant.getJsonArrayFromSet(set, Bid.querybid_int,Bid.querybid_string,Bid.querybid_float,Bid.querybid_date);
		result.put("page",page);
		result.put("pageSize",pageSize);
		result.put("bidArray", array);
		return result;
	}
	
	public JSONObject publicBid(JSONObject data) throws JSONException, SQLException{
		data.put("pubDate", new java.sql.Date(System.currentTimeMillis()));
		String[] bids=Constant.setValueToArray(Bid.bids, data);
		String[][] bidmaterial=Constant.setArrayToArray(Bid.bidmaterial, data.getJSONArray("material"));
		String[][] bidstandards=Constant.setArrayToArray(Bid.bidstandards,data.getJSONArray("standards"));
		int flag=dao.publicBid(bids,bidmaterial,bidstandards);
		if(flag>0){
			result.put("msg", "发布标书成功!");
		}else{
			result.put("msg", "发布标书失败，请重试!");
		}
		return result;
	}
	
	public JSONObject pubBidInit(JSONObject data) throws JSONException{
		int companyId=data.getJSONObject("user").getInt("companyId");
		String[] query_int={"id"};
		String[] query_string={"name"};
		ResultSet set=dao.queryMaterial(companyId);
		JSONArray array=Constant.getJsonArrayFromSet(set, query_int, query_string, null,null);
		result.put("material", array);
		set=dao.queryRelish(companyId);
		array=Constant.getJsonArrayFromSet(set, query_int, query_string, null, null);
		result.put("relish",array);
		array=Constant.getCommonBygroup(param, 10);
		result.put("unit", array);
		return result;
	}
	
	public JSONObject queryBidById(JSONObject data) throws JSONException{
		int bidId=data.getInt("bidId");
		ResultSet set=dao.queryBidById(bidId);
		try {
			set.next();
		} catch (SQLException e) {
			result.put("msg", "该标书不存在!");
			return result;
		}
		JSONObject bid=Constant.setValue(set,Bid.bids_int, Bid.bids_string, Bid.bids_float, Bid.bids_date);
		result.put("bid", bid);
		set=dao.queryBidmaterialById(bidId);
		JSONArray array=Constant.getJsonArrayFromSet(set,Bid.bidmaterial_int, Bid.bidmaterial_string, 
				Bid.bidmaterial_float, Bid.bidmaterial_date);
		result.put("material", array);
		set=dao.queryBidstandardById(bidId);
		array=Constant.getJsonArrayFromSet(set, Bid.bidstandards_int, Bid.bidstandards_string, 
				Bid.bidstandards_float, Bid.bidstandards_date);
		result.put("standards", array);
		return result;
	}
	
	public JSONObject queryProByBidId(JSONObject data) throws JSONException{
		int bidId=data.getInt("bidId");
		int page = data.getInt("page");
		int pageSize = data.getInt("pageSize");
		ResultSet set=dao.queryProByBidId(bidId,page,pageSize);
		JSONArray array=Constant.getJsonArrayFromSet(set, Bid.bidpro_int, Bid.bidpro_string, Bid.bidpro_float, Bid.bidpro_date);
		result.put("page", page);
		result.put("pageSize",pageSize);
		result.put("array", array);
		return result;
	}
	
}
