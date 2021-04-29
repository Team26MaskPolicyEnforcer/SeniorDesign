package com.example.maskenforcerpolicyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    EditText Name, Pass , updateold, updatenew, delete;
    public static final String TEXT = "text";
    public static final String SWITCH1 = "switch1";
    private String text;
    private boolean switch_on_off;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Spinner mySpinner = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.names));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);
        Button techSupport = (Button) findViewById(R.id.TechSupport);
        startupInstructionDialog();
        techSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Emailing to: donnyhuang322@gmail.com, alternative email: maskenforcer@gmail.com", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"donnyhuang322@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Issue in Application");
                i.putExtra(Intent.EXTRA_TEXT   , "Please state your issue here.");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 1){
                        Intent startIntent = new Intent(getApplicationContext(), NewStatsActivity.class);
                        startActivity(startIntent);

                    }
                    else if (position == 2){
                        Intent startIntent = new Intent(getApplicationContext(), RecordedActivity.class);
                        startActivity(startIntent);

                    }
                }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void startupInstructionDialog() {
        new AlertDialog.Builder(this).setTitle("Instructions for the Application")
                .setMessage("Welcome to the Mask Policy Enforcer! Click the spinner at the top to" +
                        " check the statistics of masks and people or watch footage, or click the button below to email us about" +
                        " any bugs. ").setPositiveButton("ok",(dialog, which) -> {
            dialog.dismiss();
        })
                .create().show();
    }



}