package scater.POJO;

public class FoodOrder {

   public static String[] foodOrder={"orderStatus","totalprice","orderDate","userId","username","tel"};
   
   public static String[] orderDetail={"orderId","foodStatus","foodId","number","price"};
  
   public static String[] queryOrder_int={"id","orderStatus","userId"};
   public static String[] queryOrder_string={"username","tel"};
   public static String[] queryOrder_float={"totalprice"};
   public static String[] queryOrder_date={"orderDate"};
   
   public static String[][] orderDetail_join={{"left join","food","foodorderdetail.foodId","food.id"},
	                                         {"left join","company","food.companyId","company.id"}};
   public static String[] orderDetail_search={"foodorderdetail.*","food.name as foodName",
	                                          "food.imgPath","company.name as companyName"};
   public static String[] orderDetail_int={"orderId","foodStatus","foodId","number"};
   public static String[] orderDetail_string={"foodName","companyName","imgPath"};
   public static String[] orderDetail_float={"price"};
   public static String[] orderDetail_date=null;
}
