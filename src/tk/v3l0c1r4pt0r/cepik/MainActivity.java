package tk.v3l0c1r4pt0r.cepik;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.client.ClientProtocolException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.SAXException;

import tk.v3l0c1r4pt0r.cepik.CarReport.EntryNotFoundException;
import tk.v3l0c1r4pt0r.cepik.CarReport.WrongCaptchaException;
import tk.v3l0c1r4pt0r.cepik.ResultsActivity.PlaceholderFragment;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity {

	public final static String resolvedData = "tk.v3l0c1r4pt0r.ksiegiwieczyste.resolvedData";
	
	private Decaptcha decaptcha = null;
	
	private WebService cepik = null;
	
	private Drawable normalDateBg = null;
	
//	private final String[] values = new String[7];
	
	String cookieContent = "";
	
	class MyTextWatcher implements TextWatcher {
		
		/*EditText et = null;
		
		public MyTextWatcher(EditText et)
		{
			this.et = et;
		}*/
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}
		
		@SuppressWarnings("deprecation")
		@Override
		public void afterTextChanged(Editable s) {
			EditText date = (EditText) findViewById(R.id.rejestracjaVal);
			
			String dateStr = date.getText().toString();
			
			String dateNewStr = DataValidator.validateDate(dateStr);
			int sel = date.getSelectionStart();
			if(dateStr != dateNewStr)
			{
				//zabezpieczenie przed nieskończoną pętlą
				date.setText(dateNewStr);
				try
				{
					date.setSelection(sel-1, sel-1);
				} catch (IndexOutOfBoundsException e)
				{
					//nie powinno się zdarzyć, ale jeśli się zdarzy to niech chociaż ktoś kto czyta logi wie
					e.printStackTrace();
				}
			}
			if(dateNewStr.length() == 10 && !DataValidator.validateDateFormat(dateNewStr))
			{
				//niepoprawny format daty
				date.setBackgroundDrawable(getResources().getDrawable(R.drawable.textfield_wrong_holo_dark));
			}
			else
				date.setBackgroundDrawable(normalDateBg);
		}
	};
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Add event listeners
        EditText dateVal = (EditText) findViewById(R.id.rejestracjaVal);
        EditText vinVal = (EditText) findViewById(R.id.vinVal);
        TextWatcher tw1 = new MyTextWatcher(/*kw1*/);
        TextWatcher tw2 = new MyTextWatcher(/*kw2*/);
		dateVal.addTextChangedListener(tw1);
		vinVal.addTextChangedListener(tw2);

		final Button btn = (Button) findViewById(R.id.sendBtn);
		reloadImage(btn);
		normalDateBg = dateVal.getBackground();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId())
    	{
    	case R.id.action_about:
    		//create dialog with author's data
			MainActivity.menuShowAbout(this);
    		break;
    	}
    	return super.onOptionsItemSelected(item);
    }
    
    public static void menuShowAbout(Activity act)
    {
		//create dialog with author's data
		AlertDialog.Builder builder = new AlertDialog.Builder(act);
		builder.setMessage(R.string.aboutContentMsg)
	       .setTitle(R.string.action_about);
		builder.setPositiveButton(string.ok, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//just close
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
    }
    
    public void sendRequest(View view)
    {
    	final Intent intent = new Intent(this, ResultActivity.class);
    	
    	final Button btn = (Button) findViewById(R.id.sendBtn);
    	btn.setEnabled(false);
    	
    	final Activity thisActivity = this;

    	final String nrRejestracyjny = ((EditText)findViewById(R.id.rejVal)).getText().toString();
    	final String vin = ((EditText)findViewById(R.id.vinVal)).getText().toString();
    	final String dataRejestracji = ((EditText)findViewById(R.id.rejestracjaVal)).getText().toString();
    	final String captcha = ((EditText)findViewById(R.id.captchaVal)).getText().toString();
    	
    	//pobieramy w nowym wątku
    	new Thread(new Runnable()
		{
			@Override
			public void run() {
		    	CarReport rep = null;
		    	try {
					rep = cepik.getReport(nrRejestracyjny, vin, dataRejestracji, captcha);
				} catch (MalformedURLException e) {
					e.printStackTrace();
					return;
				} catch (final IOException e) {
					View v = findViewById(R.id.scrollView1);
					final ImageView iv = (ImageView) findViewById(R.id.captchaImage);
					v.post(new Runnable() {
						
						@Override
						public void run() {
							iv.setImageResource(R.drawable.error);
							iv.setBackgroundColor(getResources().getColor(R.color.captchaErr));
//							refreshBtn.setEnabled(true);
							ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar);
							pb.setVisibility(ProgressBar.INVISIBLE);
							AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
							builder.setMessage(e.getLocalizedMessage())
						       .setTitle(R.string.errorMsg);
							builder.setPositiveButton(string.ok, new OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									//just close
								}
							});
							AlertDialog dialog = builder.create();
							dialog.show();
						}
					});
					return;
				} catch (final Exception e) {
					if(
							e instanceof EntryNotFoundException || 
							e instanceof WrongCaptchaException
							)
					{
						View v = findViewById(R.id.scrollView1);
						final ImageView iv = (ImageView) findViewById(R.id.captchaImage);
						v.post(new Runnable() {
							
							@Override
							public void run() {
								iv.setImageResource(R.drawable.loading);
								iv.setBackgroundColor(getResources().getColor(R.color.captchaErr));
						    	Button btn = (Button) findViewById(R.id.sendBtn);
						    	btn.setEnabled(true);
								ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar);
								pb.setVisibility(ProgressBar.INVISIBLE);
								AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
								if(e instanceof WrongCaptchaException)
								{
									builder.setMessage(getResources().getString(R.string.wrongCaptchaMsg))
								       .setTitle(R.string.errorMsg);
									EditText et = (EditText) findViewById(R.id.captchaVal);
									et.setText("");
								}
								else if(e instanceof EntryNotFoundException)
								{
									builder.setMessage(getResources().getString(R.string.notFoundMsg))
										.setTitle(R.string.notfMsg);
								}
								else 
									e.printStackTrace();
								builder.setPositiveButton(string.ok, new OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										//just close
									}
								});
								AlertDialog dialog = builder.create();
								dialog.show();
							}
						});
						//TODO: znaleźć sposób na odświeżenie obrazka be wywoływania reloadImage()
						return;
					}
					else 
					{
						e.printStackTrace();
						return;
					}
				}
		    	View v = (View) findViewById(R.id.scrollView1);
		    	final CarReport report = rep;
				v.post(new Runnable() {
					
					@Override
					public void run() {
				    	intent.putExtra(resolvedData, report);
				    	
				    	Button btn = (Button) findViewById(R.id.sendBtn);
				    	btn.setEnabled(true);
				    	
				    	startActivity(intent);
					}
				});
			}
		}).start();
    }
    
    public void reloadImage(View view)
    {
    	//ustawia tło na czarne
		final ImageView iv = (ImageView) findViewById(R.id.captchaImage);
		iv.setImageResource(R.drawable.loading);
		iv.setBackgroundColor(getResources().getColor(R.color.captchaErr));
		
		//ustaw progress na 0, wyłącz przycisk odświeżania
		final ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar);
		final ImageButton refreshBtn = (ImageButton) findViewById(R.id.refreshBtn);
//		refreshBtn.setEnabled(false);
    	
    	final Button sendBtn = (Button) findViewById(R.id.sendBtn);
    	sendBtn.setEnabled(false);
    	pb.setProgress(0);
    	pb.setVisibility(ProgressBar.VISIBLE);
    	
    	final Activity thisActivity = this;
    	
    	//pobieramy w nowym wątku
    	new Thread(new Runnable()
		{
			@Override
			public void run() {
				try {
					if(cepik==null)
					{
						cepik = new WebService();	//ustawia ciastko
					}
					
					final Bitmap captcha = cepik.getCaptcha();	//pobiera captchę
					final ImageView iv = (ImageView) findViewById(R.id.captchaImage);
					iv.post(new Runnable() {
						
						@Override
						public void run() {
					    	pb.setProgress(pb.getMax());
							iv.setImageBitmap(captcha);
							iv.setBackgroundColor(getResources().getColor(R.color.captchaBg));
//							refreshBtn.setEnabled(true);
					    	sendBtn.setEnabled(true);
					    	pb.setVisibility(ProgressBar.INVISIBLE);
						}
					});
				} catch (final IOException e)
				{
					iv.post(new Runnable() {
						
						@Override
						public void run() {
							iv.setImageResource(R.drawable.error);
							iv.setBackgroundColor(getResources().getColor(R.color.captchaErr));
//							refreshBtn.setEnabled(true);
							ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar);
							pb.setVisibility(ProgressBar.INVISIBLE);
							AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
							builder.setMessage(e.getLocalizedMessage())
						       .setTitle(R.string.errorMsg);
							builder.setPositiveButton(string.ok, new OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									//just close
								}
							});
							AlertDialog dialog = builder.create();
							dialog.show();
						}
					});
				}
			}
		}).start();
    }
    
}
