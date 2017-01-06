package com.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.todoapp.com.todoapp.datastore.PriorityEnum;

public class EditTodoActivity extends AppCompatActivity {

    EditText todoEditText;
    int position;
    String id;

    RadioGroup priorityGroup;
    RadioButton lowPriButton;
    RadioButton medPriButton;
    RadioButton highPriButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_todo);

        String todoText = getIntent().getStringExtra("todoText");
        position = getIntent().getIntExtra("position", 0);
        id = getIntent().getStringExtra("id");

        priorityGroup = (RadioGroup) findViewById(R.id.priorityGroup);

        todoEditText = (EditText)findViewById(R.id.etTodo);

        todoEditText.setText(todoText);
        todoEditText.setSelection(todoEditText.getText().length());

    }

    public void saveTodo(View view) {

        Intent data = new Intent();


        int selectedId = priorityGroup.getCheckedRadioButtonId();

        // Check which radio button was clicked
        switch(selectedId) {
            case R.id.lowPriority:
                data.putExtra("priority", PriorityEnum.LOW);
                break;
            case R.id.mediumPriority:
                data.putExtra("priority", PriorityEnum.MEDIUM);
                break;
            case R.id.highPriority:
                data.putExtra("priority", PriorityEnum.HIGH);
                break;
        }

        data.putExtra("todoText", todoEditText.getText().toString());
        data.putExtra("id",id);


        setResult(RESULT_OK, data);
        finish();


    }
}
