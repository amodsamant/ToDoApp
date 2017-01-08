package com.todoapp;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.raizlabs.android.dbflow.StringUtils;
import com.todoapp.com.todoapp.datastore.PriorityEnum;
import com.todoapp.com.todoapp.datastore.StatusEnum;
import com.todoapp.com.todoapp.datastore.ToDo;

public class EditTodoDialogFragment extends DialogFragment {

    public interface EditToDoDialogListener {
        void onFinishEditDialog(String taskName, PriorityEnum priority, String id);
        void onFinishEditDialog(ToDo data);
    }

    private EditText todoEditText;
    private String task = null;
	private int position = -1;
	private String id = null;

	private RadioGroup priorityGroup;
	private RadioButton lowPriButton;
	private RadioButton medPriButton;
	private RadioButton highPriButton;

    private Button saveButton;
    private Button cancelButton;

	public EditTodoDialogFragment() {
		// Empty constructor is required for DialogFragment
                // Make sure not to add arguments to the constructor
                // Use `newInstance` instead as shown below
	}
	
	public static EditTodoDialogFragment newInstance(String todoText, int pos, String id,
                                                     PriorityEnum priority) {
		EditTodoDialogFragment frag = new EditTodoDialogFragment();
		Bundle args = new Bundle();
		args.putString("todotext", todoText);
		args.putInt("position", pos);
		args.putString("id",id);
        args.putString("priority",priority.name());
		frag.setArguments(args);
		return frag;
	}

    public static EditTodoDialogFragment newInstance(int pos) {
        EditTodoDialogFragment frag = new EditTodoDialogFragment();
        Bundle args = new Bundle();
        args.putInt("position", pos);
        frag.setArguments(args);
        return frag;
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_edit_todo, container);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

        if(StringUtils.isNotNullOrEmpty(getArguments().getString("todotext"))) {
            task = getArguments().getString("todotext");
        }

        if(getArguments().getInt("position", 0)!= -1) {
            position = getArguments().getInt("position", 0);
        }

        if(StringUtils.isNotNullOrEmpty(getArguments().getString("id"))) {
            id = getArguments().getString("id");
        }

		priorityGroup = (RadioGroup) view.findViewById(R.id.priorityGroup);
        lowPriButton = (RadioButton) view.findViewById(R.id.lowPriority);
        medPriButton = (RadioButton) view.findViewById(R.id.mediumPriority);
        highPriButton = (RadioButton) view.findViewById(R.id.highPriority);

        if(StringUtils.isNotNullOrEmpty(getArguments().getString("priority"))) {
            switch (PriorityEnum.valueOf(getArguments().getString("priority"))) {
                case LOW:
                    lowPriButton.setChecked(true);
                    break;
                case MEDIUM:
                    medPriButton.setChecked(true);
                    break;
                case HIGH:
                    highPriButton.setChecked(true);
                    break;
            }
        }
		todoEditText = (EditText)view.findViewById(R.id.etTodo);
		todoEditText.setText(task);

		// Show soft keyboard automatically and request focus to field
		todoEditText.requestFocus();

		getDialog().getWindow().setSoftInputMode(
		    WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        saveButton = (Button) view.findViewById(R.id.saveEdit);

        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            //On click function
            public void onClick(View view) {

                PriorityEnum priority = null;
                switch(priorityGroup.getCheckedRadioButtonId()) {

                    case R.id.lowPriority:
                        priority = PriorityEnum.LOW;
                        break;
                    case R.id.mediumPriority:
                        priority = PriorityEnum.MEDIUM;
                        break;
                    case R.id.highPriority:
                        priority = PriorityEnum.HIGH;
                        break;

                }

                EditToDoDialogListener listener = (EditToDoDialogListener) getActivity();

                if(position==-1) {
                    ToDo data = new ToDo(todoEditText.getText().toString(),
                            StatusEnum.NOT_STARTED,
                            priority);

                    listener.onFinishEditDialog(data);
                } else {
                    listener.onFinishEditDialog(todoEditText.getText().toString(), priority, id);
                }
                // Close the dialog and return back to the parent activity
                dismiss();
                return;
            }
        });

        cancelButton = (Button) view.findViewById(R.id.cancelEdit);
        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            //On click function
            public void onClick(View view) {
                // Close the dialog and return back to the parent activity
                dismiss();
                return;
            }
        });

    }

//    @Override
//    public void onStart()
//    {
//        super.onStart();
//        Dialog dialog = getDialog();
//        if (dialog != null)
//        {
//            int width = ViewGroup.LayoutParams.MATCH_PARENT;
//            int height = ViewGroup.LayoutParams.MATCH_PARENT;
//            dialog.getWindow().setLayout(width, height);
//
//        }
//    }

    @Override
    public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        // Set the width of the dialog proportional to 75% of the screen width
        window.setLayout((int) (size.x * 1), WindowManager.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onResume();
    }
}