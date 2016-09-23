package net.kailyard.template.common.persistence;

import org.springframework.data.auditing.DateTimeProvider;

import java.util.Calendar;

/**
 * 获得审计的当前时间
 */
public class AuditingDateTimeProvider implements DateTimeProvider {
    @Override
    public Calendar getNow() {
        return Calendar.getInstance();
    }
}
