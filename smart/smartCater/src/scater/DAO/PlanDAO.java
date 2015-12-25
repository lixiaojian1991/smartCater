package scater.DAO;

import java.sql.ResultSet;

import csAsc.EIO.MsgEngine.CEIOMsgRouter.CParam;

public class PlanDAO extends BaseDAO {

	public PlanDAO(CParam param){
		this.param=param;
	}
	
	//得到近一周的菜品销售量和销售价钱
	public ResultSet getfoodAvg(int companyId){
		String sql="SELECT foodId,name,COUNT(foodId)/7 as avgCount,AVG(foodorderdetail.price)as price"+
				" from foodorder"+
				" LEFT JOIN foodorderdetail on foodorder.id=foodorderdetail.orderId"+
				" LEFT JOIN food on foodorderdetail.foodId=food.id"+
				" WHERE orderDate>date_sub(NOW(),interval 7 day) and companyId="+companyId+
				" GROUP BY foodId";
		return executeQuerySql(sql);
	}
	
	
	//查看当前库存
	public ResultSet getCurrentStock(int companyId){
		String sql="SELECT stock.category,materialId,relish.name,mount,unit"+
				" from stock"+
				" INNER JOIN relish on relish.id=stock.materialId"+
				" where stock.companyId=2 and stock.category='调料'"+
				" UNION"+
				" SELECT stock.category,materialId,material.name,mount,unit"+
				" from stock"+
				" INNER JOIN material on material.id=stock.materialId"+
				" where stock.companyId=2 and stock.category='食材'";
		return executeQuerySql(sql);
	}
	
	
	//得到近一周的食材总量
	public ResultSet getMaterialByWeek(int companyId){
		String sql="SELECT foodmaterial.id,material.name,sum(foodmaterial.dosage) as totalCount,foodmaterial.unit,foodmaterial.type"+
				" from foodorder"+
				" INNER JOIN foodorderdetail on foodorder.id=foodorderdetail.orderId"+
				" INNER JOIN food on foodorderdetail.foodId=food.id and food.companyId="+companyId+
				" INNER JOIN foodmaterial on food.id=foodmaterial.foodId and foodmaterial.type='食材'"+
				" INNER JOIN material on foodmaterial.id=material.id"+
				" where foodorder.orderDate>DATE_SUB(NOW(),INTERVAL 7 DAY) "+
				" GROUP BY foodmaterial.id"+
				" UNION"+
				" SELECT foodmaterial.id,relish.name,sum(foodmaterial.dosage) as totalCount,foodmaterial.unit,foodmaterial.type"+
				" from foodorder"+
				" INNER JOIN foodorderdetail on foodorder.id=foodorderdetail.orderId"+
				" INNER JOIN food on foodorderdetail.foodId=food.id and food.companyId="+companyId+
				" INNER JOIN foodmaterial on food.id=foodmaterial.foodId and foodmaterial.type='调料'"+
				" INNER JOIN relish on foodmaterial.id=relish.id"+
				" where foodorder.orderDate>DATE_SUB(NOW(),INTERVAL 7 DAY)"+
				" GROUP BY foodmaterial.id";
		return executeQuerySql(sql);
	}
	
}
