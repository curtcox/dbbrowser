
/*
 * ====================================================================
 *
 * The Clearthought Software License, Version 2.0
 *
 * Copyright (c) 2001 Daniel Barbalace.  All rights reserved.
 *
 * Project maintained at https://tablelayout.dev.java.net
 *
 * I. Terms for redistribution of original source and binaries
 *
 * Redistribution and use of unmodified source and/or binaries are
 * permitted provided that the following condition is met:
 *
 * 1. Redistributions of original source code must retain the above
 *    copyright notice and license.  You are not required to redistribute
 *    the original source; you may choose to redistribute only the
 *    binaries.
 *
 * Basically, if you distribute unmodified source, you meet
 * automatically comply with the license with no additional effort on
 * your part.
 *
 * II. Terms for distribution of derived works via subclassing and/or
 *     composition.
 *
 * You may generate derived works by means of subclassing and/or
 * composition (e.g., the Adaptor Pattern), provided that the following
 * conditions are met:
 *
 * 1. Redistributions of original source code must retain the above
 *    copyright notice and license.  You are not required to redistribute
 *    the original source; you may choose to redistribute only the
 *    binaries.
 *
 * 2. The original software is not altered.
 *
 * 3. Derived works are not contained in the info.clearthought
 *    namespace/package or any subpackage of info.clearthought.
 *
 * 4. Derived works do not use the class or interface names from the
 *    info.clearthought... packages
 *
 * For example, you may define a class with the following full name:
 *    org.nameOfMyOrganization.layouts.RowMajorTableLayout
 *
 * However, you may not define a class with the either of the
 * following full names:
 *    info.clearthought.layout.RowMajorTableLayout
 *    org.nameOfMyOrganization.layouts.TableLayout
 *
 * III. Terms for redistribution of source modified via patch files.
 *
 * You may generate derived works by means of patch files provided
 * that the following conditions are met:
 *
 * 1. Redistributions of original source code must retain the above
 *    copyright notice and license.  You are not required to
 *    redistribute the original source; you may choose to redistribute
 *    only the binaries resulting from the patch files.
 *
 * 2. The original source files are not altered.  All alteration is
 *    done in patch files.
 *
 * 3. Derived works are not contained in the info.clearthought
 *    namespace/package or any subpackage of info.clearthought.  This
 *    means that your patch files must change the namespace/package
 *    for the derived work.  See section II for examples.
 *
 * 4. Derived works do not use the class or interface names from the
 *    info.clearthought... packages.  This means your patch files
 *    must change the names of the interfaces and classes they alter.
 *    See section II for examples.
 *
 * 5. Derived works must include the following disclaimer.
 *    "This work is derived from Clearthought's TableLayout,
 *     https://tablelayout.dev.java.net, by means of patch files
 *     rather than subclassing or composition.  Therefore, this work
 *     might not contain the latest fixes and features of TableLayout."
 *
 * IV. Terms for repackaging, transcoding, and compiling of binaries.
 *
 * You may do any of the following with the binaries of the
 * original software.
 *
 * 1. You may move binaries (.class files) from the original .jar file
 *    to your own .jar file.
 *
 * 2. You may move binaries from the original .jar file to other
 *    resource containing files, including but not limited to .zip,
 *    .gz, .tar, .dll, .exe files.
 *
 * 3. You may backend compile the binaries to any platform, including
 *    but not limited to Win32, Win64, MAC OS, Linux, Palm OS, any
 *    handheld or embedded platform.
 *
 * 4. You may transcribe the binaries to other virtual machine byte
 *    code protocols, including but not limited to .NET.
 *
 * V. License Disclaimer.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR, AFFILATED BUSINESSES,
 * OR ANYONE ELSE BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 */

package com.cve.ui.layout;

import com.cve.ui.layout.UILayout.Constraint;
import java.util.*;
import static com.cve.ui.layout.TableLayout.Justification.*;


/**
 * TableLayoutConstraints binds components to their constraints.
 *
 * @version 2.0 March 14, 2004
 * @author  Daniel E. Barbalace
 */

public final class TableLayoutConstraints implements Constraint {


/** Cell in which the upper left corner of the component lays */
public final int col1, row1;

/** Cell in which the lower right corner of the component lays */
public final int col2, row2;

/** Horizontal justification if component occupies just one cell */
public final TableLayout.Justification hAlign;

/** Vertical justification if component occupies just one cell */
public final TableLayout.Justification vAlign;



/**
 * Constructs an TableLayoutConstraints with the default settings.  This
 * constructor is equivalent to TableLayoutConstraints(0, 0, 0, 0, FULL, FULL).
 */

private TableLayoutConstraints ()
{
    col1 = 0;
    row1 = 0;
    col2 = 0;
    row2 = 0;
    hAlign = vAlign = TableLayout.Justification.FULL;
}



/**
 * Constructs an TableLayoutConstraints a set of constraints.
 *
 * @param col      column where the component is placed
 * @param row      row where the component is placed
 */

private TableLayoutConstraints (int col, int row)
{
	this(col, row, col, row, FULL, FULL);
}



/**
 * Constructs an TableLayoutConstraints a set of constraints.
 *
 * @param col1      column where upper-left corner of the component is placed
 * @param row1      row where upper-left corner of the component is placed
 * @param col2      column where lower-right corner of the component is placed
 * @param row2      row where lower-right corner of the component is placed
 */

private TableLayoutConstraints (int col1, int row1, int col2, int row2)
{
	this(col1, row1, col2, row2, FULL, FULL);
}



/**
 * Constructs an TableLayoutConstraints a set of constraints.
 *
 * @param col1      column where upper-left corner of the component is placed
 * @param row1      row where upper-left corner of the component is placed
 * @param col2      column where lower-right corner of the component is placed
 * @param row2      row where lower-right corner of the component is placed
 * @param hAlign    horizontal justification of a component in a single cell
 * @param vAlign    vertical justification of a component in a single cell
 */

private TableLayoutConstraints
    (int col1, int row1, int col2, int row2, TableLayout.Justification hAlign, TableLayout.Justification vAlign)
{
    this.col1 = col1;
    this.row1 = row1;
    this.col2 = col2;
    this.row2 = row2;

    if ((hAlign == LEFT) ||
        (hAlign == RIGHT) ||
        (hAlign == CENTER) ||
        (hAlign == FULL) ||
        (hAlign == LEADING) ||
        (hAlign == TRAILING))
    {
        this.hAlign = hAlign;
    } else {
        this.hAlign = FULL;
    }

	if ((vAlign == LEFT) ||
		(vAlign == RIGHT) ||
		(vAlign == CENTER))
	{
		this.vAlign = vAlign;
	} else {
        this.vAlign = FULL;
    }
}

public static TableLayoutConstraints of(int col1, int row1) {
    return new TableLayoutConstraints(col1,row1);
}

public static TableLayoutConstraints of(int col1, int row1, int col2, int row2) {
    return new TableLayoutConstraints(col1,row1,col2,row2);
}

public static TableLayoutConstraints of(int col1, int row1, int col2, int row2, TableLayout.Justification hAlign, TableLayout.Justification vAlign) {
    return new TableLayoutConstraints(col1,row1,col2,row2,hAlign,vAlign);
}

public static TableLayoutConstraints of(int col1, int row1, TableLayout.Justification hAlign, TableLayout.Justification vAlign) {
    return new TableLayoutConstraints(col1,row1,col1,row1,hAlign,vAlign);
}

public static TableLayoutConstraints of(String string) {
    return new TableLayoutConstraints(string);
}


/**
 * Constructs an TableLayoutConstraints from a string.
 *
 * @param constraints    indicates TableLayoutConstraints's position and
 *                       justification as a string in the form "column, row" or
 *                       "column, row, horizontal justification, vertical
 *                       justification" or "column 1, row 1, column 2, row 2"
 *                       or "column 1, row 1, column 2, row 2, horizontal
 *                       justification, vertical justification".
 *                       It is also acceptable to delimit the parameters with
 *                       spaces instead of commas.
 */

private TableLayoutConstraints (String constraints) {
    // Use default values for any parameter not specified or specified
    // incorrectly.  The default parameters place the component in a single
    // cell at column 0, row 0.  The component is fully justified.
    int c1 = 0;
    int r1 = 0;
    int c2 = 0;
    int r2 = 0;
    TableLayout.Justification h = TableLayout.Justification.FULL;
    TableLayout.Justification v = TableLayout.Justification.FULL;

    // Parse constraints using spaces or commas
    StringTokenizer st = new StringTokenizer(constraints, ", ");
    int numToken = st.countTokens();

    try {
        // Check constraints
        if ((numToken != 2) && (numToken != 4) && (numToken != 6))
            throw new RuntimeException();

        // Get the first column (assume component is in only one column)
        String tokenA = st.nextToken();
        c1 = new Integer(tokenA).intValue();
        c2 = c1;

        // Get the first row (assume component is in only one row)
        String tokenB = st.nextToken();
        r1 = new Integer(tokenB).intValue();
        r2 = r1;

        // Get next two tokens
        tokenA = st.nextToken();
        tokenB = st.nextToken();

        try {
            // Attempt to use tokens A and B as col2 and row2
            c2 = new Integer(tokenA).intValue();
            r2 = new Integer(tokenB).intValue();

            // Get next two tokens
            tokenA = st.nextToken();
            tokenB = st.nextToken();
        } catch (NumberFormatException error) {
            c2 = c1;
            r2 = r1;
        }

        h = TableLayout.Justification.parse(tokenA);
        v = TableLayout.Justification.parse(tokenB);
    } catch (NoSuchElementException error) {
    } catch (RuntimeException error) {
        throw new IllegalArgumentException
            ("Expected constraints in one of the following formats:\n" +
             "  col1, row1\n  col1, row1, col2, row2\n" +
             "  col1, row1, hAlign, vAlign\n" +
             "  col1, row1, col2, row2, hAlign, vAlign\n" +
             "Constraints provided '" + constraints + "'");
    }

    // Make sure row2 >= row1
    if (r2 < r1) {
        r2 = r1;
    }

    // Make sure col2 >= col1
    if (c2 < c1) {
        c2 = c1;
    }

    col1 = c1;
    col2 = c2;
    row1 = r1;
    row2 = r2;
    hAlign = h;
    vAlign = v;
}



/**
 * Gets a string representation of this TableLayoutConstraints.
 *
 * @return a string in the form "row 1, column 1, row 2, column 2, horizontal
 *         justification, vertical justification"
 */

    @Override
public String toString () {

    return col1 + ", " + row1 + ", " + col2 + ", " + row2 + ", " +
           hAlign.toString().toLowerCase()+ ", " +
           vAlign.toString().toLowerCase();
}



}
