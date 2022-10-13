package cn.moonmc.limboauthserver.event;

import cn.jja8.limbo.eventWork.EventManager;
import cn.jja8.limbo.eventWork.Lister;
import cn.jja8.limbo.eventWork.event.player.PlayerJoinEvent;
import cn.moonmc.limboauthserver.entity.User;
import cn.moonmc.limboauthserver.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ru.nanit.limbo.server.Logger;

/**
 * @author CNLuminous
 */

@Component
public class PlayerJoin implements ApplicationRunner {
    @Autowired
    private UserMapper userMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Logger.info("PlayerJoinEvent registered!");
        EventManager.regLister(new Lister<>(PlayerJoinEvent.class) {
            @Override
            public void listen(PlayerJoinEvent event) {
                User user = userMapper.selectUser(event.getPlayer().getUUID().toString());
                if (user==null){
                    user = new User(event.getPlayer().getUUID().toString(),event.getPlayer().getName(),null,null,null);
                    userMapper.createUser(user);
                }
                Logger.info(user.toString());



            }
        });
    }
}
