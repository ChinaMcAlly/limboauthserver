package cn.moonmc.limboAdd.works.menu;

import lombok.Getter;

/**
 * 物品类型，这玩意没对照表。一个一个试吧awa
 * @author jja8
 * */
public enum ItemType {
    paper(829),
    written_book(986),
    stone(1),
    air(0),
    ;
    @Getter
    final int id;

    ItemType(int id) {
        this.id = id;
    }
}
