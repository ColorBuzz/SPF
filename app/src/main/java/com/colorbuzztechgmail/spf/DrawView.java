package com.colorbuzztechgmail.spf;

/**
 * Created by PC01 on 23/06/2017.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import  com.colorbuzztechgmail.spf.PaintType.PaintTools;


import java.util.ArrayList;
import java.util.List;

public class DrawView extends View implements View.OnTouchListener {
    private ScaleGestureDetector mScaleDetector;

    Matrix matrix;
    public PieceDecoder decoder;
    private Piece piece;
    Path piecePath;
    View container;
    PointF[] rectPolygon = new PointF[4];
    String info;
    PointF Center;
    int margin = 8;
    int viewWidth = 0;
    int viewHeight = 0;
    float zoomFactor = 1f;
    private List<List<PointF>> triangles=new ArrayList<>();




    @Override
    public Rect getClipBounds() {
        return super.getClipBounds();
    }


    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.restore();
        canvas.save();
        Paint mpaint=new Paint();
      //  mpaint.setColor(getResources().getColor(R.color.colorAmber_A400));
      /* canvas.drawRoundRect(canvas.getClipBounds().left, canvas.getClipBounds().top, canvas.getClipBounds().right,
                canvas.getClipBounds().bottom,60f,60f, mpaint);*/

        canvas.translate(Center.x, Center.y);
        canvas.scale(zoomFactor, zoomFactor);
        Bitmap backgroundBitmap=ShapeGenerator.createColoredBimap(viewWidth/zoomFactor,viewHeight/zoomFactor,getContext().getResources().getColor(R.color.selectItemList));

        Path perimeterPath = new Path();
       PaintTools perimeterTool=PaintTools.CH;


        if(decoder.PRMT_PN)
            perimeterTool=PaintTools.PN;


        for (int t = 0; t < decoder.Perimeter.size(); t++) {

            if (t == 0) {
                perimeterPath.moveTo(decoder.Perimeter.get(t).x, decoder.Perimeter.get(t).y);

            }
            if (t < decoder.Perimeter.size() - 1) {
               canvas.drawLine(decoder.Perimeter.get(t).x, decoder.Perimeter.get(t).y, decoder.Perimeter.get(t + 1).x, decoder.Perimeter.get(t + 1).y, new PaintType(perimeterTool, zoomFactor));
                perimeterPath.lineTo(decoder.Perimeter.get(t).x, decoder.Perimeter.get(t).y);
            } else if (t == decoder.Perimeter.size() - 1) {
                canvas.drawLine(decoder.Perimeter.get(t).x, decoder.Perimeter.get(t).y, decoder.Perimeter.get(0).x, decoder.Perimeter.get(0).y, new PaintType(perimeterTool, zoomFactor));
                perimeterPath.lineTo(decoder.Perimeter.get(t).x, decoder.Perimeter.get(t).y);
            }
        }






       canvas.clipPath(perimeterPath);
        canvas.drawBitmap(backgroundBitmap,-Center.x/zoomFactor,-Center.y/zoomFactor,null);

       /* if(!triangles.isEmpty()){

            for(int i=0;i<triangles.size();i++){


                for(int j=0; j<triangles.get(i).size();j++){



                    if (j < triangles.get(i).size() - 1) {
                        canvas.drawLine(triangles.get(i).get(j).x, triangles.get(i).get(j).y,triangles.get(i).get(j+1).x, triangles.get(i).get(j+1).y, new PaintType(PaintTools.TR, zoomFactor));

                    } else if (j== triangles.get(i).size()) {
                        canvas.drawLine(triangles.get(i).get(j).x, triangles.get(i).get(j).y, triangles.get(i).get(0).x, triangles.get(i).get(0).y, new PaintType(PaintTools.TR, zoomFactor));
                    }



                }


            }


        }*/


        Log.d("Center", "Piece: " + piece.getName() + " WIDTH:" + String.valueOf(backgroundBitmap.getWidth()*zoomFactor) + " HEIGHT:" + String.valueOf(backgroundBitmap.getHeight()*zoomFactor));
        Log.d("ZoomFactor", "Piece: " + piece.getName() + " factor:" + String.valueOf(zoomFactor));
         //canvas.drawPath(perimeterPath, new PaintType("CH",zoomFactor));


        //////// TOOL 0 CLOSED PATH/////////////////////////////////////
        for (List<PointF> pointFList : decoder.preview_PolyLine_TOOL_0_CL1) {
            for (int i = 0; i < pointFList.size(); i++) {
                if (i < pointFList.size() - 1) {
                    canvas.drawLine(pointFList.get(i).x, pointFList.get(i).y, pointFList.get(i + 1).x, pointFList.get(i + 1).y, new PaintType(PaintTools.CH, zoomFactor));
                } else if (i == pointFList.size() - 1) {
                    canvas.drawLine(pointFList.get(i).x, pointFList.get(i).y, pointFList.get(0).x, pointFList.get(0).y, new PaintType(PaintTools.CH, zoomFactor));
                }
            }
        }
        //////// TOOL 0 OPEN PATH/////////////////////////////////////
        for (List<PointF> pointFList : decoder.preview_PolyLine_TOOL_0_CL0) {
            for (int j = 0; j < (pointFList.size() - 1); j++) {
                canvas.drawLine(pointFList.get(j).x, pointFList.get(j).y, pointFList.get(j + 1).x, pointFList.get(j + 1).y, new PaintType(PaintTools.CH, zoomFactor));
            }

        }
        //////// TOOL 1 CLOSED PATH/////////////////////////////////////

        for (List<PointF> pointFList : decoder.preview_PolyLine_TOOL_1_CL1) {
            for (int i = 0; i < pointFList.size(); i++) {
                if (i < pointFList.size() - 1) {
                    canvas.drawLine(pointFList.get(i).x, pointFList.get(i).y, pointFList.get(i + 1).x, pointFList.get(i + 1).y, new PaintType(PaintTools.PN, zoomFactor));
                } else if (i == pointFList.size() - 1) {
                    canvas.drawLine(pointFList.get(i).x, pointFList.get(i).y, pointFList.get(0).x, pointFList.get(0).y, new PaintType(PaintTools.PN, zoomFactor));
                }
            }
        }
        //////// TOOL 1 OPEN PATH/////////////////////////////////////
        for (List<PointF> pointFList : decoder.preview_PolyLine_TOOL_1_CL0) {

            for (int j = 0; j < (pointFList.size() - 1); j++) {
                canvas.drawLine(pointFList.get(j).x, pointFList.get(j).y, pointFList.get(j + 1).x, pointFList.get(j + 1).y, new PaintType(PaintTools.PN, zoomFactor));
            }

        }

        //////// TOOL 1 OPEN PATH/////////////////////////////////////
        for (List<PointF> pointFList : decoder.preview_Point_2D_TOOL_0) {

            for (int j = 0; j < (pointFList.size()); j++) {
                canvas.drawCircle(pointFList.get(j).x, pointFList.get(j).y, 2, new PaintType(PaintTools.PT, zoomFactor));
            }

        }
        for (List<PointF> pointFList : decoder.preview_Point_2D_TOOL_1) {

            for (int j = 0; j < (pointFList.size()); j++) {
                canvas.drawCircle(pointFList.get(j).x, pointFList.get(j).y, 3, new PaintType(PaintTools.PT, zoomFactor));
            }

        }




        canvas.restore();
//

    }


    public void centerCanvas(int width, int height, boolean autoScale) {

//
//        info += String.valueOf(width);
//        info += String.valueOf(height);
//
//        Utils.toast(getContext(), info);


        if (autoScale) {
            zoomFactor = resizePreview(width, height, decoder.xMax - decoder.xMin, decoder.yMax - decoder.yMin);
        }


        Center = new PointF(((width / 2) - (zoomFactor * (decoder.xMin + (decoder.xMax - decoder.xMin) / 2))),
                (height / 2) - (zoomFactor * (decoder.yMin + (decoder.yMax - decoder.yMin) / 2)));


    }

    private float resizePreview(float screenWidth, float screenHeight, float pieceWidth, float pieceHeight) {

        float factor = 1;


        float screenArea = screenWidth * screenHeight;
        float pieceArea = pieceWidth * pieceHeight;

        if (pieceArea > screenArea) {

            if (pieceWidth > pieceHeight) {

                if (pieceWidth > screenWidth) {

                    factor = (screenWidth / (pieceWidth));

                }

            } else {

                if (pieceHeight > screenHeight) {

                    factor = (screenHeight / (pieceHeight));

                }

            }


        } else if (pieceArea < screenArea) {


            final float factorW = (screenWidth / (pieceWidth));
            final float factorH = (screenHeight / (pieceHeight));

            if (factorW < factorH) {
                factor = factorW;

            } else {

                factor = factorH;


            }


        }

        factor = factor * 0.90f;

        return factor;

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mScaleDetector.onTouchEvent(event);

        //  Utils.toast(getContext(),"TOUCH");

        return true;
    }


    @Override
    protected void onAttachedToWindow() {


        //  Utils.toast(getContext(),"ATTACH");
        super.onAttachedToWindow();
    }

    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {


            zoomFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            zoomFactor = Math.max(1f, Math.min(zoomFactor, 10.f));
            centerCanvas(viewWidth, viewHeight, false);
            // Utils.toast(getContext(),String.valueOf(zoomFactor));
            invalidate();
            return true;
        }

    }

    public Drawable getDrawable(int width, int height) {
        centerCanvas(width, height, true);
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);

        draw(c);
        Drawable d = new BitmapDrawable(b);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        return d;

    }


    public Bitmap getBitmap(int width, int height) {


        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        centerCanvas(width, height, true);

         Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawRect(0F, 0F, (float) width, (float) height, paint);


        draw(canvas);

        return bitmap;
    }




    public DrawView(Context context, Piece piece, View container,int Width,int Height) {
        super(context);
        this.piece=piece;

        margin=(int)context.getResources().getDimension(R.dimen.padding);
        viewHeight=Height;
        viewWidth=Width;
        this.container = container;

        if(container!=null) {
            mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
            this.setOnTouchListener(this);



        }
        this.setLayoutParams(new LinearLayout.LayoutParams(viewWidth, viewHeight));


        piecePath = new Path();

        decoder = new PieceDecoder(piece);


        info = "CA_" + String.valueOf(decoder.CH_ST) + " " + "COUNT:" + String.valueOf(decoder.OP_CNT0) + '\n';
        info += "PN_" + String.valueOf(decoder.PN_ST) + " " + "COUNT:" + String.valueOf(decoder.CL_CNT1 + decoder.OP_CNT1) + '\n';
        info += "PT1_" + String.valueOf(decoder.PT1_ST) + " " + "COUNT:" + String.valueOf(decoder.PT1_CNT) + '\n';
        info += "PT2_" + String.valueOf(decoder.PT2_ST) + " " + String.valueOf(decoder.PT2_CNT) + '\n';
        info += "PRMT_" + String.valueOf(decoder.PRMT_CH) + " " + '\n';

        final List<PointF> aux=new ArrayList<>();

        aux.addAll(decoder.Perimeter);

        //triangles=PolygonTriangulator.Triangulate(aux,true);



        centerCanvas(viewWidth, viewHeight,true);
        rectPolygon = decoder.getRectangle();
        if(container!=null) {

            if(container instanceof LinearLayout){
                ((LinearLayout)container).addView(this);

            }

            if(container instanceof FrameLayout){
                ((FrameLayout)container).addView(this);

            }
        }
    }
    public DrawView(Context context, List<String>rawData, View container,int Width,int Height) {
        super(context);

        margin=(int)context.getResources().getDimension(R.dimen.padding);
        viewHeight=Height;
        viewWidth=Width;
        this.container = container;

        if(container!=null) {
            mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
            this.setOnTouchListener(this);



        }
        this.setLayoutParams(new LinearLayout.LayoutParams(viewWidth, viewHeight));


        piecePath = new Path();

        decoder = new PieceDecoder(rawData);


        info = "CA_" + String.valueOf(decoder.CH_ST) + " " + "COUNT:" + String.valueOf(decoder.OP_CNT0) + '\n';
        info += "PN_" + String.valueOf(decoder.PN_ST) + " " + "COUNT:" + String.valueOf(decoder.CL_CNT1 + decoder.OP_CNT1) + '\n';
        info += "PT1_" + String.valueOf(decoder.PT1_ST) + " " + "COUNT:" + String.valueOf(decoder.PT1_CNT) + '\n';
        info += "PT2_" + String.valueOf(decoder.PT2_ST) + " " + String.valueOf(decoder.PT2_CNT) + '\n';
        info += "PRMT_" + String.valueOf(decoder.PRMT_CH) + " " + '\n';

        final List<PointF> aux=new ArrayList<>();

        aux.addAll(decoder.Perimeter);

        //triangles=PolygonTriangulator.Triangulate(aux,true);



        centerCanvas(viewWidth, viewHeight,true);
        rectPolygon = decoder.getRectangle();
        if(container!=null) {

            if(container instanceof LinearLayout){
                ((LinearLayout)container).addView(this);

            }

            if(container instanceof FrameLayout){
                ((FrameLayout)container).addView(this);

            }
        }
    }


}



