package net.kailyard.template.system.repository;

import net.kailyard.template.common.repository.BaseRepository;
import net.kailyard.template.system.entity.Permission;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PermissionDao extends BaseRepository<Permission, Long> {
    @Transactional(readOnly = true)
    List<Permission> findByMenuId(Long id);

    Permission findByPermission(String permission);
}
