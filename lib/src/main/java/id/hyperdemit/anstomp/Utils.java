package id.hyperdemit.anstomp;

public class Utils {
    public static String strRandomTimeMs(String prefix) {
        return prefix + "-" + System.currentTimeMillis() + "-" + (int) Math.floor(Math.random() * 1000);
    }
}
