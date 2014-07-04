package tk.v3l0c1r4pt0r.cepik;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link TechFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link TechFragment#newInstance} factory method
 * to create an instance of this fragment.
 * 
 */
public class TechFragment extends Fragment {
	private static final String ARG_REPORT = "report";

	private CarReport cr;

	private OnFragmentInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param cr
	 *            Car data class
	 * @return A new instance of fragment OgolneFragment.
	 */
	public static TechFragment newInstance(CarReport cr) {
		TechFragment fragment = new TechFragment();
		Bundle args = new Bundle();
		args.putSerializable(ARG_REPORT, cr);
		fragment.setArguments(args);
		return fragment;
	}

	public TechFragment() {
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
		return inflater.inflate(R.layout.fragment_tech, container, false);
	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
		
		ViewChangeHelper vch = new ViewChangeHelper(getView());

		//pojemnosc silnika
		vch.ChangeEntryState(R.id.pojemnoscCap, R.id.pojemnoscVal, cr.getPojemnoscSilnika(), true);

		//moc silnika
		vch.ChangeEntryState(R.id.mocCap, R.id.mocVal, cr.getMocSilnika(), true);
		//TODO: dodać moc w KM w nawiasie

		//emisja co2
		vch.ChangeEntryState(R.id.emisjaCap, R.id.emisjaVal, cr.getEmisjaCo2(), true);

		//srednie zuzycie
		vch.ChangeEntryState(R.id.zuzycieCap, R.id.zuzycieVal, cr.getZuzyciePaliwa(), true);

		//paliwo
		vch.ChangeEntryState(R.id.paliwoCap, R.id.paliwoVal, cr.getRodzajPaliwa(), true);

		//liczby miejsc
		vch.ChangeEntryState(R.id.miejscaCap, R.id.miejscaVal, cr.getLiczbaMiejsc(), true);
		vch.ChangeEntryState(R.id.siedzaceCap, R.id.siedzaceVal, cr.getLiczbaMiejscSiedzacych(), true);

		//masa własna
		vch.ChangeEntryState(R.id.masaCap, R.id.masaVal, cr.getMasaWlasna(), true);

		//masa z hamulcem
		vch.ChangeEntryState(R.id.przyczepaHamCap, R.id.przyczepaHamVal, cr.getMasaPrzyczepyZHamulcem(), true);

		//masa bez hamulca
		vch.ChangeEntryState(R.id.przyczepaBezCap, R.id.przyczepaBezVal, cr.getMasaPrzyczepyBezHamulca(), true);

		//DMC
		vch.ChangeEntryState(R.id.dmcCap, R.id.dmcVal, cr.getDmc(), true);

		//liczba osi
		vch.ChangeEntryState(R.id.osieCap, R.id.osieVal, cr.getLiczbaOsi(), true);

		//rozstaw osi
		vch.ChangeEntryState(R.id.rozstawOsiCap, R.id.rozstawOsiVal, cr.getRozstawOsi(), true);

		//rozstaw kół
		vch.ChangeEntryState(R.id.rozstawKolCap, R.id.rozstawKolVal, cr.getRozstawKol(), true);

		//nacisk na oś
		vch.ChangeEntryState(R.id.naciskNaOsCap, R.id.naciskNaOsVal, cr.getNaciskNaOs(), true);
    }

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
		public void onFragmentInteraction(Uri uri);
	}

}
