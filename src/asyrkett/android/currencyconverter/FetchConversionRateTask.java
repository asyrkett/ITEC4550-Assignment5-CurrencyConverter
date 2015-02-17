package asyrkett.android.currencyconverter;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

public class FetchConversionRateTask extends AsyncTask<URL, String, Double> {
	
	private static final String CONV_FMT = "###,##0.00";
	private static final String RATE_FMT = "###,##0.00000";
	private static final String SINGLE_URL_ONLY = " only accepts a single URL";
	private static final String CONV_LOOKUP = "ConversionLookup";
	private double value;
	private TextView txtView;
	private Gson gson;
	private String toCurrency;
	private String unavailableServiceMsg;

	FetchConversionRateTask(TextView _t, double value) {
		txtView = _t;
		gson = new GsonBuilder().create();
		this.value = value;
	}
	
    @Override
    protected Double doInBackground(URL... urls) {
    	
    	HttpURLConnection conn = null;
		Scanner scanner = null;
		StringBuilder jsonSB = new StringBuilder();
		URL url = null;
		
		try {
			// error if URLs != 1
			if (urls.length != 1)
				throw new IllegalArgumentException(this.getClass().getName() + SINGLE_URL_ONLY);
			// get the connection
			
			url = urls[0];
			Log.v(CONV_LOOKUP, "Host: " + url.getHost() + ", Path: " + url.getPath());
			publishProgress("opening connection");
			conn = (HttpURLConnection) url.openConnection();
			
			if (conn.getResponseCode() != 200)
            {
            	Log.v(CONV_LOOKUP, "" + conn.getResponseCode());
            	if (url.getHost().equals(MainActivity.CONV_HOST_APPSPOT))
            		unavailableServiceMsg = "Google Appspot's rate exchange is not available at the moment. Try Techunits Herokuapp.";
            	else
            		unavailableServiceMsg = "Techunits Herokuapp's rate exchange is not available at the moment. Try Google Appspot.";
            	
            	throw new IOException("Response(" + conn.getResponseCode() +
                		"):" + conn.getResponseMessage());
            }
			
			InputStream in = new BufferedInputStream(conn.getInputStream());
			// connect stream to scanner
			scanner = new Scanner(in);
			// process entire stream
			while (scanner.hasNext()) jsonSB.append(scanner.nextLine());
            Log.v(CONV_LOOKUP, "Response(" + conn.getResponseCode() +
            		"):" + conn.getResponseMessage());
            
            publishProgress(conn.getResponseCode() + conn.getResponseMessage());
		}
		catch (IOException e)
		{
			Log.e(CONV_LOOKUP, e.getMessage());
			return Double.valueOf(-1.0D);
		}
		catch (Exception e)
		{
			Log.e(CONV_LOOKUP, e.getMessage());
			return Double.valueOf(-1.0D);
		}
		finally {
			if (scanner != null) scanner.close();
			if (conn != null)conn.disconnect();
		}
		// convert response body (in builder) to POJO
		String json = jsonSB.toString();
		Log.v(CONV_LOOKUP, json);
		
		try
		{
			if (url.getHost().equals(MainActivity.CONV_HOST_APPSPOT))
			{
				ConversionRateAppspot appspotRate = gson.fromJson(json, ConversionRateAppspot.class);
				Log.v(CONV_LOOKUP, appspotRate.toString());
				toCurrency = appspotRate.getTo();
				//return the actual double rate in the POJO
				return Double.valueOf(appspotRate.getRate());
			}
			else if (url.getHost().equals(MainActivity.CONV_HOST_HEROKUAPP))
			{
				ConversionRateHerokuapp herokuappRate = gson.fromJson(json, ConversionRateHerokuapp.class);
				Log.v(CONV_LOOKUP, herokuappRate.toString());
				toCurrency = herokuappRate.getTo();
				//return the actual double rate in the POJO
				return Double.valueOf(herokuappRate.getRate());
			}
		}
		catch (Exception e)
		{
			toCurrency = "";
			Log.e(CONV_LOOKUP, e.getMessage());
		}
		
		return Double.valueOf(-1.0D);
	}
    
    protected void onProgressUpdate(String status) {
     	txtView.setText(status);
    }
    
    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(Double rate) {
    	// set the contents of TextView to the double retrieved in doInBackground
    	
    	if (rate == -1.0D)
    	{
    		txtView.setText(unavailableServiceMsg);
    		return;
    	}
    	
    	DecimalFormat currencyFormat = new DecimalFormat(CONV_FMT);
    	DecimalFormat rateFormat = new DecimalFormat(RATE_FMT);
    	double convertedCurrency = value * rate;
    	String currencySymbol = "";
    	
    	if (toCurrency.equals("JPY"))
    	{
    		currencySymbol = "¥";
    	}
    	else if (toCurrency.equals("USD"))
    	{
    		currencySymbol = "$";
    	}
    	else if (toCurrency.equals("EUR"))
    	{
    		currencySymbol = "€";
    	}
    	else if (toCurrency.equals("DKK"))
    	{
    		currencySymbol = "kr";
    	}
    	else if (toCurrency.equals("BRL"))
    	{
    		currencySymbol = "R$";
    	}
    	
    	txtView.setText("Result:\n" + currencySymbol + currencyFormat.format(convertedCurrency) +
    			"\n\nExchange Rate:\n" + rateFormat.format(rate));
   }
    
}
