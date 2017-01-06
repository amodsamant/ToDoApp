package com.todoapp;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.todoapp.com.todoapp.datastore.PriorityEnum;

public class EditTodoDialogFragment extends DialogFragment {

    public interface EditToDoDialogListener {
        void onFinishEditDialog(String taskName, PriorityEnum priority, String id);
    }

    private EditText todoEditText;
	private int position;
	private String id;

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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_edit_todo, container);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		String todoText = getArguments().getString("todotext");

		position = getArguments().getInt("position", 0);
		id = getArguments().getString("id");

		priorityGroup = (RadioGroup) view.findViewById(R.id.priorityGroup);
        lowPriButton = (RadioButton) view.findViewById(R.id.lowPriority);
        medPriButton = (RadioButton) view.findViewById(R.id.mediumPriority);
        highPriButton = (RadioButton) view.findViewById(R.id.highPriority);

        switch(PriorityEnum.valueOf(getArguments().getString("priority"))) {
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
		todoEditText = (EditText)view.findViewById(R.id.etTodo);
		todoEditText.setText(todoText);

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
                listener.onFinishEditDialog(todoEditText.getText().toString(), priority, id);
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

    @Override
    public void onStart()
    {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);

        }
    }
}