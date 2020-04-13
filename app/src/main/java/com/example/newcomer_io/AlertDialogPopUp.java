package com.example.newcomer_io;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;

class AlertDialogPopUp extends DialogFragment {
    public AlertDialogPopUp() {
        // Empty constructor required for DialogFragment
    }

    public static AlertDialogPopUp newInstance(String title, String message,String positiveButton,String negativeButton) {
        AlertDialogPopUp frag = new AlertDialogPopUp();
        Bundle args = new Bundle();
        //AIzaSyAjGcF4XC-OEVJHKPmPefDUxGjxiSCbFK8
        args.putString("title", title);
        args.putString("message", message);
        args.putString("positivebutton", positiveButton);
        args.putString("negativebuttton",negativeButton);

        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String title = getArguments().getString("title");
        String message = getArguments().getString("message");
        String positivebutton = getArguments().getString("positivebutton");
        String negativebutton = getArguments().getString("negativebutton");

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);

        alertDialogBuilder.setPositiveButton(positivebutton,  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // on success
            }
        });
        alertDialogBuilder.setNegativeButton(negativebutton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }

        });

        return alertDialogBuilder.create();
    }
}