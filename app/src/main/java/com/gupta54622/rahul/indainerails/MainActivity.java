package com.gupta54622.rahul.indainerails;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    protected static TextView textViewStatus;
    private EditText editTextPnr;
    protected static Button buttonCheckStatus;
    protected static ProgressBar progressBar;
    protected static CardView cardView;
    protected  static FloatingActionButton floatingActionButtonCancelChecking;

    private DownloadTask downloadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();


        buttonCheckStatus.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {

                //clear text view status
                textViewStatus.setText("");

                String pnr = editTextPnr.getText().toString().trim().replace("/", "");

                // check the length of pnr
                if(pnr.length() != 10){

                    // Button was clicked/tapped
                    String message = "PNR LENGTH SHOULD BE 10!!";
                    int duration = Snackbar.LENGTH_SHORT;

                    closeKeyBoard();
                    showSnackbar(view, message, duration);

                    return;

                }

                progressBar.setVisibility(View.VISIBLE);
                floatingActionButtonCancelChecking.setVisibility(View.VISIBLE);

                buttonCheckStatus.setEnabled(false);
                buttonCheckStatus.setText("checking status...");

                closeKeyBoard();

                downloadTask = new DownloadTask();
                downloadTask.execute("https://api.railwayapi.com/v2/pnr-status/pnr/" + pnr + "/apikey/tvc6hve5eg/");

            }
        });


        floatingActionButtonCancelChecking.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {

                showSnackbar(view, "canceled checking status..",  Snackbar.LENGTH_SHORT );

                try{
                downloadTask.cancel(true);
                }catch (Exception e){

                    e.printStackTrace();
                }

                buttonCheckStatus.setText("Check Status");
                buttonCheckStatus.setEnabled(true);

                progressBar.setVisibility(View.INVISIBLE);
                floatingActionButtonCancelChecking.setVisibility(View.INVISIBLE);

                cardView.setVisibility(View.INVISIBLE);
                textViewStatus.setVisibility(View.INVISIBLE);

            }
        });

    }


    void init() {

        textViewStatus = findViewById(R.id.textViewStatus);
        editTextPnr = findViewById(R.id.editTextPnr);
        buttonCheckStatus = findViewById(R.id.buttonCheckPnrStatus);
        progressBar = findViewById(R.id.progressBar);
        cardView = findViewById(R.id.cardView);
        floatingActionButtonCancelChecking = findViewById(R.id.floatingActionButtonCancelChecking);
    }

    // defining method closeKeyBoard()
    private void closeKeyBoard() {

        try {
            View view = this.getCurrentFocus();
            if (view != null) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            System.out.println("Exception generated when attempting to close keybard..");
        }
    }

    public void showSnackbar(View view, String message, int duration)
    {
        Snackbar.make(view, message, duration).show();
    }
}
