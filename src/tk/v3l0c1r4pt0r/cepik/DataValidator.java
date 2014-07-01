package tk.v3l0c1r4pt0r.cepik;

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
		//TODO: treść
		return false;
	}

}
