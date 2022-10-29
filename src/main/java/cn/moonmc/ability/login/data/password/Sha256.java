package cn.moonmc.ability.login.data.password;

import static cn.moonmc.ability.login.utils.HashUtils.isEqual;
import static cn.moonmc.ability.login.utils.HashUtils.sha256;
/**
 * 直接复制的authme的awa
 * */
public class Sha256 extends HexSaltedMethod {

    @Override
    public String computeHash(String password, String salt) {
        return "$SHA$" + salt + "$" + sha256(sha256(password) + salt);
    }

    @Override
    public boolean comparePassword(String password, HashedPassword hashedPassword) {
        String hash = hashedPassword.getHash();
        String[] line = hash.split("\\$");
        return line.length == 4 && isEqual(hash, computeHash(password, line[2]));
    }

    @Override
    public int getSaltLength() {
        return 16;
    }

}
