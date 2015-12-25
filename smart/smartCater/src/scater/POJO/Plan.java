package scater.POJO;

public class Plan {

	public static String[] foodAvg={"foodId","name","avgCount","price"};
	
	public static String[] currentStock={"category","materialId","name","mount","unit"};
	public static String[] stock_int={"materialId"};
	public static String[] stock_string={"category","name","unit"};
	public static String[] stock_float={"mount"};
	
	public static int getMin(float count){
		return (int)(count+1);
	}
	
	
	/*public static void main(String[] args) {
		
		float a=(float) (5.0/7);
		System.out.println((int)(a+1));
	}*/
}
