package cn.moonmc.ability.login.lister;

import cn.moonmc.ability.login.Login;
import cn.moonmc.ability.login.data.User;
import cn.moonmc.ability.login.event.LoginSuccessfulEvent;
import cn.moonmc.limboAdd.works.entity.Player;
import cn.moonmc.limboAdd.works.event.EventManager;
import cn.moonmc.limboAdd.works.event.playerEvent.PlayerJoinEvent;
import cn.moonmc.limboAdd.works.menu.AnvilInventory;
import cn.moonmc.limboAdd.works.menu.Item;
import cn.moonmc.limboAdd.works.menu.ItemNBTs;
import cn.moonmc.limboAdd.works.menu.ItemType;
import cn.moonmc.limboAdd.works.message.JsonText;
import lombok.Getter;

public class PlayerJoin {
    /**
     * 输入按钮
     * */
    @Getter
    final static Item inItem = new Item()
            .setItemID(ItemType.paper)
            .setItemNBTs(
                    new ItemNBTs()
                            .setDisplayName(new JsonText(">"))
            );
    /**
     * 确认按钮
     * */
    @Getter
    final static Item okTime = new Item()
            .setItemID(ItemType.paper)
            .setItemNBTs(
                    new ItemNBTs()
                            .setDisplayName(new JsonText("确定"))
            );

    /**
     * 错误提示按钮
     * */
    public Item getErrorTime(JsonText jsonText){
        return new Item()
                .setItemID(ItemType.stone)
                .setItemNBTs(
                  new ItemNBTs()
                          .setDisplayName(jsonText)
                );
    }


    final Login login;

    public PlayerJoin(Login login) {
        this.login = login;

        EventManager.regLister(PlayerJoinEvent.class, event -> {
            User user = login.getUserManager().selectOfUUID(event.getPlayer().getUUID());
            if (user==null){
                reg(event.getPlayer());
            }else {
                login(event.getPlayer(),user);
            }
        });
    }


    /**
     * 执行登录逻辑
     * */
    public void login(Player player,User user){
        AnvilInventory anvilInventory = new AnvilInventory(new JsonText("登录 | 请输入密码"));
        anvilInventory.setIn1(getInItem());
        anvilInventory.setOut(getOkTime());
        anvilInventory.setClickLister(event -> {
            if (event.getSlot()!=2) {
                return;
            }
            if (!user.getPassword().check(anvilInventory.getReSetName())) {
                anvilInventory.setOut(getErrorTime(new JsonText("密码错误！请重新输入。")));
                return;
            }
            //登录成功，关闭界面，通知事件
           // player.closeInventory();
            EventManager.call(new LoginSuccessfulEvent(player));
            //更新name和ip地址
            user.setName(player.getName());
            user.setIp(player.getClientConnection().getAddress().toString());
            login.getUserManager().update(user);
        });
        anvilInventory.setRenameItemLister(event -> {
            anvilInventory.setOut(getOkTime());
        });
        player.openInventory(anvilInventory);
    }

    /**
     * 执行注册逻辑
     * */
    public void reg(Player player){

        User user = new User(player.getUUID(),player.getName(),null,null,player.getClientConnection().getAddress().toString());


    }
}
