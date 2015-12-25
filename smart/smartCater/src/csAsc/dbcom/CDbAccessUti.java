package csAsc.dbcom;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import csAsc.com.CReturned;

public class CDbAccessUti implements IDbAccessUti
{
	
 String tServUrl="jdbc:mysql://localhost:3306";
 String tDbName ="smartcater";
 String tUserName="root";
 String tPassword="root";
	  
 protected Connection tConn = null;
 protected PreparedStatement tPStmt = null;
 protected Statement tStmt = null;
 protected ResultSet tRs = null;
 protected ResultSetMetaData tRsmd=null;
 
 //使用静态代码块初始化
 /*
 static 
 {
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
	 
 }//static
 */
 public CDbAccessUti()
 {
  tConn = null;
  tStmt = null;
  tRs = null;
  tRsmd=null; 
  //下面这句做测试用，正式的时候，把这句话注释掉
  openConn(tServUrl,tDbName,  tUserName,  tPassword);
 }
 
 public CDbAccessUti(String servUrl,String dbName, String userName, String password)
 {
 
  openConn(servUrl,dbName,  userName,  password);
   
  tServUrl= servUrl;
  tDbName=dbName;
  tUserName=userName;
  tPassword=password;
  
 }
 
 public int openConn(String servUrl,String dbName, String userName, String password)
 {//根据制定的参数创建一个数据库连接
  try
  {
   tConn = CDbConn.MySQLConnOpen(servUrl, dbName,userName, password);
   tStmt = tConn.createStatement();
   System.out.println("");
   System.out.println("====数据库成功连接====");
  }catch(ClassNotFoundException se)
  {System.out.println("数据库无法连接：驱动未找到");    
   se.printStackTrace() ;  
   tConn=null;
   return 10;
  }
  catch(SQLException se)
  {System.out.println("数据库连接失败:或者数据库Server未启动，或者用户名、密码错误，或者数据库名错误");    
   //se.printStackTrace() ; 
   tConn=null;
   return 11;
 }
  
   return 0;
 }//setConn()
 
 public int openConn()	 
 {//根据最近一次执行连接的参数，创建一个数据库连接，如果是第一次执行，则连接参数为类的初始化数据
	 
  return openConn(tServUrl, tDbName,tUserName, tPassword);
 }//setConn()
 
 public Connection getConnection(){
	 return tConn;
 }
 
 public Statement getStatement(){
	 return tStmt;
 }
 public void closeConn()
 {//关闭当前的数据库连接
  if(tConn == null) return;
  try
  {
   tConn.close();
  }catch (SQLException e)
   {
   	e.printStackTrace();
   	return ;
   }finally
   {
    tConn = null;
    tStmt = null;
    tRs = null;
    tRsmd=null;
   }
 }

 
 public ResultSet getRecsBySql(String sqlStr,CReturned rs) 
 {//执行一个给定的SQL语句，返回记录集
	  	 
  if (tConn==null)
	if (openConn()!=0)
	{
	 rs.statusCode=-1;
	 rs.statusInfo="数据库连接出错";
	 return null ;
	}
  
  ResultSet tRs=null;
  try
  {
   tRs = tStmt.executeQuery(sqlStr);
   if ( !tRs.first()) 
   {
	rs.statusCode=-3;
	rs.statusInfo="没有满足条件的记录";
	return null;
   }
  }
  catch (SQLException e)
  {
   rs.statusCode=-2;
   rs.statusInfo="数据库访问出错";	  
   return null;	  
  }
  rs.statusCode=0;//可以存放返回记录的个数
  rs.statusInfo="操作成功";
  return tRs;	 
 }
 
 
   	/**
	 * 根据给定的SQl语句，执行查询，并将满足SQL的记录，从id为tstart开始的tNum个记录返回到ResultSet对象实例
	 * 注意：这里的id指满足条件的记录的开始位置
	 * @param tSQL sql语句
	 * @param tStart  查询结果的开始位置
	 * @param tNum    返回记录的个数
	 * @return
	 */
	@Override
	public ResultSet getBySQL(String tSQL, long tStart, long tNum)
	{
		try
		{
			if(tConn==null)
			{
				System.out.println("数据库未连接或者连接不成功！！！");
				return null;
			}
			tStart=tStart-1;
			//加上限制
			tSQL=tSQL+" LIMIT "+tNum+" OFFSET "+ tStart;
			tPStmt = tConn.prepareStatement(tSQL);
			tRs = tPStmt.executeQuery();
			return tRs;
		} catch (SQLException e)
		{
			tRs=null;
			e.printStackTrace();
			return tRs;
		} 
	}

	/**
	 * 根据表名及关键字查找，将所有匹配的记录返回；
	 * @param tTableName 表名称
	 * @param tKeyName 为关键字段名称
	 * @param tKeyVal 为指定的匹配值
	 * @return ResultSet
	 */
	@Override
	public ResultSet getByKey(String tTableName, String tKeyName, String tKeyVal)
	{
		try
		{
			if(tConn==null)
			{
				System.out.println("数据库连接不成功！！！");
				return null;
			}
			String tSql = "select * from "+tTableName+ " where "+ tKeyName+"=?";
			tPStmt = tConn.prepareStatement(tSql);
			tPStmt.setString(1, tKeyVal);
			tRs = tPStmt.executeQuery();
			return tRs;
		} catch (SQLException e)
		{
			tRs=null;
			e.printStackTrace();
			return tRs;
		} 
		
	}

	/**
	 * 功能：根据给定的SQL语句，查询，将结果用ResultSet返回
	 * @param tSQL 用户编写的SQL语句
	 * @return  ResultSet
	 */
	@Override
	public ResultSet getBySQL(String tSQL)
	{
		try
		{
			if(tConn==null)
			{
				System.out.println("数据库连接不成功！！！");
				return null;
			}
			tPStmt = tConn.prepareStatement(tSQL);
			tRs = tPStmt.executeQuery();
			if(!tRs.next()) return null;
			tRs.beforeFirst();
			return tRs;
		} catch (Exception e)
		{
			tRs=null;
			e.printStackTrace();
			return tRs;
		} 
		
	}

	
	/**功能：将一个用JSON描述的记录集写入指定的数据库表
	 * 
	 * @param tTableName 表名称
	 * @param tJson  JSON描述的记录集,JSON串格式： { Cols: [f1,f2, ...], Rows:[r1, r2, ...] }
	           		每个f是一个列名,每个r又是一个JSON数组，代表一条记录
	           		例如：{"Cols":["id","no","name","school","score","degree","diploma"],"Rows":[["17","1001","王一","华工","89","修改",null],["21","1001","王一","华工","89","修改",null]]}
	 				当记录中的字段需要加引号，但当字段为null时，不需要加引号
	 * @param tKeyPosArr  数组，用来说名关键字中的每一列在JSON中的位置；
	           如关键字为f1,f2，那么tKeyPosArr是一长度为2的数组，tKeyPosArr[0]=1,tKeyPosArr[1]=2;
	           如果关键字为f1,fi,fm,那么tKeyPosArr是一长度为3的数组，tKeyPosArr[0]=1,tKeyPosArr[1]=i;tKeyPosArr[2]=m;
	            关键字：用于识别相同记录，对于关键字相同的记录，认为是相同记录（重复判定、存在判定），该关键字与数据库定义的基本无关
		关键字是由表的一个或多个列名称组成
	 * @param tStrategy ”策略“：对写入做方式一些规定，例如  
	  	1表示：  与目标重复时处理方式一，代替，即 用新内容代替原来内容
	  	2表示： 与目标重复时处理方式二，插入
	   	3表示： 与目标重复时处理方式三，跳过
	   	4表示：目标不存时处理方式一，插入
	   	5表示：目标不存时处理方式二，跳过
	 * @param tIdPos 表中id在JSON串中的位置，
	  	即在Cols: [f1,f2, ...]位置，如tIdPos=1，表示f1为id;
	 	 当JSON中的列名不包含id名，那么tIdPos取值为-1
	 *   
	 * @return  返回写入的记录数   --1：表示数据库接不成功或更新不成功；大于0表示更新数据库的行数;-2：JDBC事务回滚失败
	 */
	@Override
	public int writeTableByJson(String tTableName, String tJson,
			int[] tKeyPosArr, int tStrategy,int tIdPos)
	{	
		int effectedRows = 0;// 更新数据库的行数
		Gson gson = new Gson();
		Map<String, List> retMap = gson.fromJson(tJson, Map.class);
		List<String> colsList = retMap.get("Cols");//列名
		List<List<String>> rowsList = retMap.get("Rows");//记录集
		int cowNum=colsList.size();  //一共有多少列
		int rowNum=rowsList.size();//一共有多少条记录
		
		for(int i=0;i<cowNum;i++)
		{
			System.out.println(colsList.get(i));
		}
		String tSql="";
		for(int i=0;i<rowNum;i++)//根据记录与关键字查询
		{
			String tWhere="";
			for(int j=0;j<tKeyPosArr.length;j++)//根据关键字生成条件
			{
				if(j!=0) tWhere=tWhere+" and ";
				tWhere=tWhere+colsList.get(tKeyPosArr[j]-1)+" = "+"'"+rowsList.get(i).get(tKeyPosArr[j]-1)+"'";
			}
			System.out.println(tWhere);
			
			tSql="select * from "+tTableName+" where "+ tWhere;			

			int flag=isRecordExit(tSql);//执行查询
			if(flag==-1) //查询出错或数据库连接不成功
			{
				return -1;
			}
			else if(flag==1)//存在与目标重复的记录
			{
				switch (tStrategy)
				  {case 1:// 与目标重复时处理方式一，代替，即 用新内容代替原来内容 
					   tSql="update "+tTableName+" set ";
					   String tSetSql="";
					   int tFlag1 = 0;
						for (int k = 0; k < cowNum; k++)
						{
							if ((k + 1) == tIdPos) // 如果是id跳过
								continue;
							if (tFlag1 != 0)
							{
								tSetSql = tSetSql + ",";
							}
							tFlag1 = 1;
							tSetSql = tSetSql + colsList.get(k)+" = ";
							if (rowsList.get(i).get(k) != null)
							{
								tSetSql = tSetSql + "'"
										+ rowsList.get(i).get(k) + "'";
							} else
							{
								tSetSql = tSetSql + rowsList.get(i).get(k);
							}
						}
						tSql=tSql+tSetSql+" where "+tWhere;
						int updateRow = executeUpdate(tSql);
						if (updateRow == -2 || updateRow == -1)// 数据插入不成功
						{
							return updateRow;
						}
						effectedRows = effectedRows + updateRow;
					  break;
				  case 2:// 与目标重复时处理方式二，插入
					tSql = "insert into " + tTableName + "( ";
					String tSqlValue = "";
					int tFlag2 = 0;
					for (int k = 0; k < cowNum; k++)
					{
						if ((k + 1) == tIdPos) // 如果是id跳过
							continue;
						if (tFlag2 != 0)
						{
							tSql = tSql + ",";
							tSqlValue = tSqlValue + ",";
						}
						tFlag2 = 1;
						tSql = tSql + colsList.get(k);
						if (rowsList.get(i).get(k) != null)
						{
							tSqlValue = tSqlValue + "'"
									+ rowsList.get(i).get(k) + "'";
						} else
						{
							tSqlValue = tSqlValue + rowsList.get(i).get(k);
						}

					}
					tSql = tSql + ") values( " + tSqlValue + ")";
					int insertRow = executeInsert(tSql);
					if (insertRow == -2 || insertRow == -1)// 数据插入不成功
					{
						return insertRow;
					}
					effectedRows = effectedRows + insertRow;
					break;
				  case 3:// 与目标重复时处理方式三，跳过
					  break;				
				  }
			}
			else if(flag==0)//目标不存
			{
				switch (tStrategy)
				  {case 4://目标不存时处理方式一，插入
					   tSql="insert into "+ tTableName+"( ";
					   String tSqlValue="";
					   int tFlag4=0;
					   for(int k=0;k<cowNum;k++)
					   {
						  if((k+1)==tIdPos) //如果是id所有更跳过
							   continue;
						   if(tFlag4!=0)
						   {
							   tSql=tSql+",";
							   tSqlValue=tSqlValue+",";
						   }
						   tFlag4=1;
						   tSql=tSql+colsList.get(k);
						   if(rowsList.get(i).get(k)!=null)
						   {
							   tSqlValue=tSqlValue+"'"+rowsList.get(i).get(k)+"'";
						   }
						   else
						   {
							   tSqlValue=tSqlValue+rowsList.get(i).get(k);
						   }
						  
						   
					   }
					   tSql=tSql+") values( "+tSqlValue+ ")";	
					   int insertRow=executeInsert(tSql);
					   if(insertRow==-2||effectedRows==-1)//数据插入不成功
					   {
						   return insertRow;
					   }
					   effectedRows=effectedRows+insertRow;					   
					  break;
				  case 5://目标不存时处理方式二，跳过
					  break;				
				  }
			}
		}
		
		return effectedRows;
	}

/**
 * 插入数据库
 * @param tInsertSql 插入语句
 * @return
 */
private int executeInsert(String tInsertSql)
{
	return executeUpdate(tInsertSql);

}

//返回插入后的自增的id
public int executeInsertRetKey(String sql) throws SQLException{
	tStmt=tConn.createStatement();
	int row=tStmt.executeUpdate (sql,Statement.RETURN_GENERATED_KEYS);
	tRs = tStmt.getGeneratedKeys ();
	if(tRs.next()){
		int key = tRs.getInt(row);  
        return key; 
	}
	return -1;
}
/**
 * 功能更新数据库
 * @param updateSql  更新语句
 * @return -1：表示数据库接不成功或更新不成功；大于0表示更新数据库的行数;-2：JDBC事务回滚失败
 */
public int executeUpdate(String updateSql)
	{
		int effectedRows = 0;// 更新数据库的行数
		if (tConn == null)
		{
			System.out.println("数据库连接不成功！！！");
			effectedRows = -1;
			return effectedRows;
		}
		try
		{
			tConn.setAutoCommit(false);
			tPStmt = tConn.prepareStatement(updateSql);
			effectedRows = tPStmt.executeUpdate();
			tConn.commit();
		} catch (SQLException ex)
		{
			System.out.println("数据库写操作失败!");
			if (tConn != null)
			{
				try
				{
					tConn.rollback();
					effectedRows = -1;
					System.out.println("JDBC事务回滚成功");
					return effectedRows;
				} catch (SQLException e)
				{
					effectedRows = -2;
					e.printStackTrace();
					System.out.println("JDBC事务回滚失败");
					return effectedRows;
				}
			}
		}
		return effectedRows;
	}

/**
 * 功能：执行tSql语句查询，如果存在记录返回1，不存在记录返回0，如果数据库连接不成功或查询异常返回-1
 * @param tSelectSql  查询sql
 * @return 0:表示不存在记录；1表示记录存在；-1表示
 */
public int isRecordExit(String tSelectSql)
{
	int flag=0;
	 try
		{
			if(tConn==null)
			{
				System.out.println("数据库连接不成功！！！");
				flag=-1;
				return flag;
			}
			tPStmt = tConn.prepareStatement( tSelectSql);
			tRs = tPStmt.executeQuery();
			if(tRs.next()) 
			{
				flag=1;
				return flag;
			}
			flag=0;
			return flag;
		} catch (SQLException e)
		{
			flag=-1;
			e.printStackTrace();
			return flag;
		} 
}

@Override
public ResultSet getRecsBySql(String sqlStr) throws ClassNotFoundException,
		SQLException {
	// TODO Auto-generated method stub
	return null;
}

 
}//class
 

