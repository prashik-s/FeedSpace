package com.feedspace.loginregister.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Toast;

import com.feedspace.loginregister.R;
import com.feedspace.loginregister.helpers.InputValidation;
import com.feedspace.loginregister.sql.DatabaseHelper;
import com.feedspace.loginregister.model.User;


public class PostFeed extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activity = PostFeed.this;

    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutContent;
    private TextInputLayout textInputLayoutTags;

    private TextInputEditText textInputEditTextContent;
    private TextInputEditText textInputEditTextTags;

    private AppCompatButton appCompatButtonLogin;

//    private AppCompatTextView textViewLinkRegister;

    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_feed);
        getSupportActionBar().hide();

        initViews();
        initListeners();
        initObjects();
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {

        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

        textInputLayoutContent = (TextInputLayout) findViewById(R.id.textInputLayoutContent);
        textInputLayoutTags = (TextInputLayout) findViewById(R.id.textInputLayoutTags);

        textInputEditTextContent = (TextInputEditText) findViewById(R.id.content);
        textInputEditTextTags = (TextInputEditText) findViewById(R.id.tags);

        appCompatButtonLogin = (AppCompatButton) findViewById(R.id.post_button);

//        textViewLinkRegister = (AppCompatTextView) findViewById(R.id.textViewLinkRegister);

    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        appCompatButtonLogin.setOnClickListener(this);
//        textViewLinkRegister.setOnClickListener(this);
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        databaseHelper = new DatabaseHelper(activity);
        inputValidation = new InputValidation(activity);

    }

    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
//        Log.d(textInputEditTextContent.toString(), textInputEditTextTags.toString());


        switch (v.getId()) {
            case R.id.post_button:
//
                User post = new User();
                post.setEmail(textInputEditTextContent.getText().toString());
                post.setPassword(textInputEditTextTags.getText().toString());
                post.setId(global_variables.get_current_user());
                databaseHelper.addPost(post);
                Context context = getApplicationContext();
                CharSequence text = "Posted";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                break;
//            case R.id.textViewLinkRegister:
//                // Navigate to RegisterActivity
//                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
//                startActivity(intentRegister);
//                break;
        }
    }

    /**
     * This method is to validate the input text fields and verify login credentials from SQLite
     */
    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        textInputEditTextContent.setText(null);
        textInputEditTextTags.setText(null);
    }
}

