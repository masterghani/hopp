package edu.brown.cs.student.heuristic;

import edu.brown.cs.student.pathfinder.Edge;
import edu.brown.cs.student.pathfinder.Vertex;

/**
 * Heuristic interface.
 *
 * @param <V>  type implementing vertex interface.
 * @param <E> type implementing edge interface.
 */
public interface Heuristic<V extends Vertex<V, E>, E extends Edge<V, E>> {
  /**
   * Method to get the heuristic distance between two verticies.
   * @param v1 first vertex.
   * @param v2 second vertex.
   * @return distance between two vertices.
   */
  double getDistance(V v1, V v2);
}

