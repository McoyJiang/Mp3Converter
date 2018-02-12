package com.example.danny_jiang.sample.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.danny_jiang.sample.MainActivity;
import com.example.danny_jiang.sample.R;
import com.example.danny_jiang.sample.controller.LoginController;
import com.example.danny_jiang.sample.utils.ClearWriteEditText;
import com.example.danny_jiang.sample.utils.SoftKeyBoardStateHelper;

import java.lang.reflect.Method;

import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by Danny on 18/2/6.
 */

public class LoginActivity extends AppCompatActivity implements
        View.OnClickListener, View.OnFocusChangeListener {

    public ClearWriteEditText mLogin_userName;
    public ClearWriteEditText mLogin_passWord;
    public Button mBtn_login;
    public TextView mLogin_register;
    private LoginController mLoginController;
    private ImageView mDe_login_logo;
    private RelativeLayout mTitleBar;
    private RelativeLayout mBackground;
    private LinearLayout mLl_name_psw;
    private boolean mLogoShow = true;
    public TextView mNewUser;
    public TextView mLogin_desc;
    private ImageView mLogin_userLogo;
    private ImageView mLogin_pswLogo;
    private View mView;
    private View mUserLine;
    private View mPswLine;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        initView();
        initData();

        mLoginController = new LoginController(this);

        mBtn_login.setOnClickListener(mLoginController);
        mLogin_register.setOnClickListener(mLoginController);
        mNewUser.setOnClickListener(mLoginController);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.background:
                if (!getLogoShow()) {
                    InputMethodManager imm = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    setLogoShow(false);
                }
                break;
            case R.id.login_userName:
            case R.id.login_passWord:
                if (getLogoShow()) {
                    mTitleBar.setVisibility(View.VISIBLE);
                    mTitleBar.startAnimation(moveToView(0.0f, 0.0f, -1.0f, 0.0f));
                    mDe_login_logo.setVisibility(View.GONE);
                    mLl_name_psw.startAnimation(moveToView(0.0f, 0.0f, 0.32f, 0.0f));

                    mView.setVisibility(View.VISIBLE);
                    setLogoShow(false);
                }
                break;
            default:
                break;
        }

    }

    public void setLogoShow(boolean isLogoShow) {
        mLogoShow = isLogoShow;
    }

    public boolean getLogoShow() {
        return mLogoShow;
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.login_userName:
                if (hasFocus) {
                    mLogin_userLogo.setImageResource(R.drawable.login_user_press);
                    mUserLine.setBackgroundColor(getResources().getColor(R.color.line_press));
                } else {
                    mLogin_userLogo.setImageResource(R.drawable.login_user_normal);
                    mUserLine.setBackgroundColor(getResources().getColor(R.color.line_normal));
                }
                if (hasFocus && getLogoShow()) {
                    mTitleBar.setVisibility(View.VISIBLE);
                    mTitleBar.startAnimation(moveToView(0.0f, 0.0f, -1.0f, 0.0f));
                    mDe_login_logo.setVisibility(View.GONE);
                    mLl_name_psw.startAnimation(moveToView(0.0f, 0.0f, 0.32f, 0.0f));
                    mView.setVisibility(View.VISIBLE);
                    setLogoShow(false);
                }
                break;
            case R.id.login_passWord:
                if (hasFocus) {
                    mLogin_pswLogo.setImageResource(R.drawable.login_psw_press);
                    mPswLine.setBackgroundColor(getResources().getColor(R.color.line_press));
                } else {
                    mLogin_pswLogo.setImageResource(R.drawable.login_psw_normal);
                    mPswLine.setBackgroundColor(getResources().getColor(R.color.line_normal));
                }
                if (hasFocus && getLogoShow()) {
                    mTitleBar.setVisibility(View.VISIBLE);
                    mTitleBar.startAnimation(moveToView(0.0f, 0.0f, -1.0f, 0.0f));
                    mDe_login_logo.setVisibility(View.GONE);
                    mLl_name_psw.startAnimation(moveToView(0.0f, 0.0f, 0.32f, 0.0f));
                    setLogoShow(false);
                }
                break;
        }
    }

    private void initData() {
        mLogin_userName.setOnFocusChangeListener(this);
        mLogin_passWord.setOnFocusChangeListener(this);
        mLogin_userName.setOnClickListener(this);
        mLogin_passWord.setOnClickListener(this);
        mBackground.setOnClickListener(this);
        SoftKeyBoardStateHelper helper = new SoftKeyBoardStateHelper(findViewById(R.id.background));
        helper.addSoftKeyboardStateListener(new SoftKeyBoardStateHelper.SoftKeyboardStateListener() {
            @Override
            public void onSoftKeyboardOpened(int keyboardHeightInPx) {
                //软键盘弹起
            }

            @Override
            public void onSoftKeyboardClosed() {
                //软键盘关闭
                if (!getLogoShow()) {
                    mTitleBar.setVisibility(View.GONE);
                    mTitleBar.startAnimation(moveToView(0.0f, 0.0f, 0.0f, -1.0f));
                    mDe_login_logo.setVisibility(View.VISIBLE);
                    AlphaAnimation animation = new AlphaAnimation(0, 1);
                    animation.setDuration(250);
                    mDe_login_logo.startAnimation(animation);
                    mLl_name_psw.startAnimation(moveToView(0.0f, 0.0f, -0.09f, 0.003f));
                    setLogoShow(true);
                }
            }
        });
    }


    public TranslateAnimation moveToView(float a, float b, float c, float d) {
        TranslateAnimation mHiddenAction = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, a,
                Animation.RELATIVE_TO_SELF, b,
                Animation.RELATIVE_TO_SELF, c,
                Animation.RELATIVE_TO_SELF, d);
        mHiddenAction.setDuration(250);
        return mHiddenAction;
    }


    private void initView() {
        mLogin_userName = (ClearWriteEditText) findViewById(R.id.login_userName);
        mLogin_passWord = (ClearWriteEditText) findViewById(R.id.login_passWord);
        mBtn_login = (Button) findViewById(R.id.btn_login);
        mDe_login_logo = (ImageView) findViewById(R.id.de_login_logo);
        mLogin_register = (TextView) findViewById(R.id.login_register);
        mTitleBar = (RelativeLayout) findViewById(R.id.titlebar);
        mBackground = (RelativeLayout) findViewById(R.id.background);
        mLl_name_psw = (LinearLayout) findViewById(R.id.ll_name_psw);
        mLogin_userLogo = (ImageView) findViewById(R.id.login_userLogo);
        mLogin_pswLogo = (ImageView) findViewById(R.id.login_pswLogo);
        mView = findViewById(R.id.view);
        mUserLine = findViewById(R.id.user_line);
        mPswLine = findViewById(R.id.psw_line);

        mNewUser = (TextView) findViewById(R.id.new_user);
        mLogin_desc = (TextView) findViewById(R.id.login_desc);

        if (mLogin_userName.getText().length() == 0 || mLogin_passWord.getText().length() == 0) {
            mBtn_login.setEnabled(false);
        }

        //当把用户名删除后头像要换成默认的
        mLogin_userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mDe_login_logo.setImageResource(R.drawable.no_avatar);
                if (mLogin_userName.getText().length() == 0 || mLogin_passWord.getText().length() == 0) {
                    mBtn_login.setEnabled(false);
                } else {
                    mBtn_login.setEnabled(true);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mLogin_passWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mLogin_userName.getText().length() == 0 || mLogin_passWord.getText().length() == 0) {
                    mBtn_login.setEnabled(false);
                } else {
                    mBtn_login.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public String getUserId() {
        return mLogin_userName.getText().toString().trim();
    }

    public String getPassword() {
        return mLogin_passWord.getText().toString().trim();
    }

    public static Boolean invokeIsTestEvn() {
        try {
            Method method = JMessageClient.class.getDeclaredMethod("isTestEnvironment");
            Object result = method.invoke(null);
            return (Boolean) result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void goToActivity(LoginActivity context, Class<MainActivity> toActivity) {
        Intent intent = new Intent(context, toActivity);
        startActivity(intent);
        finish();
    }
}
