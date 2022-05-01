package com.example.filter;


import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * This is the class for the Ship.
 */

public class objClown extends obj {

    private Bitmap crownBmp;

    public objClown(int tx, int ty, int w, int wid, int hei, Bitmap pic) {
        super(tx, ty, w, wid, hei);
        crownBmp =  pic;

    }

    @Override
    public void draw(Canvas c) {
        c.drawBitmap(crownBmp, x, y, null);
    }

}
