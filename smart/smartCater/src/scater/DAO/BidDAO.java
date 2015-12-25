package scater.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import scater.POJO.Bid;

import csAsc.EIO.MsgEngine.CEIOMsgRouter.CParam;
import csAsc.dbcom.CDbAccessUti;
public class BidDAO extends BaseDAO {

	public BidDAO(CParam param){
		this.param=param;
	}
	
	public ResultSet queryBids(String[] colName, boolean PageBy,
			int page, int pageSize, String status, String sort, String keyWord){
		String[][] joinInfo_array = { { "left join", "proposal",
			"bids.id", "proposal.bidsId" } };
		String[][] sort_array = null;
		String[][] where_array = get_where_sql(status, keyWord);
		String[] group_array = new String[] { "bids.id" };
		if (sort != null && !sort.equals("")) {
			if (sort.equals("date")) { // 按照发布时间的降序排列
				sort_array = new String[][] { { "bids.pubDate", "desc" } };
			} else if (sort.equals("count")) { // 按照竞标书数量的降序排列
				sort_array = new String[][] { { "proposalCount", "desc" } };
			}
		}
		String sql = searchByPage("bids", colName, PageBy, page, pageSize,
				joinInfo_array, where_array, sort_array, group_array);
		return executeQuerySql(sql);
	}
	
	//根据招标书的状态和关键字得到sql语句的where子句
	private String[][] get_where_sql(String status,String keyWord){
		String[][] temp=null;
		String[][] result=null;
		java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
		if(status!=null&&!status.equals("")){
			if(status.equals("will")){  //查找还未开始的招标书
				temp=new String[][]{{"bids.beginDate",">",date.toString()},{}};
			}else if(status.equals("on")){   //查找进行中的招标书
				temp=new String[][]{{"bids.beginDate","<=",date.toString()},{"bids.endDate",">=",date.toString()},{}};
			}else if(status.equals("done")){  //查找已经结束的招标书
				temp=new String[][]{{"bids.endDate","<",date.toString()},{}};
			}else if(status.equals("all")){
				temp=new String[][]{{}};
			}
			if(keyWord!=null&&!keyWord.equals("")){
				temp[temp.length-1]=new String[]{"bids.name","like",keyWord};
			}else{
				result=new String[temp.length-1][];
				System.arraycopy(temp, 0, result, 0,temp.length-1);
			}
		}
		return result;
	}
	
	public int publicBid(String[] bids,String[][] bidmaterial,String[][] bidstandards) throws SQLException{
		((CDbAccessUti) param.userObj[0]).getConnection().setAutoCommit(false);
		((CDbAccessUti) param.userObj[0]).getConnection().setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
		try {
			String insert_sql = gene_Insert_sql("bids",Bid.bids, bids);
			int bidId = executeInsert(insert_sql);
			for (int i = 0; i < bidmaterial.length; i++) {
				bidmaterial[i][0] = String.valueOf(bidId);
				executeInsert(gene_Insert_sql("bidmaterial",
						Bid.bidmaterial, bidmaterial[i]));
			}
			for (int i = 0; i < bidstandards.length; i++) {
				bidstandards[i][0] = String.valueOf(bidId);
				executeInsert(gene_Insert_sql("bidstandards",
						Bid.bidstandards, bidstandards[i]));
			}
			((CDbAccessUti) param.userObj[0]).getConnection().commit();
		} catch (Exception e) {
			((CDbAccessUti) param.userObj[0]).getConnection().rollback();
			return -1;
		}
		return 1;
	}
	
	public ResultSet queryRelish(int companyId){
		String sql=searchByPage("relish", new String[]{"id","name"}, false, 0, 0, null, 
				new String[][]{{"companyId","=",String.valueOf(companyId)}}, null, null);
		return executeQuerySql(sql);
	}
	
	public ResultSet queryMaterial(int companyId){
		String sql=searchByPage("material", new String[]{"id","name"}, false, 0, 0, null, 
				new String[][]{{"companyId","=",String.valueOf(companyId)}}, null, null);
		return executeQuerySql(sql);
	}
	public ResultSet queryBidById(int bidId){
		String[][] where = { { "bids.id", "=", String.valueOf(bidId) } };
		String sql = searchByPage("bids", null, false, 0, 0, null,
				where, null, null);
		return executeQuerySql(sql);
	}
	
	public ResultSet queryBidmaterialById(int bidId){
		String[][] where = { { "bidmaterial.bidId", "=", String.valueOf(bidId) } };
		String sql = searchByPage("bidmaterial",
				null, false, 0, 0, null, where, null, null);
		return executeQuerySql(sql);
	}
	
	public ResultSet queryBidstandardById(int bidId){
		String[][] where = { { "bidstandards.bidId", "=", String.valueOf(bidId) } };
		String sql = searchByPage("bidstandards",
				null, false, 0, 0, null, where, null, null);
		return executeQuerySql(sql);
	}
	public ResultSet queryProByBidId(int bidId,int page,int pageSize){
		String sql = searchByPage("proposal", new String[]{"proposal.*","company.name"}, true, page, pageSize,
				new String[][]{{"inner join","user","proposal.userId","user.id"},{"inner join","company","user.companyId","company.id"}},
				new String[][] { { "bidsId", "=", String.valueOf(bidId) } ,{"proposal.isGive","=","0"}},
				new String[][] { { "uploadDate", "desc" } }, null);
		return executeQuerySql(sql);
	}
	
}
