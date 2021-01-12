package com.miguel.imagegallery;

public class ImageFolder {
    String name;
    String path;
    int numPics;
    String firstPic;

    public ImageFolder(){}

    public ImageFolder(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getNumPics() {
        return numPics;
    }

    public void setNumPics(int numPics) {
        this.numPics = numPics;
    }

    public String getFirstPic() {
        return firstPic;
    }

    public void setFirstPic(String firstPic) {
        this.firstPic = firstPic;
    }
}
