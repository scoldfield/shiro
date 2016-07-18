package com.cmcc.chapter5_1;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.Hex;
import org.junit.Test;

import junit.framework.Assert;

/*
 * Shiro提供了base64和16进制字符串编码/解码的API支持
 */
public class ShiroCodeDecode_1 {

    //base64进制编码、解码
    @Test
    public void base64Test() {
        String str = "hello";
        String base64Encoded = Base64.encodeToString(str.getBytes());
        
        String str2 = Base64.decodeToString(base64Encoded);
        Assert.assertEquals(str, str2);
    }
    
    //16进制编码、解码
    @Test
    public void hexTest() {
        String str = "hello";
        String hexEncoded = Hex.encodeToString(str.getBytes());
        
        String str2 = new String(Hex.decode(hexEncoded));
        Assert.assertEquals(str, str2);
    }
}
