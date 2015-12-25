package scater.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import scater.DAO.ProposalDAO;
import scater.POJO.Proposal;
import scater.common.Constant;
import csAsc.EIO.MsgEngine.CEIOMsgRouter.CParam;

public class ProposalService {

	private CParam param;
	private JSONObject result;
	private ProposalDAO dao;
	
	public ProposalService(CParam param){
		this.param=param;
		dao=new ProposalDAO(param);
		result=new JSONObject();
	}
	
	public JSONObject pubProposal(JSONObject data) throws JSONException, SQLException{
		data.put("userId", data.getJSONObject("user").getInt("id"));
		data.put("username",data.getJSONObject("user").getString("username"));
		data.put("uploadDate",new java.sql.Date(System.currentTimeMillis()));
		data.put("isWin", 2);
		data.put("isComment", 0);
		data.put("isGive", 0);
		data.put("totalScore",0);
		String[] proposal=Constant.setValueToArray(Proposal.proposal, data);
		int flag=dao.pubProposal(proposal);
		if(flag>0){
			result.put("msg","发布竞标书成功!");
		}else{
			result.put("msg","发布竞标书失败!");
		}
		return result;
	}
	
	public JSONObject queryProposal(JSONObject data) throws JSONException{
		int page=data.getInt("page");
		int pageSize=data.getInt("pageSize");
		int userId=data.getJSONObject("user").getInt("id");
		int type=data.getJSONObject("user").getInt("type");
		int companyId=data.getJSONObject("user").getInt("companyId");
		ResultSet set=dao.queryProposal(page, pageSize, type,userId,companyId);
		JSONArray array=Constant.getJsonArrayFromSet(set, Proposal.queryPro_int, 
				Proposal.queryPro_string, Proposal.queryPro_float, Proposal.queryPro_date);
		result.put("page", page);
		result.put("pageSize", pageSize);
		result.put("array", array);
		return result;
	}
	
	public JSONObject giveupProposal(JSONObject data) throws JSONException{
		String ids=data.getString("ids");
		String go=data.getString("go");
		ids="("+ids+")";
		int flag=dao.giveUpProposal(ids, go);
		if(flag>0){
			result.put("msg", "操作成功!");
		}else{
			result.put("msg", "操作失败!");
		}
		return result;
	}
	
	public JSONObject queryProById(JSONObject data) throws JSONException, SQLException{
		String proId=data.getString("proposalId");
		ResultSet set=dao.queryProById(proId);
		if(set!=null){
			set.next();
		}
		JSONObject pro=Constant.setValue(set, Proposal.proDetail_int,
				Proposal.proDetail_string, Proposal.proDetail_float, Proposal.proDetail_date);
		result.put("proposal", pro);
		return result;
	}
	
	public JSONObject updatePro(JSONObject data) throws JSONException{
		int proposalId=data.getInt("proId");
		String[] updatePro=Constant.setValueToArray(Proposal.updatePro_col, data);
		int flag=dao.updatePro(proposalId,updatePro);
		if(flag>0){
			result.put("msg", "修改成功!");
		}else{
			result.put("msg", "修改失败!");
		}
		return result;
	}
	
	public JSONObject deletePro(JSONObject data) throws JSONException, SQLException{
		String ids=data.getString("ids");
		ids="("+ids+")";
		int flag=dao.deleteProposal(ids);
		if(flag>0){
			result.put("msg", "删除成功!");
		}else{
			result.put("msg", "删除失败!");
		}
		return result;
	}
	
	public JSONObject commentPro(JSONObject data) throws JSONException, SQLException{
		int proposalId=data.getInt("proposalId");
		JSONArray array=data.getJSONArray("comment");
		String[][] sale_proposalscore=Constant.setArrayToArray(Proposal.proposalScore, array);
		int flag=dao.commentProposal(proposalId,sale_proposalscore);
		if(flag>0){
			result.put("msg", "评标成功!");
		}else{
			result.put("msg", "评标失败!，请重试!");
		}
		return result;
	}
	public JSONObject decidePro(JSONObject data) throws JSONException{
		String ids=data.getString("ids");
		String go=data.getString("go");
		String sql="("+ids+")";
		int flag=dao.decideProposal(sql,go);
		if(flag>0){
			result.put("msg", "定标成功!");
		}else{
			result.put("msg", "定标失败!，请重试!");
		}
		return result;
	}
}
