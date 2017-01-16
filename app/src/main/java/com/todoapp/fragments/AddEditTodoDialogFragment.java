package com.todoapp.fragments;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.raizlabs.android.dbflow.StringUtils;
import com.todoapp.R;
import com.todoapp.utils.ToDoConstants;
import com.todoapp.models.PriorityEnum;
import com.todoapp.models.ToDo;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class AddEditTodoDialogFragment extends DialogFragment {

    public interface AddEditToDoDialogListener {
        void onFinishAddDialog(ToDo data);
        void onFinishEditDialog(String taskName, PriorityEnum priority, String id,
                                Date dueDate);
    }

    private EditText todoEditText;
    private String task;
	private String id;
    private String dueDateString;
    private int position = -1;

    private RadioGroup priorityGroup;
	private RadioButton lowPriButton;
	private RadioButton medPriButton;
	private RadioButton highPriButton;

    private Button saveButton;
    private Button cancelButton;
    private Button dueDateButton;

    private TextView dueDateTV;

	public AddEditTodoDialogFragment() {
		// Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
	}

    /**
     * newInstance method for item to be edited
     * @param todoText
     * @param pos
     * @param id
     * @param priority
     * @param dueDate
     * @return
     */
	public static AddEditTodoDialogFragment newInstance(String todoText, int pos, String id,
                                                     PriorityEnum priority, Date dueDate) {
        AddEditTodoDialogFragment frag = new AddEditTodoDialogFragment();
		Bundle args = new Bundle();
		args.putString("todotext", todoText);
		args.putInt("position", pos);
		args.putString("id",id);
        args.putString("priority",priority.name());
        args.putString("dueDateString", ToDoConstants.DATE_FORMAT.format(dueDate));

        frag.setArguments(args);
		return frag;
	}

    /**
     * newInstance method for a new item
     * @param pos
     * @return
     */
    public static AddEditTodoDialogFragment newInstance(int pos) {
        AddEditTodoDialogFragment frag = new AddEditTodoDialogFragment();
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

        if(StringUtils.isNotNullOrEmpty(getArguments().getString("dueDateString"))) {
            dueDateString = getArguments().getString("dueDateString");
        } else {
            Date today = Calendar.getInstance().getTime();
            dueDateString = ToDoConstants.DATE_FORMAT.format(today);
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

        dueDateTV = (TextView) view.findViewById(R.id.dueDate);
        dueDateTV.setText(dueDateString);

        saveButton = (Button) view.findViewById(R.id.saveEdit);
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            //On click function
            public void onClick(View view) {

                if(!StringUtils.isNotNullOrEmpty(todoEditText.getText().toString().trim())) {
                    Toast.makeText(getContext(),"Task cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

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

                Date dueDate = null;
                try {
                   dueDate = ToDoConstants.DATE_FORMAT.parse(dueDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                AddEditToDoDialogListener listener = (AddEditToDoDialogListener) getActivity();

                if(position==-1) {
                    ToDo data = new ToDo(todoEditText.getText().toString(),
                            priority, dueDate);

                    listener.onFinishAddDialog(data);
                } else {
                    listener.onFinishEditDialog(todoEditText.getText().toString(), priority, id,
                            dueDate);
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

        dueDateButton = (Button) view.findViewById(R.id.dueDateButton);
        dueDateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Calendar cal = Calendar.getInstance(TimeZone.getDefault()); // Get current date

                // Create the DatePickerDialog instance
                DatePickerDialog datePicker = new DatePickerDialog(getContext(),
                        R.style.AppTheme, datePickerListener,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH));
                datePicker.setCancelable(false);
                datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePicker.setTitle("Select the date");
                datePicker.show();
            }
         });

    }

    // Date picker listener for setting due date string
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            dueDateString = createDueDateString(selectedYear, selectedDay, selectedMonth);
            dueDateTV.setText(dueDateString);

        }
    };

    /**
     * Function generates the required string the the format
     * @param selectedYear
     * @param selectedDay
     * @param selectedMonth
     * @return
     */
    private String createDueDateString(int selectedYear, int selectedDay, int selectedMonth) {

        String year = String.valueOf(selectedYear);
        String month = String.valueOf(selectedMonth + 1);
        String day = String.valueOf(selectedDay);

        StringBuilder builder = new StringBuilder();
        builder.append(month).append("-")
                .append(day).append("-")
                .append(year);

        return builder.toString();
    }


    @Override
    public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x * 1), WindowManager.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onResume();
    }


}