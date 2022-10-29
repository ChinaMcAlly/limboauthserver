package cn.moonmc.ability.login.data.password;

/**
 * 直接复制的authme的awa
 * The result of a hash computation. See {@link } for details.
 */
public class HashedPassword {

    /** The generated hash. */
    private final String hash;

    /**
     * Constructor.
     *
     * @param hash The computed hash
     */
    public HashedPassword(String hash) {
        this.hash = hash;
    }

    public String getHash() {
        return hash;
    }

}
