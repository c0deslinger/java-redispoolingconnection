import core.MyJedisPool;

public class Main {
    public static void main(String[] args) {
        MyJedisPool.getInstance();
        for (int i = 0; i < 100; i++) {
            final int n = i;
            Thread thread = new Thread() {
                public void run() {
                    MyJedisPool.getInstance().put("tes-"+n, "in-"+n);
                }
            };
            thread.start();
        }
    }
}
