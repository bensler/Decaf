package com.bensler.decaf.util;

/**
 * Some entity classes have a {@link #getDescription()} method. So implementing this interface makes it
 * accessible for standard rendering.
 */
public interface Described {

    String getDescription();

}
