package com.liugs.databasetest;

public final class Constant {

    public static final String SQL_CREATE_TABLE_V1 = "CREATE TABLE IF NOT EXISTS student(_id integer primary key,name text ); ";

    public static final String SQL_CREATE_TABLE_V2 = "create table if not exists student(_id integer primary key," +
            "name text , age int);";

    public static final String SQL_DROP_TABLE = "drop table if exists student";

    public static final String DB_NAME = "student_db";

    public static final String TABLE_NAME = "student";

    public static final String COLUMN_ID = "_id";

    public static final String COLUMN_NAME = "name";

    public static final String COLUMN_AGE = "age";

    public static final int VERSION = 1;
}
