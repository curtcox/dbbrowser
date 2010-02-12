package com.cve.web.management.browsers;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

/**
 * Something that limits the kind of methods and variables we are interested in.
 * @author ccox
 */
enum ModifierMask {

    /**
     * Public or protected, but not private.
     */
    PUBLIC(Modifier.PUBLIC | Modifier.PROTECTED, Modifier.PRIVATE), /**
     * Everything.
     */
    PRIVATE(0, 0);
    private final int required;
    private final int prohibited;

    ModifierMask(int required, int prohibited) {
        this.required = required;
        this.prohibited = prohibited;
    }

    /**
     * Return true, if the member passes this mask.
     */
    boolean passes(Member member) {
        int modifiers = member.getModifiers();
        // if there are some requirements and they aren't met, fail
        if (required != 0 && ((modifiers & required) == 0)) {
            return false;
        }
        if (prohibited != 0 && ((modifiers & prohibited) != 0)) {
            return false;
        }
        return true;
    }
}
