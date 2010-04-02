package com.cve.ui.swing;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.EventComboBoxModel;
import javax.swing.MutableComboBoxModel;

/**
 * 
 * @author curt
 */
final class MutableEventComboBoxModel
    extends EventComboBoxModel
    implements MutableComboBoxModel
{

    final EventList source;

    public MutableEventComboBoxModel(EventList source) {
        super(source);
        this.source = source;
    }

    @Override public void    addElement(Object e)          { source.add(e);    }
    @Override public void   removeElement(Object o)        { source.remove(o); }
    @Override public void insertElementAt(Object o, int i) { source.add(i, o); }
    @Override public void removeElementAt(int i)           { source.remove(i); }

}
