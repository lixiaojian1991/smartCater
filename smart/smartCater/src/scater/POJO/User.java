package scater.POJO;

public class User {

	public static String[] querySeller_col={"distinct(companyId)","company.name","company.imgPath"};
	public static String[][] querySeller_join={{"left join","company","food.companyId","company.id"}};
	
	public static String[] querySeller_int={"companyId"};
	public static String[] querySeller_string={"name","imgPath"};
	
	public static String[] register_user={"username","password","email","type"};
}
