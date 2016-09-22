package net.kailyard.manager.web;

import net.kailyard.common.domain.Grid;
import net.kailyard.common.domain.Result;
import net.kailyard.common.exception.ApplicationRuntimeException;
import net.kailyard.common.utils.web.Servlets;
import net.kailyard.manager.system.entity.SysConfig;
import net.kailyard.manager.system.service.SysConfigService;
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
 * 系统参数配置controller
 */
@Controller
@RequestMapping(value = "/sysConfig")
public class SysConfigController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SysConfigController.class);

    @Autowired
    private SysConfigService sysConfigService;

    @RequestMapping(value = "index")
    public String index() {
        return "system/sysConfigList";
    }

    @RequestMapping(value = "list")
    @ResponseBody
    @RequiresPermissions("sys:config:list")
    public Grid<SysConfig> list(@RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "rows", defaultValue = "20") int size, HttpServletRequest request) {
        // 获取查询条件
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

        try {
            Page<SysConfig> list = sysConfigService.findAll(searchParams, page, size);
            return new Grid(list.getTotalElements(), list.getContent());
        } catch (Exception ex) {
            LOGGER.error("list error", ex);
            return new Grid();
        }
    }

    @RequestMapping(value = "get")
    @ResponseBody
    @RequiresPermissions("sys:config:edit")
    public SysConfig get(@RequestParam("id") Long id) {
        return sysConfigService.findOne(id);
    }

    @RequestMapping(value = "save")
    @ResponseBody
    @RequiresPermissions("sys:config:edit")
    public Result save(@ModelAttribute("sysConfig") SysConfig sysConfig) {
        Result result = new Result();
        try {
            if(sysConfig==null){
                LOGGER.warn("save sysConfig occur exception.sysConfig is null");
                result.setFailure("参数错误");
            }

            if(sysConfig.getId()!=null){
                sysConfigService.modify(sysConfig);
            }
            result.setSuccess();
        } catch (ApplicationRuntimeException ex) {
            LOGGER.error("save sysConfig occur exception. sysConfig[{}]", sysConfig, ex);
            result.setFailure(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.error("save sysConfig occur exception. sysConfig[{}]", sysConfig, ex);
            result.setFailure();
        }
        return result;
    }
}
