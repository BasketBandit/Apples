package com.basketbandit.apples.util;

import com.basketbandit.apples.Application;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.Base64;

public class BufferedImageHex extends BufferedImage {
    public BufferedImageHex(int width, int height, int imageType) {
        super(width, height, imageType);
    }

    public String getAsBase64Png() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try(OutputStream outputStream = Base64.getEncoder().wrap(byteArrayOutputStream)) {
            ImageIO.write(Application.image, "png", outputStream);
        } catch (final IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
        return byteArrayOutputStream.toString();
    }
}
