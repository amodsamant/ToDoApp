package com.todoapp.models;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Date;

@Table(database = MyDatabase.class)
public class ToDo extends BaseModel {

  @PrimaryKey
  @Column(name = "Id")
  public String id;

  @Column(name = "TaskName")
  public String taskName;

  @Column(name = "Priority")
  public PriorityEnum priority;

  @Column(name = "DueDate")
  public Date dueDate;

  public static long incrementalId = 0;
  
  // Make sure to define this constructor (with no arguments)
  // If you don't querying will fail to return results!
  public ToDo() {
    super();
  }
  
  // Be sure to call super() on additional constructors as well
  public ToDo(String taskName, PriorityEnum priority){
    super();
    this.id = String.valueOf(++incrementalId);
    this.taskName = taskName;
    this.priority = priority;
  }

  public ToDo(String taskName, PriorityEnum priority, Date dueDate){
    super();
    this.id = String.valueOf(++incrementalId);
    this.taskName = taskName;
    this.priority = priority;
    this.dueDate = dueDate;
  }
}