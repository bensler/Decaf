package com.bensler.decaf.util;

/**
 * Use this exception if you just want to do some flow control.
 */
public class CanceledException extends Exception {

    public CanceledException() {
        super();
    }

    /**
     * @see  java.lang.Throwable#fillInStackTrace()
     */
    public synchronized Throwable fillInStackTrace() {

        // Do nothing.
        // fillInStackTrace() makes it expensive (CPU) to create an exception
        return this;
    }

}
