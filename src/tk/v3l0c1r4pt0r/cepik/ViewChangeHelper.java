package tk.v3l0c1r4pt0r.cepik;

import android.view.View;
import android.widget.TextView;

public class ViewChangeHelper {
	
	private View view = null;
	
	public ViewChangeHelper(View view)
	{
		this.view = view;
	}
	
	public void ChangeEntryState(int capId, int valId, String value, boolean hideOnEmpty)
	{
		TextView tv = (TextView) view.findViewById(valId);
		View capTv = (View) view.findViewById(capId);
		if(hideOnEmpty)
		{
			if(value.length()>0)
			{
				tv.setText(value);
				tv.setVisibility(View.VISIBLE);
				capTv.setVisibility(View.VISIBLE);
			}
			else
			{
				tv.setVisibility(View.GONE);
				capTv.setVisibility(View.GONE);
			}
		}
		else
		{
			tv.setText(value);
			tv.setVisibility(View.VISIBLE);
			capTv.setVisibility(View.VISIBLE);
		}
	}

}
