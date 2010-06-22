/* =============================================================================
* Override:  [OPEN-1034] findField() access for RadioGroup and CheckboxGroup
* This overrides plus invoking Ext.form.BasicForm.reset just before Ext.form.BasicForm.setValues, 
* fix the Ext.form.BasicForm.setValues method for check boxes and radio button.
* See:
* - http://www.sencha.com/forum/showthread.php?101373-OPEN-1034-findField%28%29-access-for-RadioGroup-and-CheckboxGroup&highlight=basicform+checkbox
============================================================================= */
Ext.override(Ext.form.RadioGroup, {
    //private
    isRadioGroup: true
});

// add type flag to CheckboxGroup
Ext.override(Ext.form.CheckboxGroup, {
    //private
    isCheckboxGroup: true
});

//Override for finding fields in checkboxgroups or radiogroups
Ext.override(Ext.BasicForm, {
  findField: function(id) {
        var field = this.items.get(id);

        if (!Ext.isObject(field)) {
            //searches for the field corresponding to the given id. Used recursively for composite fields
            var findMatchingField = function(f) {
                if (f.isFormField) {
                    if (f.dataIndex == id || f.id == id || f.getName() == id) {
                        field = f;
                        return false;
                    } else if (f.isComposite && f.rendered) {
                        return f.items.each(findMatchingField);
                    } else if (f.isRadioGroup && f.rendered) {
                        // for a radio group we assume
                        // only want to find the 'checked' radio
                        return f.items.each(function(sf){
                            if ((sf.dataIndex == id || sf.id == id || sf.getName() == id) && sf.getValue()) {
                                field = sf;
                                return false;
                            }
                        },this);
                    } else if (f.isCheckboxGroup && f.rendered) {
                        // for checkbox group we want 1st match
                        return f.items.each(findMatchingField);
                    }
                }
            };

            this.items.each(findMatchingField);
        }
        return field || null;
    }
});
