package edu.brown.cs.student.map;


public class NodeDist {
	
	private Node node;
	private Double dist;
	
	
	public NodeDist(Node node, Double dist) {
		this.node = node;
		this.dist = dist;
	}


	public Node getNode() {
		return this.node;
	}
	
	
	public void setNode(Node node) {
		this.node = node;
	}


	public Double getDist() {
		return this.dist;
	}
	
	public void setDist(Double dist) {
		this.dist = dist;
	}
	
	@Override
	public int hashCode() {
		return this.node.hashCode() + 31*dist.hashCode();

	}

	@Override 
	public boolean equals(Object obj) {
		    if (this == obj) {
		      return true;
		    }
		    if (obj == null) {
		      return false;
		    }
		    if (getClass() != obj.getClass()) {
		      return false;
		    }
		    NodeDist other = (NodeDist) obj;
		    return (other.getNode().equals(this.node) && 
		    		other.getDist().equals(this.dist));
		  }
	

}
