package cn.moonmc.limboauthserver.function.auth;

import cn.moonmc.limbo.Player;
import cn.moonmc.limbo.works.event.EventManager;
import cn.moonmc.limbo.works.event.Lister;
import cn.moonmc.limbo.works.event.playerEvent.PlayerJoinEvent;
import cn.moonmc.limbo.works.menu.AnvilInventory;
import cn.moonmc.limbo.works.menu.Item;
import cn.moonmc.limbo.works.menu.ItemNBTs;
import cn.moonmc.limbo.works.menu.ItemType;
import cn.moonmc.limbo.works.message.JsonText;
import cn.moonmc.limboauthserver.NanoLimbo;
import cn.moonmc.limboauthserver.entity.User;
import cn.moonmc.limboauthserver.utils.AuthService;
import com.grack.nanojson.JsonWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.nanit.limbo.connection.ClientConnection;
import ru.nanit.limbo.server.LimboServer;

import java.util.Map;

/**
 * @author CNLuminous 2022/10/15
 */
@Component
@Slf4j
@Order(1)
public class PlayerJoinFunction implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        EventManager.regLister(new Lister<>(PlayerJoinEvent.class){
            @Override
            public void listen(PlayerJoinEvent event) {
                Player player = event.getPlayer();
                User user = player.getUser();
                String tittle = "§6§l";
                if (user.getPhone()==null){
                    tittle += "请输入手机号";
                }else{
                    if (user.getPassword()==null && !NanoLimbo.config.isOnline()){
                        tittle += "请输入密码";
                    }else{
                        return;
                    }
                }
                tittle += " §r§3§l| 输入Q退出";
                log.info(user.toString());
                AnvilInventory anvilInventory = new AnvilInventory(new JsonText(tittle));
                Item item = new Item();
                item.setItemID(ItemType.paper);
                ItemNBTs itemNBTs = new ItemNBTs();
                itemNBTs.setDisplayName(new JsonText(">"));
                item.setItemNBTs(itemNBTs);
                anvilInventory.setIn1(item);
                player.openInventory(anvilInventory);


            }
        });
    }
}
