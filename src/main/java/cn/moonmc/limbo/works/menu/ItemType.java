package cn.moonmc.limbo.works.menu;

import lombok.Getter;

/**
 * 物品类型，这玩意没对照表。一个一个试吧awa
 *
 * @author jja8
 */
public enum ItemType {
    paper(829),
    stone(1),
    ;
    @Getter
    final int id;

    ItemType(int id) {
        this.id = id;
    }
}
