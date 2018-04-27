package com.tingfeng;

import com.tingfeng.domain.SysUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Assert;
import org.junit.Test;

public class ShiroTest {

    @Test
    public void testIniRealm() {
        // 1、获取SecurityManager工厂，此处使用Ini配置文件初始化SecurityManager
        Factory<SecurityManager> factroy = new IniSecurityManagerFactory("classpath:shiro.ini");

        // 2、得到SecurityManager实例并绑定给SecurityUtils
        SecurityManager securityManager = factroy.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        // 3、得到Subject及创建用户名/密码身份验证Token（即用户身份/凭证）
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("tingfeng", "123456");
        try {
            // 4、登录，即身份验证
            subject.login(token);

            System.out.printf("用户/主体，登录状态：%s \n", subject.isAuthenticated());

        } catch (AuthenticationException e) {
            // 5、身份验证失败
            e.printStackTrace();
        }

        Assert.assertEquals(true, subject.isAuthenticated()); //断言用户已经登录

        // 6、退出
        subject.logout();

        System.out.printf("用户/主体，登录状态：%s", subject.isAuthenticated());
    }


    @Test
    public void testJdbcRealm() {
        // 1、获取SecurityManager工厂，此处使用Ini配置文件初始化SecurityManager
        Factory<SecurityManager> factroy = new IniSecurityManagerFactory("classpath:shiroDBRealm.ini");

        // 2、得到SecurityManager实例并绑定给SecurityUtils
        SecurityManager securityManager = factroy.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        // 3、创建用户登陆的token（由于在自定义的DBRealm中强制限制了用户名和密码，这了输入什么也无所谓了）
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("xx", "xx");
        try {
            // 4、登录，即身份验证
            subject.login(token);

            System.out.printf("用户/主体，登录状态：%s \n", subject.isAuthenticated());

        } catch (AuthenticationException e) {
            // 5、身份验证失败
            e.printStackTrace();
        }

        Assert.assertEquals(true, subject.isAuthenticated()); //断言用户已经登录

        // 6、退出
        subject.logout();

        System.out.printf("用户/主体，登录状态：%s", subject.isAuthenticated());
    }

}
