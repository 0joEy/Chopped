package net.henrycmoss;

import java.util.List;

public class RGBColor {

    private float red, green, blue, alpha;

    public RGBColor(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public float getRed() {
        return red;
    }

    public void setRed(float red) {
        this.red = red;
    }

    public void incrementRed(float val) {
        if(red + val >= 0.0f && red + val <= 1.0f) red += val;
    }

    public float getGreen() {
        return green;
    }

    public void setGreen(float green) {
        this.green = green;
    }

    public void incrementGreen(float val) {
        if(green + val >= 0.0f && green + val <= 1.0f) green += val;
    }

    public float getBlue() {
        return blue;
    }

    public void setBlue(float blue) {
        this.blue = blue;
    }

    public void incrementBlue(float val) {
        if(blue + val >= 0.0f && blue + val <= 1.0f) blue += val;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public float[] getComponents() {
        return new float[]{red, green, blue};
    }

    public float sum() {
        float total = 0.0f;

        for(int i = 0; i < getComponents().length; i++) {
            total += getComponents()[i];
        }
        return total;
    }

    public void updateFromComponents(float[] components) {
        setRed(components[0]);
        setGreen(components[1]);
        setBlue(components[2]);
    }
}
