package com.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditTodoActivity extends AppCompatActivity {

    EditText todoEditText;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo);

        String todoText = getIntent().getStringExtra("todoText");
        position = getIntent().getIntExtra("position", 0);


        todoEditText = (EditText)findViewById(R.id.etTodo);

        todoEditText.setText(todoText);
        todoEditText.setSelection(todoEditText.getText().length());

    }

    public void saveTodo(View v) {

        Intent data = new Intent();
        data.putExtra("todoText", todoEditText.getText().toString());
        data.putExtra("position",position);
        setResult(RESULT_OK, data);
        finish();


    }
}
