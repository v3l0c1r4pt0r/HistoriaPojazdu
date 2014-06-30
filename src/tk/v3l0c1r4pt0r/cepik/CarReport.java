package tk.v3l0c1r4pt0r.cepik;

import android.R.bool;

public class CarReport {
	
	public String getMarka() {
		return marka;
	}

	public String getModel() {
		return model;
	}

	public String getPrzebieg() {
		return przebieg;
	}

	public bool getOc() {
		return oc;
	}

	public bool getPrzeglad() {
		return przeglad;
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
	String model;
	
	String przebieg;
	bool oc;
	bool przeglad;
	
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
	
	public CarReport(String nrRejestracyjny, String siteResponse)
	{
		this.nrRejestracyjny = nrRejestracyjny;
		//TODO: wypełnić na podstawie odpowiedzi serwera
	}

}
