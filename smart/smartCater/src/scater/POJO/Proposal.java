package scater.POJO;

public class Proposal {

	public static String[] proposal={"bidsId","userId","username","uploadDate","proposalPath","isWin",
		"isComment","isGive","registerdMoney","successPath","certificatePath","sellerInfo","tel","contact","totalScore"};
	
	public static String[] queryPro_col={"proposal.id","proposal.bidsId","proposal.uploadDate","totalScore","proposal.isGive",
        "isWin","isComment","proposal.username","company.name","bids.beginDate","bids.endDate"};
	public static String[][] queryPro_join1={{"inner join","bids","proposal.bidsId","bids.id"},
		{"inner join","user","bids.userId","user.id"},
		{"inner join","company","user.companyId","company.id"}};   //普通用户查看竞标书
	public static String[][] queryPro_join2={{"inner join","user user1","proposal.userId","user1.id"},
		{"inner join","bids","proposal.bidsId","bids.id"},
		{"inner join","user user2","bids.userId","user2.id"},
		{"inner join","company","user2.companyId","company.id"}};
	public static String[] queryPro_int={"id","bidsId","isWin","isComment","isGive"};
	public static String[] queryPro_string={"username","name"};
	public static String[] queryPro_float={"totalScore"};
	public static String[] queryPro_date={"uploadDate","beginDate","endDate"};
	
	public static String[] updatePro_col={"proposalPath","registerdMoney","successPath","certificatePath","sellerInfo","tel","contact"};

	public static String[] proDetail_int={"id","bidsId","userId","isWin","isComment","isGive"};
	public static String[] proDetail_string={"username","proposalPath","registerdMoney","successPath","certificatePath","sellerInfo","tel","contact"};
	public static String[] proDetail_float={"totalScore"};
	public static String[] proDetail_date={"uploadDate"};
	
	public static String[] proposalScore={"proposalId","number","score"};

}
