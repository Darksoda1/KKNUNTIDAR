package com.untidar.kkntrack.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.untidar.kkntrack.model.User;
import com.untidar.kkntrack.model.Activity;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "KKNTrack.db";
    private static final int DATABASE_VERSION = 2;

    // Users table
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USER_NAME = "full_name";
    private static final String COLUMN_USER_NIM = "nim";
    private static final String COLUMN_USER_EMAIL = "email";
    private static final String COLUMN_USER_PHONE = "phone";
    private static final String COLUMN_USER_PASSWORD = "password";

    // Activities table
    private static final String TABLE_ACTIVITIES = "activities";
    private static final String COLUMN_ACTIVITY_ID = "id";
    private static final String COLUMN_ACTIVITY_TITLE = "title";
    private static final String COLUMN_ACTIVITY_DESCRIPTION = "description";
    private static final String COLUMN_ACTIVITY_DATE = "date";
    private static final String COLUMN_ACTIVITY_TIME = "time";
    private static final String COLUMN_ACTIVITY_LOCATION = "location";
    private static final String COLUMN_ACTIVITY_USER_EMAIL = "user_email";
    private static final String COLUMN_ACTIVITY_PHOTO_PATH = "photo_path";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_NAME + " TEXT,"
                + COLUMN_USER_NIM + " TEXT,"
                + COLUMN_USER_EMAIL + " TEXT UNIQUE,"
                + COLUMN_USER_PHONE + " TEXT,"
                + COLUMN_USER_PASSWORD + " TEXT" + ")";

        String CREATE_ACTIVITIES_TABLE = "CREATE TABLE " + TABLE_ACTIVITIES + "("
                + COLUMN_ACTIVITY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_ACTIVITY_TITLE + " TEXT,"
                + COLUMN_ACTIVITY_DESCRIPTION + " TEXT,"
                + COLUMN_ACTIVITY_DATE + " TEXT,"
                + COLUMN_ACTIVITY_TIME + " TEXT,"
                + COLUMN_ACTIVITY_LOCATION + " TEXT,"
                + COLUMN_ACTIVITY_USER_EMAIL + " TEXT,"
                + COLUMN_ACTIVITY_PHOTO_PATH + " TEXT" + ")";

        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_ACTIVITIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_ACTIVITIES + " ADD COLUMN " + COLUMN_ACTIVITY_PHOTO_PATH + " TEXT");
        }
    }

    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getFullName());
        values.put(COLUMN_USER_NIM, user.getNim());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PHONE, user.getPhone());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result;
    }

    public boolean updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getFullName());
        values.put(COLUMN_USER_NIM, user.getNim());
        values.put(COLUMN_USER_PHONE, user.getPhone());

        int result = db.update(TABLE_USERS, values, COLUMN_USER_EMAIL + " = ?",
                new String[]{user.getEmail()});
        db.close();
        return result > 0;
    }

    public boolean updateUserPassword(String email, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_PASSWORD, newPassword);

        int result = db.update(TABLE_USERS, values, COLUMN_USER_EMAIL + " = ?",
                new String[]{email});
        db.close();
        return result > 0;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USER_ID};
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        return cursorCount > 0;
    }

    public boolean checkUserExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USER_ID};
        String selection = COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = {email};

        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        return cursorCount > 0;
    }

    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USER_ID, COLUMN_USER_NAME, COLUMN_USER_NIM,
                COLUMN_USER_EMAIL, COLUMN_USER_PHONE};
        String selection = COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = {email};

        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        User user = null;

        if (cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID)));
            user.setFullName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
            user.setNim(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NIM)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
            user.setPhone(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PHONE)));
        }

        cursor.close();
        db.close();
        return user;
    }

    public long addActivity(Activity activity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ACTIVITY_TITLE, activity.getTitle());
        values.put(COLUMN_ACTIVITY_DESCRIPTION, activity.getDescription());
        values.put(COLUMN_ACTIVITY_DATE, activity.getDate());
        values.put(COLUMN_ACTIVITY_TIME, activity.getTime());
        values.put(COLUMN_ACTIVITY_LOCATION, activity.getLocation());
        values.put(COLUMN_ACTIVITY_USER_EMAIL, activity.getUserEmail());
        values.put(COLUMN_ACTIVITY_PHOTO_PATH, activity.getPhotoPath());

        long result = db.insert(TABLE_ACTIVITIES, null, values);
        db.close();
        return result;
    }

    public boolean updateActivity(Activity activity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ACTIVITY_TITLE, activity.getTitle());
        values.put(COLUMN_ACTIVITY_DESCRIPTION, activity.getDescription());
        values.put(COLUMN_ACTIVITY_DATE, activity.getDate());
        values.put(COLUMN_ACTIVITY_TIME, activity.getTime());
        values.put(COLUMN_ACTIVITY_LOCATION, activity.getLocation());
        values.put(COLUMN_ACTIVITY_PHOTO_PATH, activity.getPhotoPath());

        int result = db.update(TABLE_ACTIVITIES, values, COLUMN_ACTIVITY_ID + " = ?",
                new String[]{String.valueOf(activity.getId())});
        db.close();
        return result > 0;
    }

    public boolean deleteActivity(int activityId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_ACTIVITIES, COLUMN_ACTIVITY_ID + " = ?",
                new String[]{String.valueOf(activityId)});
        db.close();
        return result > 0;
    }

    public List<Activity> getActivitiesByUser(String userEmail) {
        List<Activity> activities = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_ACTIVITY_USER_EMAIL + " = ?";
        String[] selectionArgs = {userEmail};

        Cursor cursor = db.query(TABLE_ACTIVITIES, null, selection, selectionArgs,
                null, null, COLUMN_ACTIVITY_DATE + " DESC, " + COLUMN_ACTIVITY_TIME + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Activity activity = new Activity();
                activity.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ACTIVITY_ID)));
                activity.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_ACTIVITY_TITLE)));
                activity.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_ACTIVITY_DESCRIPTION)));
                activity.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_ACTIVITY_DATE)));
                activity.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_ACTIVITY_TIME)));
                activity.setLocation(cursor.getString(cursor.getColumnIndex(COLUMN_ACTIVITY_LOCATION)));
                activity.setUserEmail(cursor.getString(cursor.getColumnIndex(COLUMN_ACTIVITY_USER_EMAIL)));
                activity.setPhotoPath(cursor.getString(cursor.getColumnIndex(COLUMN_ACTIVITY_PHOTO_PATH)));
                activities.add(activity);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return activities;
    }

    public Activity getActivityById(int activityId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_ACTIVITY_ID + " = ?";
        String[] selectionArgs = {String.valueOf(activityId)};

        Cursor cursor = db.query(TABLE_ACTIVITIES, null, selection, selectionArgs, null, null, null);
        Activity activity = null;

        if (cursor.moveToFirst()) {
            activity = new Activity();
            activity.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ACTIVITY_ID)));
            activity.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_ACTIVITY_TITLE)));
            activity.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_ACTIVITY_DESCRIPTION)));
            activity.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_ACTIVITY_DATE)));
            activity.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_ACTIVITY_TIME)));
            activity.setLocation(cursor.getString(cursor.getColumnIndex(COLUMN_ACTIVITY_LOCATION)));
            activity.setUserEmail(cursor.getString(cursor.getColumnIndex(COLUMN_ACTIVITY_USER_EMAIL)));
            activity.setPhotoPath(cursor.getString(cursor.getColumnIndex(COLUMN_ACTIVITY_PHOTO_PATH)));
        }

        cursor.close();
        db.close();
        return activity;
    }

    public int getActivitiesCountByUser(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_ACTIVITY_USER_EMAIL + " = ?";
        String[] selectionArgs = {userEmail};

        Cursor cursor = db.query(TABLE_ACTIVITIES, new String[]{"COUNT(*)"},
                selection, selectionArgs, null, null, null);

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        db.close();
        return count;
    }

    public String getLastActivityByUser(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_ACTIVITY_USER_EMAIL + " = ?";
        String[] selectionArgs = {userEmail};

        Cursor cursor = db.query(TABLE_ACTIVITIES, new String[]{COLUMN_ACTIVITY_TITLE},
                selection, selectionArgs, null, null,
                COLUMN_ACTIVITY_DATE + " DESC, " + COLUMN_ACTIVITY_TIME + " DESC", "1");

        String lastActivity = null;
        if (cursor.moveToFirst()) {
            lastActivity = cursor.getString(0);
        }

        cursor.close();
        db.close();
        return lastActivity;
    }
}
