package com.bensler.decaf.util;

/**
 * Entity just being Named.
 */
public class NamedImpl extends Object implements Named {

    private final String name_;

    public NamedImpl(final String name) {
        super();
        name_ = name;
    }

    public String getName() {
        return name_;
    }

}
