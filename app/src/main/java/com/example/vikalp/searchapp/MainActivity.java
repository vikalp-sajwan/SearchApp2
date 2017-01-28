package com.example.vikalp.searchapp;

import android.Manifest;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    // GUI elements layout 1
    EditText editText;
    CheckBox checkBoxName;
    CheckBox checkBoxDOM;
    DatePicker datePicker;
    Button button;

    // GUI elements layout 2
    TextView textView;

    FileFinder fileFinder;

    ArrayList<File> result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // getting runtime permission for reading storage on marshmallow and above
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"},1);
        }

        // binding GUI elements in Layout 1
        editText = (EditText) findViewById(R.id.editText);
        checkBoxName = (CheckBox) findViewById(R.id.checkBoxName);
        checkBoxDOM = (CheckBox) findViewById(R.id.checkBoxDOM);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        button = (Button) findViewById(R.id.button);

        fileFinder = FileFinder.getObject();
    }


    /**
     * called on clicking Search button in initial Layout
     *
     * @param view
     */
    public void search(View view) {
        if (checkBoxDOM.isChecked() || checkBoxName.isChecked()) {

            String searchString = editText.getText().toString();
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth();
            int year = datePicker.getYear();

            if (checkBoxName.isChecked()) {
                if (searchString.trim().isEmpty()) {
                    Toast t = Toast.makeText(getApplicationContext(), "please enter valid search string", Toast.LENGTH_SHORT);
                    t.show();
                    return;
                }
            }

            if (!checkBoxDOM.isChecked()) {
                result = fileFinder.searchByName(searchString);
            } else if (!checkBoxName.isChecked()) {
                result = fileFinder.searchByDOM(day, month, year);
            } else {
                result = fileFinder.searchByNameDOM(searchString, day, month, year);
            }


            Toast t = Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT);
            t.show();

        } else {
            Toast t = Toast.makeText(getApplicationContext(), "please select at least one of checkboxes", Toast.LENGTH_SHORT);
            t.show();
            return;
        }

        // displaying Layout 2
        setContentView(R.layout.activity_search_results);
        // binding textView element in Layout 2
        textView = (TextView) findViewById(R.id.TextViewResult);
        textView.setText("SEARCH RESULTS :");
        if (result.isEmpty()) {
            textView.append("\n No match found");
        } else {
            for (File entry : result) {
                textView.append("\n\n" + entry.toString());
            }
        }

    }
}
