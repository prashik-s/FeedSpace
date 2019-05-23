package com.feedspace.loginregister.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.feedspace.loginregister.R;
import com.feedspace.loginregister.adapters.UsersRecyclerAdapter;
import com.feedspace.loginregister.model.User;
import com.feedspace.loginregister.sql.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;


public class UsersListActivity extends AppCompatActivity implements View.OnClickListener{

    private AppCompatActivity activity = UsersListActivity.this;
    private AppCompatButton post_feed;
    private AppCompatButton search_button;
    private TextInputEditText search_user;
    private AppCompatTextView textViewName;
    private RecyclerView recyclerViewUsers;
    private List<User> listUsers;
    private UsersRecyclerAdapter usersRecyclerAdapter;
    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);
        getSupportActionBar().setTitle("");
        initViews();
        initObjects();
        initListeners();

    }

    private void initListeners() {
        post_feed.setOnClickListener(this);
        search_button.setOnClickListener(this);

    }

    /**
     * This method is to initialize views
     */
    private void initViews() {
        textViewName = (AppCompatTextView) findViewById(R.id.textViewName);
        recyclerViewUsers = (RecyclerView) findViewById(R.id.recyclerViewUsers);
        post_feed = (AppCompatButton) findViewById(R.id.post_feed);
        search_button = (AppCompatButton) findViewById(R.id.search_button);
        search_user = (TextInputEditText) findViewById(R.id.search_user);
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        listUsers = new ArrayList<>();
        usersRecyclerAdapter = new UsersRecyclerAdapter(listUsers);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewUsers.setLayoutManager(mLayoutManager);
        recyclerViewUsers.setItemAnimator(new DefaultItemAnimator());
        recyclerViewUsers.setHasFixedSize(true);
        recyclerViewUsers.setAdapter(usersRecyclerAdapter);
        databaseHelper = new DatabaseHelper(activity);

        String emailFromIntent = getIntent().getStringExtra("EMAIL");
        textViewName.setText(emailFromIntent);

        getDataFromSQLite();
    }

    @Override
    public void onClick(View v){
        Log.d("#############","##############");
        switch (v.getId()){
            case R.id.post_feed:
                Intent login_page = new Intent(getApplicationContext(), PostFeed.class);
                startActivity(login_page);
                break;
            case R.id.search_button:
//                todo Add_search user as friend if exists
                int post_count = databaseHelper.addFollowing(global_variables.get_current_user(),search_user.getText().toString() );
                Log.d(search_user.getText().toString(),global_variables.get_current_user() );
                if(post_count>0) {
                    Intent intentRegister = new Intent(getApplicationContext(), UsersListActivity.class);
                    startActivity(intentRegister);
                    Context context = getApplicationContext();
                    CharSequence text = "You are now following this user.";

                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else
                {
                    Context context = getApplicationContext();
                    CharSequence text = "User doesn't exist.";

                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                break;
        }
    }


    /**
     * This method is to fetch all user records from SQLite
     */
    private void getDataFromSQLite() {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                listUsers.clear();

                listUsers.addAll(databaseHelper.getAllPosts());

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                usersRecyclerAdapter.notifyDataSetChanged();
            }
        }.execute();
    }
}
