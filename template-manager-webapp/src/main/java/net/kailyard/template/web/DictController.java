package net.kailyard.template.web;

import net.kailyard.template.common.domain.Grid;
import net.kailyard.template.common.domain.Result;
import net.kailyard.template.common.exception.ApplicationRuntimeException;
import net.kailyard.template.utils.Servlets;
import net.kailyard.template.system.entity.Dict;
import net.kailyard.template.system.service.DictService;
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
import java.util.Map;

/**
 * 数据字典controller
 */
@Controller
@RequestMapping(value = "/dict")
public class DictController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DictController.class);

    @Autowired
    private DictService dictService;

    @RequestMapping(value = "index")
    public String index() {
        return "system/dictList";
    }

    @RequestMapping(value = "list")
    @ResponseBody
    @RequiresPermissions("sys:dict:list")
    public Grid<Dict> list(@RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "rows", defaultValue = "20") int size, HttpServletRequest request) {
        // 获取查询条件
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

        try {
            Page<Dict> list = dictService.findAll(searchParams, page, size);
            return new Grid(list.getTotalElements(), list.getContent());
        } catch (Exception ex) {
            LOGGER.error("list error", ex);
            return new Grid();
        }
    }

    @RequestMapping(value = "get")
    @ResponseBody
    @RequiresPermissions("sys:dict:edit")
    public Dict get(@RequestParam("id") Long id) {
        return dictService.findOne(id);
    }

    @RequestMapping(value = "save")
    @ResponseBody
    @RequiresPermissions(value={"sys:dict:edit","sys:dict:add"},logical= Logical.OR)
    public Result save(@ModelAttribute("dict") Dict dict) {
        Result result = new Result();
        try {
            dictService.save(dict);
            result.setSuccess();
        } catch (ApplicationRuntimeException ex) {
            LOGGER.error("save dict occur exception. dict[{}]", dict, ex);
            result.setFailure(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.error("save dict occur exception. dict[{}]", dict, ex);
            result.setFailure();
        }
        return result;
    }

    @RequestMapping(value = "del")
    @ResponseBody
    @RequiresPermissions("sys:dict:del")
    public Result del(@RequestParam("id") Long id) {
        Result result = new Result();
        try {
            dictService.delete(id);
            result.setSuccess();
        } catch (Exception ex) {
            LOGGER.error("del dict occur exception.", ex);
            result.setFailure();
        }
        return result;
    }
}
