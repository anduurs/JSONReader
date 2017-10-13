package arbetsprov;

import arbetsprov.JSONReader.OccurenceFilter;

public class Main 
{
	public static void main(String[] args) 
	{
		JSONReader reader = new JSONReader("entries.json");
		
		if(args.length <= 0) return;
		
		if(args[0].equals("2"))
		{
			reader.printNamesInOrderOfOccurences(OccurenceFilter.NONE);
		}
		else if(args[0].equals("3"))
		{
			reader.printNamesSorted(true);
		}
		else if(args[0].equals("4"))
		{
			if(args[1].equals("true"))
			{
				reader.printNamesSorted(true);
			}else if(args[1].equals("false"))
			{
				reader.printNamesSorted(false);
			}
			else
			{
				System.out.println("Invalid command!");
				return;
			}
		}
		else if(args[0].equals("5"))
		{
			if(args[1] != "")
			{
				reader.printNamesFiltered(args[1]);
			}
		}
		else if(args[0].equals("6"))
		{
			if(args[1].equals("odd"))
			{
				reader.printNamesInOrderOfOccurences(OccurenceFilter.ODD);
			}
			else if(args[1].equals("even"))
			{
				reader.printNamesInOrderOfOccurences(OccurenceFilter.EVEN);
			}
			else
			{
				System.out.println("Invalid command!");
				return;
			}
		}
		else
		{
			System.out.println("Invalid command!");
			return;
		}
	}

}
