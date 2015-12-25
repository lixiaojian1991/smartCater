package csAsc.dbcom;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.Properties;

//import cn.edu.scut.common.JDBCConnection;

public class DBAccess {
	// private String drv="com.mysql.jdbc.Driver";

	// private String url="jdbc:mysql://localhost:3396/jcfxdb";

	// private String usr="root";

	// private String pwd="root";

	public Connection conn = null;

	public PreparedStatement stm = null;

	public ResultSet rs = null;
	public static String url;
	/**
	 * public String getDrv() { return drv; }
	 * 
	 * public void setDrv(String drv) { this.drv = drv; }
	 * 
	 * public String getUrl() { return url; }
	 * 
	 * public void setUrl(String url) { this.url = url; }
	 * 
	 * public String getUsr() { return usr; }
	 * 
	 * public void setUsr(String usr) { this.usr = usr; }
	 * 
	 * public String getPwd() { return pwd; }
	 * 
	 * public void setPwd(String pwd) { this.pwd = pwd; }
	 **/
	public Connection getConn() {
		return conn;
	}

	//public void setConn(Connection conn) {
		//this.conn = conn;
	//}

	public PreparedStatement getStm() {
		return stm;
	}

	public void setStm(PreparedStatement stm) {
		this.stm = stm;
	}

	public ResultSet getRs() {
		return rs;
	}

	public void setRs(ResultSet rs) {
		this.rs = rs;
	}
/**
	public boolean getConnection(){
	conn=JDBCConnection.getConnection();
	if(conn!=null){return true;}
	else return false;
}
**/
	// 锟斤拷锟斤拷锟斤拷菘锟斤拷锟斤拷雍锟斤拷锟�
	public boolean createConn() {
		//Properties props = new Properties();
		try {
		//InputStream in = new BufferedInputStream (new FileInputStream(DBAccess.class.getClassLoader().getResource("/database.properties").getPath().replace("%20", " ")));
	    //props.load(in);
	    //url = props.getProperty("url");
		String url = "jdbc:mysql://localhost:3306/ecoss";
		Class.forName("com.mysql.jdbc.Driver");
		String name = "root";
		String pwd = "csasc";
		conn = DriverManager.getConnection(url, name,
					pwd);
		return true;
		} catch (Exception e) {
			return false;
		}

	}

	// 锟斤拷锟斤拷sql锟斤拷锟斤拷
	public boolean update(String sql) {
		boolean b = false;
		try {
			stm = (PreparedStatement) conn.prepareStatement(sql);
			stm.execute(sql);
			b = true;
		} catch (Exception e) {
		}
		return b;
	}

	// 锟斤拷询sql锟斤拷锟斤拷
	public ResultSet query(String sql) {
		try {
			stm = (PreparedStatement) conn.prepareStatement(sql);
			rs = (ResultSet) stm.executeQuery(sql);
		} catch (Exception e) {
		}
		return rs;
	}

	public boolean next() {
		boolean b = false;
		try {
			if (rs.next())
				b = true;
		} catch (Exception e) {
		}
		return b;
	}

	public String getValue(String field) {
		String value = null;
		try {
			if (rs != null)
				value = rs.getString(field);
		} catch (Exception e) {
		}
		return value;
	}

	public void closeRs() {
		try {
			if (rs != null)

				rs.close();
		} catch (SQLException e) {
		}

	}

	public void closeStm() {
		try {
			if (rs != null)

				stm.close();
		} catch (SQLException e) {
		}

	}

	public void closeConn() {
		try {
			if (rs != null)

				conn.close();
		} catch (SQLException e) {
		}

	}

}
