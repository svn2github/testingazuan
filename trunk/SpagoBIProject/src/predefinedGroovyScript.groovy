public String getSingleValueProfileAttribute(String attrName) {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append('<ROWS>');
		strBuf.append('<ROW ');
		strBuf.append('value=\''+attrName+'\' >');
		strBuf.append('</ROW>');
		strBuf.append('<visible-columns>');
		strBuf.append('value');
		strBuf.append('</visible-columns>');
		strBuf.append('<value-column>');
		strBuf.append('value');
		strBuf.append('</value-column>');
		strBuf.append('<description-column>');
		strBuf.append('value');
		strBuf.append('</description-column>');
		strBuf.append('</ROWS>');
		return strBuf.toString();
};

public String getMultiValueProfileAttribute(String attrName, String prefix, String newSplit, String suffix) {
	String splitter = attrName.substring(1,2);
	String valuesList = attrName.substring(3, attrName.length() - 2);
	String [] values = valuesList.split(splitter);
	String newListOfValues = values[0];
	for (i in 1..<values.length) {
		newListOfValues = newListOfValues + newSplit + values[i];
	};
	String finalResult = prefix + newListOfValues + suffix;
	
	StringBuffer strBuf = new StringBuffer();
	strBuf.append('<ROWS>');
	strBuf.append('<ROW ');
	strBuf.append('value=\''+finalResult+'\' >');
	strBuf.append('</ROW>');
	strBuf.append('<visible-columns>');
	strBuf.append('value');
	strBuf.append('</visible-columns>');
	strBuf.append('<value-column>');
	strBuf.append('value');
	strBuf.append('</value-column>');
	strBuf.append('<description-column>');
	strBuf.append('value');
	strBuf.append('</description-column>');
	strBuf.append('</ROWS>');
	
	return strBuf.toString();
};


public String returnValue(String valuein) {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append('<ROWS>');
		strBuf.append('<ROW ');
		strBuf.append('value=\''+valuein+'\' >');
		strBuf.append('</ROW>');
		strBuf.append('<visible-columns>');
		strBuf.append('value');
		strBuf.append('</visible-columns>');
		strBuf.append('<value-column>');
		strBuf.append('value');
		strBuf.append('</value-column>');
		strBuf.append('<description-column>');
		strBuf.append('value');
		strBuf.append('</description-column>');
		strBuf.append('</ROWS>');
		return strBuf.toString();
};
