package com.codepath.rmulla.simpletodo;
import java.util.Date;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by rmulla on 8/7/15.
 */
@Table(name = "TodoItems")
public class TodoItem extends Model {
    /*
    Also note that ActiveAndroid creates a local id (Id) in addition to our manually managed remoteId (unique)
    which is the id on the server (for networked applications). To access that primary key Id, you can call getId()
    on an instance of your model.
     */

    @Column(name = "Name")
    public String text;

    @Column(name = "DueDate")
    public String dueDate;

    @Column(name = "Priority")
    public Integer priority;

    @Column(name = "isCompleted")
    public boolean isCompleted;

    public TodoItem(){
        super();
    }

    public TodoItem(String todoText, String dueDate, Integer priority, boolean isCompleted){
        this.text = todoText;
        this.dueDate = dueDate;
        this.priority = priority;
        this.isCompleted = isCompleted;
    }


}
