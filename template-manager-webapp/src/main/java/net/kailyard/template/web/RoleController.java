package net.kailyard.template.web;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import net.kailyard.template.common.domain.Grid;
import net.kailyard.template.common.domain.Result;
import net.kailyard.template.common.domain.Tree;
import net.kailyard.template.utils.Servlets;
import net.kailyard.template.system.entity.Role;
import net.kailyard.template.system.service.RoleService;
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

@Controller
@RequestMapping(value = "/role")
public class RoleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "index")
    public String index() {
        return "system/roleList";
    }

    @RequestMapping(value = "list")
    @ResponseBody
    @RequiresPermissions("sys:role:list")
    public Grid<Role> list(@RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "rows", defaultValue = "20") int size, HttpServletRequest request) {
        // 获取查询条件
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

        try {
            Page<Role> list = roleService.findAll(searchParams, page, size);
            return new Grid(list.getTotalElements(), list.getContent());
        } catch (Exception ex) {
            LOGGER.error("roleList occur exception.", ex);
            return new Grid();
        }
    }

    @RequestMapping(value = "get")
    @ResponseBody
    @RequiresPermissions("sys:role:edit")
    public Role get(@RequestParam("id") Long id) {
        return roleService.findOne(id);
    }

    @RequestMapping(value = "save")
    @ResponseBody
    @RequiresPermissions(value={"sys:role:edit","sys:role:add"},logical= Logical.OR)
    public Result save(@ModelAttribute("role") Role role) {
        Result result = new Result();
        try {
            if(role==null|| Strings.isNullOrEmpty(role.getName())){
                LOGGER.warn("save role occur exception.role[{}]", role);
                result.setFailure("参数错误");
                return result;
            }
            roleService.save(role);
            result.setSuccess();
        } catch (Exception ex) {
            LOGGER.error("save role error. role[{}]", role, ex);
            result.setFailure();
        }
        return result;
    }

    @RequestMapping(value = "del")
    @ResponseBody
    @RequiresPermissions("sys:role:del")
    public Result del(@RequestParam("ids") String ids) {
        Result result = new Result();
        try {
            roleService.deleteByIds(ids);
            result.setSuccess();
        } catch (Exception ex) {
            LOGGER.error("delete role error. ids[{}]", ids, ex);
            result.setFailure();
        }
        return result;
    }

    @RequestMapping(value = "getRolePermissionTree")
    @ResponseBody
    @RequiresPermissions("sys:role:authority")
    public List<Tree> getRolePermissionTree(@RequestParam("roleId") Long roleId) {
        try {
            return roleService.findRolePermissionTreeByRoleId(roleId);
        } catch (Exception ex) {
            LOGGER.error("getRoleMenuTree exception. roleId[{}]", roleId, ex);
            return Lists.newArrayList();
        }
    }

    @RequestMapping(value = "saveRolePermissions")
    @ResponseBody
    @RequiresPermissions("sys:role:authority")
    public Result saveRolePermissions(@RequestParam("roleId") Long roleId, @RequestParam("menuIds") String ids) {
        Result result = new Result();
        try {
            roleService.saveRolePermissions(roleId, ids);
            result.setSuccess();
        } catch (Exception ex) {
            LOGGER.error("saveRolePermissions exception. roleId[{}], menuIds[{}]", roleId, ids, ex);
            result.setFailure();
        }
        return result;
    }
}
