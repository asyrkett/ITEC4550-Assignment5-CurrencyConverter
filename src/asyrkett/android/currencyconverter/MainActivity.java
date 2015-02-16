package asyrkett.android.currencyconverter;

import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity
{

	// remember to add INTERNET permission to manifest!
	public static String CONV_HOST_APPSPOT = "rate-exchange.appspot.com";
	public static String CONV_HOST_HEROKUAPP = "rate-exchange.herokuapp.com";
	private static String CONV_URL_APPSPOT = "http://" + CONV_HOST_APPSPOT + "/currency";
	private static String CONV_URL_HEROKUAPP = "https://" + CONV_HOST_HEROKUAPP + "/fetchRate";
	private static String FORMAT_PARAMS = "?from=%s&to=%s";

	private static final String CONV_LOOKUP = "ConversionLookup";

	double value;
	String params;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final Button button = (Button) findViewById(R.id.btnConvert);
		final TextView rslt = (TextView) findViewById(R.id.txtResult);
		final EditText enteredValue = (EditText) findViewById(R.id.txtValue);
		final Spinner fromCurrency = (Spinner) findViewById(R.id.spinFrom);
		final Spinner toCurrency = (Spinner) findViewById(R.id.spinTo);
		final RadioButton radAppspot = (RadioButton) findViewById(R.id.radAppspot);

		toCurrency.setSelection(2);
		
		button.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{

				if (enteredValue.getText().toString().length() == 0
						|| Double.parseDouble(enteredValue.getText().toString()) == 0)
				{
					Toast.makeText(getApplicationContext(),
							"Please enter a valid value to convert",
							Toast.LENGTH_SHORT).show();
					return;
				}
				else
				{
					value = Double.parseDouble(enteredValue.getText().toString());
				}

				if (fromCurrency.getSelectedItemId() == toCurrency.getSelectedItemId())
				{
					Toast.makeText(
							getApplicationContext(),
							"Your \"From\" and \"To\" currencies are the same."
									+ "\nPlease select different currencies to convert.",
							Toast.LENGTH_SHORT).show();
					return;
				}
				else
				{
					params = String.format(FORMAT_PARAMS, fromCurrency.getSelectedItem().toString(),
							toCurrency.getSelectedItem().toString());
				}

				FetchConversionRateTask task = new FetchConversionRateTask(rslt, value);
				rslt.setText(getString(R.string.converting));
				try
				{
					if (radAppspot.isChecked())
					{
						task.execute(new URL(CONV_URL_APPSPOT + params));
					}
					else
					{
						task.execute(new URL(CONV_URL_HEROKUAPP + params));
					}
				}
				catch (MalformedURLException e)
				{
					Log.e(CONV_LOOKUP, e.getMessage());
					Toast.makeText(MainActivity.this, 
							"Unable to retrieve conversion rate, exception="
							+ e.getMessage(), Toast.LENGTH_LONG).show();
				}
				catch (Exception e)
				{
					Log.e(CONV_LOOKUP, e.getMessage());
					Toast.makeText(MainActivity.this,
							"Unknown exception=" + e.getMessage(), Toast.LENGTH_LONG).show();
				}
			}
		});
	}
}
