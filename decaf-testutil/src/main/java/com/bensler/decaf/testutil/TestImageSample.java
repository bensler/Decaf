package com.bensler.decaf.testutil;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.junit.Assert;

public class TestImageSample {

  public final static String TYPE_PNG = "png";
  public final static String DELEMITER = ".";

  private final String baseFileName_;
  private final Class<?> callerClass_;
  private final URL url_;

  public TestImageSample() {
    this(1, null);
  }

  public TestImageSample(String name) {
    this(1, name);
  }

  public TestImageSample(int callDepth) {
    this(callDepth + 1, null);
  }

  public TestImageSample(int callDepth, String name) {
    try {
      final StackTraceElement stackFrame = Thread.currentThread().getStackTrace()[2 + callDepth];
      final String className = stackFrame.getClassName();
      final String sampleFileName;

      callerClass_ = Class.forName(className);
      baseFileName_ = callerClass_.getSimpleName()
        + DELEMITER + stackFrame.getMethodName()
        + (((name != null) && (!name.isEmpty())) ? (DELEMITER + name) : "");
      sampleFileName = getSampleFileName();
      url_ = callerClass_.getResource(sampleFileName);
      Assert.assertNotNull("missing resource " + sampleFileName, url_);
    } catch (ClassNotFoundException cnfe) {
      throw new RuntimeException(cnfe);
    }
  }

  public InputStream getSampleInputStream() throws IOException {
    return url_.openStream();
  }

  public String getSampleFileName() {
    return baseFileName_ + DELEMITER + TYPE_PNG;
  }

  public String getErrorBaseFileName() {
    return callerClass_.getPackage().getName() + DELEMITER + baseFileName_;
  }

}
