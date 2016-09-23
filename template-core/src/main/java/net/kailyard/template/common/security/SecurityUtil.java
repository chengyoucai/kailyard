package net.kailyard.template.common.security;

import org.apache.shiro.SecurityUtils;

public final class SecurityUtil {
	/**
	 * 取出Shiro中的当前用户.
	 */
	public static ShiroUser getCurrentUser() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user;
	}

	/**
	 * 取出Shiro中的当前用户id.
	 */
	public static Long getCurrentUserId() {
        ShiroUser user = getCurrentUser();
		if(user==null){
            return null;
        }
		return user.id;
	}

	/**
	 * 取出Shiro中的当前用户username.
	 */
	public static String getCurrentUsername() {
        ShiroUser user = getCurrentUser();
        if(user==null){
            return null;
        }

        return user.name;
	}

}
