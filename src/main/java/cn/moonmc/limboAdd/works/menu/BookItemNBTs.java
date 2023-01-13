package cn.moonmc.limboAdd.works.menu;

import cn.moonmc.limboAdd.works.message.JsonText;
import cn.moonmc.limboAdd.works.message.JsonTextParagraph;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import org.jetbrains.annotations.NotNull;
import ru.nanit.limbo.protocol.registry.Version;

import java.util.List;

/**
 * @author jja8
 * */
@EqualsAndHashCode(callSuper=false)
public class BookItemNBTs extends ItemNBTs{

    /**
     * 书的标题
     * */
    String bookTitle = "默认的书";
    /**
     * 书的作者
     * */
    String bookAuthor = "jianjian";

    /**
     * 书的页面
     * */
    List<JsonText> bookPages = List.of(new JsonTextParagraph("默认页面"));

    public BookItemNBTs(Version version) {
        super(version);
    }

    /**
     * 设置书的标题
     * @return 返回自己，用于神奇写法
     * */
    public BookItemNBTs setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
        return this;
    }
    /**
     * 设置书的作者
     * @return 返回自己，用于神奇写法
     * */
    public BookItemNBTs setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
        return this;
    }
    /**
     * 设置书的页面
     * @return 返回自己，用于神奇写法
     * */
    public BookItemNBTs setBookPages(List<JsonText> bookPages) {
        this.bookPages = bookPages;
        return this;
    }

    @Override
    protected void builderNBT(CompoundBinaryTag.@NotNull Builder nbt) {
        super.builderNBT(nbt);
        addTitle(nbt);
        addAuthor(nbt);
        addPages(nbt,version);
    }

    private void addPages(CompoundBinaryTag.@NotNull Builder nbt, Version version) {
        ListBinaryTag.Builder<net.kyori.adventure.nbt.BinaryTag> binaryTagBuilder = ListBinaryTag.builder();
        for (JsonText jsonText : bookPages) {
            binaryTagBuilder.add(StringBinaryTag.of(jsonText.toJsonText(version)));
        }
        nbt.put("pages",binaryTagBuilder.build());
    }

    private void addAuthor(CompoundBinaryTag.Builder nbt) {
        nbt.putString("author", bookAuthor);
    }

    private void addTitle(CompoundBinaryTag.Builder nbt) {
        nbt.putString("title", bookTitle);
    }



}
