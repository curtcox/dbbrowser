package com.cve.html;

import com.cve.html.HTML;

/**
 * The contents of a Tooltip.
 * This is what should appear in the tooltip itself and does not include
 * the logic required to make it appear.
 * See also SimpleTooltip, ColumnNameTooltip, and Link.
 */
public interface Tooltip {

    /**
     * Return the HTML that should be in the tooltip.
     */
    HTML toHTML();
}
