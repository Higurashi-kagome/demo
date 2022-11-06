package com.chalco.holder.common;

import com.chalco.holder.R;
import com.chalco.holder.entity.Permission;
import com.chalco.holder.entity.User;

import java.util.ArrayList;
import java.util.List;

public class PermissionUtils {
    /**
     * 所有入口元素的 id 组成的数组
     */
    private static final int[] permissionArr = new int[]{
            R.id.remember_pass_layout,

            R.id.start_receipt,
            R.id.start_reject_sales,
            R.id.sampling_register,
            R.id.sampling2_register,
            R.id.search_history,
            R.id.clear_history,
            R.id.change_password,
            R.id.erase_tag,
            R.id.test_write_trigger,
            R.id.test_read_trigger,

            R.id.receipt,
            R.id.search_receipt_card,
            R.id.receipt_directly,
            R.id.secondary_confirmation,
    };

    /**
     * 收货
     */
    private static final int[] acceptPermissionArr = new int[]{
            R.id.remember_pass_layout,

            R.id.start_receipt,
            R.id.start_reject_sales,
            R.id.search_history,
            R.id.clear_history,
            R.id.change_password,

            R.id.erase_tag,

            R.id.receipt,
            R.id.search_receipt_card,
            R.id.receipt_directly,
    };

    /**
     * 质检
     */
    private static final int[] samplingPermissionArr = new int[]{
            R.id.remember_pass_layout,

            R.id.start_receipt,
            R.id.start_reject_sales,
            R.id.sampling_register,
            R.id.sampling2_register,
            R.id.search_history,
            R.id.clear_history,
            R.id.change_password,

            R.id.erase_tag,

            R.id.receipt_directly,
            R.id.secondary_confirmation,
    };

    /**
     * 获取所有权限的列表
     * @return 所有权限的列表
     */
    public static List<Permission> getAllPermissions() {
        return getPermissionsList(permissionArr);
    }

    /**
     * 收货的权限列表
     */
    public static List<Permission> getAcceptPermissions() {
        return getPermissionsList(acceptPermissionArr);
    }

    /**
     * 质检权限列表
     */
    public static List<Permission> getSamplingPermissions() {
        return getPermissionsList(samplingPermissionArr);
    }

    /**
     * permission 数组转 List
     * @param permissionArr permission 数组
     * @return permission List
     */
    private static List<Permission> getPermissionsList(int[] permissionArr) {
        List<Permission> permissions = new ArrayList<>(permissionArr.length);
        for (int i = 0; i < permissionArr.length; i++) {
            permissions.add(new Permission(permissionArr[i]));
        }
        return permissions;
    }

    /**
     * 创建用户保存到数据库
     * @param password 用户密码
     * @return 是否保存成功
     */
    public static boolean createAdmin(String password) {
        User admin = new User(Constant.INIT_USER_NAME, password);
        // 初始化权限
//        List<Permission> permissionList = PermissionUtils.getAcceptPermissions();
//        List<Permission> permissionList = PermissionUtils.getSamplingPermissions();
        List<Permission> permissionList = PermissionUtils.getAllPermissions();
        for (Permission permission : permissionList) {
            permission.save();
            // permission 保存在数据库，然后将 permission 保存在 admin 中，之后保存 admin，LitePal 就能够建立表关系
            admin.getPermissionList().add(permission);
        }
        boolean save = admin.save();
        return save;
    }
}
