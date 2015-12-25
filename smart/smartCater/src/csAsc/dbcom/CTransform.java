package csAsc.dbcom;
import java.sql.*;

public class CTransform 
{
 public static int RsToArrayStr(ResultSet rs,StringBuffer arrStr)  
 {//将ResultSet型的数据串行化为字符串（行序）,存入arrStr：每条记录是一个数组元素。由各个字段的数组组成
  //每行的字段用逗号分隔，每行用一对方括号括起来，行间用逗号分隔
	 
 try
 {	 
  ResultSetMetaData rsmd=rs.getMetaData();
  int k = rsmd.getColumnCount(); //列数
  if (k<1) return 1;	  
  
  rs.beforeFirst();//把光标移动到ResulSet对象的开头
  StringBuffer tRowBuffer=new StringBuffer("[");//为提高效率使用StringBuffer  
  	 
  while (rs.next())
  {
	  tRowBuffer.append("\""+rs.getString(1)+"\"") ;
   for (int i = 2; i <= k; i++)
   {
	//System.out.println(rs.getString(i));
	   tRowBuffer.append( ",\""+rs.getString(i)+"\"");
   }
   arrStr.append("["+tRowBuffer+"],");
   tRowBuffer.setLength(0);
  }//while
  	 
 }catch ( SQLException e)
 {
  return 2;	 
 }
 
 if (arrStr.length()<1) return 4;
 arrStr.setCharAt(arrStr.length()-1, ']');
  
 System.out.println("RsToArrayStr===="+arrStr);
 return 0;
  //return "["+tRecsBuffer.substring(0, tRecsBuffer.length()-1)+"]";
 }//RsToArrayStr()
	
 public static String RsToJSONStr(ResultSet rs) throws SQLException 
 {
 //将ResultSet型的数据串行化为字符串（行序）
 //返回形式{"Cols":["no","name","score"],"Recs":[["2011","zhangsan","90"],["2014","lisi","90"]]}
  ResultSetMetaData rsmd=rs.getMetaData();
  int k = rsmd.getColumnCount(); //列数
  if (k<1) return null;
  StringBuffer  tRecsBuffer = new StringBuffer();//为提高效率使用Stringbuffer
  tRecsBuffer.append("{\"Cols\":[");
    
  for(int i=1;i<k;i++)//读取列名
  {
   tRecsBuffer.append("\""+rsmd.getColumnName(i)+"\",");
  }
  tRecsBuffer.append("\""+rsmd.getColumnName(k)+"\"],\"Recs\":[");
  rs.beforeFirst();//把光标移动到ResulSet对象的开头
  int flag=1;//标记是否存在记录
  while (rs.next())
  { 
   if(flag==1)
   {    flag=0; 	}
   StringBuffer tRowBuffer=new StringBuffer();//为提高效率使用StringBuffer
   tRowBuffer.append("\""+rs.getString(1)+"\"") ;
   for (int i = 2; i <= k; i++)
   {//System.out.println(rs.getString(i));
	tRowBuffer.append(",\"" + rs.getString(i)+"\"") ;
   }

   tRecsBuffer.append("[" + tRowBuffer + "],");
  }
  String tRecs=null;
  if(flag==0)//如果有记录，去掉尾巴多余的逗号
  {
   tRecs=tRecsBuffer.substring(0, tRecsBuffer.length()-1) ;
  }
  tRecs+="]}";
  return tRecs;
 }//RsToJSONStr()
	
 /*
 public List RsToList(ResultSet rs) throws SQLException 
 {//将ResultSet型的数据rs转换为List返回
  List tList = new ArrayList<List>();
  ResultSetMetaData tMd = rs.getMetaData();
  int k = tMd.getColumnCount(); 
  rs.beforeFirst();//把光标移动到ResulSet对象的开头
  while (rs.next())
  {
	List tRow = new ArrayList<String>();
	for (int i = 1; i <= k; i++)
	{
	 tRow.add("\""+rs.getObject(i)+"\"");
	}

	tList.add(tRow);
  }
  return tList;
 }//RsToList()
 */
}
