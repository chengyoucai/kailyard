package net.kailyard.manager.system.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import net.kailyard.common.domain.Tree;
import net.kailyard.common.security.SecurityUtil;
import net.kailyard.common.service.BaseService;
import net.kailyard.manager.system.entity.*;
import net.kailyard.manager.system.repository.PermissionDao;
import net.kailyard.manager.system.repository.RoleMenuDao;
import net.kailyard.manager.system.repository.RolePermissionDao;
import net.kailyard.manager.system.repository.SysUserRoleDao;
import net.kailyard.manager.system.utils.RoleMenuTreeUtils;
import net.kailyard.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class RoleService extends BaseService<Role, Long> {
    @Autowired
    private RoleMenuDao roleMenuDao;

    @Autowired
    private RolePermissionDao rolePermissionDao;

    @Autowired
    private SysUserRoleDao sysUserRoleDao;

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private MenuService menuService;

    /**
     * 根据传入的ids,删除对应的一组角色
     *
     * @param ids
     */
    public void deleteByIds(String ids) {
        for (Long roleId : StringUtils.convertToLongs(ids, ",")) {
            //系统管理员角色不可删除
            if (roleId.longValue() == 1l) {
                continue;
            }
            roleMenuDao.delByRoleId(roleId);
            rolePermissionDao.delByRoleId(roleId);
            sysUserRoleDao.delByRoleId(roleId);
            //删除role
            delete(roleId);
        }
    }

    /**
     * 保存角色对应的权限
     * @param roleId
     * @param ids
     */
    public void saveRolePermissions(Long roleId, String ids) {
        //删除角色对应的菜单和权限
        roleMenuDao.delByRoleId(roleId);
        rolePermissionDao.delByRoleId(roleId);

        if(Strings.isNullOrEmpty(ids)){
            return;
        }

        List<RoleMenuRel> roleMenus = Lists.newArrayList();
        List<RolePermissionRel> rolePermissionRels = Lists.newArrayList();

        RoleMenuTreeUtils.parseId(ids, roleId, roleMenus, rolePermissionRels, SecurityUtil.getCurrentUserId(), new Date());

        if (!CollectionUtils.isEmpty(roleMenus)){
            roleMenuDao.save(roleMenus);
        }

        if (!CollectionUtils.isEmpty(rolePermissionRels)){
            rolePermissionDao.save(rolePermissionRels);
        }
    }

    /**
     * 获得角色对应的菜单和权限树
     * @param roleId
     * @return
     */
    public List<Tree> findRolePermissionTreeByRoleId(Long roleId) {
        // 获取已关联菜单IDs与按钮IDs
        List<Long> selectedMenuIds = RoleMenuTreeUtils.selectedMenuIds(roleMenuDao.findByRoleId(roleId));

        List<Long> selectedPermissionIds =
                RoleMenuTreeUtils.selectedPermissionIds(rolePermissionDao.findByRoleId(roleId));

        List<Tree> result = Lists.newArrayList();

        //获得所有菜单
        List<Menu> rootMenus = menuService.getMenuTree();
        for (Menu menu : rootMenus) {
            result.add(convertTree(menu, selectedMenuIds, selectedPermissionIds));
        }

        return result;
    }

    protected Tree convertTree(Menu menu, List<Long> selectedMenuIds, List<Long> selectedPermissionIds) {
        Tree menuTree = RoleMenuTreeUtils.convertTree(menu);

        if (!CollectionUtils.isEmpty(menu.getChildren())) {
            for (Menu subMenu : menu.getChildren()) {
                Tree subMenuTree = RoleMenuTreeUtils.convertTree(subMenu);

                menuTree.addChild(addPermissionToMenu(subMenuTree, subMenu.getId(), selectedPermissionIds));
            }
        }

        return menuTree;
    }

    protected Tree addPermissionToMenu(Tree tree, Long menuId, List<Long> selectedPermissionIds) {
        List<Permission> permissions = permissionDao.findByMenuId(menuId);
        if (CollectionUtils.isEmpty(permissions)) {
            return tree;
        }

        for (Permission permission : permissions) {
            tree.addChild(RoleMenuTreeUtils.convertTree(permission, menuId, selectedPermissionIds));
        }
        return tree;
    }
}
