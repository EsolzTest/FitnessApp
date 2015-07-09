package com.esolz.fitnessapp.datatype;

/**
 * Created by su on 18/6/15.
 */
public class GraphClientImagesDataType {
    String img_id;
    String image_link;
    String image_thumbnail;
    String uploaded_date;

    public GraphClientImagesDataType(String uploaded_date, String image_thumbnail, String image_link, String img_id) {
        this.uploaded_date = uploaded_date;
        this.image_thumbnail = image_thumbnail;
        this.image_link = image_link;
        this.img_id = img_id;
    }

    public String getImg_id() {
        return img_id;
    }

    public void setImg_id(String img_id) {
        this.img_id = img_id;
    }

    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    public String getImage_thumbnail() {
        return image_thumbnail;
    }

    public void setImage_thumbnail(String image_thumbnail) {
        this.image_thumbnail = image_thumbnail;
    }

    public String getUploaded_date() {
        return uploaded_date;
    }

    public void setUploaded_date(String uploaded_date) {
        this.uploaded_date = uploaded_date;
    }
}
