package net.kailyard.template.common.persistence;

import net.kailyard.template.common.security.SecurityUtil;
import org.springframework.data.domain.AuditorAware;

/**
 *
 */
public class UserIdAuditorAware implements AuditorAware<Long> {

    @Override
    public Long getCurrentAuditor() {
        return SecurityUtil.getCurrentUserId();
    }
}
