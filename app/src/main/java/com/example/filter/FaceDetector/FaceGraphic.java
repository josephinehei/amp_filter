package com.example.filter.FaceDetector;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.filter.R;
import com.example.filter.obj;
import com.example.filter.objClown;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceLandmark;

import com.example.filter.GraphicOverlay;

/**
 * Draws on the face to create the graphic overlay or filter
 */
public class FaceGraphic extends GraphicOverlay.Graphic {

    private static final float FACE_POSITION_RADIUS = 4.0f;

    private final Paint facePositionPaint;

    private Paint YellowPaint;
    private Paint BluePaint;
    private Paint BlackPaint;
    private Paint BlushPaint;
    private static final float STROKE_WIDTH = 5.0f;
    static final String TAG = "FaceGraphic";
    private volatile Face face;

    Bitmap clownBMP;
    obj clown;

    // protected Handler handler;

    //assigning the colors and stroke/fill types to be used later
    public FaceGraphic(GraphicOverlay overlay, Face face) {
        super(overlay);

        this.face = face;
        final int selectedColor = Color.WHITE;
        //  handler = h;
        facePositionPaint = new Paint();
        facePositionPaint.setColor(selectedColor);

        YellowPaint = new Paint();
        YellowPaint.setColor(Color.YELLOW);
        YellowPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        YellowPaint.setStrokeWidth(STROKE_WIDTH);

        BluePaint = new Paint();
        BluePaint.setColor(Color.BLUE);
        BluePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        BluePaint.setStrokeWidth(2);

        BlackPaint = new Paint();
        BlackPaint.setColor(Color.BLACK);
        BlackPaint.setStyle(Paint.Style.STROKE);
        BlackPaint.setStrokeWidth(30);

        BlushPaint = new Paint();
        BlushPaint.setColor(Color.RED);
        BlushPaint.setAlpha(100);
        BlushPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        BlushPaint.setStrokeWidth(100f);

        clownBMP = BitmapFactory.decodeResource(overlay.getResources(), R.drawable.clown_hat);

    }
    
    /**
     * Draws the face annotations for position on the supplied canvas.
     */
    @Override
    public void draw(Canvas canvas) {
        Face face = this.face;

        if (face == null) {
            return;
        }

        float x = translateX(face.getBoundingBox().centerX());
        float y = translateY(face.getBoundingBox().centerY());

        float yOffset = scale(face.getBoundingBox().height() / 2.0f);
        float top = y - yOffset;
        float cx, cy;
        float lx = 0f, ly = 0f, mx = 0, my = 0, rx = 0, ry = 0;
        int xl = 10;  //how big the x is.

        //drawing a clown hat object on the top of the head
        clown = new objClown(((int) x - clownBMP.getWidth()/2), ((int) top - (clownBMP.getHeight()/2) - 2), 0, clownBMP.getWidth(), clownBMP.getHeight(), clownBMP);
        clown.draw(canvas);

        //draw a triangle above and below the eye
        FaceLandmark leftEye = face.getLandmark(FaceLandmark.LEFT_EYE);
        if (leftEye != null && leftEye.getPosition() != null) {
            Log.d(TAG, "LEFT EYE");
            cx = translateX(leftEye.getPosition().x);
            cy = translateY(leftEye.getPosition().y);

            drawTriangle((int) cx - 100, (int) cy - 50, 200, 100, false, BluePaint, canvas);
            drawTriangle((int) cx - 100, (int) cy + 50, 200, 100, true, BluePaint, canvas);

        }

        //draw a triangle above and below the eye
        FaceLandmark rightEye = face.getLandmark(FaceLandmark.RIGHT_EYE);
        if (rightEye != null && rightEye.getPosition() != null) {
            Log.d(TAG, "RIGHT EYE");
            cx = translateX(rightEye.getPosition().x);
            cy = translateY(rightEye.getPosition().y);

            drawTriangle((int) cx - 100, (int) cy - 50, 200, 100, false, BluePaint, canvas);
            drawTriangle((int) cx - 100, (int) cy + 50, 200, 100, true, BluePaint, canvas);
        }

        FaceLandmark leftmouth = face.getLandmark(FaceLandmark.MOUTH_LEFT);
        FaceLandmark rightmouth = face.getLandmark(FaceLandmark.MOUTH_RIGHT);
        FaceLandmark bottommouth = face.getLandmark(FaceLandmark.MOUTH_BOTTOM);
        if (leftmouth != null && rightmouth != null && bottommouth != null &&
                leftmouth.getPosition() != null && rightmouth.getPosition() != null && bottommouth.getPosition() != null) {
            Log.d(TAG, "LEFT MOUTH");
            lx = translateX(leftmouth.getPosition().x - 15 );
            ly = translateY(leftmouth.getPosition().y - 15);

            Log.d(TAG, "RIGHT MOUTH");
            rx = translateX(rightmouth.getPosition().x + 15 );
            ry = translateY(rightmouth.getPosition().y - 15);

            //      Log.d(TAG, "BOTTOM MOUTH");
            mx = translateX(bottommouth.getPosition().x );
            my = translateY(bottommouth.getPosition().y + 15);


            //now draw the mouth.   First check if all points exists
            if (lx > 0.0f && rx > 0.0f && mx > 0.0f) {
                //so first if one side of the mouth is higher, use that one.
                //          Log.v(TAG, "Drawing mouth");
                if (ly < ry)  //left side is higher
                    ry = ly;
                else
                    ly = ry;

                RectF rectF = new RectF(lx, ly, rx, my );

                canvas.drawRoundRect(rectF, 1,1, BlackPaint);
                }
            }


        //draw semi transparent circle on the cheek
        FaceLandmark leftCheek = face.getLandmark(FaceLandmark.LEFT_CHEEK);
        if (leftCheek != null && leftCheek.getPosition() != null) {
            canvas.drawCircle(
                    translateX(leftCheek.getPosition().x - 5),
                    translateY(leftCheek.getPosition().y - 10 ),
                    50f,
                    BlushPaint);
        }

        //draw semi transparent circle on the cheek
        FaceLandmark rightCheek =
                face.getLandmark(FaceLandmark.RIGHT_CHEEK);
        if (rightCheek != null && rightCheek.getPosition() != null) {
            canvas.drawCircle(
                    translateX(rightCheek.getPosition().x + 5),
                    translateY(rightCheek.getPosition().y - 10),
                    50f,
                    BlushPaint);
        }

        //Draw a yellow circle on the nose
        FaceLandmark noseBase =
                face.getLandmark(FaceLandmark.NOSE_BASE);
        if (noseBase != null && noseBase.getPosition() != null) {
            canvas.drawCircle(
                    translateX(noseBase.getPosition().x + 10),
                    translateY(noseBase.getPosition().y - 3),
                    40f,
                    YellowPaint);
            canvas.drawCircle(
                    translateX(noseBase.getPosition().x),
                    translateY(noseBase.getPosition().y),
                    60f,
                    YellowPaint);
            canvas.drawCircle(
                    translateX(noseBase.getPosition().x - 10),
                    translateY(noseBase.getPosition().y - 3),
                    40f,
                    YellowPaint);
        }
    }

    private void drawTriangle(int x, int y, int width, int height, boolean inverted, Paint paint, Canvas canvas){

        Point p1 = new Point(x,y);
        int pointX = x + width/2;
        int pointY = inverted?  y + height : y - height;

        Point p2 = new Point(pointX,pointY);
        Point p3 = new Point(x+width,y);


        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(p1.x,p1.y);
        path.lineTo(p2.x,p2.y);
        path.lineTo(p3.x,p3.y);
        path.close();

        canvas.drawPath(path, paint);
    }
}
