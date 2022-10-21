package com.uet.iot.util;

import java.util.List;

public class Util {
	public static boolean isNull(Object object) {
		return object == null;
	}
	
	public static boolean isNullOrEmpty(Object object) {
		if (object instanceof List) {
			List<?> lst = (List<?>) object;
			return lst.isEmpty();
		}
		return object == null || object == "" || object == "undefined";
	}
}