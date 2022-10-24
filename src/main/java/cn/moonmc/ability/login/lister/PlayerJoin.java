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

import java.util.*;

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
            if (event.getCommand().equals(event.getPlayer().getQuitCmd())){
                event.getPlayer().disconnect(new JsonTextParagraph("§6§l您已退出登录流程"));
            }
            if (event.getCommand().equals(event.getPlayer().getRegCmd())){
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
                                                new JsonTextParagraph("服规《服务器法则手册》\n"
                                                        +"核心观念：禁止以下行为\n" +
                                                        "游戏作弊 不尊重他人 故意卡服 不爱惜服务器 \n" +
                                                        "推广第三方 引流第三方 不珍惜游玩资格\n" +
                                                        "服主态度：\n" +
                                                        "对于君子 以礼相待 \n" +
                                                        "对于小人 更加小人\n\n"+"请继续阅读本条例后面的内容,阅读完成后即可注册。"),
                                                new JsonTextParagraph("【服务器法则手册】\n" +
                                                        "  更新日期2021年7月17日\n" +
                                                        "《服务器法则手册》以下简称“法律手册”\n" +"在你开始游玩本服务器之前，请确认您已认真阅读本“法律手册”，并已经完全了解和同意本法律手册的条款。否则你应该拒绝游玩本服！如果你对服务器法律和条款有建议，欢迎提出！\n"),
                                                new JsonTextParagraph("法律手册的意义：国有国法，家有家规，服有服规，无规则不成方圆。为了给大家带来更好的游戏体验，保证服务器的和平公正，这一切都是为了给大家带来更好的游戏体验，才特设此法律手册。"),
                                                new JsonTextParagraph("重要提醒：\n" +
                                                        "\n" +
                                                        "服务器法律会不定时更新，请随时注意官网play.moonmc.cn的公示!保证你已阅读最新版“法律手册”。\n" +
                                                        "发现违规者请及时举报，知情不报将视为同罪。\n" +
                                                        "服务器鼓励玩家举报违法违规行为。可以使用截图，视频录制等方法，把违规违法资料发到官网，并联系在线管理员以解决"),
                                                new JsonTextParagraph("服务器装有监控插件，使用指令/co i可以查询记录，举报者将获得罚款的60%作为报酬，可匿名举报。\n" +
                                                        "服务器法则为玩家必看，管理有权在不通知的前提下直接处罚违规玩家。\n" +
                                                                "罚款可以作为管理的私有财产以做奖励。\n" +
                                                                "本服处罚只认违规ID，请各位保管好自己的账号。\n" +
                                                                "自首可以从轻处罚或免于处罚。"),
                                                new JsonTextParagraph("帮好友自首也可以减免处罚！\n" +
                                                        "手册内规定罚款无法偿还的，按10w游戏币=1天计算。\n" +
                                                        "服主享有本法则的解释权。"),
                                                new JsonTextParagraph("一.破坏游戏平衡(作弊/辅助)违规\n" +
                                                        "作为一款以原版生存为主要玩法的服务器，拒绝任何利用，外挂软件/插件漏洞/程序/游戏漏洞/辅助等，破\n" +
                                                        "坏正常游戏平衡的行为。有相关(使用作弊工具等)违规行为被视为非平衡游戏违规。处30天至永久封禁，罚款25w至无 上限的惩罚。"),
                                                new JsonTextParagraph("二.干扰他人游戏体验违规\n" +
                                                        "作为以多人游戏为基础的生存服务器，玩家之间的信息及交流与游戏体验尤为重要，有破坏，干扰，影响,\n" +
                                                        "他人正常游戏及游戏成果包括但不限于(破坏建筑，未经允许击杀其他玩家，盗窃，骗取，言语攻击其他玩.\n" +
                                                        "家等)行为被视为干扰他人游戏体验违规。处1小时至永久封禁，罚款1w-400w不等。 禁言1分钟至720小时不等，清空或收回非正常所得的一切"),
                                                new JsonTextParagraph("利益,须尽力恢复受害者受侵害的部分。"),
                                                new JsonTextParagraph("三.破坏服务器运行违规\n" +
                                                        "因服务器应用限制，服务器负载有限，使用任何形式干扰服务器的流畅运行，干扰他人游戏体验，在管理警" +
                                                        "告三次后无作为继续侵害的。视为破坏服务器运行违规。每一名玩家都必须知道什么行为是破坏服务器稳定" +
                                                        "运行的行为，并制止这种行为。在服务器里不知不等于无罪。处1天至永久封禁不等，罚款10w-1300w不等,及恢复非法侵害行为等。"),
                                                new JsonTextParagraph("四.违规推流引流\n" +
                                                        "本服拒绝任何形式在游戏内发布商业信息，包括但不限于:视频连接，直播连接，正版代购，购物消费，其\n" +
                                                        "它服务器。尤其是庄主已经运营或即将运营的项目。无论是商业还是非商业形式的。有发现以任何形式推流\n" +
                                                        "引流或其他商业非商业行为的，视为违规推流引流。处封禁1天至永久封禁，罚款5w-5000w不等.及禁言1小时-无上限等。"),
                                                new JsonTextParagraph("五.虚拟财产套现货币(人民币及其它法定货币)违规\n" +
                                                        "以任何形式(包括但不限于骗取、偷取、赌博、买卖、集资套取、高利贷、非法借贷、游戏内道具、游戏\n" +
                                                        "金币、游戏权限、游戏账号等)与其他玩家换取货币的行为。无论最终获利者为谁，实行行为的侵犯者，都\n" +
                                                        "将视为非法手段获取货币违规。处封禁30天至永久封禁，罚款涉案金额*1.5倍~3值的赔偿。\n"),
                                                new JsonTextParagraph("注:请各位玩家保管好自己的游戏资产及私人财产安全，利用服务器与其他玩家进行货币交易或进行赌博的\n" +
                                                        "行为，本服务器有义务进行监管和向有关部门举报，所造成的损失与服务器无关。"),
                                                new JsonTextParagraph("六.管理员法\n" +
                                                        "请各位管理员严格按照服规秉公执法，每个执法都要有服规条款的支持。不能封禁玩家超过30天。永久封号的需提交给服主处理! 对于新玩家和首犯，管理员应以劝导为主，或从轻处罚!\n" +
                                                        "●保证不公报私仇\n" +
                                                        "●保证不情绪化执法\n" +
                                                        "●保证不以个人意愿代替服规\n" +
                                                        "●保证执法量刑参考《服务器案例 》"),
                                                new JsonTextArticle(new JsonTextParagraph("前往注册\n"))
                                                        .addParagraph(
                                                                new JsonTextParagraph("§6§l同意服规,前往注册\n")
                                                                        .setClickEvent(new ClickEventRunCommand("/"+player.getRegCmd()))
                                                                        .setHoverEvent(new HoverEventShowText("点击即认为您同意以上服务器条例,\n并且同意遵守服务器规则"))
                                                        )
                                                        .addParagraph(
                                                                new JsonTextParagraph("§c§l拒绝服规,退出服务器\n")
                                                                        .setClickEvent(new ClickEventRunCommand("/"+player.getQuitCmd()))
                                                                        .setHoverEvent(new HoverEventShowText("点击即认为您同意以上服务器条例,\n并且同意遵守服务器规则"))
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
            send[0] = true;
            reg1(player,user,passwordText);
        });
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
                reg0(player);
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
