package com.tingfeng.shrio.realm;

import com.tingfeng.domain.SysUser;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.ArrayList;
import java.util.List;

public class ShiroDBRealmWithPwdEncrypt extends AuthorizingRealm {

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
        //SysUser sysUser = userService.queryUserByUsernameAndPassword(username);
        SysUser sysUser = new SysUser();
        sysUser.setUsername("jack");
        sysUser.setPassword("4d13c8017a891f1fef7e353a07723443");
        sysUser.setAuthSalt("abcd");

        // 3、判断用户是否存在，不存在返回null，存在返回AuthenticationInfo
        if (sysUser == null) {
            return null;
        }

        String dbPassword = sysUser.getPassword();
        String authSalt = sysUser.getAuthSalt();

        // 密文密码，迭代3次（校验
        String userPassword = new Md5Hash(password, authSalt, 3).toString();
        System.out.println(userPassword);
        if (!userPassword.equals(dbPassword)) {
            //return null;
            // 密码异常
            throw new IncorrectCredentialsException();
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

        // 1、从principals中获取用户
        SysUser sessionUser = (SysUser) principals.getPrimaryPrincipal();

        // 2、模拟数据库获取的权限（资源权限字符串）
        List<String> permissionList = new ArrayList<>();
        permissionList.add("user:add");
        permissionList.add("user:del");
        permissionList.add("user:mod");
        permissionList.add("user:queryall");

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addStringPermissions(permissionList);

        return simpleAuthorizationInfo;
    }

}
