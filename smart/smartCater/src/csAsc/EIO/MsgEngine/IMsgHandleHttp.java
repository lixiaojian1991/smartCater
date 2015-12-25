package csAsc.EIO.MsgEngine;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public interface IMsgHandleHttp 
{
 public StringBuffer handleMsgHttp(HttpServletRequest msgReq) throws ServletException,IOException;
}
