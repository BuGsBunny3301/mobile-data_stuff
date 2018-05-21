package com.example.bug.stuff;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper db;
    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        db = new DatabaseHelper(this);
        listView = findViewById(R.id.listView);
        arrayList = new ArrayList<>();
        populateArrayList();
        arrayAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, arrayList
        );
    }

    void populateArrayList(){
        DatabaseHelper db = new DatabaseHelper(this);
        arrayList.clear();
        Cursor data = db.getListContents();

        while(data.moveToNext()) {

            arrayList.add(data.getString(1)); //1 is column reference (item)
            arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
            listView.setAdapter(arrayAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.click) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater layoutInflater = getLayoutInflater();
            final View view = layoutInflater.inflate(R.layout.menu_layout, null);
            builder.setView(view);

            final EditText editText = view.findViewById(R.id.editText);

            builder.setTitle("Add New Task");
            builder.setMessage("What do you want to do next");
            builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    boolean insertData = db.AddData(editText.getText().toString());

                    if(insertData == true) {
                        Toast.makeText(MainActivity.this, "YOU ENTERED DATA SUCCESSFULLY!", Toast.LENGTH_LONG).show();

                    }else {
                        Toast.makeText(MainActivity.this, "SOMETHING WENT WRONG!", Toast.LENGTH_LONG).show();
                    }
                    populateArrayList();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //Do nothing
                }
            });

            builder.create().show();
        }
        return super.onOptionsItemSelected(item);
    }
}
