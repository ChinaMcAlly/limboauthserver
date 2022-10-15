package cn.moonmc.limboauthserver.function.auth;

import cn.moonmc.limbo.Player;
import cn.moonmc.limbo.works.event.EventManager;
import cn.moonmc.limbo.works.event.Lister;
import cn.moonmc.limbo.works.event.playerEvent.PlayerClickContainer;
import cn.moonmc.limbo.works.event.playerEvent.PlayerJoinEvent;
import cn.moonmc.limbo.works.event.playerEvent.PlayerRenameItem;
import cn.moonmc.limbo.works.menu.AnvilInventory;
import cn.moonmc.limbo.works.menu.Item;
import cn.moonmc.limbo.works.menu.ItemNBTs;
import cn.moonmc.limbo.works.menu.ItemType;
import cn.moonmc.limbo.works.message.JsonText;
import cn.moonmc.limboauthserver.entity.User;
import cn.moonmc.limboauthserver.utils.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author CNLuminous 2022/10/15
 */
@Component
@Slf4j
@Order(1)
public class PlayerJoinFunction implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        EventManager.regLister(new Lister<>(PlayerJoinEvent.class){
            @Override
            public void listen(PlayerJoinEvent event) {
                Player player = event.getPlayer();
                User user = AuthService.selectUser(player.getUUID().toString());//正版和离线的uuid不同，这样存可能出现问题
                if (user==null){
                    if (!AuthService.register(user = new User(player))) {
                        player.disconnect(new JsonText("创建用户信息失败，请稍后重试！"));
                    }
                }
                //进入验证密码流程
                verifyPassword(event.getPlayer(),user);
            }
        });
    }

    //第一个流程，验证密码
    private void verifyPassword(Player player, User user) {
        if (user.getPassword()==null){//如果没有密码就进入创建密码流程
            final String[] password = new String[2];
            AnvilInventory anvilInventory = new AnvilInventory(new JsonText("为"+player.getName()+"创建密码 | 请输入密码"));
            Item item = new Item();
            item.setItemID(ItemType.paper);
            ItemNBTs itemNBTs = new ItemNBTs();
            itemNBTs.setDisplayName(new JsonText(">"));
            item.setItemNBTs(itemNBTs);
            anvilInventory.setIn1(item);
            player.openInventory(anvilInventory);
            //这里注册的监听器会随着界面被关闭失效
            //注册改名监听器
            anvilInventory.setRenameItemLister(new Lister<>(PlayerRenameItem.class) {
                @Override
                public void listen(PlayerRenameItem event) {
                    password[0] = event.getName();

                    //如果有错误提示在修改密码时去掉提示
                    if (anvilInventory.getOut()!=null){
                        anvilInventory.setOut(null);
                    }

                }
            });
            //注册点击监听器
            anvilInventory.setClickLister(new Lister<>(PlayerClickContainer.class) {
                @Override
                public void listen(PlayerClickContainer event) {
                    if (event.getSlot()==2) {//如果玩家点击确定，就进入重复密码流程
                        if (password[0]==null||password[0].length()<6){
                            Item item = new Item();
                            item.setItemID(ItemType.stone);
                            ItemNBTs itemNBTs = new ItemNBTs();
                            itemNBTs.setDisplayName(new JsonText("密码长度必须大于等于6位"));
                            item.setItemNBTs(itemNBTs);
                            anvilInventory.setOut(item);
                            return;
                        }

                        //进入重复密码流程
                        AnvilInventory anvilInventory = new AnvilInventory(new JsonText("为"+player.getName()+"创建密码 | 请再输入一次密码，确保我们得到的是正确的密码"));
                        Item item = new Item();
                        item.setItemID(ItemType.paper);
                        ItemNBTs itemNBTs = new ItemNBTs();
                        itemNBTs.setDisplayName(new JsonText(">"));
                        item.setItemNBTs(itemNBTs);
                        anvilInventory.setIn1(item);
                        player.openInventory(anvilInventory);
                        anvilInventory.setRenameItemLister(new Lister<>(PlayerRenameItem.class) {
                            @Override
                            public void listen(PlayerRenameItem event) {
                                password[1] = event.getName();

                                //如果有错误提示在修改密码时去掉提示
                                if (anvilInventory.getOut()!=null){
                                    anvilInventory.setOut(null);
                                }
                            }
                        });

                        anvilInventory.setClickLister(new Lister<>(PlayerClickContainer.class){
                            @Override
                            public void listen(PlayerClickContainer event) {
                                if (event.getSlot()==2){//玩家已经输入完成重复密码，开始验证
                                    if (!password[0].equals(password[1])){//提示两次密码不一致
                                        Item item = new Item();
                                        item.setItemID(ItemType.stone);
                                        ItemNBTs itemNBTs = new ItemNBTs();
                                        itemNBTs.setDisplayName(new JsonText("两次密码不一致！"));
                                        item.setItemNBTs(itemNBTs);
                                        anvilInventory.setOut(item);
                                        return;
                                    }
                                    //密码一致，将密码写入数据库
                                    user.setPassword(password[0]);
                                    AuthService.update(user);
                                    //进入验证手机号流程
                                    verifyPhone(player,user);
                                }
                            }
                        });
                    }
                }
            });
        }else {//有密码就进入登录流程
            final String[] password = new String[1];
            AnvilInventory anvilInventory = new AnvilInventory(new JsonText("登录 | 请输入密码"));
            Item item = new Item();
            item.setItemID(ItemType.paper);
            ItemNBTs itemNBTs = new ItemNBTs();
            itemNBTs.setDisplayName(new JsonText(">"));
            item.setItemNBTs(itemNBTs);
            anvilInventory.setIn1(item);
            player.openInventory(anvilInventory);
            //这里注册的监听器会随着界面被关闭失效
            //注册改名监听器
            anvilInventory.setRenameItemLister(new Lister<>(PlayerRenameItem.class) {
                @Override
                public void listen(PlayerRenameItem event) {
                    password[0] = event.getName();

                    //如果有错误提示在修改密码时去掉提示
                    if (anvilInventory.getOut()!=null){
                        anvilInventory.setOut(null);
                    }
                }
            });
            //注册点击监听器
            anvilInventory.setClickLister(new Lister<>(PlayerClickContainer.class) {
                @Override
                public void listen(PlayerClickContainer event) {
                    if (!user.getPassword().equals(password[0])) {//密码不一致，提示玩家重新输入
                        Item item = new Item();
                        item.setItemID(ItemType.stone);
                        ItemNBTs itemNBTs = new ItemNBTs();
                        itemNBTs.setDisplayName(new JsonText("密码错误，请重新输入！"));
                        item.setItemNBTs(itemNBTs);
                        anvilInventory.setOut(item);
                        return;
                    }
                    //登录成功，进入验证手机号流程
                    verifyPhone(player,user);
                }
            });

        }
    }

    //第二个流程验证手机号
    public void verifyPhone(Player player, User user){
        if (user.getPhone()==null){
            //绑定手机号流程
        }else {
            //player.关闭当前界面() //这个方法忘记写了
            player.sendTitle(new JsonText("登录成功！"),new JsonText("马上有选择服务器菜单打开,其实还没写awa。"),1,100,1);
        }

    }


}
