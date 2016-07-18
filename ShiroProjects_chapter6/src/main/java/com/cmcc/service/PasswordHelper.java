package com.cmcc.service;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import com.cmcc.entity.User;

public class PasswordHelper {
    
    private RandomNumberGenerator randomNunberGenerator = new SecureRandomNumberGenerator();
    private String algorithmName = "md5";
    private final int hashIterator = 2;
    
    public void encryptPassword(User user) {
        user.setSalt(randomNunberGenerator.nextBytes().toHex());
        
        /*
         * SimpleHash(String algorithmName, Object source, Object salt, int hashIterations)方法的作用：
         * 返回一个使用指定algorithmName算法以及盐salt来对原数据source进行加密，且加密次数为hashIterations次。
         * 加密后的对象可以转换为16进制、64进制或者字符串进行存储
         */
        String newPassword = new SimpleHash(
                algorithmName,          //使用的是基本的md5加密算法
                user.getPassword(),     //待加密的为明密码
                ByteSource.Util.bytes(user.getCredentialsSalt()),       // user.getCredentialsSalt()是自定义的方法，将username + salt作为共同的加密盐
                hashIterator).toHex();  //加密后转换成16进制作为新的密码存储起来
        user.setPassword(newPassword);
    }
}
