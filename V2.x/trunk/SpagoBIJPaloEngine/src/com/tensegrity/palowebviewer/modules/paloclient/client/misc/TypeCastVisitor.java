package com.tensegrity.palowebviewer.modules.paloclient.client.misc;

import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.XAxis;
import com.tensegrity.palowebviewer.modules.paloclient.client.XConsolidatedElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XCube;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDatabase;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElementNode;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.XRoot;
import com.tensegrity.palowebviewer.modules.paloclient.client.XServer;
import com.tensegrity.palowebviewer.modules.paloclient.client.XSubset;
import com.tensegrity.palowebviewer.modules.paloclient.client.XView;


public abstract class TypeCastVisitor implements IXVisitor, IXConsts
{

    public boolean hasFinished() {
		return false;
	}

	public void visit(XObject obj) {
    	if(obj == null)
    		return;
        switch(obj.getTypeID()){
        case TYPE_ROOT:{
            visitRoot((XRoot)obj);
            break;
        }
        case TYPE_SERVER:{
            visitServer((XServer)obj);
            break;
        }
        case TYPE_DATABASE:{
            visitDatabase((XDatabase)obj);
            break;
        }
        case TYPE_CUBE:{
            visitCube((XCube)obj);
            break;
        }
        case TYPE_DIMENSION:{
            visitDimension((XDimension)obj);
            break;
        }
        case TYPE_ELEMENT:{
            visitElement((XElement)obj);
            break;
        }
        case TYPE_CONSOLIDATED_ELEMENT:{
            visitConsolidatedElement((XConsolidatedElement)obj);
            break;
        }
        case TYPE_SUBSET:{
            visitSubset((XSubset)obj);
            break;
        }
        case TYPE_VIEW:{
            visitView((XView)obj);
            break;
        }
    	case TYPE_AXIS:{
    		visitAxis((XAxis)obj);
    		break;
    	}
    	case TYPE_ELEMENT_NODE:{
    		visitElementNode((XElementNode)obj);
    	}
        }
    }

	public abstract void visitElementNode(XElementNode node);

	public abstract void visitRoot(XRoot root);

    public abstract void visitServer(XServer server);

    public abstract void visitDatabase(XDatabase database);

    public abstract void visitDimension(XDimension dimension);

    public abstract void visitConsolidatedElement(XConsolidatedElement consolidatedElement);
    
    public abstract void visitElement(XElement element);

    public abstract void visitCube(XCube cube);

    public abstract void visitSubset(XSubset subset);

    public abstract void visitView(XView view);

    public abstract void visitAxis(XAxis axis);

}
