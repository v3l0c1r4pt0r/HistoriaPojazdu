package tk.v3l0c1r4pt0r.cepik;

public class Event {
	
	private String data;
	private String opis;
	
	public String getData() {
		return data;
	}

	public String getOpis() {
		return opis;
	}

	public Event(String data, String opis)
	{
		this.data = data;
		this.opis = opis;
	}

}
