package cn.moonmc.limbo.works.menu;

import cn.moonmc.limbo.Player;
import cn.moonmc.limbo.packets.out.PacketSetContainerSlot;
import cn.moonmc.limbo.works.event.Event;
import cn.moonmc.limbo.works.event.Lister;
import cn.moonmc.limbo.works.event.playerEvent.PlayerClickContainer;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

/**
 * 代表一个需要打开展示的库存
 *
 * @author jja8
 */
public abstract class ShowInventory extends InventoryManager.Control implements Inventory {

    protected int windowID;
    /**
     * 点击事件监听器,当玩家在此界面点击时传递事件
     */
    @Getter
    @Setter
    Lister<PlayerClickContainer> clickLister;
    /**
     * 关闭事件监听器
     */
    @Getter
    @Setter
    Lister<BeClose> closeLister;

    /**
     * 被点击时传递事件
     */
    @Override
    protected void beClick(PlayerClickContainer event) {
        if (clickLister != null) {
            clickLister.listen(event);
        }
    }

    /**
     * 被关闭时传递事件
     */
    @Override
    protected void beClose(Player player) {
        if (closeLister != null) {
            closeLister.listen(new BeClose(player));
        }
    }

    /**
     * 发送物品更新
     */
    protected void sendSlot(int slot, @NotNull Item item, @NotNull Player player) {
        PacketSetContainerSlot packetSetContainerSlot = new PacketSetContainerSlot();
        packetSetContainerSlot.setWindowId(windowID);
        packetSetContainerSlot.setSlotId((short) slot);
        packetSetContainerSlot.setSlot(item.createSlot());
        player.getClientConnection().sendPacket(packetSetContainerSlot);
    }

    public static class BeClose implements Event {
        @Getter
        final Player player;

        public BeClose(Player player) {
            this.player = player;
        }
    }

}
