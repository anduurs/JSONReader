package arbetsprov;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

import arbetsprov.JSONReader.OccurenceFilter;
import arbetsprov.models.Person;

public class JSONReaderTest {
	
	private JSONReader jsonReader = new JSONReader("entries.json");
	
	@Test
	public void namesShouldBeInOrderOfOccurence()
	{
		Map<String, Integer> firstNamesSorted = jsonReader.getNamesInOrderOfOccurence("firstName", OccurenceFilter.NONE);
		List<Integer> firstNamesOccurencesList = new ArrayList<>();
		
		for(String name: firstNamesSorted.keySet())
		{
			firstNamesOccurencesList.add(firstNamesSorted.get(name));
		}
		
		Integer previous = null;
		boolean isSorted = true;
		
		for(Integer i : firstNamesOccurencesList)
		{
			if(previous != null && i.compareTo(previous) > 0) 
			{
				isSorted = false;
				break;
			}
			previous = i;
		}
		
		assertTrue(isSorted);
	}
	
	@Test
	public void namesShouldBeRemovedByFilter()
	{
		String filterName = "Svensson";
		List<Person> persons = jsonReader.getPersonsWithFilter(filterName);
		
		boolean filterWorked = true;
		
		for(Person person : persons)
		{
			if(!(person.getLastName().equals(filterName) || person.getFirstName().equals(filterName)))
			{
				filterWorked = false;
				break;
			}
		}
		
		assertTrue(filterWorked);
	}
	
	@Test
	public void valuesShouldIncrementIfKeyIsDuplicated()
	{
		Map<String, Integer> table = new HashMap<>();
		String key = "Leif";
		
		int tableSize = 10;
		
		for(int i = 0; i < tableSize; i++)
		{
			jsonReader.addToTable(table, key);
		}
		
		assertTrue(table.get(key) == tableSize);
	}
}
