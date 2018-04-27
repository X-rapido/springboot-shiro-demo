package com.tingfeng.shrio.realm;

import com.tingfeng.domain.SysUser;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class ShroDBRealm extends AuthorizingRealm{


    /**
     * 用户验证登陆认证
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        // 1、从token中获取用户在表单域中输入的用户名和密码
        UsernamePasswordToken userToken = (UsernamePasswordToken) token;
        String username = userToken.getUsername();
        String password = String.valueOf(userToken.getPassword());

        // 2、根据用户名和密码进行数据库匹配。这里测试使用了硬编码
        //SysUser sysUser = userService.queryUserByUsernameAndPassword(username, password);
        SysUser sysUser = new SysUser();
        sysUser.setUsername("tingfeng");
        sysUser.setPassword("123456");

        // 3、判断用户是否存在，不存在返回null，存在返回AuthenticationInfo
        if (sysUser == null) {
            return null;
        }

        // 4、返回AuthenticationInfo
        // 参数意义：
        //    @param principal   the 'primary' principal associated with the specified realm. 用户对象，使用一个对象类或字符串，存在session中
        //    @param credentials the credentials that verify the given principal.   用户密码、
        //              我们目前是根据用户名和密码查询，但是也可以根据用户名查询密码，然后在进行密码密码的比对
        //    @param realmName   the realm from where the principal and credentials were acquired. 自定义realm的名称
        //
        AuthenticationInfo info = new SimpleAuthenticationInfo(sysUser, password.toCharArray(), getName());

        return info;
    }

    /**
     * 用于授权鉴权
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }
}
