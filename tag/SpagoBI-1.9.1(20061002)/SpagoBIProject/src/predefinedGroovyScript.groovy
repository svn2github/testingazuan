public String getSingleValueProfileAttribute(String attrName) {
	return attrName;
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
	return finalResult;
};
