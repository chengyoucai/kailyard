package net.kailyard.manager.system.repository;

import net.kailyard.common.repository.BaseRepository;
import net.kailyard.manager.system.entity.SysUser;

public interface SysUserDao extends BaseRepository<SysUser, Long> {
    SysUser findByLoginName(String loginName);
}
