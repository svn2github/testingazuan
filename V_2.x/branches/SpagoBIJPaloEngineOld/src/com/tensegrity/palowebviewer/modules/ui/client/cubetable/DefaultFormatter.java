package com.tensegrity.palowebviewer.modules.ui.client.cubetable;

import com.tensegrity.palowebviewer.modules.paloclient.client.IElementType;
import com.tensegrity.palowebviewer.modules.paloclient.client.XNumericType;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IResultElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.ResultDouble;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.ResultString;

public class DefaultFormatter implements IFormatter {

	private char floatSeparator = '.';
	private int fracDigits = 2;
	private String decimalSeparator = "";
	private final StringBuffer buffer  = new StringBuffer();
	private int exp;
	private char[] digitArray = new char[30];
	private int sepIndex;
	private int digitCount;
	private boolean negative;
	private int fraction;
	private double value;

	public void setFloatSeparator(char value) {
		floatSeparator = value;
	}

	public void setDecimalSeparator(String value) {
		if (value == null) {
			value = "";
		}
		this.decimalSeparator = value;
	}

	public void setFractionalDigitsNumber(int value) {
		if (value < 0)
			value = 0;
		this.fracDigits = value;
	}

	private String formatFraction(double number) {
		this.value = number;
		isNegative();
		buffer.setLength(0);
		int tenFrNPow = tenPower();
		calculateFraction(value, tenFrNPow);
		doCarry(tenFrNPow);
		
		String result = new Double(value).toString();
		int eIndex = result.indexOf('E');
		if(eIndex<0)
			eIndex = result.indexOf('e');
		exp = 0;
		String digits = result;
		if(eIndex>=0){
			exp = new Integer(result.substring(eIndex+1)).intValue();
			digits = result.substring(0, eIndex);
		}
		getDigits(digits);
		formatIntPart();
		
		appendFractionDigits(tenFrNPow);
		if (negative)
			buffer.insert(0, '-');
		result = buffer.toString();
		return result;
	}

	private void appendFractionDigits(int tenFrNPow) {
		if (fracDigits > 0) {
			buffer.append(floatSeparator);
			completeDigits(tenFrNPow, fraction, buffer);
			buffer.append(fraction);
		}
	}

	private void doCarry(int tenFrNPow) {
		if (fraction >= tenFrNPow) {
			value += 1;
			fraction %= tenFrNPow;
		}
	}

	private void isNegative() {
		negative = value < 0;
		if (negative)
			value = -value;
	}

	private void getDigits(String digits) {
		char[] string = digits.toCharArray();
		digitCount = 0;
		sepIndex = -1;
		for (int i = 0; i < string.length; i++) {
			char ch = string[i];
			if('0'<= ch && ch<='9') {
				digitArray[digitCount] = ch;
				digitCount++;
			}
			else if( ch == '-'){
				negative = true;
			} else {
				sepIndex = i;
			}
		}
		if(sepIndex == -1) {
			sepIndex = digitCount;
		}
		else {
			if(string[0] == '-'){
				sepIndex--;
			}
		}
	}

	private void formatIntPart() {
		int intDigCount = exp+sepIndex;
		if(intDigCount <=0) {
			buffer.append('0');
		}
		else {
			int n = Math.min(intDigCount, digitCount);
			buffer.append(digitArray, 0, n);
			for(int i = n; i<intDigCount; i++){
				buffer.append('0');
			}
			for(int i= buffer.length()-3; i > 0; i-=3) {
				buffer.insert(i, decimalSeparator);
			}
		}
	}

	private void calculateFraction(double value, int tenFrNPow) {
		value = value - Math.floor(value);//this is for toooo big numbers. E19 or so.
		double a = (value * tenFrNPow);
		fraction = (int) (a % tenFrNPow);
		int afterAfterPoint = (int) ((a * 10)% 10);
		if (afterAfterPoint >= 5) {
			fraction++;
		}
	}

	private int tenPower() {
		int result = 1;
		for (int i = 0; i < fracDigits; i++) {
			result *= 10;
		}
		return result;
	}

	private void completeDigits(int tenInFRNPower, int afterPoint, StringBuffer result) {
		int copyPower = tenInFRNPower / 10;
		while ((copyPower > afterPoint) && (copyPower > 1)) {
			copyPower /= 10;
			result.append('0');
		}
	}

	public IResultElement unformat(String value, IElementType type) {
		IResultElement r;
		if (type.equals(XNumericType.instance)) {
			value = value.replaceAll("\\s*", "");
			if (decimalSeparator.length() != 0)
				value = value.replaceAll("\\" + decimalSeparator, "");
			value = value.replace(floatSeparator, '.');
			double dv = Double.parseDouble(value);
			r = new ResultDouble(dv);
		} else {
			r = new ResultString(value);
		}
		return r;
	}

	public String format(IResultElement value) {
		String r = null;
		if (value instanceof ResultDouble) {
			ResultDouble rd = (ResultDouble) value;
			double dv = rd.getDoubleValue();
			r = formatFraction(dv);
		} else {
			ResultString rs = (ResultString) value;
			r = rs.getValue();
		}
		return r;
	}

}
