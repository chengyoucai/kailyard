package net.kailyard.manager.system.utils;

import net.kailyard.manager.system.entity.SysUser;
import net.kailyard.utils.Codes;
import net.kailyard.utils.Digests;
import org.apache.shiro.codec.Hex;

/**
 */
public final class SysUserUtils {
    public static final String HASH_ALGORITHM = "SHA-1";
    public static final int HASH_TIMES = 1024;
    private static final int SALT_SIZE = 8;

    private SysUserUtils(){}

    /**
     * 生成随机的salt,并用sha-1进行hash
     */
    public static SysUser entryptPassword(SysUser user) {
        byte[] salt = Digests.generateSalt(SALT_SIZE);
        user.setSalt(Hex.encodeToString(salt));
        user.setPassword(Digests.sha1ToHex(user.getPlainPassword().getBytes(), salt, HASH_TIMES));

        return user;
    }

    /**
     * 校验密码
     * @param user
     * @param password
     * @return
     */
    public static boolean checkPassword(SysUser user, String password) {
        String target = Digests.sha1ToHex(password.getBytes(), Codes.decodeHex(user.getSalt()), HASH_TIMES);
        return target.equals(user.getPassword());
    }
}
