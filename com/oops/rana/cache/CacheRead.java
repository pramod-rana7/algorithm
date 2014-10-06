/**
 * 
 */
package com.rana.cache;

import java.beans.XMLDecoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;

/**
 * @author pramod_rana7
 * Read data from the xml file
 */
public class CacheRead implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Parametrize Constructor
	 * @param file in which object is written
	 * @throws FileNotFoundException 
	 */
	public CacheRead(File paramFile) throws FileNotFoundException{
		decode =new XMLDecoder(new FileInputStream(paramFile));
	}
	
	/**
	 * @return Object read from the file
	 */
	public Object readObject(){
		return this.decode.readObject();
	}
	
	/**
	 * Closing the CacheRead 
	 */
	public void close(){
		if(this.decode!=null){
			this.decode.close();
			this.decode=null;
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

	/** Reading the Object store in a file **/
	XMLDecoder decode;
}
