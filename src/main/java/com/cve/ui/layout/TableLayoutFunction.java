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

import com.cve.ui.layout.TableLayout.Justification;
import com.cve.ui.layout.UILayout.Bounds;
import com.cve.ui.layout.UILayout.Component;
import com.cve.ui.layout.UILayout.Constraint;
import com.cve.ui.layout.UILayout.Container;
import com.cve.ui.layout.UILayout.Dimension;
import com.cve.ui.layout.UILayout.Insets;
import com.cve.ui.layout.UILayout.Orientation;
import com.cve.util.Check;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import static com.cve.ui.layout.TableLayoutConstants.*;


/**
 * Functional version of TableLayout
 */

public final class TableLayoutFunction implements
    UILayout.Function
{



/*
    Note: In this file, a cr refers to either a column or a row.  cr[C] always
    means column and cr[R] always means row.  A cr size is either a column
    width or a row Height.  TableLayout views columns and rows as being
    conceptually symmetric.  Therefore, much of the code applies to both
    columns and rows, and the use of the cr terminology eliminates redundancy.
    Also, for ease of reading, z always indicates a parameter whose value is
    either C or R.
*/



/** Default row/column size */
static final double defaultSize[][] = {{}, {}};

// TODO: rather than use 0,1 we should use an enum to make how this works clearer.
/** Indicates a column */
static final int C = 0;

/** Indicates a row */
static final int R = 1;

/**
 * Sizes of crs expressed in absolute and relative terms
 */
final double crSpec[][] = {null, null};

/**
 * Sizes of crs in pixels
 */
int crSize[][] = {null, null};

/**
 * Offsets of crs in pixels.  The left boarder of column n is at
 crOffset[C][n] and the right boarder is at cr[C][n + 1] for all
 columns including the last one.  crOffset[C].length = crSize[C].length + 1
 */
int crOffset[][] = {null, null};



/**
 * List of components and their sizes
 */
Map<Component,Entry> entries;


/**
 * Horizontal gap between columns in pixels
 */
final int hGap;

/**
 * Vertical gap between rows in pixels
 */
final int vGap;


//******************************************************************************
//** Constructors                                                            ***
//******************************************************************************



/**
 * Constructs an instance of TableLayout.  This TableLayout will have no columns
 * or rows.  This constructor is most useful for bean-oriented programming and
 * dynamically adding columns and rows.
 */

private TableLayoutFunction () {
    init(defaultSize[C], defaultSize[R]);
    hGap = 0;
    vGap = 0;
}



/**
 * Constructs an instance of TableLayout.
 *
 * @param size    widths of columns and heights of rows in the format,
 *                {{col0, col1, col2, ..., colN}, {row0, row1, row2, ..., rowM}}
 *                If this parameter is invalid, the TableLayout will have
 *                exactly one row and one column.
 */

private TableLayoutFunction (double [][] size) {
    // Make sure columns and rows and nothing else is specified
    if ((size != null) && (size.length == 2)) {
        init(size[C], size[R]);
        hGap = 0;
        vGap = 0;
        return;
    }
    String message =
        "Parameter size should be an array, a[2], where a[0] is the " +
         "is an array of column widths and a[1] is an array or row " +
         "heights.";
    throw new IllegalArgumentException(message);
}

/**
 * Constructs an instance of TableLayout.
 *
 * @param size    widths of columns and heights of rows in the format,
 *                {{col0, col1, col2, ..., colN}, {row0, row1, row2, ..., rowM}}
 *                If this parameter is invalid, the TableLayout will have
 *                exactly one row and one column.
 */

public static TableLayoutFunction of(double [][] size) {
    return new TableLayoutFunction(size);
}


/**
 * Constructs an instance of TableLayout.
 *
 * @param col    widths of columns in the format, {{col0, col1, col2, ..., colN}
 * @param row    heights of rows in the format, {{row0, row1, row2, ..., rowN}
 */

private TableLayoutFunction (double [] col, double [] row) {
    init(col, row);
    hGap = 0;
    vGap = 0;
}

/**
 * Constructs an instance of TableLayout.
 *
 * @param col    widths of columns in the format, {{col0, col1, col2, ..., colN}
 * @param row    heights of rows in the format, {{row0, row1, row2, ..., rowN}
 */
public static TableLayoutFunction of(double [] col, double [] row) {
    return new TableLayoutFunction(col,row);
}

public static TableLayoutFunction of(int col, int row) {
    return new TableLayoutFunction(fillArray(col),fillArray(row));
}

/**
 * Return an array of size + 2.  It will contain all size elements except at the ends.
 * a[] = border, fill, fill, ... fill, fill, border
 */
private static double[] fillArray(int size) {
    double[] a = new double[size + 2];
    double border = 10;
    a[0] = border;
    a[size+1] = border;
    for (int i=0; i<size; i++) {
        a[i+1] = TableLayoutConstants.PREFERRED;
    }
    return a;
}

/**
 * Initializes the TableLayout for all constructors.
 *
 * @param col    widths of columns in the format, {{col0, col1, col2, ..., colN}
 * @param row    heights of rows in the format, {{row0, row1, row2, ..., rowN}
 */

private void init (double [] col, double [] row) {
    col = Check.notNull(col);
    row = Check.notNull(row);

    // Create new rows and columns
    crSpec[C] = new double[col.length];
    crSpec[R] = new double[row.length];

    // Copy rows and columns
    System.arraycopy(col, 0, crSpec[C], 0, crSpec[C].length);
    System.arraycopy(row, 0, crSpec[R], 0, crSpec[R].length);

    // Make sure rows and columns are valid
    for (int counter = 0; counter < crSpec[C].length; counter++) {
        double v = crSpec[C][counter];
        if ((v < 0.0) && (v != FILL) && (v != PREFERRED) && (v != MINIMUM)) {
            crSpec[C][counter] = 0.0;
        }
    }

    for (int counter = 0; counter < crSpec[R].length; counter++) {
        double v = crSpec[R][counter];
        if ((v < 0.0) && (v != FILL) && (v != PREFERRED) && (v != MINIMUM)) {
            crSpec[R][counter] = 0.0;
        }
    }

    // Create an empty list of components
    entries = Maps.newHashMap();

}



//******************************************************************************
//** Get/Set methods                                                         ***
//******************************************************************************



/**
 * Gets the constraints of a given component.
 *
 * @param component    desired component
 *
 * @return If the given component is found, the constraints associated with
 *         that component.  If the given component is null or is not found,
 *         null is returned.
 */

TableLayoutConstraints getConstraints(Component component) {
    Entry entry = Check.notNull(entries.get(component));
    return TableLayoutConstraints.of
        (entry.cr1[C], entry.cr1[R], entry.cr2[C], entry.cr2[R],
         entry.alignment[C], entry.alignment[R]);
}



/**
 * Sets the constraints of a given component.
 *
 * @param component     desired component.  This parameter cannot be null.
 * @param constraint    new set of constraints.  This parameter cannot be null.
 */

void setConstraints(Component component, TableLayoutConstraints constraint) {
    Check.notNull(component);
    Check.notNull(constraint);
    entries.put(component, new Entry(component, constraint));
}



/**
 * Adjusts the number and sizes of rows in this layout.  After calling this
 * method, the caller should request this layout manager to perform the
 * layout.  This can be done with the following code:
 *
 * <pre>
 *     layout.layoutContainer(container);
 *     container.repaint();
 * </pre>
 *
 * or
 *
 * <pre>
 *     window.pack()
 * </pre>
 *
 * If this is not done, the changes in the layout will not be seen until the
 * container is resized.
 *
 * @param column    widths of each of the columns
 *
 * @see #getColumn
 */

void setColumn (double column[]) {
    setCr(C, column);
}



/**
 * Adjusts the number and sizes of rows in this layout.  After calling this
 * method, the caller should request this layout manager to perform the
 * layout.  This can be done with the following code:
 *
 * <code>
 *     layout.layoutContainer(container);
 *     container.repaint();
 * </code>
 *
 * or
 *
 * <pre>
 *     window.pack()
 * </pre>
 *
 * If this is not done, the changes in the layout will not be seen until the
 * container is resized.
 *
 * @param row    heights of each of the rows.  This parameter cannot be null.
 *
 * @see #getRow
 */

void setRow (double row[]) {
    setCr(R, row);
}



/**
 * Sets the sizes of rows or columns for the methods setRow or setColumn.
 *
 * @param z       indicates row or column
 * @param size    new cr size
 */

void setCr (int z, double size[]) {
    // Copy crs
    crSpec[z] = new double[size.length];
    System.arraycopy(size, 0, crSpec[z], 0, crSpec[z].length);

    // Make sure rows are valid
    for (int counter = 0; counter < crSpec[z].length; counter++) {
        double v = crSpec[z][counter];
        if ((v < 0.0) && (v != FILL) && (v != PREFERRED) && (v != MINIMUM)) {
            crSpec[z][counter] = 0.0;
        }
    }
}



/**
 * Adjusts the width of a single column in this layout.  After calling this
 * method, the caller should request this layout manager to perform the
 * layout.  This can be done with the following code:
 *
 * <code>
 *     layout.layoutContainer(container);
 *     container.repaint();
 * </code>
 *
 * or
 *
 * <pre>
 *     window.pack()
 * </pre>
 *
 * If this is not done, the changes in the layout will not be seen until the
 * container is resized.
 *
 * @param i       zero-based index of column to set.  If this parameter is not
 *                valid, an ArrayOutOfBoundsException will be thrown.
 * @param size    width of the column.  This parameter cannot be null.
 *
 * @see #getColumn
 */

void setColumn (int i, double size) {
    setCr(C, i, size);
}



/**
 * Adjusts the height of a single row in this layout.  After calling this
 * method, the caller should request this layout manager to perform the
 * layout.  This can be done with the following code:
 *
 * <code>
 *     layout.layoutContainer(container);
 *     container.repaint();
 * </code>
 *
 * or
 *
 * <pre>
 *     window.pack()
 * </pre>
 *
 * If this is not done, the changes in the layout will not be seen until the
 * container is resized.
 *
 * @param i       zero-based index of row to set.  If this parameter is not
 *                valid, an ArrayOutOfBoundsException will be thrown.
 * @param size    height of the row.  This parameter cannot be null.
 *
 * @see #getRow
 */

void setRow (int i, double size) {
    setCr(R, i, size);
}



/**
 * Sets the sizes of rows or columns for the methods setRow or setColumn.
 *
 * @param z       indicates row or column
 * @param i       indicates which cr to resize
 * @param size    new cr size
 */
void setCr (int z, int i, double size) {
    // Make sure size is valid
    if ((size < 0.0) && (size != FILL) && (size != PREFERRED) && (size != MINIMUM)) {
        size = 0.0;
    }

    // Copy new size
    crSpec[z][i] = size;
}



/**
 * Gets the sizes of columns in this layout.
 *
 * @return widths of each of the columns
 *
 * @see #setColumn
 */
double [] getColumn () {
    // Copy columns
    double column[] = new double[crSpec[C].length];
    System.arraycopy(crSpec[C], 0, column, 0, column.length);

    return column;
}



/**
 * Gets the height of a single row in this layout.
 *
 * @return height of the requested row
 *
 * @see #setRow
 */
double [] getRow () {
    // Copy rows
    double row[] = new double[crSpec[R].length];
    System.arraycopy(crSpec[R], 0, row, 0, row.length);

    return row;
}



/**
 * Gets the width of a single column in this layout.
 *
 * @param i    zero-based index of row to get.  If this parameter is not valid,
 *             an ArrayOutOfBoundsException will be thrown.
 *
 * @return width of the requested column
 *
 * @see #setRow
 */
double getColumn (int i) {
    return crSpec[C][i];
}



/**
 * Gets the sizes of a row in this layout.
 *
 * @param i    zero-based index of row to get.  If this parameter is not valid,
 *             an ArrayOutOfBoundsException will be thrown.
 *
 * @return height of each of the requested row
 *
 * @see #setRow
 */
double getRow (int i) {
    return crSpec[R][i];
}



/**
 * Gets the number of columns in this layout.
 *
 * @return the number of columns
 */
int getNumColumn() {
    return crSpec[C].length;
}



/**
 * Gets the number of rows in this layout.
 *
 * @return the number of rows
 */
int getNumRow() {
    return crSpec[R].length;
}


//******************************************************************************
//** Insertion/Deletion methods                                              ***
//******************************************************************************



/**
 * Inserts a column in this layout.  All components to the right of the
 * insertion point are moved right one column.  The container will need to
 * be laid out after this method returns.  See <code>setColumn</code>.
 *
 * @param i       zero-based index at which to insert the column
 * @param size    size of the column to be inserted
 *
 * @see #setColumn
 * @see #deleteColumn
 */
void insertColumn (int i, double size) {
    insertCr(C, i, size);
}



/**
 * Inserts a row in this layout.  All components below the insertion point
 * are moved down one row.  The container will need to be laid out after this
 * method returns.  See <code>setRow</code>.
 *
 * @param i       zero-based index at which to insert the row
 * @param size    size of the row to be inserted
 *
 * @see #setRow
 * @see #deleteRow
 */
void insertRow (int i, double size) {
    insertCr(R, i, size);
}



/**
 * Inserts a cr for the methods insertRow or insertColumn.
 *
 * @param z       indicates row or column
 * @param i       zero-based index at which to insert the cr
 * @param size    size of cr being inserted
 */
void insertCr (int z, int i, double size) {
    // Make sure position is valid
    if ((i < 0) || (i > crSpec[z].length))
        throw new IllegalArgumentException
            ("Parameter i is invalid.  i = " + i + ".  Valid range is [0, " +
             crSpec[z].length + "].");

    // Make sure row size is valid
    if ((size < 0.0) && (size != FILL) && (size != PREFERRED) && (size != MINIMUM)) {
        size = 0.0;
    }

    // Copy crs
    double cr[] = new double[crSpec[z].length + 1];
    System.arraycopy(crSpec[z], 0, cr, 0, i);
    System.arraycopy(crSpec[z], i, cr, i + 1, crSpec[z].length - i);

    // Insert cr
    cr[i] = size;
    crSpec[z] = cr;

    for (Entry entry : entries.values()) {

        // Is the first cr below the new cr
        if (entry.cr1[z] >= i) {
            // Move first cr
            entry.cr1[z]++;
        }
        // Is the second cr below the new cr
        if (entry.cr2[z] >= i) {
            // Move second cr
            entry.cr2[z]++;
        }
    }

}



/**
 * Deletes a column in this layout.  All components to the right of the
 * deletion point are moved left one column.  The container will need to
 * be laid out after this method returns.  See <code>setColumn</code>.
 *
 * @param i    zero-based index of column to delete
 *
 * @see #setColumn
 * @see #deleteColumn
 */
void deleteColumn (int i) {
    deleteCr(C, i);
}



/**
 * Deletes a row in this layout.  All components below the deletion point are
 * moved up one row.  The container will need to be laid out after this method
 * returns.  See <code>setRow</code>.  There must be at least two rows in order
 * to delete a row.
 *
 * @param i    zero-based index of row to delete
 *
 * @see #setRow
 * @see #deleteRow
 */
void deleteRow (int i) {
    deleteCr(R, i);
}



/**
 * Deletes a cr for the methods deleteRow or deleteColumn.
 *
 * @param z       indicates row or column
 * @param i       zero-based index of cr to delete
 */
void deleteCr (int z, int i) {
    // Make sure position is valid
    if ((i < 0) || (i >= crSpec[z].length)) {
        throw new IllegalArgumentException
            ("Parameter i is invalid.  i = " + i + ".  Valid range is [0, " +
             (crSpec[z].length - 1) + "].");
    }

    // Copy rows
    double cr[] = new double[crSpec[z].length - 1];
    System.arraycopy(crSpec[z], 0, cr, 0, i);
    System.arraycopy(crSpec[z], i + 1, cr, i, crSpec[z].length - i - 1);

    // Delete row
    crSpec[z] = cr;

    for (Entry entry : entries.values()) {
        // Is the first row below the new row
        if (entry.cr1[z] > i) {
            // Move first row
            entry.cr1[z]--;
        }

        // Is the second row below the new row
        if (entry.cr2[z] > i) {
            // Move second row
            entry.cr2[z]--;
        }
    }

}



//******************************************************************************
//** Misc methods                                                            ***
//******************************************************************************



/**
 * Converts this TableLayout to a string.
 *
 * @return a string representing the columns and row sizes in the form
 *         "{{col0, col1, col2, ..., colN}, {row0, row1, row2, ..., rowM}}"
 */

@Override
public String toString () {
    int counter;
    String value = "TableLayout {{";

    if (crSpec[C].length > 0) {
        for (counter = 0; counter < crSpec[C].length - 1; counter++) {
            value += crSpec[C][counter] + ", ";
        }
        value += crSpec[C][crSpec[C].length - 1] + "}, {";
    } else {
        value += "}, {";
    }

    if (crSpec[R].length > 0) {
        for (counter = 0; counter < crSpec[R].length - 1; counter++) {
            value += crSpec[R][counter] + ", ";
        }
        value += crSpec[R][crSpec[R].length - 1] + "}}";
    } else {
        value += "}}";
    }

    return value;
}



/**
 * Determines whether or not there are any components with invalid constraints.
 * An invalid constraint is one that references a non-existing row or column.
 * For example, on a table with five rows, row -1 and row 5 are both invalid.
 * Valid rows are 0 through 4, inclusively.  This method is useful for
 * debugging.
 *
 * @return a list of TableLayout.Entry instances referring to the invalid
 *         constraints and corresponding components
 *
 * @see #getOverlappingEntry
 */

java.util.List<Entry> getInvalidEntry () {
    LinkedList<Entry> listInvalid = new LinkedList();
    try {
        for (Entry entry : entries.values()) {
            if ((entry.cr1[R] < 0) || (entry.cr1[C] < 0) ||
                (entry.cr2[R] >= crSpec[R].length) ||
                (entry.cr2[C] >= crSpec[C].length))
            {
                listInvalid.add((Entry) entry.copy());
            }
        }
    } catch (CloneNotSupportedException error) {
        throw new RuntimeException("Unexpected CloneNotSupportedException");
    }

    return listInvalid;
}



/**
 * Gets a list of overlapping components and their constraints.  Two
 * components overlap if they cover at least one common cell.  This method is
 * useful for debugging.
 *
 * @return a list of zero or more TableLayout.Entry instances
 *
 * @see #getInvalidEntry
 */

java.util.List<Entry> getOverlappingEntry () {

    LinkedList<Entry> listOverlapping = new LinkedList();

    try {
        // Count constraints
        int numEntry = entries.size();

        // If there are no components, they can't be overlapping
        if (numEntry == 0)
            return listOverlapping;

        // Put entries in an array
        Entry entry[] = (Entry []) entries.values().toArray(new Entry[numEntry]);

        // Check all components
        for (int knowUnique = 1; knowUnique < numEntry; knowUnique++) {
            for (int checking = knowUnique - 1; checking >= 0; checking--) {
                if(
                    (
                     (entry[checking].cr1[C] >= entry[knowUnique].cr1[C]) &&
                     (entry[checking].cr1[C] <= entry[knowUnique].cr2[C]) &&
                     (entry[checking].cr1[R] >= entry[knowUnique].cr1[R]) &&
                     (entry[checking].cr1[R] <= entry[knowUnique].cr2[R])
                    )
                    ||
                    (
                     (entry[checking].cr2[C] >= entry[knowUnique].cr1[C]) &&
                     (entry[checking].cr2[C] <= entry[knowUnique].cr2[C]) &&
                     (entry[checking].cr2[R] >= entry[knowUnique].cr1[R]) &&
                     (entry[checking].cr2[R] <= entry[knowUnique].cr2[R])
                    )
                   )
                {
                    listOverlapping.add((Entry) entry[checking].copy());
                }
            }
        }
    } catch (CloneNotSupportedException error) {
        throw new RuntimeException("Unexpected CloneNotSupportedException");
    }

    return listOverlapping;
}



//******************************************************************************
//** Calculation methods                                                     ***
//******************************************************************************



/**
 * Calculates the sizes of the rows and columns based on the absolute and
 * relative sizes specified in <code>crSpec[R]</code> and <code>crSpec[C]</code>
 * and the size of the container.  The result is stored in <code>crSize[R]</code>
 * and <code>crSize[C]</code>.
 *
 * @param container    container using this TableLayout
 */

void calculateSize (ImmutableList<Component> components, Dimension d) {

    int availableWidth = d.width;
    int availableHeight = d.height;

    // Compensate for horiztonal and vertical gaps
    if (crSpec[C].length > 0) {
        availableWidth -= hGap * (crSpec[C].length - 1);
    }

    if (crSpec[R].length > 0) {
        availableHeight -= vGap * (crSpec[R].length - 1);
    }

    // Create array to hold actual sizes in pixels
    crSize[C] = new int[crSpec[C].length];
    crSize[R] = new int[crSpec[R].length];

    // Assign absolute sizes (must be done before assignPrefMinSize)
    availableWidth = assignAbsoluteSize(C, availableWidth);
    availableHeight = assignAbsoluteSize(R, availableHeight);

    // Assign preferred and minimum sizes (must be done after assignAbsoluteSize)
    availableWidth = assignPrefMinSize(C, availableWidth, MINIMUM);
    availableWidth = assignPrefMinSize(C, availableWidth, PREFERRED);
    availableHeight = assignPrefMinSize(R, availableHeight, MINIMUM);
    availableHeight = assignPrefMinSize(R, availableHeight, PREFERRED);

    // Assign relative sizes
    availableWidth = assignRelativeSize(C, availableWidth);
    availableHeight = assignRelativeSize(R, availableHeight);

    // Assign fill sizes
    assignFillSize(C, availableWidth);
    assignFillSize(R, availableHeight);

}



/**
 * Assigns absolute sizes.
 *
 * @param z                indicates row or column
 * @param availableSize    amount of space available in the container
 *
 * @return the amount of space available after absolute crs have been assigned
 *         sizes
 */

int assignAbsoluteSize (int z, int availableSize) {
    int numCr = crSpec[z].length;

    for (int counter = 0; counter < numCr; counter++) {
        if ((crSpec[z][counter] >= 1.0) || (crSpec[z][counter] == 0.0))
        {
            crSize[z][counter] = (int) (crSpec[z][counter] + 0.5);
            availableSize -= crSize[z][counter];
        }
    }

    return availableSize;
}



/**
 * Assigns relative sizes.
 *
 * @param z                indicates row or column
 * @param availableSize    amount of space available in the container
 *
 * @return the amount of space available after relative crs have been assigned
 *         sizes
 */
int assignRelativeSize (int z, int availableSize) {
    int relativeSize = (availableSize < 0) ? 0 : availableSize;
    int numCr = crSpec[z].length;

    for (int counter = 0; counter < numCr; counter++) {
        if ((crSpec[z][counter] > 0.0) && (crSpec[z][counter] < 1.0))
        {
            crSize[z][counter] =
                (int) (crSpec[z][counter] * relativeSize + 0.5);

            availableSize -= crSize[z][counter];
        }
    }
    return availableSize;
}



/**
 * Assigns FILL sizes.
 *
 * @param z                indicates row or column
 * @param availableSize    amount of space available in the container
 */
void assignFillSize (int z, int availableSize) {
    // Skip if there is no more space to allocate
    if (availableSize <= 0) {
        return;
    }
    // Count the number of "fill" cells
    int numFillSize = 0;
    int numCr = crSpec[z].length;

    for (int counter = 0; counter < numCr; counter++) {
        if (crSpec[z][counter] == FILL)
            numFillSize++;
    }

    // If numFillSize is zero, the if statement below will always evaluate to
    // false and the division will not occur.

    // If there are more than one "fill" cell, slack may occur due to rounding
    // errors
    int slackSize = availableSize;

    // Assign "fill" cells equal amounts of the remaining space
    for (int counter = 0; counter < numCr; counter++) {
        if (crSpec[z][counter] == FILL)
        {
            crSize[z][counter] = availableSize / numFillSize;
            slackSize -= crSize[z][counter];
        }
    }
    // Assign one pixel of slack to each FILL cr, starting at the last one,
    // until all slack has been consumed
    for (int counter = numCr - 1; (counter >= 0) && (slackSize > 0); counter--)
    {
        if (crSpec[z][counter] == FILL)
        {
            crSize[z][counter]++;
            slackSize--;
        }
    }
}



/**
 * Assigned sizes to preferred and minimum size columns and rows.  This
 * reduces the available width and height.  Minimum widths/heights must be
 * calculated first because they affect preferred widths/heights, but not vice
 * versa.  The end result is that any component contained wholly or partly in
 * a column/row of minimum/preferred width or height will get at least its
 * minimum/preferred width or height, respectively.
 *
 * @param z                indicates row or column
 * @param availableSize    amount of space available in the container
 * @param typeOfSize       indicates preferred or minimum
 *
 * @return the amount of space available after absolute crs have been assigned
 *         sizes
 */

int assignPrefMinSize(int z, int availableSize, double typeOfSize) {
    // Get variables referring to columns or rows (crs)
    int numCr = crSpec[z].length;

    // Address every cr
    for (int counter = 0; counter < numCr; counter++)
        // Is the current cr a preferred/minimum (based on typeOfSize) size
        if (crSpec[z][counter] == typeOfSize) {
            // Assume a maximum width of zero
            int maxSize = 0;

            // Find maximum preferred/min width of all components completely
            // or partially contained within this cr
            Iterator<Entry> iterator = entries.values().iterator();

            nextComponent:
            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();

                // Skip invalid entries
                if ((entry.cr1[z] < 0) || (entry.cr2[z] >= numCr))
                    continue nextComponent;

                // Find the maximum desired size of this cr based on all crs
                // the current component occupies
                if ((entry.cr1[z] <= counter) && (entry.cr2[z] >= counter)) {
                    // Setup size and number of adjustable crs
                    Dimension p = (typeOfSize == PREFERRED) ?
                        entry.component.getPreferredSize() :
                        entry.component.getMinimumSize();

                    int size = (p == null) ? 0 :
                        ((z == C) ? p.width : p.height);
                    int numAdjustable = 0;

                    // Calculate for preferred size
                    if (typeOfSize == PREFERRED) {
                        // Consider all crs this component occupies
                        for (int entryCr = entry.cr1[z]; entryCr <= entry.cr2[z]; entryCr++) {
                            // Subtract absolute, relative, and minumum cr
                            // sizes, which have already been calculated
                            if ((crSpec[z][entryCr] >= 0.0) || (crSpec[z][entryCr] == MINIMUM)) {
                                size -= crSize[z][entryCr];
                            } else if (crSpec[z][entryCr] == PREFERRED) {
                            // Count preferred/min width columns
                                numAdjustable++;
                            // Skip any component that occupies a fill cr
                            // because the fill should fulfill the size
                            // requirements
                            } else if (crSpec[z][entryCr] == FILL) {
                                continue nextComponent;
                            }
                        }
                    // Calculate for minimum size
                    } else {
                        // Consider all crs this component occupies
                        for (int entryCr = entry.cr1[z]; entryCr <= entry.cr2[z]; entryCr++) {
                            // Subtract absolute and relative cr sizes, which
                            // have already been calculated
                            if (crSpec[z][entryCr] >= 0.0)
                                size -= crSize[z][entryCr];
                            // Count preferred/min width columns
                            else if ((crSpec[z][entryCr] == PREFERRED) ||
                                     (crSpec[z][entryCr] == MINIMUM))
                            {
                                numAdjustable++;
                            }
                            // Skip any component that occupies a fill cr
                            // because the fill should fulfill the size
                            // requirements
                            else if (crSpec[z][entryCr] == FILL)
                                continue nextComponent;
                        }
                    }
                    // Adjust remaining size by eliminating space for gaps between the crs this component occupies
                    int numCrOccupiedByThisComponent = entry.cr2[z] - entry.cr1[z] + 1;

                    if (numCrOccupiedByThisComponent > 1) {
                        int gap = (z == 0) ? hGap : vGap;
                        size -= (numCrOccupiedByThisComponent - 1) * gap;
                    }

                    // Divide the size evenly among the adjustable crs
                    size = (int) Math.ceil(size / (double) numAdjustable);

                    // Take the maximumn size
                    if (maxSize < size) {
                        maxSize = size;
                    }
                }
            }

            // Assign preferred size
            crSize[z][counter] = maxSize;

            // Reduce available size
            availableSize -= maxSize;
        }

    return availableSize;
}



/**
 * To lay out the specified container using this layout.  This method reshapes
 * the components in the specified target container in order to satisfy the
 * constraints of all components.
 *
 * <p>User code should not have to call this method directly.</p>
 *
 * @param container    container being served by this layout manager
 */

@Override
public ImmutableMap<Component, Bounds> layout(
    ImmutableList<Component> components, ImmutableMap<Component, Constraint> constraints, Dimension dim)
{
    Map<Component, Bounds> bounds = Maps.newHashMap();
    calculateSize(components,dim);

    // Get component orientation and insets
    Orientation co = null; // container.getComponentOrientation();
    boolean isRightToLeft = (co != null) && !co.leftToRight;

    // Layout components
    for (Component component : components) {
        layout(component, dim,isRightToLeft);
    }
    return ImmutableMap.copyOf(bounds);
}




void layout(Component component, Dimension d, boolean isRightToLeft) {
    Entry entry = Check.notNull(entries.get(component));

    // The following block of code has been optimized so that the
    // preferred size of the component is only obtained if it is
    // needed.  There are components in which the getPreferredSize
    // method is extremely expensive, such as data driven controls
    // with a large amount of data.

    // Get the preferred size of the component
    int preferredWidth = 0;
    int preferredHeight = 0;

    if ((entry.alignment[C] != Justification.FULL) || (entry.alignment[R] != Justification.FULL)) {
        Dimension preferredSize = component.getPreferredSize();

        preferredWidth = preferredSize.width;
        preferredHeight = preferredSize.height;
    }

    // Calculate the coordinates and size of the component
    int value[] = calculateSizeAndOffset(entry, preferredWidth, true);
    int x = value[0];
    int w = value[1];
    value = calculateSizeAndOffset(entry, preferredHeight, false);
    int y = value[0];
    int h = value[1];

    // Compensate for component orientation.
    if (isRightToLeft) {
        x = d.width - x - w;
    }
    // Move and resize component
    debug("Setting " + component + " " + x + ","+ y + w + h);
    component.setBounds(x, y, w, h);
}

void debug(String s) {
    //System.out.println(s);
}

/**
 * Calculates the vertical/horizontal offset and size of a component.
 *
 * @param entry    entry containing component and contraints
 * @param preferredSize    previously calculated preferred width/height of
 *                         component
 * @param isColumn         if true, this method is being called to calculate
 *                         the offset/size of a column.  if false,... of a row.
 *
 * @return an array, a, of two integers such that a[0] is the offset and
 *         a[1] is the size
 */

int [] calculateSizeAndOffset(Entry entry, int preferredSize, boolean isColumn) {
    // Get references to cr properties
    int crOffset[] = isColumn ? this.crOffset[C] : this.crOffset[R];
    TableLayout.Justification entryAlignment = isColumn ? entry.alignment[C] : entry.alignment[R];

    // Determine cell set size
    int cellSetSize = isColumn ?
        crOffset[entry.cr2[C] + 1] - crOffset[entry.cr1[C]] :
        crOffset[entry.cr2[R] + 1] - crOffset[entry.cr1[R]];

    // Determine the size of the component
    int size;

    if ((entryAlignment == Justification.FULL) || (cellSetSize < preferredSize))
        size = cellSetSize;
    else
        size = preferredSize;

    // Since the component orientation is adjusted for in the layoutContainer
    // method, we can treat leading justification as left justification and
    // trailing justification as right justification.
    if (isColumn && (entryAlignment == Justification.LEADING))
        entryAlignment = Justification.LEFT;

    if (isColumn && (entryAlignment == Justification.TRAILING))
        entryAlignment = Justification.RIGHT;

    // Determine offset
    int offset;

    switch (entryAlignment) {
        case LEFT : // Align left/top side along left edge of cell
            offset = crOffset[isColumn ? entry.cr1[C] : entry.cr1[R]];
        break;

        case RIGHT : // Align right/bottom side along right edge of cell
            offset =
                crOffset[(isColumn ? entry.cr2[C] : entry.cr2[R]) + 1] - size;
        break;

        case CENTER : // Center justify component
            offset = crOffset[isColumn ? entry.cr1[C] : entry.cr1[R]] +
                     ((cellSetSize - size) >> 1);
        break;

        case FULL : // Align left/top side along left/top edge of cell
            offset = crOffset[isColumn ? entry.cr1[C] : entry.cr1[R]];
        break;

        default : // This is a never should happen case, but just in case
            offset = 0;
    }

    // Compensate for gaps
    if (isColumn) {
        offset += hGap * entry.cr1[C];
		int cumlativeGap = hGap * (entry.cr2[C] - entry.cr1[C]);

		switch (entryAlignment) {
			case RIGHT :
				offset += cumlativeGap;
			break;

			case CENTER :
				offset += cumlativeGap >> 1;
			break;

			case FULL :
				size += cumlativeGap;
			break;
		}
    } else {
        offset += vGap * entry.cr1[R];
		int cumlativeGap = vGap * (entry.cr2[R] - entry.cr1[R]);

		switch (entryAlignment) {
			case BOTTOM :
				offset += cumlativeGap;
			break;

			case CENTER :
				offset += cumlativeGap >> 1;
			break;

			case FULL :
				size += cumlativeGap;
			break;
		}
    }

    // Package return values
    int value[] = {offset, size};
    return value;
}




/**
 * Calculates the preferred or minimum size for the methods preferredLayoutSize
 * and minimumLayoutSize.
 *
 * @param container     container whose size is being calculated
 * @param typeOfSize    indicates preferred or minimum
 *
 * @return a dimension indicating the container's preferred or minimum size
 */
Dimension calculateLayoutSize (ImmutableList<Component> components, double typeOfSize) {
    //  Get preferred/minimum sizes
    Entry[] entryList = (Entry []) entries.values().toArray(new Entry[entries.size()]);
    int numEntry = entryList.length;
    Dimension prefMinSize[] = new Dimension[numEntry];

    for (int i = 0; i < numEntry; i++) {
        prefMinSize[i] = (typeOfSize == PREFERRED) ?
            entryList[i].component.getPreferredSize() :
            entryList[i].component.getMinimumSize();
    }

    // Calculate sizes
    int width  = calculateLayoutSize(components, C, typeOfSize, entryList, prefMinSize);
    int height = calculateLayoutSize(components, R, typeOfSize, entryList, prefMinSize);

    return Dimension.of(width, height);
}



/**
 * Calculates the preferred or minimum size for the method
 * calculateLayoutSize(Container container, double typeOfSize).  This method
 * is passed the preferred/minimum sizes of the components so that the
 * potentially expensive methods getPreferredSize()/getMinimumSize() are not
 * called twice for the same component.
 *
 * @param container      container whose size is being calculated
 * @param z              indicates row or column
 * @param typeOfSize     indicates preferred or minimum
 * @param entryList      list of Entry objects
 * @param prefMinSize    list of preferred or minimum sizes
 *
 * @return a dimension indicating the container's preferred or minimum size
 */

int calculateLayoutSize
    (ImmutableList<Component> components, int z, double typeOfSize, Entry entryList[],
     Dimension prefMinSize[])
{
    Dimension size;      // Preferred/minimum size of current component
    int scaledSize = 0;  // Preferred/minimum size of scaled components
    int temp;            // Temporary variable used to compare sizes
    int counter;         // Counting variable

    // Get number of crs
    int numCr = crSpec[z].length;

    // Determine percentage of space allocated to fill components.  This is
    // one minus the sum of all scalable components.
    double fillSizeRatio = 1.0;
    int numFillSize = 0;

    for (counter = 0; counter < numCr; counter++) {
        if ((crSpec[z][counter] > 0.0) && (crSpec[z][counter] < 1.0))
            fillSizeRatio -= crSpec[z][counter];
        else if (crSpec[z][counter] == FILL)
            numFillSize++;
    }

    // Adjust fill ratios to reflect number of fill rows/columns
    if (numFillSize > 1) {
        fillSizeRatio /= numFillSize;
    }

    // Cap fill ratio bottoms to 0.0
    if (fillSizeRatio < 0.0) {
        fillSizeRatio = 0.0;
    }

    // Create array to hold actual sizes in pixels
    crSize[z] = new int[numCr];

    // Assign absolute sizes (must be done before assignPrefMinSize)
    // This is done to calculate absolute cr sizes
    assignAbsoluteSize(z, 0);

    // Assign preferred and minimum sizes (must be done after assignAbsoluteSize)
    // This is done to calculate preferred/minimum cr sizes
    assignPrefMinSize(z, 0, MINIMUM);
    assignPrefMinSize(z, 0, PREFERRED);

    int crPrefMin[] = new int[numCr];

    for (counter = 0; counter < numCr; counter++) {
        if ((crSpec[z][counter] == PREFERRED) ||
            (crSpec[z][counter] == MINIMUM))
        {
            crPrefMin[counter] = crSize[z][counter];
        }
    }

    // Find maximum preferred/minimum size of all scaled components
    int numColumn = crSpec[C].length;
    int numRow = crSpec[R].length;
    int numEntry = entryList.length;

    for (int entryCounter = 0; entryCounter < numEntry; entryCounter++) {
        // Get next entry
        Entry entry = entryList[entryCounter];

        // Make sure entry is in valid rows and columns
        if ((entry.cr1[C] < 0) || (entry.cr1[C] >= numColumn) ||
                                  (entry.cr2[C] >= numColumn) ||
            (entry.cr1[R] < 0) || (entry.cr1[R] >= numRow)    ||
                                  (entry.cr2[R] >= numRow))
        {
            // Skip the bad component
            continue;
        }

        // Get preferred/minimum size of current component
        size = prefMinSize[entryCounter];

        //----------------------------------------------------------------------

        // Calculate portion of component that is not absolutely sized
        int scalableSize = (z == C) ? size.width : size.height;

        for (counter = entry.cr1[z]; counter <= entry.cr2[z]; counter++) {
            if (crSpec[z][counter] >= 1.0)
                scalableSize -= crSpec[z][counter];
            else if ((crSpec[z][counter] == PREFERRED) ||
                     (crSpec[z][counter] == MINIMUM))
            {
                scalableSize -= crPrefMin[counter];
            }
        }
        //----------------------------------------------------------------------

        // Determine total percentage of scalable space that the component
        // occupies by adding the relative columns and the fill columns
        double relativeSize = 0.0;

        for (counter = entry.cr1[z]; counter <= entry.cr2[z]; counter++) {
            // Cr is scaled
            if ((crSpec[z][counter] > 0.0) && (crSpec[z][counter] < 1.0))
                // Add scaled size to relativeWidth
                relativeSize += crSpec[z][counter];
            // Cr is fill
            else if ((crSpec[z][counter] == FILL) && (fillSizeRatio != 0.0))
                // Add fill size to relativeWidth
                relativeSize += fillSizeRatio;
        }

        // Determine the total scaled size as estimated by this component
        if (relativeSize == 0)
            temp = 0;
        else
            temp = (int) (scalableSize / relativeSize + 0.5);

        //----------------------------------------------------------------------

        // If the container needs to be bigger, make it so
        if (scaledSize < temp) {
            scaledSize = temp;
        }
    }

    // totalSize is the scaledSize plus the sum of all absolute sizes and all
    // preferred sizes
    int totalSize = scaledSize;

    for (counter = 0; counter < numCr; counter++)
        // Is the current cr an absolute size
        if (crSpec[z][counter] >= 1.0)
            totalSize += (int) (crSpec[z][counter] + 0.5);
        // Is the current cr a preferred/minimum size
        else if ((crSpec[z][counter] == PREFERRED) ||
                 (crSpec[z][counter] == MINIMUM))
        {
            // Add preferred/minimum width
            totalSize += crPrefMin[counter];
        }

    // Compensate for horizontal and vertical gap
    if (numCr > 0)
        totalSize += ((z == C) ? hGap : vGap) * (numCr - 1);

    return totalSize;
}




//******************************************************************************
//** java.awt.event.LayoutManager2 methods                                   ***
//******************************************************************************




//******************************************************************************
//*** Inner Class                                                            ***
//******************************************************************************



// The following inner class is used to bind components to their constraints
public static final class Entry implements Cloneable {
    /** Component bound by the constraints */
    final Component component;

    /** Cell in which the upper-left corner of the component lies */
    final int cr1[];

    /** Cell in which the lower-right corner of the component lies */
    final int cr2[];

    /** Horizontal and vertical alignment */
    final Justification[] alignment;

    /**
     * Constructs an Entry that binds a component to a set of constraints.
     *
     * @param component     component being bound
     * @param constraint    constraints being applied
     */

    public Entry (Component component, TableLayoutConstraints constraint)
    {
        this.cr1 = new int[] {constraint.col1, constraint.row1};
        this.cr2 = new int[] {constraint.col2, constraint.row2};
        this.alignment = new Justification[] {constraint.hAlign, constraint.vAlign};
        this.component = component;
    }

    /**
     * Copies this Entry.
     */

    public Object copy () throws CloneNotSupportedException {
        return clone();
    }

    /**
     * Gets the string representation of this Entry.
     *
     * @return a string in the form
     *         "(col1, row1, col2, row2, vAlign, hAlign) component"
     */

    @Override
    public String toString () {
        TableLayoutConstraints c = TableLayoutConstraints.of
            (cr1[C], cr1[R], cr2[C], cr2[R], alignment[C], alignment[R]);

        return "(" + c + ") " + component;
    }
}



}
