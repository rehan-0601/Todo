package com.codepath.rmulla.simpletodo;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;


/**
 * Created by rmulla on 8/9/15.
 */
public class EditItemDialog extends DialogFragment implements  View.OnClickListener{//OnItemSelectedListener,
    private EditText etItem;
    private Spinner spinner;
    private DatePicker datePicker;

    public EditItemDialog(){
        //empty constructor required for dialog fragment
    }

    public interface EditItemDialogListener {
        void onFinishEditDialog(String todoItemText, int priority, boolean isCompleted, String date);

    }


    public static EditItemDialog newInstance(String title, TodoItem currentItem) {
        EditItemDialog frag = new EditItemDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("currText", currentItem.text);
        args.putInt("currPriority", currentItem.priority);
        args.putBoolean("currIsCompleted", currentItem.isCompleted);
        args.putString("dueDate", currentItem.dueDate);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_item, container);

        Button btnSave = (Button)view.findViewById(R.id.btnSave);
        //etItem = (EditText)view.findViewById(R.id.etEditItem);
        datePicker = (DatePicker)view.findViewById(R.id.datePicker);
        spinner = (Spinner)view.findViewById(R.id.spinner);
        //Create an ArrayAdapter to populate values in the spinner
        ArrayList<String> spinnerOptions = new ArrayList<>();
        spinnerOptions.add("1");
        spinnerOptions.add("2");
        spinnerOptions.add("3");
        ArrayAdapter<String> spinnerAdapter=new ArrayAdapter<>(view.getContext(),android.R.layout.simple_spinner_item, spinnerOptions);
        // Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //apply the adapter to the spinner
        spinner.setAdapter(spinnerAdapter);
        //set the listener-dont need this
        //spinner.setOnItemSelectedListener(this);

        etItem = (EditText) view.findViewById(R.id.etItem);
        String title = getArguments().getString("title", "Edit Item");
        getDialog().setTitle(title);

        //gather all the args you passed in. and set in the view
        String currText = getArguments().getString("currText");
        Integer currPriority = getArguments().getInt("currPriority");
        //Integer priority_for_spinner = Integer.parseInt(currPriority);
        etItem.setText(currText);
        spinner.setSelection(currPriority-1);

        String date = getArguments().getString("dueDate");
        if(date.equals("-")){
            //if date is set to "-", means this is a new item. pick the current date for the date picker

        }
        else{
            //set the date to what was passed. 'MM/DD/YYYY'
            String[] date_parts = date.split("/");
            String month = date_parts[0];
            String day = date_parts[1];
            String year = date_parts[2];
            datePicker.updateDate(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day));
        }


        //Add click handler on save button
        btnSave.setOnClickListener(this);

        // Show soft keyboard automatically
        etItem.requestFocus();

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return view;
    }

    /*public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        Toast.makeText(view.getContext(),
                "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
                Toast.LENGTH_SHORT).show();
    }*/

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    @Override
    public void onClick(View v){
        EditItemDialogListener listener = (EditItemDialogListener) getActivity();
        listener.onFinishEditDialog(etItem.getText().toString(),spinner.getSelectedItemPosition()+1,
                                    false,datePicker.getMonth()+1+"/"+datePicker.getDayOfMonth()+"/"+datePicker.getYear());
        dismiss();
    }
}


