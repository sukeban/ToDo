package com.sukeban.todo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class ToDoActivity extends Activity{ 
	
	private ArrayList<String> todoItems;
	private ArrayAdapter<String> todoAdapter;
	private ListView lvItems;
	private EditText etNewItem;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
                
    	etNewItem = (EditText)findViewById(R.id.etNewItem);
    	
        lvItems = (ListView)findViewById(R.id.lvItems);
        reloadItems();

        todoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);
        lvItems.setAdapter(todoAdapter);
        
        setupListViewListener();        
    }
    
    private void setupListViewListener() {    	
    	lvItems.setOnItemLongClickListener(new OnItemLongClickListener() {
    		@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View view,
					int pos, long id) {
				todoItems.remove(pos);
				todoAdapter.notifyDataSetChanged();
				saveItems();
				return false;
			}
    	});
    }
    
    private void reloadItems() {
    	File filesDir = getFilesDir();
    	File todoFile = new File(filesDir, "todo.txt");
    	try {
    		todoItems = new ArrayList<String>(FileUtils.readLines(todoFile));
    		
    	} catch (IOException e) {
    		todoItems = new ArrayList<String>();
    		e.printStackTrace();
    	}
    }
    
    private void saveItems() {
    	File filesDir = getFilesDir();
    	File todoFile = new File(filesDir, "todo.txt");
    	try {
    		FileUtils.writeLines(todoFile, todoItems);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    public void addTodoItem(View v) {
    	String itemText = etNewItem.getText().toString();
    	todoAdapter.add(itemText);
		saveItems();
    	etNewItem.setText("");
    }
    
}
