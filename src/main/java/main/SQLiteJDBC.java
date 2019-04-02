package main;

import java.sql.*;

public class SQLiteJDBC {
    private Connection c = null;
    private Statement stmt = null;

    public Connection getC() {
        return c;
    }

    public SQLiteJDBC() {

        try {

            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:YuBanServer.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");
            stmt = c.createStatement();
            String sql = "CREATE TABLE USER_BASE_INFO" +
                    "(NAME      TEXT NOT NULL," +
                    "PASSWORD   TEXT NOT NULL," +
                    "EMAIL      TEXT NOT NULL," +
                    "NLANGUAGE  TEXT NOT NULL," +
                    "SLANGUAGE  TEXT NOT NULL," +
                    "LEVEL      INT  NOT NULL," +
                    "PICURL     TEXT NOT NULL," +
                    "USERID     TEXT PRIMARY KEY NOT NULL," +
                    "INTEREST   TEXT NOT NULL," +
                    "TOPERSON   TEXT NOT NULL," +
                    "TOGOAL     TEXT NOT NULL);";
            stmt.executeUpdate(sql);
            c.commit();
            String sql_creat_problem = "CREATE TABLE USER_QUESTION" +
                    "(USERID    TEXT NOT NULL," +
                    "NAME       TEXT NOT NULL," +
                    "QUESTIONTITLE TEXT NOT NULL," +
                    "QUESTIONNUMBER INT PRIMARY KEY NOT NULL," +
                    "QUESTIONDETAIL TEXT NOT NULL," +
                    "QUESTIONTIME   TEXT NOT NULL," +
                    "PICTURE    TEXT NOT NULL," +
                    "COUNTRY    TEXT NOT NULL);";
            stmt.executeUpdate(sql_creat_problem);
            c.commit();
            String sql_comment = "CREATE TABLE USER_COMMENT" +
                    "(QUESTIONNUMBER  INT NOT NULL," +
                    "USERID          TEXT NOT NULL," +
                    "COMMENTDETAIL   TEXT NOT NULL," +
                    "COMMENTTIME     TEXT NOT NULL);";
            stmt.executeUpdate(sql_comment);
            c.commit();

            String sql_chat = "CREATE TABLE CHAT_LIST" +
                    "(TEACHERID     TEXT PRIMARY KEY NOT NULL," +
                    "NAME           TEXT NOT NULL," +
                    "PICURL         TEXT NOT NULL," +
                    "SCORE          TEXT NOT NULL," +
                    "LANGUAGE       TEXT NOT NULL," +
                    "INTRODUCELINK  TEXT NOT NULL," +
                    "EXPERIENCE     TEXT NOT NULL," +
                    "TYPE           TEXT NOT NULL," +
                    "PLAIN          TEXT NOT NULL);";
            stmt.executeUpdate(sql_chat);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

    }

    public Statement getStmt() {
        return stmt;
    }

    public void stop() {
        try {
            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}