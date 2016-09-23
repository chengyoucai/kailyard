package net.kailyard.template.web;

import org.apache.shiro.authz.AuthorizationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class ExceptionController implements ErrorController {
    private final static String ERROR_PATH = "/error";

    private ErrorAttributes errorAttributes;

    @Autowired
    private ServerProperties serverProperties;

    /**
     * 初始化ExceptionController
     * @param errorAttributes
     */
    @Autowired
    public ExceptionController(ErrorAttributes errorAttributes) {
        Assert.notNull(errorAttributes, "ErrorAttributes must not be null");
        this.errorAttributes = errorAttributes;
    }

    /**
     * 定义404的ModelAndView
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(produces = "text/html",value = "401")
    public ModelAndView errorHtml401(HttpServletRequest request,
            HttpServletResponse response) {
        return errorHtml(request, response);
    }

    /**
     * 定义404的JSON数据
     * @param request
     * @return
     */
    @RequestMapping(value = "401")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> error401(HttpServletRequest request) {
        return error(request);
    }

    /**
     * 定义404的ModelAndView
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(produces = "text/html",value = "404")
    public ModelAndView errorHtml404(HttpServletRequest request,
            HttpServletResponse response) {
        return errorHtml(request, response);
    }

    @RequestMapping(value = "404")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> error404(HttpServletRequest request) {
        return error(request);
    }

    @RequestMapping(produces = "text/html",value = "500")
    public ModelAndView errorHtml500(HttpServletRequest request,
            HttpServletResponse response) {
        return errorHtml(request, response);
    }

    @RequestMapping(value = "500")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> error500(HttpServletRequest request) {
        return error(request);
    }

    @RequestMapping(value = ERROR_PATH, produces = "text/html")
    public ModelAndView errorHtml(HttpServletRequest request,
            HttpServletResponse response) {
        ServletRequestAttributes requestAttributes = new ServletRequestAttributes(request);

        Throwable throwable = errorAttributes.getError(requestAttributes);
        if(throwable instanceof AuthorizationException){
            response.setStatus(HttpStatus.FORBIDDEN.value());
            Map<String, Object> model = new LinkedHashMap<String, Object>();
            model.put("timestamp", new Date());
            model.put("status", HttpStatus.FORBIDDEN.value());
            model.put("error", throwable.getMessage());
            return new ModelAndView("exception/error", model);
        } else {
            response.setStatus(getStatus(request).value());
            Map<String, Object> model = getErrorAttributes(requestAttributes,
                    isIncludeStackTrace(request, MediaType.TEXT_HTML));
            return new ModelAndView("exception/error", model);
        }
    }

    @RequestMapping(value = ERROR_PATH)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        ServletRequestAttributes requestAttributes = new ServletRequestAttributes(request);
        Throwable throwable = errorAttributes.getError(requestAttributes);
        if(throwable instanceof AuthorizationException){
            Map<String, Object> body = new LinkedHashMap<String, Object>();
            body.put("timestamp", new Date());
            body.put("status", HttpStatus.FORBIDDEN.value());
            body.put("error", throwable.getMessage());
            return new ResponseEntity<Map<String, Object>>(body, HttpStatus.FORBIDDEN);
        } else {
            Map<String, Object> body = getErrorAttributes(requestAttributes,
                    isIncludeStackTrace(request, MediaType.TEXT_HTML));
            HttpStatus status = getStatus(request);
            return new ResponseEntity<Map<String, Object>>(body, status);
        }
    }

    /**
     * Determine if the stacktrace attribute should be included.
     * @param request the source request
     * @param produces the media type produced (or {@code MediaType.ALL})
     * @return if the stacktrace attribute should be included
     */
    protected boolean isIncludeStackTrace(HttpServletRequest request,
            MediaType produces) {
        ErrorProperties.IncludeStacktrace include = this.serverProperties.getError().getIncludeStacktrace();
        if (include == ErrorProperties.IncludeStacktrace.ALWAYS) {
            return true;
        }
        if (include == ErrorProperties.IncludeStacktrace.ON_TRACE_PARAM) {
            return getTraceParameter(request);
        }
        return false;
    }

    private Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes,
            boolean includeStackTrace) {
        return this.errorAttributes.getErrorAttributes(requestAttributes,
                includeStackTrace);
    }

    private Throwable getError(RequestAttributes requestAttributes) {
        return this.errorAttributes.getError(requestAttributes);
    }

    /**
     * 是否包含trace
     * @param request
     * @return
     */
    private boolean getTraceParameter(HttpServletRequest request) {
        String parameter = request.getParameter("trace");
        if (parameter == null) {
            return false;
        }
        return !"false".equals(parameter.toLowerCase());
    }

    /**
     * 获取错误编码
     * @param request
     * @return
     */
    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request
                .getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        try {
            return HttpStatus.valueOf(statusCode);
        }
        catch (Exception ex) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    @Override
    public String getErrorPath() {
        return "";
    }
}
