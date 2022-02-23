package edu.brown.cs.student.pathfinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import edu.brown.cs.student.heuristic.Heuristic;


public abstract class AbstractPathFinder<V extends Vertex<V, E>, 
E extends Edge<V, E>, H extends Heuristic<V, E>> {

  public static final class HeapEntry<V> implements Comparable<HeapEntry<V>> {

    private final V node;
    private final double distance; // The priority key.

    public HeapEntry(V node, double distance) {
      this.node = node;
      this.distance = distance;
    }

    public V getNode() {
      return node;
    }

    public double getDistance() {
      return distance;
    }

    @Override
    public int compareTo(HeapEntry<V> o) {
      return Double.compare(this.distance, o.distance);
    }
  }

  protected final H heuristic;

  protected AbstractPathFinder() {
	  this.heuristic = null;
  }


  public abstract List<V> search(V sourceNode, V targetNode);


  protected List<V> tracebackPath(V touchNode, 
      Map<V, V> parentsA,
      Map<V, V> parentsB) {
    List<V> path = new ArrayList<>();
    V currentNode = touchNode;

    while (currentNode != null) {
      path.add(currentNode);
      currentNode = parentsA.get(currentNode);
    }

    Collections.reverse(path);

    if (parentsB != null) {
      currentNode = parentsB.get(touchNode);

      while (currentNode != null) {
        path.add(currentNode);
        currentNode = parentsB.get(currentNode);
      }
    }

    return path;
  }


  protected List<V> tracebackPath(V targetNode, 
      Map<V, V> parents) {
    return tracebackPath(targetNode, parents, null);
  }
}
