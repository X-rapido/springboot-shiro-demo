package com.tingfeng;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.Assert;
import org.junit.Test;

public class PwdHashTest {

    @Test
    public void testPwdHash() {
        String password = "abc123";     // 明文密码
        String salt = "abcdefg";        // 盐值
        int hashIterations = 3;         // 迭代次数

        String passwordMD5 = new Md5Hash(password).toString();
        String passwordMD5Salt = new Md5Hash(password, salt).toString();
        String passwordMD5SaltCounts = new Md5Hash(password, salt, hashIterations).toString();

        System.out.printf("原密码, %s \n", password);
        System.out.printf("MD5加密后密码, %s \n", passwordMD5);
        System.out.printf("MD5加密加盐后密码, %s \n", passwordMD5Salt);
        System.out.printf("MD5加密加盐并且散列3次后密码, %s \n", passwordMD5SaltCounts);

        // 断言
        Assert.assertEquals("e99a18c428cb38d5f260853678922e03", passwordMD5);
        Assert.assertEquals("ef9010d01d2ffa6f182830735df712ea", passwordMD5Salt);
        Assert.assertEquals("f7a160579c1a491e69c39efdf82b1f1d", passwordMD5SaltCounts);
    }
}
