public class MinimalistTimer {
    private long time;

    public MinimalistTimer() {
        this.time = System.nanoTime();
    }

    /**
     * @return Nanoseconds since the timer was created or this method was last called.
     */
    public long time() {
        long oldTime = time;
        long newTime = System.nanoTime();
        time = newTime;
        return newTime - oldTime;
    };
}
