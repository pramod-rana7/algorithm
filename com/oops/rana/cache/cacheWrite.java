/**
 * 
 */
package com.rana.cache;

import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;

/**
 * @author pramod_rana7
 *
 */
public class CacheWrite implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @throws FileNotFoundException 
	 * 
	 */
	public CacheWrite(File paramFile) throws FileNotFoundException {
		this.encode = new XMLEncoder(new FileOutputStream(paramFile));
		this.size=0;
	}
	
	public void write(Object paramObject){
		this.encode.writeObject(paramObject);
		this.encode.flush();
		this.size++;
	}
	
	/**
	 * close the cache writer when the writing finish
	 */
	public void close(){
		if(this.encode!=null){
			this.encode.flush();
			this.encode.close();
			this.encode=null;
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		this.close();
		super.finalize();
	}
	public int getSize(){
		return this.size;
	}
	
	/** encoding **/
	private XMLEncoder encode;
	
	/** Counting Number of input **/
	private int size;
}
