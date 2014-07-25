package tk.v3l0c1r4pt0r.cepik;

public class ColoredEvent extends Event {
	
	private int colorId = -1;

	public int getColorId() {
		return colorId;
	}

	public ColoredEvent(String data, String opis) {
		super(data, opis);
	}
	
	public ColoredEvent(String data, String opis, int colorId)
	{
		super(data, opis);
		this.colorId = colorId;
	}

}
