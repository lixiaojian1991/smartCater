package csAsc.com;

public class CMeException extends Exception
{
 private static final long serialVersionUID = 1L;
 public CMeException() 
 {
	super();
 }
   
  private String message;
  public CMeException(String message)
  {this.message = message; }
   
  public String getMessage()
   {return message;}
}
