package tk.v3l0c1r4pt0r.cepik;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.TableLayout;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link OsFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link OsFragment#newInstance} factory method to
 * create an instance of this fragment.
 * 
 */
public class OsFragment extends Fragment {
	private static final String ARG_REPORT = "report";

	private CarReport cr;

	private OnFragmentInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param cr
	 *            Car data class
	 * @return A new instance of fragment OsFragment.
	 */
	public static OsFragment newInstance(CarReport cr) {
		OsFragment fragment = new OsFragment();
		Bundle args = new Bundle();
		args.putSerializable(ARG_REPORT, cr);
		fragment.setArguments(args);
		return fragment;
	}

	public OsFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			cr = (CarReport) getArguments().getSerializable(ARG_REPORT);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_os, container, false);
	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
		
		ViewChangeHelper vch = new ViewChangeHelper(getView());

		//wlasciciele, wspolwlasciciele
		vch.ChangeEntryState(R.id.wlascicieleCap, R.id.wlascicieleVal, cr.getWlascicieleSum(), true);
		vch.ChangeEntryState(R.id.wspolwlascicieleCap, R.id.wspolwlascicieleVal, cr.getWspolwlascicieleSum(), true);
		vch.ChangeEntryState(R.id.wlascicieleAkCap, R.id.wlascicieleAkVal, cr.getWlascicieleAkt(), true);
		vch.ChangeEntryState(R.id.wspolwlascicieleAkCap, R.id.wspolwlascicieleAkVal, cr.getWspolwlascicieleAkt(), true);
		
		//województwo
		vch.ChangeEntryState(R.id.wojewodztwoCap, R.id.wojewodztwoVal, cr.getWojewodztwo(), true);
		
		//badanie i polisa
		vch.ChangeEntryState(R.id.ocFullCap, R.id.ocFullVal, cr.getPolisa(), true);
		vch.ChangeEntryState(R.id.przegladFullCap, R.id.przegladFullVal, cr.getBadanie(), true);
		
		//oś
		android.support.v4.app.FragmentManager manager = getFragmentManager();
		android.support.v4.app.FragmentTransaction trans = manager.beginTransaction();
		for(Event zdarzenie : cr.getZdarzenia())
		{
			Fragment event = AxisEntryFragment.newInstance(zdarzenie);
			Fragment border = AxisBorderFragment.newInstance();
			trans.add(R.id.axisTable, border);
			trans.add(R.id.axisTable, event);
		}
		trans.commit();
    }

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onFragmentInteraction(Uri uri);
	}

}
