package classes;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class BagOfWords {

	HashMap<String, Integer> bow = new HashMap<String,Integer>();
	
	public int getId(String word){
		
		if(bow.containsKey(word))
			return bow.get(word);
		else{
			bow.put(word, bow.size());
			return bow.size()-1;
		}
		
	}
	
	/* ----------- DELEGATES FROM HASHMAP --------------*/
	
	public int size(){
		return bow.size();
	}

	public boolean equals(Object o) {
		return bow.equals(o);
	}

	public boolean isEmpty() {
		return bow.isEmpty();
	}

	public Integer get(Object key) {
		return bow.get(key);
	}

	public int hashCode() {
		return bow.hashCode();
	}

	public boolean containsKey(Object key) {
		return bow.containsKey(key);
	}

	public String toString() {
		return bow.toString();
	}

	public Integer put(String key, Integer value) {
		return bow.put(key, value);
	}

	public void putAll(Map<? extends String, ? extends Integer> m) {
		bow.putAll(m);
	}

	public Integer remove(Object key) {
		return bow.remove(key);
	}

	public void clear() {
		bow.clear();
	}

	public boolean containsValue(Object value) {
		return bow.containsValue(value);
	}

	public Object clone() {
		return bow.clone();
	}

	public Set<String> keySet() {
		return bow.keySet();
	}

	public Collection<Integer> values() {
		return bow.values();
	}

	public Set<Entry<String, Integer>> entrySet() {
		return bow.entrySet();
	}
	
	
	
	
	
}
