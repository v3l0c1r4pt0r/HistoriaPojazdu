package tk.v3l0c1r4pt0r.cepik;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
	
	public boolean isHomologacja() {
		return homologacja;
	}

	public String gethKategoria() {
		return hKategoria;
	}

	public String gethWersja() {
		return hWersja;
	}

	public String gethNumer() {
		return hNumer;
	}

	public String gethWariant() {
		return hWariant;
	}
	
	public String getEmisjaCo2() {
		return emisjaCo2;
	}

	public String getZuzyciePaliwa() {
		return zuzyciePaliwa;
	}

	public String getDopuszczalnaLadownosc() {
		return dopuszczalnaLadownosc;
	}

	public String getRozstawOsi() {
		return rozstawOsi;
	}

	public String getRozstawKol() {
		return rozstawKol;
	}

	public String getNaciskNaOs() {
		return naciskNaOs;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<Event> getZdarzenia() {
		return zdarzenia;
	}

	public String getWlascicieleSum() {
		return wlascicieleSum;
	}

	public String getWspolwlascicieleSum() {
		return wspolwlascicieleSum;
	}

	public String getWojewodztwo() {
		return wojewodztwo;
	}

	public String getPolisa() {
		return polisa;
	}

	public String getBadanie() {
		return badanie;
	}

	public String getKradzionyOpis() {
		return kradzionyOpis;
	}

	public String getWlascicieleAkt() {
		return wlascicieleAkt;
	}

	public String getWspolwlascicieleAkt() {
		return wspolwlascicieleAkt;
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
	String emisjaCo2;

	String zuzyciePaliwa;
	String liczbaMiejsc;
	String liczbaMiejscSiedzacych;
	
	String masaWlasna;
	String masaPrzyczepyZHamulcem;
	String masaPrzyczepyBezHamulca;
	String dopuszczalnaLadownosc;
	String dmc;
	
	String liczbaOsi;
	String rozstawOsi;
	String rozstawKol;
	String naciskNaOs;
	
	String dataRejestracji;
	String dataWydaniaDowodu;
	String dataWydaniaKartyPojazdu;
	
	boolean homologacja;
	String hKategoria;
	String hWersja;
	String hNumer;
	String hWariant;

	List<Event> zdarzenia = new ArrayList<Event>();

	String wlascicieleSum;
	String wspolwlascicieleSum;
	
	String wojewodztwo;
	String polisa;
	String badanie;
	String kradzionyOpis;
	String wlascicieleAkt;
	String wspolwlascicieleAkt;
	
	public CarReport(String nrRejestracyjny, String vin, String dataRejestracji)
	{
		this.nrRejestracyjny = nrRejestracyjny;
		this.vin = vin;
		this.dataRejestracji = dataRejestracji;
	}
	
	public CarReport(String nrRejestracyjny, String siteResponse) throws EntryNotFoundException, WrongCaptchaException
	{
		this.nrRejestracyjny = nrRejestracyjny;
		if(siteResponse.indexOf("nieprawidłowy kod") != -1)
		{
			throw new WrongCaptchaException();
		}
		else if(siteResponse.indexOf("nie znaleziono") != -1)
		{
			throw new EntryNotFoundException();
		}
		Document doc = Jsoup.parse(siteResponse);
		
    	this.marka = setById("marka", doc);
    	this.typ = setById("typ", doc);
    	this.model = setById("model", doc);
    	this.przebieg = setById("aktualnyStanLicznika", doc);
    	this.przebiegUnit = setById("jednostkaLicznika", doc);
    	this.rokProdukcji = setById("rokProdukcji", doc);
    	this.rodzaj = setById("rodzaj", doc).toLowerCase();
    	this.podrodzaj = setById("podrodzaj", doc).toLowerCase();
    	this.pojemnoscSilnika = setById("pojemnosc", doc);
    	this.rodzajPaliwa = setById("paliwo", doc).toLowerCase();
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

    	try
    	{
    		this.dataRejestracji = doc.getElementsContainingOwnText("Pierwsza rejestracja w Polsce").
    				parents().parents().parents().
    				get(0).getElementsByClass("date").
    				get(0).getElementsByTag("p").
    				html();
		}
		catch(NullPointerException e)
		{
			this.dataRejestracji = "";
		}
		catch(IndexOutOfBoundsException e)
		{
			this.dataRejestracji = "";
		}


    	this.dataWydaniaDowodu = setById("dataWydaniaDowRej", doc);
    	this.dataWydaniaKartyPojazdu = setById("dataWydaniaKartyPojazdu", doc);

    	try
    	{
			Element el = doc.getElementsContainingOwnText("Świadectwo homologacji").parents().get(0);
			if(el != null)
			{
				this.homologacja = true;
				this.hKategoria = stringDeHTML(el.getElementById("dokumentyKategoria").html());
				this.hWersja = stringDeHTML(el.getElementById("dokumentWersja").html());
				this.hNumer = stringDeHTML(el.getElementById("dokumentNumer").html());
				this.hWariant = stringDeHTML(el.getElementById("dokumentWariant").html());
			}
			else
				this.homologacja = false;
    	}
		catch(NullPointerException e)
		{
			this.homologacja = false;
		}
		catch(IndexOutOfBoundsException e)
		{
			this.homologacja = false;
		}

    	//techniczne
    	this.mocSilnika = setById("mocSilnika", doc);
    	this.emisjaCo2 = setById("emisjaCO2", doc);
    	this.zuzyciePaliwa = setById("srednieZuzyciePaliwa", doc);
    	this.liczbaMiejsc = setById("liczbaMiejscOgolem", doc);
    	this.liczbaMiejscSiedzacych = setById("liczbaMiejscSiedzacych", doc);
    	this.masaWlasna = setById("masaWlasna", doc);
    	this.masaPrzyczepyZHamulcem = setById("maxMasaPrzyczepyZHamulcem", doc);
    	this.masaPrzyczepyBezHamulca = setById("maxMasaPrzyczepyBezHamulca", doc);
    	this.dopuszczalnaLadownosc = setById("dopuszczalnaLadownosc", doc);
    	this.dmc = setById("dopuszczalnaMasaCalkowita", doc);
    	this.liczbaOsi = setById("liczbaOsi", doc);
    	this.rozstawOsi = setById("rozstawOsi", doc);
    	this.rozstawKol = setById("rozstawKol", doc);
    	this.naciskNaOs = setById("maxNaciskNaOs", doc);
			
		//TODO: przetestować działanie przeglądu dla innych konfiguracji
			
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

    	//os
    	Element table = doc.getElementById("timeline");
    	Element produkcja = table.getElementsByTag("thead").get(0).getElementById("production");
    	{
    		String data = produkcja.getElementsByClass("date").get(0).getElementsByTag("strong").get(0).html();
    		String opis = produkcja.getElementsByClass("description").get(0).getElementsByTag("strong").get(0).html();
    		this.zdarzenia.add(new Event(data, opis));
    	}
    	
    	for(int i = 0; i < 10; i++)//TODO: TEMP
    	{
    		this.zdarzenia.add(new Event(""+(int)(1900+i), "Zdarzenie #"+i));
    	}
    	
    	Element raport = table.getElementsByTag("tfoot").get(0).getElementById("summary");
    	{
    		String data = raport.getElementsByClass("date").get(0).html();
    		String opis = raport.getElementsByClass("description").get(0).getElementsByTag("p").get(0).html();
    		this.zdarzenia.add(new Event(data, opis));
    	}
    	
    	
    	this.wlascicieleSum = setById("iloscWlascicieliPojazdu2", doc);
    	this.wspolwlascicieleSum = setById("iloscWspolwlascicieliPojazdu2", doc);
    	this.wojewodztwo = setById("miejsceZarejestrowania", doc);
    	
    	this.polisa = setById("infoOPolisieOC", doc);
    	if(this.polisa == "")
    		this.polisa = setById("infoOPolisieOCBrak", doc);
    	
    	this.badanie = setById("infoOBadaniuTechnicznym", doc);
    	if(this.badanie == "")
    		this.badanie = setById("infoOBadaniuTechnicznymBrak", doc);
    	
    	this.wlascicieleAkt = setById("aktualnaIloscWlascicieliPojazdu2", doc);
    	this.wspolwlascicieleAkt = setById("aktualnaIloscWspolwlascicieliPojazdu2", doc);
	}
	
	private String setById(String id, Document doc)
	{
		String result;
    	try
    	{
			result = stringDeHTML(doc.getElementById(id).html());
    	}
		catch(NullPointerException e)
		{
			result = "";
		}
    	return result;
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
