package tk.v3l0c1r4pt0r.cepik.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

	/**
	 * An array of sample (dummy) items.
	 */
	public static List<DummyItem> ITEMS = new ArrayList<DummyItem>();

	/**
	 * A map of sample (dummy) items, by ID.
	 */
	public static Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

	static {
		// Add 3 sample items.
		addItem(new DummyItem("1", "WA12345","FSO Polonez"));
		addItem(new DummyItem("2", "WB98765","Fiat 126p"));
		addItem(new DummyItem("3", "WW9375W","VW Golf"));
	}

	private static void addItem(DummyItem item) {
		ITEMS.add(item);
		ITEM_MAP.put(item.id, item);
	}

	/**
	 * A dummy item representing a piece of content.
	 */
	public static class DummyItem {
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

		public DummyItem(String id, String content, String subcontent) {
			this.id = id;
			this.nrRej = content;
			this.opis = subcontent;
			this.vin = "";
			this.dataRej = "";
		}

		public DummyItem(String id, String rej, String opis, String vin, String data) {
			this.id = id;
			this.nrRej = rej;
			this.opis = opis;
			this.vin = vin;
			this.dataRej = data;
		}
	}
}
