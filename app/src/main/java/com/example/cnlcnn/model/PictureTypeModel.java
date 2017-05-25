package com.example.cnlcnn.model;

/**
 * Created by cnlcnn on 2017/5/4.
 */

public class PictureTypeModel {
    private String name;

    private int imageId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public PictureTypeModel(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }
}
