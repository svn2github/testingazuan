package it.eng.spagobi.studio.geo.util;

import it.eng.spagobi.studio.geo.editors.model.geo.GuiParam;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ParamConverter implements Converter {

    public boolean canConvert(Class clazz) {
            return clazz.equals(GuiParam.class);
    }

    public void marshal(Object value, 
    					HierarchicalStreamWriter writer,
    					MarshallingContext context) {
            GuiParam param = (GuiParam) value;
            writer.addAttribute("name", param.getName());
            writer.setValue(param.getValue());
            
    }

    public Object unmarshal(HierarchicalStreamReader reader,
                    UnmarshallingContext context) {
    		GuiParam par = new GuiParam();
    		String value =reader.getValue();
    		String name = reader.getAttribute("name");
    		par.setName(name);
            par.setValue(value);
            
            return par;
    }

}
