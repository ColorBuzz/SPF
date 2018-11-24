package com.colorbuzztechgmail.spf;

import android.graphics.Color;
import android.graphics.Paint;
import android.print.PrinterId;

/**
 * Created by PC01 on 06/01/2018.
 */

public class PaintType extends Paint {

    public enum PaintTools{

        PN,
        CH,
        PT,
        TR,
        PRMT


    }



    public PaintType(PaintTools Tool,float zoomFactor) {
        super();
        setStrokeJoin(Join.ROUND);
        setStrokeCap(Cap.ROUND);
        setStrokeWidth(3f/zoomFactor);
            setAntiAlias(true);


        setPaintSetting(Tool);

    }

    private void setPaintSetting(PaintTools paintTools){


        switch (paintTools){

            case  PN:

                this.setColor(Color.RED);


                break;

            case  CH:

                this.setColor(Color.BLACK);

                break;

            case  PT:
                this.setColor(Color.BLUE);
                break;

            case TR:

                this.setColor(Color.GREEN);

                break;
            default:

                this.setColor(Color.parseColor("#cfd8dc"));

                break;
        }






    }
}
