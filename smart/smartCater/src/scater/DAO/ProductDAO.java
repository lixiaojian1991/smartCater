package scater.DAO;

import java.sql.ResultSet;

import scater.POJO.Product;
import csAsc.EIO.MsgEngine.CEIOMsgRouter.CParam;

public class ProductDAO extends BaseDAO {

	public ProductDAO(CParam param){
		this.param=param;
	}
	
	public ResultSet queryAllProduct(int page,int pageSize){
		String sql=searchByPage("product", Product.market_total_col, false, page, pageSize, 
				new String[][]{{"inner join","company","company.id","product.companyId"}}, null, null, null);
		return executeQuerySql(sql);
	}
	
	public ResultSet queryProById(String productId){
		String sql=searchByPage("product",null, false, 0, 0, null, new String[][]{{"id","=",productId}}, null,null);
		return executeQuerySql(sql);
	}
	
	public ResultSet queryPriceById(String productId){
		String sql=searchByPage("price",null, false,0, 0,null, new String[][]{{"productId","=",productId}},null,null);
		return executeQuerySql(sql);
	}
	
	public ResultSet querySelfPro(int page,int pageSize,String companyId){
		String sql=searchByPage("product", Product.market_total_col, false, page, pageSize, 
				new String[][]{{"inner join","company","company.id","product.companyId"}}, 
				new String[][]{{"product.companyId","=",companyId}}, null, null);
		return executeQuerySql(sql);
	}
}
