package csAsc.com.file;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

public class CFileRW 
{//文件读写类，提供静态方法，供用户直接调用
	
 public static StringBuffer readTextFile(String filePath)  throws IOException
 {//将文本文件filePath读出返回到一个字符串缓冲区中
  StringBuffer fileStr=null;
	 
  return fileStr;
 }
 
 public static long readTextFileTHttpResp(String filePath, HttpServletResponse res)  throws ServletException,IOException
 {//将文本文件filePath读出后写入res（不经过字符串缓存），返回写入的字符数目，负数表示出错
  
  return -1;
 }
 
}


