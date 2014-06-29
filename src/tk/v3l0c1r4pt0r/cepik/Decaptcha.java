package tk.v3l0c1r4pt0r.cepik;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xml.sax.SAXException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.ImageReader;

public class Decaptcha {
	
	private String key = "";
	private String challenge = "";
	private String respose;
	private String imageUri;
	private String cookie;
	
	public Decaptcha()
	{
		
	}
	
	public Decaptcha(String key)
	{
		this.key = key;
	}

	public String getImageUri() throws IOException, ParserConfigurationException
	{
		downloadRecaptcha();
		imageUri = "http://www.google.com/recaptcha/api/image?c="+challenge+"&th=";
		return imageUri;
	}
	
	public Bitmap getImage() throws IOException, ParserConfigurationException
	{
		String uri = getImageUri();
		Bitmap bmp = downloadImage(uri);
		return bmp;
	}
	
	private void downloadRecaptcha() throws IOException, ParserConfigurationException
	{
		if(challenge=="" || key=="")
		{
			Document doc = downloadDocument("http://ekw.ms.gov.pl/pdcbdkw/pdcbdkw.html");
			String url = doc.getElementsByTag("noscript").get(1).getElementsByTag("iframe").get(0).attr("src");
			key = url.substring(url.indexOf("?k=")+3);
			String script = downloadText("http://www.google.com/recaptcha/api/challenge?k="+key);
			challenge = script.substring(script.indexOf("challenge : '")+13);
			challenge = challenge.substring(0,challenge.indexOf('\''));
		}
		String script = downloadText("http://www.google.com/recaptcha/api/reload?c="+challenge+"&k="+key+"&reason=i&type=image&lang=pl&th=");
		challenge = script.substring(script.indexOf("('")+2);
		challenge = challenge.substring(0,challenge.indexOf('\''));
	}
	
	private Document downloadDocument(String target) throws IOException, ParserConfigurationException
	{
//		URL url = new URL(target);
		String content = downloadText(target);
		Document doc = Jsoup.parse(content);
		return doc;
	}
	
	private String downloadText(String url) throws IOException
	{
	    HttpClient httpClient = new DefaultHttpClient();
	    HttpContext localContext = new BasicHttpContext();
	    HttpGet httpGet = new HttpGet(url);
	    httpGet.removeHeader(httpGet.getFirstHeader("UserAgent"));
	    httpGet.setHeader("UserAgen", "Mozilla/5.0 (X11; Linux x86_64; rv:29.0) Gecko/20100101 Firefox/29.0");
	    HttpResponse response = httpClient.execute(httpGet, localContext);
	    /*Header[] hdrs = response.getHeaders("Set-Cookie");
	    cookie = "";
	    for(Header hdr : hdrs)
	    {
	    	if(hdr != null)
	    	{
	    		String ck = "";
	    		ck = hdr.getValue();
	    		ck = ck.substring(0, ck.indexOf(';')+1);
	    		cookie += ck;
	    	}
	    }*/
	    String result = "";

	    BufferedReader reader = new BufferedReader(
	        new InputStreamReader(
	          response.getEntity().getContent()
	        )
	      );

	    String line = null;
	    while ((line = reader.readLine()) != null){
	      result += line + "\n";
	    }
	    return result;
	}
	
	public String downloadText(String url, Map<String, String> post) throws IOException
	{
	    HttpClient httpClient = new DefaultHttpClient();
//	    HttpContext localContext = new BasicHttpContext();
	    List<NameValuePair> params = new ArrayList<NameValuePair>();
	    
	    //set post data
	    for(Map.Entry<String, String> kv : post.entrySet())
	    {
//	    	localContext.setAttribute(kv.getKey(), kv.getValue());
	    	params.add(new BasicNameValuePair(kv.getKey(), kv.getValue()));
	    }
	    HttpPost httpPost = new HttpPost(url);
	    httpPost.setEntity(new UrlEncodedFormEntity(params));
	    httpPost.removeHeader(httpPost.getFirstHeader("UserAgent"));
	    httpPost.setHeader("UserAgen", "Mozilla/5.0 (X11; Linux x86_64; rv:29.0) Gecko/20100101 Firefox/29.0");
	    /*if(cookie != "")
	    	httpPost.addHeader("Cookie", cookie);*/
	    HttpResponse response = httpClient.execute(httpPost, (HttpContext)null);
	    String result = "";

	    BufferedReader reader = new BufferedReader(
	        new InputStreamReader(
	          response.getEntity().getContent()
	        )
	      );

	    String line = null;
	    while ((line = reader.readLine()) != null){
	      result += line + "\n";
	    }
	    return result;
	}
	
	private Bitmap downloadImage(String url) throws IOException
	{
	    HttpClient httpClient = new DefaultHttpClient();
	    HttpContext localContext = new BasicHttpContext();
	    HttpGet httpGet = new HttpGet(url);
	    httpGet.removeHeader(httpGet.getFirstHeader("UserAgent"));
	    httpGet.setHeader("UserAgen", "Mozilla/5.0 (X11; Linux x86_64; rv:29.0) Gecko/20100101 Firefox/29.0");
	    HttpResponse response = httpClient.execute(httpGet, localContext);
	    Bitmap bmp = BitmapFactory.decodeStream(response.getEntity().getContent());
	    return bmp;
	}

	public String getKey() {
		return key;
	}

	public String getChallenge() {
		return challenge;
	}

	public void setChallenge(String value) {
		challenge = value;
	}

	public String getCookie() {
		return cookie;
	}

}