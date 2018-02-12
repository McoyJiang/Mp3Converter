package com.example.danny_jiang.sample.controller;

import android.text.TextUtils;
import android.view.View;

import com.example.danny_jiang.sample.MainActivity;
import com.example.danny_jiang.sample.R;
import com.example.danny_jiang.sample.activity.LoginActivity;
import com.example.danny_jiang.sample.utils.ToastUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by ${chenyn} on 2017/2/16.
 */

public class LoginController implements View.OnClickListener {

    private LoginActivity mContext;

    public LoginController(LoginActivity loginActivity) {
        this.mContext = loginActivity;
    }

    private boolean isContainChinese(String str) {
        Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

    private boolean whatStartWith(String str) {
        Pattern pattern = Pattern.compile("^([A-Za-z]|[0-9])");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

    private boolean whatContain(String str) {
        Pattern pattern = Pattern.compile("^[0-9a-zA-Z][a-zA-Z0-9_\\-@\\.]{3,127}$");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                //登陆验证
                final String userId = mContext.getUserId();
                final String password = mContext.getPassword();
                if (TextUtils.isEmpty(userId)) {
                    ToastUtil.shortToast(mContext, "用户名不能为空");
                    mContext.mLogin_userName.setShakeAnimation();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    ToastUtil.shortToast(mContext, "密码不能为空");
                    mContext.mLogin_passWord.setShakeAnimation();
                    return;
                }
                if (userId.length() < 4 || userId.length() > 128) {
                    mContext.mLogin_userName.setShakeAnimation();
                    ToastUtil.shortToast(mContext, "用户名为4-128位字符");
                    return;
                }
                if (password.length() < 4 || password.length() > 128) {
                    mContext.mLogin_userName.setShakeAnimation();
                    ToastUtil.shortToast(mContext, "密码为4-128位字符");
                    return;
                }
                if (isContainChinese(userId)) {
                    mContext.mLogin_userName.setShakeAnimation();
                    ToastUtil.shortToast(mContext, "用户名不支持中文");
                    return;
                }
                if (!whatStartWith(userId)) {
                    mContext.mLogin_userName.setShakeAnimation();
                    ToastUtil.shortToast(mContext, "用户名以字母或者数字开头");
                    return;
                }
                if (!whatContain(userId)) {
                    mContext.mLogin_userName.setShakeAnimation();
                    ToastUtil.shortToast(mContext, "只能含有: 数字 字母 下划线 . - @");
                    return;
                }
                //登陆
                //if (JGApplication.registerOrLogin % 2 == 1) {
                if (true) {
                    JMessageClient.login(userId, password, new BasicCallback() {
                        @Override
                        public void gotResult(int responseCode, String responseMessage) {
                            if (responseCode == 0) {

                                ToastUtil.shortToast(mContext, "登陆成功");
                                mContext.goToActivity(mContext, MainActivity.class);
                            } else {
                                ToastUtil.shortToast(mContext, "登陆失败" + responseMessage);
                            }
                        }
                    });
                    //注册
                } else {
                    JMessageClient.register(userId, password, new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {

                        }
                    });
                }
                break;
            case R.id.login_register:
            case R.id.new_user:
                mContext.mLogin_passWord.setText("");
                //if (JGApplication.registerOrLogin % 2 == 0) {
                if (true) {
                    mContext.mBtn_login.setText("注册");
                    mContext.mNewUser.setText("去登陆");
                    mContext.mLogin_register.setText("立即登陆");
                    mContext.mLogin_desc.setText("已有账号? ");
                } else {
                    mContext.mBtn_login.setText("登录");
                    mContext.mNewUser.setText("新用户");
                    mContext.mLogin_register.setText("立即注册");
                    mContext.mLogin_desc.setText("还没有账号? ");
                }
                break;
        }
    }
}
