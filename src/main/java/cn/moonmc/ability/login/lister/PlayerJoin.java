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
import ru.nanit.limbo.server.Logger;

public class PlayerJoin {
    /**
     * 输入按钮
     * */
    @Getter
    final static Item inItem = new Item()
            .setItemID(ItemType.paper)
            .setItemNBTs(
                    new ItemNBTs()
                            .setDisplayName(new JsonText(""))
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
            Logger.info(event.getPlayer().getUUID());
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
        anvilInventory.setRenameItemLister(event -> anvilInventory.setOut(getOkTime()));
        anvilInventory.setClickLister(event -> {
            if (event.getSlot()!=2) {
                return;
            }
            if (anvilInventory.getReSetName()==null||!user.getPassword().check(anvilInventory.getReSetName())) {
                anvilInventory.setOut(getErrorTime(new JsonText("密码错误！请重新输入。")));
                return;
            }
            //登录成功，关闭界面，通知事件
            player.closeInventory();
            EventManager.call(new LoginSuccessfulEvent(player));
            //更新name和ip地址
            user.setName(player.getName());
            user.setIp(player.getClientConnection().getAddress().toString());
            login.getUserManager().update(user);
        });
        player.openInventory(anvilInventory);
    }

    /**
     * 执行注册逻辑
     * */
    public void reg(Player player){
        User user = new User(player.getUUID(),player.getName(),null,null,player.getClientConnection().getAddress().toString());
        AnvilInventory anvilInventory = new AnvilInventory(new JsonText("创建账户 | 请设置密码。"));
        anvilInventory.setIn1(getInItem());
        anvilInventory.setOut(getOkTime());
        anvilInventory.setRenameItemLister(event -> anvilInventory.setOut(getOkTime()));
        anvilInventory.setClickLister(event -> {
            if (event.getSlot()!=2) {
                return;
            }
            String passwordText = anvilInventory.getReSetName();
            if (passwordText==null||passwordText.length()<6){
                anvilInventory.setOut(getErrorTime(new JsonText("密码长度必须大于6位！请重新输入。")));
                return;
            }
            regPassword(player,user,passwordText);
        });
        player.openInventory(anvilInventory);
    }
    private void regPassword(Player player,User user,String passwordText1){
        AnvilInventory anvilInventory = new AnvilInventory(new JsonText("创建账户 | 请重新输入一次密码确保我们得到的是正确的密码。"));
        anvilInventory.setIn1(getInItem());
        anvilInventory.setOut(getOkTime());
        anvilInventory.setRenameItemLister(event -> anvilInventory.setOut(getOkTime()));
        anvilInventory.setClickLister(event -> {
            if (event.getSlot()!=2) {
                return;
            }
            String passwordText2 = anvilInventory.getReSetName();
            if (passwordText2==null||!passwordText2.equals(passwordText1)){
                anvilInventory.setOut(getErrorTime(new JsonText("两次密码不一致！请重新输入。")));
                return;
            }
            user.setPassword(User.Password.plaintextPassword(passwordText2));
            regPhone(player,user);
        });
        player.openInventory(anvilInventory);
    }

    private void regPhone(Player player, User user) {
        AnvilInventory anvilInventory = new AnvilInventory(new JsonText("创建账户 | 请输入手机号。"));
        anvilInventory.setIn1(getInItem());
        anvilInventory.setOut(getOkTime());
        anvilInventory.setRenameItemLister(event -> anvilInventory.setOut(getOkTime()));
        anvilInventory.setClickLister(event -> {
            if (event.getSlot()!=2) {
                return;
            }
            String phone = anvilInventory.getReSetName();
            if (phone==null){
                anvilInventory.setOut(getErrorTime(new JsonText("手机号格式不正确！请重新输入。")));
                return;
            }
            // 这里应该发送验证码，但是发不了所以先搞个固定值吧
            regPhone1(player,user,phone,"123456");
        });
        player.openInventory(anvilInventory);
    }

    private void regPhone1(Player player, User user,String phone,String verificationCode){
        AnvilInventory anvilInventory = new AnvilInventory(new JsonText("创建账户 | 请输入您接收到的手机验证码。"));
        anvilInventory.setIn1(getInItem());
        anvilInventory.setOut(getOkTime());
        anvilInventory.setRenameItemLister(event -> anvilInventory.setOut(getOkTime()));
        anvilInventory.setClickLister(event -> {
            if (event.getSlot()!=2) {
                return;
            }
            String yzm = anvilInventory.getReSetName();
            if (yzm==null||!yzm.equals(verificationCode)){
                anvilInventory.setOut(getErrorTime(new JsonText("验证码错误！请重新输入。")));
                return;
            }
            user.setPhone(phone);
            login.getUserManager().insert(user);
            //注册成功，关闭界面，通知事件
            player.closeInventory();
            EventManager.call(new LoginSuccessfulEvent(player));
        });
        player.openInventory(anvilInventory);
    }
}
