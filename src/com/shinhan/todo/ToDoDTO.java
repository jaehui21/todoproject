package com.shinhan.todo;

import java.sql.Date;
import java.text.SimpleDateFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ToDoDTO {
	private int tno;
	private String content;
	private Date todo_date;
	private boolean is_check;
	private int user_id;
	private Integer category_id;
	private Integer priority_id;
	
    private String category_name;
    private String priority_level;
    
    @Override
    public String toString() {
        return (is_check ? "[O] " : "[ ] ") +
                new SimpleDateFormat("yyyy-MM-dd").format(todo_date) + " - " +
                (category_name == null ? "" : "[" + category_name + "] ") +
                (priority_level == null ? "" : "[" + priority_level + "] ")
                + content;
    }
}
