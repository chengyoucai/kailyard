package net.kailyard.manager.web;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import net.kailyard.common.domain.Grid;
import net.kailyard.common.domain.Result;
import net.kailyard.common.domain.Tree;
import net.kailyard.common.exception.ApplicationRuntimeException;
import net.kailyard.common.security.SecurityUtil;
import net.kailyard.common.utils.web.Servlets;
import net.kailyard.manager.system.entity.SysUser;
import net.kailyard.manager.system.service.SysUserService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 用户管理controller
 */
@Controller
@RequestMapping(value = "/sysUser")
public class SysUserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SysUserController.class);

    @Autowired
    private SysUserService sysUserService;

    @RequestMapping(value = "index")
    public String index() {
        return "system/userList";
    }

    @RequestMapping(value = "list")
    @ResponseBody
    @RequiresPermissions("sys:user:list")
    public Grid<SysUser> list(@RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "rows", defaultValue = "20") int size, HttpServletRequest request) {
        // 获取查询条件
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

        try {
            Page<SysUser> list = sysUserService.findAll(searchParams, page, size);
            return new Grid(list.getTotalElements(), list.getContent());
        } catch (Exception ex) {
            LOGGER.error("list error", ex);
            return new Grid();
        }
    }

    @RequestMapping(value = "get")
    @ResponseBody
    @RequiresPermissions("sys:user:list")
    public SysUser get(@RequestParam("id") Long id) {
        return sysUserService.getUser(id);
    }

    @RequestMapping(value = "save")
    @ResponseBody
    @RequiresPermissions(value={"sys:user:edit","sys:user:add"},logical= Logical.OR)
    public Result save(@ModelAttribute("sysUser") SysUser user) {
        Result result = new Result();
        try {
            if(user==null){
                LOGGER.warn("save SysUser occur exception.SysUser is null");
                result.setFailure("参数错误");
            }

            if(user.getId()!=null){
                if(Strings.isNullOrEmpty(user.getName())){
                    LOGGER.warn("update SysUser occur exception.SysUser[{}]", user);
                    result.setFailure("参数错误");
                }
                sysUserService.modify(user);
            } else {
                if(Strings.isNullOrEmpty(user.getName())||Strings.isNullOrEmpty(user.getLoginName())||Strings.isNullOrEmpty(user.getPlainPassword())){
                    LOGGER.warn("add SysUser occur exception.SysUser[{}]", user);
                    result.setFailure("参数错误");
                }
                user.setRoles("normal");
                sysUserService.add(user);
            }
            result.setSuccess();
        } catch (ApplicationRuntimeException ex) {
            LOGGER.error("save SysUser occur exception. SysUser[{}]", user, ex);
            result.setFailure(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.error("save SysUser occur exception. SysUser[{}]", user, ex);
            result.setFailure();
        }
        return result;
    }

    @RequestMapping(value = "del")
    @ResponseBody
    @RequiresPermissions("sys:user:del")
    public Result del(@RequestParam("ids") String ids) {
        Result result = new Result();
        try {
            sysUserService.deleteByIds(ids);
            result.setSuccess();
        } catch (Exception ex) {
            LOGGER.error("delete sysuser occur exception. ids[{}]", ids, ex);
            result.setFailure();
        }
        return result;
    }

    @RequestMapping(value = "getSysUserRoleTree")
    @ResponseBody
    @RequiresPermissions("sys:user:role:auth")
    public List<Tree> getSysUserRoleTree(@RequestParam("userId") Long userId) {
        try {
            return sysUserService.findSysUserRoleTreeByUserId(userId);
        } catch (Exception ex) {
            LOGGER.error("getSysUserRoleTree occur exception. userId[{}]", userId, ex);
            return Lists.newArrayList();
        }
    }

    @RequestMapping(value = "saveSysUserRoles")
    @ResponseBody
    @RequiresPermissions("sys:user:role:auth")
    public Result saveSysUserRoles(@RequestParam("userId") Long userId, @RequestParam(value="id", required = false) String roleIds) {
        Result result = new Result();
        try {
            sysUserService.saveSysUserRoles(userId, roleIds);
            result.setSuccess();
        } catch (Exception ex) {
            LOGGER.error("saveSysUserRoles occur exception. userId[{}], roleIds[{}]", userId, roleIds, ex);
            result.setFailure();
        }
        return result;
    }

    @RequestMapping(value = "savePa")
    @ResponseBody
    public Result savePa(@RequestParam("oldPassword") String oldPassword, @RequestParam("plainPassword") String plainPassword) {
        Result result = new Result();
        Long userId = SecurityUtil.getCurrentUserId();
        if(userId==null){
            result.setFailure("请登录");
            return result;
        }

        try {
            sysUserService.modifyPassword(userId, oldPassword, plainPassword);
            result.setSuccess();
        } catch (ApplicationRuntimeException ex) {
            LOGGER.error("savePa occur exception.", ex);
            result.setFailure(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.error("savePa occur exception.", ex);
            result.setFailure();
        }
        return result;
    }
}
