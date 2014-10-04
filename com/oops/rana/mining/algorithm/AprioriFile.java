/**
 * 
 */
package com.oops.rana.mining.algorithm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.oops.rana.cache.CacheRead;
import com.oops.rana.cache.CacheWrite;

/**
 * @author pramod_rana7
 * Use File system to store the data
 */
public class AprioriFile<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Parameterized Constructor 
	 * @param listOfObjectList
	 * @param minSupport
	 */
	public AprioriFile(List<List<T>> listOfObjectList,int minSupport) {
		this.listOfObjectList=listOfObjectList;
		this.minSupport=minSupport;
		this.backupMetaData=new HashMap<String, Integer>();
		this.cacheName="./cache/"+this.toString();
		this.primaryCacheFile=this.cacheName+"p.xml";
	}

	private void createPrimaryFrequentItemSet() throws FileNotFoundException{
		
		/** Get unique set **/
		Set<T> localSet=new HashSet<T>();
		
		Map<Set<T>,AprioriData<T>> primaryDataMap=new HashMap<Set<T>, AprioriData<T>>();
		
		/** localTransactionID to give every item an internal Transaction ID**/
		int localTransactionID=0;
		
		for(List<T> localList:this.listOfObjectList){
			for(T localT:localList){
				Set<T> localSetT=new HashSet<T>();
				localSetT.add(localT);
				if(localSet.add(localT)){
					/** Adding data to primaryDataMap **/
					/** Creating New AprioriData */
					primaryDataMap.put(localSetT, new AprioriData<T>(localSetT,localTransactionID,localT));
				}else{
					/** add transaction Id and increment the count of AprioriData **/
					primaryDataMap.get(localSetT).incrementCount(localTransactionID);
				}
			}
			/** increment of transaction ID **/
			localTransactionID++;
		}
		
		/** removing  all the value less than minFrequiecy **/
		this.reduce(primaryDataMap);
		CacheWrite cacheWriter=new CacheWrite(new File(this.primaryCacheFile));
		for(Set<T> key:primaryDataMap.keySet()){
			cacheWriter.write(primaryDataMap.get(key));
		}
		this.backupMetaData.put(this.primaryCacheFile,cacheWriter.getSize());
		cacheWriter.close();
	}

	/**
	 * Remove the item which frequency is less than or equal to minSupport
	 * @param dataMap
	 */
	private void reduce(Map<Set<T>, AprioriData<T>> dataMap){
		List<Set<T>> localKey=new ArrayList<Set<T>>();
		for(Set<T> key:dataMap.keySet()){
			/** check the frequency of Item is less than the minSupport **/
			if(dataMap.get(key).getCount()<=this.minSupport){
				/** creating List of Key which frequency is less **/
				localKey.add(key);
			}
		}
		for(Set<T> key:localKey){
			/** remove value form the dataMap whose key are below **/
			dataMap.remove(key);
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<AprioriData<T>> cacheReadAll(String fileName) throws FileNotFoundException{
		List<AprioriData<T>> aprioriDataList= new ArrayList<AprioriData<T>>();
		AprioriData<T> aprioriData=null;
		Integer size=this.backupMetaData.get(fileName);
		if(size!=null && size>0){
			CacheRead reader=new CacheRead(new File(fileName));
			for(int i=0;i<size;i++){
				aprioriData=(AprioriData<T>) reader.readObject();
				aprioriDataList.add(aprioriData);
			}	
		}
		return aprioriDataList;
	}
	
	/**
	 * Creation of Frequent Item Set of size paramInt
	 * paramInt must be grater than 1
	 * @param paramInt	
	 * @throws FileNotFoundException 
	 */
	public void createFrequentItemSet(int paramInt) throws FileNotFoundException{
		/** Check listOfObject is not empty **/
		if(this.listOfObjectList!=null||this.listOfObjectList.size()!=0){
			/** Check primary data exist **/
			if(!(new File(this.primaryCacheFile).exists())){
				/** true : execute procedure createDataMap() **/
				this.createPrimaryFrequentItemSet();
			}
			
			/** Defining Set of key Set<T> **/
			Set<Set<T>> keySets;
			
			/** local cache fileName **/			
			String localCacheName=this.cacheName+paramInt+".xml";
			
			if(paramInt<2){
				return;
			}else if(paramInt==2){		/** Check paramInt is equal to 2 **/
				/** generation of sets of Key of size 2 **/
				List<AprioriData<T>> listData=cacheReadAll(this.primaryCacheFile);
				Set<Set<T>> keys=new HashSet<Set<T>>();
				for(AprioriData<T> data:listData){
					keys.add(data.getItemSet());
				}
				keySets=createSubSets(keys,2);
			}else{
				/** executing procedure for createCandidateSet for creation of previous subsets **/
				this.createFrequentItemSet(paramInt-1);
				/** generation of sets of Key of size paramInt **/
				List<AprioriData<T>> listData=cacheReadAll(localCacheName);
				Set<Set<T>> keys=new HashSet<Set<T>>();
				for(AprioriData<T> data:listData){
					keys.add(data.getItemSet());
				}
				keySets=createSubSets(keys, paramInt);
			}
			
			/** keySets is empty return**/
			if(keySets==null||keySets.size()==0){
				return;
			}
			
			/** Initializing candidateDataMap **/
			Map<Set<T>, AprioriData<T>> candidateDataMap=new HashMap<Set<T>, AprioriData<T>>();
			
			/** process starts for the creation of candidateDataMap **/
			
			/** skipping the loop and taking next value**/
			outerLabe:
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
						aprioriData=this.cacheRead(primaryCacheFile,localSet);
					}else{
						/** creating new AprioriData object to match with previous **/
						AprioriData<T> localAprioriData=this.cacheRead(primaryCacheFile,localSet);
						
						/** creating new Set of Integer to retain the value of previous AprioriData**/
						Set<Integer> intersection = new HashSet<Integer>(localAprioriData.getTransactionSet());
						/** get the intersect value of Set**/
						intersection.retainAll(aprioriData.getTransactionSet());
						
						/** if intersection is less or equal to minFrequiecy skipping the set **/
						if(intersection.size()<=this.minSupport){
							continue outerLabe;
						}
						
						/** check for the minimum count itemSet **/
						if(localAprioriData.getCount()>this.cacheRead(this.primaryCacheFile,aprioriData.getItemSet()).getCount()){
							
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

				if(aprioriData==null){
					continue;
				}
				/** Creating key for Map primaryDataMap **/
				Set<T> minValue=new HashSet<T>();
				/** adding the value which will form Key **/
				minValue.add(aprioriData.getMin());

				/** Assigning new value to aprioriData Object with update values **/
				aprioriData=new AprioriData<T>(localKeySetT, this.cacheRead(this.primaryCacheFile,minValue).getTransactionSet(),
						aprioriData.getCount(), aprioriData.getMin());
				
				/** Adding the aprioriData with key to candidateDataMap **/
				candidateDataMap.put(localKeySetT, aprioriData);
			}
			/** removing values with frequency less than minFrequiency **/
			this.reduce(candidateDataMap);

			CacheWrite cacheWriter=new CacheWrite(new File(localCacheName));
			for(Set<T> key:candidateDataMap.keySet()){
				cacheWriter.write(candidateDataMap.get(key));
			}
			this.backupMetaData.put(localCacheName,cacheWriter.getSize());
			cacheWriter.close();
		
		}
		/** process ends for creation of candidateDataMap **/
		
	}
	
	@SuppressWarnings("unchecked")
	private AprioriData<T> cacheRead(String fileName,Set<T> key) throws FileNotFoundException{
		AprioriData<T> aprioriData=null;
		Integer size=this.backupMetaData.get(fileName);
		if(size!=null && size>0){
			CacheRead reader=new CacheRead(new File(fileName));
			for(int i=0;i<size;i++){
				aprioriData=(AprioriData<T>) reader.readObject();
				if(aprioriData.getItemSet().equals(key)){
					return aprioriData;
				}
			}	
		}
		return null;
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
	
	
	public List<AprioriData<T>> getItemList(int paramFrequency) throws FileNotFoundException{
		if(paramFrequency<1){
			return null;
		}else if(paramFrequency==1){
			return this.cacheReadAll(primaryCacheFile);
		}else{
			return this.cacheReadAll(this.cacheName+paramFrequency+".xml");
		}
		
	}
	
	/** List of data provided **/
	private List<List<T>> listOfObjectList;
	
	/** the value required to support the minimum count of object **/
	private int minSupport;
	
	private Map<String,Integer> backupMetaData;
	private String cacheName;
	private String primaryCacheFile;

}
