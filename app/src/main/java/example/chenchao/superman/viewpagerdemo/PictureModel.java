package example.chenchao.superman.viewpagerdemo;

/**
 * Created by chenchao on 2019/4/21.
 */

public class PictureModel {
    private int pictureRes;
    private int width;
    private int height;

    public PictureModel() {
    }

    public PictureModel(int pictureId, int width, int height) {
        this.pictureRes = pictureId;
        this.width = width;
        this.height = height;
    }

    public int getPictureRes() {
        return pictureRes;
    }

    public void setPictureRes(int pictureRes) {
        this.pictureRes = pictureRes;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
