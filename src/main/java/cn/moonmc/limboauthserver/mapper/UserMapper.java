package cn.moonmc.limboauthserver.mapper;

import cn.moonmc.limboauthserver.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    /**
     * 初始化玩家数据
     * @param uuid 玩家UUID
     * @param username 玩家用户名
     * @return 用户类
     */
    @Insert("insert into `users` (`uuid`, `username`, `password`, `phone`, `status`) VALUES (#{uuid}, #{username}, NULL, NULL, '0')")
    void createUser(User user);

    /**
     * 从数据库中获取玩家数据
     * @param uuid 玩家UUID
     * @return 用户类
     */
    @Select("SELECT * FROM `users` WHERE uuid=#{uuid}")
    User selectUser(String uuid);
}