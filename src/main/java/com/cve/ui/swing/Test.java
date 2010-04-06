package com.cve.ui.swing;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

/**
 *
 * @author curt
 */
public class Test {

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override public void run() {
                show();
            }
        });
    }

    static void show() {
         JXFrame frame = new JXFrame();

         // a container to put all JXTaskPane together
         JXTaskPaneContainer taskPaneContainer = new JXTaskPaneContainer();

         // create a first taskPane with common actions
         JXTaskPane actionPane = new JXTaskPane();
         actionPane.setTitle("Files and Folders");
         actionPane.setSpecial(true);

         // actions can be added, an hyperlink will be created
         Action renameSelectedFile = new AbstractAction() {
            @Override public void actionPerformed(ActionEvent ae) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
         };
         actionPane.add(renameSelectedFile);
         actionPane.add(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
         });

         // add this taskPane to the taskPaneContainer
         taskPaneContainer.add(actionPane);

         // create another taskPane, it will show details of the selected file
         JXTaskPane details = new JXTaskPane();
         details.setTitle("Details");

         // add standard components to the details taskPane
         JLabel searchLabel = new JLabel("Search:");
         JTextField searchField = new JTextField("");
         details.add(searchLabel);
         details.add(searchField);

         taskPaneContainer.add(details);

         // put the action list on the left
         frame.add(taskPaneContainer, BorderLayout.EAST);

         // and a file browser in the middle
         //frame.add(new JLabel("Center"), BorderLayout.CENTER);

         frame.pack();
         frame.setVisible(true);
    }

}
