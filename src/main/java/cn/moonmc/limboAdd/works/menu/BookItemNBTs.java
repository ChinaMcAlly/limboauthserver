package cn.moonmc.limboAdd.works.menu;

import cn.moonmc.limboAdd.works.message.JsonText;
import lombok.Data;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Data
public class BookItemNBTs extends ItemNBTs{

    /**
     * 书的标题
     * */
    String title;
    /**
     * 书的作者
     * */
    String author;

    /**
     * 书的页面
     * */
    List<JsonText> pages;

    @Override
    protected void builderNBT(CompoundBinaryTag.@NotNull Builder nbt) {
        super.builderNBT(nbt);
        addTitle(nbt);
        addAuthor(nbt);
        addPages(nbt);
    }

    private void addPages(CompoundBinaryTag.@NotNull Builder nbt) {
        if (pages!=null){
            ListBinaryTag.Builder<net.kyori.adventure.nbt.BinaryTag> binaryTagBuilder = ListBinaryTag.builder();
            for (JsonText jsonText : lore) {
                binaryTagBuilder.add(StringBinaryTag.of(jsonText.toJsonText()));
            }
            nbt.put("pages",binaryTagBuilder.build());
        }
    }

    private void addAuthor(CompoundBinaryTag.Builder nbt) {
        if (author!=null){
            nbt.putString("author",author);
        }
    }

    private void addTitle(CompoundBinaryTag.Builder nbt) {
        if (title!=null){
            nbt.putString("title",title);
        }
    }



}
