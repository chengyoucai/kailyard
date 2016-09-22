package net.kailyard.manager.system.repository;

import net.kailyard.common.repository.BaseRepository;
import net.kailyard.manager.system.entity.SysUserRoleRel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SysUserRoleDao extends BaseRepository<SysUserRoleRel, Long> {
    @Transactional(readOnly = true)
    @Query("select r.roleId from SysUserRoleRel r where r.userId=?1 ")
    List<Long> findRoleIdsByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("delete from SysUserRoleRel r where r.roleId = ?1")
    void delByRoleId(Long roleId);

    @Modifying
    @Transactional
    @Query("delete from SysUserRoleRel r where r.userId = ?1")
    void delByUserId(Long userId);

}
