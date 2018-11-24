package com.colorbuzztechgmail.spf;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.colorbuzztechgmail.spf.databinding.PieceInfodBinding;
import com.colorbuzztechgmail.spf.databinding.PieceGridBinding;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import static com.colorbuzztechgmail.spf.Utils.ViewMode.GRID;
import static com.colorbuzztechgmail.spf.Utils.ViewMode.LIST;

public class PieceCheckableListViewHolder extends ViewHolderMultiChoice{


    public TextView titleText;
    public ImageView image;
    public Toolbar toolbar;
    private View frontView,backView;
    public EasyFlipView flipView;



    public PieceCheckableListViewHolder(View v) {
        super(v);
        toolbar   = (Toolbar) v.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.submenu_piece_option);
        frontView=v.findViewById(R.id.frontView);
        backView=v.findViewById(R.id.infoContainer);
        flipView=v.findViewById(R.id.flipView);
        image=v.findViewById(R.id.image2);
       v.setOnClickListener(this);
       v.setOnLongClickListener(this);
        setCheckableContainer(v.findViewById(R.id.checkLayout));

    }

    public void performBindPiece(Object obj) {

        final PieceGridBinding pieceBinding= DataBindingUtil.bind(frontView);

        pieceBinding.setVariable(BR.obj,obj);

        pieceBinding.notifyChange();
        pieceBinding.executePendingBindings();

   /*    final PieceInfodBinding pieceInfoBinding= DataBindingUtil.bind(backView);

        pieceInfoBinding.setVariable(BR.obj,obj);
        pieceInfoBinding.executePendingBindings();

        pieceInfoBinding.notifyChange();

        setPreview(pieceBinding.getRoot(),(Piece)obj);*/


    }


    public static PieceCheckableListViewHolder create(ViewGroup parent, Utils.ViewMode viewMode) {

        ViewDataBinding viewDataBinding;
        switch (viewMode) {


            case LIST:

                View view1=  LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.piece_flippable_item, parent, false);
                return new  PieceCheckableListViewHolder (view1);
            case GRID:
                View view=  LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.piece_flippable_item, parent, false);
                return new  PieceCheckableListViewHolder (view);

            default:

                return null;
        }

    }




    public void setPreview(View header, Piece piece) {

        if (piece.getImage() != null) {
            image.setImageDrawable(piece.getImage());

        }else{

            image.setImageDrawable(header.getContext().getDrawable(R.drawable.ic_info_grey_24dp));

        }


    }
}






