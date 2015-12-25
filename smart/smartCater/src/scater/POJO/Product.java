package scater.POJO;

public class Product {

	public static String[] market_total_col = { "product.id", "product.name",
			"briefInfo", "showPrice", "companyId", "imagePath","company.name as companyName" };
	public static String[] market_int_col={"id","companyId"};
	public static String[] market_string_col={"name","briefInfo","showPrice","imagePath","companyName"};
	
	public static String[] product_int={"id","companyId"};
	public static String[] product_string={"name","briefInfo","showPrice","sellerUnit","spefication","payWay",
		    "transSupport","saleprice","brand","category","import","area","saleWay","material","class","special",
		    "garantee","batchDate","manufacture","imagePath"};
	public static String[] price_int={"productId","minNumber","maxNumber"};
	public static String[] price_float={"price"};
}
