package com.android.aifoodapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.android.aifoodapp.R;

public class RecipeActivity extends AppCompatActivity {

    private Activity activity;
    private ImageButton btn_back_recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        initialize();
        setting();
        addListener();
    }

    private void initialize(){
        activity = this;
        btn_back_recipe = findViewById(R.id.btn_back_recipe);
    }

    private void setting(){

    }

    private void addListener(){
        btn_back_recipe.setOnClickListener(listener_back_click);
    }

    private View.OnClickListener listener_back_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };
}