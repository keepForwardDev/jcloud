package com.jcloud.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class TypeUtil {




	public static Long toLon(Object obj) {
		if (obj == null || StringUtils.isBlank(obj.toString()))
			return null;
		if (obj instanceof Long) {
			return (Long) obj;
		} else if (obj instanceof BigInteger) {
			return ((BigInteger) obj).longValue();
		} else if (obj instanceof String) {
			return Long.valueOf(obj.toString());
		} else if (obj instanceof BigDecimal) {
			return Long.valueOf(obj.toString());
		} else if (obj instanceof Integer) {
			return Long.valueOf(obj.toString());
		} else if (obj instanceof Double) {
			Double d = (Double) obj;
			return d.longValue();
		}
		return null;
	}

	public static Float toFlo(Object obj) {
		if (obj == null || StringUtils.isBlank(obj.toString())) {
			return null;
		} else if (obj instanceof String) {
			return Float.valueOf(obj.toString());
		} else if (obj instanceof Float) {
			return (Float) obj;
		} else if (obj instanceof BigInteger) {
			return ((BigInteger) obj).floatValue();
		}
		return null;
	}

	public static Double toDou(Object obj) {
		if (obj == null || StringUtils.isBlank(obj.toString())) {
			return null;
		} else if (obj instanceof String) {
			return Double.valueOf(obj.toString());
		} else if (obj instanceof Double) {
			return (Double) obj;
		} else if (obj instanceof BigInteger) {
			return ((BigInteger) obj).doubleValue();
		} else if (obj instanceof BigDecimal) {
			return ((BigDecimal) obj).doubleValue();
		}
		return null;
	}

	public static String toStr(Object obj) {
		if (obj == null)
			return "";
		if (obj instanceof String) {
			return (String) obj;
		}
		return obj.toString();
	}

	public static Integer toInt(Object obj) {
		if (obj == null || StringUtils.isBlank(obj.toString())) {
			return null;
		} else if (obj instanceof String) {
			return Integer.valueOf(obj.toString());
		} else if (obj instanceof Integer) {
			return (Integer) obj;
		} else if (obj instanceof BigDecimal) {
			return ((BigDecimal) obj).intValue();
		} else if (obj instanceof BigInteger) {
			return ((BigInteger) obj).intValue();
		}
		return null;
	}

	public static Date toDate(Object obj) {
		if (obj == null)
			return null;
		if (obj instanceof Date) {
			return (Date) obj;
		}
		return null;
	}

	/**
	 * 类型转换，将不同的类型转为给的类型，
	 *
	 * @param type 转换的类型
	 * @param n    需要转换值
	 * @return
	 */
	public static Number castType(Number type, Number n) {
		if (n == null) {
			return null;
		} else if (type instanceof Long) {
			return n.longValue();
		} else if (type instanceof BigInteger) {
			return BigInteger.valueOf(n.longValue());
		} else if (type instanceof BigDecimal) {
			return BigDecimal.valueOf(n.longValue());
		} else if (type instanceof Integer) {
			return n.intValue();
		} else if (type instanceof Double) {
			return n.doubleValue();
		} else if (type instanceof Float) {
			return n.floatValue();
		}
		return null;
	}
}
