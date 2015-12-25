package scater.common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import csAsc.EIO.MsgEngine.CEIOMsgRouter.CParam;
import csAsc.dbcom.CDbAccessUti;

public class Constant {
	public static String upload="";
	
	public static JSONArray getCommonBygroup(CParam param,int group) throws JSONException{
		String sql="select id,name from common where groups="+group;
		ResultSet set=((CDbAccessUti)param.userObj[0]).getBySQL(sql);
		JSONArray array=new JSONArray();
		if(set!=null){
			try{
				while(set.next()){
					JSONObject object=new JSONObject();
					object.put("id",set.getInt("id"));
					object.put("name", set.getString("name"));
					array.put(object);
				}
			}catch(SQLException e){
				System.out.println("操作结果集ResultSet的时候出错了呢！！！");
				e.printStackTrace();
			}
		}
		return array;
	}
	
	public static String toStringFromFloat(float price){
		DecimalFormat decimalFormat=new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
		if(price==0.0){
			return "0";
		}else{
		   return decimalFormat.format(price);
		}
	}
	public static JSONObject  setValue(ResultSet set,String[] array_int,String[] array_string,String[] array_float,String[] array_date){
		JSONObject result=new JSONObject();
		if(set!=null){
			if(array_int!=null){
				for(String string:array_int){
					try {
						result.put(string, set.getInt(string));
					} catch (JSONException e) {
						System.out.println("从ResultSet里面取key值为"+string+"出错");
					} catch (SQLException e) {
						System.out.println("从ResultSet里面取int型值时发生SQL异常!");
					}
				}
			}
			if(array_string!=null){
				for(String string:array_string){
					try {
						result.put(string,set.getString(string));
					} catch (JSONException e) {
						System.out.println("从ResultSet里面取key值为"+string+"出错");
					} catch (SQLException e) {
						System.out.println("从ResultSet里面取string类型值时发生SQL异常!");
					}
				}
			}
			if(array_float!=null){
				for(String string:array_float){
					try {
						result.put(string, toStringFromFloat(set.getFloat(string)));
					} catch (JSONException e) {
						System.out.println("从ResultSet里面取key值为"+string+"出错");
					} catch (SQLException e) {
						System.out.println("从ResultSet里面取float类型值时发生SQL异常!");
					}
				}
			}
			if(array_date!=null){
				for(String string:array_date){
					try {
						result.put(string, set.getDate(string));
					} catch (JSONException e) {
						System.out.println("从ResultSet里面取key值为"+string+"出错");
					} catch (SQLException e) {
						System.out.println("从ResultSet里面取date类型时发生SQL异常!");
					}
				}
			}
		}
		return result;
	}
	
	public static String[] setValueToArray(String[] keys,JSONObject object) throws JSONException{
		String[] values=new String[keys.length];
		for(int i=0;i<keys.length;i++){
			values[i]=object.getString(keys[i]);
		}
		return values;
	}
	
	public static String[][] setArrayToArray(String[] keys,JSONArray array) throws JSONException{
		String[][] result=new String[array.length()][keys.length];
		for(int i=0;i<array.length();i++){
			JSONObject object=array.getJSONObject(i);
			result[i]=setValueToArray(keys, object);
		}
		return result;
	}
	
	public static JSONArray getJsonArrayFromSet(ResultSet set,String[] array_int,String[] array_string,String[] array_float,String[] array_date){
		JSONArray array=new JSONArray();
		if(set!=null){
			try {
				while(set.next()){
					JSONObject object=setValue(set, array_int, array_string, array_float,array_date);
					array.put(object);
				}
			} catch (SQLException e) {
				System.out.println("从ResultSet里面取值变换JSONObject的时候抛出SQL异常!");
			}
		}
		return array;
	}
}
