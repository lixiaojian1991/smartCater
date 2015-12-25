package scater.POJO;

public class Bid {

	
	public static String[] querybid_col={"bids.id","bids.name", "bids.pubDate", 
		                            "type","beginDate","endDate","count(proposal.id) as proposalCount" };
	public static String[] querybid_int={"id", "proposalCount"};
	public static String[] querybid_string={"name", "type"};
	public static String[] querybid_float=null;
	public static String[] querybid_date={"pubDate","beginDate","endDate"};
	
	
	public static String[] bids={ "number", "name", "description", "type",
		"content", "startDate", "finishDate", "bidPath", "bidName",
		"beginDate", "endDate", "contact", "tel", "sellerInfo",
		"materials", "userId","username","pubDate" };
	public static String[] bidmaterial={"bidId","type","id","name","mount","unit"};
	public static String[] bidstandards={"bidId","number","name","weight"};
	
	public static String[] bids_int={"id", "userId"};
	public static String[] bids_string={"number","name", "description", "type", "content", "bidPath",
		                        "bidName", "contact", "tel", "sellerInfo", "materials","username" };
	public static String[] bids_float=null;
	public static String[] bids_date={"pubDate","startDate", "finishDate", "beginDate", "endDate"};
	
	public static String[] bidmaterial_int={"bidId", "id"};
	public static String[] bidmaterial_string={"type","name", "unit"};
	public static String[] bidmaterial_float={"mount"};
	public static String[] bidmaterial_date=null;
	
	public static String[] bidstandards_int={"bidId", "number"};
	public static String[] bidstandards_string={"name"};
	public static String[] bidstandards_float={"weight"};
	public static String[] bidstandards_date=null;
	
	public static String[] bidpro_int={"id","bidsId","userId","isWin","isComment"};
	public static String[] bidpro_string={"username","name"};
	public static String[] bidpro_float={"totalScore"};
	public static String[] bidpro_date={"uploadDate"};
}
