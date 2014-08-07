package tk.v3l0c1r4pt0r.cepik;

import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

/**
 * A fragment representing a list of Items.
 * <p />
 * Large screen devices (such as tablets) are supported by replacing the
 * ListView with a GridView.
 * <p />
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class HistoryFragment extends Fragment implements
		AbsListView.OnItemClickListener {

	private static final String ARG_HISTORY = "historyMap";

	private List<HistoryElement> mHistory;

	private OnFragmentInteractionListener mListener;

	/**
	 * The fragment's ListView/GridView.
	 */
	private AbsListView mListView;

	/**
	 * The Adapter which will be used to populate the ListView/GridView with
	 * Views.
	 */
	private ListAdapter mAdapter;

	public static HistoryFragment newInstance(Serializable historyMap) {
		HistoryFragment fragment = new HistoryFragment();
		Bundle args = new Bundle();
		args.putSerializable(ARG_HISTORY, historyMap);
		fragment.setArguments(args);
		return fragment;
	}

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public HistoryFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments() != null && getArguments().getSerializable(ARG_HISTORY) instanceof List<?>) {
			mHistory = (List<HistoryElement>) getArguments().getSerializable(ARG_HISTORY);
		}

		mAdapter = new ArrayAdapter<HistoryElement>(getActivity(),
				android.R.layout.simple_list_item_2, android.R.id.text1,
				mHistory) {
					  @Override
					  public View getView(int position, View convertView, ViewGroup parent) {
					    View view = super.getView(position, convertView, parent);
					    TextView text1 = (TextView) view.findViewById(android.R.id.text1);
					    TextView text2 = (TextView) view.findViewById(android.R.id.text2);

					    text1.setText(mHistory.get(position).getNrRej());
					    text2.setText(mHistory.get(position).getOpis());
					    return view;
					  }
				};
		notifyCollectionChanged();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_item, container, false);

		// Set the adapter
		mListView = (AbsListView) view.findViewById(android.R.id.list);
		((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

		// Set OnItemClickListener so we can be notified on item clicks
		mListView.setOnItemClickListener(this);

		return view;
	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
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
//		MenuItem history = (MenuItem) activity.findViewById(R.id.action_history);
//		history.setVisible(false);
	}

	@Override
	public void onDetach() {
//		MenuItem history = (MenuItem) getActivity().findViewById(R.id.action_history);
//		history.setVisible(true);
		super.onDetach();
		mListener = null;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (null != mListener) {
			// Notify the active callbacks interface (the activity, if the
			// fragment is attached to one) that an item has been selected.
			mListener
					.onFragmentInteraction(mHistory.get(position).id);
		}
	}

	/**
	 * The default content for this Fragment has a TextView that is shown when
	 * the list is empty. If you would like to change the text, call this method
	 * to supply the text it should use.
	 */
	public void setEmptyText(CharSequence emptyText) {
		View emptyView = mListView.getEmptyView();

		if (emptyText instanceof TextView) {
			((TextView) emptyView).setText(emptyText);
		}
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
		public void onFragmentInteraction(String id);
	}
	
	/**
	 * Notifies the adapter that the collection it uses has changed
	 */
	public void notifyCollectionChanged() {
		try
		{
			((ArrayAdapter<HistoryElement>) mAdapter).notifyDataSetChanged();
		}
		catch(ClassCastException e) {}
		catch(NullPointerException e) {}
	}

}
