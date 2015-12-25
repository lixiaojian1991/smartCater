package scater.DAO;

import csAsc.EIO.MsgEngine.CEIOMsgRouter.CParam;

public class FoodDAO extends BaseDAO {

	public FoodDAO(CParam param){
		this.param=param;
	}
	
	public int givenFood(int orderId,String foodIds){
		String sql="update foodorderdetail set foodStatus=1 where orderId="+orderId+" and foodId in("
				+foodIds+")";
		return executeUpdate(sql);
	}
}
