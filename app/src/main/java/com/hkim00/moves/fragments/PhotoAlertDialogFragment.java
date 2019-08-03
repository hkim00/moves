package com.hkim00.moves.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class PhotoAlertDialogFragment extends DialogFragment {

    public PhotoAlertDialogFragment() { } //empty constructor is required

    public static PhotoAlertDialogFragment newInstance(String title) {
        PhotoAlertDialogFragment frag = new PhotoAlertDialogFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }


    public interface PhotoDialogListener {
        void onFinishDialog(boolean fromCamera);
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Profile Photo");
        alertDialogBuilder.setMessage("Where do you want to get your photo from?");
        alertDialogBuilder.setPositiveButton("Camera", (dialog, which) -> {
            PhotoDialogListener listener = (PhotoDialogListener) getActivity();
            listener.onFinishDialog(true);
            dismiss();
        });
        alertDialogBuilder.setNegativeButton("Photo Library", (dialog, which) -> {
            PhotoDialogListener listener = (PhotoDialogListener) getActivity();
            listener.onFinishDialog(false);
            dismiss();
        });

        return alertDialogBuilder.create();
    }

}
