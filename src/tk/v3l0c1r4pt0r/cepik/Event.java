package tk.v3l0c1r4pt0r.cepik;

import java.io.Serializable;

public class Event implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7035014300998246955L;
	
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
