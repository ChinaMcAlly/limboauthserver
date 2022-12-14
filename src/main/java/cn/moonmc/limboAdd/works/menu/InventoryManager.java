package cn.moonmc.limboAdd.works.menu;

import cn.moonmc.limboAdd.AddServer;
import cn.moonmc.limboAdd.works.entity.Player;
import cn.moonmc.limboAdd.works.event.EventManager;
import cn.moonmc.limboAdd.works.event.playerEvent.*;

import java.util.*;

/**
 * 一个库存管理器。主要负责通知点击事件和关闭事件
 * @author jja8
 * */
public class InventoryManager {
    AddServer addServer;
    public InventoryManager(AddServer addServer) {
        this.addServer = addServer;

        //注册监听器
        EventManager.regLister(PlayerQuitEvent.class, event -> {
            Control control = playerControlMap.remove(event.getPlayer());
            if (control!=null){
                control.openPlayers.remove(event.getPlayer());
                control.beClose(event.getPlayer());
            }
        });
        EventManager.regLister(PlayerCloseContainer.class, event -> {
            Control control = playerControlMap.get(event.getPlayer());
            if (control!=null && control.windowID==event.getWindowID()){
                playerControlMap.remove(event.getPlayer());
                control.openPlayers.remove(event.getPlayer());
                control.beClose(event.getPlayer());
            }
        });
        EventManager.regLister(PlayerClickContainer.class, event -> {
            Control control = playerControlMap.get(event.getPlayer());
            if (control!=null && control.windowID==event.getWindowID()){
                control.beClick(event);
            }
        });
        EventManager.regLister(PlayerClickButtonContainer.class,event -> {
            Control control = playerControlMap.get(event.getPlayer());
            if (control!=null && control.windowID==event.getWindowId()){
                control.beClickButton(event);
            }

        });

        //通知铁砧改变名字
        EventManager.regLister(PlayerRenameItem.class, event -> {
            Control control = playerControlMap.get(event.getPlayer());
            if (control instanceof AnvilInventory anvilInventory) {
                anvilInventory.renameItem(event);
            }
        });
    }


    abstract static class Control{
        private static int windowIdCount = 0;
        private static int nexWindowId(){
            return (windowIdCount++ % 126)+1;
        }

        protected int windowID = nexWindowId();

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
        /**
         * 点击按钮时通知
         */
        protected void beClickButton(PlayerClickButtonContainer event){};
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
    public void openInventory(Control control, Player player){
        Control nastControl = playerControlMap.remove(player);
        if (nastControl!=null){
            nastControl.openPlayers.remove(player);
            nastControl.beClose(player);
        }
        try {
            control.show(player);
        }catch (Throwable e){
            e.printStackTrace();
        }
        playerControlMap.put(player,control);
        control.openPlayers.add(player);
    }

    /**
     * 获取玩家正在查看的界面
     * */
    public Control lookingInventory(Player player){
        return playerControlMap.get(player);
    }

}
