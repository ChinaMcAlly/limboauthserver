package cn.moonmc.ability.login.function.auth;

import cn.moonmc.ability.login.entity.User;
import cn.moonmc.ability.login.utils.AuthService;
import cn.moonmc.limboAdd.works.entity.Player;
import cn.moonmc.limboAdd.works.event.EventManager;
import cn.moonmc.limboAdd.works.event.playerEvent.PlayerJoinEvent;
import cn.moonmc.limboAdd.works.menu.AnvilInventory;
import cn.moonmc.limboAdd.works.menu.Item;
import cn.moonmc.limboAdd.works.menu.ItemNBTs;
import cn.moonmc.limboAdd.works.menu.ItemType;
import cn.moonmc.limboAdd.works.message.JsonText;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author CNLuminous 2022/10/15
 */
@Slf4j
public class PlayerJoinFunction{
    /**确定按钮*/
    @Getter
    static Item oKButton = new Item()
            .setItemID(ItemType.paper)
            .setCount(1)
            .setItemNBTs(
                    new ItemNBTs()
                            .setDisplayName(
                                    new JsonText("确定")
                            )
            );

    /**输入按钮*/
    @Getter
    static Item inputButton = new Item()
            .setItemID(ItemType.paper)
            .setCount(1)
            .setItemNBTs(
                    new ItemNBTs()
                            .setDisplayName(
                                    new JsonText(">")
                            )
            );

    /**
     * 错误按钮
     * @param jsonText 按钮提示的信息，可以null
     * @param lore 按钮提示的信息，可以null
     * */
    public static Item getErrorButton(JsonText jsonText, List<JsonText> lore){
        Item item = new Item().setCount(1).setItemID(ItemType.stone);
        if (jsonText==null&&lore==null){
            return item;
        }
        item.setItemNBTs(new ItemNBTs());
        if (jsonText!=null){
            item.getItemNBTs().setDisplayName(jsonText);
        }
        if (lore!=null){
            item.getItemNBTs().setLore(lore);
        }
        return item;
    }

    /**
     * 输入文本处理，服务器获取到的文本和用户实际想输入的文本可能不一致。例如用户输入的是“123456”我们得到的可能是“>123456” 具体情况看 inputButton的名字，此方法用来处理这个。
     * */
    public static String inputTextRevert(String inText){
        if (inText.startsWith(">")){
           inText = inText.replaceFirst(">","");
        }
        return inText;
    }




    public static void run() {
        EventManager.regLister(PlayerJoinEvent.class, event -> {
            Player player = event.getPlayer();
            User user = AuthService.selectUser(player.getUUID().toString());//正版和离线的uuid不同，这样存可能出现问题
            if (user==null){
                user = new User(event.getPlayer());
            }
            //进入验证密码流程
            verifyPassword(event.getPlayer(),user);
        });
    }

    //第一个流程，验证密码
    private static void verifyPassword(Player player, User user) {
        if (user.getPassword()==null){//如果没有密码就进入创建密码流程
            final String[] password = new String[2];
            AnvilInventory anvilInventory = new AnvilInventory(new JsonText("为"+player.getName()+"创建密码 | 请输入密码"));
            anvilInventory.setOut(oKButton);
            anvilInventory.setIn1(inputButton);
            player.openInventory(anvilInventory);
            //这里注册的监听器会随着界面被关闭失效
            //注册改名监听器
            anvilInventory.setRenameItemLister(event -> {
                password[0] = inputTextRevert(event.getName());

                //如果有错误提示在修改密码时去掉提示
                if (anvilInventory.getOut()!=oKButton){
                    anvilInventory.setOut(oKButton);
                }
            });
            //注册点击监听器
            anvilInventory.setClickLister(event -> {
                if (event.getSlot()==2) {//如果玩家点击确定，就进入重复密码流程
                    if (password[0]==null||password[0].length()<6){
                        anvilInventory.setOut(getErrorButton(new JsonText("密码长度必须大于等于6位"),null));
                        return;
                    }

                    //进入重复密码流程
                    AnvilInventory anvilInventory1 = new AnvilInventory(new JsonText("为"+player.getName()+"创建密码 | 请再输入一次密码，确保我们得到的是正确的密码"));
                    anvilInventory1.setOut(oKButton);
                    anvilInventory1.setIn1(inputButton);
                    player.openInventory(anvilInventory1);
                    anvilInventory1.setRenameItemLister(event1 -> {
                        password[1] = inputTextRevert(event1.getName());

                        //如果有错误提示在修改密码时去掉提示
                        if (anvilInventory1.getOut()!=oKButton){
                            anvilInventory1.setOut(oKButton);
                        }
                    });

                    anvilInventory1.setClickLister(event12 -> {
                        if (event12.getSlot()==2){//玩家已经输入完成重复密码，开始验证
                            if (!password[0].equals(password[1])){//提示两次密码不一致
                                anvilInventory1.setOut(getErrorButton(new JsonText("两次密码不一致！"),null));
                                return;
                            }
                            //密码一致，将密码写入数据库
                            user.setPassword(password[0]);
                            AuthService.update(user);
                            //进入验证手机号流程
                            verifyPhone(player,user);
                        }
                    });
                }
            });
        }else {//有密码就进入登录流程
            final String[] password = new String[1];
            AnvilInventory anvilInventory = new AnvilInventory(new JsonText("登录 | 请输入密码"));
            anvilInventory.setOut(oKButton);
            anvilInventory.setIn1(inputButton);
            player.openInventory(anvilInventory);
            //这里注册的监听器会随着界面被关闭失效
            //注册改名监听器
            anvilInventory.setRenameItemLister(event -> {
                password[0] = inputTextRevert(event.getName());

                //如果有错误提示在修改密码时去掉提示
                if (anvilInventory.getOut()!=oKButton){
                    anvilInventory.setOut(oKButton);
                }
            });
            //注册点击监听器
            anvilInventory.setClickLister(event -> {
                if (!user.getPassword().equals(password[0])) {//密码不一致，提示玩家重新输入
                    anvilInventory.setOut(getErrorButton(new JsonText("密码错误，请重新输入！"),null));
                    return;
                }
                //登录成功，进入验证手机号流程
                verifyPhone(player,user);
            });

        }
    }

    //第二个流程验证手机号
    public static void verifyPhone(Player player, User user){
        if (user.getPhone()==null){
            //绑定手机号流程
        }else {
            //player.关闭当前界面() //这个方法忘记写了
            player.sendTitle(new JsonText("登录成功！"),new JsonText("马上有选择服务器菜单打开,其实还没写awa。"),1,100,1);
        }

    }


}
