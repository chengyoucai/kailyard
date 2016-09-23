package net.kailyard.manager.system.repository;

import net.kailyard.common.repository.BaseRepository;
import net.kailyard.manager.system.entity.SysConfig;

public interface SysConfigDao extends BaseRepository<SysConfig, Long> {
    SysConfig findByConfigName(String configName);
}
