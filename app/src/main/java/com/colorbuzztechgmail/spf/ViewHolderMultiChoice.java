package com.colorbuzztechgmail.spf;

import android.databinding.ViewDataBinding;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Checkable;


public class ViewHolderMultiChoice extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{



        ItemClickListener itemClickListener;
        MultiChoiceHelper multiChoiceHelper;
        ViewDataBinding mBinding;
        int mode;
        View checkableContainer;

        public ViewHolderMultiChoice(ViewDataBinding itemsBinding,int mode) {

        super(itemsBinding.getRoot());


        this.mode = mode;
        this.mBinding = itemsBinding;

    }

    public ViewHolderMultiChoice(View v) {

        super(v);


        this.mode = mode;

    }


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public View getCheckableContainer() {
        return checkableContainer;
    }

    public void setCheckableContainer(View checkableContainer) {

            if(checkableContainer!=null) {

                this.checkableContainer = checkableContainer;
                this.checkableContainer.setOnClickListener(this);
                this.checkableContainer.setOnLongClickListener(this);
            }
    }

    public void performBind(Object obj){

            if(getBinding()!=null){


                getBinding().setVariable(BR.obj,obj);
                getBinding().executePendingBindings();
                getBinding().notifyChange();


            }
    }



    @Override
        public void onClick(View v) {

       if (itemClickListener != null) {


                    itemClickListener.onClick(v,getAdapterPosition(),false);

                }


        }

        @Override
        public boolean onLongClick(View v) {

         if(itemClickListener!=null){

             itemClickListener.onClick(v,getAdapterPosition(),true);

         }

         return true;
      }


        public ViewDataBinding getBinding() {

            return mBinding;
        }

        public void updateCheckedState(long itemId) {
            final boolean isChecked = multiChoiceHelper.isItemChecked(itemId);

            if(checkableContainer!=null) {


                if (getCheckableContainer() instanceof Checkable) {
                    ((CheckableLinearLayout) getCheckableContainer()).setChecked(isChecked);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    getCheckableContainer().setActivated(isChecked);
                }

            }
        }



        public void bind(MultiChoiceHelper multiChoiceHelper, long itemId) {
            this.multiChoiceHelper = multiChoiceHelper;
            if (multiChoiceHelper != null) {
                updateCheckedState(itemId);
            }
        }

        public boolean isMultiChoiceActive() {
            return (multiChoiceHelper != null) && (multiChoiceHelper.getCheckedItemCount() > 0);
        }

    }


