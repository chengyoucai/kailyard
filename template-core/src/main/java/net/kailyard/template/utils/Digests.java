package net.kailyard.template.utils;

import com.google.common.base.Throwables;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;

/**
 *
 */
public class Digests {
    public static final String SHA1 = "SHA-1";
    public static final String MD5 = "MD5";

    /**
     * sha1 散列
     * @param in 待散列
     * @return 返回散列后的数组
     */
    public static byte[] sha1(byte[] in) {
        return digest(in, SHA1, null, 1);
    }

    /**
     * sha1 散列 返回hex
     * @param in 待散列
     * @return 返回散列后的数组的hex字符串
     */
    public static String sha1ToHex(byte[] in) {
        return Codes.encodeHex(sha1(in));
    }

    /**
     * sha1 散列,加入混淆参数,以及混淆次数
     * @param in  待散列值
     * @param salt  混淆参数
     * @param times  混淆次数
     * @return 返回散列后的byte数组
     */
    public static byte[] sha1(byte[] in, byte[] salt, int times) {
        return digest(in, SHA1, salt, times);
    }

    /**
     * sha1 散列,加入混淆参数,以及混淆次数
     * @param in  待散列值
     * @param salt  混淆参数
     * @param times  混淆次数
     * @return 返回散列后的byte数组的hex字符串
     */
    public static String sha1ToHex(byte[] in, byte[] salt, int times) {
        return Codes.encodeHex(sha1(in, salt, times));
    }

    /**
     * MD5散列
     * @param in
     * @return
     */
    public static byte[] md5(byte[] in) {
        return digest(in, MD5, null, 1);
    }

    /**
     *
     * @param in
     * @return
     */
    public static String md5ToHex(byte[] in) {
        return Codes.encodeHex(digest(in, MD5, null, 1));
    }

    /**
     * 对字符串进行散列, 支持md5与sha1算法.
     */
    private static byte[] digest(byte[] in, String algorithm, byte[] salt, int times) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);

            if (salt != null) {
                digest.update(salt);
            }

            byte[] result = digest.digest(in);

            for (int i = 1; i < times; i++) {
                digest.reset();
                result = digest.digest(result);
            }
            return result;
        } catch (GeneralSecurityException e) {
            throw Throwables.propagate(e);
        }
    }

    /**
     * 生成随机的Byte[]
     *
     * @param size byte[]大小
     * @return
     */
    public static byte[] generateSalt(int size) {
//        Validate.isTrue(numBytes > 0, "numBytes argument must be a positive integer (1 or larger)", numBytes);
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[size];
        random.nextBytes(bytes);
        return bytes;
    }
}
