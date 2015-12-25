package scater.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import scater.POJO.FoodOrder;

import csAsc.EIO.MsgEngine.CEIOMsgRouter.CParam;
import csAsc.dbcom.CDbAccessUti;

public class FoodOrderDAO extends BaseDAO {

	public FoodOrderDAO(CParam param){
		this.param=param;
	}
	
	public int newOrder(String[] foodorder,String[][] orderDetail) throws JSONException, SQLException{
		((CDbAccessUti)param.userObj[0]).getConnection().setAutoCommit(false);
		((CDbAccessUti)param.userObj[0]).getConnection().setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
		try{
			String sql=gene_Insert_sql("foodorder", FoodOrder.foodOrder,foodorder);
			int orderId=executeInsert(sql);
			for(int i=0;i<orderDetail.length;i++){
				orderDetail[i][0]=String.valueOf(orderId);
			    executeInsert(gene_Insert_sql("foodorderdetail",FoodOrder.orderDetail, orderDetail[i]));
			    executeUpdate(updateFoodNumber(Integer.parseInt(orderDetail[i][2]),Integer.parseInt(orderDetail[i][3])));
			}
			((CDbAccessUti)param.userObj[0]).getConnection().commit();
			return 1;
		}catch(Exception e){
			((CDbAccessUti)param.userObj[0]).getConnection().rollback();
			return -1;
		}
	}
	
	public String updateFoodNumber(int foodId,int number){
		StringBuffer sb=new StringBuffer();
		sb.append("update  food set actualnumber=actualnumber+"+number +" where id="+foodId);
		return sb.toString();
	}
	
	public int payOrder(int orderId){
		StringBuffer sb=new StringBuffer();
		sb.append("update foodorder set orderStatus=2 where id="+orderId);
		return executeUpdate(sb.toString());
	}
	
	public int cancelOrder(int orderId) throws SQLException{
		String sql="select id,orderStatus from foodorder where id="+orderId;
		ResultSet set=executeQuerySql(sql);
		if(set!=null){
			set.next();
			int orderStatus=set.getInt("orderStatus");
			if(orderStatus==2){
				return 3;  //已支付，不能取消
			}else if(orderStatus==1){
				sql="update foodorder set orderStatus=3 where id="+orderId;
				int flag=executeUpdate(sql);
				if(flag>0){
					return 1;  //取消成功 
				}else{
					return -1;  //取消失败
				}
			}else{
				return 4;  //已取消
			}
		}else{
			return 2;  //订单不存在
		}
	}
	
	public ResultSet queryOrder(int page,int pageSize,int userId){
		String[][] where={{"userId","=",String.valueOf(userId)}};
		String[][] sort={{"orderDate","desc"}};
		String sql=searchByPage("foodorder", null, true, page, pageSize,null,where,sort ,null);
		return executeQuerySql(sql);
	}
	
	public ResultSet orderDetail(int orderId){
		String[][] where={{"orderId","=",String.valueOf(orderId)}};
		String sql=searchByPage("foodorderdetail", FoodOrder.orderDetail_search, false, 0, 0, 
				   FoodOrder.orderDetail_join, where, null, null);
		return executeQuerySql(sql);
	}
}
