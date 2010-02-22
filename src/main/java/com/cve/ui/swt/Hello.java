package com.cve.ui.swt;

import org.eclipse.jface.window.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

public class Hello extends ApplicationWindow {

  public Hello() {
    super(null);
  }

    @Override
  protected Control createContents(Composite parent) {
    Button b = new Button(parent, SWT.PUSH);
    b.setText("Hello World");
    return b;
  }

  public static void main(String[] args) {
    Hello w = new Hello();
    w.setBlockOnOpen(true);
    w.open();
    Display.getCurrent().dispose();
  }
}
	