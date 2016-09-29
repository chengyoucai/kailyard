package net.kailyard.template.system.repository;

import net.kailyard.template.common.repository.BaseRepository;
import net.kailyard.template.system.entity.RolePermissionRel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RolePermissionDao extends BaseRepository<RolePermissionRel, Long> {
    @Transactional(readOnly = true)
    @Query("from RolePermissionRel t where t.roleId in (?1)")
    List<RolePermissionRel> findByRoleIds(List<Long> ids);

    @Transactional(readOnly = true)
    @Query(value="select distinct b.permissions from ss_role_permission_rel a, ss_permission b where b.id = a.permission_id and a.role_id in (?1)" ,nativeQuery=true)
    List<String> findPermissionsByRoleIds(List<Long> ids);

    @Modifying
    @Transactional
    @Query("delete from RolePermissionRel r where r.roleId = ?1")
    void delByRoleId(Long roleId);

    @Modifying
    @Transactional
    @Query("delete from RolePermissionRel r where r.permissionId = ?1")
    void delByPermissionId(Long permissionId);

    List<RolePermissionRel> findByRoleId(Long roleId);
}
