package asyrkett.android.currencyconverter;

public class WebServiceUnavailableException extends Exception
{
	private static final long serialVersionUID = 6214060473147612492L;
	
	public WebServiceUnavailableException()
	{
		super();
	}
	
	public WebServiceUnavailableException(String message)
	{
		super(message);
	}
}