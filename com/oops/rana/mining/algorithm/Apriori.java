package com.oops.rana.mining.algorithm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author parmodrana7
 * <h1> Implementation of Apriori  algorithm </h1>
 * <p> Apriori is used to mine all frequent itemsets from the source provided.</p>
 * <b> T is the type of Data provided </b>
 * 
 */
public class Apriori<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Test Data
	 * @return
	 */
	public static List<List<Integer>> getTestLists(){
    	List<List<Integer>> list=new ArrayList<List<Integer>>();
    	list.add(Arrays.asList(new Integer[]{1,2,5}));
    	list.add(Arrays.asList(new Integer[]{2,4}));
    	list.add(Arrays.asList(new Integer[]{2,4}));
    	list.add(Arrays.asList(new Integer[]{1,2,4}));
    	list.add(Arrays.asList(new Integer[]{1,3}));
    	list.add(Arrays.asList(new Integer[]{2,3}));
    	list.add(Arrays.asList(new Integer[]{1,3}));
    	list.add(Arrays.asList(new Integer[]{1,2,3,5}));
    	list.add(Arrays.asList(new Integer[]{1,2,3}));
    	return list;

	}
	
	/**
	 * <h2> Parameterize Constructor </h2>
	 * @param listOfObjectList
	 * @param minFrequency
	 * <p> Description: It take two parameter listOfObjectList and minFrequency </p>
	 */
	public Apriori(List<List<T>> listOfObjectList,int minFrequency) {
		this.listOfObject=new ArrayList<T>();
		this.primaryDataMap= new HashMap<Set<T>, AprioriData<T>>();
		this.candidateDataMap= new HashMap<Set<T>, AprioriData<T>>();
		this.listOfObjectList=listOfObjectList;
		this.minFrequency=minFrequency;
		this.allDataMap = new HashMap<Set<T>, AprioriData<T>>();
	}
	
	/**
	 * <h2>Get List of distinct Object of listofObjectList</h2>
	 * @return List<T>
	 */
	public List<T> getListDistinctOfObject(){
		/** check listOfObject is empty **/
		if(this.listOfObject==null||this.listOfObject.size()==0){
			/** true :  call procedure createDataMap() **/
			this.createDataMap();
		}
		return this.listOfObject;
	}
	
	/**
	 * <h2> Console output of allDataMap </h2>
	 */
	public void displayAllDataMap(){
		System.out.println("\nAll Data Map \n");
		this.display(this.allDataMap);
	}
	/**
	 * <h2> Console output of primaryDataMap </h2>
	 */
	public void displayPrimaryDataMap(){
		System.out.println("\nPrimary Data Map \n");
		this.display(this.primaryDataMap);
	}
	/**
	 * <h2> Console output of candidateDataMap </h2>
	 */
	public void displayCandidateDataMap(){
		System.out.println("\nCandidate Data Map \n");
		this.display(this.candidateDataMap);
	}
	
	/**
	 * <h2> To get AprioriData list from CandidateDataMap </h2>
	 * @return List of Object of AprioriData
	 */
	public  List<AprioriData<T>> getCandidateDataMap(){
		return this.getDataMap(this.candidateDataMap);
	}
	
	/**
	 * <h2> To get AprioriData list from primaryDataMap </h2>
	 * @return List of Object of AprioriData
	 */
	public  List<AprioriData<T>> getPrimaryDataMap(){
		return this.getDataMap(this.primaryDataMap);
	}
	
	/**
	 * <h2> To get AprioriData list form CandidateDataMap </h2>
	 * @return List of Object of AprioriData
	 */
	public  List<AprioriData<T>> getallDataMap(){
		return this.getDataMap(this.allDataMap);
	}

	/**
	 * Creation of Candidate Item Set of size paramInt
	 * paramInt must be grater than 1
	 * @param paramInt
	 */
	public void createCandidateSet(int paramInt){
		/** Check listOfObject is not empty **/
		if(this.listOfObjectList==null||this.listOfObjectList.size()==0){
			return;
		}
		/** Check primaryDataMap is not empty **/
		if(this.isMapEmpty(this.primaryDataMap)){
			/** true : execute procedure createDataMap() **/
			this.createDataMap();
		}
		
		Set<Set<T>> keySets;	/** Defining Set of key Set<T> **/
		
		if(paramInt<2){
			return;
		}else if(paramInt==2){		/** Check paramInt is equal to 2 **/
			/** generation of sets of Key of size 2 **/
			keySets=createSubSets(this.primaryDataMap.keySet(),2);
		}else{
			/** executing procedure for createCandidateSet for creation of previous subsets **/
			this.createCandidateSet(paramInt-1);
			/** generation of sets of Key of size paramInt **/
			keySets=createSubSets(this.candidateDataMap.keySet(), paramInt);
		}
		
		/** keySets is empty return**/
		if(keySets==null||keySets.size()==0){
			return;
		}
		
		/** Initializing candidateDataMap or removing previous content**/
		this.candidateDataMap=new HashMap<Set<T>, AprioriData<T>>();
		
		/** process starts for the creation of candidateDataMap **/
		
		/** getting localKeySetT from keySets **/
		for(Set<T> localKeySetT:keySets){		
			/** creating New object of  AprioriData<T> **/
			AprioriData<T> aprioriData=null;

			/** getting Object from localKeySetT **/
			for(T localT:localKeySetT){
				
				/** creating new localSet **/
				Set<T> localSet=new HashSet<T>();
				/** adding Object to localSet **/
				localSet.add(localT);
				/** checking aprioriData is null to initialize**/
				if(aprioriData==null){
					/** initializing aprioriData **/
					aprioriData=this.primaryDataMap.get(localSet);
				}else{
					/** creating new AprioriData object to match with previous **/
					AprioriData<T> localAprioriData=this.primaryDataMap.get(localSet);
					
					/** creating new Set of Integer to retain the value of previous AprioriData**/
					Set<Integer> intersection = new HashSet<Integer>(localAprioriData.getTransactionSet());
					/** get the intersect value of Set**/
					intersection.retainAll(aprioriData.getTransactionSet());
					/**
					if(intersection.size()<=this.minFrequency){
						break;
					}
					**/
					/** check for the minimum count itemSet **/
					if(this.primaryDataMap.get(localAprioriData.getItemSet()).getCount()>
							this.primaryDataMap.get(aprioriData.getItemSet()).getCount()){
						
						/** Assigning aprioriData  the updated value **/
						aprioriData=new AprioriData<T>(localSet, intersection,
								intersection.size(), aprioriData.getMin());
					}else{
						/** Assigning aprioriData  the updated value **/
						aprioriData=new AprioriData<T>(localSet, intersection,
								intersection.size(), localAprioriData.getMin());
					}
				}
			}

			/** Creating key for Map primaryDataMap **/
			Set<T> minValue=new HashSet<T>();
			/** adding the value which will form Key **/
			minValue.add(aprioriData.getMin());

			/** Assigning new value to aprioriData Object with update values **/
			aprioriData=new AprioriData<T>(localKeySetT, this.primaryDataMap.get(minValue).getTransactionSet(),
					aprioriData.getCount(), aprioriData.getMin());
			
			/** Adding the aprioriData with key to candidateDataMap **/
			this.candidateDataMap.put(localKeySetT, aprioriData);
		}
		/** process ends for creation of candidateDataMap **/
		
		/** removing values with frequency less than minFrequiency **/
		this.reduce(this.candidateDataMap);
		
		/** Adding the candidateDataMap to allDataMap **/
		this.allDataMap.putAll(this.candidateDataMap);
	}
	
	/**
	 * Creating subs
	 * @param parentSet
	 * @param order
	 * @return
	 */
	public static <T> Set<Set<T>> createSubSets(Set<Set<T>> parentSet,int oder){
		/** Get all the distinct Item from the Set of Set<T> **/
		Set<T> distinctElementSet=Apriori.getDistinctSet(parentSet);
		
		/** process start to generate all subset **/
		
		/** Initialize new subSets **/
		List<Set<T>> subSets = new ArrayList<Set<T>>();
		for(T addToSets:distinctElementSet) {
    	    
    		/** Initialize new newSets **/
    		List<Set<T>> newSets = new ArrayList<Set<T>>();
    		
    	    for(Set<T> curSet:subSets) {
    	    	/** Initialize new Set **/
    	    	Set<T> copyPlusNew = new HashSet<T>();
    	    	/** creating subsets **/
    	        copyPlusNew.addAll(curSet);
    	        copyPlusNew.add(addToSets);
    	        newSets.add(copyPlusNew);
    	    }
    	    Set<T> newValSet = new HashSet<T>();
    	    newValSet.add(addToSets);
    	    newSets.add(newValSet);
    	    subSets.addAll(newSets);
    	}

    	Set<Set<T>> localSet=new HashSet<Set<T>>();
		
    	/** filtering Subset for getting required order of subset **/
		for(Set<T> sets:subSets){
			if(sets.size()==oder){
				localSet.add(sets);
			}
		}
    	return localSet;
	}
	/**
	 * Get distinct object form the Set of Set<T>
	 * @param parmaSetOfSet
	 * @return Set<T>
	 */
	public static <T> Set<T> getDistinctSet(Set<Set<T>> parmaSetOfSet){
		Set<T> localSet=new HashSet<T>();
		for(Set<T> localSetT:parmaSetOfSet){
			for(T localT:localSetT){
				/** parse all the elements and try to add into set */
				localSet.add(localT);
			}
		}
		return localSet;
	}
	
	/**
	 * Remove the item which frequency is less than or equal to minFrequency
	 * @param dataMap
	 */
	private void reduce(Map<Set<T>, AprioriData<T>> dataMap){
		List<Set<T>> localKey=new ArrayList<Set<T>>();
		for(Set<T> key:dataMap.keySet()){
			/** check the frequency of Item is less than the minFrequency **/
			if(dataMap.get(key).getCount()<=this.minFrequency){
				/** creating List of Key which frequency is less **/
				localKey.add(key);
			}
		}
		for(Set<T> key:localKey){
			/** remove value form the dataMap whose key are below **/
			dataMap.remove(key);
		}
	}

	/**
	 * Console Display of Map<Set<T>,AprioriData<T>>
	 * @param paramMap
	 */
	private void display(Map<Set<T>,AprioriData<T>> paramMap){
		for(AprioriData<T> local:paramMap.values()){
			System.out.print("Item Set : "+local.getItemSet());
			System.out.print(", values :"+local.getCount());
			System.out.print(", itemSet:"+local.getTransactionSet());
			System.out.println(", min :"+local.getMin());
		}
		System.out.println("\n-------------------------------------------------\n");
	}
	/**
	 * <h2> Creation of DataMap to start the Process of Apriori </h2>
	 */
	private void createDataMap(){
		this.listOfObject=new ArrayList<T>();
		Set<T> localSet=new HashSet<T>();
		
		/** localTransactionID to give every item an internal Transaction ID**/
		int localTransactionID=0;
		
		for(List<T> localList:this.listOfObjectList){
			for(T localT:localList){
				Set<T> localSetT=new HashSet<T>();
				localSetT.add(localT);
				if(localSet.add(localT)){
					/** Adding data to primaryDataMap **/
					/** Creating New AprioriData */
					this.primaryDataMap.put(localSetT, new AprioriData<T>(localSetT,localTransactionID,localT));
				}else{
					/** add transaction Id and increment the count of AprioriData **/
					this.primaryDataMap.get(localSetT).incrementCount(localTransactionID);
				}
			}
			/** increment of transaction ID **/
			localTransactionID++;
		}
		
		/** removing  all the value less than minFrequiecy **/
		this.reduce(this.primaryDataMap);
		
		/** adding primaryDataMap to allDataMap **/
		this.allDataMap.putAll(this.primaryDataMap);
		
		/** List of Distinct Objects **/
		this.listOfObject.addAll(localSet);
	}
	/**
	 * Check the map is Empty
	 * @param paramMap
	 * @return boolean 
	 */
	private boolean isMapEmpty(Map<Set<T>,AprioriData<T>> paramMap){
		return paramMap==null||paramMap.size()==0;
	}

	/**
	 * Get List of AprioriData<T> 
	 * @param dataMap
	 * @return
	 */
	private List<AprioriData<T>> getDataMap(Map<Set<T>,AprioriData<T>> dataMap){
		if(this.isMapEmpty(dataMap)){
			return new ArrayList<AprioriData<T>>();
		}
		return new ArrayList<AprioriData<T>>(dataMap.values());	
	}

	private List<List<T>> listOfObjectList;
	private List<T> listOfObject;
	private Map<Set<T>, AprioriData<T>> primaryDataMap;
	private int minFrequency;
	private Map<Set<T>,AprioriData<T>> candidateDataMap;
	private Map<Set<T>,AprioriData<T>> allDataMap;
}
