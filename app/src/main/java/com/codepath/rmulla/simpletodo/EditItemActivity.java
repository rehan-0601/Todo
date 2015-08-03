package com.codepath.rmulla.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Selection;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditItemActivity extends AppCompatActivity {

    private final int RESULT_OK = 200;

    EditText etEditItem;
    String itemValue;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        String itemValue = getIntent().getStringExtra("item");
        position = getIntent().getIntExtra("position", 0);
        etEditItem = (EditText)findViewById(R.id.etEditItem);
        etEditItem.setText(itemValue);
        Selection.setSelection(etEditItem.getText(), etEditItem.getText().length());
        setTitle("Edit Item");

        //Toast.makeText(this, itemValue, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);
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

    public void onSubmit(View view) {
        etEditItem = (EditText)findViewById(R.id.etEditItem);
        Intent data = new Intent();
        data.putExtra("updatedItem", etEditItem.getText().toString());
        data.putExtra("position", position);
        setResult(RESULT_OK, data);
        finish();
    }
}
