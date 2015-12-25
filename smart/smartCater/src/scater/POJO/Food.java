package scater.POJO;

public class Food {

	public static String[] onFood={"food.id","food.name as foodName","price","plannumber","actualnumber","food.imgPath","companyId","company.name as companyName"};
	public static String[][] onFood_join={{"left join","company","company.id","food.companyId"}};
	public static String[][] onFood_where={{"food.isSell","=","1"}};
	public static String[] onFood_int={"id","companyId","plannumber","actualnumber"};
	public static String[] onFood_string={"foodName","imgPath","companyName"};
	public static String[] onFood_float={"price"};
}
