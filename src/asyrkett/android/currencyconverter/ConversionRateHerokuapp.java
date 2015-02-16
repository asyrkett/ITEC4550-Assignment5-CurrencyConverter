package asyrkett.android.currencyconverter;

public class ConversionRateHerokuapp {

	private String To;
	private String From;
	private float Rate;

	public String getTo()
	{
		return To;
	}

	public void setTo(String to)
	{
		this.To = to;
	}

	public String getFrom()
	{
		return From;
	}

	public void setFrom(String from)
	{
		this.From = from;
	}

	public void setRate(float rate)
	{
		this.Rate = rate;
	}

	public float getRate() {
		return Rate;
	}

	@Override
	public String toString()
	{
		return "ConversionRateHerokuapp [To=" + To + ", From=" + From
				+ ", Rate=" + Rate + "]";
	}
}
