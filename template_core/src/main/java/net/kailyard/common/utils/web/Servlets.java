package net.kailyard.common.utils.web;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.net.HttpHeaders;
import eu.bitwalker.useragentutils.UserAgent;
import net.kailyard.common.utils.Constants;
import net.kailyard.common.utils.DateUtil;
import net.kailyard.common.utils.Exceptions;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

//import javax.servlet.ServletRequest;

/**
 * servlet工具类
 */
public final class Servlets {
    private Servlets() {
    }

    public static final String DATE_END_WITH = "_DATE";

    /**
     * 设置过期时间
     *
     * @param response
     * @param expiresSeconds
     */
    public static void setExpiresHeader(HttpServletResponse response, long expiresSeconds) {
        // Http 1.0 header, set a fix expires date.
        response.setDateHeader(HttpHeaders.EXPIRES, System.currentTimeMillis() + (expiresSeconds * 1000));
        // Http 1.1 header, set a time after now.
        response.setHeader(HttpHeaders.CACHE_CONTROL, "private, max-age=" + expiresSeconds);
    }

    /**
     * 禁止客户端缓存
     *
     * @param response
     */
    public static void setNoCacheHeader(HttpServletResponse response) {
        // Http 1.0 header
        response.setDateHeader(HttpHeaders.EXPIRES, 1L);
        response.addHeader(HttpHeaders.PRAGMA, "no-cache");
        // Http 1.1 header
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-age=0");
    }

    /**
     * 设置LastModified Header
     *
     * @param response
     * @param lastModifiedDate
     */
    public static void setLastModifiedHeader(HttpServletResponse response, long lastModifiedDate) {
        response.setDateHeader(HttpHeaders.LAST_MODIFIED, lastModifiedDate);
    }

    /**
     * 设置Etag Header
     *
     * @param response
     * @param etag
     */
    public static void setEtag(HttpServletResponse response, String etag) {
        response.setHeader(HttpHeaders.ETAG, etag);
    }

    /**
     * 判断文件是否修改,如果未修改, 返回false, 同时设置http status为304
     *
     * @param request
     * @param response
     * @param lastModified 内容的最后修改时间.
     * @return
     */
    public static boolean checkIfModifiedSince(HttpServletRequest request, HttpServletResponse response,
            long lastModified) {
        long ifModifiedSince = request.getDateHeader(HttpHeaders.IF_MODIFIED_SINCE);
        if ((ifModifiedSince >= 0) && (lastModified < (ifModifiedSince + 1000))) {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return false;
        }
        return true;
    }

    /**
     * 计算Etag是否已无效. 如果Etag有效, 返回false, 同时设置http status 304
     *
     * @param request
     * @param response
     * @param etag     内容的ETag.
     * @return
     */
    public static boolean checkIfNoneMatchEtag(HttpServletRequest request, HttpServletResponse response, String etag) {
        String headerValue = request.getHeader(HttpHeaders.IF_NONE_MATCH);
        if (null == headerValue) {
            boolean conditionSatisfied = false;
            if (!"*".equals(headerValue)) {
                StringTokenizer commaTokenizer = new StringTokenizer(headerValue, ",");

                while (!conditionSatisfied && commaTokenizer.hasMoreTokens()) {
                    String currentToken = commaTokenizer.nextToken();
                    if (currentToken.trim().equals(etag)) {
                        conditionSatisfied = true;
                    }
                }
            } else {
                conditionSatisfied = true;
            }

            if (conditionSatisfied) {
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                response.setHeader(HttpHeaders.ETAG, etag);
                return false;
            }
        }
        return true;
    }

    /**
     * 设置让浏览器弹出下载对话框的Header
     *
     * @param request
     * @param response
     * @param fileName 下载后的文件名.
     */
    public static void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response,
            String fileName) {
        // 中文文件名支持
        String encodedfileName = null;
        // 替换空格,否则firefox下有空格文件名会被截断,其他浏览器会将空格替换成+号
        encodedfileName = fileName.trim().replaceAll(" ", Constants.SEP_UNDERLINE);
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        String browser = null; // 获取客户端浏览器
        if (null != userAgent) {
            browser = userAgent.getBrowser().getName();
        }
        boolean isIE = (!Strings.isNullOrEmpty(browser) && browser.toUpperCase().indexOf("INTERNET EXPLORER") >= 0);
        if (isIE) {
            try {
                encodedfileName = URLEncoder.encode(fileName, Constants.CHARSET_UTF_8);
            } catch (UnsupportedEncodingException ex) {
                throw Exceptions.unchecked(ex);
            }
        } else {
            encodedfileName = new String(fileName.getBytes(), Charsets.ISO_8859_1);
        }

        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedfileName + "\"");

    }

    /**
     * 取得带相同前缀的Request Parameters, 参考spring WebUtils
     *
     * @param request
     * @param prefix
     * @return
     */
    public static Map<String, Object> getParametersStartingWith(HttpServletRequest request, String prefix) {
        Preconditions.checkNotNull(request, "request can't be null.");

        Enumeration<?> paramNames = request.getParameterNames();
        Map<String, Object> params = Maps.newHashMap();

        if (null == prefix) {
            prefix = "";
        }

        while ((null != paramNames) && paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            if ("".equals(prefix) || paramName.startsWith(prefix)) {
                String unprefixed = paramName.substring(prefix.length());
                String[] values = request.getParameterValues(paramName);
                if ((null == values) || (values.length == 0)) {
                    // Do nothing, no values found at all.
                    continue;
                } else if (values.length > 1) {
                    params.put(unprefixed, values);
                } else if (unprefixed.endsWith(DATE_END_WITH)) {
                    // 如果搜索为时间转换成date类型
                    unprefixed = unprefixed.replace(DATE_END_WITH, "");
                    params.put(unprefixed, DateUtil.parseDate(values[0]));
                } else {
                    params.put(unprefixed, values[0]);
                }
            }
        }
        return params;
    }

    /**
     * 组合Parameters生成Query String的Parameter部分, 并在paramter name上加上prefix.
     *
     * @param params
     * @param prefix
     * @return
     */
    public static String encodeParameterStringWithPrefix(Map<String, Object> params, String prefix) {
        if (CollectionUtils.isEmpty(params)) {
            return "";
        }
        if (null == prefix) {
            prefix = "";
        }

        StringBuilder queryStringBuilder = new StringBuilder();
        Iterator<Entry<String, Object>> it = params.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, Object> entry = it.next();
            queryStringBuilder.append(prefix).append(entry.getKey()).append('=').append(entry.getValue());
            if (it.hasNext()) {
                queryStringBuilder.append('&');
            }
        }
        return queryStringBuilder.toString();
    }
}
