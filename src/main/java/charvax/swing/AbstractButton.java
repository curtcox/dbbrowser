/* class AbstractButton
 *
 * Copyright (C) 2001  R M Pitman
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package charvax.swing;

import charva.awt.EventQueue;
import charva.awt.ItemSelectable;
import charva.awt.Toolkit;
import charva.awt.Window;
import charva.awt.event.*;

import java.util.Enumeration;
import java.util.Vector;

/**
 * This forms the base class for components that exhibit button-like
 * behavior.
 */
public abstract class AbstractButton
        extends JComponent
        implements ItemSelectable, KeyListener {

    /**
     * @deprecated Replaced by setText().
     */
    public void setLabel(String label_) {
        setText(label_);
    }

    /**
     * Sets the button's label text.
     */
    public void setText(String label_) {
        _label = label_;
    }

    /**
     * Set the button's mnemonic character.
     * The mnemonic is the key which will activate this button if focus
     * is contained somewhere within this button's ancestor window.<p>
     * <p/>
     * A mnemonic must correspond to a single key on the keyboard and should be
     * specified using one of the VK_XXX keycodes defined in
     * java.awt.event.KeyEvent. Mnemonics are case-insensitive, therefore a key
     * event with the corresponding keycode would cause the button to be
     * activated whether or not the Shift modifier was pressed.<p>
     * <p/>
     * If the character defined by the mnemonic is found within the button's
     * label string, the first occurrence of it will be underlined to
     * indicate the mnemonic to the user. If the corresponding character
     * is not contained within the button's label, then it will be displayed
     * to the right, surrounded by parentheses.<p>
     * <p/>
     * In the case of a JButton, JCheckBox or JRadioButton, this method
     * should only be called <em>after</em> the button has been added
     * to a Window, otherwise pressing the corresponding key will have
     * no effect.
     *
     * @param mnemonic_ the keycode of the mnemonic key.
     */
    public void setMnemonic(int mnemonic_) {
        _mnemonic = mnemonic_;
        Window ancestor = super.getAncestorWindow();
        if (ancestor != null) {
            ancestor.addKeyListener(this);
        }
    }

    /**
     * @deprecated Replaced by getText().
     */
    public String getLabel() {
        return getText();
    }

    /**
     * Returns the button's label text.
     */
    public String getText() {
        return _label;
    }

    /**
     * Returns the button's mnemonic character.
     */
    public int getMnemonic() {
        return _mnemonic;
    }

    /**
     * Sets the action command for this button.
     */
    public void setActionCommand(String text_) {
        _actionCommand = text_;
    }

    /**
     * Returns the action command for this button.
     */
    public String getActionCommand() {
        return _actionCommand;
    }

    /**
     * Process a MouseEvent that was generated by clicking the mouse
     * somewhere inside this component.
     */
    public void processMouseEvent(MouseEvent e) {
        // Request focus if this is a MOUSE_PRESSED
        super.processMouseEvent(e);

        if (e.getButton() == MouseEvent.BUTTON1 &&
                e.getModifiers() == MouseEvent.MOUSE_CLICKED &&
                this.isFocusTraversable()) {

            this.doClick();
        }
    }

    /**
     * Programmatically performs a "click" of this button.
     */
    public void doClick() {

        // This is required because our parent window will send the KeyEvent
        // to the Container containing the component with the current focus.
//	super.requestFocus();
        getParent().setFocus(this);	// Mustn't generate FocusEvents (?)

        Toolkit.getDefaultToolkit().fireKeystroke(KeyEvent.VK_ENTER, this);
    }

    /**
     * Sets the state of the button. Note that this method does not post
     * an ActionEvent.
     */
    public void setSelected(boolean state_) {
        // Post an ItemEvent if the state has changed.
        if (_selected != state_) {
            int statechange = state_ ? ItemEvent.SELECTED : ItemEvent.DESELECTED;
            ItemEvent evt = new ItemEvent(this, this, statechange);
            Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(evt);
        }

        _selected = state_;
        if (isDisplayed())
            super.repaint();
    }

    /**
     * Returns the state of the button.
     */
    public boolean isSelected() {
        return _selected;
    }

    /**
     * Process events.
     */
    protected void processEvent(AWTEvent evt_) {

        super.processEvent(evt_);

        if (evt_ instanceof KeyEvent) {
            KeyEvent key_event = (KeyEvent) evt_;
            if ((!key_event.isConsumed()) &&
                    key_event.getKeyCode() == KeyEvent.VK_ENTER &&
                    super.isEnabled()) {

                EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
                queue.postEvent(new ActionEvent(this, getActionCommand()));
                key_event.consume();
            }
        } else if (evt_ instanceof ActionEvent)
            fireActionPerformed((ActionEvent) evt_);
        else if (evt_ instanceof ItemEvent)
            fireItemStateChanged((ItemEvent) evt_);
    }

    /**
     * Register an ActionListener object for this button.
     */
    public void addActionListener(ActionListener al_) {
        if (_actionListeners == null)
            _actionListeners = new Vector();
        _actionListeners.add(al_);
    }

    /**
     * Invoke all the ActionListener callbacks that may have been registered
     * for this button.
     */
    protected void fireActionPerformed(ActionEvent ae_) {
        if (_actionListeners != null) {
            for (Enumeration e = _actionListeners.elements();
                 e.hasMoreElements();) {

                ActionListener al = (ActionListener) e.nextElement();
                al.actionPerformed(ae_);
            }
        }
    }

    /**
     * Register an ItemListener object for this component.
     */
    public void addItemListener(ItemListener il_) {
        if (_itemListeners == null)
            _itemListeners = new Vector();
        _itemListeners.add(il_);
    }

    public void removeItemListener(ItemListener listener_) {
        if (_itemListeners == null)
            return;
        _itemListeners.remove(listener_);
    }

    /**
     * Implements the KeyListener interface; this is called if a control
     * character or a function key or cursor key was typed.
     */
    public void keyPressed(KeyEvent ke_) {
        if (ke_.getKeyCode() == getMnemonic()) {
            doClick();
            ke_.consume();
        }
    }

    /**
     * Implements the KeyListener interface; this is called if a printable
     * (ASCII or ISO8859-1) character was typed.
     */
    public void keyTyped(KeyEvent ke_) {
        // We must accept either uppercase or lowercase mnemonic characters.
        char keyLower = Character.toLowerCase((char) ke_.getKeyChar());
        char mnemonicLower = Character.toLowerCase((char) getMnemonic());
        if (keyLower == mnemonicLower) {
            doClick();
            ke_.consume();
        }
    }

    /**
     * Implements the KeyListener interface but is never invoked.
     */
    public void keyReleased(KeyEvent ke_) {
    }

    /**
     * Invoke all the ItemListener callbacks that may have been registered
     * for this component.
     */
    protected void fireItemStateChanged(ItemEvent ie_) {
        if (_itemListeners != null) {
            for (Enumeration e = _itemListeners.elements();
                 e.hasMoreElements();) {

                ItemListener il = (ItemListener) e.nextElement();
                il.itemStateChanged(ie_);
            }
        }
    }

    /**
     * Returns the complete label string, including the mnemonic key
     * surrounded by parentheses (if there is a mnemonic key and the
     * mnemonic key does not appear within the label).
     */
    protected String getLabelString() {
        String label = getText();
        if (_mnemonic == 0)
            return label;	// no mnemonic is set

        if (_mnemonic >= ' ' &&
                _mnemonic < 0xff &&
                label.indexOf((char) _mnemonic) != -1) {
            return label;	// the mnemonic char appears within the label
        }

        StringBuffer buf = new StringBuffer(label);
        if (_mnemonic < ' ') {
            char c = (char) (_mnemonic + '@');
            return buf.append(" (^").append(c).append(")").toString();
        } else if (_mnemonic >= KeyEvent.VK_F1 && _mnemonic <= KeyEvent.VK_F20) {
            int offset = _mnemonic - KeyEvent.VK_F1 + 1;
            return buf.append(" (F").append(offset).append(")").toString();
        } else
            return label;   // At this stage we don't handle other chars
    }

    //====================================================================
    // INSTANCE VARIABLES

    private String _label;

    /**
     * The string that is sent in an ActionEvent when ENTER is pressed.
     */
    private String _actionCommand;

    private int _mnemonic = 0;

    /**
     * The state of the button. This is package-private because ButtonGroup
     * needs to be able to change the state of the button without calling
     * setSelected().
     */
    boolean _selected = false;

    /**
     * A list of ActionListeners registered for this component.
     */
    protected Vector _actionListeners = null;

    /**
     * A list of ItemListeners registered for this component.
     */
    protected Vector _itemListeners = null;
}