package com.codepath.rmulla.simpletodo;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements EditItemDialog.EditItemDialogListener {
    private final int REQUEST_CODE = 20;
    int editposition;
    //ArrayList<String> items;
    ArrayList<TodoItem> items = new ArrayList<>();
    //ArrayAdapter<String> itemsAdapter;
    TodoItemsAdapter itemsAdapter;
    ListView lvItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Simple Todo");
        lvItems = (ListView)findViewById(R.id.lvItems);
        //items = new ArrayList<String>();
        readItemsFromDB();
        /*readItems();*/
        //itemsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, items);
        itemsAdapter = new TodoItemsAdapter(this, items);
        lvItems.setAdapter(itemsAdapter);
        //items.add("First Item");
        //items.add("Second Item");
        Date today = Calendar.getInstance().getTime();
        //itemsAdapter.add(new TodoItem("todo5", today, 1, false));
        //itemsAdapter.add(new TodoItem("todo2", today, 2, false));

        setupListViewListener();
    }

    private void setupListViewListener(){
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        writeItemsToDB();
                        /*writeItems();*/
                        return true;
                    }

                });
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Not Needed since we're using the EditItem Fragment/Dialog now instead of a new activity
                //launchEditActivityView(position);
                TodoItem currentItem = items.get(position);
                //saving the position of the item, will need this once i return from the dialog
                editposition = position;
                showEditDialog(currentItem);

            }
        });
    }

    private void showEditDialog(TodoItem currentItem) {
        FragmentManager fm = getSupportFragmentManager();
        EditItemDialog editItemDialog = EditItemDialog.newInstance("Edit Todo Item", currentItem);
        editItemDialog.show(fm, "fragment_edit_item");
    }

    private void launchEditActivityView(int position){
        Intent i = new Intent(MainActivity.this, EditItemActivity.class);
        //String itemValue = items.get(position);
        TodoItem itemValue = items.get(position);
        i.putExtra("item", itemValue.text);
        i.putExtra("position", position);
        startActivityForResult(i, REQUEST_CODE);
    }

    private void readItemsFromDB(){
        List<TodoItem> readItems = new Select()
                .from(TodoItem.class)
                .execute();

        items.addAll(readItems);
    }

    private void writeItemsToDB(){
        //items has been updated in memory prior to calling this function.
        //Run a transaction and update DB with current items from mem
        //delete DB before that to avoid duplication-
        //fix thisTODO-this is very inefficient

        new Delete().from(TodoItem.class).execute();
        ActiveAndroid.beginTransaction();
        try{
            for(int i=0; i <items.size();i++){
                items.get(i).save();
            }
            ActiveAndroid.setTransactionSuccessful();
        }
        finally {
            ActiveAndroid.endTransaction();
        }
    }
    /*
    private void readItems(){
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch(IOException e){
            items = new ArrayList<String>();
        }
    }
    */
    /*
    private void writeItems(){
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try{
            FileUtils.writeLines(todoFile, items);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void onAddItem(View v){
        EditText etNewItem = (EditText)findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        //add the new text to the adapter. this will render it.

        TodoItem newTodoItem = new TodoItem(itemText, "-", 1, false);
        items.add(newTodoItem);
        //clear off the editText box
        etNewItem.setText("");
        /*writeItems();*/
        writeItemsToDB();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        //handle each request's response here
        if(requestCode == REQUEST_CODE && resultCode == 200){
            //retreive data sent by EditItem View
            String updatedItemValue = data.getExtras().getString("updatedItem");
            int position = data.getIntExtra("position", 0);
            //update the ArrayList, notify changes, write out changes to file
            //update the oldItem with the new values and then set
            TodoItem oldItem = items.get(position);
            oldItem.text = updatedItemValue;
            items.set(position,oldItem);
            itemsAdapter.notifyDataSetChanged();
            /*writeItems();*/
            writeItemsToDB();

        }
    }

    @Override
    public void onFinishEditDialog(String todoItemText, int priority, boolean isCompleted, String date) {
        //Toast.makeText(this, "Hi, " + todoItemText, Toast.LENGTH_SHORT).show();
        TodoItem editedItem = items.get(editposition);
        editedItem.text=todoItemText;
        editedItem.priority=priority;
        editedItem.isCompleted=isCompleted;
        editedItem.dueDate=date;
        //save updated item to DB. notify adapter of changes.
        editedItem.save();
        itemsAdapter.notifyDataSetChanged();

    }

}
