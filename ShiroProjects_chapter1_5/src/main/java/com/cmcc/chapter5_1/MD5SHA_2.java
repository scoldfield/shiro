package com.cmcc.chapter5_1;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.Sha1Hash;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.crypto.hash.Sha512Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.junit.Test;

/*
 * 散列算法：MD5和SHA
 */
public class MD5SHA_2 {

    //MD5对密码进行散列
    @Test
    public void MD5Test() {
        String str = "hello";
        String salt = "123";
        String md5 = new Md5Hash(str, salt).toString(); //还可以转换为toBase64()/toHex()
        
        //还可以指定散列次数
        String md5_2 = new Md5Hash(str, salt, 2).toString();    //散列2次
        
        System.out.println("hello + 123 散列1次后为：" + md5);
        System.out.println("hello + 123 散列2次后为：" + md5_2);
    }
    
    //SHA算法对密码进行散列
    @Test
    public void SHATest() {
        String source = "hello";
        String salt = "123";
        
        //SHA256算法散列
        String sha256_1 = new Sha256Hash(source, salt).toString();
        String sha256_2 = new Sha256Hash(source, salt, 2).toString();
        //SHA1算法散列
        String sha1_1 = new Sha1Hash(source, salt).toString();
        String sha1_2 = new Sha1Hash(source, salt, 2).toString();
        //SHA512算法散列
        String sha512_1 = new Sha512Hash(source, salt).toString();
        String sha512_2 = new Sha512Hash(source, salt, 2).toString();
        
        System.out.println("hello + 123 通过sha256算法散列1次后为：" + sha256_1);
        System.out.println("hello + 123 通过sha256算法散列2次后为：" + sha256_2);
        System.out.println("hello + 123 通过sha1算法散列1次后为：" + sha1_1);
        System.out.println("hello + 123 通过sha1算法散列2次后为：" + sha1_2);
        System.out.println("hello + 123 通过sha512算法散列1次后为：" + sha512_1);
        System.out.println("hello + 123 通过sha512算法散列2次后为：" + sha512_2);
    }
    
    //SimpleHash算法对密码进行散列
    @Test
    public void simpleHashTest() {
        String source = "hello";
        String salt = "123";
        
        //1次散列
        String sh1_1 = new SimpleHash("SHA-1", source, salt).toString();
        String sh1_2 = new SimpleHash("SHA-1", source, salt, 2).toString();
        
        System.out.println("hello + 123 通过SimpleHash通用算法散列1次后为：" + sh1_1);
        System.out.println("hello + 123 通过SimpleHash通用算法散列2次后为：" + sh1_2);
    }
}
