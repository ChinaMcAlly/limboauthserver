package cn.moonmc.limboauthserver.utils;

import cn.moonmc.limbo.works.menu.Item;
import cn.moonmc.limbo.works.menu.ItemNBTs;
import cn.moonmc.limbo.works.menu.ItemType;
import cn.moonmc.limbo.works.message.JsonText;

/**
 * package cn.moonmc.limboauthserver.utils
 * project limboauthserver
 * Created by @author XuHang on date 2022/10/16
 */
public class GUIUtils {
    public static Item oKButton = new Item()
            .setItemID(ItemType.paper)
            .setCount(1)
            .setItemNBTs(
                    new ItemNBTs()
                            .setDisplayName(
                                    new JsonText("确定")
                            )
            );

    public static Item inputButton = new Item()
            .setItemID(ItemType.paper)
            .setCount(1)
            .setItemNBTs(
                    new ItemNBTs()
                            .setDisplayName(
                                    new JsonText(">")
                            )
            );

    public static String inputTextRevert(String inText) {
        if (inText.startsWith(">")) {
            inText = inText.replaceFirst(">", "");
        }
        return inText;
    }
}
