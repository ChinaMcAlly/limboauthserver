package cn.moonmc.ability.login.lister;

import cn.moonmc.ability.login.Login;
import cn.moonmc.ability.login.data.User;
import cn.moonmc.ability.login.data.password.EncryptionMethod;
import cn.moonmc.ability.login.data.password.Sha256;
import cn.moonmc.ability.login.event.LoginSuccessfulEvent;
import cn.moonmc.ability.login.utils.SMSCodeUtils;
import cn.moonmc.ability.notify.ServerEula;
import cn.moonmc.limboAdd.works.entity.Player;
import cn.moonmc.limboAdd.works.event.EventManager;
import cn.moonmc.limboAdd.works.event.playerEvent.PlayerCommandEvent;
import cn.moonmc.limboAdd.works.event.playerEvent.PlayerJoinEvent;
import cn.moonmc.limboAdd.works.menu.*;
import cn.moonmc.limboAdd.works.message.*;
import com.google.gson.Gson;
import lombok.Getter;
import ru.nanit.limbo.protocol.registry.Version;
import ru.nanit.limbo.server.LimboServer;
import ru.nanit.limbo.server.Logger;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerJoin {
    final static Random random = new Random();
    final static EncryptionMethod encryptionMethod = new Sha256();

    /**
     * 输入按钮
     * */
    public Item getInItem(Version version) {
        return new Item(version)
                .setItemID(ItemType.paper)
                .setItemNBTs(
                        new ItemNBTs(version)
                                .setDisplayName(new JsonTextParagraph(""))
                );

    }
    /**
     * 确认按钮
     * */
    public Item getOkTime(Version version) {
        return new Item(version)
                .setItemID(ItemType.paper)
                .setItemNBTs(
                        new ItemNBTs(version)
                                .setDisplayName(new JsonTextParagraph("确定"))
                );
    }

    /**
     * 错误提示按钮
     * */
    public Item getErrorTime(JsonText jsonText,Version version){
        return new Item(version)
                .setItemID(ItemType.stone)
                .setItemNBTs(
                  new ItemNBTs(version)
                          .setDisplayName(jsonText)
                );
    }


    final Login login;

    public PlayerJoin(Login login) {
        this.login = login;
        EventManager.regLister(PlayerJoinEvent.class, event -> {
            Logger.info(event.getPlayer().getUUID());

            String pattern = "[A-Za-z0-9_]{3,15}";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(event.getPlayer().getName());
            if (!m.matches()){
                event.getPlayer().disconnect(new JsonTextParagraph("§c用户名不合法!服务器仅支持3-15位英文/数字/下划线的ID注册"));
                return;
            }
                //添加附件
            event.getPlayer().getAttachments().set(LoginState.class,new LoginState());

            User user = login.getUserManager().selectOfUUID(event.getPlayer().getUUID());
            if (user==null){
                reg(event.getPlayer());//注册流程
            }else {
                login(event.getPlayer(),user);//登录流程
            }
        });

        EventManager.regLister(PlayerCommandEvent.class, event -> {
            //获取附件
            LoginState state = event.getPlayer().getAttachments().get(LoginState.class);
            if (state==null || state.logined){
                return;
            }
            if (event.getCommand().equals(state.getQuitCmd())){
                event.getPlayer().disconnect(new JsonTextParagraph("§6§l您已退出登录流程1"));
            }
            if (event.getCommand().equals(state.getRegCmd())){
                reg0(event.getPlayer());
            }
        });
    }

    public String getIP(Player player){
        String s = player.getClientConnection().getAddress().toString();
        return s.split(":")[0].replaceFirst("/","");
    }


    /**
     * 执行登录逻辑
     * */
    public void login(Player player,User user){
        Version version = player.getClientConnection().getClientVersion();
        //如果玩家ip相同，并且没有超过6小时，则免登录
        if (getIP(player).equals(user.getIp()) && user.getLastLogin()+1000*60*60*6>System.currentTimeMillis()){
            user.setName(player.getName());
            user.setLastLogin(System.currentTimeMillis());
            login.getUserManager().update(user);
            player.getAttachments().get(LoginState.class).logined = true;
            EventManager.call(new LoginSuccessfulEvent(player));
            return;
        }

        boolean[] send = {false};
        AnvilInventory anvilInventory = new AnvilInventory(new JsonTextParagraph("登录 | 请输入密码"));
        Item in1 = getInItem(version).copy();
        in1.getItemNBTs().setLore(List.of(
                new JsonTextParagraph("§f§l请输入"+player.getName()+"的密码。"),
                new JsonTextParagraph("§f若你没有注册过此ID，请在启动器更换ID"),
                new JsonTextParagraph("§fID仅支持包含字母与数字")
        ));
        anvilInventory.setIn1(in1);
        anvilInventory.setIn2(new Item(version).setItemID(ItemType.paper).setItemNBTs(new ItemNBTs(version).setDisplayName(new JsonTextParagraph("QAQ忘记密码了"))));
        anvilInventory.setOut(getOkTime(version));
        anvilInventory.setRenameItemLister(event -> anvilInventory.setOut(getOkTime(version)));
        anvilInventory.setCloseLister(event -> {
            if(!send[0]) {
                event.getPlayer().disconnect(new JsonTextParagraph("§6§l您已退出登录流程2"));
            }
        });
        anvilInventory.setClickLister(event -> {
            if (send[0]){
                return;
            }
            if (event.getSlot()==0){
                player.openInventory(anvilInventory);
            }
            if (event.getSlot()==1){
                send[0] = true;
                resetPassword(player,user);
            }
            if (event.getSlot()!=2) {
                return;
            }
            if (anvilInventory.getReSetName()==null||! encryptionMethod.comparePassword(anvilInventory.getReSetName(),user.getPassword())) {
                anvilInventory.setOut(getErrorTime(new JsonTextParagraph("密码错误！请重新输入。"),version));
                return;
            }
            //登录成功，关闭界面，通知事件
            send[0] = true;
            player.closeInventory();

            //设置玩家已登录
            player.getAttachments().get(LoginState.class).logined = true;
            //更新name和ip地址,上次登录时间
            user.setName(player.getName());
            user.setIp(getIP(player));
            user.setLastLogin(System.currentTimeMillis());
            login.getUserManager().update(user);

            EventManager.call(new LoginSuccessfulEvent(player));
        });
        player.openInventory(anvilInventory);
    }

    /**
     * 重置密码逻辑
     * */
    private void resetPassword(Player player,User user){
        Version version = player.getClientConnection().getClientVersion();
        boolean[] send = {false};
        AnvilInventory anvilInventory = new AnvilInventory(new JsonTextParagraph("重置密码 | 请输入此账号绑定的手机号 "+user.getPhone().substring(0,3)+"***"+user.getPhone().substring(user.getPhone().length()-1)));
        anvilInventory.setIn1(getInItem(version));
        anvilInventory.setIn2(new Item(version).setItemID(ItemType.paper).setItemNBTs(new ItemNBTs(version).setDisplayName(new JsonTextParagraph("想起来了awa"))));
        anvilInventory.setOut(getOkTime(version));
        anvilInventory.setRenameItemLister(event -> {if(!send[0]) {
            anvilInventory.setOut(getOkTime(version));
        }
        });
        anvilInventory.setCloseLister(event -> {if(!send[0]) {
            event.getPlayer().disconnect(new JsonTextParagraph("§6§l您已退出登录流程3"));
        }
        });
        anvilInventory.setClickLister(event -> {
            if (send[0]){
                return;
            }
            if (event.getSlot()==1){
                send[0] = true;
                login(player,user);
            }
            if (event.getSlot()!=2) {
                return;
            }
            String phoneText = anvilInventory.getReSetName();
            if (phoneText==null||!phoneText.equals(user.getPhone())){
                anvilInventory.setOut(getErrorTime(new JsonTextParagraph("手机号错误！请重新输入。"),version));
                return;
            }
            long phone;
            try {
                phone = Long.parseLong(phoneText);
            }catch (NumberFormatException e){
                anvilInventory.setOut(getErrorTime(new JsonTextParagraph("手机号格式不正确！请重新输入。"),version));
                return;
            }
            String code = String.valueOf(random.nextInt(100000,999999));
            send[0] = true;
            anvilInventory.setOut(getErrorTime(new JsonTextParagraph("正在发送验证码，请稍等.."),version));
            //发送验证码
            SMSCodeUtils.sendMessage(code,phone, SMSCodeUtils.Type.resetpwd);
            resetPassword1(player,user,code);
        });
        player.openInventory(anvilInventory);
    }

    private void resetPassword1(Player player,User user,String verificationCode){
        Version version = player.getClientConnection().getClientVersion();
        Logger.info("验证码:"+verificationCode);
        boolean[] send = {false};
        AnvilInventory anvilInventory = new AnvilInventory(new JsonTextParagraph("重置密码 | 请输入您接收到的手机验证码。"));
        anvilInventory.setIn1(getInItem(version));
        anvilInventory.setOut(getOkTime(version));
        anvilInventory.setRenameItemLister(event -> {if(!send[0]) {
            anvilInventory.setOut(getOkTime(version));
        }
        });
        anvilInventory.setCloseLister(event -> {if(!send[0]) {
            event.getPlayer().disconnect(new JsonTextParagraph("§6§l您已退出登录流程4"));
        }
        });
        anvilInventory.setClickLister(event -> {
            if (send[0]){
                return;
            }
            if (event.getSlot()!=2) {
                return;
            }
            String yzm = anvilInventory.getReSetName();
            if (yzm==null||!yzm.equals(verificationCode)){
                anvilInventory.setOut(getErrorTime(new JsonTextParagraph("验证码错误！请重新输入。"),version));
                return;
            }
            send[0] = true;
            resetPassword2(player,user);
        });
        player.openInventory(anvilInventory);
    }

    private void resetPassword2(Player player,User user){
        Version version = player.getClientConnection().getClientVersion();
        boolean[] send = {false};
        AnvilInventory anvilInventory = new AnvilInventory(new JsonTextParagraph("重置密码 | 请设置密码。"));
        anvilInventory.setIn1(getInItem(version));
        anvilInventory.setOut(getOkTime(version));
        anvilInventory.setRenameItemLister(event -> anvilInventory.setOut(getOkTime(version)));
        anvilInventory.setCloseLister(event -> {if(!send[0]) {
            event.getPlayer().disconnect(new JsonTextParagraph("§6§l您已退出登录流程5"));
        }
        });
        anvilInventory.setClickLister(event -> {
            if (send[0]){
                return;
            }
            if (event.getSlot()!=2) {
                return;
            }
            String passwordText = anvilInventory.getReSetName();
            if (passwordText==null||passwordText.length()<6){
                anvilInventory.setOut(getErrorTime(new JsonTextParagraph("密码长度必须大于6位！请重新输入。"),version));
                return;
            }
            send[0] = true;
            resetPassword3(player,user,passwordText);
        });
        player.openInventory(anvilInventory);
    }

    private void resetPassword3(Player player, User user, String passwordText) {
        Version version = player.getClientConnection().getClientVersion();
        boolean[] send = {false};
        AnvilInventory anvilInventory = new AnvilInventory(new JsonTextParagraph("重置密码 | 请重新输入一次密码确保我们得到的是正确的密码。"));
        anvilInventory.setIn1(getInItem(version));
        anvilInventory.setIn2(new Item(version).setItemID(ItemType.paper).setItemNBTs(new ItemNBTs(version).setDisplayName(new JsonTextParagraph("上一步"))));
        anvilInventory.setOut(getOkTime(version));
        anvilInventory.setRenameItemLister(event -> {if(!send[0]) {
            anvilInventory.setOut(getOkTime(version));
        }
        });
        anvilInventory.setCloseLister(event -> {if(!send[0]) {
            event.getPlayer().disconnect(new JsonTextParagraph("§6§l您已退出登录流程6"));
        }
        });
        anvilInventory.setClickLister(event -> {
            if (send[0]){
                return;
            }
            if (event.getSlot()==1){
                send[0] = true;
                resetPassword2(player,user);
            }
            if (event.getSlot()!=2) {
                return;
            }
            String passwordText2 = anvilInventory.getReSetName();
            if (passwordText2==null||!passwordText2.equals(passwordText)){
                anvilInventory.setOut(getErrorTime(new JsonTextParagraph("两次密码不一致！请重新输入。"),version));
                return;
            }
            //更新密码
            user.setPassword(encryptionMethod.computeHash(passwordText2));
            //更新name和ip地址
            user.setName(player.getName());
            user.setIp(getIP(player));
            //更新数据可能需要等待
            send[0] = true;
            anvilInventory.setOut(getErrorTime(new JsonTextParagraph("正在重置密码，请稍等.."),version));
            login.getUserManager().update(user);
            //关闭界面
            player.closeInventory();
            //通知登录成功事件
            player.getAttachments().get(LoginState.class).logined = true;
            EventManager.call(new LoginSuccessfulEvent(player));
        });
        player.openInventory(anvilInventory);
    }

    public void reg(Player player){
        Version version = player.getClientConnection().getClientVersion();
        LecternInventory lecternInventory = new LecternInventory(new Item(version)
                .setItemID(ItemType.written_book)
                .setCount(1)
                .setItemNBTs(
                        new BookItemNBTs(version)
                                .setBookTitle("服规")
                                .setBookAuthor("沙盒世界视角")
                                .setBookPages(ServerEula.buildEula(LimboServer.getInstance().getServerEula().getEula(),player))
                                )
                );
        lecternInventory.setCloseLister(event -> {
            player.openInventory(lecternInventory);
        });
        lecternInventory.setClickButtonLister(event -> {
            if (event.getButtonId()==3){
                event.getPlayer().disconnect(new JsonTextParagraph("§6§l您已退出登录流程"));
            }
        });
        player.openInventory(lecternInventory);


//        PacketSetContainerSlot = new PacketSetContainerSlot();
//        packetSetContainerSlot.setWindowID(0);
//        packetSetContainerSlot.setStateID(0);
//        packetSetContainerSlot.setSlotID((short) 36);
//        packetSetContainerSlot.setSlot(item.createSlot());
//        getClientConnection().sendPacket(packetSetContainerSlot);
//        setShortcutBarSlot(0);
//        clientConnection.sendPacket(new PacketOpenBook());
//        packetSetContainerSlot.setSlot(new Item().setItemID(ItemType.air).createSlot());
//        getClientConnection().sendPacket(packetSetContainerSlot);
        //玩家执行书本上的命令才表示同意协议，之后才执行 reg0() 方法开始注册。
    }
    /**
     * 执行注册逻辑
     * */
    public void reg0(Player player){
        Version version = player.getClientConnection().getClientVersion();
        boolean[] send = {false};
        User user = new User(player.getUUID(),player.getName(),null,null,getIP(player),System.currentTimeMillis());
        AnvilInventory anvilInventory = new AnvilInventory(new JsonTextParagraph("创建账户 | 请设置密码。"));
        Item in1 = getInItem(version).copy();
        in1.getItemNBTs().setLore(List.of(
                new JsonTextParagraph("§f§l为"+player.getName()+"创建密码。"),
                new JsonTextParagraph("§d§l请记住此ID "+player.getName()),
                new JsonTextParagraph("§f若不想使用此ID，请在启动器更换ID"),
                new JsonTextParagraph("§fID仅支持包含字母与数字")
        ));
        anvilInventory.setIn1(in1);
        anvilInventory.setOut(getOkTime(version));
        anvilInventory.setRenameItemLister(event -> anvilInventory.setOut(getOkTime(version)));
        anvilInventory.setCloseLister(event -> {
            if(!send[0]) {
            event.getPlayer().disconnect(new JsonTextParagraph("§6§l您已退出登录流程7"));
        }
        });
        anvilInventory.setClickLister(event -> {
            if (send[0]){
                return;
            }
            if (event.getSlot()!=2) {
                return;
            }
            String passwordText = anvilInventory.getReSetName();
            if (passwordText==null||passwordText.length()<6){
                anvilInventory.setOut(getErrorTime(new JsonTextParagraph("密码长度必须大于6位！请重新输入。"),version));
                return;
            }
            send[0] = true;
            reg1(player,user,passwordText);
        });
        player.openInventory(anvilInventory);
    }
    private void reg1(Player player, User user, String passwordText1){
        Version version = player.getClientConnection().getClientVersion();
        boolean[] send = {false};
        AnvilInventory anvilInventory = new AnvilInventory(new JsonTextParagraph("创建账户 | 请重新输入一次密码确保我们得到的是正确的密码。"));
        anvilInventory.setIn1(getInItem(version));
        anvilInventory.setIn2(new Item(version).setItemID(ItemType.paper).setItemNBTs(new ItemNBTs(version).setDisplayName(new JsonTextParagraph("上一步"))));
        anvilInventory.setOut(getOkTime(version));
        anvilInventory.setRenameItemLister(event -> anvilInventory.setOut(getOkTime(version)));
        anvilInventory.setCloseLister(event -> {if(!send[0]) {
            event.getPlayer().disconnect(new JsonTextParagraph("§6§l您已退出登录流程8"));
        }
        });
        anvilInventory.setClickLister(event -> {
            if (send[0]){
                return;
            }
            if (event.getSlot()==1){
                send[0] = true;
                reg0(player);
            }
            if (event.getSlot()!=2) {
                return;
            }
            String passwordText2 = anvilInventory.getReSetName();
            if (passwordText2==null||!passwordText2.equals(passwordText1)){
                anvilInventory.setOut(getErrorTime(new JsonTextParagraph("两次密码不一致！请重新输入。"),version));
                return;
            }
            user.setPassword(encryptionMethod.computeHash(passwordText2));
            send[0] = true;
            reg2(player,user);
        });
        player.openInventory(anvilInventory);
    }

    private void reg2(Player player, User user) {
        Version version = player.getClientConnection().getClientVersion();
        boolean[] send = {false};
        AnvilInventory anvilInventory = new AnvilInventory(new JsonTextParagraph("创建账户 | 请输入手机号。"));
        anvilInventory.setIn1(getInItem(version));
        anvilInventory.setOut(getOkTime(version));
        anvilInventory.setRenameItemLister(event -> {if(!send[0]) {
            anvilInventory.setOut(getOkTime(version));
        }
        });
        anvilInventory.setCloseLister(event -> {if(!send[0]) {
            event.getPlayer().disconnect(new JsonTextParagraph("§6§l您已退出登录流程9"));
        }
        });
        anvilInventory.setClickLister(event -> {
            if (send[0]){
                return;
            }
            if (event.getSlot()!=2) {
                return;
            }
            String phoneText = anvilInventory.getReSetName();
            if (phoneText==null){
                anvilInventory.setOut(getErrorTime(new JsonTextParagraph("手机号格式不正确！请重新输入。"),version));
                return;
            }
            long phone;
            try {
                phone = Long.parseLong(phoneText);
            }catch (NumberFormatException e){
                anvilInventory.setOut(getErrorTime(new JsonTextParagraph("手机号格式不正确！请重新输入。"),version));
                return;
            }
            //验证手机号是否已经注册
            //验证手机号可能需要等待
            send[0] = true;
            anvilInventory.setOut(getErrorTime(new JsonTextParagraph("正在验证手机号，请稍等.."),version));
            User phoneUser = login.getUserManager().selectOfPhone(phoneText);
            send[0] = false;
            if (phoneUser!=null){
                Item out = getErrorTime(new JsonTextParagraph("§d此手机号已经被注册！"),version);
                out.getItemNBTs().setLore(List.of(
                        new JsonTextParagraph("§f手机号已被"+phoneUser.getName()+"注册。"),
                        new JsonTextParagraph("§d§l如果是您本人注册的，请继续使用本账号。"),
                        new JsonTextParagraph("§f请关闭游戏"),
                        new JsonTextParagraph("§f在启动器使用"+phoneUser.getName()+"启动游戏"),
                        new JsonTextParagraph("§f再次进入服务器，输入密码登录，或找回密码。")
                ));
                anvilInventory.setOut(out);
                return;
            }
            String code = String.valueOf(random.nextInt(100000,999999));
            //发送验证码可能需要等待
            send[0] = true;
            anvilInventory.setOut(getErrorTime(new JsonTextParagraph("正在发送验证码，请稍等.."),version));
            //发送验证码
            SMSCodeUtils.sendMessage(code,phone, SMSCodeUtils.Type.bind);
            reg3(player,user,phoneText,code);
        });
        player.openInventory(anvilInventory);
    }

    private void reg3(Player player, User user, String phone, String verificationCode){
        Version version = player.getClientConnection().getClientVersion();
        Logger.info("验证码:"+verificationCode);
        boolean[] send = {false};
        AnvilInventory anvilInventory = new AnvilInventory(new JsonTextParagraph("创建账户 | 请输入您接收到的手机验证码。"));
        anvilInventory.setIn1(getInItem(version));
        anvilInventory.setOut(getOkTime(version));
        anvilInventory.setCloseLister(event -> {if(!send[0]) {
            event.getPlayer().disconnect(new JsonTextParagraph("§6§l您已退出登录流程10"));
        }
        });
        anvilInventory.setRenameItemLister(event -> {if(!send[0]) {
            anvilInventory.setOut(getOkTime(version));
        }
        });
        anvilInventory.setClickLister(event -> {
            if (send[0]){
                return;
            }
            if (event.getSlot()!=2) {
                return;
            }
            String yzm = anvilInventory.getReSetName();
            if (yzm==null||!yzm.equals(verificationCode)){
                anvilInventory.setOut(getErrorTime(new JsonTextParagraph("验证码错误！请重新输入。"),version));
                return;
            }
            user.setPhone(phone);
            //提交到数据库可能需要等待
            send[0] = true;
            anvilInventory.setOut(getErrorTime(new JsonTextParagraph("正在完成注册，请稍等.."),version));
            user.setLastLogin(System.currentTimeMillis());
            login.getUserManager().insert(user);
            //注册成功，关闭界面，通知事件
            player.closeInventory();
            player.getAttachments().get(LoginState.class).logined = true;
            EventManager.call(new LoginSuccessfulEvent(player));
        });
        player.openInventory(anvilInventory);
    }
}
