package net.kailyard.template.system.service;

import com.google.common.collect.Maps;
import net.kailyard.template.common.exception.ApplicationRuntimeException;
import net.kailyard.template.common.service.BaseService;
import net.kailyard.template.system.entity.Menu;
import net.kailyard.template.system.entity.Permission;
import net.kailyard.template.system.repository.MenuDao;
import net.kailyard.template.system.repository.PermissionDao;
import net.kailyard.template.system.repository.RolePermissionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PermissionService extends BaseService<Permission, Long> {
    @Autowired
    private RolePermissionDao rolePermissionDao;

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private MenuDao menuDao;

    public Page<Permission> findPermissions(Map<String, Object> searchParams,
            PageRequest pageRequest) {
        Page<Permission> list = findAll(searchParams, pageRequest);

        List<Permission> l = list.getContent();
        if(CollectionUtils.isEmpty(l)){
            return list;
        }

        List<Menu> menus = menuDao.findMenus();
        Map<Long, String> menuNames = Maps.newHashMap();
        for (Menu menu : menus) {
            menuNames.put(menu.getId(), menu.getName());
        }

        for (Permission p : l) {
            p.setMenuName(menuNames.get(p.getMenuId()));
        }

        return list;
    }

    /**
     *
     * 删除权限
     *
     * @param id
     */
    public void del(Long id) {
        //删除权限和角色的对应关系
        rolePermissionDao.delByPermissionId(id);
        delete(id);
    }

    /**
     * 新增权限
     * @param permission
     */
    public void add(Permission permission) {
        if (null != permissionDao.findByPermission(permission.getPermission())) {
            throw new ApplicationRuntimeException("权限已存在!");
        }

        save(permission);
    }

    /**
     * 修改权限
     * @param permission
     */
    public void modify(Permission permission) {
        Permission permissionDB = permissionDao.findByPermission(permission.getPermission());
        if (null != permissionDB && !permissionDB.getId().equals(permission.getId())) {
            throw new ApplicationRuntimeException("权限已存在!");
        }

        save(permission);
    }
}
