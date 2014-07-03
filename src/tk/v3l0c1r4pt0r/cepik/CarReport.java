package tk.v3l0c1r4pt0r.cepik;

import java.io.Serializable;
import java.util.InputMismatchException;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class CarReport implements Serializable {
	
	public class EntryNotFoundException extends Exception
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = -1777986521521918105L;
		
	}
	
	public class WrongCaptchaException extends Exception
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 998154090424975252L;
		
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2593232509120742403L;

	public String getMarka() {
		return marka;
	}

	public String getTyp() {
		return typ;
	}

	public String getModel() {
		return model;
	}

	public String getPrzebieg() {
		return przebieg;
	}

	public String getPrzebiegUnit() {
		return przebiegUnit;
	}

	public boolean getOc() {
		return oc;
	}

	public boolean getPrzeglad() {
		return przeglad;
	}

	public boolean getKradziony() {
		return kradziony;
	}

	public String getNrRejestracyjny() {
		return nrRejestracyjny;
	}

	public String getVin() {
		return vin;
	}

	public String getRokProdukcji() {
		return rokProdukcji;
	}

	public String getStatus() {
		return status;
	}

	public String getRodzaj() {
		return rodzaj;
	}

	public String getPodrodzaj() {
		return podrodzaj;
	}

	public String getPojemnoscSilnika() {
		return pojemnoscSilnika;
	}

	public String getRodzajPaliwa() {
		return rodzajPaliwa;
	}

	public String getMocSilnika() {
		return mocSilnika;
	}

	public String getLiczbaMiejsc() {
		return liczbaMiejsc;
	}

	public String getLiczbaMiejscSiedzacych() {
		return liczbaMiejscSiedzacych;
	}

	public String getMasaWlasna() {
		return masaWlasna;
	}

	public String getMasaPrzyczepyZHamulcem() {
		return masaPrzyczepyZHamulcem;
	}

	public String getMasaPrzyczepyBezHamulca() {
		return masaPrzyczepyBezHamulca;
	}

	public String getDmc() {
		return dmc;
	}

	public String getLiczbaOsi() {
		return liczbaOsi;
	}

	public String getDataRejestracji() {
		return dataRejestracji;
	}

	public String getDataWydaniaDowodu() {
		return dataWydaniaDowodu;
	}

	public String getDataWydaniaKartyPojazdu() {
		return dataWydaniaKartyPojazdu;
	}

	String marka;
	String typ;
	String model;
	
	String przebieg;
	String przebiegUnit;
	boolean oc;
	boolean przeglad;
	boolean kradziony;
	
	String nrRejestracyjny;
	String vin;
	String rokProdukcji;
	String status;
	
	String rodzaj;
	String podrodzaj;
	
	String pojemnoscSilnika;
	String rodzajPaliwa;
	
	String mocSilnika;
	String liczbaMiejsc;
	String liczbaMiejscSiedzacych;
	
	String masaWlasna;
	String masaPrzyczepyZHamulcem;
	String masaPrzyczepyBezHamulca;
	String dmc;
	
	String liczbaOsi;
	
	String dataRejestracji;
	String dataWydaniaDowodu;
	String dataWydaniaKartyPojazdu;
	
	//TODO: oś czasu
	
	public CarReport(String nrRejestracyjny, String vin, String dataRejestracji)
	{
		this.nrRejestracyjny = nrRejestracyjny;
		this.vin = vin;
		this.dataRejestracji = dataRejestracji;
	}
	
	public CarReport(String nrRejestracyjny, String siteResponse) throws EntryNotFoundException, WrongCaptchaException
	{
		this.nrRejestracyjny = nrRejestracyjny;
		//TODO: wypełnić na podstawie odpowiedzi serwera
		//TODO: obsłużyć not found
		if(siteResponse.indexOf("nieprawidłowy kod") != -1)
		{
			throw new WrongCaptchaException();
		}
		else if(siteResponse.indexOf("nie znaleziono") != -1)
		{
			throw new EntryNotFoundException();
		}
		Document doc = Jsoup.parse(siteResponse);
		
    	try
    	{
    		this.marka = stringDeHTML(doc.getElementById("marka").html());
    	}
    	catch(NullPointerException e)
    	{
    		this.marka = "";
    	}
    	try
    	{
    		this.typ = stringDeHTML(doc.getElementById("typ").html());
    	}
    	catch(NullPointerException e)
    	{
    		this.typ = "";
    	}
    	try
    	{
    		this.model = stringDeHTML(doc.getElementById("model").html());
    	}
    	catch(NullPointerException e)
    	{
    		this.model = "";
    	}
    	try
    	{
    		this.przebieg = stringDeHTML(doc.getElementById("aktualnyStanLicznika").html());
    	}
    	catch(NullPointerException e)
    	{
    		this.przebieg = "";
    	}
    	try
    	{
    		this.przebiegUnit = stringDeHTML(doc.getElementById("jednostkaLicznika").html());
    	}
    	catch(NullPointerException e)
    	{
    		this.przebiegUnit = "";
    	}
    	try
    	{
    		this.rokProdukcji = stringDeHTML(doc.getElementById("rokProdukcji").html());
    	}
    	catch(NullPointerException e)
    	{
    		this.rokProdukcji = "";
    	}
    	try
    	{
    		this.rodzaj = stringDeHTML(doc.getElementById("rodzaj").html().toLowerCase());
    	}
    	catch(NullPointerException e)
    	{
    		this.rodzaj = "";
    	}
    	try
    	{
    		this.podrodzaj = stringDeHTML(doc.getElementById("podrodzaj").html().toLowerCase());
    	}
    	catch(NullPointerException e)
    	{
    		this.podrodzaj = "";
    	}
    	try
    	{
    		this.pojemnoscSilnika = stringDeHTML(doc.getElementById("pojemnosc").html());
    	}
    	catch(NullPointerException e)
    	{
    		this.pojemnoscSilnika = "";
    	}
    	try
    	{
    		this.rodzajPaliwa = stringDeHTML(doc.getElementById("paliwo").html().toLowerCase());
    	}
    	catch(NullPointerException e)
    	{
    		this.rodzajPaliwa = "";
    	}
    	try
    	{
			this.vin = stringDeHTML(doc.getElementsContainingOwnText("VIN").select("span").html());
    	}
    	catch(NullPointerException e)
    	{
    		this.vin = "";
    	}
    	try
    	{
			this.status = stringDeHTML(doc.getElementsContainingOwnText("Status rejestracji").select("span").html()).toLowerCase();
    	}
    	catch(NullPointerException e)
    	{
    		this.status = "";
    	}
			
			//TODO: przetestować działanie oc i przeglądu dla innych konfiguracji
			
    	try
    	{
			if(doc.getElementsContainingOwnText("Polisa OC").select("span")
					.html().indexOf("brak informacji o aktualnej polisie")!=-1)
				this.oc = false;
			else
				this.oc = true;
    	}
    	catch(NullPointerException e)
    	{
    		this.oc = false;
    	}
			
    	try
    	{
			if(doc.getElementsContainingOwnText("Badanie techniczne").select("span")
					.html().indexOf("aktualne") != -1)
				this.przeglad = true;
			else
				this.przeglad = false;
    	}
    	catch(NullPointerException e)
    	{
    		this.przeglad = false;
    	}
    	try
    	{
    		String stolen = doc.getElementsByClass("stolen").html();
    		if(stolen.indexOf("kradziony") != -1)
    		{
    			kradziony = true;
    		}
    		else
    			kradziony = false;
    	}
    	catch(NullPointerException e)
    	{
    		kradziony = false;
    	}
	}
    
    private String stringDeHTML(String str)
    {
    	Pattern pattern = Pattern.compile("\\<[/a-zA-Z]*\\>");
    	str = pattern.matcher(str).replaceAll("");
    	
    	pattern = Pattern.compile("\\&Oacute\\;");
    	str = pattern.matcher(str).replaceAll("Ó");
    	
    	pattern = Pattern.compile("\\&oacute\\;");
    	str = pattern.matcher(str).replaceAll("ó");
    	
    	pattern = Pattern.compile("\\&quot\\;");
    	str = pattern.matcher(str).replaceAll("'");
    	
    	pattern = Pattern.compile("\\&nbsp\\;");
    	str = pattern.matcher(str).replaceAll(" ");
    	
    	pattern = Pattern.compile("\\&sup3\\;");
    	str = pattern.matcher(str).replaceAll("³");
    	return str;
    }

}
