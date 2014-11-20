package graphModels;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import classes.Index;

public interface RandomWalk {


	Map<Integer,Double> getNodeScores(Set<Integer> nodes) throws IOException;
	void setIndex(Index index);

}
