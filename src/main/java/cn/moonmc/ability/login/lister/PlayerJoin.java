package cn.moonmc.ability.login.lister;

import cn.moonmc.ability.login.Login;
import cn.moonmc.ability.login.data.User;
import cn.moonmc.ability.login.event.LoginSuccessfulEvent;
import cn.moonmc.ability.login.utils.SMSCodeUtils;
import cn.moonmc.limboAdd.works.entity.Player;
import cn.moonmc.limboAdd.works.event.EventManager;
import cn.moonmc.limboAdd.works.event.playerEvent.PlayerCommandEvent;
import cn.moonmc.limboAdd.works.event.playerEvent.PlayerJoinEvent;
import cn.moonmc.limboAdd.works.menu.*;
import cn.moonmc.limboAdd.works.message.*;
import lombok.Getter;
import ru.nanit.limbo.server.Logger;

import java.util.List;
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
                            .setDisplayName(new JsonTextParagraph(""))
            );
    /**
     * 确认按钮
     * */
    @Getter
    final static Item okTime = new Item()
            .setItemID(ItemType.paper)
            .setItemNBTs(
                    new ItemNBTs()
                            .setDisplayName(new JsonTextParagraph("确定"))
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
                reg(event.getPlayer());//注册流程
            }else {
                login(event.getPlayer(),user);//登录流程
            }
        });

        EventManager.regLister(PlayerCommandEvent.class, event -> {
            reg(event.getPlayer());
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
        boolean[] send = {false};
        AnvilInventory anvilInventory = new AnvilInventory(new JsonTextParagraph("登录 | 请输入密码"));
        anvilInventory.setIn1(getInItem());
        anvilInventory.setIn2(new Item().setItemID(ItemType.paper).setItemNBTs(new ItemNBTs().setDisplayName(new JsonTextParagraph("QAQ忘记密码了"))));
        anvilInventory.setOut(getOkTime());
        anvilInventory.setRenameItemLister(event -> anvilInventory.setOut(getOkTime()));
        anvilInventory.setCloseLister(event -> {if(!send[0])event.getPlayer().openInventory(anvilInventory);});
        anvilInventory.setClickLister(event -> {
            if (send[0]){
                return;
            }
            if (event.getSlot()==1){
                send[0] = true;
                resetPassword(player,user);
            }
            if (event.getSlot()!=2) {
                return;
            }
            if (anvilInventory.getReSetName()==null||!user.getPassword().check(anvilInventory.getReSetName())) {
                anvilInventory.setOut(getErrorTime(new JsonTextParagraph("密码错误！请重新输入。")));
                return;
            }
            //登录成功，关闭界面，通知事件
            send[0] = true;
            player.closeInventory();
            EventManager.call(new LoginSuccessfulEvent(player));

            if (!player.getName().equals(user.getName())||!getIP(player).equals(user.getIp())){
                //更新name和ip地址
                user.setName(player.getName());
                user.setIp(getIP(player));
                login.getUserManager().update(user);
            }
        });
        player.openInventory(anvilInventory);
    }

    /**
     * 重置密码逻辑
     * */
    private void resetPassword(Player player,User user){
        boolean[] send = {false};
        AnvilInventory anvilInventory = new AnvilInventory(new JsonTextParagraph("重置密码 | 请输入此账号绑定的手机号 "+user.getPhone().substring(0,3)+"***"+user.getPhone().substring(user.getPhone().length()-1)));
        anvilInventory.setIn1(getInItem());
        anvilInventory.setIn2(new Item().setItemID(ItemType.paper).setItemNBTs(new ItemNBTs().setDisplayName(new JsonTextParagraph("想起来了awa"))));
        anvilInventory.setOut(getOkTime());
        anvilInventory.setRenameItemLister(event -> {if(!send[0])anvilInventory.setOut(getOkTime());});
        anvilInventory.setCloseLister(event -> {if(!send[0])event.getPlayer().openInventory(anvilInventory);});
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
                anvilInventory.setOut(getErrorTime(new JsonTextParagraph("手机号错误！请重新输入。")));
                return;
            }
            long phone;
            try {
                phone = Long.parseLong(phoneText);
            }catch (NumberFormatException e){
                anvilInventory.setOut(getErrorTime(new JsonTextParagraph("手机号格式不正确！请重新输入。")));
                return;
            }
            String code = String.valueOf(random.nextInt(100000,999999));
            send[0] = true;
            anvilInventory.setOut(getErrorTime(new JsonTextParagraph("正在发送验证码，请稍等..")));
            //发送验证码
            SMSCodeUtils.sendMessage(code,phone, SMSCodeUtils.Type.resetpwd);
            resetPassword1(player,user,code);
        });
        player.openInventory(anvilInventory);
    }

    private void resetPassword1(Player player,User user,String verificationCode){
        boolean[] send = {false};
        AnvilInventory anvilInventory = new AnvilInventory(new JsonTextParagraph("重置密码 | 请输入您接收到的手机验证码。"));
        anvilInventory.setIn1(getInItem());
        anvilInventory.setOut(getOkTime());
        anvilInventory.setRenameItemLister(event -> {if(!send[0])anvilInventory.setOut(getOkTime());});
        anvilInventory.setCloseLister(event -> {if(!send[0])event.getPlayer().openInventory(anvilInventory);});
        anvilInventory.setClickLister(event -> {
            if (send[0]){
                return;
            }
            if (event.getSlot()!=2) {
                return;
            }
            String yzm = anvilInventory.getReSetName();
            if (yzm==null||!yzm.equals(verificationCode)){
                anvilInventory.setOut(getErrorTime(new JsonTextParagraph("验证码错误！请重新输入。")));
                return;
            }
            send[0] = true;
            resetPassword2(player,user);
        });
        player.openInventory(anvilInventory);
    }

    private void resetPassword2(Player player,User user){
        boolean[] send = {false};
        AnvilInventory anvilInventory = new AnvilInventory(new JsonTextParagraph("重置密码 | 请设置密码。"));
        anvilInventory.setIn1(getInItem());
        anvilInventory.setOut(getOkTime());
        anvilInventory.setRenameItemLister(event -> anvilInventory.setOut(getOkTime()));
        anvilInventory.setCloseLister(event -> {if(!send[0])event.getPlayer().openInventory(anvilInventory);});
        anvilInventory.setClickLister(event -> {
            if (send[0]){
                return;
            }
            if (event.getSlot()!=2) {
                return;
            }
            String passwordText = anvilInventory.getReSetName();
            if (passwordText==null||passwordText.length()<6){
                anvilInventory.setOut(getErrorTime(new JsonTextParagraph("密码长度必须大于6位！请重新输入。")));
                return;
            }
            send[0] = true;
            resetPassword3(player,user,passwordText);
        });
        player.openInventory(anvilInventory);
    }

    private void resetPassword3(Player player, User user, String passwordText) {
        boolean[] send = {false};
        AnvilInventory anvilInventory = new AnvilInventory(new JsonTextParagraph("重置密码 | 请重新输入一次密码确保我们得到的是正确的密码。"));
        anvilInventory.setIn1(getInItem());
        anvilInventory.setIn2(new Item().setItemID(ItemType.paper).setItemNBTs(new ItemNBTs().setDisplayName(new JsonTextParagraph("上一步"))));
        anvilInventory.setOut(getOkTime());
        anvilInventory.setRenameItemLister(event -> {if(!send[0])anvilInventory.setOut(getOkTime());});
        anvilInventory.setCloseLister(event -> {if(!send[0])event.getPlayer().openInventory(anvilInventory);});
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
                anvilInventory.setOut(getErrorTime(new JsonTextParagraph("两次密码不一致！请重新输入。")));
                return;
            }
            //更新密码
            user.setPassword(User.Password.plaintextPassword(passwordText2));
            //更新name和ip地址
            user.setName(player.getName());
            user.setIp(getIP(player));
            //更新数据可能需要等待
            send[0] = true;
            anvilInventory.setOut(getErrorTime(new JsonTextParagraph("正在重置密码，请稍等..")));
            login.getUserManager().update(user);
            //关闭界面
            player.closeInventory();
            //通知登录成功事件
            EventManager.call(new LoginSuccessfulEvent(player));
        });
        player.openInventory(anvilInventory);
    }

    public void reg(Player player){
        player.openBook(new Item()
                .setItemID(ItemType.written_book)
                .setCount(1)
                .setItemNBTs(
                        new BookItemNBTs()
                                .setBookTitle("服规")
                                .setBookAuthor("沙盒世界视角")
                                .setBookPages(
                                        List.of(
                                                new JsonTextParagraph("第一页,第一页不好玩，快去第二页"),
                                                new JsonTextArticle(new JsonTextParagraph("第二页\n"))
                                                        .addParagraph(new JsonTextParagraph("这是下一段了哦\n"))
                                                        .addParagraph(
                                                                new JsonTextParagraph("点击我回到第一页\n")
                                                                        .setClickEvent(new ClickEventChangePage(1))
                                                                        .setHoverEvent(new HoverEventShowText("等什么啊，点我啊！！"))
                                                        )
                                                        .addParagraph(
                                                                new JsonTextParagraph("点我执行命令\n")
                                                                        .setClickEvent(new ClickEventRunCommand("/命令"))
                                                                        .setHoverEvent(new HoverEventShowItem(
                                                                                new Item()
                                                                                        .setItemID(ItemType.stone)
                                                                                        .setItemNBTs(
                                                                                                new ItemNBTs()
                                                                                                        .setDisplayName(new JsonTextParagraph("我是一个小石头"))
                                                                                                        .setLore(
                                                                                                                List.of(
                                                                                                                        new JsonTextParagraph("我显示的是物品属性"),
                                                                                                                        new JsonTextParagraph("我只是一个石头")
                                                                                                                )
                                                                                                        )
                                                                                        )
                                                                        ))
                                                        )
                                                        .addParagraph(
                                                                new JsonTextParagraph("点我打开链接\n")
                                                                        .setHoverEvent(new HoverEventShowText("还是显示文本方便啊，显示物品太麻烦了"))
                                                                        .setClickEvent(new ClickEventoOpenUrl("https://jja8.cn"))
                                                        )
                                        )
                                )
                )
        );
        //玩家执行书本上的命令才表示同意协议，之后才执行 reg0() 方法开始注册。
        //玩家执行书本上的命令才表示同意协议，之后才执行 reg0() 方法开始注册。
        //玩家执行书本上的命令才表示同意协议，之后才执行 reg0() 方法开始注册。
        //玩家执行书本上的命令才表示同意协议，之后才执行 reg0() 方法开始注册。
        //玩家执行书本上的命令才表示同意协议，之后才执行 reg0() 方法开始注册。
        //玩家执行书本上的命令才表示同意协议，之后才执行 reg0() 方法开始注册。
    }
    /**
     * 执行注册逻辑
     * */
    public void reg0(Player player){
        boolean[] send = {false};
        User user = new User(player.getUUID(),player.getName(),null,null,getIP(player));
        AnvilInventory anvilInventory = new AnvilInventory(new JsonTextParagraph("创建账户 | 请设置密码。"));
        anvilInventory.setIn1(getInItem());
        anvilInventory.setOut(getOkTime());
        anvilInventory.setRenameItemLister(event -> anvilInventory.setOut(getOkTime()));
        anvilInventory.setCloseLister(event -> {if(!send[0])event.getPlayer().openInventory(anvilInventory);});
        anvilInventory.setClickLister(event -> {
            if (send[0]){
                return;
            }
            if (event.getSlot()!=2) {
                return;
            }
            String passwordText = anvilInventory.getReSetName();
            if (passwordText==null||passwordText.length()<6){
                anvilInventory.setOut(getErrorTime(new JsonTextParagraph("密码长度必须大于6位！请重新输入。")));
                return;
            }
            reg1(player,user,passwordText);
        });
        send[0] = true;
        player.openInventory(anvilInventory);
    }
    private void reg1(Player player, User user, String passwordText1){
        boolean[] send = {false};
        AnvilInventory anvilInventory = new AnvilInventory(new JsonTextParagraph("创建账户 | 请重新输入一次密码确保我们得到的是正确的密码。"));
        anvilInventory.setIn1(getInItem());
        anvilInventory.setIn2(new Item().setItemID(ItemType.paper).setItemNBTs(new ItemNBTs().setDisplayName(new JsonTextParagraph("上一步"))));
        anvilInventory.setOut(getOkTime());
        anvilInventory.setRenameItemLister(event -> anvilInventory.setOut(getOkTime()));
        anvilInventory.setCloseLister(event -> {if(!send[0])event.getPlayer().openInventory(anvilInventory);});
        anvilInventory.setClickLister(event -> {
            if (send[0]){
                return;
            }
            if (event.getSlot()==1){
                send[0] = true;
                reg(player);
            }
            if (event.getSlot()!=2) {
                return;
            }
            String passwordText2 = anvilInventory.getReSetName();
            if (passwordText2==null||!passwordText2.equals(passwordText1)){
                anvilInventory.setOut(getErrorTime(new JsonTextParagraph("两次密码不一致！请重新输入。")));
                return;
            }
            user.setPassword(User.Password.plaintextPassword(passwordText2));
            send[0] = true;
            reg2(player,user);
        });
        player.openInventory(anvilInventory);
    }

    private void reg2(Player player, User user) {
        boolean[] send = {false};
        AnvilInventory anvilInventory = new AnvilInventory(new JsonTextParagraph("创建账户 | 请输入手机号。"));
        anvilInventory.setIn1(getInItem());
        anvilInventory.setOut(getOkTime());
        anvilInventory.setRenameItemLister(event -> {if(!send[0])anvilInventory.setOut(getOkTime());});
        anvilInventory.setCloseLister(event -> {if(!send[0])event.getPlayer().openInventory(anvilInventory);});
        anvilInventory.setClickLister(event -> {
            if (send[0]){
                return;
            }
            if (event.getSlot()!=2) {
                return;
            }
            String phoneText = anvilInventory.getReSetName();
            if (phoneText==null){
                anvilInventory.setOut(getErrorTime(new JsonTextParagraph("手机号格式不正确！请重新输入。")));
                return;
            }
            long phone;
            try {
                phone = Long.parseLong(phoneText);
            }catch (NumberFormatException e){
                anvilInventory.setOut(getErrorTime(new JsonTextParagraph("手机号格式不正确！请重新输入。")));
                return;
            }
            String code = String.valueOf(random.nextInt(100000,999999));
            //发送验证码可能需要等待
            send[0] = true;
            anvilInventory.setOut(getErrorTime(new JsonTextParagraph("正在发送验证码，请稍等..")));
            //发送验证码
            SMSCodeUtils.sendMessage(code,phone, SMSCodeUtils.Type.bind);
            reg3(player,user,phoneText,code);
        });
        player.openInventory(anvilInventory);
    }

    private void reg3(Player player, User user, String phone, String verificationCode){
        boolean[] send = {false};
        AnvilInventory anvilInventory = new AnvilInventory(new JsonTextParagraph("创建账户 | 请输入您接收到的手机验证码。"));
        anvilInventory.setIn1(getInItem());
        anvilInventory.setOut(getOkTime());
        anvilInventory.setCloseLister(event -> {if(!send[0])event.getPlayer().openInventory(anvilInventory);});
        anvilInventory.setRenameItemLister(event -> {if(!send[0])anvilInventory.setOut(getOkTime());});
        anvilInventory.setClickLister(event -> {
            if (send[0]){
                return;
            }
            if (event.getSlot()!=2) {
                return;
            }
            String yzm = anvilInventory.getReSetName();
            if (yzm==null||!yzm.equals(verificationCode)){
                anvilInventory.setOut(getErrorTime(new JsonTextParagraph("验证码错误！请重新输入。")));
                return;
            }
            user.setPhone(phone);
            //提交到数据库可能需要等待
            send[0] = true;
            anvilInventory.setOut(getErrorTime(new JsonTextParagraph("正在完成注册，请稍等..")));
            login.getUserManager().insert(user);
            //注册成功，关闭界面，通知事件
            player.closeInventory();
            EventManager.call(new LoginSuccessfulEvent(player));
        });
        player.openInventory(anvilInventory);
    }
}
