package scater.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONObject;
import scater.POJO.User;
import csAsc.EIO.MsgEngine.CEIOMsgRouter.CParam;

public class UserDAO extends BaseDAO {

	public UserDAO(CParam param){
		this.param=param;
	}
	
	public ResultSet querySeller(int page,int pageSize){
		String sql=searchByPage("food", User.querySeller_col, true, page, pageSize, User.querySeller_join,null, null,null);
		return executeQuerySql(sql);
	}
	
	public int register(String[] user) throws SQLException{
		String sql=gene_Insert_sql("user", User.register_user, user);
		return executeInsert(sql);
	}
	
	public boolean existUsername(String username){
		String sql="select id from user where username='"+username+"'";
		ResultSet set=executeQuerySql(sql);
		if(set!=null){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean existEmail(String email){
		String sql="select id from user where email='"+email+"'";
		ResultSet set=executeQuerySql(sql);
		if(set!=null){
			return true;
		}else{
			return false;
		}
	}
	
	public ResultSet checkUser(String username,String email){
		String sql="select id,username,email,password from user where username='"+username+"' and email='"+email+"'";
		return executeQuerySql(sql);
	}
}
