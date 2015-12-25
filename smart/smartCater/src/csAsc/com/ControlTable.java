package csAsc.com;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import csAsc.dbcom.DBAccess;



;

public class ControlTable {
	public ArrayList<ArrayList<String>> authTable=new ArrayList<ArrayList<String>>();
	
	public  List<String>  getPageAuthTable(int authid) {
		
		DBAccess db = new DBAccess();
		List<String> lists=new ArrayList<String>();
		if(db.createConn()){
			String sql = "select pages from controltable where right_id='" + authid + "'";
			ResultSet rs = db.query(sql);
			try{
				while(rs.next()){
				lists.add(rs.getString("pages"));
			}
			}catch (SQLException e) {
				//return 0;
				
			}
			
		}
		db.closeRs();
		db.closeStm();
		db.closeConn();
		return lists;

		
	}
	
	
	/**
	 * 获得指定权限id的控制列表所对应的json格式
	 * @param authid
	 * @return
	 */
	public StringBuffer getControlTable(int authid){
		DBAccess db = new DBAccess();
		StringBuffer controlTable= new StringBuffer();
		List<List> resource=new ArrayList<List>();
		//controlTable.append("{");
		if(db.createConn()){
			String sql = "select pages, element_id, permission from controltable where right_id='" + authid + "'";
			ResultSet rs = db.query(sql);
			try{
				while(rs.next()){
					List<String> oneRow=new ArrayList<String>();
					oneRow.add(rs.getString("pages"));
					oneRow.add(rs.getString("element_id"));
					oneRow.add(Integer.toString(rs.getInt("permission")));
					resource.add(oneRow);
				}
			}catch (SQLException e) {
				System.out.println("数据库读取异常。");
			}			
		}
		db.closeRs();
		db.closeStm();
		db.closeConn();
		
		HashMap<String, StringBuffer> resourceMap=new HashMap<String, StringBuffer> ();
		for(List oneRow: resource){
			StringBuffer oneResource=new StringBuffer();
			if(!resourceMap.containsKey(oneRow.get(0))){
				
				oneResource.append("\""+oneRow.get(0)+"\":[[\""+oneRow.get(1)+"\",\""+oneRow.get(2)+"\"]");
			}else {
				oneResource=resourceMap.get(oneRow.get(0));
				oneResource.append(",[\""+oneRow.get(1)+"\",\""+oneRow.get(2)+"\"]");
			}
			resourceMap.put((String)oneRow.get(0), oneResource);
		}
		Iterator it=resourceMap.entrySet().iterator();
		int count=1;
		while(it.hasNext()){
			Entry entry =(Entry)it.next();
			String oneString="";
			if(count<resourceMap.size()){
				oneString=entry.getValue()+"],";
			}else{
				oneString=entry.getValue()+"]";
			}
			count++;
			controlTable.append(oneString);
		}
		//controlTable.append("}");
		return controlTable;
	}
}









