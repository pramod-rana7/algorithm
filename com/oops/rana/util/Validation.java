/**
 * 
 */
package com.oops.rana.util;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author parmodrana7
 *
 */
public class Validation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private Validation() {
	}
	
	/**
	 * Check the Object is Null
	 * @param  paramT generic type
	 * @return boolean
	 */
	public static <T> boolean isNull(T paramT){
		return paramT==null||paramT.equals(null);
	}
	
	/**
	 * Check the Object is not Null
	 * @param  paramT generic type
	 * @return boolean
	 */
	public static <T> boolean isNotNull(T paramT){
		return paramT!=null && !paramT.equals(null);
	}
	
	public static boolean isValidLength(String paramString,int length){
		if(Validation.isNull(paramString)){
			return false;
		}
		return paramString.length()==length;
	}
	
	public static boolean isValidLength(long paramLong,int length){
		return isValidLength(String.valueOf(paramLong),length);
	}
	
	public static boolean matches(String regex,String paramString){
		if(Validation.isNull(regex) || Validation.isNull(paramString)){
			return false;
		}
		try{
			Pattern pattern=Pattern.compile(regex);
			Matcher matcher=pattern.matcher(paramString);
			return matcher.matches();			
		}catch(PatternSyntaxException e){
			return false;
		}
	}
	
	public static final String DIGIT_REGEX="[\\d]"; 			/** Any digit, short for [0-9] **/
	public static final String NON_DIGIT_REGEX="[\\D]";			/** A non-digit, short for [^0-9] **/ 
	public static final String WHITESPACE_REGEX="[\\s]";		/** A whitespace character, short for [ \t\n\x0b\r\f] **/
	public static final String NON_WHITESPACE_REGEX="[\\S]";	/** A non-whitespace character, short for [^\s] **/
}
