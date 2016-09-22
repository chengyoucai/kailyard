package net.kailyard.common.service;

import net.kailyard.common.entity.BaseEntity;
import net.kailyard.common.persistence.DynamicSpecifications;
import net.kailyard.common.repository.BaseRepository;
import net.kailyard.common.security.SecurityUtil;
import net.kailyard.common.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 通用基础类
 */
public abstract class BaseService<T, ID extends Serializable> {
	@Autowired
	private BaseRepository<T, ID> baseRepository;

	private Class<T> entityClass;

	@SuppressWarnings("unchecked")
	public BaseService() {
		if (null == entityClass) {
			entityClass = (Class<T>) ((ParameterizedType) getClass()
					.getGenericSuperclass()).getActualTypeArguments()[0];
		}
	}

	@Transactional(readOnly = false)
	public T save(T entity) {
		if(entity instanceof BaseEntity){
            BaseEntity target = (BaseEntity) entity;
            if(target.getId()==null){
                target.setCreateTime(new Date());
                target.setCreateUserId(SecurityUtil.getCurrentUserId());
            } else {
                target.setUpdateTime(new Date());
                target.setUpdateUserId(SecurityUtil.getCurrentUserId());
            }
//        } else if(entity instanceof RelEntity){
//            RelEntity target = (RelEntity) entity;
//            if(target.getId()==null){
//                target.setCreateTime(new Date());
//                target.setCreateUserId(SecurityUtil.getCurrentUserId());
//            }
        }
		return baseRepository.save(entity);
	}

	@Transactional(readOnly = false)
	public void delete(ID id) {
		baseRepository.delete(id);
	}

	@Transactional(readOnly = false)
	public void delete(T entity) {
		baseRepository.delete(entity);
	}

	@Transactional(readOnly = false)
	public void delete(List<T> entities) {
		baseRepository.delete(entities);
	}

	@Transactional(readOnly = true)
	public T findOne(ID id) {
		return baseRepository.findOne(id);
	}

	@Transactional(readOnly = true)
	public List<T> findAll(List<ID> ids) {
		return (List<T>) baseRepository.findAll(ids);
	}

    @Transactional(readOnly = true)
    public Page<T> findAll(Map<String, Object> searchParams,
            int page, int size) {
        return findAll(searchParams, PageUtil.build(page, size));
    }

	@Transactional(readOnly = true)
	public Page<T> findAll(Map<String, Object> searchParams,
			PageRequest pageRequest) {
		Specification<T> spec = DynamicSpecifications.buildSpecification(searchParams,
				entityClass);
		return baseRepository.findAll(spec, pageRequest);
	}

	@Transactional(readOnly = true)
	public List<T> findAll(Map<String, Object> searchParams) {
		Specification<T> spec = DynamicSpecifications.buildSpecification(searchParams,
				entityClass);
		return baseRepository.findAll(spec);
	}
}
