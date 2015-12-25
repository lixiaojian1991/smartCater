package scater.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import scater.POJO.Proposal;

import csAsc.EIO.MsgEngine.CEIOMsgRouter.CParam;
import csAsc.dbcom.CDbAccessUti;

public class ProposalDAO extends BaseDAO {

	public ProposalDAO(CParam param){
		this.param=param;
	}
	
	public int pubProposal(String[] proposal) throws SQLException{
		String sql=gene_Insert_sql("proposal", Proposal.proposal, proposal);
		return executeInsert(sql);
	}
	
	public ResultSet queryProposal(int page,int pageSize,int type,int userId,int companyId){
		String sql="";
		if(type==2){
			sql=searchByPage("proposal", Proposal.queryPro_col, true, page, pageSize,Proposal.queryPro_join1,
					new String[][]{{"proposal.userId","=",String.valueOf(userId)}}, new String[][]{{"uploadDate","desc"}}, null);
		}else if(type==10){
			sql=searchByPage("proposal", Proposal.queryPro_col, true, page, pageSize, Proposal.queryPro_join2, 
					new String[][]{{"user1.companyId","=",String.valueOf(companyId)}}, new String[][]{{"uploadDate","desc"}}, null);
		}
		return executeQuerySql(sql);
	}

	public int giveUpProposal(String ids, String go) {
		String sql=null;
		if(go.equals("giveup")){
			sql="update proposal set isGive=1 where id in"+ids;
		}else{
			sql="update proposal set isGive=0 where id in"+ids;
		}
		return executeUpdate(sql);
	}

	public ResultSet queryProById(String proId) {
		String sql=searchByPage("proposal",null, false, 0, 0, null, new String[][]{{"id","=",proId}}, null, null);
		return executeQuerySql(sql);
	}

	public int updatePro(int proposalId,String[] updatePro) {
		String sql=gene_update_sql(proposalId, "proposal", Proposal.updatePro_col, updatePro);
		return executeUpdate(sql);
	}

	public int deleteProposal(String ids) throws SQLException{
		String[] sqlList=new String[2];
		sqlList[0]="delete from proposalscore where proposalId in"+ids;
		sqlList[1]="delete from proposal where id in"+ids;
		return executeSqlArray(sqlList);
	}
	
	public int decideProposal(String ids,String go){
		String sql=null;
        if(go.equals("win")){
        	sql = "update proposal set isWin=1 where id in" + ids;
        }else{
        	sql = "update proposal set isWin=0 where id in" + ids;
        }
		return executeUpdate(sql);
	}

	public int commentProposal(int proposalId, String[][] sale_proposalscore) throws SQLException {
		HashMap<Integer, Float> map = new HashMap<Integer, Float>();
		String sql = searchByPage("bidstandards", new String[] {
				"bidstandards.number", "bidstandards.weight" },
				false, 0, 0, new String[][] {
						{ "inner join", "bids", "bidstandards.bidId",
								"bids.id" },
						{ "inner join", "proposal",
								"proposal.bidsId", "bids.id" } },
				null, null, null);
		ResultSet set = ((CDbAccessUti) param.userObj[0]).getBySQL(sql);
		if (set != null) {
			while (set.next()) {
				map.put(set.getInt("number"), set.getFloat("weight"));
			}
		}
		float totalScore = 0;
		String[] sqlList = new String[sale_proposalscore.length + 1];
		for (int i = 0; i < sale_proposalscore.length; i++) {
			sqlList[i] = gene_Insert_sql("proposalscore",
					Proposal.proposalScore, sale_proposalscore[i]);
			int number=Integer.parseInt(sale_proposalscore[i][1]);
			totalScore += Float.valueOf(sale_proposalscore[i][2]).floatValue()*map.get(number)/100;
		}
		sqlList[sale_proposalscore.length] = "update proposal set totalScore='"
				+ totalScore + "' ,set isComment=1 where id=" + proposalId;
		return executeSqlArray(sqlList);
	}
}
