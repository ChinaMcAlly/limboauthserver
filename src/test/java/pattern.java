import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author CNLuminous 2022/10/30
 */
public class pattern {
    public static void main(String[] args) {
        String name = "^^^^12122aa";
        String pattern = "[A-Za-z0-9_]{3,15}";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(name);
        if (!m.matches()){
            System.out.println("不匹配");
        }else{
            System.out.println("匹配");
        }

    }
}
