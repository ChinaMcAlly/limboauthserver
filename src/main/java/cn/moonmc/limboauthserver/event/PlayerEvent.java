package cn.moonmc.limboauthserver.event;

import cn.moonmc.limbo.eventWork.EventManager;
import cn.moonmc.limbo.eventWork.Lister;
import cn.moonmc.limbo.eventWork.event.player.PlayerJoinEvent;
import cn.moonmc.limbo.eventWork.event.player.PlayerLoginEvent;
import cn.moonmc.limbo.eventWork.event.player.PlayerRenameItem;
import cn.moonmc.limbo.packets.out.PacketOpenWindow;
import cn.moonmc.limbo.packets.out.PacketSetContainerProperty;
import cn.moonmc.limbo.packets.out.PacketSetContainerSlot;
import cn.moonmc.limboauthserver.entity.User;
import cn.moonmc.limboauthserver.mapper.UserMapper;
import com.grack.nanojson.JsonWriter;
import lombok.extern.slf4j.Slf4j;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ru.nanit.limbo.protocol.packets.login.PacketDisconnect;
import ru.nanit.limbo.protocol.packets.play.PacketChatMessage;
import ru.nanit.limbo.util.Colors;

import java.util.Map;

/**
 * @author CNLuminous
 */

@Component
@Slf4j
public class PlayerEvent implements ApplicationRunner {
    private UserMapper userMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        EventManager.regLister(new Lister<>(PlayerJoinEvent.class) {
            @Override
            public void listen(PlayerJoinEvent event) {
                //初始化用户数据
                User user = userMapper.selectUser(event.getPlayer().getUUID().toString());
                if (user==null){
                    user = new User(event.getPlayer().getUUID().toString(),event.getPlayer().getName(),null,null,"0");
                    userMapper.createUser(user);
                }
            }
        });
        EventManager.regLister(new Lister<>(PlayerLoginEvent.class){
            @Override
            public void listen(PlayerLoginEvent event) {
                System.out.println(111);
                //打开登录GUI
                PacketOpenWindow packetOpenWindow = new PacketOpenWindow();
                packetOpenWindow.setWindowsType(PacketOpenWindow.WindowsType.anvil);
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


                }
                event.getPlayer().getClientConnection().sendPacket(new PacketSetContainerProperty());

            }
        });

    }

    @Autowired
    public void autoWired(UserMapper userMapper){
        this.userMapper = userMapper;
    }
}
