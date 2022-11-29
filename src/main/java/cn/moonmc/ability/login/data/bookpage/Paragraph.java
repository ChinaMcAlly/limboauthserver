package cn.moonmc.ability.login.data.bookpage;

import com.google.gson.annotations.SerializedName;

/**
 * @author CNLuminous 2022/11/28
 * Json: {"text":"§6§l同意服规,前往注册\n","hoverEvent":{"text":"点击即认为您同意以上服务器条例,\n并且同意遵守服务器规则"},"clickEvent":{"page":6,"url":"https:","command":"/quit"}}
 *
 */
public class Paragraph {

    @SerializedName("text")
    private String text;
    @SerializedName("hoverEvent")
    private HoverEventDTO hoverEvent;
    @SerializedName("clickEvent")
    private ClickEventDTO clickEvent;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public HoverEventDTO getHoverEvent() {
        return hoverEvent;
    }

    public void setHoverEvent(HoverEventDTO hoverEvent) {
        this.hoverEvent = hoverEvent;
    }

    public ClickEventDTO getClickEvent() {
        return clickEvent;
    }

    public void setClickEvent(ClickEventDTO clickEvent) {
        this.clickEvent = clickEvent;
    }

    public static class HoverEventDTO {
        @SerializedName("text")
        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public static class ClickEventDTO {
        @SerializedName("page")
        private Integer page;
        @SerializedName("url")
        private String url;
        @SerializedName("command")
        private String command;

        public Integer getPage() {
            return page;
        }

        public void setPage(Integer page) {
            this.page = page;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getCommand() {
            return command;
        }

        public void setCommand(String command) {
            this.command = command;
        }

        @Override
        public String toString() {
            return "ClickEventDTO{" +
                    "page=" + page +
                    ", url='" + url + '\'' +
                    ", command='" + command + '\'' +
                    '}';
        }
    }
}
