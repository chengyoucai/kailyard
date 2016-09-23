package net.kailyard.template.system.repository;

import net.kailyard.template.common.repository.BaseRepository;
import net.kailyard.template.system.entity.SysUser;

public interface SysUserDao extends BaseRepository<SysUser, Long> {
    SysUser findByLoginName(String loginName);
}
