public class MinimalistTimer {
    private long time;

    public MinimalistTimer() {
        this.time = System.nanoTime();
    }

    public long time() {
        long oldTime = time;
        long newTime = System.nanoTime();
        time = newTime;
        return newTime - oldTime;
    };
}
