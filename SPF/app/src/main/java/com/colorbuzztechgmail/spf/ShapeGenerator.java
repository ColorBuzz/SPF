package com.colorbuzztechgmail.spf;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

/**
 * Created by PC01 on 14/01/2018.
 */

public class ShapeGenerator {

    public static final int MODE_ROUND_RECT=2;
    public static final int MODE_CIRCLE=1;
    public static final int SIZE_BIG=1;
    public static final int SIZE_SMALL=2;


    int targetWidth ;
    int targetHeight;
    Context context;


    public ShapeGenerator(Context context) {
        super();
        this.context=context;
        targetHeight= (int)context.getResources().getDimension(R.dimen.piece_pic_size);
        targetWidth= (int)context.getResources().getDimension(R.dimen.piece_pic_size);

    }


    public Bitmap getShape(@Nullable Bitmap scaleBitmapImage, int mode, int color, int sizeMode) {

          switch (sizeMode){


              case SIZE_BIG:
                  targetHeight= (int)context.getResources().getDimension(R.dimen.piece_pic_size);
                  targetWidth= (int)context.getResources().getDimension(R.dimen.piece_pic_size);
                  break;

              case SIZE_SMALL:
                  targetHeight= (int)context.getResources().getDimension(R.dimen.piece_pic_detail_size);
                  targetWidth= (int)context.getResources().getDimension(R.dimen.piece_pic_detail_size);
                  break;



          }

        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight,Bitmap.Config.ARGB_8888);


        Bitmap backgroundBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight,Bitmap.Config.ARGB_8888);


        if(scaleBitmapImage==null){
            if(color==0){

                backgroundBitmap=createColoredBimap(targetWidth,targetHeight,Utils.getRandomColor());

            }else{

                backgroundBitmap=createColoredBimap(targetWidth,targetHeight,color);


            }

        }else{


            if(color==0) {

                backgroundBitmap = createColoredBimap(targetWidth, targetHeight, Utils.getRandomColor());

            }

        }


        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        if(mode==MODE_CIRCLE) {
            path.addCircle(((float) targetWidth - 1) / 2,
                    ((float) targetHeight - 1) / 2,
                    (Math.min(((float) targetWidth),
                            ((float) targetHeight)) / 2),
                    Path.Direction.CCW);
        }
        if (mode==MODE_ROUND_RECT) {

        path.addRoundRect(0,0,targetWidth,targetHeight,8f,8f, Path.Direction.CCW);
        }
        canvas.clipPath(path);
        canvas.drawBitmap(backgroundBitmap,
                new Rect(0, 0, targetWidth
                        ,targetHeight),
                new Rect(0, 0, targetWidth, targetHeight), null);

        if(scaleBitmapImage!=null) {
            canvas.drawBitmap(scaleBitmapImage,
                    new Rect(0, 0, scaleBitmapImage.getWidth(),
                            scaleBitmapImage.getHeight()),
                    new Rect(0, 0, targetWidth, targetHeight), null);
        }
        return targetBitmap;
    }


    public Bitmap DrawableToBitmap(Drawable drawable){

        if((!(drawable instanceof VectorDrawable)) && (!(drawable instanceof GradientDrawable)))  {
            if (drawable != null) {


                return ((BitmapDrawable) drawable).getBitmap();

            }
        }else{


            return getBitmapFromVectorDrawable(context,drawable);

        }
       return null;
    }

    public Drawable getDrawableShape(Drawable drawable,int mode,int color, int sizeMode){

        return  new BitmapDrawable(context.getResources(),getShape(DrawableToBitmap(drawable),mode,color,sizeMode));

    }

    public static Bitmap createColoredBimap(float width, float height, int color) {
        final  Bitmap bitmap = Bitmap.createBitmap((int)width, (int)height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(0f, 0f, (float) width, (float) height, paint);
        return bitmap;
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, Drawable drawable) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

       final Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
