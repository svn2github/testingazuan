import it.eng.spagobi.scripting.ITestInterface;

public class Tester implements ITestInterface {
    public String getMessage() {
       return "Groovy implementation is running";
    }
}

Object o = new Tester();
