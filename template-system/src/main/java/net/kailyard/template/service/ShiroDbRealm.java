package net.kailyard.template.service;

import net.kailyard.template.common.security.ShiroUser;
import net.kailyard.template.system.entity.SysUser;
import net.kailyard.template.system.service.SysUserService;
import net.kailyard.template.system.utils.SysUserUtils;
import net.kailyard.template.utils.Codes;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

public class ShiroDbRealm extends AuthorizingRealm {

    @Autowired
    protected SysUserService sysUserService;

    @Autowired
    private AuthorityService authorityService;

    /**
     * 认证回调函数,登录时调用.
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        SysUser user = sysUserService.findUserByLoginName(token.getUsername());
        if (user != null) {
            byte[] salt = Codes.decodeHex(user.getSalt());
            return new SimpleAuthenticationInfo(new ShiroUser(user.getId(), user.getLoginName(), user.getName()),
                    user.getPassword(),
                    ByteSource.Util.bytes(salt),
                    getName());
        } else {
            return null;
        }
    }


    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
        SysUser user = sysUserService.findUserByLoginName(shiroUser.getLoginName());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRoles(user.getRoleList());
        info.addStringPermissions(authorityService.findPermissionsByUserId(user.getId()));
        return info;
    }

    /**
     * 设定Password校验的Hash算法与迭代次数.
     */
    @PostConstruct
    public void initCredentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(SysUserUtils.HASH_ALGORITHM);
        matcher.setHashIterations(SysUserUtils.HASH_TIMES);
        setCredentialsMatcher(matcher);
    }

}
