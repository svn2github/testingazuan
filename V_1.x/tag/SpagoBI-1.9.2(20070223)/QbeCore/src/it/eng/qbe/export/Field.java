/**
 * 
 */
package it.eng.qbe.export;

/**
 * @author Gioia
 *
 */
public class Field {
	private String name;
	private String description;
	private String classType;
	private int displySize;
	
	public  Field(String name, String classType, int displySize) {
		this.name = name;
		this.classType = classType;
		this.displySize = displySize;
		this.description = "";
	}

	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = getFieldType(classType);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	/**
     * Return the correct field type...     *
     */
    public static String getFieldType(String type)
    {

        if (type == null) return "java.lang.Object";

        if (type.equals("java.lang.Boolean") || type.equals("boolean")) return "java.lang.Boolean";
        if (type.equals("java.lang.Byte") || type.equals("byte")) return "java.lang.Byte";
        if (type.equals("java.lang.Integer") || type.equals("int")) return "java.lang.Integer";
        if (type.equals("java.lang.Long") || type.equals("long")) return "java.lang.Long";
        if (type.equals("java.lang.Double") || type.equals("double")) return "java.lang.Double";
        if (type.equals("java.lang.Float") || type.equals("float")) return "java.lang.Float";
        if (type.equals("java.lang.Short") || type.equals("short")) return "java.lang.Short";
        if (type.startsWith("[")) return "java.lang.Object";
       
        return type;
    }

	public int getDisplySize() {
		return displySize;
	}

	public void setDisplySize(int displySize) {
		this.displySize = displySize;
	}
}
