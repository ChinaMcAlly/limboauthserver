package cn.moonmc.limboauthserver.mapper;

import cn.moonmc.limboauthserver.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {
    /**
     * 创建玩家数据
     * @param user 用户类
     * @return 是否成功
     */
    @Insert("insert into `users` (`uuid`, `username`, `password`, `phone`, `status`) VALUES (#{uuid}, #{username}, #{password}, #{phone}, #{status})")
    boolean createUser(User user);

    /**
     * 从数据库中获取玩家数据
     * @param uuid 玩家UUID
     * @return 用户类
     */
    @Select("SELECT `uuid`,`username`,`password`,`phone`,`status` FROM `users` WHERE uuid=#{uuid}")
    User selectUser(String uuid);

    /**
     * 修改玩家数据,根据UUID进行修改
     * @param user 用户类
     * @return 是否成功
     */
    @Update("UPDATE `users` SET `username` = #{username},`password` = #{password},`phone`=#{phone},`status` = #{status} WHERE `users`.`uuid` = #{uuid}")
    boolean updateUser(User user);
}