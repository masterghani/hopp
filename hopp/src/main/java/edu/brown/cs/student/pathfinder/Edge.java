package edu.brown.cs.student.pathfinder;

/**
 * Interface to enforce methods that all Edge classes need to implement.
 * @param <V> type implementing vertex interface
 * @param <E> type implementing edge interface
 *
 */
public interface Edge<V extends Vertex<V, E>, E extends Edge<V, E>> {

  /**
   * Getter for ID of the edge
   * @return String ID of the edge
   */
  String getID();


  /**
   * Getter for the source of the edge.
   *
   * @return The source of the edge.
   */
  V getSource();

  /**
   * Getter for the destination of the edge.
   *
   * @return The destination of the edge.
   */
  V getDestination();

  /**
   * Getter for the weight of the edge.
   *
   * @return The weight of the edge.
   */
  Double getWeight();

  void setWeight(Double weight);


}

