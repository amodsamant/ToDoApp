package com.todoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.todoapp.com.todoapp.datastore.ToDo;

import java.util.List;

public class ToDoAdapter extends ArrayAdapter<ToDo> {
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
       // Lookup view for data population
       TextView todoTask = (TextView) convertView.findViewById(R.id.todoBody);
       TextView todoPriority = (TextView) convertView.findViewById(R.id.todoPriority);
       // Populate the data into the template view using the data object
        todoTask.setText(todo.taskName);
        todoPriority.setText(todo.priority.name());
       // Return the completed view to render on screen
       return convertView;
   }
}