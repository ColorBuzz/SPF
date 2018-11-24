package com.colorbuzztechgmail.spf;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC01 on 06/01/2018.
 */

public class PieceDecoder {

    private List<String> rawData=null;

    public  static final String TOOL_0="TOOL 0";
    public  static final String TOOL_1="TOOL 1";
    public  static final String GEOM_TYPE_POLYLINE_2D="GEOM_TYPE POLYLINE_2D";
    public  static final String GEOM_TYPE_POINT_2D="GEOM_TYPE POINT_2D";


    public boolean CH_ST;
    public boolean PN_ST;
    public boolean XCHPN_ST;
    public boolean PT1_ST;
    public boolean PT2_ST;
    public boolean PTX_ST ;


    public  boolean PRMT_CH;
    public  boolean PRMT_PN;

    public  boolean CC;
    public  boolean CA;
    public  boolean LC;
    public  boolean LA;
    public  boolean PT1;
    public  boolean PT2;
    public  boolean EX;

    //Dibujado

    private Matrix matrixPreview;
    public float xMax,yMax,xMin,yMin;
    public Drawable drawable;

    //Puntos

    public List<List<PointF>> preview_PolyLine_TOOL_0_CL0 ;
    public List<List<PointF>> preview_PolyLine_TOOL_0_CL1 ;
    public List<List<PointF>> preview_PolyLine_TOOL_1_CL0 ;
    public List<List<PointF>> preview_PolyLine_TOOL_1_CL1 ;
    public List<List<PointF>> preview_PolyLine_TOOL_X_CL0 ;
    public List<List<PointF>> preview_PolyLine_TOOL_X_CL1 ;
    public List<List<PointF>> preview_Point_2D_TOOL_0 ;
    public List<List<PointF>> preview_Point_2D_TOOL_1 ;
    public List<List<PointF>> preview_Point_2D_TOOL_X ;

    public int OP_CNT0;
    public int CL_CNT0;
    public int OP_CNT1;
    public int CL_CNT1;
    public int PT1_CNT;
    public int PT2_CNT;

    public List<PointF> Perimeter;
    public PointF[] rectPolygon= new PointF[4];

      //Escalado

    private float factor=1f;
    public float scalePreview;

    public  PieceDecoder(Piece piece){

        super();


        decodePiece(piece.getRawData());
        setTools(piece);

    }

    public  PieceDecoder(List<String> rawData){

        super();

        decodePiece(rawData);


    }

    private void initializeValues(){



        preview_PolyLine_TOOL_0_CL0=new ArrayList<>();
        preview_PolyLine_TOOL_0_CL1=new ArrayList<>();
        preview_PolyLine_TOOL_1_CL0=new ArrayList<>();
        preview_PolyLine_TOOL_1_CL1=new ArrayList<>();
        preview_PolyLine_TOOL_X_CL0=new ArrayList<>();
        preview_PolyLine_TOOL_X_CL1=new ArrayList<>();
        preview_Point_2D_TOOL_0=new ArrayList<>();
        preview_Point_2D_TOOL_1=new ArrayList<>();
        preview_Point_2D_TOOL_X=new ArrayList<>();
        Perimeter=new ArrayList<>();
    }

    private void decodePiece(List<String> rawData) {
        this.rawData=rawData;
        initializeValues();
        for (int i = 0; i < rawData.size(); i++) {
            final String gemotry,element,tool;

            if (rawData.get(i).contains("ENTITY")) {


                gemotry = rawData.get(i + 1).trim();
                element = rawData.get(i + 2).trim();
                tool = rawData.get(i + 3).trim();

                if (gemotry.equals(GEOM_TYPE_POLYLINE_2D)) {
                    //Herramienta Cuchilla
                    if (!element.equals("ELEM_TYPE 1")) {

                        int totalVertex = Integer.valueOf(rawData.get(i + 5).substring(8).trim());

                        //Se toma de referencia la cantidad de vertices para saber donde cuanto contar para llegar al ISCLOSED
                        //Comprobación de Contorno Abierto o Cerrado
                        if (rawData.get(i + 6 + totalVertex).contains("ISCLOSED 1")) {

                            StringToPointList(rawData.subList(i + 6, i + 6 + (totalVertex )), tool, gemotry, "CL");

                        } else if (rawData.get(i + 6 + totalVertex).contains("ISCLOSED 0")) {

                            StringToPointList(rawData.subList(i + 6, i + 6 + (totalVertex )), tool, gemotry, "OP");
                        }

                    } else {

                        int totalVertex = Integer.valueOf(rawData.get(i + 5).substring(8).trim());

                        //Se toma de referencia la cantidad de vertices para saber donde cuanto contar para llegar al ISCLOSED
                        //Comprobación de Contorno Abierto o Cerrado
                        if (rawData.get(i + 6 + totalVertex).contains("ISCLOSED 1")) {

                            StringToPointList(rawData.subList(i + 6, i + 6 + (totalVertex)), tool, gemotry, "PRMT_CH");

                        }

                    }

                } else if (gemotry.equals(GEOM_TYPE_POINT_2D)) {

                    StringToPointList(rawData.subList(i + 5, i + 6), tool, gemotry, "PT");

                }

            }

        }
    }

    public PointF[] getRectangle() {

        xMax=Perimeter.get(0).x;
        yMax=Perimeter.get(0).y;
        for (int i = 0; i < Perimeter.size(); i++) {


         //  Log.e("pieceDecoder",Perimeter.get(i).toString());
            if (Perimeter.get(i).x > xMax) {
                xMax = Perimeter.get(i).x;
            }

            if (Perimeter.get(i).y > yMax) {
                yMax = Perimeter.get(i).y;

            }

        }
        xMin = xMax;
        yMin = yMax;
        for (int i = 0; i < Perimeter.size(); i++) {


            if (Perimeter.get(i).x < xMin) {
                xMin = Perimeter.get(i).x;

            }

            if (Perimeter.get(i).y < yMin) {
                yMin = Perimeter.get(i).y;

            }

        }
        rectPolygon[0] = new PointF(xMin, yMin);
        rectPolygon[1] = new PointF(xMax, yMin);
        rectPolygon[2] = new PointF(xMax, yMax);
        rectPolygon[3] = new PointF(xMin, yMax);

     /*   Log.e("pieceDecoder",
                "xMin:" + String.valueOf(xMin)+ " " +
                        "yMin:" + String.valueOf(yMin) + " " +
                        "xMax:" + String.valueOf(xMax)+" "+
                        "yMax:" + String.valueOf(yMax));*/

         return  rectPolygon;
    }

    private void StringToPointList(List<String> pointList,String tool,String geometry,String type){

        final  List<PointF> pointFList=new ArrayList<>();

        for(int i=0;i<pointList.size();i++){

         final String[]auxPoint=pointList.get(i).trim().split(",");
         final PointF point=new PointF(Float.valueOf(auxPoint[0]),Float.valueOf(auxPoint[1]));
         pointFList.add(point);

        }

        if(!pointFList.isEmpty()){


            if(geometry.equals("GEOM_TYPE POLYLINE_2D")){

                switch (tool) {

                    case TOOL_0:///CUCHILLA

                        CH_ST=true;
                        if (type.equals("CL")) {
                            CC = true;

                            preview_PolyLine_TOOL_0_CL1.add(pointFList);

                            CL_CNT0=preview_PolyLine_TOOL_0_CL1.size();

                        } else if (type.equals("OP")) {
                            CA = true;

                            preview_PolyLine_TOOL_0_CL0.add(pointFList);

                            OP_CNT0=  preview_PolyLine_TOOL_0_CL0.size();

                        }else if (type.equals("PRMT_CH")) {
                            PRMT_CH = true;

                            Perimeter.addAll(pointFList);

                            getRectangle();



                        }
                        break;

                    case TOOL_1://BOLIGRAFO

                        PN_ST=true;

                        if (type.equals("CL")) {
                            LC = true;

                            preview_PolyLine_TOOL_1_CL1.add(pointFList);

                            CL_CNT1=preview_PolyLine_TOOL_1_CL1.size();

                        } else if (type.equals("OP")) {
                            LA = true;

                            preview_PolyLine_TOOL_1_CL0.add(pointFList);
                            OP_CNT1=preview_PolyLine_TOOL_1_CL0.size();

                        }else if (type.equals("PRMT_CH")) {

                             PRMT_PN=true;

                            Perimeter.addAll(pointFList);

                            getRectangle();

                        }

                        break;


                   default:

                       if (type.equals("CL")) {

                           preview_PolyLine_TOOL_X_CL1.add(pointFList);

                       } else if (type.equals("OP")) {

                           preview_PolyLine_TOOL_X_CL0.add(pointFList);
                       }

                       XCHPN_ST=true;

                        break;

                }

            }else if(geometry.equals("GEOM_TYPE POINT_2D")){

                switch (tool) {

                    case TOOL_0:
                        PT1_ST=true;
                        PT1=true;
                       preview_Point_2D_TOOL_0.add(pointFList);
                       PT1_CNT=preview_Point_2D_TOOL_0.size();
                        break;


                    case TOOL_1:

                        PT2_ST=true;
                        preview_Point_2D_TOOL_1.add(pointFList);
                        PT2_CNT=preview_Point_2D_TOOL_1.size();
                        break;


                    default:

                        preview_Point_2D_TOOL_X.add(pointFList);

                        PTX_ST=true;
                        break;

                }




            }


        }



    }

    public void setTools(Piece piece){

        List<String>tools=new ArrayList<>();
        if(CH_ST){

            tools.add("C1");

        }
        if(PN_ST){

            tools.add("B1");

        }  if(XCHPN_ST){

            tools.add("N.A.1");

        }  if(PT1_ST){

            tools.add("T1");

        }  if(PT2_ST){

            tools.add("T2");

        }  if(PTX_ST){

            tools.add("N.A.2");

        }

        piece.setToolList(tools);
    }

    public Float getRectagleArea(){



        return  ( xMax -  xMin) * (yMax-yMin);

    }


    public float calculateArea(){
        double area = 0f;

        if(!Perimeter.isEmpty()) {

            final List<List<PointF>> triangles = PolygonTriangulator.Triangulate(Perimeter, true);



            if (!triangles.isEmpty()) {

                for (List<PointF> pointFList : triangles) {

                    final double area2 = getTriangleArea(pointFList);

                    area = area + area2;


                }

                Log.d("Area", " Area: " + String.valueOf(area));

            }

        }
        return (float) area;
    }

    private double getTriangleArea(List<PointF> points){

        double area=0f;
        double vx1,vx2,vy1,vy2,vx3,vy3,m1,m2,m3,s;

        vx1= points.get(1).x-points.get(0).x;
        vy1= points.get(1).y-points.get(0).y;

        vx2= points.get(2).x-points.get(0).x;
        vy2= points.get(2).y-points.get(0).y;

        vx3= points.get(2).x-points.get(1).x;
        vy3= points.get(2).y-points.get(1).y;


        m1= Math.sqrt((Math.pow(vx1,2)+ Math.pow(vy1 ,2)));

        m2= Math.sqrt((Math.pow(vx2,2)+ Math.pow(vy2 ,2)));

        m3= Math.sqrt((Math.pow(vx3,2)+ Math.pow(vy3 ,2)));


        s=(m1+m2+m3)/2;

        area=Math.sqrt(s*((s-m1)*(s-m2)*(s-m3)));







        return   Math.round(area);
    }

}
