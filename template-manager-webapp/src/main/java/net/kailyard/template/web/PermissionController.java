package net.kailyard.template.web;

import com.google.common.base.Strings;
import net.kailyard.template.common.domain.Grid;
import net.kailyard.template.common.domain.Result;
import net.kailyard.template.utils.PageUtil;
import net.kailyard.template.utils.Servlets;
import net.kailyard.template.system.entity.Menu;
import net.kailyard.template.system.entity.Permission;
import net.kailyard.template.system.service.MenuService;
import net.kailyard.template.system.service.PermissionService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/permission")
public class PermissionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PermissionController.class);

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private MenuService menuService;

    @RequestMapping(value = "index")
    public String permissionList() {
        return "system/permissionList";
    }

    @RequestMapping(value = "list")
    @ResponseBody
    @RequiresPermissions("sys:permission:list")
    public Grid<Permission> list(@RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "rows", defaultValue = "20") int size, @RequestParam("order") String order, @RequestParam("sort") String sort, HttpServletRequest request) {
        // 获取查询条件
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

        try {
            PageRequest pageRequest = PageUtil.build(page, size, order, sort);

            Page<Permission> list = permissionService.findPermissions(searchParams, pageRequest);
            return new Grid(list.getTotalElements(), list.getContent());
        } catch (Exception ex) {
            LOGGER.error("PermissionController userList error", ex);
            return new Grid();
        }
    }

    @RequestMapping(value = "menuList")
    @ResponseBody
    @RequiresPermissions("sys:permission:list")
    public List<Menu> menuList(@RequestParam(value = "s", defaultValue = "default") String s) {
        List<Menu> menuList = new ArrayList<>();
        if("default".equals(s)){
            Menu menu = new Menu();
            menu.setName("-- 请选择 --");
            menu.setId(-1l);
            menuList.add(menu);
        }
        menuList.addAll(menuService.getMenuTree(false));
        return menuList;
    }

    @RequestMapping(value = "get")
    @ResponseBody
    @RequiresPermissions("sys:permission:edit")
    public Permission get(@RequestParam("id") Long id) {
        return permissionService.findOne(id);
    }

    @RequestMapping(value = "save")
    @ResponseBody
    @RequiresPermissions(value={"sys:permission:edit","sys:permission:add"},logical= Logical.OR)
    public Result save(@ModelAttribute("permission") Permission permission) {
        Result result = new Result();
        try {
            if(permission==null){
                LOGGER.warn("update permission occur exception.permission is null");
                result.setFailure("参数错误");
            }

            if(permission.getId()!=null){
                if(Strings.isNullOrEmpty(permission.getPermissions())){
                    LOGGER.warn("update permission occur exception.permission[{}]", permission);
                    result.setFailure("参数错误");
                }
                permissionService.modify(permission);
            } else {
                if(Strings.isNullOrEmpty(permission.getName())||Strings.isNullOrEmpty(permission.getPermissions())){
                    LOGGER.warn("add permission occur exception.SysUser[{}]", permission);
                    result.setFailure("参数错误");
                }
                permissionService.add(permission);
            }
            result.setSuccess();
        } catch (Exception ex) {
            LOGGER.error("save permission occur exception.permission[{}]", permission, ex);
            result.setFailure();
        }
        return result;
    }

    @RequestMapping(value = "del")
    @ResponseBody
    @RequiresPermissions("sys:permission:del")
    public Result del(@RequestParam("id") Long id) {
        Result result = new Result();
        try {
            permissionService.del(id);
            result.setSuccess();
        } catch (Exception ex) {
            LOGGER.error("del permission occur exception.", ex);
            result.setFailure();
        }
        return result;
    }
}
