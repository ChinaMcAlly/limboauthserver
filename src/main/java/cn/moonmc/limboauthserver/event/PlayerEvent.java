package cn.moonmc.limboauthserver.event;

import cn.moonmc.limbo.packets.out.PacketOpenMenu;
import cn.moonmc.limbo.packets.out.PacketSetContainerProperty;
import cn.moonmc.limbo.packets.out.PacketSetContainerSlot;
import cn.moonmc.limbo.works.event.EventManager;
import cn.moonmc.limbo.works.event.Lister;
import cn.moonmc.limbo.works.event.playerEvent.PlayerConnectEvent;
import cn.moonmc.limbo.works.event.playerEvent.PlayerJoinEvent;
import cn.moonmc.limbo.works.event.playerEvent.PlayerRenameItem;
import cn.moonmc.limboauthserver.AuthService;
import cn.moonmc.limboauthserver.entity.User;
import com.grack.nanojson.JsonWriter;
import lombok.extern.slf4j.Slf4j;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author CNLuminous
 */

@Component
@Slf4j
public class PlayerEvent implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        EventManager.regLister(new Lister<>(PlayerConnectEvent.class) {
            @Override
            public void listen(PlayerConnectEvent event) {
                //初始化用户数据
                User user = AuthService.selectUser(event.getPlayer().getUUID().toString());
                if (user==null){
                    user = new User(event.getPlayer().getUUID().toString(),event.getPlayer().getName(),null,null,"0");
                    AuthService.register(user);
                }
            }
        });
        EventManager.regLister(new Lister<>(PlayerJoinEvent.class){
            @Override
            public void listen(PlayerJoinEvent event) {
                //打开登录GUI
                PacketOpenMenu packetOpenWindow = new PacketOpenMenu();
                packetOpenWindow.setWindowsType(PacketOpenMenu.WindowsType.anvil);
                packetOpenWindow.setSlots(3);
                packetOpenWindow.setTitle("方块沙盒视角登录系统 | 输入Q退出");
                event.getPlayer().getClientConnection().sendPacket(packetOpenWindow);
                PacketSetContainerSlot pa = new PacketSetContainerSlot();
                pa.setSlotID((short) 0);
                pa.getSlot().setCount(1);
                pa.getSlot().setHasItem(true);
                pa.getSlot().setItemID(829);
                pa.getSlot().setNbt(CompoundBinaryTag
                        .builder()
                        .put(
                                "display",
                                CompoundBinaryTag
                                        .builder()
                                        .putString(
                                                "Name",
                                                JsonWriter.string(
                                                        Map.of("text",">")
                                                )).build()
                        ).build());
                event.getPlayer().getClientConnection().sendPacket(pa);
                log.info(pa.getSlot().getNbt().toString());
            }
        });


        EventManager.regLister(new Lister<>(PlayerRenameItem.class){
            @Override
            public void listen(PlayerRenameItem event) {
                //玩家退出登录操作
                if (">q".equals(event.getName()) || "q".equals(event.getName())) {
                    event.getPlayer().disconnect("用户退出登录操作");
                }

                event.getPlayer().getClientConnection().sendPacket(new PacketSetContainerProperty());

            }
        });

    }
}
