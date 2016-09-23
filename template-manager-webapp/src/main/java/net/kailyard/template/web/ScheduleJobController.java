package net.kailyard.template.web;

import net.kailyard.template.common.domain.Grid;
import net.kailyard.template.common.domain.Result;
import net.kailyard.template.common.exception.ApplicationRuntimeException;
import net.kailyard.template.utils.Servlets;
import net.kailyard.template.schedule.entity.ScheduleJob;
import net.kailyard.template.schedule.service.ScheduleJobService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping(value = "/scheduleJob")
public class ScheduleJobController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleJobController.class);

    @Autowired
    private ScheduleJobService scheduleJobService;

    @RequestMapping(value = "index")
    public String index() {
        return "system/jobList";
    }

    @RequestMapping(value = "list")
    @ResponseBody
    @RequiresPermissions("sys:schedule:list")
    public Grid<ScheduleJob> list(@RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "rows", defaultValue = "20") int size, HttpServletRequest request) {
        // 获取查询条件
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

        try {
            Page<ScheduleJob> list = scheduleJobService.findAll(searchParams, page, size);
            return new Grid(list.getTotalElements(), list.getContent());
        } catch (Exception ex) {
            LOGGER.error("list error", ex);
            return new Grid();
        }
    }

    @RequestMapping(value = "get")
    @ResponseBody
    @RequiresPermissions("sys:schedule:list")
    public ScheduleJob get(@RequestParam("id") Long id) {
        return scheduleJobService.findOne(id);
    }

    @RequestMapping(value = "start")
    @ResponseBody
    @RequiresPermissions("sys:schedule:start")
    public Result start(@RequestParam("id") Long id) {
        Result result = new Result();
        try {
            scheduleJobService.scheduleJob(id);
            result.setSuccess();
        } catch (ApplicationRuntimeException ex) {
            LOGGER.error("start scheduleJob occur exception. scheduleJob[{}]", id, ex);
            result.setFailure(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.error("start scheduleJob occur exception. scheduleJob[{}]", id, ex);
            result.setFailure();
        }
        return result;
    }

    @RequestMapping(value = "stop")
    @ResponseBody
    @RequiresPermissions("sys:schedule:stop")
    public Result stop(@RequestParam("id") Long id) {
        Result result = new Result();
        try {
            scheduleJobService.unScheduleJob(id);
            result.setSuccess();
        } catch (ApplicationRuntimeException ex) {
            LOGGER.error("stop scheduleJob occur exception. scheduleJob[{}]", id, ex);
            result.setFailure(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.error("stop scheduleJob occur exception. scheduleJob[{}]", id, ex);
            result.setFailure();
        }
        return result;
    }

    @RequestMapping(value = "update")
    @ResponseBody
    @RequiresPermissions("sys:schedule:update")
    public Result update(@RequestParam("id") Long id, @RequestParam("jobCron") String jobCron) {
        Result result = new Result();
        try {
            scheduleJobService.updateCron(id, jobCron);
            result.setSuccess();
        } catch (ApplicationRuntimeException ex) {
            LOGGER.error("update scheduleJob occur exception. scheduleJob[{},{}]", id, jobCron, ex);
            result.setFailure(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.error("update scheduleJob occur exception. scheduleJob[{},{}]", id, jobCron, ex);
            result.setFailure();
        }
        return result;
    }

}
