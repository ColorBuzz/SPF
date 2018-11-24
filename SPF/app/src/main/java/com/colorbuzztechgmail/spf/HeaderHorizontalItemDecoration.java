package com.colorbuzztechgmail.spf;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by PC01 on 01/05/2018.
 */

public class HeaderHorizontalItemDecoration extends RecyclerView.ItemDecoration {

    private StickyHeaderInterface mListener;
    private int mStickyHeaderWidth;
    View currentHeader;
    public HeaderHorizontalItemDecoration(RecyclerView recyclerView, @NonNull StickyHeaderInterface listener) {
        mListener = listener;

        // On Sticky Header Click
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            public boolean onInterceptTouchEvent(final RecyclerView recyclerView, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN) {

                   float result=recyclerView.getChildAt(0).getRight();
                    if (motionEvent.getX() <=result ) {

                        String txt = "material";
                        if (currentHeader != null) {
                            txt = ((TextView) currentHeader.findViewById(R.id.text)).getText().toString();
                             mListener.ToggleTitle(txt);
                        }



                       // Utils.toast(recyclerView.getContext(), txt);

                        return true;

                    }
                }
                return false;
            }

            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {



            }

            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        View topChild = parent.getChildAt(0);
        if ((topChild)==null) {
            return;
        }


        int topChildPosition = parent.getChildAdapterPosition(topChild);

        if (topChildPosition == RecyclerView.NO_POSITION) {
            return;
        }
         currentHeader = getHeaderViewForItem(topChildPosition, parent);



        fixLayoutSize(parent, currentHeader);
        int contactPoint = currentHeader.getBottom();
        View childInContact = getChildInContact(parent, contactPoint);
        if ( (childInContact)==null) {
            return;
        }

        if (mListener.isHeader(parent.getChildAdapterPosition(childInContact))) {
            moveHeader(c, currentHeader, childInContact);
            return;
        }


        drawHeader(c, currentHeader);
    }

    private View getHeaderViewForItem(int itemPosition, RecyclerView parent) {
        int headerPosition = mListener.getHeaderPositionForItem(itemPosition);
        int layoutResId = mListener.getHeaderLayout(headerPosition);
        View header = LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);
        mListener.bindHeaderData(header, headerPosition);
        return header;
    }

    private void drawHeader(Canvas c, View header) {
        c.save();
        c.translate(0, 0);
        header.draw(c);
        c.restore();
    }

    private void drawPrevious(Canvas c,RecyclerView parent) {
        View previous = LayoutInflater.from(parent.getContext()).inflate(R.layout.pp, parent, false);
        fixLayoutSize(parent,previous);
        c.save();
        c.translate(0, 0);
        previous.draw(c);

    }

    private void drawNext(Canvas c,RecyclerView parent) {
        View previous = LayoutInflater.from(parent.getContext()).inflate(R.layout.pp, parent, false);
        fixLayoutSize(parent,previous);
        c.save();
        c.translate(200, 0);
        previous.draw(c);

    }


    private void moveHeader(Canvas c, View currentHeader, View nextHeader) {
        c.save();
        c.translate(nextHeader.getLeft() - currentHeader.getWidth(),0 );
        currentHeader.draw(c);
        c.restore();
    }

    private View getChildInContact(RecyclerView parent, int contactPoint) {
        View childInContact = null;
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child.getRight() > contactPoint) {
                if (child.getLeft() <= contactPoint) {
                    // This child overlaps the contactPoint
                    childInContact = child;
                    break;
                }
            }
        }
        return childInContact;
    }

    /**
     * Properly measures and layouts the top sticky header.
     * @param parent ViewGroup: RecyclerView in this case.
     */
    private void fixLayoutSize(ViewGroup parent, View view) {

        // Specs for parent (RecyclerView)
        int widthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth(), View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight(), View.MeasureSpec.EXACTLY);

        // Specs for children (headers)
        int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec, parent.getPaddingLeft() + parent.getPaddingRight(), view.getLayoutParams().width);
        int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec, parent.getPaddingTop() + parent.getPaddingBottom(), view.getLayoutParams().height);

        view.measure(childWidthSpec, childHeightSpec);

        view.layout(0, 0,mStickyHeaderWidth= view.getMeasuredWidth(),   view.getMeasuredHeight());
    }

    public interface StickyHeaderInterface {

        /**
         * This method gets called by {@link HeaderHorizontalItemDecoration} to fetch the position of the header item in the adapter
         * that is used for (represents) item at specified position.
         * @param itemPosition int. Adapter's position of the item for which to do the search of the position of the header item.
         * @return int. Position of the header item in the adapter.
         */
        int getHeaderPositionForItem(int itemPosition);

        /**
         * This method gets called by {@link HeaderHorizontalItemDecoration} to get layout resource id for the header item at specified adapter's position.
         * @param headerPosition int. Position of the header item in the adapter.
         * @return int. Layout resource id.
         */
        int getHeaderLayout(int headerPosition);

        /**
         * This method gets called by {@link HeaderHorizontalItemDecoration} to setup the header View.
         * @param header View. Header to set the data on.
         * @param headerPosition int. Position of the header item in the adapter.
         */
        void bindHeaderData(View header, int headerPosition);

        /**
         * This method gets called by {@link HeaderHorizontalItemDecoration} to verify whether the item represents a header.
         * @param itemPosition int.
         * @return true, if item at the specified adapter's position represents a header.
         */
        boolean isHeader(int itemPosition);

        void ToggleTitle(String title);
    }
}