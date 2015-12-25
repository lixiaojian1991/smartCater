package csAsc.com.file;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
public class CTextFileReader 
{
 /**
  * 功能：Java读取txt文件的内容，并把内容以String形式返回
  * 步骤：1：先获得文件句柄
  * 2：获得文件句柄当做是输入一个字符流
  * 3.用缓冲方式读取字符流
  * 4：用readline()一行一行输出到一个字串变量fileContent。
  * 5.最后返回字符串fileContent
  * 备注：需要考虑的是异常情况
  * @param filePath
 */
	 public static int readTxtFileToStringBuffer(String filePath,StringBuffer retSB)
	  {
		  int count=0;//计算返回字符数量
		  try
		  {
			//先获得文件句柄
			File file = new File(filePath);

			if (file.isFile() && file.exists())
			{ // 判断文件是否存在
				
				String encoding = get_charset(file);//获取文件编码
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);// 考虑到编码格式
				//用缓冲方式读取字符流
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				//用readline()一行一行输出到字符串缓冲区fileContentBuffer
				while ((lineTxt = bufferedReader.readLine()) != null)
				{ 	count+=lineTxt.length();
					retSB.append(lineTxt);
				}
				read.close();
			} 
			//找不到指定文件
			else
			{
				System.out.println("找不到指定的文件");
				return -1;
			}
		  } catch (Exception e)
		  {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		  }
	     return count;
	  }
	  
   /**
    * 功能：Java读取txt文件的内容，并把内容以StringBuffer形式返回
    * 步骤：1：先获得文件句柄
    * 2：获得文件句柄当做是输入一个字符流
    * 3.用缓冲方式读取字符流
    * 4：用readline()一行一行输出到字符串缓冲区fileContentBuffer。
    * 5.最后返回字符串缓冲区fileContentBuffer
    * 备注：需要考虑的是异常情况
    * @param filePath
    */
 public  static StringBuffer readTxtFileToStringBuffer(String filePath)
 {
  StringBuffer fileContentBuffer=new StringBuffer();;
  try
  {
	//先获得文件句柄
   File file = new File(filePath);

   if (file.isFile() && file.exists())
   { // 判断文件是否存在
  	String encoding = get_charset(file);//获取文件编码
	InputStreamReader read = new InputStreamReader(
			new FileInputStream(file), encoding);// 考虑到编码格式
	//用缓冲方式读取字符流
	BufferedReader bufferedReader = new BufferedReader(read);
	String lineTxt = null;
	//用readline()一行一行输出到字符串缓冲区fileContentBuffer
	while ((lineTxt = bufferedReader.readLine()) != null)
	{
	 fileContentBuffer.append(lineTxt);
	}
	read.close();
   } 
   //找不到指定文件
   else
   {
	System.out.println("找不到指定的文件");
   }
  } catch (Exception e)
    {
	 System.out.println("读取文件内容出错");
	 e.printStackTrace();
	}
  return fileContentBuffer;
 }
		  
 //自己编写的内部方法，用来判断文件类型
  private static String get_charset( File file )
  {
   String charset = "GBK";
   byte[] first3Bytes = new byte[3];
   try
   {
	boolean checked = false;
	BufferedInputStream bis = new BufferedInputStream( new FileInputStream( file ) );
	bis.mark( 0 );
	int read = bis.read( first3Bytes, 0, 3 );
	if ( read == -1 ) {bis.close(); return charset;};
	if ( first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE ) 
	{
	 charset = "UTF-16LE";
	 checked = true;
	}
	else if ( first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF ) 
	{
	 charset = "UTF-16BE";
	 checked = true;
	}
	else if ( first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB && first3Bytes[2] == (byte) 0xBF ) 
	{
	 charset = "UTF-8";
	 checked = true;
	}
	bis.reset();
	if ( !checked )
	{
	 //	int len = 0;
	 int loc = 0;
     while ( (read = bis.read()) != -1 ) 
     { loc++;
       if ( read >= 0xF0 ) break;
	   if ( 0x80 <= read && read <= 0xBF ) // 单独出现BF以下的，也算是GBK
		 break;
	   if ( 0xC0 <= read && read <= 0xDF ) 
	   {
		read = bis.read();
		if ( 0x80 <= read && read <= 0xBF ) // 双字节 (0xC0 - 0xDF) (0x80// - 0xBF),也可能在GB编码内
		 continue;
		else break;
	   }
	   else if ( 0xE0 <= read && read <= 0xEF )
	   {// 也有可能出错，但是几率较小
		read = bis.read();
		if ( 0x80 <= read && read <= 0xBF ) 
		{
		 read = bis.read();
		 if ( 0x80 <= read && read <= 0xBF ) 
		 {
		  charset = "UTF-8";
		  break;
		 }
	     else break;
		}
		else break;
	   }
	  }
		//System.out.println( loc + " " + Integer.toHexString( read ) );
	 }
	 bis.close();
	} catch ( Exception e ) 
      {	e.printStackTrace();}
  return charset;
 } 
}
