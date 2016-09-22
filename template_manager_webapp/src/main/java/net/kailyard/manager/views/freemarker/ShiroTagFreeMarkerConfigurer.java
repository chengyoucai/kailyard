package net.kailyard.manager.views.freemarker;

import com.jagregory.shiro.freemarker.ShiroTags;
import freemarker.template.TemplateException;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;

/**
 * 集成shiro tag到freemarker里面
 */
public class ShiroTagFreeMarkerConfigurer extends FreeMarkerConfigurer {
    private final static String SHIRO_TAG = "shiro";

    @Override
    public void afterPropertiesSet() throws IOException, TemplateException {
        super.afterPropertiesSet();
        this.getConfiguration().setSharedVariable(SHIRO_TAG, new ShiroTags());
    }
}
