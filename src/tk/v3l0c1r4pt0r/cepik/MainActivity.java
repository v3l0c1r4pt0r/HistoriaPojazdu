package tk.v3l0c1r4pt0r.cepik;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import tk.v3l0c1r4pt0r.cepik.CarReport.EntryNotFoundException;
import tk.v3l0c1r4pt0r.cepik.CarReport.WrongCaptchaException;
import tk.v3l0c1r4pt0r.cepik.ResultActivity.PlaceholderFragment;
import tk.v3l0c1r4pt0r.cepik.ResultActivity.SectionsPagerAdapter;
import tk.v3l0c1r4pt0r.cepik.WebService.InvalidInputException;
import tk.v3l0c1r4pt0r.cepik.dummy.DummyContent;
import tk.v3l0c1r4pt0r.cepik.dummy.DummyContent.DummyItem;
import android.net.Uri;
import android.os.Bundle;
import android.R.string;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Configuration;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.internal.view.menu.ActionMenuItemView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements
	ActionBar.TabListener,
	InputFragment.OnFragmentInteractionListener,
	HistoryFragment.OnFragmentInteractionListener {

	public final static String report = "tk.v3l0c1r4pt0r.HistoriaPojazdu.report";
	public final static String downloader = "tk.v3l0c1r4pt0r.HistoriaPojazdu.downloader";
	
	private WebService cepik = null;
	private ActionBar actionBar = null;
	private Activity thisActivity = null;
	
	@SuppressLint("UseSparseArrays")
	private Map<Integer,Boolean> btnVisibility = new HashMap<Integer,Boolean>();


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
	
//	private final String[] values = new String[7];
	
	String cookieContent = "";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        btnVisibility.put(R.id.action_history, true);
        btnVisibility.put(R.id.action_search, false);

		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		this.actionBar = actionBar;
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
//		ActionBarUtils.setHasEmbeddedTabs(actionBar, false);

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
				.setOnPageChangeListener(new OnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						try
						{
							actionBar.setSelectedNavigationItem(position);
							onPageChanged(position);
						}
						catch(IllegalStateException e)
						{
							e.printStackTrace();
						}
					}

					@Override
					public void onPageScrollStateChanged(int arg0) {
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
						onPageChanged(arg0);
					}
					
					private void onPageChanged(int position)
					{
						switch(position)
						{
						case 0:
							btnVisibility.clear();
							btnVisibility.put(R.id.action_search, false);
							btnVisibility.put(R.id.action_history, true);
							break;
						case 1:
							btnVisibility.clear();
							btnVisibility.put(R.id.action_search, true);
							btnVisibility.put(R.id.action_history, false);
							break;
						default:
							btnVisibility.clear();
							btnVisibility.put(R.id.action_search, true);
							btnVisibility.put(R.id.action_history, true);
						}
						supportInvalidateOptionsMenu();
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setTabListener(this).setIcon(mSectionsPagerAdapter.getPageIcon(i)));
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
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        search.setVisible(btnVisibility.get(R.id.action_search));
        MenuItem history = menu.findItem(R.id.action_history);
        history.setVisible(btnVisibility.get(R.id.action_history));
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId())
    	{
    	case R.id.action_about:
    		//create dialog with author's data
			MainActivity.menuShowAbout(this);
    		break;
    	case R.id.action_help:
    		Intent intent = new Intent(this, HelpActivity.class);
    		startActivity(intent);
    		break;
    	case R.id.action_search:
    		mViewPager.setCurrentItem(actionBar.getTabAt(0).getPosition(),true);
    		break;
    	case R.id.action_history:
    		mViewPager.setCurrentItem(actionBar.getTabAt(1).getPosition(),true);
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
    
    public void sendRequest(final View view)
    {
    	final Intent intent = new Intent(this, ResultActivity.class);
    	
    	final Button btn = (Button) findViewById(R.id.sendBtn);
    	btn.setEnabled(false);
    	
    	final Activity thisActivity = this;

    	final String nrRejestracyjny = ((EditText)findViewById(R.id.rejVal)).getText().toString();
    	final String vin = ((EditText)findViewById(R.id.vinVal)).getText().toString();
    	final String dataRejestracji = ((EditText)findViewById(R.id.rejestracjaVal)).getText().toString();
    	final String captcha = ((EditText)findViewById(R.id.captchaVal)).getText().toString();
    	
    	//pobieramy w nowym wątku
    	new Thread(new Runnable()
		{
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
		    	CarReport rep = null;
		    	try {
					rep = cepik.getReport(nrRejestracyjny, vin, dataRejestracji, captcha);
				} catch (final IOException e) {
					View v = findViewById(R.id.scrollView1);
					final ImageView iv = (ImageView) findViewById(R.id.captchaImage);
					v.post(new Runnable() {
						
						@Override
						public void run() {
							iv.setImageResource(R.drawable.error);
							iv.setBackgroundColor(getResources().getColor(R.color.captchaErr));
							
							ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar);
							pb.setVisibility(ProgressBar.INVISIBLE);
							
							AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
							builder.setMessage(e.getLocalizedMessage())
						       .setTitle(R.string.errorMsg);
							builder.setPositiveButton(string.ok, new OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									//just close
								}
							});
							AlertDialog dialog = builder.create();
							dialog.show();
						}
					});
					return;
				} catch (final Exception e) {
					if(
							e instanceof EntryNotFoundException || 
							e instanceof WrongCaptchaException
							)
					{
						View v = findViewById(R.id.scrollView1);
						final ImageView iv = (ImageView) findViewById(R.id.captchaImage);
						v.post(new Runnable() {
							
							@Override
							public void run() {
								iv.setImageResource(R.drawable.loading);
								iv.setBackgroundColor(getResources().getColor(R.color.captchaErr));
						    	Button btn = (Button) findViewById(R.id.sendBtn);
						    	btn.setEnabled(true);
								ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar);
								pb.setVisibility(ProgressBar.INVISIBLE);
								if(e instanceof WrongCaptchaException)
								{
									Toast toast = Toast.makeText(
											thisActivity, 
											getString(R.string.wrongCaptchaMsg), 
											Toast.LENGTH_SHORT
											);
									toast.show();
								}
								else if(e instanceof EntryNotFoundException)
								{
									Toast toast = Toast.makeText(
											thisActivity, 
											getString(R.string.notFoundMsg), 
											Toast.LENGTH_SHORT
											);
									toast.show();
								}
								else 
								{
									Toast toast = Toast.makeText(
											thisActivity, 
											getString(R.string.error)+e.getClass().getName(), 
											Toast.LENGTH_SHORT
											);
									toast.show();
									e.printStackTrace();
								}
								reloadImage(view);
							}
						});
						return;
					}
					else if(e instanceof InvalidInputException)
					{
						int id = 0;	//check which version of textfield res to get
						if(android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.GINGERBREAD_MR1)
							id = R.drawable.textfield_wrong_holo_dark;
						else
							id = R.drawable.textfield_wrong;
						final int drawableId = id;
						switch(((InvalidInputException) e).field)
						{
						case Rej:
							final EditText rejVal = (EditText) findViewById(R.id.rejVal);
							rejVal.post(new Runnable() {
								
								@Override
								public void run() {
									rejVal.setBackgroundDrawable(
											getResources().getDrawable(drawableId)
											);
								}
							});
							break;
						case Vin:
							final EditText vinVal = (EditText) findViewById(R.id.vinVal);
							vinVal.post(new Runnable() {
								
								@Override
								public void run() {
									vinVal.setBackgroundDrawable(
											getResources().getDrawable(drawableId)
											);
								}
							});
							break;
						case Date:
							final EditText dateVal = (EditText) findViewById(R.id.rejestracjaVal);
							dateVal.post(new Runnable() {
								
								@Override
								public void run() {
									dateVal.setBackgroundDrawable(
											getResources().getDrawable(drawableId)
											);
								}
							});
							break;
						case Captcha:
							final EditText captchaVal = (EditText) findViewById(R.id.captchaVal);
							captchaVal.post(new Runnable() {
								
								@Override
								public void run() {
									captchaVal.setBackgroundDrawable(
											getResources().getDrawable(drawableId)
											);
								}
							});
							break;
						default:
							break;
						}
						final Button sendBtn = (Button) findViewById(R.id.sendBtn);
						sendBtn.post(new Runnable() {
							
							@Override
							public void run() {
								sendBtn.setEnabled(true);
							}
						});
						return;
					}
					else 
					{
						View v = findViewById(R.id.scrollView1);
						v.post(new Runnable() {
							
							@Override
							public void run() {
								Toast toast = Toast.makeText(
										thisActivity, 
										getString(R.string.error) + e.getClass().getName() +
										((e.getLocalizedMessage() != null && e.getLocalizedMessage().length() > 0) ? 
												getString(R.string.errorMore) + e.getLocalizedMessage() : ""), 
										Toast.LENGTH_SHORT
										);
								toast.show();
							}
						});
						e.printStackTrace();
						return;
					}
				}
		    	View v = (View) findViewById(R.id.scrollView1);
		    	final CarReport report = rep;
				v.post(new Runnable() {
					
					@Override
					public void run() {
				    	intent.putExtra(MainActivity.report, report);
				    	intent.putExtra(MainActivity.downloader, cepik);
				    	
				    	Button btn = (Button) findViewById(R.id.sendBtn);
				    	btn.setEnabled(true);
				    	
				    	try
				    	{
				    		startActivity(intent);
				    	}
				    	catch(RuntimeException e)
				    	{
							Toast toast = Toast.makeText(
									thisActivity, 
									getString(R.string.runtimeError) + e.getCause().getClass().getName() +
									((e.getLocalizedMessage() != null && e.getLocalizedMessage().length() > 0) ? 
											getString(R.string.errorMore) + e.getLocalizedMessage() : ""), 
									Toast.LENGTH_LONG
									);
							toast.show();
				    		e.printStackTrace();
				    	}
					}
				});
			}
		}).start();
    }
    
    public void reloadImage(View view)
    {
    	//ustawia tło na czarne
		final ImageView iv = (ImageView) findViewById(R.id.captchaImage);
		iv.setImageResource(R.drawable.loading);
		iv.setBackgroundColor(getResources().getColor(R.color.captchaErr));
		
		//ustaw progress na 0, wyłącz przycisk odświeżania
		final ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar);
    	
    	final Button sendBtn = (Button) findViewById(R.id.sendBtn);
    	sendBtn.setEnabled(false);
    	pb.setProgress(0);
    	pb.setVisibility(ProgressBar.VISIBLE);
    	
    	final Activity thisActivity = this;
    	
    	//pobieramy w nowym wątku
    	new Thread(new Runnable()
		{
			@Override
			public void run() {
				try {
					if(cepik==null)
					{
						cepik = new WebService(getApplicationContext());	//ustawia ciastko
					}
					
					final Bitmap captcha = cepik.getCaptcha();	//pobiera captchę
					final ImageView iv = (ImageView) findViewById(R.id.captchaImage);
					iv.post(new Runnable() {
						
						@Override
						public void run() {
					    	pb.setProgress(pb.getMax());
							iv.setImageBitmap(captcha);
							iv.setBackgroundColor(getResources().getColor(R.color.captchaBg));
//							refreshBtn.setEnabled(true);
					    	sendBtn.setEnabled(true);
					    	pb.setVisibility(ProgressBar.INVISIBLE);
					    	EditText captchaVal = (EditText) findViewById(R.id.captchaVal);
					    	captchaVal.setText("");
						}
					});
				} catch (final IOException e)
				{
					iv.post(new Runnable() {
						
						@Override
						public void run() {
							iv.setImageResource(R.drawable.error);
							iv.setBackgroundColor(getResources().getColor(R.color.captchaErr));
//							refreshBtn.setEnabled(true);
							ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar);
							pb.setVisibility(ProgressBar.INVISIBLE);
							Toast toast = Toast.makeText(
									thisActivity, 
									e.getLocalizedMessage(), 
									Toast.LENGTH_LONG
									);
							toast.show();
						}
					});
				} catch (final Exception e)
				{
					iv.post(new Runnable() {
						
						@Override
						public void run() {
							Toast toast = Toast.makeText(
									thisActivity, 
									getString(R.string.error) + e.getClass().getName() +
									(e.getLocalizedMessage() != null && e.getLocalizedMessage().length() > 0 ? 
											getString(R.string.errorMore) + e.getLocalizedMessage() : ""), 
									Toast.LENGTH_SHORT
									);
							toast.show();
						}
					});
					e.printStackTrace();
				}
			}
		}).start();
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
				return InputFragment.newInstance("","");
			case 1:
				return HistoryFragment.newInstance("","");
			default:
			return PlaceholderFragment.newInstance(position + 1);
			}
		}

		@Override
		public int getCount() {
			// Show 2 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_szukaj).toUpperCase(l);
			case 1:
				return getString(R.string.title_historia).toUpperCase(l);
			}
			return null;
		}

		public Drawable getPageIcon(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getResources().getDrawable(android.R.drawable.ic_menu_search);
			case 1:
				return getResources().getDrawable(android.R.drawable.ic_menu_recent_history);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFragmentInteraction(String id) {
		DummyItem item = DummyContent.ITEM_MAP.get(id);
		EditText rej = (EditText) findViewById(R.id.rejVal);
		EditText vin = (EditText) findViewById(R.id.vinVal);
		EditText dat = (EditText) findViewById(R.id.rejestracjaVal);
		rej.setText(item.getNrRej());
		actionBar.selectTab(actionBar.getTabAt(0));
		
	}
    
}
