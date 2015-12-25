package csAsc.dbcom;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class CDbConn 
{
	public static Connection MySQLConnOpen(String servUrl,String dbName, String userName, String passWord)
			   throws ClassNotFoundException,SQLException
	{//为指定的MySQL数据库服务器建立连接，并返回所建立的连接；
	 //servUrl为要连接的数据库服务器的URL,例如，下面的servUrl表示一个MySQL本地连接：
	 //   servUrl="jdbc:mysql://localhost:3306/"
	 //dbName为要连接的DataBase名称；
	 //userName为要连接的数据库的登陆用户名；
	 //passWord为要连接的数据库的登陆密码；
	 //对于驱动器未发现异常和数据库连接异常，由调用者处理 
		 
	 Class.forName("com.mysql.jdbc.Driver");//加载MySql的驱动类
	 String tServUrlName =servUrl+"/"+dbName;
	 Connection tConn=null;
	 
	 tConn = DriverManager.getConnection(tServUrlName, userName, passWord); //获得连接   
	  
	 return tConn;
	}//CMySQLConnOpen
	
	public static Connection OracleConnOpen(String servUrl,String dbName, String userName, String passWord)
			   throws ClassNotFoundException,SQLException
	{//Oracle数据库连接	
	 return null;
	}
}

