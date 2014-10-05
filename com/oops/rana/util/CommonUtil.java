/**
 * 
 */
package com.oops.rana.util;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author parmodrana7
 *
 */
public final class CommonUtil implements Serializable {

	/**
	 * Constructor Private 
	 * Only single Instance Possible
	 */
	private CommonUtil(){
		
	}

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
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

	/**
	 * Check the String is Empty
	 * @param java.lang.String paramString  
	 * @return boolean
	 */
	public static boolean isEmpty(String paramString){
		return CommonUtil.isNull(paramString)||paramString.trim().length()==0;
	}
	
	/**
	 * Generate Random String
	 * @param  paramString String from the Random string created
	 * @param paramLength length of random string created
	 * @return random string containing character from above paramString of length paramLength
	 */
	public static String getRandomString(String paramString,int paramLength){
		if(paramLength<1){
			throw new IllegalArgumentException("paramLength must be positive");
		}
		StringBuilder stringBuilder=new StringBuilder(paramLength);
		Random random = new Random();
		for(int j=0;j<paramLength;j++){
			stringBuilder.append(paramString.charAt(random.nextInt(paramString.length())));
		}
		return stringBuilder.toString();		
	}
	
	/**
	 * Get random string. It contain characters
	 * @param paramLength length of random string created
	 * @return random string containing alpha character from A-Z and a-z of length paramLength
	 */
	public static String getRandomCharacterString(int paramLength){
		final String CHARACTER_STRING="abcdefghijklmnopqurstuvwxyzABCDEFGHIJKLMNOPQURSTUVWXYZ";
		return getRandomString(CHARACTER_STRING,paramLength);       
    }
	
	
	/**
	 * Get random string. It contain numbers
	 * @param paramLength length of random string created
	 * @return random string containing numeric character from 0-9 of length paramLength
	 */
	public static String getRandomNumericString(int paramLength){
		final String NUMERIC_STRING="0123456789";
		return getRandomString(NUMERIC_STRING,paramLength); 
    }
	
	
	/**
	 * Get random string. It contain numbers and character
	 * @param paramLength length of random string created
	 * @return random string containing alpha numeric character from A-Z and a-z and 0-9 of length paramLength
	 */
	public static String getRandomAlphaNumeric(int paramLength){
		final String ALPHA_NUMERIC_STRING="abcdefghijklmnopqurstuvwxyzABCDEFGHIJKLMNOPQURSTUVWXYZ012345789";
		return getRandomString(ALPHA_NUMERIC_STRING,paramLength);
	}
	
	
	/**
	 * 
	 * Using reflection get the Public Static Final field value
	 * @param className of class whose public static final field value is required
	 * @param fieldName public static final field name as string 
	 * @param type of return public static final field value.
	 * @return value of public static final fieldName
	 * @throws ClassNotFoundException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static <T> T getPublicStaticFinalValue(String className,String fieldName,Class<T> type)
			throws ClassNotFoundException, NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException, InstantiationException{
		return CommonUtil.getPublicStaticFinalValue(Class.forName(className), fieldName, type);
	}
	
	
	/**
	 * Using reflection get the Public Static Final field value
	 * @param clazz class whose public static final field value is required
	 * @param fieldName public static final field name as string 
	 * @param type of return public static final field value.
	 * @return value of public static final fieldName
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static <T> T getPublicStaticFinalValue(Class<?> clazz,String fieldName,Class<T> type)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException, InstantiationException{
			Field field=clazz.getField(fieldName);
			if(CommonUtil.isPublicStaticFinal(field)){
				return type.cast(field.get(null));
			}
			else{
				throw new IllegalAccessException("The field "+fieldName+" is not Public Static Final");
			}
	}
	
	
	/**
	 * Check the Field is Public Static Final
	 * @param java.lang.reflect.Field 
	 * @return boolean
	 */
	public static boolean isPublicStaticFinal(Field field) {
	    int modifiers = field.getModifiers();
	    return (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers));
	}
	
	
	/**
	 * Convert raw list to type safe List
	 * @param rawList List type is undefined
	 * @param type convert List type
	 * @return type safe List
	 * @throws ClassCastException
	 */
	public static <T> List<T> convertRawList(List<?> rawList,Class<T> type){
		if(CommonUtil.isNull(rawList)||CommonUtil.isNull(type)|| rawList.size()==0){
			return null;			
		}
		final List<T> paramList= new ArrayList<T>(rawList.size()); /** creating new List of size rawList **/ 
		for(final Object object:rawList){
		    try {
		        paramList.add(type.cast(object)); /** Type casting of object and adding to List **/
		    } catch(ClassCastException cce) {
		    	/** Exception Message **/
			}
		}
		return paramList;
	}
	
	
	/**
	 * Convert primitive array to java.util.List
	 * @param paramArrayT primitive array 
	 * @return raw java.util.List
	 * @throws IllegalArgumentException
	 */
	public static <T> List<? extends Object> primitiveAsList(T paramArrayT)throws IllegalArgumentException{
		if(!paramArrayT.getClass().isArray()){
			throw new IllegalArgumentException("Only primitive Type Array is supported");
		}
		
		if(paramArrayT.getClass().equals(byte[].class) ){
			byte[] byteArray= (byte[]) paramArrayT;
			int length=byteArray.length;
			List<Byte> byteList=new ArrayList<Byte>(length);
			for(int i=0;i<length;i++){
				byteList.add(byteArray[i]);
			}
			return byteList;
		}
		
		if(paramArrayT.getClass().equals(short[].class) ){
			short[] shortArray= (short[]) paramArrayT;
			int length=shortArray.length;
			List<Short> shortList=new ArrayList<Short>(length);
			for(int i=0;i<length;i++){
				shortList.add(shortArray[i]);
			}
			return shortList;
		}
		
		if(paramArrayT.getClass().equals(int[].class) ){
			int[] intArray= (int[]) paramArrayT;
			int length=intArray.length;
			List<Integer> intList=new ArrayList<Integer>(length);
			for(int i=0;i<length;i++){
				intList.add(intArray[i]);
			}
			return intList;
		}
		
		if(paramArrayT.getClass().equals(char[].class) ){
			char[] charArray= (char[]) paramArrayT;
			int length=charArray.length;
			List<Character> charList=new ArrayList<Character>(length);
			for(int i=0;i<length;i++){
				charList.add(charArray[i]);
			}
			return charList;
		}
		
		if(paramArrayT.getClass().equals(long[].class) ){
			long[] longArray= (long[]) paramArrayT;
			int length=longArray.length;
			List<Long> longList=new ArrayList<Long>(length);
			for(int i=0;i<length;i++){
				longList.add(longArray[i]);
			}
			return longList;
		}
		
		if(paramArrayT.getClass().equals(float[].class) ){
			float[] floatArray= (float[]) paramArrayT;
			int length=floatArray.length;
			List<Float> floatList=new ArrayList<Float>(length);
			for(int i=0;i<length;i++){
				floatList.add(floatArray[i]);
			}
			return floatList;
		}

		if(paramArrayT.getClass().equals(double[].class) ){
			double[] doubleArray= (double[]) paramArrayT;
			int length=doubleArray.length;
			List<Double> doubleList=new ArrayList<Double>(length);
			for(int i=0;i<length;i++){
				doubleList.add(doubleArray[i]);
			}
			return doubleList;
		}

		if(paramArrayT.getClass().equals(boolean[].class) ){
			boolean[] booleanArray= (boolean[]) paramArrayT;
			int length=booleanArray.length;
			List<Boolean> booleanList=new ArrayList<Boolean>(length);
			for(int i=0;i<length;i++){
				booleanList.add(booleanArray[i]);
			}
			return booleanList;
		}			
		throw new IllegalArgumentException("Only primitive Type Array is supported");
	}
	
	
	/**
	 * Shuffle the String content
	 * @param paramString passed string which content is to shuffle
	 * @return String which is shuffled
	 */
	public static String shuffle(String paramString){
		if(CommonUtil.isEmpty(paramString)){
			return paramString;
		}
		StringBuilder paramStringBuilder=new StringBuilder(paramString);
		StringBuilder paramSuffeledString=new StringBuilder();
		while(paramStringBuilder.length()!=0){
			int index = (int) Math.floor(Math.random() * paramStringBuilder.length());
			paramSuffeledString.append(paramStringBuilder.charAt(index));
			paramStringBuilder.deleteCharAt(index);
		}
		return paramSuffeledString.toString();
	}

	
	/**
	 * Get the field getter value
	 * @param className which field getter value is required
	 * @param fieldName is the field whose getter value is required
	 * @return Object 
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws ClassNotFoundException
	 */
	public static Object getGetterValue(String className, String fieldName)
			throws IllegalAccessException, IllegalArgumentException,InvocationTargetException, 
			InstantiationException, NoSuchMethodException, SecurityException, ClassNotFoundException{
		return CommonUtil.getGetterValue(Class.forName(className), fieldName);
	}


	/**
	 * Get the field getter value
	 * @param clazz which field getter value is required
	 * @param fieldName is the field whose getter value is required
	 * @return Object value of getter method
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public static Object getGetterValue(Class<?> clazz, String fieldName)
			throws IllegalAccessException, IllegalArgumentException,InvocationTargetException, 
			InstantiationException, NoSuchMethodException, SecurityException{
		if(CommonUtil.isEmpty(fieldName)){
			throw new NullPointerException("fieldName cannot be empty. Passed value is "+fieldName);
		}
		Method method=clazz.getMethod("get"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1));
		return method.invoke(clazz.newInstance());	
	}
	
	
	/**
	 * Using Reflection to invoke the methodName of the className 
	 * @param className which method need to invoke
	 * @param methodName is invoked 
	 * @param objects are the method parameters
	 * @return the method value. if void it return null
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
	public static Object invokeMethod(String className,String methodName,Object... objects)
			throws NoSuchMethodException,SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException,InstantiationException, ClassNotFoundException{
		return CommonUtil.invokeMethod(Class.forName(className), methodName, objects);
	}
	
	
	/**
	 * Using Reflection to invoke the methodName of the class
	 * @param clazz which method need to invoke
	 * @param methodName is invoked 
	 * @param objects are the method parameters
	 * @return the method value. if void it return null
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	public static Object invokeMethod(Class<?> clazz,String methodName,Object... objects)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, InstantiationException{
		if(CommonUtil.isEmpty(methodName)){
			throw new NullPointerException("fieldName cannot be empty. Passed value is "+methodName);
		}
		Method method;
		if(objects.length>0){ /** check passed parameter length **/
			method=clazz.getMethod(methodName,getMethodParametersType(clazz, methodName));
		}else{
			method=clazz.getMethod(methodName);
		}
		return method.invoke(clazz.newInstance(),objects);			
	}	


	/**
	 * Get the method parameter type array
	 * @param clazz whose method required
	 * @param methodName whose parameter required
	 * @return Array of parameter type 
	 * @throws NoSuchMethodException
	 */
	public static Class<?>[] getMethodParametersType(Class<?> clazz,String methodName)
			throws NoSuchMethodException{
		for(Method method:clazz.getMethods()){
			if(method.getName().equals(methodName)){
				return method.getParameterTypes();
			}
		}
		throw new NoSuchMethodException("No such method is paresent in "+clazz.getName()+" of name "+methodName);
	}
}
