package com.colorbuzztechgmail.spf;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

/**
 * A generic undo/redo implementation for TextViews.
 */
public class TextViewUndoRedo {

    /**
     * Is undo/redo being performed? This member signals if an undo/redo
     * operation is currently being performed. Changes in the text during
     * undo/redo are not recorded because it would mess up the undo history.
     */
     private String mBeforeChange;

    /**
     * The change listener.
     */
    private EditTextChangeListener mChangeListener;

    /**
     * The edit text.
     */
    private TextView mTextView;

    private View mUndoRedoView;

    // =================================================================== //

    /**
     * Create a new TextViewUndoRedo and attach it to the specified TextView.
     *
     * @param textView
     *            The text view for which the undo/redo is implemented.
     *
     * @param undoRedoView
     *            The   view for  undo action.
     */
    public TextViewUndoRedo(TextView textView,View undoRedoView) {
        mTextView = textView;
        mBeforeChange=textView.getText().toString();
        mUndoRedoView=undoRedoView;
        mChangeListener = new EditTextChangeListener(mBeforeChange.toString(),mUndoRedoView);
        mTextView.addTextChangedListener(mChangeListener);

        mUndoRedoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                undo();
                 view.setVisibility(View.GONE);
            }
        });
    }


    /**
     * Perform undo.
     */
    public void undo() {

        mTextView.setText(mBeforeChange);

    }


    private final class EditTextChangeListener implements TextWatcher {

        private String mBeforeChange;
        private View mUndoRedoView;
        public EditTextChangeListener(String mBeforeChange,View mUndoView) {

            this.mBeforeChange=mBeforeChange;
            this.mUndoRedoView=mUndoView;
        }


        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }

        public void afterTextChanged(Editable s) {


            if(!(mBeforeChange.equals(s.toString()))){

                mUndoRedoView.setVisibility(View.VISIBLE);


            }else{

                mUndoRedoView.setVisibility(View.GONE);


            }



        }
    }
}