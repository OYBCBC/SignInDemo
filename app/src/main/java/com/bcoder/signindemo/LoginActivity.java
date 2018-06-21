package com.bcoder.signindemo;

import android.animation.Animator;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bcoder.signindemo.view.button.CircleButton;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private CircleButton loginButton;
    private RelativeLayout rlContent;
    private Handler handler;
    private Animator animator;

    private TextInputEditText login_user_id_et;
    private TextInputEditText login_pass_once_et;
    private AppCompatCheckBox reme_pass_ckb;
    private TextView noAccount;

    private void bindViews() {

        login_user_id_et = findViewById(R.id.login_user_id_et);
        login_user_id_et.setLines(1);

        login_pass_once_et = findViewById(R.id.login_pass_once_et);
        login_pass_once_et.setLines(1);

        reme_pass_ckb = findViewById(R.id.reme_pass_ckb);
        noAccount = findViewById(R.id.no_account);

        noAccount.setOnClickListener(this);
    }


    private void bindLoginButtonViews() {
        loginButton = findViewById(R.id.button_login);
        rlContent = findViewById(R.id.rl_content);
        rlContent.getBackground().setAlpha(0);
        handler = new Handler();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.startAnim();

                handler.postDelayed(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void run() {

                        //跳转
                        gotoNew();
                    }
                }, 3000);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bindLoginButtonViews();
        bindViews();

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void gotoNew() {
        loginButton.gotoNew();

        final Intent intent = new Intent(this, MainAcitivity.class);

        int xc = (loginButton.getLeft() + loginButton.getRight()) / 2;
        int yc = (loginButton.getTop() + loginButton.getBottom()) / 2;

        animator = ViewAnimationUtils.createCircularReveal(rlContent, xc, yc, 0, 1111);

        animator.setDuration(300);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);

                    }
                }, 200);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
        rlContent.getBackground().setAlpha(255);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        animator.cancel();

        rlContent.getBackground().setAlpha(0);
        loginButton.regainBackground();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.no_account:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;

        }
    }
}