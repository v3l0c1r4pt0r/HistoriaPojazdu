package tk.v3l0c1r4pt0r.cepik;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Locale;

import tk.v3l0c1r4pt0r.cepik.WebService.ReportNotGeneratedException;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class ResultActivity extends ActionBarActivity implements
		ActionBar.TabListener, 
		TechFragment.OnFragmentInteractionListener, 
		OgolneFragment.OnFragmentInteractionListener, 
		DokFragment.OnFragmentInteractionListener, 
		OsFragment.OnFragmentInteractionListener {
	
	private CarReport report = null;
	private WebService downloader = null;
	private ActionBar actionBar = null;
	private Activity thisActivity = null;

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_results);
		
		thisActivity = this;
		
		//pobierz wartość z poprzedniej aktywności
		Intent intent = getIntent();
		report = (CarReport) intent.getSerializableExtra(MainActivity.report);
		downloader = (WebService) intent.getSerializableExtra(MainActivity.downloader);

		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		this.actionBar = actionBar;
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		ActionBarUtils.setHasEmbeddedTabs(actionBar, false);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}
	
	@Override
	public void onConfigurationChanged (Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		ActionBarUtils.setHasEmbeddedTabs(actionBar, false);
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
		int id = item.getItemId();
		switch(id)
		{
		case R.id.action_about:
			ResultActivity.menuShowAbout(this);
			break;
		case R.id.action_download:
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						PdfDescriptor pdf = downloader.getReportPdf();
						File downloads = Environment.getExternalStoragePublicDirectory(
								Environment.DIRECTORY_DOWNLOADS
								);
						final String filePath = downloads.getAbsolutePath() + "/" + pdf.getName();
						File output = new File(filePath);
						FileOutputStream fos = new FileOutputStream(output);
						fos.write(pdf.getPdf());
						fos.close();
						//zapisałem, pokaż dymek ze ścieżką
						thisActivity.findViewById(R.id.pager).post(new Runnable() {
							
							@Override
							public void run() {
								Toast toast = Toast.makeText(
										thisActivity, 
										getString(R.string.downloadedMsg) + "\"" + filePath + "\"", 
										Toast.LENGTH_LONG
										);
								toast.show();
							}
						});
						//otwórz w domyślnej przeglądarce
						Intent intent = new Intent();
						intent.setAction(android.content.Intent.ACTION_VIEW);
						intent.setDataAndType(Uri.fromFile(output), "application/pdf");
						startActivity(intent);
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ReportNotGeneratedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
    
    public static void menuShowAbout(Activity act)
    {
		//create dialog with author's data
		AlertDialog.Builder builder = new AlertDialog.Builder(act);
		builder.setMessage(R.string.aboutContentMsg)
	       .setTitle(R.string.action_about);
		builder.setPositiveButton(string.ok, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//just close
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
    }

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			switch(position)
			{
			case 0:
				return OgolneFragment.newInstance(report);
			case 1:
				return TechFragment.newInstance(report);
			case 2:
				return DokFragment.newInstance(report);
			case 3:
				return OsFragment.newInstance("", "");
			default:
			return PlaceholderFragment.newInstance(position + 1);
			}
		}

		@Override
		public int getCount() {
			// Show 4 total pages.
			return 3;
			//FIXME: ustawiony w zależności od liczby kart
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_ogolne).toUpperCase(l);
			case 1:
				return getString(R.string.title_techniczne).toUpperCase(l);
			case 2:
				return getString(R.string.title_dokumenty).toUpperCase(l);
			case 3:
				return getString(R.string.title_os).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		/*@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_test, container,
					false);
			TextView textView = (TextView) rootView
					.findViewById(R.id.section_label);
			textView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return rootView;
		}*/
	}

	@Override
	public void onFragmentInteraction(Uri uri) {
		
	}

}
