package com.todoapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.todoapp.R;
import com.todoapp.utils.ToDoConstants;
import com.todoapp.models.PriorityEnum;
import com.todoapp.models.ToDo;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.todoapp.R.id.todoPriority;

public class ToDoAdapter extends ArrayAdapter<ToDo> {

    Date dueDate;

    public ToDoAdapter(Context context, List<ToDo> toDos) {
       super(context, 0, toDos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ToDo todo = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
           convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_todo, parent, false);
        }

        TextView todoTaskTV = (TextView) convertView.findViewById(R.id.todoBody);
        TextView todoPriorityTV = (TextView) convertView.findViewById(todoPriority);
        TextView dueDateTV = (TextView) convertView.findViewById(R.id.todoDueDate);
        todoTaskTV.setText(todo.taskName);
        todoTaskTV.setTextColor(Color.DKGRAY);

        setPriorityText(todoPriorityTV, todo.priority);

        if(todo.dueDate==null) {
            dueDate = Calendar.getInstance().getTime();
        } else {
            dueDate = todo.dueDate;
        }

        String dueBy = ToDoConstants.DATE_FORMAT.format(dueDate);

        Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.DAY_OF_YEAR, 1);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(dueDate);

        Calendar c3 = Calendar.getInstance();
        c3.add(Calendar.DAY_OF_YEAR, 0);

        dueDateTV.setTextColor(Color.DKGRAY);
        if(c3.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c3.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)) {
            dueBy = ToDoConstants.TODAY;
        } else if(c3.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c3.get(Calendar.DAY_OF_YEAR) > c2.get(Calendar.DAY_OF_YEAR)){
            dueBy = ToDoConstants.OVERDUE;
            dueDateTV.setTextColor(Color.RED);
        } else if(c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)) {
                dueBy = ToDoConstants.TOMORROW;
        }

        dueDateTV.setText(dueBy);

       return convertView;
   }

    /**
     * Function sets the priority text view with the priority and color
     * @param todoPriorityTV
     * @param priority
     */
    public void setPriorityText(TextView todoPriorityTV, PriorityEnum priority) {

        todoPriorityTV.setText(priority.name());

        switch (PriorityEnum.valueOf(priority.name())) {
            case LOW:
                todoPriorityTV.setTextColor(ContextCompat.getColor(getContext(),
                        R.color.mediumPriorityColor));
                break;
            case MEDIUM:
                todoPriorityTV.setTextColor(ContextCompat.getColor(getContext(),
                        R.color.lowPriorityColor));
                break;
            case HIGH:
                todoPriorityTV.setTextColor(Color.RED);
                break;
        }

    }
}