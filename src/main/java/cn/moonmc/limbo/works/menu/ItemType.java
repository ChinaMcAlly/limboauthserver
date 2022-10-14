package cn.moonmc.limbo.works.menu;

import lombok.Getter;

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
