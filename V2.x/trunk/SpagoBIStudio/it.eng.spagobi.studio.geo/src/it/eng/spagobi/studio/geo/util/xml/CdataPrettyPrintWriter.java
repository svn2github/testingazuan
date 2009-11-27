package it.eng.spagobi.studio.geo.util.xml;

import java.io.Writer;

import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XmlFriendlyReplacer;

public class CdataPrettyPrintWriter extends PrettyPrintWriter {
	public CdataPrettyPrintWriter(Writer writer, XmlFriendlyReplacer replacer) {
		super(writer, replacer);
	}

	protected void writeText(QuickWriter writer, String text) {
		// clean up text here
		super.writeText(writer, text);
	}
}
