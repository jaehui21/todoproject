package com.shinhan.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {
	// DB 연결
	public static Connection getConnection() {
		Connection conn = null;
		String url = "jdbc:oracle:thin:@192.168.0.156:1521:xe";
		String userid = "manager";
		String userpass = "todo";

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(url, userid, userpass);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return conn;
	}

	// DB 연결 해제
	public static void dbDisconnect(Connection conn, Statement st, ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	public static String buildDebugSQL(String sqlTemplate, Object... params) {
	    for (Object param : params) {
	        String val = (param instanceof String) ? "'" + param + "'" : String.valueOf(param);
	        sqlTemplate = sqlTemplate.replaceFirst("\\?", val);
	    }
	    return sqlTemplate;
	}
	
	// 실행문 조회하는 곳에서 해당 코드 사용
//	String debugSQL = DBUtil.buildDebugSQL(sql,
//			pat.getPatient_name(), pat.getRrn(),
//			pat.getPhone(),pat.getAddr(),oldname,oldrrn);
//	System.out.println("실행 SQL: " + debugSQL);
}