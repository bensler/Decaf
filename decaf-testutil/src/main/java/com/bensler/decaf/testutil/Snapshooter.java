package com.bensler.decaf.testutil;

import java.awt.Color;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.ComparisonFailure;

class Snapshooter implements Runnable {

  private final Bender parent_;
  private final Component component_;
  private final TestImageSample sampleImage_;

  public Snapshooter(Bender parent, Component target, TestImageSample sample) {
    parent_ = parent;
    component_ = target;
    sampleImage_ = sample;
  }

  @Override
  public void run() {
    try {
      final BufferedImage image = ImageIO.read(ImageIO.createImageInputStream(
        sampleImage_.getSampleInputStream()
      ));
      final BufferedImage actual = new BufferedImage(
        component_.getWidth(), component_.getHeight(),
        BufferedImage.TYPE_INT_RGB
      );
      final BufferedImage diffImage;

      component_.paint(actual.getGraphics());
      diffImage = diffImage(image, actual);
      if (diffImage != null) {
        final AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        final String failedGifName = sampleImage_.getErrorBaseFileName() + ".failed.gif";
        final String actualFileName = sampleImage_.getErrorBaseFileName() + ".actual.png";

        encoder.setDelay(700);   // ms
        encoder.start(new FileOutputStream(new File(parent_.getReportsDir(), failedGifName)));
        encoder.setSize(diffImage.getWidth(), diffImage.getHeight());
        encoder.addFrame(image);
        encoder.addFrame(diffImage);
        encoder.addFrame(actual);
        encoder.finish();
        ImageIO.write(actual, "png", new File(parent_.getReportsDir(), actualFileName));
        parent_.addFailure(new ComparisonFailure(
          "screenshot is different from expected appearance", sampleImage_.getSampleFileName(), failedGifName)
        );
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    };
  }

  private BufferedImage diffImage(BufferedImage img1, BufferedImage img2) {
    final int width1 = img1.getWidth();
    final int width2 = img2.getWidth();
    final int height1 = img1.getHeight();
    final int height2 = img2.getHeight();
    final int width = Math.max(width1, width2);
    final int height = Math.max(height1, height2);
    final BufferedImage diffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    final Color warningColor = new Color(255, 128, 128, 255);
    boolean diff = false;

    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        final int rgb1;
        
        if (
          ((x < width1) && (x < width2) && (y < height1) && (y < height2))
          && ((rgb1 = img1.getRGB(x, y)) == img2.getRGB(x, y))
        ) {
          diffImg.setRGB(x, y, rgb1);
        } else {
          diffImg.setRGB(x, y, warningColor.getRGB());
          diff = true;
        }
      }
    }
    return (diff ? diffImg : null);
  }

}