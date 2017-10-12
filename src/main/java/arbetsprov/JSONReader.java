package arbetsprov;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.json.JSONArray;

import arbetsprov.models.Person;

public class JSONReader 
{
	public enum OccurenceFilter{ODD, EVEN, NONE};
	
	private JSONArray jsonArray;
	
	public JSONReader(String fileName)
	{
		jsonArray = new JSONArray(readFile(fileName));
	}
	
	public void printAllEntries()
	{
		printNamesInOrderOfOccurences(OccurenceFilter.ODD);
		printNamesSorted(true);
		printNamesFiltered("Svensson");
	}
	
	public void printNamesInOrderOfOccurences(OccurenceFilter occurenceFilter)
	{
		Map<String, Integer> firstNames = getNamesInOrderOfOccurence("firstName", occurenceFilter);
		Map<String, Integer> lastNames  = getNamesInOrderOfOccurence("lastName", occurenceFilter);

		System.out.println("Most popular first names: ");
		
		for(String name: firstNames.keySet())
		{
			System.out.println(name + " " + firstNames.get(name));
		}
		
		System.out.println("\nMost popular last names: ");
		
		for(String name: lastNames.keySet())
		{
			System.out.println(name + " " + lastNames.get(name));
		}
	}
	
	public void printNamesSorted(boolean naturalOrder)
	{
		List<String> firstNames = new ArrayList<>();
		List<String> lastNames  = new ArrayList<>();
		
		for(int i = 0; i < jsonArray.length(); i++)
		{
			firstNames.add(jsonArray.getJSONObject(i).get("firstName").toString());
			lastNames.add(jsonArray.getJSONObject(i).get("lastName").toString());
		}
		
		Collections.sort(firstNames);
		Collections.sort(lastNames);
		
		if(!naturalOrder)
		{
			Collections.reverse(firstNames);
			Collections.reverse(lastNames);
		}
		
		System.out.println("\nFirst names sorted: ");

		for(String name : firstNames)
		{
			System.out.println(name);
		}
		
		System.out.println("\nLast names sorted: ");

		for(String name : lastNames)
		{
			System.out.println(name);
		}
	}
	
	public void printNamesFiltered(String filterName)
	{
		List<Person> persons = getPersonsWithFilter(filterName);
		
		System.out.println("\nNames after applying filter: ");

		for(Person person : persons)
		{
			System.out.println(person.toString());
		}
		
		if(persons.size() == 0)
		{
			System.out.println("No names found");
		}
	}
	
	public List<Person> getPersonsWithFilter(String filterName)
	{
		List<Person> persons = new ArrayList<>();
		
		for(int i = 0; i < jsonArray.length(); i++)
		{
			String firstName = jsonArray.getJSONObject(i).get("firstName").toString();
			String lastName  = jsonArray.getJSONObject(i).get("lastName").toString();
			
			if(filterName.equals(firstName) || filterName.equals(lastName))
			{
				persons.add(new Person(firstName, lastName));
			}
		}
		
		return persons;
	}
	
	public Map<String, Integer> getNamesInOrderOfOccurence(String nameType, OccurenceFilter filter)
	{
		Map<String, Integer> nameTable = new HashMap<>();
		
		for(int i = 0; i < jsonArray.length(); i++)
		{
			String firstName = jsonArray.getJSONObject(i).get(nameType).toString();
			addToTable(nameTable, firstName);
		}
		
		//Sorts the entries in the hashmap by their value
		Map<String, Integer> nameTableSorted = nameTable.entrySet().stream().
				sorted(Entry.<String, Integer>comparingByValue().reversed()).
				collect(Collectors.toMap(Entry::getKey, Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
		
		
		if(filter == OccurenceFilter.NONE)
		{
			return nameTableSorted;
		}
		
		Iterator<Map.Entry<String, Integer>> iterator = nameTableSorted.entrySet().iterator();
			
		while(iterator.hasNext())
		{
			Map.Entry<String, Integer> entry = iterator.next();
			boolean evenUp = entry.getValue() % 2 == 0;
				
			if((filter == OccurenceFilter.EVEN) ? !evenUp : evenUp)
			{
				iterator.remove();
			}
		}
		
		return nameTableSorted;
	}
	
	public void addToTable(Map<String, Integer> table, String key)
	{
		if(table.containsKey(key))
		{
			int count = table.get(key);
			table.put(key, ++count);
		}else
		{
			table.put(key, 1);
		}
	}
	
	private String readFile(String fileName)
	{
		StringBuilder jsonSource = new StringBuilder();
		
		try 
		{
			ClassLoader loader = getClass().getClassLoader();

			InputStream in = loader.getResourceAsStream(fileName);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			
			while((line = reader.readLine()) != null)
			{
				jsonSource.append(line).append('\n');
			}
				
			reader.close();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
			System.exit(0);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return jsonSource.toString();
	}
	
	public JSONArray getJSONArray()
	{
		return jsonArray;
	}
	
}
