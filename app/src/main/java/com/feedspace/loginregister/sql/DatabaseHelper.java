package com.feedspace.loginregister.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.feedspace.loginregister.activities.global_variables;

import com.feedspace.loginregister.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "UserManager.db";

    // User table name
    private static final String TABLE_USER = "user";

    // User Table Columns names
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";

    // create table sql query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD + " TEXT" + ")";

    private String CREATE_POST_TABLE = "CREATE TABLE post(id TEXT, " +
            "content TEXT, tags TEXT)";

    // drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;
    private String CREATE_FOLLOW_TABLE = "CREATE TABLE follow(user1 TEXT, user2 TEXT)";
    /**
     * Constructor
     * 
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_POST_TABLE);
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_FOLLOW_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop User Table if exist
        db.execSQL(DROP_USER_TABLE);

        // Create tables again
        onCreate(db);

    }

    /**
     * This method is to create user record
     *
     * @param
     */
    public void addPost(User post){
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("inside ", "++++++++++++++++++");
        ContentValues values = new ContentValues();
        values.put("id", post.getId());
        values.put("content", post.getEmail());
        values.put("tags", post.getPassword());

        // Inserting Row
        db.insert("post", null, values);
        db.close();
    }

    public List<User> getAllPosts() {
        // array of columns to fetch
        String[] columns = {
                "id", "content", "tags"
        };

        SQLiteDatabase db = this.getReadableDatabase();
        List<User> postList = new ArrayList<User>();
//        String[] cols = {
//                "post", "follow"
//        };
        String[] sel = {
                global_variables.get_current_user()
        };
        String[] follow_columns = {
                "user1",
                "user2"
        };

        // query the user table
        Cursor cursor = db.query("follow", //Table to query
                follow_columns,    //columns to return
                "follow.user1 = ?",        //columns for the WHERE clause
                sel,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                null);

        HashSet<String> set = new HashSet<String>();

        if (cursor.moveToFirst()) {
            do {
                String temp = cursor.getString(cursor.getColumnIndex("user2"));
                set.add(temp);
            } while (cursor.moveToNext());
        }
        cursor.close();

        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        cursor = db.query("post", //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                null); //The sort order

        Log.d("_------------",Integer.toString(cursor.getCount()));
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User post = new User();
//                post.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("CHKEfdlkjdlkj "))));
                post.setEmail(cursor.getString(cursor.getColumnIndex("content")));
                post.setPassword(cursor.getString(cursor.getColumnIndex("tags")));
                post.setName(cursor.getString(cursor.getColumnIndex("id")));
                // Adding post record to list
                Log.d("&&&&&&&&&&&&&&&",post.getPassword());
                if(set.contains(post.getName()) )
                    postList.add(post);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return postList;

    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    public int addFollowing(String user1, String user2){
        SQLiteDatabase db = this.getWritableDatabase();

        String[] sel = {
                user2
        };
        String[] follow_columns = {
                COLUMN_USER_EMAIL
        };
        Log.d(user1, user2);

        // query the user table
        Cursor cursor = db.query(TABLE_USER, //Table to query
                follow_columns,    //columns to return
                "user.user_email = ?",        //columns for the WHERE clause
                sel,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                null);


        if(cursor.getCount() > 0) {
            ContentValues values = new ContentValues();
            values.put("user1", user1);
            values.put("user2", user2);

            // Inserting Row
            db.insert("follow", null, values);
        }

        int number_of_users = cursor.getCount();
        Log.d(Integer.toString(number_of_users),"dlfsj" );
        cursor.close();
        db.close();
        return number_of_users;

    }

    /**
     * This method is to fetch all user and return the list of user records
     *
     * @return list
     */
    public List<User> getAllUser() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_EMAIL,
                COLUMN_USER_NAME,
                COLUMN_USER_PASSWORD
        };
        // sorting orders
        String sortOrder =
                COLUMN_USER_NAME + " ASC";
        List<User> userList = new ArrayList<User>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
//                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return userList;
    }

    /**
     * This method to update user record
     *
     * @param user
     */
    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        // updating row
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    /**
     * This method is to delete user record
     *
     * @param user
     */
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_USER, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    /**
     * This method to check user exist or not
     *
     * @param email
     * @return true/false
     */
    public boolean checkUser(String email) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    /**
     * This method to check user exist or not
     *
     * @param email
     * @param password
     * @return true/false
     */
    public boolean checkUser(String email, String password) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {email, password};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }
}
