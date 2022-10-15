package cn.moonmc.limbo.works.menu;

import cn.moonmc.limbo.Player;
import cn.moonmc.limbo.works.event.EventManager;
import cn.moonmc.limbo.works.event.Lister;
import cn.moonmc.limbo.works.event.playerEvent.PlayerClickContainer;
import cn.moonmc.limbo.works.event.playerEvent.PlayerCloseContainer;
import cn.moonmc.limbo.works.event.playerEvent.PlayerQuitEvent;
import cn.moonmc.limbo.works.event.playerEvent.PlayerRenameItem;

import java.util.*;

/**
 * 一个库存管理器。主要负责通知点击事件和关闭事件
 * @author jja8
 * */
public class InventoryManager {
    public static void run(){
        //注册监听器
        EventManager.regLister(new Lister<>(PlayerQuitEvent.class) {
            @Override
            public void listen(PlayerQuitEvent event) {
                Control control = playerControlMap.remove(event.getPlayer());
                if (control!=null){
                    control.openPlayers.remove(event.getPlayer());
                    control.beClose(event.getPlayer());
                }
            }
        });
        EventManager.regLister(new Lister<>(PlayerCloseContainer.class) {
            @Override
            public void listen(PlayerCloseContainer event) {
                Control control = playerControlMap.remove(event.getPlayer());
                if (control!=null){
                    control.openPlayers.remove(event.getPlayer());
                    control.beClose(event.getPlayer());
                }
            }
        });
        EventManager.regLister(new Lister<>(PlayerClickContainer.class) {
            @Override
            public void listen(PlayerClickContainer event) {
                Control control = playerControlMap.get(event.getPlayer());
                if (control!=null){
                    control.beClick(event);
                }
            }
        });

        //通知铁砧改变名字
        EventManager.regLister(new Lister<>(PlayerRenameItem.class) {
            @Override
            public void listen(PlayerRenameItem event) {
                Control control = playerControlMap.get(event.getPlayer());
                if (control instanceof AnvilInventory anvilInventory) {
                    anvilInventory.renameItem(event);
                }
            }
        });
        //实现通知
    }

    abstract static class Control{
        private final List<Player> openPlayers = new ArrayList<>();
        //需要实现或者用来实现
        /**
         * 给玩家打开界面
         * */
        protected abstract void show(Player player);
        /**
         * 当窗口被关闭时
         * */
        protected void beClose(Player player){};
        /**
         * 被点击时通知
         * */
        protected void beClick(PlayerClickContainer event){};

        //用来调用
        /**
         * 获取所有正在查看此界面的玩家
         * */
        protected Iterator<Player> getOpenPlayers(){
            return openPlayers.iterator();
        }
    }


    static Map<Player,Control> playerControlMap = new HashMap<>();

    /**
     * 当窗口被打开时传递进来注册打开状态,才能监听点击事件和关闭事件
     * */
    public static void openInventory(Control control, Player player){
        Control nastControl = playerControlMap.remove(player);
        if (nastControl!=null){
            nastControl.openPlayers.remove(player);
            nastControl.beClose(player);
        }
        playerControlMap.put(player,control);
        try {
            control.show(player);
        }catch (Throwable e){
            e.printStackTrace();
        }
        control.openPlayers.add(player);
    }

    /**
     * 获取玩家正在查看的界面
     * */
    public static Control lookingInventory(Player player){
        return playerControlMap.get(player);
    }

}
