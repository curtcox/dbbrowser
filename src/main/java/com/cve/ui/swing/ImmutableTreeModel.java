package com.cve.ui.swing;

import com.cve.util.Check;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.awt.EventQueue;
import java.util.List;
import java.util.Map;
import javax.annotation.concurrent.Immutable;
import javax.swing.JFrame;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.jdesktop.swingx.JXTree;

/**
 * An immutable tree model.
 * This class is designed recursively, so that any multi-node model is built
 * on models for its subtrees.  While not specific to this implementation,
 * it is worth noting that the TreeModel interface implies that a given object
 * always has the same children.  Users of TreeModelS, like JTree, may go further
 * and consider any pair of equivalent objects to be the same.
 * <p>
 * With that in mind, consider a TreeModel for a directory tree.  The directory
 * name alone isn't a safe choice for the nodes.  Using it would cause a problem
 * in the following case.
 * <ul>
 *   <li> src
 *       <ul>
 *           <li> com directory contents
 *       </ul>
 *   <li> test
 *       <ul>
 *           <li> com different directory contents
 *       </ul>
 * </ul>
 * If the name alone is desired at each node in the tree, options include:
 * <ol>
 *    <li> Wrapping the path with an object that provides the desired toString().
 *    <li> Using an appropriate renderer.
 * </ol>
 * @author curt
 */
@Immutable
public final class ImmutableTreeModel implements TreeModel {

    /**
     * The object at the root of this tree.
     */
    final Object root;

    /**
     * The sub-trees immediately under the root.
     */
    final ImmutableList<ImmutableTreeModel> subTrees;

    /**
     * Use a factory.
     */
    private ImmutableTreeModel(Object root, List<ImmutableTreeModel> children) {
        this.root = Check.notNull(root);
        this.subTrees = ImmutableList.copyOf(children);
    }

    /**
     * Create a single node tree with no children.
     */
    static ImmutableTreeModel of(Object leaf) {
        List<ImmutableTreeModel> children = ImmutableList.of();
        return new ImmutableTreeModel(leaf,children);
    }

    /**
     * Create a tree with the given root and children.
     */
    static ImmutableTreeModel of(Object root, List<ImmutableTreeModel> children) {
        return new ImmutableTreeModel(root,children);
    }

    /**
     * Create a tree with the given root and nodes.
     * Nodes that are ImmutableTreeModels are taken to be the children of
     * the immediately preceeding node.
     * All other nodes are taken to be the root of an ImmutableTreeModel.
     */
    public static ImmutableTreeModel root(Object root, Object... nodes) {
        if (nodes.length==0) {
            return of(root);
        }
        List<Object> roots = Lists.newArrayList();
        Map<Object,List<ImmutableTreeModel>> kids = Maps.newHashMap();
        Object subRoot = null;
        for (Object node : nodes) {
            if (node instanceof List) {
                kids.put(subRoot, (List) node);
            } else {
                subRoot = node;
                roots.add(subRoot);
            }
        }

        List<ImmutableTreeModel> myKids = childrenFrom(roots,kids);
        return new ImmutableTreeModel(root,myKids);
    }

    /**
     * Create a tree with the given root and nodes.
     * Nodes that are ImmutableTreeModels are taken to be the children of
     * the immediately preceeding node.
     * All other nodes are taken to be the root of an ImmutableTreeModel.
     */
    public static List<ImmutableTreeModel> nodes(Object root, Object... nodes) {
        if (nodes.length==0) {
            return ImmutableList.of(of(root));
        }
        List<Object> roots = Lists.newArrayList();
        Map<Object,List<ImmutableTreeModel>> kids = Maps.newHashMap();
        Object subRoot = null;
        for (Object node : nodes) {
            if (node instanceof List) {
                kids.put(subRoot, (List) node);
            } else {
                subRoot = node;
                roots.add(subRoot);
            }
        }
        
        List<ImmutableTreeModel> myKids = childrenFrom(roots,kids);
        return myKids;
    }

    static ImmutableList<ImmutableTreeModel> childrenFrom(List<Object> roots, Map<Object,List<ImmutableTreeModel>> kids) {
        List<ImmutableTreeModel> list = Lists.newArrayList();
        for (Object root : roots) {
            List<ImmutableTreeModel> myKids = kids.get(root);
            if (myKids==null) {
                list.add(of(root));
            } else {
                list.add(of(root,myKids));
            }
        }
        return ImmutableList.copyOf(list);
    }

    /**
     * Returns the root of the tree. Returns null only if the tree has no nodes.
     */
    @Override public Object getRoot() { return root; }

    /**
     * Returns the child of parent at index index  in the parent's child array.
     * parent must be a node previously obtained from this data source.
     */
    @Override public Object getChild(Object parent, int index) {
        if (parent==root) {
            return subTrees.get(index).root;
        }
        for (ImmutableTreeModel subTree : subTrees) {
            Object child = subTree.getChild(parent, index);
            if (child!=null) {
                return child;
            }
        }
        return null;
    }

    /**
     * Returns the index of child in parent. If parent is null or child is null, returns -1.
     */
    @Override public int getIndexOfChild(Object parent, Object child) {
        if (parent==root) {
            for (int i=0; i<subTrees.size(); i++) {
                if (child==subTrees.get(i).root) {
                    return i;
                }
            }
            return -1;
        }
        for (ImmutableTreeModel subTree : subTrees) {
            int index = subTree.getIndexOfChild(parent, child);
            if (index!=-1) {
                return index;
            }
        }
        return -1;
    }

    /**
     * Returns the number of children of parent.
     * Returns 0 if the node is a leaf or if it has no children.
     * Parent must be a node previously obtained from this data source.
     */
    @Override public int getChildCount(Object parent) {
        if (parent==root) {
            return subTrees.size();
        }
        int NOT_IN_THIS_SUBTREE = -1;
        for (ImmutableTreeModel subTree : subTrees) {
            int count = subTree.getChildCount(parent);
            if (count!=NOT_IN_THIS_SUBTREE) {
                return count;
            }
        }
        return NOT_IN_THIS_SUBTREE;
    }

    /**
     * Returns true if node is a leaf.
     * It is possible for this method to return false  even if node has no children.
     * A directory in a filesystem, for example, may contain no files;
     * the node representing the directory is not a leaf, but it also has no children. 
     */
    @Override public boolean isLeaf(Object node) {
        if (node==root) {
            return subTrees.isEmpty();
        }
        for (ImmutableTreeModel subTree : subTrees) {
            if (subTree.isLeaf(node)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void valueForPathChanged(TreePath path, Object o) {
        // don't bother -- we're immutable
    }


    @Override
    public void addTreeModelListener(TreeModelListener listener) {
        // don't bother -- we're immutable
    }

    @Override
    public void removeTreeModelListener(TreeModelListener listener) {
        // don't bother -- we're immutable
    }

    @Override
    public String toString() {
        return root + " : " + subTrees;
    }

    static void print(String message) {
        System.out.println(message);
    }
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override public void run() {
                show();
            }
        });
    }

    static TreeModel sample() {
        return root("Numbers",
             0, nodes(10,40, nodes(41,42), 50),
           100, nodes(110,120),
           200
        );
    }

    static TreeModel sample2() {
        return root("Numbers",
             0, nodes(10,20,30,40, nodes(41,42,43,44,45,46,47,48,49), 50,60,70,80,90),
           100, nodes(110,120,130,140,150,160,170,180,190),
           200, nodes(210,220,230,240,250,260,270,280,290),
           300
        );
    }

    static void show() {
        JFrame frame = new JFrame();
        JXTree tree = new JXTree(sample2());
        frame.add(tree);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(800,800);
    }
}
