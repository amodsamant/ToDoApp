package com.todoapp.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.todoapp.R;
import com.todoapp.adapters.ToDoAdapter;
import com.todoapp.fragments.AddEditTodoDialogFragment;
import com.todoapp.models.PriorityEnum;
import com.todoapp.models.ToDo;
import com.todoapp.models.ToDo_Table;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainToDoActivity extends AppCompatActivity
        implements AddEditTodoDialogFragment.AddEditToDoDialogListener {

    private List<ToDo> toDoList;
    private ToDoAdapter adapter;
    private ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // read all the items in the database
        toDoList = readAllItems();

        lvItems = (ListView)findViewById(R.id.lvItems);

        // Create the adapter to convert the array to views
        adapter = new ToDoAdapter(this, toDoList);

        // Attach the adapter to a ListView
        lvItems.setAdapter(adapter);

        setupListViewListener();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddItem(view);
            }
        });
    }

    /**
     * Function to call add edit dialog fragment
     * @param v
     */
    public void onAddItem(View v) {

        AddEditTodoDialogFragment editTodoDialogFragment =
                AddEditTodoDialogFragment.newInstance(-1);
        editTodoDialogFragment.show(getSupportFragmentManager(), "fragment_edit_todo");


    }

    /**
     * List view listener to handle on item click and long click
     */
    private void setupListViewListener() {

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View item, int pos, long id) {

                AddEditTodoDialogFragment editTodoDialogFragment =
                        AddEditTodoDialogFragment.newInstance(adapter.getItem(pos).taskName,pos,
                        adapter.getItem(pos).id,adapter.getItem(pos).priority,
                                adapter.getItem(pos).dueDate);
                editTodoDialogFragment.show(getSupportFragmentManager(), "fragment_edit_todo");

            }
        });

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View item, int pos, long id) {

                deleteItem(adapter.getItem(pos).id, pos);
                return true;
            }
        });
    }

    /**
     * Function reads all items from the db and returns them with priority of high, medium and low
     * respectively and also runs a sort on date on top of it if same priorities.
     * @return List of ToDo objects
     */
    private List<ToDo> readAllItems() {

        List<ToDo> allTasks = SQLite.select().
                from(ToDo.class).queryList();

        final String ORDER = "HML";
        Collections.sort(allTasks, new Comparator<ToDo>() {
            @Override
            public int compare(ToDo o1, ToDo o2) {

                if(ORDER.indexOf(o1.priority.name().charAt(0)) ==
                        ORDER.indexOf(o2.priority.name().charAt(0))) {

                    return o1.dueDate.compareTo(o2.dueDate);
                } else {
                    return ORDER.indexOf(o1.priority.name().charAt(0)) -
                            ORDER.indexOf(o2.priority.name().charAt(0));
                }
            }
        });

        for(ToDo task: allTasks) {
            if(Long.valueOf(task.id) >= ToDo.incrementalId) {
                ToDo.incrementalId = Long.valueOf(task.id);
            }
        }

        return allTasks;
    }

    /**
     * Function to write the ToDo data object and update the list adapter
     * @param data
     */
    private void writeItem(ToDo data) {

        data.save();

        toDoList.clear();
        toDoList.addAll(readAllItems());
        adapter.notifyDataSetChanged();

    }

    /**
     * Function to update the item with the given id to the updated taskName , priority and
     * dueDate
     * @param taskName
     * @param id
     * @param priority
     * @param dueDate
     */
    private void updateItem(String taskName, String id, PriorityEnum priority, Date dueDate) {

        SQLite.update(ToDo.class)
                .set(ToDo_Table.TaskName.eq(taskName),ToDo_Table.Priority.eq(priority),
                        ToDo_Table.DueDate.eq(dueDate))
                .where(ToDo_Table.Id.is(id)).execute();

        toDoList.clear();
        toDoList.addAll(readAllItems());
        adapter.notifyDataSetChanged();

    }

    /**
     * Function deletes the item with the given id
     * @param id
     * @param position
     */
    private void deleteItem(String id, int position) {

        SQLite.delete(ToDo.class).where(ToDo_Table.Id.is(id)).execute();
        toDoList.remove(position);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onFinishEditDialog(String taskName, PriorityEnum priority, String id,
                                   Date dueDate) {
        updateItem(taskName, id, priority, dueDate);
    }

    @Override
    public void onFinishAddDialog(ToDo data) {
        writeItem(data);
    }


}
