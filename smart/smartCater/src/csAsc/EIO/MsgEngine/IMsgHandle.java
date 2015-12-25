package csAsc.EIO.MsgEngine;
import java.io.IOException;

import org.json.JSONException;

public interface IMsgHandle 
{//扩展的消息处理器接口
	static String RetMsgHead="";
	 //处理GET型消息，retMsgHead是要返回到请求端的信息头	
	public StringBuffer handleMsg(StringBuffer msgData) throws IOException,JSONException;
	 
	 
}
