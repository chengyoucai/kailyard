package net.kailyard.manager.system.service;

import net.kailyard.common.exception.ApplicationRuntimeException;
import net.kailyard.common.service.BaseService;
import net.kailyard.manager.system.entity.SysConfig;
import net.kailyard.manager.system.repository.SysConfigDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
@Service
@Transactional
public class SysConfigService extends BaseService<SysConfig, Long> {
    @Autowired
    private SysConfigDao sysConfigDao;

    /**
     * 新增系统参数配置
     * @param sysConfig
     */
    public void add(SysConfig sysConfig) {
        if (null != sysConfigDao.findByConfigName(sysConfig.getConfigName())) {
            throw new ApplicationRuntimeException("configName已存在!");
        }

        save(sysConfig);
    }

    /**
     * 修改系统参数配置
     * @param newSysConfig
     */
    public void modify(SysConfig newSysConfig) {
        SysConfig sysConfig = sysConfigDao.findOne(newSysConfig.getId());
        if (null == sysConfig) {
            throw new ApplicationRuntimeException("该系统参数不存在,无法修改!");
        }

        sysConfig.setConfigValue(newSysConfig.getConfigValue());
        sysConfig.setDescription(newSysConfig.getDescription());
        save(sysConfig);
    }
}
