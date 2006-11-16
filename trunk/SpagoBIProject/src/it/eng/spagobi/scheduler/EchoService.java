package it.eng.spagobi.scheduler;

public class EchoService {
	
	public String echo(String echostring) {
		System.out.println("echoservice = " + echostring);
        return echostring;
	}
	
}
