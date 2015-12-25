package scater.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import csAsc.EIO.MsgEngine.CEIOMsgRouter.CParam;
import csAsc.dbcom.CDbAccessUti;

public class BaseDAO {

	public CParam param;

	public BaseDAO() {
	}

	/**
	 * 生成插入语句的sql语句
	 * @param tablename  将要插入表的表名
	 * @param colName    插入的列的名字
	 * @param colValue   插入每列的值，和列的名字对应的值
	 * @return   String  返回生成的sql语句
	 */
	public String gene_Insert_sql(String tablename, String[] colName,
			String[] colValue){
		String sql = "insert into " + tablename + "(";
		for (int i = 0; i < colName.length; i++) {
			sql += colName[i] + ",";
		}
		sql = sql.substring(0, sql.length() - 1) + ") values('";
		for (int i = 0; i < colValue.length; i++) {
			sql += colValue[i] + "','";
		}
		sql = sql.substring(0, sql.length() - 2) + ")";
		return sql;
	}

	/**
	 * 生成查询语句
	 * @param fromTable  查询的主表
	 * @param searchCol  查询列，晋档searchAll为false时有用
	 * @param byPage     是否查询某一页还是全部    true：查询某一页   false：查询全部
	 * @param page       查询的页数
	 * @param pageSize   每页的条数
	 * @param joinInfo   join的表和列明，格式为：{{"left join","joinTable","col1" ,"col2"}}
	 * @param where      where子句，格式为{{"col","=","value"}}
	 * @param sort       排序的列，{{"col","desc"}}
	 * @param group      分组的列{"col1","col2"}
	 * @return			 生成的sql语句
	 */
	public String searchByPage(String fromTable, String[] searchCol,
			boolean byPage, int page, int pageSize, String[][] joinInfo,
			String[][] where, String[][] sort, String[] group) {
		StringBuffer sql = new StringBuffer("select ");
		int i = 0;
		if (searchCol == null) {
			sql.append("*");
		} else {
			for (i = 0; i < searchCol.length; i++) {
				if (i == 0) {
					sql.append(searchCol[i]);
				} else {
					sql.append("," + searchCol[i]);
				}
			}
		}
		sql.append(" from " + fromTable);
		if(joinInfo!=null){
			for(i=0;i<joinInfo.length;i++){
				sql.append(" "+joinInfo[i][0]+" "+joinInfo[i][1]+" on "+joinInfo[i][2]+"="+joinInfo[i][3]);
			}
		}
		if(where!=null){
			sql.append(" where 1=1");
			for(i=0;i<where.length;i++){
				sql.append(" and "+where[i][0]+" "+where[i][1]);
				if(where[i][1].equals("like")){
					sql.append(" '%"+where[i][2]+"%'");
				}else{
					sql.append(" '"+where[i][2]+"'");
				}
			}
		}
		if(group!=null){
			sql.append(" group by ");
			for(i=0;i<group.length;i++){
				if(i==0){
					sql.append(group[i]);
				}else{
					sql.append(","+group[i]);
				}
			}
		}
		if(sort!=null){
			sql.append(" order by ");
			for(i=0;i<sort.length;i++){
				if(i==0){
					sql.append(sort[i][0]+" "+sort[i][1]);
				}else{
					sql.append(","+sort[i][0]+" "+sort[i][1]);
				}
			}
		}
		if(byPage){
			sql.append(" limit "+ ((page - 1) * pageSize) + ","+ (page * pageSize));
		}
		return sql.toString();
	}

	/**
	 * 查找所有表的记录数
	 * 
	 * @param tablename
	 *            表名
	 * @return
	 * @throws SQLException
	 */
	public int getAllRecord(String tablename) throws SQLException {
		String sql = "select count(*) as count from " + tablename;
		ResultSet rst = ((CDbAccessUti) param.userObj[0]).getBySQL(sql);
		rst.next();
		return rst.getInt("count");
	}

	/**
	 * 根据id删除某个表里面的记录，此记录只适应没有记录依赖此单表的情况
	 * 
	 * @param tablename
	 *            表名
	 * @param id
	 *            主键id
	 * @return
	 */
	public int deleteRecordById(String tablename, int id) {
		String sql = "delete from " + tablename + " where id='" + id + "'";
		System.out.println("删除sql : "+sql);
		return executeUpdate(sql);

	}

	/**
	 * 用于单表的条件查询
	 * 
	 * @param operators
	 *            key：列名 value:=,>,<,<=，>=,like
	 * @param conditions
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public ResultSet searchRecordByMap(String tablename,
			HashMap<String, String> operators,
			HashMap<String, String> conditions) throws ClassNotFoundException,
			SQLException {
		String sql = "select * from " + tablename + " where ";
		Iterator<Entry<String, String>> interator = operators.entrySet()
				.iterator();
		while (interator.hasNext()) {
			Entry<String, String> entry = interator.next();
			sql += entry.getKey() + " " + entry.getValue();
			if (entry.getValue().equals("like")) {
				sql += " '%" + conditions.get(entry.getKey()) + "%' And ";
			} else {
				sql += "'" + conditions.get(entry.getKey()) + "' And ";
			}
		}
		sql = sql.substring(0, sql.length() - 4);
		return ((CDbAccessUti) param.userObj[0]).getBySQL(sql);
	}

	public ResultSet executeQuerySql(String sql) {
		System.out.println("执行的查询语句为："+sql);
		return ((CDbAccessUti) param.userObj[0]).getBySQL(sql);
	}

	public int getCountByResultSet(ResultSet rst) throws SQLException {
		int count = 0;
		if (rst == null) {
			return 0;
		}
		while (rst.next()) {
			count++;
		}
		return count;
	}

	public String gene_update_sql(int id, String tablename, String[] colName,
			String[] colValue) {
		String sql = "update " + tablename + " set ";
		for (int i = 0; i < colName.length; i++) {
			sql += colName[i] + "='" + colValue[i] + "',";
		}
		sql = sql.substring(0, sql.length() - 1);
		sql += " where id=" + id;
		return sql;
	}

	/*
	 * 批量处理sql语句，大部分是用于插入和删除的时候，保证事务的安全性 若是执行成功，返回1，执行失败，返回-1
	 */
	public int executeSqlArray(String[] sqlArray) throws SQLException {
		((CDbAccessUti) param.userObj[0]).getConnection().setAutoCommit(false);
		((CDbAccessUti) param.userObj[0])
				.getConnection()
				.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
		try {
			for (String string : sqlArray) {
				executeUpdate(string);
			}
			((CDbAccessUti) param.userObj[0]).getConnection().commit();
		} catch (Exception e) {
			((CDbAccessUti) param.userObj[0]).getConnection().rollback();
			return -1;
		}
		return 1;
	}

	public int executeInsert(String sql) throws SQLException{
		System.out.println("插入语句sql : "+sql);
		return ((CDbAccessUti) param.userObj[0]).executeInsertRetKey(sql);
	}

	public int executeUpdate(String sql) {
		System.out.println("更新sql : "+sql);
		return ((CDbAccessUti) param.userObj[0]).executeUpdate(sql);
	}
	
	
	public String gene_union_sql(String sql1,String sql2,int page,int pageSize){
		String sql=sql1+" union "+sql2+" limit "+((page - 1) * pageSize) + ","+ (page * pageSize);
		return sql;
	}
}
