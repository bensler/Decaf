package com.bensler.decaf.util;

/**
 * Many entity classes have a {@link #getName()} method. So implementing this interface makes it accessible for standard
 * rendering.
 */
public interface Named {

    String getName();

}
