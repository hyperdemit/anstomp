package id.hyperdemit.anstomp;

public class Debug {
    public static final boolean DEBUG_DEV = true;
    public static final boolean DEBUG_PROD = true;
    private boolean enable;

    public Debug(boolean enable) {
        this.enable = enable;
    }

    public void iPrint(String className, String message) {
        if (enable) {
            System.out.println("Anstomp/I: " + className + ": " + message);
        }
    }

    public void ePrint(String className, String message) {
        if (enable) {
            System.out.println("Anstomp/E: " + className + ": " + message);
        }
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
