package com.beaverpurtennis.utils;

import java.util.Iterator;
import java.util.Map;

public class Validator {

	public static boolean validateField(Map<String,String> keyValuePair){
		Iterator<String> keys = keyValuePair.keySet().iterator();
		while (keys.hasNext()){
			String value = keyValuePair.get(keys.next());
			if (value == null || value.equals("")){
				return false;
			}
		}
		return true;
	}
}
