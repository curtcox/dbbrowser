package com.cve.web.core.models;

import com.cve.web.core.Model;
import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.concurrent.Immutable;

/**
 * The lines of a text file.
 * @author curt
 */
@Immutable
public final class TextFileModel implements Model {

    public final ImmutableList<String> lines;

    /**
     * Line to high light, if one exists
     */
    public final int highlight;

    public static final int NONE = -1;

    private TextFileModel(List<String> lines, int highlight) {
        this.lines = ImmutableList.copyOf(lines);
        this.highlight = highlight;
    }

    public static TextFileModel of(List<String> lines) {
        return new TextFileModel(lines,NONE);
    }
}
