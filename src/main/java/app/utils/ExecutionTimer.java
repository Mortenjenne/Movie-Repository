package app.utils;

public class ExecutionTimer
{
    private static Long start;

    public static void start()
    {
        start = System.currentTimeMillis();
    }

    public static void finish()
    {
        Long finish = System.currentTimeMillis();
        Long elapsedTime = finish - start;
        long minutes = (elapsedTime / 1000) / 60;
        long seconds = (elapsedTime / 1000) % 60;
        long milliseconds = elapsedTime % 1000;

        // Only 2 decimals on milliseconds
        long hundredths = milliseconds / 10;

        System.out.printf("Total runtime: %02d:%02d.%02d%n",
                minutes,
                seconds,
                hundredths);
    }
}
