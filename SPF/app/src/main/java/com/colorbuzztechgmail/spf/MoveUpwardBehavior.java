package com.colorbuzztechgmail.spf;

/**
 * Created by PC01 on 08/01/2018.
 */


        import android.os.Build;
        import android.support.design.widget.CoordinatorLayout;
        import android.support.design.widget.Snackbar;
        import android.util.Log;
        import android.view.View;

public class MoveUpwardBehavior extends CoordinatorLayout.Behavior<View> {
    private static final boolean SNACKBAR_BEHAVIOR_ENABLED;

    float posXmin =3000f;
    float posXmax=0.0f;
    float posX=0.0f;

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {



        return SNACKBAR_BEHAVIOR_ENABLED && dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public void onDependentViewRemoved(CoordinatorLayout parent, View child, View dependency) {
        super.onDependentViewRemoved(parent, child, dependency);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {


        if(posXmin>dependency.getX()){

            posXmin=dependency.getX();

        }else if(posXmax<=dependency.getX()){

            posXmax=dependency.getX();

        }

            float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
            posX = dependency.getX();
            child.setTranslationY(translationY);
            Log.e("container", "POS X :" + String.valueOf( posX )+ " " +"POS minX :" + String.valueOf( posXmin )+ " "+"POS maxX :" + String.valueOf( posXmax ));




        return true;
    }

    static {
        SNACKBAR_BEHAVIOR_ENABLED = Build.VERSION.SDK_INT >= 11;
    }
}