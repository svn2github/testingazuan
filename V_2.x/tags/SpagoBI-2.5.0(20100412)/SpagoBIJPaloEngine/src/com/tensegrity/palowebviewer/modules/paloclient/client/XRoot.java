package com.tensegrity.palowebviewer.modules.paloclient.client;

import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XHelper;


/**
 * XRoot object - is non visual object, which reflects root of web-server structure. Contains XServers 
 * @author dmol
 *
 */
public class XRoot extends XObject {
	
    public static final String ID = "XRoot";

    private XServer[] servers;

    public XRoot() {
        super(ID, "");
    }

    public XRoot(XServer[] servers) {
        super(ID, "");
        this.servers = servers;
    }

    public XServer[] getServers() {
        return servers;
    }

    public void setServers(XServer[] servers) {
        this.servers = servers;
        XHelper.setParent(servers, this);
    }

    public void set(XObject object) {
        if(!(object instanceof XRoot))
            throw new IllegalArgumentException("XObject has to be of type XRoot");
        super.set(object);
            setServers(((XRoot)object).getServers());
    }

    public boolean equals(Object o) {
        if(o instanceof XRoot)
            return equals((XRoot)o);
        else
            return false;
    }

    public boolean equals(XRoot root) {
        if(root == null)
            return false;
        return super.equals(root);
    }

    public int getTypeID() {
        return TYPE_ROOT;
    }

}
