package com.basketbandit.quizzical.util;

import java.awt.image.BufferedImage;

public class BufferedImageHex extends BufferedImage {
    public BufferedImageHex(int width, int height, int imageType) {
        super(width, height, imageType);
    }

    public String getHex(int x, int y) {
        StringBuilder hex = new StringBuilder(Integer.toHexString(getRGB(x, y) & 0xffffff));
        while(hex.length() < 6) {
            hex.insert(0, "0");
        }
        hex.insert(0, "#");
        return hex.toString();
    }
}
