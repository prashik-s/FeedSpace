package com.feedspace.loginregister.activities;

public class global_variables {
    private static String current_user;
    public static String get_current_user(){
        return current_user;
    }
    public static void set_current_user(String cur_user){
        global_variables.current_user = cur_user;
    }
}
