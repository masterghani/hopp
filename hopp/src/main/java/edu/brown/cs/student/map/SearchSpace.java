package edu.brown.cs.student.map;

import java.util.HashSet;
import java.util.Set;


public class SearchSpace {
	
	private String nodeToken;
	private NodePair nodePair;
	private Set<NodeDist> nodeDists;
	
	public SearchSpace(String nodeToken, NodePair nodePair) {
		this.nodeToken = nodeToken;
		this.nodePair = nodePair;
		this.nodeDists = new HashSet<NodeDist>();
	}
	
	public SearchSpace(String nodeToken, NodePair nodePair, Set<NodeDist> nodeDists) {
		this.nodeToken = nodeToken;
		this.nodePair = nodePair;
		this.nodeDists = nodeDists;
	}
	
	public String getNodeToken() {
		return this.nodeToken;
	}
	
	public void setNodeToken(String nodeToken) {
		this.nodeToken = nodeToken;
	}
	
	public NodePair getNodePair() {
		return this.nodePair;
	}
	
	public void setNodePair(NodePair nodePair) {
		this.nodePair = nodePair;
	}
	
	public Set<NodeDist> getSpace() {
		return this.nodeDists;
	}

	public void setSpace(Set<NodeDist> nodeDists) {
		this.nodeDists = nodeDists;
	}
	
	public void insertIntoSpace(NodeDist nodeDist) {
		this.nodeDists.add(nodeDist);	
	}
	
	public void removeFromSpace(NodeDist nodeDist) {
		this.nodeDists.remove(nodeDist);	
	}
	
	
	@Override
	public int hashCode() {
		  return (this.nodeToken.hashCode())+
				  (31*this.nodePair.getOrigin().hashCode())+
				  (17*this.nodePair.getOrigin().hashCode());

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
		    SearchSpace other = (SearchSpace) obj;
		    return (other.getNodePair().equals(this.nodePair) 
		    		&& other.getNodeToken().equals(this.nodeToken));
		  }
	

}
