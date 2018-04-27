package com.tingfeng;

import com.tingfeng.domain.SysUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class ShiroIniTest {

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

        // 4、登录，即身份验证
        subject.login(token);

        System.out.printf("用户/主体，登录状态：%s \n", subject.isAuthenticated());

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

        // 4、登录，即身份验证
        subject.login(token);

        System.out.printf("用户/主体，登录状态：%s \n", subject.isAuthenticated());

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

        System.out.println("用户是否含有 user:add 权限：" + subject.isPermitted("user:add"));
        System.out.println("用户是否含有 user:del 权限：" + subject.isPermitted("user:del"));
        System.out.println("用户是否含有 user:mod 权限：" + subject.isPermitted("user:mod"));
        System.out.println("用户是否含有 user:queryall 权限：" + subject.isPermitted("user:queryall"));

        // 断言用户已经登录
        Assert.assertEquals(true, subject.isAuthenticated());

        // 主体用户执行登出操作
        subject.logout();

        System.out.printf("用户/主体，登录状态：%s", subject.isAuthenticated());
    }


    @Test
    public void testUserIniAuthPermission() {
        // 使用工厂类加载ini配置文件，包含用户主体登陆及权限信息
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiroPermission.ini");

        // 创建安全管理者
        SecurityManager securityManager = factory.getInstance();

        // 设置当前环境
        SecurityUtils.setSecurityManager(securityManager);

        // 获取主体信息
        Subject subject = SecurityUtils.getSubject();

        // 创建用户登陆的token（由于在自定义的DBRealm中强制限制了用户名和密码，这了输入什么也无所谓了）
        UsernamePasswordToken token = new UsernamePasswordToken("lucy", "abc123");

        // 主体用户执行token的登陆认证
        subject.login(token);

        // isAuthenticated 判断是否登陆
        System.out.printf("\n用户/主体，登录状态：%s ", subject.isAuthenticated());

        // hasRole 验证角色
        System.out.println("\n用户是否含有 admin 角色：" + subject.hasRole("admin"));
        System.out.println("用户是否含有 admin,hr 角色：" + Arrays.toString(subject.hasRoles(Arrays.asList("admin", "coder", "hr"))));

        // isPermitted 验证权限
        System.out.println("\n用户是否含有 user:add 权限：" + subject.isPermitted("user:add"));
        System.out.println("用户是否含有 user:del 权限：" + subject.isPermitted("user:del"));
        System.out.println("用户是否含有 user:add,user:del 权限：" + Arrays.toString(subject.isPermitted("user:add", "user:del")));
        System.out.println("用户是否含有 user:mod 权限：" + subject.isPermitted("user:mod"));
        System.out.println("用户是否含有 user:queryall 权限：" + subject.isPermitted("user:queryall"));


        // checkRole，验证成功无返回，验证失败抛异常 UnauthorizedException
        subject.checkRole("coder");

        // 断言用户已经登录
        Assert.assertEquals(true, subject.isAuthenticated());

        // 主体用户执行登出操作
        subject.logout();


        System.out.printf("\n用户/主体，登录状态：%s", subject.isAuthenticated());
    }


}
