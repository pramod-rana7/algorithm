/**
 * 
 */
package com.oops.rana.mining.algorithm;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author parmodrana7
 * <h1> AprioriData<T> </h1>
 * <p> AprioriData<T> is used Support Apriori data .</p>
 * <b> T is the type of Data provided </b>
 */
public class AprioriData<T> implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * ItemSet
	 * @return the typeObject
	 */
	public Set<T> getItemSet() {
		return this.itemSet;
	}

	/**
	 * Count of Itemset
	 * @return the count
	 */
	public int getCount() {
		synchronized (this) {
			return this.count;	
		}			
	}
	
	/**
	 * Item which have minimum Count
	 * @return the T
	 */
	public T getMin(){
		return this.min;
	}
	
	/**
	 * set of Transaction
	 * @return the Set<Integer>
	 */
	public Set<Integer> getTransactionSet(){
		return this.transactionSet;
	}
	
	/**
	 * Parameterize Constructor
	 * @param itemSet
	 * @param transectionID
	 * @param min
	 */
	public AprioriData(Set<T> itemSet,int transectionID,T min){
		this.itemSet=itemSet;
		this.transactionSet=new HashSet<Integer>();
		this.transactionSet.add(transectionID);
		this.min=min;
	}
	
	/**
	 * Parameterize Constructor
	 * @param itemSet
	 * @param transactionSet
	 * @param count
	 * @param min
	 */
	public AprioriData(Set<T> itemSet,Set<Integer> transactionSet,int count,T min){
		this.itemSet=itemSet;
		this.transactionSet=transactionSet;
		this.count=count;
		this.min=min;
	}
	/**
	 * increase the count by 1
	 * @param transectionID
	 */
	public void incrementCount(int transectionID){
		if(this.transactionSet.add(transectionID)){
			synchronized (this) {
				this.count++;
			}
		}
	}
	
	private Set<T> itemSet;
	private volatile int count=1;
	private T min;
	private Set<Integer> transactionSet;
}

