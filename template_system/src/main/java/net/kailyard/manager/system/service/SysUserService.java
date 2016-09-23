package net.kailyard.manager.system.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import net.kailyard.common.domain.Tree;
import net.kailyard.common.exception.ApplicationRuntimeException;
import net.kailyard.common.persistence.DynamicSpecifications;
import net.kailyard.common.service.BaseService;
import net.kailyard.common.utils.Constants;
import net.kailyard.common.utils.PageUtil;
import net.kailyard.manager.system.entity.Role;
import net.kailyard.manager.system.entity.SysUser;
import net.kailyard.manager.system.entity.SysUserRoleRel;
import net.kailyard.manager.system.repository.RoleDao;
import net.kailyard.manager.system.repository.SysUserDao;
import net.kailyard.manager.system.repository.SysUserRoleDao;
import net.kailyard.manager.system.utils.SysUserUtils;
import net.kailyard.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static net.kailyard.manager.system.utils.SysUserUtils.entryptPassword;

/**
 *
 */
@Service
@Transactional
public class SysUserService extends BaseService<SysUser, Long> {
    @Autowired
    private RoleDao roleDao;

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private SysUserRoleDao sysUserRoleDao;

    /**
     * 根据登录名获得操作员信息
     * @param loginName
     * @return
     */
    @Transactional(readOnly = true)
    public SysUser findUserByLoginName(String loginName) {
        return sysUserDao.findByLoginName(loginName);
    }

    /**
     * 获得操作员信息
     * @param userId
     * @return
     */
    public SysUser getUser(Long userId) {
        return findOne(userId);
    }

    /**
     * 新增操作员
     * @param user
     */
    public void add(SysUser user) {
        if (null != sysUserDao.findByLoginName(user.getLoginName())) {
            throw new ApplicationRuntimeException("登录名已存在!");
        }

        save(entryptPassword(user));
    }

    /**
     * 修改操作员
     * @param newSysUser
     */
    public void modify(SysUser newSysUser) {
        SysUser sysUser = findOne(newSysUser.getId());
        if (null == sysUser) {
            throw new ApplicationRuntimeException("该用户不存在,无法修改!");
        }

        sysUser.setEmail(newSysUser.getEmail());
        sysUser.setName(newSysUser.getName());
        if (!Strings.isNullOrEmpty(newSysUser.getPlainPassword())) {
            sysUser.setPlainPassword(newSysUser.getPlainPassword());
            sysUser = entryptPassword(sysUser);
        }
        save(sysUser);
    }

    /**
     * 修改密码
     * @param userId
     * @param oldPassword
     * @param newPassword
     */
    public void modifyPassword(Long userId, String oldPassword, String newPassword) {
        SysUser user = findOne(userId);
        if(user==null){
            throw new ApplicationRuntimeException("用户不存在");
        }

        if(SysUserUtils.checkPassword(user, oldPassword)){
            user.setPlainPassword(newPassword);
            user = SysUserUtils.entryptPassword(user);

            save(user);
        } else {
            throw new ApplicationRuntimeException("旧密码不对");
        }
    }

    /**
     * 返回后台管理人员列表
     *
     * @param searchParams
     * @param page
     * @param size
     * @return
     */
    public Page<SysUser> findAll(Map<String, Object> searchParams, int page, int size) {
        Specification<SysUser> spec = DynamicSpecifications.buildSpecification(searchParams, SysUser.class);
        return sysUserDao.findAll(spec, PageUtil.build(page, size));
    }

    /**
     * 删除给定id的操作员
     *
     * @param ids
     */
    public void deleteByIds(String ids) {
        for (Long userId : StringUtils.convertToLongs(ids, ",")) {
            //系统管理员不可删除
            if (userId.longValue() == 1l) {
                continue;
            }

            //删除操作员和角色的关系
            sysUserRoleDao.delByUserId(userId);
            delete(userId);
        }
    }

    /**
     * 获得操作员的角色树
     * @param userId
     * @return
     */
    public List<Tree> findSysUserRoleTreeByUserId(Long userId) {
        List<Long> selectedRoleIds = sysUserRoleDao.findRoleIdsByUserId(userId);

        List<Tree> result = Lists.newArrayList();
        for (Role role : roleDao.findAll()) {
            result.add(new Tree(role.getId().toString(), role.getName(), role.getId().toString(),
                    selectedRoleIds.contains(role.getId())));
        }
        return result;
    }

    /**
     * 保存操作员对应的角色
     * @param userId
     * @param ids
     */
    public void saveSysUserRoles(Long userId, String ids) {
        //删除操作员对应的角色
        sysUserRoleDao.delByUserId(userId);

        if(Strings.isNullOrEmpty(ids)){
            return;
        }

        String[] roleIds = ids.split(Constants.SEP_COMMA);
        for (String roleId : roleIds) {
            SysUserRoleRel sysUserRoleRel = new SysUserRoleRel();
            sysUserRoleRel.setUserId(userId);
            sysUserRoleRel.setRoleId(Long.parseLong(roleId));
            sysUserRoleDao.save(sysUserRoleRel);
        }
    }
}
