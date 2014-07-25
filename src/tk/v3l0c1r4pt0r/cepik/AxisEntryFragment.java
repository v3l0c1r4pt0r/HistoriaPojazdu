package tk.v3l0c1r4pt0r.cepik;

import android.R.menu;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the {@link AxisEntryFragment.OnFragmentInteractionListener}
 * interface to handle interaction events. Use the
 * {@link AxisEntryFragment#newInstance} factory method to create an instance of
 * this fragment.
 *
 */
public class AxisEntryFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_EVENT = "event";

	// TODO: Rename and change types of parameters
	private Event mEvent;

	private OnFragmentInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 *
	 * @param date
	 *            Parameter 1.
	 * @param description
	 *            Parameter 2.
	 * @return A new instance of fragment AxisEntryFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static AxisEntryFragment newInstance(Event event) {
		AxisEntryFragment fragment = new AxisEntryFragment();
		Bundle args = new Bundle();
		args.putSerializable(ARG_EVENT, event);
		fragment.setArguments(args);
		return fragment;
	}

	public AxisEntryFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mEvent = (Event) getArguments().getSerializable(ARG_EVENT);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_axis_entry, container, false);
	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
	{
        super.onActivityCreated(savedInstanceState);
		
		ViewChangeHelper vch = new ViewChangeHelper(getView());

		vch.ChangeEntryState(-1, R.id.elDataVal, Html.fromHtml(mEvent.getData()), false);
		vch.ChangeEntryState(-1, R.id.elOpisVal, Html.fromHtml(mEvent.getOpis()), false);
		
		if(mEvent instanceof ColoredEvent)
		{
			TextView opisCell = (TextView) getView().findViewById(R.id.elOpisVal);
			opisCell.setBackgroundColor(((ColoredEvent) mEvent).getColorId());
		}
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
