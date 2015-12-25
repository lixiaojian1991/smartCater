package csAsc.dbcom;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;



public class CAPDButils 
{
	public static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	// 为基本语句动态插入多个where and
	public static String appendDyCons(String baseSql, List<String> conditions)
	{
		if (conditions == null || conditions.size() == 0)
		{
			System.out.println("genDyConsSql:" + baseSql);
			return baseSql;
		}
		StringBuffer baseSqlBuffer = new StringBuffer(baseSql);
		if (conditions.size() > 0)
		{
			baseSqlBuffer.append(" where ");
			for (int i = 0; i < conditions.size() - 1; i++)
			{
				baseSqlBuffer.append(conditions.get(i) + " ");
				baseSqlBuffer.append("and ");
			}
			baseSqlBuffer.append(conditions.get(conditions.size() - 1));
		}
		System.out.println("genDyConsSql:" + baseSqlBuffer.toString());
		return baseSqlBuffer.toString();
	}

	// 为基本与语句插入分页（即limit ,
	// ），注意：分页子句务必出现在where子句之后！由于从表格来的数据往往从0开始，这恰与limit子句相符合
	public static String appendLimits(String baseSql, long start, long num)
	{
		if (start < -1)
		{
			start = 0;
		}
		return baseSql + " limit " + start + "," + num;
	}

	/**
	 * 把整数id数组变成起点-终点数组
	 * 例如inList=[1,2,3,4,7,8,9]
	 * 调用arrToRange方法后变为：[1-4,7-9]
	 * @param inList
	 * @return
	 */
	// 把整数id数组变成起点-终点数组
	public static List<String> arrToRange(List<Integer> inList)
	{
		Collections.sort(inList);
		List<String> outList = new ArrayList<String>();
		int first = Integer.MIN_VALUE;
		int right = Integer.MIN_VALUE;
		for (int i = 0; i < inList.size(); i++)
		{
			int id = inList.get(i);
			if (first == Integer.MIN_VALUE)
			{
				first = id;
				right = id;
				continue;
			}
			if (id - 1 == right)
			{
				right = id;
			} else if (id > right - 1)
			{
				outList.add(first + "-" + right);
				first = id;
				right = id;
			}
		}
		if (first != Integer.MIN_VALUE)
		{
			outList.add(first + "-" + right);
		}
		return outList;
	}

	/**
	 * 根据ex和isIn生成加入where条件的子句，当isIn为false时，加入where条件的子句为not in子句,否则为in子句
	 * 
	 * @param colName  列名
	 * @param isIn  值为true或false
	 * @param ex colName的值
	 * @return
	 */
	// 
	public static String appendInCondition(String colName, boolean isIn,
			int[] ex)
	{
		StringBuffer exBuffer = new StringBuffer();
		for (int i = 0; i < ex.length - 1; i++)
		{
			exBuffer.append(ex[i] + ",");
		}
		exBuffer.append(ex[ex.length - 1]);
		if (isIn)
		{
			return (colName + " in (" + exBuffer.toString() + ")");
		} else
		{
			return (colName + " not in (" + exBuffer.toString() + ")");
		}
	}

	// order必须放在where子句之后！
	public static String appendOrder(String string, boolean isAsc,
			String colName)
	{
		if (isAsc)
		{
			return string + " order by " + colName;
		}
		return string + " order by " + colName + " desc";
	}

	// date转String yyyy-MM-dd HH:mm:ss
	public static String date2string(Date date)
	{
		return sdf.format(date);
	}

	// string转date yyyy-MM-dd HH:mm:ss
	public static Date string2date(String string)
	{
		try
		{
			return sdf.parse(string);
		} catch (ParseException e)
		{
			return new Date();
		}
	}

//	// 根据批次流水号id获取详情的所有审批状态值
//	public Set<Integer> getAccTypesBySerialId(int serialId) throws Exception
//	{
//		Set<Integer> set = new HashSet<Integer>();
//		if (tConn == null)
//			openConn();
//
//		ResultSet tRs = null;
//		tRs = tStmt
//				.executeQuery("select distinct acc_type from ais_batch_detail_t where serial_num="
//						+ serialId);
//		while (tRs.next())
//		{
//			set.add(tRs.getInt(1));
//		}
//		return set;
//	}
//
//	// SELECT * from ais_batch_info_t where id IN (select serial_num from
//	// ais_batch_detail_t WHERE acc_type=1)
//	// 根据搜索条件获取符合搜索条件的批次记录总数，传入-1表示全部都符合
//	public int getBatchSum(int groupId, int accType, CReturned rs)
//	{
//		// 存储where and 条件集
//		List<String> conditions = new ArrayList<String>();
//		if (groupId != -1)
//		{
//			conditions.add("batch_group=" + groupId);
//		}
//		if (accType != -1)
//		{
//			conditions
//					.add("id IN (select serial_num from ais_batch_detail_t WHERE acc_type="
//							+ accType + ")");
//		}
//		if (tConn == null)
//			if (openConn() != 0)
//			{
//				rs.statusCode = -1;
//				rs.statusInfo = "数据库连接出错";
//				return 0;
//			}
//		ResultSet tRs = null;
//		try
//		{
//			tRs = tStmt.executeQuery(appendDyCons(
//					"SELECT COUNT(*) from ais_batch_info_t", conditions));
//			if (tRs.next())
//			{
//				rs.statusCode = 0;// 可以存放返回记录的个数
//				rs.statusInfo = "操作成功";
//				return (tRs.getInt(1));
//			}
//		} catch (SQLException e)
//		{
//			rs.statusCode = -2;
//			rs.statusInfo = "数据库访问出错";
//			return 0;
//		}
//		return 0;
//	}
//
//	// 根据搜索条件获取符合条件的批次记录，需要分页显示
//	public JSONArray getBatchByPage(int groupId, int accType, long start,
//			long num, CReturned rs)
//	{
//		JSONArray resultJsonArray = new JSONArray();
//		// 存储where and 条件集
//		List<String> conditions = new ArrayList<String>();
//		if (groupId != -1)
//		{
//			conditions.add("batch_group=" + groupId);
//		}
//		if (accType != -1)
//		{
//			conditions
//					.add("id IN (select serial_num from ais_batch_detail_t WHERE acc_type="
//							+ accType + ")");
//		}
//		String sql = appendDyCons("SELECT * from ais_batch_info_t", conditions);
//		sql = appendLimits(sql, start, num);
//		System.out.println("sql:" + sql);
//		// 保证数据库连接
//		if (tConn == null)
//			if (openConn() != 0)
//			{
//				rs.statusCode = -1;
//				rs.statusInfo = "数据库连接出错";
//				return resultJsonArray;
//			}
//		try
//		{
//			ResultSet resultSet = tStmt.executeQuery(sql);
//			while (resultSet.next())
//			{
//				Map<String, Object> map = new HashMap<String, Object>();
//				map.put("key", resultSet.getInt(1));
//				map.put("value", new String[] { resultSet.getString(2),
//						resultSet.getString(3), resultSet.getString(4),
//						resultSet.getString(5) });
//				resultJsonArray.put(map);
//			}
//			rs.statusCode = 0;// 可以存放返回记录的个数
//			rs.statusInfo = "操作成功";
//			return resultJsonArray;
//		} catch (SQLException e)
//		{
//			rs.statusCode = -2;
//			rs.statusInfo = "数据库访问出错";
//			return resultJsonArray;
//		}
//	}
//
//	// 根据搜索条件删除批次，返回起点、终点
//	public List<String> delBatchById(int groupId, int accType, boolean isAll,
//			int[] ex, CReturned rs)
//	{
//		// 作为返回值用
//		List<Integer> delIdList = new ArrayList<Integer>();
//		List<String> conditions = new ArrayList<String>();
//		if (isAll == false)
//		{
//			if (ex == null || ex.length == 0)
//			{
//				return new ArrayList<String>();
//			} else
//			{
//				conditions.add(appendInCondition("id", true, ex));
//			}
//
//			// 返回内容，实质是int[]转list，目前暂无合适的方法
//			for (int i = 0; i < ex.length; i++)
//			{
//				delIdList.add(ex[i]);
//			}
//		} else
//		{
//			if (ex == null || ex.length == 0)
//			{
//
//			} else
//			{
//				conditions.add(appendInCondition("id", false, ex));
//				// 插入其他搜索条件
//				if (groupId != -1)
//				{
//					conditions.add("batch_group=" + groupId);
//				}
//				if (accType != -1)
//				{
//					conditions
//							.add("id IN (select serial_num from ais_batch_detail_t WHERE acc_type="
//									+ accType + ")");
//				}
//			}
//			// 需要获取即将要删除的id集合，并以起点-终点的形式返回，目前仅在全选模式下启用，原因是去选模式只要返回一样的就行
//			// 保证数据库连接
//			if (tConn == null)
//				if (openConn() != 0)
//				{
//					rs.statusCode = -1;
//					rs.statusInfo = "数据库连接出错";
//					return new ArrayList<String>();
//				}
//
//			try
//			{
//				ResultSet resultSet = tStmt.executeQuery(appendDyCons(
//						"select id from ais_batch_info_t", conditions));
//				while (resultSet.next())
//				{
//					delIdList.add(resultSet.getInt(1));
//				}
//				rs.statusCode = 0;// 可以存放返回记录的个数
//				rs.statusInfo = "操作成功";
//			} catch (SQLException e)
//			{
//				rs.statusCode = -2;
//				rs.statusInfo = "数据库访问出错";
//				return new ArrayList<String>();
//			}
//		}
//
//		// // 正式执行删除操作
//		// 保证数据库连接
//		if (tConn == null)
//			if (openConn() != 0)
//			{
//				rs.statusCode = -1;
//				rs.statusInfo = "数据库连接出错";
//				return new ArrayList<String>();
//			}
//		try
//		{
//			// 返回的结果是修改的数目，暂时不作处理
//			tStmt.executeUpdate(appendDyCons("delete from ais_batch_info_t",
//					conditions));
//
//			rs.statusCode = 0;// 可以存放返回记录的个数
//			rs.statusInfo = "操作成功";
//			// 返回修改的id集合
//			return arrToRange(delIdList);
//		} catch (SQLException e)
//		{
//			rs.statusCode = -2;
//			rs.statusInfo = "数据库访问出错";
//			return new ArrayList<String>();
//		}
//	}

	public static void main(String[] args) throws Exception
	{
		CAPDButils apdButils = new CAPDButils();
		// System.out.println(apdButils.getAccTypesBySerialId(1).size());
		// System.out.println(apdButils.getBatchSum(-1, -1));
		// JSONArray jsonArray = apdButils.getBatchByPage(-1, -1, 8, 5);
		// for (int i = 0; i < jsonArray.length(); i++)
		// {
		// System.out.println(jsonArray.get(i).toString());
		// }
		// System.out.println(apdButils.delBatchById(-1, -1, true, new int[] {
		// 3,
		// 4, 8, 1000 }));
	}
}
