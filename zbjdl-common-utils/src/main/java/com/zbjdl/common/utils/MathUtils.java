package com.zbjdl.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MathUtils {
	private static final int DEFAULT_DIV_SCALE = 20;

	private static final int BITS = 2;

	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));

		BigDecimal b2 = new BigDecimal(Double.toString(v2));

		return b1.add(b2).doubleValue();
	}

	public static String add(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);

		BigDecimal b2 = new BigDecimal(v2);

		return b1.add(b2).toString();
	}

	public static double subtract(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));

		BigDecimal b2 = new BigDecimal(Double.toString(v2));

		return b1.subtract(b2).doubleValue();
	}

	public static String subtract(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);

		BigDecimal b2 = new BigDecimal(v2);

		return b1.subtract(b2).toString();
	}

	public static double multiply(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));

		BigDecimal b2 = new BigDecimal(Double.toString(v2));

		return b1.multiply(b2).doubleValue();
	}

	public static String multiply(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);

		BigDecimal b2 = new BigDecimal(v2);

		return b1.multiply(b2).toString();
	}

	public static double divide(double v1, double v2) {
		return divide(v1, v2, DEFAULT_DIV_SCALE);
	}

	public static double divide(double v1, double v2, int scale) {
		return divide(v1, v2, scale, RoundingMode.HALF_UP);
	}

	public static double divide(double v1, double v2, int scale, RoundingMode round_mode) {
		if (v2 == 0.0D)
			return 0.0D;
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}

		BigDecimal b1 = new BigDecimal(Double.toString(v1));

		BigDecimal b2 = new BigDecimal(Double.toString(v2));

		return b1.divide(b2, scale, round_mode).doubleValue();
	}

	public static String divide(String v1, String v2) {
		return divide(v1, v2, DEFAULT_DIV_SCALE);
	}

	public static String divide(String v1, String v2, int scale) {
		return divide(v1, v2, DEFAULT_DIV_SCALE, RoundingMode.HALF_UP);
	}

	public static String divide(String v1, String v2, int scale, RoundingMode round_mode) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}

		BigDecimal b1 = new BigDecimal(v1);

		BigDecimal b2 = new BigDecimal(v2);

		return b1.divide(b2, scale, round_mode).toString();
	}

	public static BigDecimal round(BigDecimal v) {
		return round(v, MathUtils.BITS);
	}

	public static BigDecimal round(BigDecimal v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		return round(v, scale, RoundingMode.HALF_UP);
	}

	public static BigDecimal round(BigDecimal v, int scale, RoundingMode round_mode) {
		BigDecimal result = v.setScale(scale, round_mode);
		return result;
	}

	public static double round(double v, int scale) {
		return round(v, scale, RoundingMode.HALF_UP);
	}

	public static double round(double v, int scale, RoundingMode round_mode) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}

		BigDecimal b = new BigDecimal(Double.toString(v));

		return b.setScale(scale, round_mode).doubleValue();
	}

	public static String round(String v, int scale) {
		return round(v, scale, RoundingMode.HALF_UP);
	}

	public static String round(String v, int scale, RoundingMode round_mode) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}

		BigDecimal b = new BigDecimal(v);

		return b.setScale(scale, round_mode).toString();
	}

	public static Double add(Double a, Double b) {
		if ((a == null) && (b == null))
			return null;
		double x = (a == null) ? 0.0D : a.doubleValue();
		double y = (b == null) ? 0.0D : b.doubleValue();
		return new Double(add(x, y));
	}

	public static Double subtract(Double a, Double b) {
		if ((a == null) && (b == null))
			return null;
		double x = (a == null) ? 0.0D : a.doubleValue();
		double y = (b == null) ? 0.0D : b.doubleValue();
		return new Double(subtract(x, y));
	}

	public static boolean isPlus(Double d) {
		if (d == null)
			return false;
		return (d.doubleValue() > 0.0D);
	}

	public static Double multiply(Double a, Double b) {
		if ((a == null) && (b == null))
			return null;
		double x = (a == null) ? 0.0D : a.doubleValue();
		double y = (b == null) ? 0.0D : b.doubleValue();
		return new Double(multiply(x, y));
	}

	public static Double divide(Double a, Double b) {
		if ((a == null) && (b == null))
			return new Double(0.0D);
		double x = (a == null) ? 0.0D : a.doubleValue();
		double y = (b == null) ? 0.0D : b.doubleValue();
		if ((x == 0.0D) || (y == 0.0D))
			return new Double(0.0D);
		return new Double(divide(x, y));
	}

	public static Double sum(Double[] values) {
		Double result = new Double(0.0D);
		for (int i = 0; i < values.length; ++i) {
			result = add(result, values[i]);
		}
		return result;
	}

	public static Integer add(Integer a, Integer b) {
		int x = (a == null) ? 0 : a.intValue();
		int y = (b == null) ? 0 : b.intValue();
		return new Integer(x + y);
	}

	public static Double reverse(Double a) {
		return subtract(new Double(0.0D), a);
	}

	public static String formateDouble(Double a) {
		if (a != null) {
			DecimalFormat form = new DecimalFormat("####0.00");
			return form.format(a.doubleValue());
		}
		return "0.00";
	}

	public static String formateDouble(double a) {
		DecimalFormat form = new DecimalFormat("####0.00");
		return form.format(a);
	}

	public static String formateDoubleRate(Double a) {
		if (a != null) {
			DecimalFormat form = new DecimalFormat("####0.0000");
			return form.format(a.doubleValue());
		}
		return "0.0000";
	}

	public static String formateRate(Double a, int peci) {
		String result = "";
		if (a != null) {
			StringBuffer sb = new StringBuffer("####0.");
			for (int i = 0; i < peci; ++i) {
				sb.append("0");
			}
			DecimalFormat form = new DecimalFormat(sb.toString());
			result = form.format(a.doubleValue());
		}
		return result;
	}

	public static String formateDoubleRate(double a) {
		DecimalFormat form = new DecimalFormat("####0.0000");
		return form.format(a);
	}

	public static Double changeDoubleNull(Double a) {
		return ((a == null) ? new Double(0.0D) : a);
	}

	public static void main(String[] args) {
		System.out.println(add("8.5", "3.2"));
		System.out.println(add(8.012D, 2.5D));
		System.out.println(add(8L, 2L));
		System.out.println(multiply(8.01D, 2.01D));

		BigDecimal s1 = new BigDecimal(1234567810.124);
		BigDecimal s2 = new BigDecimal(12);

		BigDecimal result = s2.subtract(s2);
		result = result.setScale(2, RoundingMode.HALF_UP);
		int c = result.compareTo(BigDecimal.ZERO);
		System.out.println(c);
		System.out.println("result: " + result);
	}

	public static int compare(Double a, Double b) {
		Double diff = subtract(a, b);
		if ((diff == null) || (diff.doubleValue() == 0.0D))
			return 0;
		if (diff.doubleValue() > 0.0D) {
			return 1;
		}
		return -1;
	}

	public static long add(long v1, long v2) {
		BigDecimal b1 = new BigDecimal(Long.toString(v1));
		BigDecimal b2 = new BigDecimal(Long.toString(v2));
		return b1.add(b2).longValue();
	}

	public static Long add(Long v1, Long v2) {
		if ((v1 == null) && (v2 == null))
			return null;
		long x = (v1 == null) ? 0L : v1.longValue();
		long y = (v2 == null) ? 0L : v2.longValue();
		return new Long(add(x, y));
	}
}
