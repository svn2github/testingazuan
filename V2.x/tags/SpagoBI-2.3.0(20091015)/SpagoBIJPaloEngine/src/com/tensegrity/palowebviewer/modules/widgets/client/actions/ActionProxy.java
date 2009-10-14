package com.tensegrity.palowebviewer.modules.widgets.client.actions;


/**
 * A proxy for an action. The base action can be changed and
 * listeners of the proxy actions will be notified of the change.
 * It is used to add to some widget(or few of them) and afterwords define actual action
 *
 */
public class ActionProxy extends AbstractAction
{

    private IAction action;
    private final IPropertyListener propertyLIstener = new IPropertyListener() {

        public void onEnabled() {
            notifyPropertyChangeListeners();
        }

        public void onDisabled() {
            notifyPropertyChangeListeners();
        }

    };

    public void setAction(IAction action) {
        boolean state = isEnabled();
        if(this.action != null)
            this.action.removePropertyListener(propertyLIstener);
        this.action = action; 
        if(this.action != null)
            this.action.addPropertyListener(propertyLIstener);
        if(state != isEnabled())
            notifyPropertyChangeListeners();
    }

    public IAction getAction() {
        return this.action;
    }

    public boolean isEnabled() {
        IAction action = getAction();
        boolean result = false;
        if(action != null)
            result = action.isEnabled();
        return result;
    }

    public void setEnabled(boolean enabled) {
    }

    public void onActionPerformed(Object arg) {
        IAction action = getAction();
        if(action != null)
            action.onActionPerformed(arg);
    }

}
