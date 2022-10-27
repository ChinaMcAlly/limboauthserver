package cn.moonmc.limboAdd.works.menu;

import cn.moonmc.limboAdd.packets.out.PacketSetContainerSlot;
import cn.moonmc.limboAdd.works.entity.Player;
import cn.moonmc.limboAdd.works.event.Event;
import cn.moonmc.limboAdd.works.event.Lister;
import cn.moonmc.limboAdd.works.event.playerEvent.PlayerClickButtonContainer;
import cn.moonmc.limboAdd.works.event.playerEvent.PlayerClickContainer;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 *  代表一个需要打开展示的库存
 * @author jja8
 * */
public abstract class ShowInventory extends InventoryManager.Control implements Inventory{

    public static class BeClose implements Event {
        @Getter
        final Player player;
        public BeClose(Player player) {
            this.player = player;
        }
    }

    protected int windowID = new Random().nextInt(0,127);

    /**
     * 点击事件监听器,当玩家在此界面点击时传递事件
     * */
    @Getter
    @Setter
    Lister<PlayerClickContainer> clickLister;

    /**
     * 点击按钮监听器
     */
    @Getter
    @Setter
    Lister<PlayerClickButtonContainer> clickButtonLister;
    /**
     * 关闭事件监听器
     * */
    @Getter
    @Setter
    Lister<BeClose> closeLister;

    /**
     * 被点击时传递事件
     * */
    @Override
    protected void beClick(PlayerClickContainer event) {
        try {
            if (clickLister!=null){
                clickLister.listen(event);
            }
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }

    }

    @Override
    protected void beClickButton(PlayerClickButtonContainer event){
        try {
            if (clickButtonLister!=null){
                clickButtonLister.listen(event);
            }
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }

    }
    /**
     * 被关闭时传递事件
     * */
    @Override
    protected void beClose(Player player) {
        try {
            if (closeLister!=null){
                closeLister.listen(new BeClose(player));
            }
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }

    }

    /**
     * 发送物品更新
     * */
    protected void sendSlot(int slot, @NotNull Item item,@NotNull Player player){
        PacketSetContainerSlot packetSetContainerSlot = new PacketSetContainerSlot();
        packetSetContainerSlot.setWindowID(windowID);
        packetSetContainerSlot.setSlotID((short) slot);
        packetSetContainerSlot.setSlot(item.createSlot());
        player.getClientConnection().sendPacket(packetSetContainerSlot);
    }

}
