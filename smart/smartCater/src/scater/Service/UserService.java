package scater.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import scater.DAO.UserDAO;
import scater.POJO.User;
import scater.common.Constant;
import scater.common.SendMail;
import csAsc.EIO.MsgEngine.CEIOMsgRouter.CParam;


public class UserService {

	private CParam param;
	private JSONObject result;
	private UserDAO dao;
	
	public UserService(CParam param){
		this.param=param;
		result=new JSONObject();
		dao=new UserDAO(param);
	}
	
	public JSONObject login(JSONObject data) throws JSONException, SQLException{
		String username=data.getString("username");
		String password=data.getString("password");
		String sql=dao.searchByPage("user", null, false, 0, 0, null, new String[][]{{"username","=",username}}, null, null);
		ResultSet set=dao.executeQuerySql(sql);
		if(set==null){
			result.put("msg", "没有此用户!");
			return result;
		}
		set.next();
		JSONObject object=Constant.setValue(set,new String[]{"id","type","companyId"}, new String[]{"username","password"}, null,null);
		int type=object.getInt("type");
		if(type!=2&&type!=10){
			result.put("msg", "您没有此权限!");
			return result;
		}
		if(object.getString("password").equals(password)){
			result.put("msg", "登录成功!");
			result.put("user", object);
		}else{
			result.put("msg", "密码不正确!");
		}
		
		return result;
	}
	
	public JSONObject isLogin(HttpSession session) throws JSONException{
		if(session==null){
			result.put("login", false);
		}else if(session.getAttribute("user")==null){
			result.put("login", false);
		}else{
			result.put("login", true);
		}
		return result;
	}
	
	public JSONObject register(JSONObject data) throws JSONException, SQLException{
		data.put("type", 0);
		boolean flagName=dao.existUsername(data.getString("username"));
		if(flagName){
			result.put("msg", "用户名已存在!");
			return result;
		}
		boolean flagEmail=dao.existEmail(data.getString("email"));
		if(flagEmail){
			result.put("msg", "该邮箱已经被注册!");
			return result;
		}
		String[] user=Constant.setValueToArray(User.register_user,data);
		int flag=dao.register(user);
		if(flag>0){
			result.put("msg", "注册成功!");
		}else{
			result.put("msg", "注册失败!");
		}
		return result;
	}
	
	public JSONObject findPassword(JSONObject data) throws JSONException, SQLException, MessagingException{
		ResultSet set=dao.checkUser(data.getString("username"), data.getString("email"));
		if(set==null){
			result.put("msg", "用户名和邮箱不匹配!");
			return result;
		}
		set.next();
		String password=set.getString("password");
		SendMail.sendPassword(data.getString("email"), password);
		result.put("msg", "密码已发送到邮箱!");
		return result;
	}
	public JSONObject querySeller(JSONObject data) throws JSONException, SQLException{
		int page=data.getInt("page");
		int pageSize=data.getInt("pageSize");
		ResultSet set=dao.querySeller(page, pageSize);
		if(set==null){
			result.put("array", "暂时没有商家买菜呢!");
		}else{
			JSONArray array=Constant.getJsonArrayFromSet(set, User.querySeller_int, User.querySeller_string, null, null);
			result.put("array",array);
		}
		result.put("page", page);
		return result;
	}
}
