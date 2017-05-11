package eladkay.hudpixel.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class Handler {
    private static PrintStream out;
    public static void nullOut() {
        PrintStream stream = System.out;
        System.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                //noop
            }
        }));
        out = stream;
    }
    public static void bringBackOut() {
        System.setOut(out);
    }
}