package tk.v3l0c1r4pt0r.cepik;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the {@link InputFragment.OnFragmentInteractionListener}
 * interface to handle interaction events. Use the
 * {@link InputFragment#newInstance} factory method to create an instance of
 * this fragment.
 *
 */
public class InputFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	
	private Drawable normalDateBg = null;
	private Drawable normalVinBg = null;
	private Drawable normalRejBg = null;
	private Drawable normalCaptchaBg = null;

	private OnFragmentInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 *
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment InputFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static InputFragment newInstance(String param1, String param2) {
		InputFragment fragment = new InputFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	public InputFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_input, container, false);
	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        
        MainActivity activity = (MainActivity) getActivity();
        
        //Add event listeners
        EditText dateVal = (EditText) activity.findViewById(R.id.rejestracjaVal);
        EditText vinVal = (EditText) activity.findViewById(R.id.vinVal);
        EditText rejVal = (EditText) activity.findViewById(R.id.rejVal);
        EditText captchaVal = (EditText) activity.findViewById(R.id.captchaVal);
        TextWatcher dateWatch = new MyTextWatcher();
        TextWatcher vinWatch = new MyTextWatcher();
        TextWatcher rejWatch = new MyTextWatcher();
        TextWatcher captchaWatch = new MyTextWatcher();
		dateVal.addTextChangedListener(dateWatch);
		vinVal.addTextChangedListener(vinWatch);
		rejVal.addTextChangedListener(rejWatch);
		captchaVal.addTextChangedListener(captchaWatch);

		final Button btn = (Button) activity.findViewById(R.id.sendBtn);
		activity.reloadImage(btn);
		normalDateBg = dateVal.getBackground();
		normalRejBg = rejVal.getBackground();
		normalVinBg = vinVal.getBackground();
		normalCaptchaBg = captchaVal.getBackground();
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
//		MenuItem search = (MenuItem) getActivity().findViewById(R.id.action_search);
//		search.setVisible(false);
	}

	@Override
	public void onDetach() {
//		MenuItem search = (MenuItem) getActivity().findViewById(R.id.action_search);
//		search.setVisible(true);
		super.onDetach();
		mListener = null;
	}
	
	class MyTextWatcher implements TextWatcher {
		
		/*EditText et = null;
		
		public MyTextWatcher(EditText et)
		{
			this.et = et;
		}*/
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}
		
		@SuppressWarnings("deprecation")
		@Override
		public void afterTextChanged(Editable s) {
			EditText date = (EditText) getActivity().findViewById(R.id.rejestracjaVal);
			
			String dateStr = date.getText().toString();
			
			String dateNewStr = DataValidator.validateDate(dateStr);
			int sel = date.getSelectionStart();
			if(dateStr != dateNewStr)
			{
				//zabezpieczenie przed nieskończoną pętlą
				date.setText(dateNewStr);
				try
				{
					date.setSelection(sel-1, sel-1);
				} catch (IndexOutOfBoundsException e)
				{
					//nie powinno się zdarzyć, ale jeśli się zdarzy to niech chociaż ktoś kto czyta logi wie
					e.printStackTrace();
				}
			}
			if(dateNewStr.length() == 10 && !DataValidator.validateDateFormat(dateNewStr))
			{
				//niepoprawny format daty
				int id = 0;	//check which version of textfield res to get
				if(android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.GINGERBREAD_MR1)
					id = R.drawable.textfield_wrong_holo_dark;
				else
					id = R.drawable.textfield_wrong;
				final int drawableId = id;
				date.setBackgroundDrawable(getResources().getDrawable(drawableId));
			}
			else
				date.setBackgroundDrawable(normalDateBg);
			EditText rej = (EditText) getActivity().findViewById(R.id.rejVal);
			EditText vin = (EditText) getActivity().findViewById(R.id.vinVal);
			EditText captcha = (EditText) getActivity().findViewById(R.id.captchaVal);
			rej.setBackgroundDrawable(normalRejBg);
			vin.setBackgroundDrawable(normalVinBg);
			captcha.setBackgroundDrawable(normalCaptchaBg);
			
		}
	};

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
