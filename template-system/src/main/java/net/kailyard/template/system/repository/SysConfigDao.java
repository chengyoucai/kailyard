package net.kailyard.template.system.repository;

import net.kailyard.template.common.repository.BaseRepository;
import net.kailyard.template.system.entity.SysConfig;

public interface SysConfigDao extends BaseRepository<SysConfig, Long> {
    SysConfig findByConfigName(String configName);
}
