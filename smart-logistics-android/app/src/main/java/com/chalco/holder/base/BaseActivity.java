package com.chalco.holder.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.chalco.holder.common.Constant;
import com.chalco.holder.common.PermissionUtils;
import com.chalco.holder.common.SpUtils;
import com.chalco.holder.common.Validator;
import com.chalco.holder.entity.Permission;
import com.chalco.holder.entity.User;

import org.litepal.LitePal;

import java.util.List;

/**
 * 活动基类
 */
public class BaseActivity extends AppCompatActivity {

    protected final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);//设置左上角的图标是否可以点击
            actionBar.setDisplayHomeAsUpEnabled(true);//给左上角图标的左边加上一个返回的图标
        }
    }

    /**
     * 返回按钮
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String command = intent.getStringExtra(Constant.COMMAND_KEY);
        if (!Validator.isNotBlank(command)) intent.putExtra(Constant.COMMAND_KEY, "");
        setIntent(intent);
    }

    public String getCommand() {
        return getIntent().getStringExtra(Constant.COMMAND_KEY);
    }

    /**
     * 给子类调用实现按权限隐藏内容
     */
    protected void initViewByPermission() {
        String account = SpUtils.getAccount(this);
        if (account == null) return;
        // isEager 参数设为 true，表示激进模式，会查出联表数据（User 下的 List<Permission>）
        List<User> userList = LitePal
                .where("name=?", account)
                .find(User.class, true);
        if (userList.isEmpty()) return;
        User user = userList.get(0);
        List<Permission> allPermissions = PermissionUtils.getAllPermissions();
        List<Permission> permissionList = user.getPermissionList();
        // 用户有的权限，从 allPermissions 中移除
        for (Permission permission : permissionList) {
            // 受 Permission equals 方法的影响
            allPermissions.remove(permission);
        }
        // 此时 allPermissions 中的权限当前用户没有，隐藏对应的元素
        for (Permission permission : allPermissions) {
            int pid = permission.getPid();
            View viewById = findViewById(pid);
            if (viewById != null) viewById.setVisibility(View.GONE);
        }
    }
}
