package it.eng.spagobi.importexport;

public interface ITransformer {

	public byte[] transform(byte[] content, String tmpFolder, String archiveName);
	
}
