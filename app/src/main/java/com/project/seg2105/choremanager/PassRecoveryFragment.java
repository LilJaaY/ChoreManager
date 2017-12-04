package com.project.seg2105.choremanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class PassRecoveryFragment extends DialogFragment {

    public interface RecoveryDialogListener {
        public void onPosClick(DialogFragment dialog, String user, String recovery);
    }

    RecoveryDialogListener listener;
    View view;
    EditText userInput;
    EditText recoveryInput;
    String user;
    String recovery;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_password_recovery, null);
        userInput = view.findViewById(R.id.Username);
        recoveryInput = view.findViewById(R.id.Recovery);
        builder.setView(view);

        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                user = userInput.getText().toString();
                recovery = recoveryInput.getText().toString();
                listener.onPosClick(PassRecoveryFragment.this, user, recovery);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.setTitle("Password Recovery");

        return builder.create();
    }
}
