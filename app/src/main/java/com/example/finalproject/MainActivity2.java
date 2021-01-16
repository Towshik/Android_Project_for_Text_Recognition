package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import org.apache.commons.io.FileUtils;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {
    String detectedText;
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdaptar;
    ListView detecteditems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();
        detectedText = intent.getStringExtra("message_key");
        detecteditems=(ListView) findViewById(R.id.detecteditems);
        items=new ArrayList<String>();
        readItems();
        itemsAdaptar =new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items);
        detecteditems.setAdapter(itemsAdaptar);
        itemsAdaptar.add(detectedText);
        saveItems();
        setupListViewListener();

    }

    public void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile, (String) null));
        } catch (IOException e) {
            items = new ArrayList<String>();
            e.printStackTrace();
        }
    }

    private void setupListViewListener(){
        detecteditems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                items.remove(position);
                itemsAdaptar.notifyDataSetChanged();
                saveItems();
                return true;
            }
        });

    }

    public void saveItems(){
        File filesDir=getFilesDir();
        File todoFile=new File(filesDir,"todo.txt");
        try {
            FileUtils.writeLines(todoFile,items);
        }catch(IOException e){
            e.printStackTrace();
        }

    }
}