package net.kailyard.manager.web;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import net.kailyard.common.domain.Grid;
import net.kailyard.common.domain.Result;
import net.kailyard.manager.system.entity.Menu;
import net.kailyard.manager.system.service.MenuService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/menu")
public class MenuController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private MenuService menuService;

    @RequestMapping(value = "index")
    public String index() {
        return "system/menuList";
    }

    @RequestMapping(value = "treeList")
    @ResponseBody
    @RequiresPermissions("sys:menu:list")
    public Grid<Menu> menuTree() {
        try {
            return new Grid<>(0L, menuService.getMenuTree());
        } catch (Exception ex) {
            LOGGER.error("menuTree occur exception.", ex);
            return new Grid<Menu>();
        }
    }

    @RequestMapping(value = "getRootMenuList")
    @ResponseBody
    @RequiresPermissions("sys:menu:list")
    public List<Menu> getMenuList() {
        List<Menu> result = Lists.newArrayList();
        Menu menu = new Menu();
        menu.setId(0L);
        menu.setName("主菜单");
        result.add(menu);
        List<Menu> list = menuService.getByPid(0L);
        if (!CollectionUtils.isEmpty(list)) {
            result.addAll(list);
        }
        return result;
    }

    @RequestMapping(value = "get")
    @ResponseBody
    @RequiresPermissions("sys:menu:edit")
    public Menu get(@RequestParam("id") Long id) {
        return menuService.findOne(id);
    }

    @RequestMapping(value = "save")
    @ResponseBody
    @RequiresPermissions(value={"sys:menu:edit","sys:menu:add"},logical= Logical.OR)
    public Result save(@ModelAttribute("menu") Menu menu) {
        Result result = new Result();
        try {
            if(menu==null|| Strings.isNullOrEmpty(menu.getName())){
                LOGGER.warn("save menu occur exception.menu[{}]", menu);
                result.setFailure("参数错误");
                return result;
            }
            menuService.save(menu);
            result.setSuccess();
        } catch (Exception ex) {
            LOGGER.error("save menu occur exception.menu[{}]", menu, ex);
            result.setFailure();
        }
        return result;
    }

    @RequestMapping(value = "del")
    @ResponseBody
    @RequiresPermissions("sys:menu:del")
    public Result del(@RequestParam("id") Long id) {
        Result result = new Result();
        try {
            menuService.del(id);
            result.setSuccess();
        } catch (Exception ex) {
            LOGGER.error("del menu occur exception.", ex);
            result.setFailure();
        }
        return result;
    }
}
