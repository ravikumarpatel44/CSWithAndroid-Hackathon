package com.example.sandeepkumar.appagame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import java.util.Random;

public class instruction extends AppCompatActivity implements View.OnClickListener {

    ViewFlipper viewFlipper;
    Button btnnext;
    Button  btnhome;
    Button btnprevious;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);

        viewFlipper =(ViewFlipper) findViewById(R.id.viewflipper);
        btnnext = (Button) findViewById(R.id.next);
        btnprevious = (Button) findViewById(R.id.previous);
        btnhome =(Button) findViewById(R.id.home);

        btnnext.setOnClickListener(this);
        btnprevious.setOnClickListener(this);
        btnhome.setOnClickListener(this);


    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

        if(v == btnnext)
        {
            viewFlipper.showNext();
        }
        else if(v == btnprevious){
            viewFlipper.showPrevious();
        }
        else {
            Intent intent =new Intent(instruction.this , MainActivity.class);
            startActivity(intent);
        }

    }
}
