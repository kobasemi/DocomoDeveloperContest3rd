package com.koba.myshelf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemClear extends Thread {

	
	Pass passes = new Pass();
	String DBPASS = Pass.DBPASS;
	String PATH = Pass.PATH;
	String USER = Pass.USER;
	
	
	String table =  null;
	int item  = 0;
	
	ItemClear(String t,int i){
		
		table = t;
		item = i;
		
	}
	

		
 public void run() {
		Connection con = null;
	    PreparedStatement ps = null;
	    Long uid = 15L;
	    String commenttable = null;
	    

	    try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

         // データベースへ接続
	    try {
             con = DriverManager.getConnection(PATH,USER,DBPASS);
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

        java.sql.Statement stmt = null;
		try {
			stmt = con.createStatement();
		} catch (SQLException e2) {
			// TODO 自動生成された catch ブロック
			e2.printStackTrace();
		}

		

		
		String userprof = "select * from "+table +" where id = " + item;
		
		try {
			ps = con.prepareStatement(userprof);
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
			ResultSet res = null;
		try {
			res = ps.executeQuery();
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	
		
		try {
			while(res.next()){
		
				uid = res.getLong("uid");
		
			}
		} catch (SQLException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}
		
		
		//自分のテーブルからクリア
		
		commenttable = "uid " + uid + " id " + item;
		
		
         String ex1 = "delete from "+table +" where id = " + item;
         String dp1 = "Drop from "+ commenttable;
         String ex2 = "delete from all_item where iid = " + item;

       
         try {
			stmt.executeUpdate(ex1);
			stmt.executeUpdate(dp1);
			stmt.executeUpdate(ex2);
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		

		   }
	
	//commenttableをDROP
 
 	//all_itemをDrop
	
	
	
	
}
