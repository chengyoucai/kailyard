package net.kailyard.template.system.repository;

import net.kailyard.template.common.repository.BaseRepository;
import net.kailyard.template.system.entity.Menu;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MenuDao extends BaseRepository<Menu, Long> {
    @Transactional(readOnly = true)
    List<Menu> findByParentIdOrderBySortAsc(Long pid);

    @Transactional(readOnly = true)
    List<Menu> findByParentId(Long pid);

    @Transactional(readOnly = true)
    List<Menu> findByParentIdGreaterThanOrderByParentIdAscSortAsc(Long parentId);

    @Transactional(readOnly = true)
    @Query(value="select new Menu(m.id, m.name) from Menu m")
    List<Menu> findMenus();
}
