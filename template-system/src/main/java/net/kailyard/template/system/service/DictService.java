package net.kailyard.template.system.service;

import net.kailyard.template.common.service.BaseService;
import net.kailyard.template.system.entity.Dict;
import net.kailyard.template.system.repository.DictDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 */
@Service
@Transactional
public class DictService extends BaseService<Dict, Long> {
    @Autowired
    private DictDao dictDao;

    /**
     * 返回某种类型的数据字典
     * @param dictType
     */
    @Transactional(readOnly=true)
    public List<Dict> findByType(String dictType) {
        return dictDao.findByDictTypeOrderBySortAsc(dictType);
    }
}
