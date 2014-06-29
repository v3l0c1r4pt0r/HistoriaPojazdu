package tk.v3l0c1r4pt0r.cepik;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.R.string;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ResultsActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_results);
		
		Intent intent = getIntent();
		String[] extras = intent.getStringArrayExtra(MainActivity.resolvedData);
		if(extras.length >= 7)
		{
			TextView tv = (TextView)findViewById(R.id.nrKWTxtVal);
			tv.setText(extras[0]);
			tv = (TextView) findViewById(R.id.wlascicielTxtVal);
			tv.setText(extras[1]);
			tv = (TextView) findViewById(R.id.polozenieTxtVal);
			tv.setText(extras[2]);
			tv = (TextView) findViewById(R.id.dataZapisTxtVal);
			tv.setText(extras[3]);
			tv = (TextView) findViewById(R.id.dataZamkTxtVal);
			tv.setText(extras[4]);
			tv = (TextView) findViewById(R.id.typTxtVal);
			tv.setText(extras[5]);
			tv = (TextView) findViewById(R.id.wydzialTxtVal);
			tv.setText(extras[6]);
		}
		else throw new ArrayIndexOutOfBoundsException();
		
		//notify MainActivity's thread that values will never be used so it can delete them
//		extras.notifyAll();

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.results, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch(item.getItemId())
		{
		case R.id.action_about:
    		//create dialog with author's data
			MainActivity.menuShowAbout(this);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_results,
					container, false);
			return rootView;
		}
	}

}
