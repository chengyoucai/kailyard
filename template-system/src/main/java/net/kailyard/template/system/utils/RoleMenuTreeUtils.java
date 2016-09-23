package net.kailyard.template.system.utils;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import net.kailyard.template.common.domain.Tree;
import net.kailyard.template.system.entity.RoleMenuRel;
import net.kailyard.template.system.entity.RolePermissionRel;
import net.kailyard.template.utils.Constants;
import net.kailyard.template.system.entity.Menu;
import net.kailyard.template.system.entity.Permission;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 */
public final class RoleMenuTreeUtils {
    public final static String MENU_PRE = "menu_";
    public final static String PERMISSION_PRE = "permission_";

    private RoleMenuTreeUtils() {
    }

    public static Tree convertTree(Menu menu) {
        return new Tree(MENU_PRE + menu.getId(), menu.getName(), MENU_PRE + menu.getParentId());
    }

    public static Tree convertTree(Permission permission, Long menuId, List<Long> selectedPermissionIds) {
        return new Tree(PERMISSION_PRE + permission.getId(), permission.getName(), MENU_PRE + menuId,
                selectedPermissionIds.contains(permission.getId()));
    }

    public static List<Long> selectedMenuIds(List<RoleMenuRel> roleMenuRels) {
        List<Long> selectedMenuIds = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(roleMenuRels)) {
            for (RoleMenuRel roleMenuRel : roleMenuRels) {
                selectedMenuIds.add(roleMenuRel.getMenuId());
            }
        }

        return selectedMenuIds;
    }

    public static List<Long> selectedPermissionIds(List<RolePermissionRel> rolePermissionRels) {
        List<Long> selectedPermissionIds = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(rolePermissionRels)) {
            for (RolePermissionRel rolePermissionRel : rolePermissionRels) {
                selectedPermissionIds.add(rolePermissionRel.getPermissionId());
            }
        }

        return selectedPermissionIds;
    }

    public static void parseId(String ids, Long roleId, List<RoleMenuRel> roleMenus, List<RolePermissionRel> rolePermissionRels, Long createUserId, Date date){
        String[] menuPermissionIds = ids.split(Constants.SEP_COMMA);
        for (String menuPermissionId : menuPermissionIds) {
            if(menuPermissionId.startsWith(MENU_PRE)){
                Long id = convertId(menuPermissionId, MENU_PRE);
                if(id == null){
                    continue;
                }

                roleMenus.add(new RoleMenuRel(roleId, id, createUserId, date));
            } else if (menuPermissionId.startsWith(PERMISSION_PRE)){
                Long id = convertId(menuPermissionId, PERMISSION_PRE);
                if(id == null){
                    continue;
                }

                rolePermissionRels.add(new RolePermissionRel(roleId, id, createUserId, date));
            }
        }
    }

    public static Long convertId(String idString, String pre){
        String id = idString.substring(pre.length());
        if(Strings.isNullOrEmpty(id)){
            return null;
        }

        return Long.parseLong(id);
    }
}
