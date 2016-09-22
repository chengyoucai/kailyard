package net.kailyard.manager.system.repository;

import net.kailyard.common.repository.BaseRepository;
import net.kailyard.manager.system.entity.Permission;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PermissionDao extends BaseRepository<Permission, Long> {
    @Transactional(readOnly = true)
    List<Permission> findByMenuId(Long id);

    Permission findByPermission(String permission);
}
