package net.msonic.testsyncdata.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import net.msonic.testsyncdata.R;

/**
 * Created by manuelzegarra on 28/02/16.
 */
public class PedidoItemEdit extends DialogFragment implements TextView.OnEditorActionListener {

    public PedidoItemEdit(){

    }


    public interface UserNameListener {
        void onFinishUserDialog(String user);
    }




    private EditText mEditText;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pedido_item_edit, container);
        //mEditText = (EditText) view.findViewById(R.id.username);

        // set this instance as callback for editor action
        //mEditText.setOnEditorActionListener(this);
        //mEditText.requestFocus();
        //getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        //getDialog().setTitle("Please enter username");

        return view;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        // Return input text to activity
       // UserNameListener activity = (UserNameListener) getActivity();
        //activity.onFinishUserDialog(mEditText.getText().toString());

        UserNameListener activity = (UserNameListener) getActivity();
        activity.onFinishUserDialog("OK");

        this.dismiss();
        return true;
    }



}
