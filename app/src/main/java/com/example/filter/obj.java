package com.example.filter;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * simple abstract class to hold the basics for the "objs"
 */

public abstract class obj {
    int x, y;
    Rect rec;

    obj() {
        x = 0;
        y = 0;
    }

    obj(int tx, int ty, int w, int wid, int hei) {
        x = tx;
        y = ty;
        rec = new Rect(x, y, x + wid, y + hei);
    }

    public abstract void draw(Canvas c);

    void move(int tx, int ty) {
        x += tx;
        y += ty;
        rec.offsetTo(x, y);
    }

    public void set(int tx, int ty) {
        x = tx;
        y = ty;
        rec.offsetTo(x, y);
    }

}
