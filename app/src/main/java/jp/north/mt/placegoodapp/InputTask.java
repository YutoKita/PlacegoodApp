package jp.north.mt.placegoodapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class InputTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_input);

        Intent intent = getIntent();
        double value1 = intent.getDoubleExtra("VALUE1", 0);

        TextView textView = (TextView) findViewById(R.id.latitudeTextView);
        textView.setText(String.valueOf(value1));


    }
}
