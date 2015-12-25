package csAsc.com;

public class CMyException extends Exception
{
 private static final long serialVersionUID = 1L;
 public CMyException() 
 {
	super();
 }
   
  private String message;
  public CMyException(String message)
  {this.message = message; }
   
  public String getMessage()
   {return message;}
}
