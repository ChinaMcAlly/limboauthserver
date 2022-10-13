package cn.moonmc.limboauthserver.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    /**
     * 初始化玩家数据
     * @param uuid 玩家UUID
     * @param username 玩家用户名
     * @return 是否创建成功
     */
    @Insert("insert into `users` (`uuid`, `username`, `password`, `phone`, `status`) VALUES (#{uuid}, #{username}, NULL, NULL, '0')")
    boolean createUser(String uuid,String username);

}