package com.zbjdl.security;

public class LuhnUtil {
	public static void main(String[] args) {
		String cardNumber = "37715502164480";
		System.out.println(cardNumber + getCheckNumber(cardNumber));

		System.out.println(verifyCardNo("377155021644809"));
	}

	/**
	 * Luhn算法 校验卡号是否满足规则
	 * @param number
	 * @return
	 */
	public static boolean verifyCardNo(String number) {
		int s1 = 0, s2 = 0;
		String reverse = new StringBuffer(number).reverse().toString();
		for (int i = 0; i < reverse.length(); i++) {
			int digit = Character.digit(reverse.charAt(i), 10);
			if (i % 2 == 0) {// this is for odd digits, they are 1-indexed in the algorithm
				s1 += digit;
			} else {// add 2 * digit for 0-4, add 2 * digit - 9 for 5-9
				s2 += 2 * digit;
				if (digit >= 5) {
					s2 -= 9;
				}
			}
		}
		return (s1 + s2) % 10 == 0;
	}

	/**
	 * Luhn算法 根据卡号获取校验位
	 * 
	 * @param cardNumber
	 * @return
	 */
	public static int getCheckNumber(String cardNumber) {
		int totalNumber = 0;
		for (int i = cardNumber.length() - 1; i >= 0; i -= 2) {
			int tmpNumber = calculate(Integer.parseInt(String.valueOf(cardNumber.charAt(i))) * 2);
			if (i == 0) {
				totalNumber += tmpNumber;
			} else {
				totalNumber += tmpNumber + Integer.parseInt(String.valueOf(cardNumber.charAt(i - 1)));
			}
		}
		if (totalNumber >= 0 && totalNumber < 9) {
			return (10 - totalNumber);
		} else {
			String str = String.valueOf(totalNumber);
			if (Integer.parseInt(String.valueOf(str.charAt(str.length() - 1))) == 0) {
				return 0;
			} else {
				return (10 - Integer.parseInt(String.valueOf(str.charAt(str.length() - 1))));
			}
		}
	}

	//计算数字各位和
	private static int calculate(int number) {
		String str = String.valueOf(number);
		int total = 0;
		for (int i = 0; i < str.length(); i++) {
			total += Integer.valueOf(Integer.parseInt(String.valueOf(str
					.charAt(i))));
		}
		return total;
	}
}
