package net.kailyard.template.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.kailyard.template.common.security.SecurityUtil;
import net.kailyard.template.system.entity.Menu;
import net.kailyard.template.system.repository.RoleMenuDao;
import net.kailyard.template.system.repository.RolePermissionDao;
import net.kailyard.template.system.repository.SysUserRoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
@Transactional
public class AuthorityService {
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	@Autowired
	private RolePermissionDao roleButtonDao;
    @Autowired
    private RoleMenuDao roleMenuDao;

    /**
     * 获得用户的所有权限
     * @param userId
     * @return
     */
	public List<String> findPermissionsByUserId(Long userId) {
		List<Long> roleIds = sysUserRoleDao.findRoleIdsByUserId(userId);

		if(CollectionUtils.isEmpty(roleIds)){
			return Collections.emptyList();
		}

		return roleButtonDao.findPermissionsByRoleIds(roleIds);
	}

    /**
     * 获得当前用户的所有菜单
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Menu> getMenuTree() {
        List<Long> roleIds = sysUserRoleDao.findRoleIdsByUserId(SecurityUtil.getCurrentUserId());

        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }

        // 根据角色IDs获取菜单Ids
        List<Menu> menus = roleMenuDao.findMenuTreeByRoleIds(roleIds);

        if (CollectionUtils.isEmpty(menus)) {
            return Collections.emptyList();
        }

        // 获得一级菜单
        Map<Long, Menu> topMenus = Maps.newHashMap();
        List<Menu> tempMenus = new ArrayList(menus.size());//剩余菜单临时存放
        for (Menu menu : menus) {
            if(menu.getParentId().longValue() == 0L) {
                topMenus.put(menu.getId(), menu);
            }else {
                tempMenus.add(menu);
            }
        }

        //从余下的菜单里建立二级和一级菜单关系
        for (Menu menu : tempMenus) {
            Menu parentMenu = topMenus.get(menu.getParentId());
            if (parentMenu != null) {
                parentMenu.addChildren(menu);
            }
        }

        List<Menu> retMenus = Lists.newArrayList(topMenus.values());

        // 对子menu进行排序
        for (Menu menu : retMenus) {
            if(!CollectionUtils.isEmpty(menu.getChildren())) {
                Collections.sort(menu.getChildren(), new Comparator<Menu>() {
                    @Override
                    public int compare(Menu o1, Menu o2) {
                        return o1.getSort() - o2.getSort();
                    }
                });
            }
        }

        // 对主menu进行排序
        Collections.sort(retMenus, new Comparator<Menu>() {
            @Override
            public int compare(Menu o1, Menu o2) {
                return o1.getSort() - o2.getSort();
            }
        });
		return retMenus;
	}
}
