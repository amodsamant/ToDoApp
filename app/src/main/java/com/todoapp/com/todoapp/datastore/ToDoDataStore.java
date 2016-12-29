package com.todoapp.com.todoapp.datastore;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.sql.Date;

@Table(database = MyDatabase.class)
public class ToDoDataStore extends BaseModel {

  @PrimaryKey
  @Column(name = "Id")
  public String id;

  @Column(name = "TaskName")
  public String taskName;

  @Column(name = "Status")
  public StatusEnum status;

  @Column(name = "Priority")
  public PriorityEnum priority;

  @Column(name = "DueDate")
  public Date dueDate;
  
  // Make sure to define this constructor (with no arguments)
  // If you don't querying will fail to return results!
  public ToDoDataStore() {
    super();
  }
  
  // Be sure to call super() on additional constructors as well
  public ToDoDataStore(String taskName, StatusEnum status, PriorityEnum priority){
    super();
    this.taskName = taskName;
    this.status = status;
    this.priority = priority;
  }

  public ToDoDataStore(String taskName, StatusEnum status, PriorityEnum priority, Date dueDate){
    super();
    this.taskName = taskName;
    this.status = status;
    this.priority = priority;
    this.dueDate = dueDate;
  }
}