package tk.v3l0c1r4pt0r.cepik;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class DataValidator {
	
	public static String validateDate(String date)
	{
		boolean notValid = true;
		while(notValid)
		{
			for(int i = 0; i < date.length(); i++)
			{
				char c = date.charAt(i);
				if(c >= '0' && c <= '9');
				else if(c == '.');
				else
				{
					date = date.replace(""+c, "");
					continue;
				}
			}
			notValid = false;
		}
		return date;
		
	}
	
	public static boolean validateDateFormat(String date)
	{
    	Pattern pattern = Pattern.compile("([0-9]{2})\\.([0-9]{2})\\.([0-9]{4})");
    	Matcher m = pattern.matcher(date);
    	if(m.matches())
    	{
	    	int dzien = Integer.parseInt(m.group(1));
	    	int miesiac = Integer.parseInt(m.group(2));
	    	int rok = Integer.parseInt(m.group(3));
	    	if(
	    			dzien > 0 && dzien < 32 &&
	    			miesiac > 0 && miesiac < 13 &&
	    			rok > 1800 && rok < 3000
	    	)
	    		return true;
	    	else
	    		return false;
    	}
    	else 
    		return false;
	}

}
