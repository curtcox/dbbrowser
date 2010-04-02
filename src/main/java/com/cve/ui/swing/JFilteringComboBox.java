package com.cve.ui.swing;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Arrays;
import javax.swing.JComboBox;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.matchers.TextMatcherEditor;
import ca.odell.glazedlists.matchers.ThreadedMatcherEditor;
import ca.odell.glazedlists.swing.EventComboBoxModel;
import java.awt.Container;

/**
 * This subclass of <code>JComboBox</code> adds the ability to filter the
 * contents of the model as the value of its editor component changes. Case
 * insensitive String filtering is used when filtering the contents of the
 * model. The contents of the model are always filtered in a background Thread
 * and the model updated on the Event Dispatch Thread.
 */
final class JFilteringComboBox extends JComboBox {
    /** Glazed Lists classes which back the combobox model to provide filtering capability. */
    final EventList items = new BasicEventList();
    final TextMatcherEditor textMatcherEditor;
    final FilterList filteredItems;
    final EventComboBoxModel model;


    /** Invoke a background filtering of the combobox model when the combobox editor changes its Document. */
    final FilteringDocumentListener filterInvoker = new FilteringDocumentListener();

    /** Watch for changes to the Document. */
    final PropertyChangeListener documentWatcher = new DocumentWatcher();

    /**
     * Creates a combobox which filters its values, all of which are assumed
     * to be String values.
     */
    JFilteringComboBox() {
        this(GlazedLists.toStringTextFilterator());
    }

    /**
     * Creates a combobox which filters its values according to the Strings
     * extracted by the given <code>textFilterator</code> from each value in
     * the model.
     *
     * @param textFilterator an object capable of mapping each combo box item
     * to a list of strings to be considered when filtering
     */
    JFilteringComboBox(final TextFilterator textFilterator) {
        textMatcherEditor = new TextMatcherEditor(textFilterator);
        filteredItems = new FilterList(items, new ThreadedMatcherEditor(textMatcherEditor));
        model = new MutableEventComboBoxModel(filteredItems);
        setModel(model);
        setEditable(true);

        JTextComponent editorComponent = (JTextComponent) getEditor().getEditorComponent();
        // handle changes to the document editor
        editorComponent.getDocument().addDocumentListener(filterInvoker);

        // listens for changes in the Document
        editorComponent.addPropertyChangeListener("document", documentWatcher);
    }

    /**
     * Set the values within the combobox model.
     */
    public void setItems(Collection items) {
        this.items.clear();
        this.items.addAll(items);
    }

/**
 * Get the values within the combobox model. Changes to the returned
 * collection will be reflected in the combo box.
 */
public Collection getItems() {
    return items;
}

/**
When the Document behind the combo box's editor component
changes, it is a signal to refilter the available combo box
selections.

Only typing in the combo box editor should cause filtering to occur
so we must avoid filtering when the Document is changing due to a
change of the selected item, hence the short circuit in the Runnable.

We flush the value from the document to the combo box to guarantee
the combo box has the most recent typed value when its model is changed
out from under it. This of course happens as a consequence of the filter
that immediately follows the call to setSelectedItem().

Changing the combo box's model always forces the document to be changed,
which is illegal to do directly in the document's filterInvoker. Therefore,
we post this work in a runnable to the end of the event queue.
*/
  final class FilteringDocumentListener implements DocumentListener {
        @Override
        public void changedUpdate(DocumentEvent e) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override public void run() {
                    String documentText = ((JTextComponent) editor.getEditorComponent()).getText();
                    if (documentText.equals(getSelectedItem())) return;
                    setSelectedItem(documentText);
                    textMatcherEditor.setFilterText(documentText.split(" "));
                 }
            });
        }
        @Override public void insertUpdate(DocumentEvent e) {
            this.changedUpdate(e);
        }
        @Override public void removeUpdate(DocumentEvent e) {
            this.changedUpdate(e);
        }
   }

    /**
     * Watch the Document behind the editor component in case it changes. If a
     * new Document is swapped in, stop filtering based on changes in the old
     * Document and begin filtering based on changes in the new Document.
     */
    final class DocumentWatcher implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            final Document oldDocument = (Document) evt.getOldValue();
            final Document newDocument = (Document) evt.getNewValue();

            if (oldDocument != null) {
                    oldDocument.removeDocumentListener(filterInvoker);
            }
            if (newDocument != null) {
                    newDocument.addDocumentListener(filterInvoker);
            }
        }
    }

    public static void main(String[] args) {
        JFilteringComboBox comboBox = new JFilteringComboBox();
        comboBox.setItems(Arrays.asList(new Object[] {
                "Jack", "Jagger", "Jal", "Jamison", "Jarman", "Jarvis", "Jenna", "Jennifer", "Jenny", "Joelle", "Jolene", "Joline", "Josephine"
        }));

        JFrame frame = new JFrame("Filtering ComboBox Example");


        Container contents = frame.getContentPane();
        contents.setLayout(new BorderLayout());
        contents.add(comboBox, BorderLayout.CENTER);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.show();
    }
}