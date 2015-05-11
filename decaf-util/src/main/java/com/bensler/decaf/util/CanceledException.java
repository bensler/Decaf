package com.bensler.decaf.util;

/**
 * Use this exception if you want to do some black magic flow control.
 *
 * <p>It does <b>not</b> carry a stacktrace or cause. As it is stateless you can simply throw {@link #CANCELED} 
 * again and again.
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
        // Does nothing.
        // fillInStackTrace() makes it expensive creating an exception
        return this;
    }

}
