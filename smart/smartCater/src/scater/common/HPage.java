package scater.common;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONException;
import org.json.JSONObject;

import scater.DAO.BaseDAO;

public class HPage {

	private int page;
	private int totalPage;
	private int pageSize;
	private BaseDAO baseDao;
	private int totalRecord;
	private String sql;
	private JSONObject json;
	
	public HPage(int page,int pageSize,BaseDAO baseDao,String sql){
		this.page=page;
		this.pageSize=pageSize;
		this.baseDao=baseDao;
		this.sql=sql;
	}
	
	public void ComputeTotalPage(){
		ResultSet set=baseDao.executeQuerySql(sql);
		try {
			totalRecord=baseDao.getCountByResultSet(set);
		} catch (SQLException e) {
			System.out.println("计算总记录数时出错!!!!");
			e.printStackTrace();
		}
		totalPage=totalRecord%pageSize==0?totalRecord/pageSize:totalRecord/pageSize+1;
	}
	
	public JSONObject toJSON(){
		ComputeTotalPage();
		json=new JSONObject();
		try {
			json.put("page", page);
			json.put("totalPage", totalPage);
			json.put("pageSize", pageSize);
			json.put("totalRecord",totalRecord);
		} catch (JSONException e) {
			System.out.println("封装有关分页的时候出错啦!!!");
			e.printStackTrace();
		}
		return json;
	}
	
	@Override
	public String toString() {
		return json.toString();
	}
}
