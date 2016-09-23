package net.kailyard.template.system.service;

import com.google.common.collect.Lists;
import net.kailyard.template.common.service.BaseService;
import net.kailyard.template.system.entity.Menu;
import net.kailyard.template.system.entity.Permission;
import net.kailyard.template.system.repository.MenuDao;
import net.kailyard.template.system.repository.PermissionDao;
import net.kailyard.template.system.repository.RoleMenuDao;
import net.kailyard.template.system.repository.RolePermissionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@Transactional
public class MenuService extends BaseService<Menu, Long> {
	@Autowired
	private MenuDao menuDao;

    @Autowired
    private RoleMenuDao roleMenuDao;

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private RolePermissionDao rolePermissionDao;

    /**
	 *
	 * 获取menu的treeGrid列表
	 *
	 * @return
	 */
	public List<Menu> getMenuTree() {
        return getMenuTree(true);
	}

	/**
	 * 
	 * 根据ParentId获取菜单列表
	 * 
	 * @param pid
	 * @return
	 */
	public List<Menu> getByPid(Long pid) {
		return menuDao.findByParentIdOrderBySortAsc(pid);
	}

	/**
	 *
	 * 删除菜单信息
	 *
	 * @param id
	 */
	public void del(Long id) {
		List<Menu> childMenu = menuDao
				.findByParentId(id);
		if (!CollectionUtils.isEmpty(childMenu)) {
			for (Menu menu : childMenu) {
                deleteMenuAndPermission(menu.getId());
			}
		}

        deleteMenuAndPermission(id);
	}

    //删除子菜单,以及这些菜单对应的permission,以及这些菜单和角色的关系
    private void deleteMenuAndPermission(Long menuId){
        List<Permission> ps = permissionDao.findByMenuId(menuId);
        //删除角色和权限关系
        if (!CollectionUtils.isEmpty(ps)) {
            for (Permission permission : ps) {
                rolePermissionDao.delByPermissionId(permission.getId());
            }
        }

        //删除角色和菜单关系
        roleMenuDao.delByMenuId(menuId);
        //删除菜单和级联删除permission
		menuDao.delete(menuId);
    }

    /**
     *
     * @param isReturnTopMenusHasNoSub 是否返回不包含submenu的topmenu
     * @return
     */
    public List<Menu> getMenuTree(boolean isReturnTopMenusHasNoSub) {
		List<Menu> result = Lists.newArrayList();
		List<Menu> rootMenu = menuDao.findByParentIdOrderBySortAsc(0L);
		if (!CollectionUtils.isEmpty(rootMenu)) {
			for (Menu menu : rootMenu) {
				List<Menu> childMenu = menuDao
						.findByParentIdOrderBySortAsc(menu.getId());
				if (!CollectionUtils.isEmpty(childMenu)) {
					menu.setChildren(childMenu);
                    result.add(menu);
				} else if(isReturnTopMenusHasNoSub){
                    result.add(menu);
                }
			}
		}

		return result;
	}

	/**
     * 获得给定的菜单id对应的权限列表
     * @param menuId
     * @return
     */
    public List<Permission> getPermissionListByMenuId(Long menuId){
        return permissionDao.findByMenuId(menuId);
    }
}
