package csAsc.com;

public class CReturned 
{
 public int statusCode;
 public String statusInfo;
 public Object returned;  
 
 public CReturned()
 {
  statusCode=0;
  statusInfo="未操作";
  returned=null;  	 
 }
 
 public String getReturnStatusStr()
 {
  
  int sc=statusCode;
  if (sc<0) sc = -sc;
  return ""+ sc + "_"+statusInfo;
  
 }
}
