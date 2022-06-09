package com.basketbandit.apples.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.Base64;
import java.util.concurrent.ThreadLocalRandom;

public class Utilities {

    /**
     * Converts a BufferedImage object into a base64 png equivalent string.
     * @param image {@link BufferedImage}
     * @return {@link String}
     */
    public static String image2base64(BufferedImage image) {
        try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            OutputStream outputStream = Base64.getEncoder().wrap(byteArrayOutputStream)) {
            ImageIO.write(image, "png", outputStream);
            return byteArrayOutputStream.toString();
        } catch(final IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

    /**
     * Generates a thread local random integer within the given bounds.
     * @param bound int
     * @return int
     */
    public static int random(int bound) {
        return ThreadLocalRandom.current().nextInt(bound);
    }
}
