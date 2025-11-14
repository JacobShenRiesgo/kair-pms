package com.j4va.kair;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestMySQL {
    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/kairdb",
                    "root",
                    ""   // <-- CHANGE THIS
            );
            System.out.println("Connected to MySQL successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
