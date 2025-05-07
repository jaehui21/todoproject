package com.shinhan.todo;

import java.sql.Date;
import java.util.List;

public class ToDoService {
	
	ToDoDAO todoDAO = new ToDoDAO();
	
	public List<ToDoDTO> selectAllToday(int userId) {
		return todoDAO.selectAllToday(userId);
	}
	
	public List<ToDoDTO> selectAll(int userId) {
	    return todoDAO.selectAll(userId);
	}
	
    public List<ToDoDTO> selectByDate(Date date, int userId) {
    	return todoDAO.selectByDate(date, userId);
    }
    
    public ToDoDTO selectByTno(int userId, int tno) {
        return todoDAO.selectByTno(userId, tno);
    }
    
    public int insertToDo(ToDoDTO todo) {
    	return todoDAO.insertToDo(todo);
    }
    
    public int updateToDo(ToDoDTO todo) {
    	return todoDAO.updateToDo(todo);
    }
    
    public int deleteToDo(int tno) {
    	return todoDAO.deleteToDo(tno);
    }
    
    public int updateCheck(int tno, boolean isCheck) {
    	return todoDAO.updateCheck(tno, isCheck);
    }
}
