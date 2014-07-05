package tk.v3l0c1r4pt0r.cepik;

public class WebFile {
	
	private byte[] pdf;
	private String name;
	
	public byte[] getPdf() {
		return pdf;
	}

	public String getName() {
		return name;
	}
	
	public WebFile(String pdfName, byte[] pdf) {
		this.name = pdfName;
		this.pdf = pdf;
	}

}
