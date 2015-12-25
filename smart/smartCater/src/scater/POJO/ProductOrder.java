package scater.POJO;

public class ProductOrder {

	public static String[] materialOrder={"number","name","userId","date","companyId","orderStatus"};
	public static String[] orderDetail={"orderId","productId","mount","unit","price"};
	
	public static String[] productCart={"userId","productId","mount","price","unit"};
	
	public static String[] queryOrder_int={"id","userId","companyId"};
	public static String[] queryOrder_string={"number","name"};
	public static String[] queryOrder_float=null;
	public static String[] queryOrder_date={"date"};
	
	public static String[] orderDetail_int={"orderId","productId"};
	public static String[] orderDetail_string={"name","unit","briefInfo","imagePath"};
	public static String[] orderDetail_float={"mount","price"};
	public static String[] orderDetail_date=null;
	
	
	public static String[] cart_int={"id","userId","productId"};
	public static String[] cart_string={"unit","briefInfo","name","briefInfo"};
	public static String[] cart_float={"mount","price",};
	public static String[] cart_date=null;
	
	public static String[] procart_int={"productId"};
	public static String[] procart_string={"unit"};
	public static String[] procart_float={"mount","price"};
	public static String[] procart_date=null;
}
