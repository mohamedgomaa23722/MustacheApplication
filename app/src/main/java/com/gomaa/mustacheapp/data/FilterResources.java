package com.gomaa.mustacheapp.data;

public class FilterResources {

    private int resourceImage;
    private int resourceTexture;

    public FilterResources(int resourceImage, int resourceTexture) {
        this.resourceImage = resourceImage;
        this.resourceTexture = resourceTexture;
    }

    public int getResourceImage() {
        return resourceImage;
    }

    public int getResourceTexture() {
        return resourceTexture;
    }
}
