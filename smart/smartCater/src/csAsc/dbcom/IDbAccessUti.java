package csAsc.dbcom;

import java.sql.ResultSet;
import java.sql.SQLException;

import csAsc.com.CReturned;

public interface IDbAccessUti 
{

 public int openConn(String servUrl,String dbName, String userName, String password)
			   throws ClassNotFoundException,SQLException;
   //根据制定的参数创建一个数据库连接
 public int openConn()
		  throws ClassNotFoundException,SQLException;
	//根据最近一次执行连接的参数，创建一个数据库连接，如果是第一次执行，则连接参数为类的初始化数据
 public void closeConn();
  //关闭当前的数据库连接		
 public ResultSet getRecsBySql(String sqlStr,CReturned rs)  ;
 //执行一个给定的SQL语句，返回记录集到tRs
 public ResultSet getRecsBySql(String sqlStr) throws ClassNotFoundException,SQLException;
   //执行一个给定的SQL语句，返回记录集	
 
	
	/**
	 * 根据给定的SQl语句，执行查询，并将满足SQL的记录，从id为tstart开始的tNum个记录返回到ResultSet对象实例
	 * 注意：这里的id指满足条件的记录的开始位置
	 * @param tSQL sql语句
	 * @param tStart  查询结果的开始位置
	 * @param tNum    返回记录的个数
	 * @return  ResultSet
	 */
	public ResultSet getBySQL(String tSQL,long tStart, long tNum);	
	
	/**
	 * 根据表名及关键字查找，将所有匹配的记录返回；
	 * @param tTableName 表名称
	 * @param tKeyName 为关键字段名称
	 * @param tKeyVal 为指定的匹配值
	 * @return ResultSet
	 */
	public ResultSet getByKey(String tTableName,String tKeyName,String tKeyVal);

	
	/**
	 * 功能：根据给定的SQL语句，查询，将结果用ResultSet返回
	 * @param tSQL 用户编写的SQL语句
	 * @return  ResultSet
	 */
	public ResultSet getBySQL(String tSQL);
	
	
	/**功能：将一个用JSON描述的记录集写入指定的数据库表
	 * 
	 * @param tTableName 表名称
	 * @param tJson  JSON描述的记录集,JSON串格式： { Cols: [f1,f2, ...], Rows:[r1, r2, ...] }
	 *           每个f是一个列名,每个r又是一个JSON数组，代表一条记录
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
		 
	 *@param tIdPos 表中id在JSON串中的位置，
	  	即在Cols: [f1,f2, ...]位置，如tIdPos=1，表示f1为id;
	 	 当JSON中的列名不包含id名，那么tIdPos取值为-1
	 * @return  返回写入的记录数
	 */
	int writeTableByJson(String tTableName,String tJson,int[] tKeyPosArr,int tStrategy,int tIdPos);
}
