package com.tensegrity.palowebviewer.modules.paloclient.client.misc;

import java.util.ArrayList;
import java.util.List;

import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;


public class NodeCollectorVisitor implements IXVisitor
{

    private final List list = new ArrayList();
    private final IXCondition condition;
    private boolean collectDuplicates;

    public void setCollectDuplicates(boolean value) {
        this.collectDuplicates = value;
    }

    public boolean getCollectDuplicates() {
        return collectDuplicates;
    }

    public NodeCollectorVisitor(IXCondition condition) {
        if(condition == null)
            condition = new IXCondition(){ 
                public boolean check(XObject obj) {
                    return true;
                }
            };
        this.condition = condition;
    }

    public XObject[] getCollectedItems() {
        XObject[] result = new XObject[getCount()];
        for( int i = 0 ; i < result.length ; i++ ) { 
            result[i] = getItem(i);
        } 
        return result;
    }

    public XObject getItem(int i) {
        return (XObject)list.get(i);
    }

    public int getCount() {
        return list.size();
    }

    public void clear() {
        list.clear();
    }

    public void visit(XObject object) {
        boolean collect = condition.check(object);
        if(collect && !getCollectDuplicates()) {
            collect = !list.contains(object);
        }
        if(collect) {
            list.add(object);
        }
    }

	public boolean hasFinished() {
		return false;
	}

}
