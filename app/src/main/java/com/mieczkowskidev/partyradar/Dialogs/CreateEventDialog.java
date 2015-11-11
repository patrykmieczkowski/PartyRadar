package com.mieczkowskidev.partyradar.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.mieczkowskidev.partyradar.MainActivity;
import com.mieczkowskidev.partyradar.R;

/**
 * Created by Patryk Mieczkowski on 2015-11-11.
 */
public class CreateEventDialog extends Dialog implements
        android.view.View.OnClickListener {

    public Activity activity;
    public Dialog d;
    public Button createButton;
    public EditText descriptionEdit;
    public ProgressBar progressBar;


    public CreateEventDialog(Activity a) {
        super(a);
        this.activity = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.create_event_dialog);
        createButton = (Button) findViewById(R.id.create_event_button);
        progressBar = (ProgressBar) findViewById(R.id.create_event_progress_bar);
        descriptionEdit = (EditText) findViewById(R.id.event_description_edit);

        createButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_event_button:
                createEvent();
                break;
//            case R.id.btn_no:
//                dismiss();
//                break;
            default:
                break;
        }
//        dismiss();
    }

    private void createEvent(){

        descriptionEdit.clearFocus();
        createButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        ((MainActivity) activity).createPostOnServer(descriptionEdit.getText().toString());

    }

}