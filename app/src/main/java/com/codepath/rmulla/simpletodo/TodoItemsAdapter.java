package com.codepath.rmulla.simpletodo;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import android.widget.Toast;

/**
 * Created by rmulla on 8/7/15.
 */
class TodoItemsAdapter extends ArrayAdapter<TodoItem> {
    public TodoItemsAdapter(Context context, ArrayList<TodoItem> todoItems){
        super(context,0, todoItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        // Get the data item for this position
        TodoItem todoItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.todo_item, parent, false);
        }
        // Lookup view for data population
        TextView etItem = (TextView) convertView.findViewById(R.id.etItem);
        TextView tvPriority = (TextView) convertView.findViewById(R.id.tvPriority);
        TextView etDate = (TextView) convertView.findViewById(R.id.etDate);
        // Populate the data into the template view using the data object
        etItem.setText(todoItem.text);
        //int year = todoItem.dueDate.getYear()+1900;
        //if the dueDate == today, then display today in Red
        Calendar calendar = Calendar.getInstance();
        //Toast.makeText(getContext(), ""+calendar.get(Calendar.DAY_OF_MONTH) + calendar.get(Calendar.MONTH)+ calendar.get(Calendar.YEAR)+"",Toast.LENGTH_SHORT).show();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH)+1;
        int year = calendar.get(Calendar.YEAR);
        etDate.setText(todoItem.dueDate);
        if(!todoItem.dueDate.equals("-")){
            if(Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)).equals( todoItem.dueDate.split("/")[1]) && Integer.toString(calendar.get(Calendar.MONTH)+1).equals(todoItem.dueDate.split("/")[0]) && Integer.toString(calendar.get(Calendar.YEAR)).equals(todoItem.dueDate.split("/")[2])) {
                etDate.setTextColor(Color.RED);
            }
            else etDate.setTextColor(Color.BLACK);
        }
        tvPriority.setText(todoItem.priority.toString());
        //Toast.makeText(getContext(), todoItem.priority.toString(), Toast.LENGTH_SHORT).show();
        if(todoItem.priority == 1) {
            tvPriority.setTextColor(Color.RED);
        }
        else if (todoItem.priority == 2){
            //RGB for orange
            tvPriority.setTextColor(Color.rgb(255,165,0 ));
        }
        else tvPriority.setTextColor(Color.YELLOW);

        // Return the completed view to render on screen
        return convertView;


    }
}
