package vectorModels;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

class MapDoubleValueComparator<K> implements Comparator<K> {

Map<K,Double> map;


public MapDoubleValueComparator(Map<Integer, Double> scores) {
	this.map = map;
}


@Override
public int compare(K o1, K o2) {
	return (map.get(o1).compareTo(map.get(o2)));
}



}