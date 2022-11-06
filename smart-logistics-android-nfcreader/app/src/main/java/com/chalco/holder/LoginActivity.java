package com.chalco.holder;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import com.chalco.holder.activity.SampleReadTextActivity;
import com.chalco.holder.base.NfcFixActivity;
import com.chalco.holder.common.Constant;
import com.chalco.holder.common.CrashHandler;
import com.chalco.holder.common.PermissionUtils;
import com.chalco.holder.common.SpUtils;
import com.chalco.holder.common.Utils;
import com.chalco.holder.entity.User;

import org.litepal.LitePal;

import java.util.List;

/**
 * 登录界面
 */
public class LoginActivity extends NfcFixActivity {

    private SharedPreferences pref;
    private EditText accountEdit;
    private EditText passwordEdit;
    private Button login;
    private CheckBox rememberPass;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        // 将全局异常处理器加到应用中
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
        setContentView(R.layout.activity_login);
        initAdmin();
        initViewByPermission();
        initView();
    }

    /**
     * 视图初始化
     */
    private void initView() {
        passwordEdit = findViewById(R.id.password);
        // 用户名，获取缓存，实现记住用户名
        accountEdit = findViewById(R.id.account);
        String cachedAccount = SpUtils.getAccount(this);
        if (cachedAccount != null) accountEdit.setText(cachedAccount);
        // 密码，是否记住密码，记住则尝试填充
        rememberPass = findViewById(R.id.remember_pass);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember = pref.getBoolean("remember_password", false);
        if (isRemember) {
            // 将密码设置到文本框中
            String password = pref.getString("password", "");
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
        }
        // 登录按钮
        login = findViewById(R.id.login);
        login.setOnClickListener(v -> {
            String account = accountEdit.getText().toString();
            String password = passwordEdit.getText().toString();
            List<User> userList = LitePal
                    .where("name=? AND password=?", account, password)
                    .find(User.class);
            // 能在数据库找到，则登录成功
            if (!userList.isEmpty()) {
                // 缓存用户名
                SpUtils.putAccount(this, account);
                // 如果点击了记住密码的复选框，则缓存密码
                SharedPreferences.Editor editor = pref.edit();
                if (rememberPass.isChecked()) { // 检查复选框是否被选中
                    editor.putBoolean("remember_password", true);
                    editor.putString("password", password);
                } else {
                    editor.clear();
                }
                editor.apply();
                // 跳转到主界面
                Utils.jumpTo(this, SampleReadTextActivity.class);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 初始化管理员用户（将其添加到用户表）
     */
    private void initAdmin() {
        List<User> userList = LitePal
                .where("name=?", Constant.INIT_USER_NAME)
                .find(User.class);
        // 检查是否保存有 admin 用户，没有保存，则创建管理员用户
        if (userList.isEmpty()) {
            boolean save = PermissionUtils.createAdmin(Constant.INIT_PASSWORD);
            if (!save) {
                Toast.makeText(LoginActivity.this, "初始化数据库出错", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
