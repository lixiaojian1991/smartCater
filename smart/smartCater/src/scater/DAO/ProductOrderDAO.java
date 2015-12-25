package scater.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import scater.POJO.ProductOrder;
import csAsc.EIO.MsgEngine.CEIOMsgRouter.CParam;
import csAsc.dbcom.CDbAccessUti;

public class ProductOrderDAO extends BaseDAO {

	public ProductOrderDAO(CParam param){
		this.param=param;
	}
	
	public int newOrder(String[] materialOrder,String[][] orderDetail) throws SQLException{
		((CDbAccessUti)param.userObj[0]).getConnection().setAutoCommit(false);
		((CDbAccessUti)param.userObj[0]).getConnection().setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
		try{
			String sql=gene_Insert_sql("materialorder", ProductOrder.materialOrder,materialOrder);
			int orderId=executeInsert(sql);
			for(int i=0;i<orderDetail.length;i++){
				orderDetail[i][0]=String.valueOf(orderId);
			    executeInsert(gene_Insert_sql("orderdetail",ProductOrder.orderDetail, orderDetail[i]));
			}
			((CDbAccessUti)param.userObj[0]).getConnection().commit();
		}catch(Exception e){
			((CDbAccessUti)param.userObj[0]).getConnection().rollback();
			return -1;
		}
		return 1;
	}
	
	public ResultSet querySelfOrder(int userId,int page,int pageSize){
		String sql=searchByPage("materialorder", null, true, page, pageSize, 
				null, new String[][]{{"userId","=",String.valueOf(userId)}}, 
				new String[][]{{"date","desc"}},null);
		return executeQuerySql(sql);
	}
	
	public ResultSet queryOrderDetail(int orderId){
		String sql=searchByPage("orderdetail", new String[]{"orderdetail.*","name","briefInfo","imagePath"}, false, 0, 0,
				new String[][]{{"left join","product","orderdetail.productId","product.id"}},
				new String[][]{{"orderId","=",String.valueOf(orderId)}},null,null);
		return executeQuerySql(sql);
	}
	
	public ResultSet queryProsByCarts(String ids){
		String sql="select * from productcart where id in("+ids+")";;
		return executeQuerySql(sql);
	}
	public int payOrder(int orderId){
		String sql=gene_update_sql(orderId, "materialorder", new String[]{"orderStatus"},new String[]{"1"});
		return executeUpdate(sql);
	}
	
	public ResultSet queryCart(int userId){
		String sql=searchByPage("productcart", new String[]{"productcart.*","name","briefInfo","imagePath"}, false, 0, 0, 
				new String[][]{{"left join","product","productcart.productId","product.id"}},
				new String[][]{{"userId","=",String.valueOf(userId)}}, null, null);
		return executeQuerySql(sql);
	}
	
	public int addCart(String[] productCart) throws SQLException{
		String sql=gene_Insert_sql("productcart", ProductOrder.productCart, productCart);
		return executeInsert(sql);
	}
	
	public ResultSet existPro(int userId,int productId){
		String[][] where={{"userId","=",String.valueOf(userId)},{"productId","=",String.valueOf(productId)}};
		String sql=searchByPage("productcart", new String[]{"id"}, false, 0, 0, null, 
				where, null, null);
		return executeQuerySql(sql);
	}
	public int delCart(String ids){
		String sql="delete from productcart where id in("+ids+")";
		return executeUpdate(sql);
	}
}
