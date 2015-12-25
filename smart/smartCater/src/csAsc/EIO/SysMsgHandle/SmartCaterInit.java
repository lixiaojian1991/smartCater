package csAsc.EIO.SysMsgHandle;

import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import csAsc.EIO.MsgEngine.CEIOMsgRouter.CParam;
import csAsc.dbcom.CDbAccessUti;

public class SmartCaterInit {// 应用系统的初始化程序
	public int SmartCaterInit(CParam param) throws SQLException, JSONException,
			ClassNotFoundException {
		String tServUrl = "jdbc:mysql://localhost:3306";
		String tDbName = "smartcater";
		String tUserName = "root";
		String tPassWord = "root";
		String connStr;
		connStr = param.eioConfigObj.getInitParameter("DatabaseConnection");
		if (connStr == null)
			return 2;// 读取配置文件错误
		if (connStr.equals(""))
			return 2;// 读取配置文件错误

		try {
			JSONObject connObj = new JSONObject(connStr);
			JSONObject jObj = connObj.getJSONObject("MySQLTest");
			tServUrl = jObj.getString("Server");
			tDbName = jObj.getString("Databse");
			tUserName = jObj.getString("User");
			tPassWord = jObj.getString("Password");
		} catch (JSONException e) {
			return 3;
		}
		param.userObj[0] = new CDbAccessUti();
		try {
			((CDbAccessUti) param.userObj[0]).openConn(tServUrl, tDbName,
					tUserName, tPassWord);
		} catch (Exception e) {
			return 10;
		}
		return 0;
	}
}
