package cn.moonmc.ability.login.lister;

import cn.moonmc.ability.login.Login;
import cn.moonmc.ability.login.data.User;
import cn.moonmc.ability.login.event.LoginSuccessfulEvent;
import cn.moonmc.ability.login.utils.SMSCodeUtils;
import cn.moonmc.limboAdd.works.entity.Player;
import cn.moonmc.limboAdd.works.event.EventManager;
import cn.moonmc.limboAdd.works.event.Lister;
import cn.moonmc.limboAdd.works.event.playerEvent.PlayerJoinEvent;
import cn.moonmc.limboAdd.works.menu.*;
import cn.moonmc.limboAdd.works.message.JsonText;
import lombok.Getter;
import ru.nanit.limbo.server.Logger;

import java.util.Random;

public class PlayerJoin {
    final static Random random = new Random();
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
        anvilInventory.setIn2(new Item().setItemID(ItemType.paper).setItemNBTs(new ItemNBTs().setDisplayName(new JsonText("QAQ忘记密码了"))));
        anvilInventory.setOut(getOkTime());
        anvilInventory.setRenameItemLister(event -> anvilInventory.setOut(getOkTime()));
        anvilInventory.setCloseLister(event -> event.getPlayer().openInventory(anvilInventory));
        anvilInventory.setClickLister(event -> {
            if (event.getSlot()==1){
                resetPassword(player,user);
            }
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
     * 重置密码逻辑
     * */
    private void resetPassword(Player player,User user){
        boolean[] send = {false};
        AnvilInventory anvilInventory = new AnvilInventory(new JsonText("重置密码 | 请输入此账号绑定的手机号 "+user.getPhone().substring(0,3)+"***"+user.getPhone().substring(user.getPhone().length()-1)));
        anvilInventory.setIn1(getInItem());
        anvilInventory.setIn2(new Item().setItemID(ItemType.paper).setItemNBTs(new ItemNBTs().setDisplayName(new JsonText("想起来了awa"))));
        anvilInventory.setOut(getOkTime());
        anvilInventory.setRenameItemLister(event -> {if(!send[0])anvilInventory.setOut(getOkTime());});
        anvilInventory.setCloseLister(event -> event.getPlayer().openInventory(anvilInventory));
        anvilInventory.setClickLister(event -> {
            if (send[0]){
                return;
            }
            if (event.getSlot()==1){
                login(player,user);
            }
            if (event.getSlot()!=2) {
                return;
            }
            String phoneText = anvilInventory.getReSetName();
            if (phoneText==null||!phoneText.equals(user.getPhone())){
                anvilInventory.setOut(getErrorTime(new JsonText("手机号错误！请重新输入。")));
                return;
            }
            long phone;
            try {
                phone = Long.parseLong(phoneText);
            }catch (NumberFormatException e){
                anvilInventory.setOut(getErrorTime(new JsonText("手机号格式不正确！请重新输入。")));
                return;
            }
            String code = String.valueOf(random.nextInt(100000,999999));
            send[0] = true;
            anvilInventory.setOut(getErrorTime(new JsonText("正在发送验证码，请稍等..")));
            //发送验证码
            SMSCodeUtils.sendMessage(code,phone, SMSCodeUtils.Type.resetpwd);
            resetPassword1(player,user,code);
        });
        player.openInventory(anvilInventory);
    }

    private void resetPassword1(Player player,User user,String verificationCode){
        AnvilInventory anvilInventory = new AnvilInventory(new JsonText("重置密码 | 请输入您接收到的手机验证码。"));
        anvilInventory.setIn1(getInItem());
        anvilInventory.setOut(getOkTime());
        anvilInventory.setCloseLister(event -> event.getPlayer().openInventory(anvilInventory));
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
            resetPassword2(player,user);
        });
        player.openInventory(anvilInventory);
    }

    private void resetPassword2(Player player,User user){
        AnvilInventory anvilInventory = new AnvilInventory(new JsonText("重置密码 | 请设置密码。"));
        anvilInventory.setIn1(getInItem());
        anvilInventory.setOut(getOkTime());
        anvilInventory.setRenameItemLister(event -> anvilInventory.setOut(getOkTime()));
        anvilInventory.setCloseLister(event -> event.getPlayer().openInventory(anvilInventory));
        anvilInventory.setClickLister(event -> {
            if (event.getSlot()!=2) {
                return;
            }
            String passwordText = anvilInventory.getReSetName();
            if (passwordText==null||passwordText.length()<6){
                anvilInventory.setOut(getErrorTime(new JsonText("密码长度必须大于6位！请重新输入。")));
                return;
            }
            resetPassword3(player,user,passwordText);
        });
        player.openInventory(anvilInventory);
    }

    private void resetPassword3(Player player, User user, String passwordText) {
        AnvilInventory anvilInventory = new AnvilInventory(new JsonText("重置密码 | 请重新输入一次密码确保我们得到的是正确的密码。"));
        anvilInventory.setIn1(getInItem());
        anvilInventory.setIn2(new Item().setItemID(ItemType.paper).setItemNBTs(new ItemNBTs().setDisplayName(new JsonText("上一步"))));
        anvilInventory.setOut(getOkTime());
        anvilInventory.setRenameItemLister(event -> anvilInventory.setOut(getOkTime()));
        anvilInventory.setCloseLister(event -> event.getPlayer().openInventory(anvilInventory));
        anvilInventory.setClickLister(event -> {
            if (event.getSlot()==1){
                resetPassword2(player,user);
            }
            if (event.getSlot()!=2) {
                return;
            }
            String passwordText2 = anvilInventory.getReSetName();
            if (passwordText2==null||!passwordText2.equals(passwordText)){
                anvilInventory.setOut(getErrorTime(new JsonText("两次密码不一致！请重新输入。")));
                return;
            }
            //更新密码
            user.setPassword(User.Password.plaintextPassword(passwordText2));
            //更新name和ip地址
            user.setName(player.getName());
            user.setIp(player.getClientConnection().getAddress().toString());
            login.getUserManager().update(user);
            player.closeInventory();
            //通知登录成功事件
            EventManager.call(new LoginSuccessfulEvent(player));
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
        anvilInventory.setCloseLister(event -> event.getPlayer().openInventory(anvilInventory));
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
        anvilInventory.setIn2(new Item().setItemID(ItemType.paper).setItemNBTs(new ItemNBTs().setDisplayName(new JsonText("上一步"))));
        anvilInventory.setOut(getOkTime());
        anvilInventory.setRenameItemLister(event -> anvilInventory.setOut(getOkTime()));
        anvilInventory.setCloseLister(event -> event.getPlayer().openInventory(anvilInventory));
        anvilInventory.setClickLister(event -> {
            if (event.getSlot()==1){
                reg(player);
            }
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
        boolean[] send = {false};
        AnvilInventory anvilInventory = new AnvilInventory(new JsonText("创建账户 | 请输入手机号。"));
        anvilInventory.setIn1(getInItem());
        anvilInventory.setOut(getOkTime());
        anvilInventory.setRenameItemLister(event -> {if(!send[0])anvilInventory.setOut(getOkTime());});
        anvilInventory.setCloseLister(event -> event.getPlayer().openInventory(anvilInventory));
        anvilInventory.setClickLister(event -> {
            if (send[0]){
                return;
            }
            if (event.getSlot()!=2) {
                return;
            }
            String phoneText = anvilInventory.getReSetName();
            if (phoneText==null){
                anvilInventory.setOut(getErrorTime(new JsonText("手机号格式不正确！请重新输入。")));
                return;
            }
            long phone;
            try {
                phone = Long.parseLong(phoneText);
            }catch (NumberFormatException e){
                anvilInventory.setOut(getErrorTime(new JsonText("手机号格式不正确！请重新输入。")));
                return;
            }
            String code = String.valueOf(random.nextInt(100000,999999));
            send[0] = true;
            anvilInventory.setOut(getErrorTime(new JsonText("正在发送验证码，请稍等..")));
            //发送验证码
            SMSCodeUtils.sendMessage(code,phone, SMSCodeUtils.Type.bind);
            regPhone1(player,user,phoneText,code);
        });
        player.openInventory(anvilInventory);
    }

    private void regPhone1(Player player, User user,String phone,String verificationCode){
        AnvilInventory anvilInventory = new AnvilInventory(new JsonText("创建账户 | 请输入您接收到的手机验证码。"));
        anvilInventory.setIn1(getInItem());
        anvilInventory.setOut(getOkTime());
        anvilInventory.setCloseLister(event -> event.getPlayer().openInventory(anvilInventory));
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
