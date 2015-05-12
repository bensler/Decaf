package com.bensler.decaf.util;

/**
 * Use this exception if you want to do some black magic flow control. Even though it is considered
 * bad practice it is quite handy in heavily recursive algorithms to just throw an exception instead
 * of passing a <code>boolean</code> return value through many stack frames.
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
     * Does nothing.
     */
    @Override
    public synchronized Throwable fillInStackTrace() {
        // Does nothing.
        // fillInStackTrace() makes it expensive creating an exception
        return this;
    }

}
