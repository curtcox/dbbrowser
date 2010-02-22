package com.cve.ui.swing;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 *
 * @author Curt
 */
public final class SwingRouterFrame extends JFrame {

    final JButton    forward = new JButton(">");
    final JButton    back    = new JButton("<");
    final JButton    reload  = new JButton("@");
    final JComboBox  address = new JComboBox();

    private SwingRouterFrame() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static SwingRouterFrame of() {
        try {
            FutureTask task = new FutureTask(new Callable(){
                @Override
                public Object call() throws Exception {
                    return new SwingRouterFrame();
                }
            });
            EventQueue.invokeAndWait(task);
            return (SwingRouterFrame) task.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public void browse(URI uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
