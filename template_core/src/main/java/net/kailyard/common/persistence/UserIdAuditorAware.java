package net.kailyard.common.persistence;

import net.kailyard.common.security.SecurityUtil;
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
