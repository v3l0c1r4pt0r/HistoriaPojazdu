package tk.v3l0c1r4pt0r.cepik;

public class HistoryElement {
	public String id;
	private String nrRej;
	private String opis;
	private String vin;
	private String dataRej;

	public String getNrRej() {
		return nrRej;
	}

	public String getOpis() {
		return opis;
	}

	public String getVin() {
		return vin;
	}

	public String getDataRej() {
		return dataRej;
	}

	public HistoryElement(String id, String content, String subcontent) {
		this.id = id;
		this.nrRej = content;
		this.opis = subcontent;
		this.vin = "";
		this.dataRej = "";
	}

	public HistoryElement(String id, String rej, String opis, String vin, String data) {
		this.id = id;
		this.nrRej = rej;
		this.opis = opis;
		this.vin = vin;
		this.dataRej = data;
	}
}
