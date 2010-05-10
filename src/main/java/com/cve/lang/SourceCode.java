package com.cve.lang;

import com.cve.util.Check;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.concurrent.Immutable;

/**
 * Program source code.  Usually Java, but perhaps not.
 * @author curt
 */
@Immutable
public final class SourceCode {

    final String source;

    /**
     * This string is used for any unavailable source code lines.
     */
    public static final SourceCode UNAVAILABLE = new SourceCode("Unavailable");

    private SourceCode(String source) {
        this.source = Check.notNull(source);
    }

    static SourceCode of(String source) {
        return new SourceCode(source);
    }

    static ImmutableList<SourceCode> readFrom(ResourceLocation resource) {
        List<SourceCode> lines = Lists.newArrayList();
        for (String string : resource.readLines()) {
            lines.add(of(string));
        }
        return ImmutableList.copyOf(lines);
    }


    @Override
    public String toString() {
        return source;
    }

    @Override
    public int hashCode() {
        return source.hashCode();
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o) {
        SourceCode other = (SourceCode) o;
        return source.equals(other.source);
    }
}
