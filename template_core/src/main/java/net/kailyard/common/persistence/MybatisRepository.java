package net.kailyard.common.persistence;

import org.springframework.stereotype.Repository;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repository
public @interface MybatisRepository {
    String value() default "";
}
