package com.onlineinkpot.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.widget.Toast;

/**
 * Created by USER on 9/14/2017.
 */

public class AndroidOpenDbHelper extends SQLiteOpenHelper
{
    public static final String DB_NAME = "undergraduate_gpa_db_news_eight_eleven";
    public static final int DB_VERSION = 4;
    public static final String TABLE_NAME_BASIC = "basic_details_table_two";
    public static final String TABLE_NAME_BASIC_FIVE = "basic_details_table_five";

    public static final String TABLE_NAME_QUESTIONNEW = "question_tablenew";

    public static final String TABLE_NAME_NOTIFICATION = "notificationtable";
    public static final String STUDENT_ID = "studentid";
    public static final String SEMESTER_ID = "semesterid";
    public static final String SUBJECT_ID = "subjectid";
    public static final String SUBJECT_NAME = "subjectname";
    public static final String COURSE_ID = "courseid";
    public static final String TOTAL_PRICE = "totalprice";
    public static final String DURATION = "duration";
    public static final String HEADER = "header";
    public static final String TOPIC = "topic";
    public static final String CHAPTER = "chapter";
    public static final String UNIT = "unit";
    public static final String SUBJECT = "subject";
    public static final String SEMESTER = "semester";
    public static final String COURSE = "course";
    public static final String COLLEGE = "college";
    public static final String UNIVERSITY = "university";
    public static final String DATE = "date";
    public static final String FLAG = "flag";
    public static final String ID = "id";
    public static final String OPTION1 = "option1";
    public static final String OPTION2 = "option2";
    public static final String OPTION3 = "option3";
    public static final String OPTION4 = "option4";
    public static final String QUESTION = "question";
    public static final String CORRECT_ANSWER = "correctanswer";
    public static final String CORRECT_ANSWERVALUE = "correctanswervalue";


    public static final String STUDENT_ANSWER = "studentanswer";


    SQLiteDatabase sqlitedatabase;
    Context contextnew;

    public AndroidOpenDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQueryToCreateUndergraduateDetailsTable = "create table if not exists " + TABLE_NAME_BASIC_FIVE + " ( " + BaseColumns._ID + " integer primary key autoincrement, "
                + STUDENT_ID + " text not null, "
                + SEMESTER_ID + " text not null, "
                + SUBJECT_ID + " text  not null,"
                + SUBJECT_NAME + " text not null unique, "
                + COURSE_ID + " text not null, "
                + TOTAL_PRICE + " text not null,"
                + DURATION + " text not null" +
                ");";

        String questiontable = "create table if not exists " + TABLE_NAME_QUESTIONNEW + " ( " + BaseColumns._ID + " integer primary key autoincrement, "
                + ID + " text not null, "
                + STUDENT_ID + " text not null, "
                + QUESTION + " text not null, "
                + CORRECT_ANSWER + " text  not null,"
                + STUDENT_ANSWER + " text  not null,"
                + CORRECT_ANSWERVALUE + " text  not null,"
                + OPTION1 + " text  not null,"
                + OPTION2 + " text  not null,"
                + OPTION3 + " text  not null,"
                + OPTION4 + " text  not null" +
                ");";


        String notificationtable = "create table if not exists " + TABLE_NAME_NOTIFICATION + " ( " + BaseColumns._ID + " integer primary key autoincrement, "
                + HEADER + " text not null unique, "
                + FLAG + " text  not null" +
                ");";

        db.execSQL(sqlQueryToCreateUndergraduateDetailsTable);
        db.execSQL(questiontable);
        db.execSQL(notificationtable);
    }

    public void fetchTotalData() {
        String pricenew = "";
        sqlitedatabase = this.getReadableDatabase();
        String query = "SELECT totalprice FROM basic_details_table_three";
        Cursor cursor = sqlitedatabase.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() != true) {
                String price = cursor.getString(cursor.getColumnIndex("totalprice"));
                pricenew = price + pricenew;
            }

            Toast.makeText(contextnew, "values are" + pricenew, Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1 && newVersion == 2) {
        }
    }
}