package net.kailyard.template.system.repository;

import net.kailyard.template.common.repository.BaseRepository;
import net.kailyard.template.system.entity.Menu;
import net.kailyard.template.system.entity.RoleMenuRel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RoleMenuDao extends BaseRepository<RoleMenuRel, Long> {
    /**
     * 根据roleids获得父menu不为空的菜单(只有父id不为空才能组成菜单树)
     * @param ids
     * @return
     */
    @Transactional(readOnly = true)
    @Query("select distinct b  from Menu b, RoleMenuRel a where b.id = a.menuId and b.parentId != null and a.roleId in (?1)")
    List<Menu> findMenuTreeByRoleIds(List<Long> ids);

    @Modifying
    @Transactional
    @Query("delete from RoleMenuRel r where r.menuId = ?1")
    void delByMenuId(Long menuId);

    @Modifying
    @Transactional
    @Query("delete from RoleMenuRel r where r.roleId = ?1")
    void delByRoleId(Long roleId);

    List<RoleMenuRel> findByRoleId(Long roleId);
}
