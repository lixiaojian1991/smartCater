package scater.common;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;

import sun.misc.BASE64Decoder;

import csAsc.EIO.MsgEngine.CEIOMsgRouter.CParam;

public class SavePicture {

	public static String savePicture(CParam param) throws JSONException, IOException{
		HttpServletRequest msgReq;
		msgReq = param.getMsgReq();
		msgReq.setCharacterEncoding("UTF-8");
		JSONObject jsonObject = new JSONObject(msgReq.getParameter(param.getReqMsgDataId()));
		String baseUrl = jsonObject.getString("img"); //获取base64编码
		String fileExt = jsonObject.getString("ext"); //获取文件类型
		
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] b = decoder.decodeBuffer(baseUrl);
		ServletContext sc = msgReq.getSession().getServletContext();
		String uploadDir = Constant.upload;
		String uploadFileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())+"."+fileExt;
		String uploadFilePath = uploadDir+uploadFileName;
		String fullPath = sc.getRealPath("/")+uploadFilePath;
		OutputStream imgOut = new FileOutputStream(fullPath);      
		imgOut.write(b);  
		imgOut.flush();  
		imgOut.close();  
		return uploadFilePath;
	}
}
