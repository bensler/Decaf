package com.bensler.decaf.util;

/**
 * Use this exception if you just want to do some flow control.
 *
 * <p>It does <b>not</b> carry a stacktrace or cause. As this class is stateless you can simply use {@link #CANCELED}.
 */
public class CanceledException extends Exception {

    public static final CanceledException CANCELED = new CanceledException();

    protected CanceledException() {
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
