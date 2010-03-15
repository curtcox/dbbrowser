package com.cve.ui.swing;

import java.awt.EventQueue;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JTree.DynamicUtilTreeNode;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import org.jdesktop.swingx.JXTree;

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
        JFrame frame = new JFrame();
        Hashtable map = new Hashtable();
        map.put("1", "1.1");
        map.put("1.1", "1.1.1");
        JXTree tree = new JXTree(map);
        frame.add(tree);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(800,800);
    }

      /**
      * Returns a <code>TreeModel</code> wrapping the specified object.
      * If the object is:<ul>
      * <li>an array of <code>Object</code>s,
      * <li>a <code>Hashtable</code>, or
      * <li>a <code>Vector</code>
      * </ul>then a new root node is created with each of the incoming
      * objects as children. Otherwise, a new root is created with the
      * specified object as its value.
      *
      * @param value the <code>Object</code> used as the foundation for
      * the <code>TreeModel</code>
      * @return a <code>TreeModel</code> wrapping the specified object
      * Read more: http://kickjava.com/src/javax/swing/JTree.java.htm#ixzz0hh3gEqTh
      */
     static TreeModel createTreeModel(Object value) {
         DefaultMutableTreeNode root;

         if((value instanceof Object[]) || (value instanceof Hashtable) ||
            (value instanceof Vector)) {
             root = new DefaultMutableTreeNode("root");
             DynamicUtilTreeNode.createChildren(root, value);
         }
         else {
             root = new DynamicUtilTreeNode("root", value);
         }
         return new DefaultTreeModel(root, false);
     }


}
