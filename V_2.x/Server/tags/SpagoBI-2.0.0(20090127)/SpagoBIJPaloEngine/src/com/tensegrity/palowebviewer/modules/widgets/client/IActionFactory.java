package com.tensegrity.palowebviewer.modules.widgets.client;

import com.tensegrity.palowebviewer.modules.widgets.client.actions.IAction;


/**
 * IActionFactory generates action for given object.
 */
public interface IActionFactory
{

    public IAction getActionFor(Object object);

}
