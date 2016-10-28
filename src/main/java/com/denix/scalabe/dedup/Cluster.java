package com.denix.scalabe.dedup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cluster implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String owner;
	private List<String> relatives;
	public Cluster(String owner) {
		this.owner = owner;
		this.relatives = new ArrayList<String>();
		this.relatives.add(owner);
	}
	
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public List<String> getRelatives() {
		return relatives;
	}
	public void setRelatives(List<String> relatives) {
		this.relatives = relatives;
	}
}
