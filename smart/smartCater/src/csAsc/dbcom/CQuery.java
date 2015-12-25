package csAsc.dbcom;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import csAsc.dbcom.*;
public class CQuery 
{
 
	
 public static ResultSet getRecsBySql(String sqlStr)
 {
  //访问MySQL示例：打开一个已经建立好的数据库表，将其中的记录显示出来
  //数据库名test，表名与结构staff（no, name, score）
  String tServUrl="jdbc:mysql://localhost:3306";
  String tUserName="root";
  String tPassWord="csasc";
  String tDbName ="test";
  Connection tConn=null;
  Statement tStmt=null;
  	  
  try
  {
  
   tConn = CDbConn.MySQLConnOpen(tServUrl, tDbName,tUserName, tPassWord);
   tStmt = tConn.createStatement();
  }
  catch(ClassNotFoundException se)
  {System.out.println("数据库无法连接：驱动未找到");    
   se.printStackTrace() ;    
  }
  catch(SQLException se)
  {System.out.println("数据库连接失败:或者数据库Server未启动，或者用户名、密码错误，或者数据库名错误");    
   se.printStackTrace() ;    
  }
  
  ResultSet tRs=null;
  try
  {	 
   tRs = tStmt.executeQuery(sqlStr); 
   /*
   while(tRs.next())
   {// 选择name这列数据
	tName = tRs.getString("name");
	// 首先使用ISO-8859-1字符集将name解码为字节序列并将结果存储新的字节数组中。
	// 然后使用GB2312字符集解码指定的字节数组
	//  tName = new String(tName.getBytes("ISO-8859-1"),"GB2312");
	// 输出结果
	//System.out.println(tName + "\t"+tRs.getString("no") +"\t"+ tRs.getString("score") );
	   
   }  */
	  
	//  tStmt.close();
	//  tRs.close();
	//  tConn.close();  
   } 
   catch (SQLException e) 
	{System.out.println("MySQL操作错误");
     e.printStackTrace();
    }	 
    
 return tRs;	 
 }
}
