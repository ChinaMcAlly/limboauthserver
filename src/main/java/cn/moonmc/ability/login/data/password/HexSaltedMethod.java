package cn.moonmc.ability.login.data.password;

import cn.moonmc.ability.login.utils.RandomStringUtils;

/**
 * 直接复制的authme的awa
 * Common type for encryption methods which use a random String of hexadecimal characters
 * and store the salt with the hash itself.
 */
public abstract class HexSaltedMethod implements EncryptionMethod {

    public abstract int getSaltLength();

    @Override
    public abstract String computeHash(String password, String salt);

    @Override
    public HashedPassword computeHash(String password) {
        String salt = generateSalt();
        return new HashedPassword(computeHash(password, salt));
    }

    @Override
    public abstract boolean comparePassword(String password, HashedPassword hashedPassword);

    @Override
    public String generateSalt() {
        return RandomStringUtils.generateHex(getSaltLength());
    }

    @Override
    public boolean hasSeparateSalt() {
        return false;
    }
}
