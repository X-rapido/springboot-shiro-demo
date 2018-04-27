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


    @Test
    public void testJdbcRealmWithPwdEncrypt() {
        // 使用工厂类加载ini配置文件，包含用户主体登陆及权限信息
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiroDBRealmWithPwdEncrypt.ini");

        // 创建安全管理者
        SecurityManager securityManager = factory.getInstance();

        // 设置当前环境
        SecurityUtils.setSecurityManager(securityManager);

        // 获取主体信息
        Subject subject = SecurityUtils.getSubject();

        // 创建用户登陆的token（由于在自定义的DBRealm中强制限制了用户名和密码，这了输入什么也无所谓了）
        UsernamePasswordToken token = new UsernamePasswordToken("xx", "xx");

        // 主体用户执行token的登陆认证
        subject.login(token);

        // 判断是否登陆
        System.out.printf("用户/主体，登录状态：%s", subject.isAuthenticated());

        SysUser sessionUser = (SysUser) subject.getPrincipal();
        System.out.println("已登陆用户：" + sessionUser.getUsername() + "\t，密码：" + sessionUser.getPassword());

        System.out.println("用户是否含有 user:add 权限："+subject.isPermitted("user:add"));
        System.out.println("用户是否含有 user:del 权限："+subject.isPermitted("user:del"));
        System.out.println("用户是否含有 user:mod 权限："+subject.isPermitted("user:mod"));
        System.out.println("用户是否含有 user:queryall 权限："+subject.isPermitted("user:queryall"));

        // 断言用户已经登录
        Assert.assertEquals(true, subject.isAuthenticated());

        // 主体用户执行登出操作
        subject.logout();

        System.out.printf("用户/主体，登录状态：%s", subject.isAuthenticated());
    }
}
