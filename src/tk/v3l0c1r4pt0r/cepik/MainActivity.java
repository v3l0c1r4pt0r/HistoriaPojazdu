package tk.v3l0c1r4pt0r.cepik;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.client.ClientProtocolException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.SAXException;

import tk.v3l0c1r4pt0r.cepik.CarReport.EntryNotFoundException;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity {

	public final static String resolvedData = "tk.v3l0c1r4pt0r.ksiegiwieczyste.resolvedData";
	
	private Decaptcha decaptcha = null;
	
	private WebService cepik = null;
	
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
		
		@Override
		public void afterTextChanged(Editable s) {
			EditText kw1 = (EditText) findViewById(R.id.rejVal);
			EditText kw2 = (EditText) findViewById(R.id.vinVal);
			String kw1Str = kw1.getText().toString();
			String kw2Str = kw2.getText().toString();
			if(kw1Str.length() == 4 && kw2Str.length() == 8)
				countChecksum(kw1Str, kw2Str);
		}
	};
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Add event listeners
        EditText kw1 = (EditText) findViewById(R.id.rejVal);
        EditText kw2 = (EditText) findViewById(R.id.vinVal);
        TextWatcher tw1 = new MyTextWatcher(/*kw1*/);
        TextWatcher tw2 = new MyTextWatcher(/*kw2*/);
		kw1.addTextChangedListener(tw1);
		kw2.addTextChangedListener(tw2);

		final Button btn = (Button) findViewById(R.id.sendBtn);
		reloadImage(btn);
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (EntryNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					//FIXME: zamienić na info o braku pojazdu w bazie
				}
		    	View v = (View) findViewById(R.id.scrollView1);
		    	final CarReport report = rep;
				v.post(new Runnable() {
					
					@Override
					public void run() {
				    	intent.putExtra(resolvedData, report);
				    	startActivity(intent);
					}
				});
			}
		}).start();
    	
    	
    	/*//Set progress
    	final ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar);
    	pb.setProgress(0);
    	pb.setVisibility(ProgressBar.VISIBLE);
    	
    	//Download data
    	final Map<String,String> request = new HashMap<String, String>();
    	
    	//KW1
    	EditText et = (EditText) findViewById(R.id.KW1);
    	request.put("nrKW1", et.getText().toString().toUpperCase());
    	
    	//KW2
    	et = (EditText) findViewById(R.id.KW2);
    	request.put("nrKW2", et.getText().toString());
    	
    	//KW3
    	et = (EditText) findViewById(R.id.KW3Checksum);
    	request.put("nrKW3", et.getText().toString());
    	
    	pb.setProgress(pb.getMax()*10/100);
    	
    	request.put("recaptcha_challenge_field", decaptcha.getChallenge());
    	
    	pb.setProgress(pb.getMax()*25/100);
    	
    	//CAPTCHA
    	et = (EditText) findViewById(R.id.captchaTxt);
    	request.put("recaptcha_response_field", et.getText().toString());
    	
    	request.put("t_action", "szukaj");
    	
    	final StringBuilder sb = new StringBuilder();
    	EditText edit = (EditText) findViewById(R.id.KW1);
    	sb.append(edit.getText().toString());
    	edit = (EditText) findViewById(R.id.KW2);
    	sb.append("/" + edit.getText().toString());
    	edit = (EditText) findViewById(R.id.KW3Checksum);
    	sb.append("/" + edit.getText().toString());
    	final String KWnumber = sb.toString();
    	
    	final Handler handler = new Handler();
    	
    	final Activity activity = this;
    	pb.setProgress(pb.getMax()*35/100);
    	
    	new Thread(new Runnable()
		{
			@Override
			public void run() {
		    	try {
					String response = decaptcha.downloadText("http://ekw.ms.gov.pl/pdcbdkw/pdcbdkw.html",request);
			    	pb.setProgress(pb.getMax()*50/100);
					//get values from response
					Document doc = Jsoup.parse(response);
					Elements wyniki = doc.getElementById("formArea").getElementsByClass("opis");
			    	final String[] values = new String[7];
					
			    	values[0] = wyniki.get(0).getElementsByClass("poleWartosc").html();
					if(!wyniki.get(0).getElementsByClass("poleWartosc").html().equalsIgnoreCase(KWnumber))//numer
					{
						if(wyniki.html().indexOf("Nie znaleziono księgi")==-1)
							throw new InputMismatchException();
						else
							throw new EntryNotFoundException();
					}
					
			    	values[1] = wyniki.get(6).getElementsByClass("poleWartosc").html();//wlasciciel
			    	values[2] = wyniki.get(5).getElementsByClass("poleWartosc").html();//polozenie
			    	values[3] = wyniki.get(3).getElementsByClass("poleWartosc").html();//data_zapis
			    	values[4] = wyniki.get(4).getElementsByClass("poleWartosc").html();//data_zamkn
			    	values[5] = wyniki.get(1).getElementsByClass("poleWartosc").html();//typ
			    	values[6] = wyniki.get(2).getElementsByClass("poleWartosc").html();//wydzial
			    	
			    	//clean strings
			    	values[1] = stringDeHTML(values[1]);
			    	values[2] = stringDeHTML(values[2]);
			    	values[5] = stringDeHTML(values[5]);//41937
			    	values[6] = stringDeHTML(values[6]);
			    	pb.setProgress(pb.getMax()*60/100);
			    	handler.post(new Runnable() {
						
						@Override
						public void run() {
					    	intent.putExtra(resolvedData, values);
					    	pb.setVisibility(ProgressBar.INVISIBLE);
					    	pb.setProgress(pb.getMax()*1);
					    	startActivity(intent);
							
						}
					});
			    	//wait until values are used?
//			    	try {
//						values.wait();
//					} catch (InterruptedException e) {
//					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (IndexOutOfBoundsException e)
				{
					//kw user looked for did not exist,
					//display error to the user
					final ImageView iv = (ImageView) findViewById(R.id.captchaImage);
					final Button btn = (Button) findViewById(R.id.sendBtn);
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							iv.setImageResource(R.drawable.error);
							btn.setEnabled(false);
							ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar);
							pb.setVisibility(ProgressBar.INVISIBLE);
							AlertDialog.Builder builder = new AlertDialog.Builder(activity);
							builder.setMessage(R.string.wrongKwNumberMsg)
						       .setTitle(R.string.errorMsg);
							builder.setPositiveButton(string.ok, new OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									//just close
								}
							});
							AlertDialog dialog = builder.create();
							dialog.show();
							reloadImage(findViewById(R.id.refreshBtn));
							EditText te = (EditText)findViewById(R.id.captchaTxt);
							te.setText("");
						}
					});
				} catch (InputMismatchException e) {
					//wrong captcha entered,
					//display error
					final ImageView iv = (ImageView) findViewById(R.id.captchaImage);
					final Button btn = (Button) findViewById(R.id.sendBtn);
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							iv.setImageResource(R.drawable.error);
							btn.setEnabled(false);
							ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar);
							pb.setVisibility(ProgressBar.INVISIBLE);
							AlertDialog.Builder builder = new AlertDialog.Builder(activity);
							builder.setMessage(R.string.wrongCaptchaMsg)
						       .setTitle(R.string.errorMsg);
							builder.setPositiveButton(string.ok, new OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									//just close
								}
							});
							AlertDialog dialog = builder.create();
							dialog.show();
							reloadImage(findViewById(R.id.refreshBtn));
							EditText te = (EditText)findViewById(R.id.captchaTxt);
							te.setText("");
						}
					});
				} catch (EntryNotFoundException e) {
					//kw user looked for did not exist,
					//display error to the user
					final ImageView iv = (ImageView) findViewById(R.id.captchaImage);
					final Button btn = (Button) findViewById(R.id.sendBtn);
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							iv.setImageResource(R.drawable.error);
							btn.setEnabled(false);
							ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar);
							pb.setVisibility(ProgressBar.INVISIBLE);
							AlertDialog.Builder builder = new AlertDialog.Builder(activity);
							builder.setMessage(R.string.wrongKwNumberMsg)
						       .setTitle(R.string.errorMsg);
							builder.setPositiveButton(string.ok, new OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									//just close
								}
							});
							AlertDialog dialog = builder.create();
							dialog.show();
							reloadImage(findViewById(R.id.refreshBtn));
							EditText te = (EditText)findViewById(R.id.captchaTxt);
							te.setText("");
						}
					});
				}
			}
		}).start();*/
    }
    
    public void countChecksum(String KW1, String KW2)
    {
    	int dlugoscMax    = 12;
    	int pierwszaCyfra =  4;
    	String dobreLitery   = "ABCDEFGHIJKLMNOPRSTUWYZ";
    	int[] waga          = {1,3,7,1, 3,7,1,3, 7,1,3,7};		// wagi dla kolejnych znakow lancucha
    	int[] litery        = {0,1,3};							// Ktore znaki w lancucu musza byc dobrymi literami
    	int suma = 0;
    	int indeks;
    	
    	//count checksum
    	String lancuch = (KW1 + KW2);
    	lancuch = lancuch.toUpperCase();

    	//Suma dla 3 liter
    	for(int x = 0; x < litery.length; x++) {
    		indeks = dobreLitery.indexOf( lancuch.substring(litery[x],litery[x]+1) );
    		suma  += (indeks + 1) * waga[litery[x]];
    	}
    	// Suma dla cyfry na 2 pozycji ze skorygowana waga
    	try
    	{
    		Integer.parseInt(lancuch.substring(2,3));
    		suma += Integer.parseInt(lancuch.substring(2,3)) * waga[2];
    	}
    	catch(NumberFormatException nfe)
    	{
    		indeks = dobreLitery.indexOf(lancuch.substring(2,3));
    		suma  += (indeks + 1) * waga[2];
    	}
    	// Suma dla 8 pozostalych cyfr
    	for (int i = pierwszaCyfra; i < dlugoscMax; i++) {
    		suma += Integer.parseInt(lancuch.substring(i,i+1)) * waga[i];
    	}
    	
    	EditText edit = (EditText) findViewById(R.id.rejestracjaVal);
    	StringBuilder sb = new StringBuilder();
    	sb.append(suma % 10);
    	edit.setText(sb.toString());
    }
    
    public void reloadImage(View view)
    {
    	//ustawia tło na czarne
		final ImageView iv = (ImageView) findViewById(R.id.captchaImage);
		iv.setBackgroundColor(getResources().getColor(R.color.captchaBg));
		
		//ustaw progress na 0, wyłącz przycisk odświeżania
		final ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar);
		final Button btn = (Button) findViewById(R.id.sendBtn);
		btn.setEnabled(true);
    	pb.setProgress(0);
    	pb.setVisibility(ProgressBar.VISIBLE);
    	
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
					//TODO: wyłapać błąd wczytywania
					final ImageView iv = (ImageView) findViewById(R.id.captchaImage);
					final Button btn = (Button) findViewById(R.id.sendBtn);
					iv.post(new Runnable() {
						
						@Override
						public void run() {
					    	pb.setProgress(pb.getMax());
							iv.setImageBitmap(captcha);
							iv.setBackgroundColor(getResources().getColor(R.color.captchaBg));
							btn.setEnabled(true);
					    	pb.setVisibility(ProgressBar.INVISIBLE);
						}
					});
				} catch (IOException e) {
					e.printStackTrace();//FIXME: obsłużyć
				}
			}
		}).start();
    }
    
    private String stringDeHTML(String str)
    {
    	Pattern pattern = Pattern.compile("\\<[/a-zA-Z]*\\>");
    	str = pattern.matcher(str).replaceAll("");
    	pattern = Pattern.compile("\\&Oacute\\;");//12567
    	str = pattern.matcher(str).replaceAll("Ó");
    	pattern = Pattern.compile("\\&quot\\;");//39164
    	str = pattern.matcher(str).replaceAll("'");
    	return str;
    }
    
}
