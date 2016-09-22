package net.kailyard.manager.system.repository;

import net.kailyard.common.repository.BaseRepository;
import net.kailyard.manager.system.entity.Dict;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DictDao extends BaseRepository<Dict, Long> {
    @Transactional(readOnly = true)
    List<Dict> findByDictTypeOrderBySortAsc(String dictType);
}
