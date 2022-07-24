package com.example.chatting;

import android.widget.ImageView;
import android.widget.TextView;

public class Interests {

 int image;
 String text;
 int clickShow;

    public Interests(int image, String text, int clickShow) {
        this.image = image;
        this.text = text;
        this.clickShow = clickShow;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getClickShow() {
        return clickShow;
    }

    public void setClickShow(int clickShow) {
        this.clickShow = clickShow;
    }
}
