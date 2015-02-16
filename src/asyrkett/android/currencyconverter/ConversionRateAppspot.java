package asyrkett.android.currencyconverter;

public class ConversionRateAppspot
{
	private String to;
	private float rate;
	private String from;
	
	public String getTo()
	{
		return to;
	}
	
	public void setTo(String to)
	{
		this.to = to;
	}
	
	public float getRate()
	{
		return rate;
	}
	
	public void setRate(float rate)
	{
		this.rate = rate;
	}
	
	public String getFrom()
	{
		return from;
	}
	
	public void setFrom(String from)
	{
		this.from = from;
	}

	@Override
	public String toString()
	{
		return "ConversionRateAppspot [to=" + to + ", rate=" + rate + ", from="
				+ from + "]";
	}
}
