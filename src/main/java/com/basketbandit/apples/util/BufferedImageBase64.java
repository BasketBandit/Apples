package com.basketbandit.apples.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.Base64;

public class BufferedImageBase64 extends BufferedImage {
    public BufferedImageBase64(int width, int height, int imageType) {
        super(width, height, imageType);
    }

    public String getAsBase64Png() {
        try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            OutputStream outputStream = Base64.getEncoder().wrap(byteArrayOutputStream)) {
            ImageIO.write(this, "png", outputStream);
            return byteArrayOutputStream.toString();
        } catch (final IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }
}
