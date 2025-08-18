package io.github.hato1883.core.ui.tui;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TUIInput {

    private static final BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
    private static Thread inputThread;
    private static volatile boolean running = false;

    public static void start() {
        if (running) return;
        running = true;
        inputThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (running) {
                if (scanner.hasNextLine()) {
                    inputQueue.offer(scanner.nextLine().trim());
                }
            }
        }, "Global-TUI-Input-Thread");
        inputThread.setDaemon(true);
        inputThread.start();
    }

    public static void stop() {
        running = false;
        if (inputThread != null) {
            inputThread.interrupt();
            inputThread = null;
        }
    }

    public static String pollInput() {
        return inputQueue.poll();
    }

    public static void clear() {
        inputQueue.clear();
    }
}
