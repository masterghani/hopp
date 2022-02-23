package edu.brown.cs.student.pathfinder;

import java.util.Set;


/**
 * Interface to enforce methods that all Vertex classes need to implement.
 * @param <V> type implementing vertex interface
 * @param <E> type implementing edge interface
 *
 */
public interface Vertex<V extends Vertex<V, E>, E extends Edge<V, E>> {

  /**
   * Getter for the vertex's neighbors.
   *
   * @return A set of edges, representing all the edge of neighbors to the
   *         vertex.
   */
  Set<E> getForwardNeighbors();

  Set<E> getBackwardNeighbors();

  /**
   * Getter for the ID of the vertex.
   *
   * @return A string, representing the ID of the vertex.
   */
  String getID();


  /**
   * Get edge that will be used in best path to get to this node
   * @return best edge
   */
  E getBestIncoming();

  /**
   * set edge that will be used in best path to get to this node
   * @return best edge
   */
  void setBestIncoming(E edge);

}