package com.sukeban.todo;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;

public class ToDoActivity extends Activity{

    // TodoDatabaseHandler is a SQLiteOpenHelper class connecting to SQLite
    private TodoItemDatabaseHandler dbHandler;

    private TodoCursorAdapter todoAdapter;
    private ListView lvItems;

    private EditText etNewItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);

        etNewItem = (EditText)findViewById(R.id.etNewItem);

        lvItems = (ListView)findViewById(R.id.lvItems);
        dbHandler = new TodoItemDatabaseHandler(this);

        Cursor todoCursor = getNewCursor();
        todoAdapter = new TodoCursorAdapter(this, todoCursor);
        lvItems.setAdapter(todoAdapter);

        setupListViewListener();
    }

    private Cursor getNewCursor()
    {
        // Query for items from the database and get a cursor back
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor todoCursor = db.rawQuery("SELECT  * FROM todo_items", null);
        return todoCursor;
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View view, int pos, long id) {

                TodoItem item = dbHandler.getTodoItem((Integer) view.getTag());
                dbHandler.deleteTodoItem(item);

                Cursor todoCursor = getNewCursor();
                todoAdapter.swapCursor(todoCursor);

                return false;
            }
        });
    }

    public void addTodoItem(View v) {
        String itemText = etNewItem.getText().toString();

        dbHandler.addTodoItem(new TodoItem(itemText, 1));
        Cursor todoCursor = getNewCursor();
        todoAdapter.swapCursor(todoCursor);

        etNewItem.setText("");
    }

}
