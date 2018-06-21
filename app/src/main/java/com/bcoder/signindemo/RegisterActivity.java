package com.bcoder.signindemo;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bcoder.signindemo.util.UserStore;

import java.io.IOException;

/**
 * Created by Administrator on 2018/6/19.
 */

public class RegisterActivity extends AppCompatActivity {

    private UserStore db;


    //UI
    private TextInputLayout register_user_id_til;
    private TextInputEditText register_user_id_et;
    private TextInputLayout register_pass_once_til;
    private TextInputEditText register_pass_once_et;
    private TextInputLayout register_pass_confirm_til;
    private TextInputEditText register_pass_confirm_et;
    private Button register_button;
    private Context context = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initDB();
        bindViews();

    }

    private void initDB() {
        db = new UserStore(this);
    }

    private void bindViews() {
        register_user_id_til = findViewById(R.id.register_user_id_til);
        register_user_id_et = findViewById(R.id.register_user_id_et);
        register_user_id_et.setLines(1);
        register_pass_once_til = findViewById(R.id.register_pass_once_til);
        register_pass_once_et = findViewById(R.id.register_pass_once_et);
        register_pass_once_et.setLines(1);
        register_pass_confirm_til = findViewById(R.id.register_pass_confirm_til);
        register_pass_confirm_et = findViewById(R.id.register_pass_confirm_et);
        register_pass_confirm_et.setLines(1);
        register_button = findViewById(R.id.button_register);

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (register_user_id_et.getText().toString().length() <= 6)
                    register_user_id_til.setError("用户ID 长度需要大于6位");
                if (register_pass_once_et.getText().toString().length() <= 7) {
                    register_pass_once_til.setError("密码长度应当大于7位");
                } else if (register_pass_confirm_et.getText().toString().equals(register_pass_once_et.getText().toString())) {

                    if (sqlitequery(register_user_id_et.getText().toString()))
                        register_user_id_til.setError("用户名已存在");
                    else {
                        //存储 账户密码
                        String userid = register_user_id_et.getText().toString();
                        String psw = register_pass_once_et.getText().toString();
                        sqliteinsert(userid, psw);


                        Toast.makeText(context, "注册成功", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        intent.putExtra("userId", userid);
                        intent.putExtra("newId", true);
                        startActivity(intent);
                    }
                } else {
                    register_pass_confirm_til.setError("两次密码输入不一致");
                }


            }
        });

    }

    public void sqliteinsert(String userID, String psw) {
        final SQLiteDatabase dbWrite = db.getReadableDatabase();
        //以键值对的形式存入数据
        ContentValues cv = new ContentValues();
        cv.put("userid", userID);
        cv.put("psw", psw);
        dbWrite.insert("user", null, cv);
    }

    /**
     * @param theUserID
     * @return 找到相同id返回true， 相反false
     */
    public boolean sqlitequery(String theUserID) {
        SQLiteDatabase dbRead = db.getReadableDatabase();
        Cursor cursor = dbRead.query("user", null, null, null, null, null, null);
        while (cursor.moveToLast()) {
            String userid = cursor.getString(cursor.getColumnIndex("userid"));
            if (userid == theUserID) {
                return true;
            }
            break;
        }
        cursor.close();
        return false;
    }
}
