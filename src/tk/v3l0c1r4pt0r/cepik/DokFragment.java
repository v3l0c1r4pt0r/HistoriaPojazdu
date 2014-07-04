package tk.v3l0c1r4pt0r.cepik;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link DokFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link DokFragment#newInstance} factory method to
 * create an instance of this fragment.
 * 
 */
public class DokFragment extends Fragment {
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
	public static DokFragment newInstance(CarReport cr) {
		DokFragment fragment = new DokFragment();
		Bundle args = new Bundle();
		args.putSerializable(ARG_REPORT, cr);
		fragment.setArguments(args);
		return fragment;
	}

	public DokFragment() {
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
		return inflater.inflate(R.layout.fragment_dok, container, false);
	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

		TextView tv = null;
		TextView capTv = null;
		
		ViewChangeHelper vch = new ViewChangeHelper(getView());

		//data pierwszej rejestracji
		vch.ChangeEntryState(R.id.rejestracjaCap, R.id.rejestracjaVal, cr.getDataRejestracji(), true);

		//data wydania aktualnego dowodu
		vch.ChangeEntryState(R.id.dowodCap, R.id.dowodVal, cr.getDataWydaniaDowodu(), true);

		//data wydania karty pojazdu
		vch.ChangeEntryState(R.id.kartaCap, R.id.kartaVal, cr.getDataWydaniaKartyPojazdu(), true);
		
		//świadectwo homologacji
		if(cr.isHomologacja())
		{
			//kategoria
			vch.ChangeEntryState(R.id.kategoriaCap, R.id.kategoriaVal, cr.gethKategoria(), false);
			
			//wersja
			vch.ChangeEntryState(R.id.wersjaCap, R.id.wersjaVal, cr.gethWersja(), false);
			
			//numer
			vch.ChangeEntryState(R.id.numerCap, R.id.numerVal, cr.gethNumer(), false);
			tv = (TextView) getView().findViewById(R.id.numerVal);
			capTv = (TextView) getView().findViewById(R.id.numerCap);
			tv.setText(cr.gethNumer());
			tv.setVisibility(View.VISIBLE);
			capTv.setVisibility(View.VISIBLE);
			
			//wariant
			vch.ChangeEntryState(R.id.wariantCap, R.id.wariantVal, cr.gethWariant(), false);
		}
		else
		{
			((TextView) getView().findViewById(R.id.homoCap)).setVisibility(View.GONE);
			
			((TextView) getView().findViewById(R.id.kategoriaVal)).setVisibility(View.GONE);
			((TextView) getView().findViewById(R.id.kategoriaCap)).setVisibility(View.GONE);
			
			((TextView) getView().findViewById(R.id.wersjaVal)).setVisibility(View.GONE);
			((TextView) getView().findViewById(R.id.wersjaCap)).setVisibility(View.GONE);
			
			((TextView) getView().findViewById(R.id.numerVal)).setVisibility(View.GONE);
			((TextView) getView().findViewById(R.id.numerCap)).setVisibility(View.GONE);
			
			((TextView) getView().findViewById(R.id.wariantVal)).setVisibility(View.GONE);
			((TextView) getView().findViewById(R.id.wariantCap)).setVisibility(View.GONE);
		}
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
