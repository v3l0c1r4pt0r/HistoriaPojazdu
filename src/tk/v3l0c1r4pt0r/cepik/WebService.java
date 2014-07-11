package tk.v3l0c1r4pt0r.cepik;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import tk.v3l0c1r4pt0r.cepik.CarReport.EntryNotFoundException;
import tk.v3l0c1r4pt0r.cepik.CarReport.WrongCaptchaException;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class WebService implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8796560971836763585L;
	private String cookie;
	private String javaxState;
	private static String mainUrl = "https://historiapojazdu.gov.pl/historia-pojazdu-web/index.xhtml";
	private static String pdfUrl = "https://historiapojazdu.gov.pl/historia-pojazdu-web/historiaPojazdu.xhtml";
	private static String captchaUrl = "https://historiapojazdu.gov.pl/historia-pojazdu-web/captcha";
	private static String userAgent = "Mozilla/5.0 (X11; Linux x86_64; rv:30.0) Gecko/20100101 Firefox/30.0";
	
	private Context context = null;
	
	public enum Field
	{
		Rej, Vin, Date, Captcha
	}
	
	public class ReportNotGeneratedException extends Exception
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 6951871210320962719L;
		
	}
	
	public class InvalidInputException extends Exception
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 2215118885828047337L;
		public Field field;
		
		public InvalidInputException(Field f)
		{
			this.field = f;
		}
		
	}
	
	public WebService(Context con) throws KeyManagementException, NoSuchAlgorithmException, IOException
	{   
		this.context = con;

		URL url = new URL(mainUrl);
		HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
		
		urlConnection.setSSLSocketFactory(getFactory());
		
		urlConnection.setRequestProperty("User-Agent", userAgent);
		try {
			urlConnection.connect();
			List<String> cookies = urlConnection.getHeaderFields().get("Set-Cookie");
			cookie = "";
			for(String c : cookies)
			{
				cookie += c.substring(0, c.indexOf(';')) + "; ";
			}
		}
		catch(IOException e)
		{
			throw e;
		}
	    finally {
	    	urlConnection.disconnect();
	    }
	}
	
	private byte[] getResponse(URL url) throws IOException, KeyManagementException, NoSuchAlgorithmException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
		
		urlConnection.setSSLSocketFactory(getFactory());
		urlConnection.setRequestProperty("User-Agent", userAgent);
		urlConnection.setRequestProperty("Cookie", cookie);

		try {
			urlConnection.connect();
			InputStream is = urlConnection.getInputStream();
			int len = 0;
			while ((len = is.read(buffer)) != -1) 
			{
				baos.write(buffer, 0, len);
			}
			
		}
		catch(IOException e)
		{
			throw e;
		}
	    finally {
	    	urlConnection.disconnect();
	    }
		return baos.toByteArray();
	}
	
	private byte[] getResponse(URL url, String postData) throws IOException, KeyManagementException, NoSuchAlgorithmException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
		
		urlConnection.setSSLSocketFactory(getFactory());
		urlConnection.setRequestProperty("User-Agent", userAgent);
		urlConnection.setRequestProperty("Cookie", cookie);
		urlConnection.setRequestMethod("POST");
		urlConnection.setDoInput(true);
		urlConnection.setDoOutput(true);

		OutputStream os = urlConnection.getOutputStream();
		BufferedWriter writer = new BufferedWriter(
		        new OutputStreamWriter(os, "UTF-8"));
		writer.write(postData);
		writer.flush();
		writer.close();
		os.close();

		try {
			urlConnection.connect();
			InputStream is = urlConnection.getInputStream();
			int len = 0;
			while ((len = is.read(buffer)) != -1) 
			{
				baos.write(buffer, 0, len);
			}
			
		}
		catch(IOException e)
		{
			throw e;
		}
	    finally {
	    	urlConnection.disconnect();
	    }
		return baos.toByteArray();
	}
	
	private WebFile getResponseAsFile(URL url, String postData) throws IOException, KeyManagementException, NoSuchAlgorithmException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
		
		urlConnection.setSSLSocketFactory(getFactory());
		urlConnection.setRequestProperty("User-Agent", userAgent);
		urlConnection.setRequestProperty("Cookie", cookie);
		urlConnection.setRequestMethod("POST");
		urlConnection.setDoInput(true);
		urlConnection.setDoOutput(true);

		OutputStream os = urlConnection.getOutputStream();
		BufferedWriter writer = new BufferedWriter(
		        new OutputStreamWriter(os, "UTF-8"));
		writer.write(postData);
		writer.flush();
		writer.close();
		os.close();
		
		String fileName;

		try {
			urlConnection.connect();
			InputStream is = urlConnection.getInputStream();
			int len = 0;
			while ((len = is.read(buffer)) != -1) 
			{
				baos.write(buffer, 0, len);
			}
			String disposition = urlConnection.getHeaderField("Content-Disposition");
			fileName = disposition.substring(disposition.indexOf("filename=")+9);
		}
		catch(IOException e)
		{
			throw e;
		}
	    finally {
	    	urlConnection.disconnect();
	    }
		return new WebFile(fileName,baos.toByteArray());
	}
	
	public Bitmap getCaptcha() throws MalformedURLException, IOException, KeyManagementException, NoSuchAlgorithmException
	{
		byte[] response = getResponse(new URL(captchaUrl));
		Bitmap bmp = BitmapFactory.decodeByteArray(response, 0, response.length);
		return bmp;
	}
	
	public CarReport getReport(String nrRejestracyjny, String vin, String dataRejestracji, String captcha) 
			throws MalformedURLException, IOException, EntryNotFoundException, WrongCaptchaException, 
			InvalidInputException, KeyManagementException, NoSuchAlgorithmException
	{
		if(nrRejestracyjny.length() == 0)
		{
			throw new InvalidInputException(Field.Rej);
		}
		else if(vin.length() < 17)
		{
			/*//przykładowy raport
			String post = 
					  "formularz=formularz&"
					+ "rej="+nrRejestracyjny+"&"
					+ "vin="+vin+"&"
					+ "data="+dataRejestracji+"&"
					+ "captchaAnswer="+captcha+"&"
					+ "btnSprawdz=Sprawd%C5%BA+pojazd+%C2%BB&"
					+ "com.sun.faces.StatelessPostback=value";
			byte[] response = getResponse(new URL("https://historiapojazdu.gov.pl/historia-pojazdu-web/przykladowy-raport.xhtml"));
			String reportStr = new String(response);
			//get javaxState
			CarReport cr = new CarReport(nrRejestracyjny, reportStr);
			Document doc = Jsoup.parse(reportStr);
			javaxState = doc.getElementById("javax.faces.ViewState").attributes().get("value");
			return cr;*/
			throw new InvalidInputException(Field.Vin);
		}
		else if(dataRejestracji.length() < 10)
		{
			throw new InvalidInputException(Field.Date);
		}
		else if(captcha.length() == 0)
		{
			throw new InvalidInputException(Field.Captcha);
		}
		else
		{
			//prawdziwy raport
			String post = 
					  "formularz=formularz&"
					+ "rej="+nrRejestracyjny+"&"
					+ "vin="+vin+"&"
					+ "data="+dataRejestracji+"&"
					+ "captchaAnswer="+captcha+"&"
					+ "btnSprawdz=Sprawd%C5%BA+pojazd+%C2%BB&"
					+ "com.sun.faces.StatelessPostback=value";
			byte[] response = getResponse(new URL(mainUrl), post);
			String reportStr = new String(response);
			//get javaxState
			CarReport cr = new CarReport(nrRejestracyjny, reportStr);
			Document doc = Jsoup.parse(reportStr);
			javaxState = doc.getElementById("javax.faces.ViewState").attributes().get("value");
			return cr;
		}
	}
	
	public WebFile getReportPdf() throws MalformedURLException, IOException, ReportNotGeneratedException, KeyManagementException, NoSuchAlgorithmException
	{
		if(javaxState != null && javaxState != "")
		{
			String post = 
					    "formularz=formularz&"
					  + "javax.faces.ViewState="+javaxState+"&"
					  + "pobierzRaportPdf=pobierzRaportPdf";
			WebFile response = getResponseAsFile(new URL(pdfUrl), post);
			return response;
		}
		else
			throw new ReportNotGeneratedException();
	}
	
	public class EmptyTrustManager implements X509TrustManager
	{//FIXME: it is not good idea to do this that way

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	private SSLSocketFactory getFactory() throws KeyManagementException, NoSuchAlgorithmException
	{
		// Create an SSLContext that uses our TrustManager
		SSLContext context = SSLContext.getInstance("TLS");
		TrustManager[] tms = new TrustManager[1];
		tms[0] = new EmptyTrustManager();
		context.init(null, tms, null);
		return context.getSocketFactory();
	}

}
