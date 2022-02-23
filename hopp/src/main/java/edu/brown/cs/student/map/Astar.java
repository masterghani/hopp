package edu.brown.cs.student.map;

import edu.brown.cs.student.pathfinder.AbstractPathFinder.HeapEntry;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;




/**
 * Class to execute the Dijkstra algorithm.
 * @param <E> type implementing edge interface
 * @param <V> type implementing vertex interface
 * @param <H> type implementing heuristic interface
 *
 */
public class Astar {

  private Haversine heuristic = null;
  private final PriorityQueue<HeapEntry<Node>> openA = new PriorityQueue<>();
  private final PriorityQueue<HeapEntry<Node>> openB = new PriorityQueue<>();
  private final Map<Node, Node> parentsA = new HashMap<>();
  private final Map<Node,Node> parentsB = new HashMap<>();
  private final Map<Node, Double> distanceA = new HashMap<>();
  private final Map<Node, Double> distanceB = new HashMap<>();
  private final Set<Node> closed = new HashSet<>();


  private double fA;
  private double fB;
  private double bestPathLength;
  private Node meetNode = null;
  private Node sourceNode;
  private Node targetNode;


  /**
   * Default constructor with given heuristic.
   * @param heuristic a Heuristic.
   */
  public Astar(Haversine heuristic) {
	  
	  this.heuristic = heuristic;
  }
  
  
  //older code to look to search spaces for debugging purposes (not called)
  
  public SearchSpace getSearchspaceSource(Node sourceNode, Node targetNode) {
	  if (this.distanceA.size()==1) {
		  search(sourceNode, targetNode);
	  }
	  Set<NodeDist> forwardNodeDists = new HashSet<>();
	  for (Map.Entry<Node,Double> entry : this.distanceA.entrySet()) {
		  forwardNodeDists.add(new NodeDist(entry.getKey(), entry.getValue()));
	  }
	  
	  return new SearchSpace(sourceNode.getID(), new NodePair(sourceNode.getID(), 
	      targetNode.getID()), forwardNodeDists);
	  
  }
  
  public SearchSpace getSearchspaceTarget(Node sourceNode, Node targetNode) {
	  if (this.distanceB.size()==1) {
		  System.out.println("search space target");
		  search(sourceNode, targetNode);
	  }
	  
	  System.out.println(this.distanceB.size());
	  
	  Set<NodeDist> backwardNodeDists = new HashSet<>();
	  for (Map.Entry<Node,Double> entry : this.distanceB.entrySet()) {
		  backwardNodeDists.add(new NodeDist(entry.getKey(), entry.getValue()));
	  }
	  
	  System.out.println(backwardNodeDists.size());
	  
	  
	  return new SearchSpace(targetNode.getID(), new NodePair(sourceNode.getID(), 
          targetNode.getID()), backwardNodeDists);
  }
  
  public Double getShortestDistance(Node sourceNode, Node targetNode) {
	  if (sourceNode.equals(targetNode)) {
		  return 0.0;
	  }
	  
	  
	  if (this.meetNode==null) {
		  
		  search(sourceNode, targetNode);

		  if (this.meetNode==null) {
			  return -1.0;
			  //it is only still null if there is no path between the nodes  
		  }
		  
	  }
	  
	  return this.distanceA.get(this.meetNode) + this.distanceB.get(this.meetNode);
	  
  }
  


  public void search(Node sourceNode, Node targetNode) {
	System.out.println("starting search");
	clear(sourceNode, targetNode);

    if (sourceNode.equals(targetNode)) {
    	return;
    }
    
    while (!this.openA.isEmpty() && !this.openB.isEmpty()) {
      if (this.openA.size() <= this.openB.size()) {
    	  
    	  System.out.println(this.openA.size());
        expandForward();
      } else {
    	  System.out.println(this.openB.size());
    	  expandBackward();
      }
    }
    
    System.out.println("finished search");
  }


  private void expandForward() {
    Node currentNode = this.openA.remove().getNode();
    
    
    if (this.closed.contains(currentNode)) {
      return;
    }
    this.closed.add(currentNode);

    //rejection criteria
    if ((this.distanceA.get(currentNode) + 
        this.heuristic.getDistance(currentNode, this.targetNode)) >= 
        this.bestPathLength ||
        (this.distanceA.get(currentNode) + 
            this.fB - this.heuristic.getDistance(currentNode, this.sourceNode)) >= 
            this.bestPathLength) {
    } else {

      for (Way childEdge : currentNode.getForwardNeighbors()) {
        Node childNode = childEdge.getDestination();
        
        if (this.closed.contains(childNode)) {
          continue;
        }


        double temp = this.distanceA.get(currentNode) + childEdge.getWeight();

        if (!this.distanceA.containsKey(childNode) || 
            this.distanceA.get(childNode) > temp) {

          childNode.setBestIncoming(childEdge);

          this.distanceA.put(childNode, temp);
          this.parentsA.put(childNode, currentNode);



          HeapEntry<Node> e = new HeapEntry<Node>(childNode,
              temp + this.heuristic.getDistance(childNode, this.targetNode));

          this.openA.add(e);

          if (this.distanceB.containsKey(childNode)) {
        	  
        	  
            double pathLength = temp + this.distanceB.get(childNode);
            
            
            if (this.bestPathLength > pathLength) {
              this.bestPathLength = pathLength;
              this.meetNode = childNode;
            }
          }

        }
      }	
    }
    if (!this.openA.isEmpty()) {
      this.fA = this.openA.peek().getDistance();
    }
  }

  private void expandBackward() {
	  
	  
    Node currentNode = this.openB.remove().getNode();
    
    
    if (this.closed.contains(currentNode)) {
      return;
    }

    this.closed.add(currentNode);
    //rejection criteria

    if (this.distanceB.get(currentNode) + 
        this.heuristic.getDistance(currentNode, this.sourceNode) 
    >= this.bestPathLength || (this.distanceB.get(currentNode) + this.fA -
    this.heuristic.getDistance(currentNode, this.targetNode)) >= this.bestPathLength) {
    	

    } else {
      for (Way parentEdge : currentNode.getBackwardNeighbors()) {
        Node parentNode = parentEdge.getSource();
        
        
        if (this.closed.contains(parentNode)) {
          continue;
        }

        double temp = this.distanceB.get(currentNode) + 
            parentEdge.getWeight();
        

        if (!this.distanceB.containsKey(parentNode) || 
            this.distanceB.get(parentNode) > temp) {
        	

          currentNode.setBestIncoming(parentEdge);
          this.distanceB.put(parentNode, temp);
          this.parentsB.put(parentNode, currentNode);

          HeapEntry<Node> e = new HeapEntry<Node>(parentNode,
              temp + this.heuristic.getDistance(parentNode, this.sourceNode));

          this.openB.add(e);

          if (this.distanceA.containsKey(parentNode)) {
            double pathLength = temp + this.distanceA.get(parentNode);
            if (this.bestPathLength > pathLength) {
            	
            	
              this.bestPathLength = pathLength;
              this.meetNode = parentNode;
            }
          }
        }
      }
    }

    if (!this.openB.isEmpty()) {
      this.fB = this.openB.peek().getDistance();
    }
  }

  

  public void clear(Node sourceNode, Node targetNode) {
    this.openA.clear();
    this.openB.clear();
    this.parentsA.clear();
    this.parentsB.clear();
    this.distanceA.clear();
    this.distanceB.clear();
    this.closed.clear();

    double totalDist = this.heuristic.getDistance(sourceNode, targetNode);
    
    
    this.fA = totalDist;
    this.fB = totalDist;
    this.bestPathLength = Double.MAX_VALUE;
    this.meetNode = null;
    this.sourceNode = sourceNode;
    this.targetNode = targetNode;
    
    
    this.openA.add(new HeapEntry<Node>(this.sourceNode, this.fA));
    this.openB.add(new HeapEntry<Node>(this.targetNode, this.fB));
    this.parentsA.put(this.sourceNode,null);
    this.parentsB.put(this.targetNode,null);
    
    
    this.distanceA.put(this.sourceNode, 0.0);
    this.distanceB.put(this.targetNode, 0.0);

  }
  
  
  
  

}

