package com.colorbuzztechgmail.spf;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PolygonTriangulator
{
    /// <summary>
    /// Calculate list of convex polygons or triangles.
    /// </summary>
    /// <param name="Polygon">Input polygon without self-intersections (it can be checked with SelfIntersection().</param>
    /// <param name="triangulate">true: splitting on triangles; false: splitting on convex polygons.</param>
    /// <returns></returns>
    public static List<List<PointF>> Triangulate(List<PointF> Polygon, boolean triangulate)
    {
        List<List<PointF>> result = new ArrayList<>();
        List<PointF> tempPolygon = Polygon;
        List<PointF> convPolygon = new ArrayList<>();

        int begin_ind = 0;
        int cur_ind;
        int begin_ind1;
        int N = Polygon.size();
        int Range;

        if (Square(tempPolygon) < 0)
            Collections.reverse(tempPolygon);

        while (N >= 3)
        {
            while ((PMSquare(tempPolygon.get(begin_ind), tempPolygon.get((begin_ind + 1) % N),
                    tempPolygon.get((begin_ind + 2) % N)) < 0) ||
                    (Intersect(tempPolygon, begin_ind, (begin_ind + 1) % N, (begin_ind + 2) % N) == true))
            {
                begin_ind++;
                begin_ind %= N;
            }
            cur_ind = (begin_ind + 1) % N;
            convPolygon=new ArrayList<>();
            convPolygon.add(tempPolygon.get(begin_ind));
            convPolygon.add(tempPolygon.get(cur_ind));
            convPolygon.add(tempPolygon.get((begin_ind + 2) % N));

            if (triangulate == false)
            {
                begin_ind1 = cur_ind;
                while ((PMSquare(tempPolygon.get(cur_ind), tempPolygon.get((cur_ind + 1) % N),
                        tempPolygon.get((cur_ind + 2) % N)) > 0) && ((cur_ind + 2) % N != begin_ind))
                {
                    if ((Intersect(tempPolygon, begin_ind, (cur_ind + 1) % N, (cur_ind + 2) % N) == true) ||
                            (PMSquare(tempPolygon.get(begin_ind), tempPolygon.get((begin_ind + 1) % N),
                                    tempPolygon.get((cur_ind + 2) % N)) < 0))
                        break;
                    convPolygon.add(tempPolygon.get((cur_ind + 2) % N));
                    cur_ind++;
                    cur_ind %= N;
                }
            }

            Range = cur_ind - begin_ind;
            if (Range > 0)
            {
                tempPolygon=removePoint(tempPolygon,begin_ind + 1, Range);
            }
            else
            {
                tempPolygon=removePoint(tempPolygon, begin_ind + 1, N - begin_ind - 1);
                tempPolygon=removePoint(tempPolygon, 0, cur_ind + 1);
            }
            N = tempPolygon.size();
            begin_ind++;
            begin_ind %= N;

            result.add(convPolygon);

        }

        return result;
    }

    public static int SelfIntersection(List<PointF> polygon)
    {
        if (polygon.size() < 3)
            return 0;
        int High = polygon.size() - 1;
        PointF O = new PointF();
        int i;
        for (i = 0; i < High; i++)
        {
            for (int j = i + 2; j < High; j++)
            {
                if (LineIntersect(polygon.get(i), polygon.get(i + 1),
                        polygon.get(j), polygon.get(j + 1),  O) == 1)
                    return 1;
            }
        }
        for (i = 1; i < High - 1; i++)
            if (LineIntersect(polygon.get(i), polygon.get(i + 1), polygon.get(High), polygon.get(0), O) == 1)
                return 1;
        return -1;
    }

    public static float Square(List<PointF> polygon)
    {
        float S = 0;
        if (polygon.size() >= 3)
        {
            for (int i = 0; i < polygon.size() - 1; i++)
                S += PMSquare((PointF)polygon.get(i), (PointF)polygon.get(i + 1));
            S += PMSquare((PointF)polygon.get(polygon.size() - 1), (PointF)polygon.get(0));
        }
        return S;
    }

    public int IsConvex(List<PointF> Polygon)
    {
        if (Polygon.size() >= 3)
        {
            if (Square(Polygon) > 0)
            {
                for (int i = 0; i < Polygon.size() - 2; i++)
                    if (PMSquare(Polygon.get(i), Polygon.get(i + 1), Polygon.get(i + 2)) < 0)
                        return -1;
                if (PMSquare(Polygon.get(Polygon.size() - 2), Polygon.get(Polygon.size() - 1), Polygon.get(0)) < 0)
                    return -1;
                if (PMSquare(Polygon.get(Polygon.size() - 1), Polygon.get(0), Polygon.get(1)) < 0)
                    return -1;
            }
            else
            {
                for (int i = 0; i < Polygon.size() - 2; i++)
                    if (PMSquare(Polygon.get(i), Polygon.get(i + 1), Polygon.get(i + 2)) > 0)
                        return -1;
                if (PMSquare( Polygon.get(Polygon.size() - 2),  Polygon.get(Polygon.size() - 1), Polygon.get(0)) > 0)
                    return -1;
                if (PMSquare( Polygon.get(Polygon.size() - 1),Polygon.get(0), Polygon.get(1)) > 0)
                    return -1;
            }
            return 1;
        }
        return 0;
    }

    static boolean Intersect(List<PointF> polygon, int vertex1Ind, int vertex2Ind, int vertex3Ind)
    {
        float s1, s2, s3;
        for (int i = 0; i < polygon.size(); i++)
        {
            if ((i == vertex1Ind) || (i == vertex2Ind) || (i == vertex3Ind))
                continue;
            s1 = PMSquare(polygon.get(vertex1Ind), polygon.get(vertex2Ind), polygon.get(i));
            s2 = PMSquare(polygon.get(vertex2Ind), polygon.get(vertex3Ind), polygon.get(i));
            if (((s1 < 0) && (s2 > 0)) || ((s1 > 0) && (s2 < 0)))
                continue;
            s3 = PMSquare(polygon.get(vertex3Ind),polygon.get(vertex1Ind), polygon.get(i));
            if (((s3 >= 0) && (s2 >= 0)) || ((s3 <= 0) && (s2 <= 0)))
                return true;
        }
        return false;
    }

    static float PMSquare(PointF p1, PointF p2)
    {
        return (p2.x * p1.y - p1.x * p2.y);
    }

    static float PMSquare(PointF p1, PointF p2, PointF p3)
    {
        return (p3.x - p1.x) * (p2.y - p1.y) - (p2.x - p1.x) * (p3.y - p1.y);
    }

    static int LineIntersect(PointF A1, PointF A2, PointF B1, PointF B2, PointF O)
    {
        float a1 = A2.y - A1.y;
        float b1 = A1.x - A2.x;
        float d1 = -a1 * A1.x - b1 * A1.y;
        float a2 = B2.y - B1.y;
        float b2 = B1.x - B2.x;
        float d2 = -a2 * B1.x - b2 * B1.y;
        float t = a2 * b1 - a1 * b2;

        if (t == 0)
            return -1;

        O.y = (a1 * d2 - a2 * d1) / t;
        O.x = (b2 * d1 - b1 * d2) / t;

        if (A1.x > A2.x)
        {
            if ((O.x < A2.x) || (O.x > A1.x))
                return 0;
        }
        else
        {
            if ((O.x < A1.x) || (O.x > A2.x))
                return 0;
        }

        if (A1.y > A2.y)
        {
            if ((O.y < A2.y) || (O.y > A1.y))
                return 0;
        }
        else
        {
            if ((O.y < A1.y) || (O.y > A2.y))
                return 0;
        }

        if (B1.x > B2.x)
        {
            if ((O.x < B2.x) || (O.x > B1.x))
                return 0;
        }
        else
        {
            if ((O.x < B1.x) || (O.x > B2.x))
                return 0;
        }

        if (B1.y > B2.y)
        {
            if ((O.y < B2.y) || (O.y > B1.y))
                return 0;
        }
        else
        {
            if ((O.y < B1.y) || (O.y > B2.y))
                return 0;
        }

        return 1;
    }

    private static List<PointF> removePoint(List<PointF> pointList,int index,int range){


        for (int i=range-1;i>=0;i--){

            pointList.remove(index+i);

        }



        return pointList;

    }
}