package com.todoapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.todoapp.com.todoapp.datastore.PriorityEnum;
import com.todoapp.com.todoapp.datastore.StatusEnum;
import com.todoapp.com.todoapp.datastore.ToDo;
import com.todoapp.com.todoapp.datastore.ToDo_Table;

import java.util.List;

public class MainToDoActivity extends AppCompatActivity implements EditTodoDialogFragment.EditToDoDialogListener {

    List<ToDo> toDoList;
    ToDoAdapter adapter;
    ListView lvItems;

    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_to_do);

        toDoList = readAllItems();
        lvItems = (ListView)findViewById(R.id.lvItems);

        // Create the adapter to convert the array to views
        adapter = new ToDoAdapter(this, toDoList);

        // Attach the adapter to a ListView
        lvItems.setAdapter(adapter);

        setupListViewListener();
    }

    public void onAddItem(View v) {

        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        etNewItem.setText("");
        writeItem(itemText);
    }

    private void setupListViewListener() {

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View item, int pos, long id) {

                FragmentManager fm = getSupportFragmentManager();
                EditTodoDialogFragment editTodoDialogFragment
                        = EditTodoDialogFragment.newInstance(adapter.getItem(pos).taskName,pos,
                        adapter.getItem(pos).id,adapter.getItem(pos).priority);
                editTodoDialogFragment.show(fm, "fragment_edit_todo");

            }
        });


        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View item, int pos, long id) {

                deleteItem(adapter.getItem(pos).taskName, pos);
                return true;
            }
        });

    }


    private List<ToDo> readAllItems() {

        return SQLite.select().
                from(ToDo.class).queryList();

    }

    private void writeItem(String taskName) {

        ToDo data = new ToDo(taskName, StatusEnum.NOT_STARTED, PriorityEnum.LOW);
        data.save();

        toDoList.add(data);
        adapter.notifyDataSetChanged();

    }

    private void updateItem(String taskName, String id, PriorityEnum priority) {

        SQLite.update(ToDo.class)
                .set(ToDo_Table.TaskName.eq(taskName),ToDo_Table.Priority.eq(priority))
                .where(ToDo_Table.Id.is(id)).execute();

        toDoList.clear();
        toDoList.addAll(readAllItems());
        adapter.notifyDataSetChanged();

    }

    private void deleteItem(String taskName, int position) {

        SQLite.delete(ToDo.class).where(ToDo_Table.TaskName.is(taskName)).execute();
        toDoList.remove(position);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onFinishEditDialog(String taskName, PriorityEnum priority, String id) {
        updateItem(taskName, id, priority);
    }


}
