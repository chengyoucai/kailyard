package net.kailyard.template.system.repository;

import net.kailyard.template.common.repository.BaseRepository;
import net.kailyard.template.system.entity.Dict;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DictDao extends BaseRepository<Dict, Long> {
    @Transactional(readOnly = true)
    List<Dict> findByDictTypeOrderBySortAsc(String dictType);
}
