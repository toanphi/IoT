package com.uet.iot.util;

import java.io.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class LircConfigCreator {

    private final String WINDOWS_OS_PREFIX = "windows";
    private final String OS_NAME_KEY = "os.name";

    private ProcessBuilder processBuilder;
    private Process process;
    private OutputStream stdin;
    private InputStream stderr;
    private InputStream stdout;
    private BufferedReader reader;
    private BufferedWriter writer;

    public LircConfigCreator() throws IOException {
        if (isWindows()) {
            processBuilder = new ProcessBuilder("cmd.exe");
        } else {
            processBuilder = new ProcessBuilder("bash");
        }
    }

    public boolean startProcess() throws IOException {
        process = processBuilder.start();

        stdin = process.getOutputStream ();
        stderr = process.getErrorStream ();
        stdout = process.getInputStream ();

        writer = new BufferedWriter(new OutputStreamWriter(stdin));
        reader = new BufferedReader (new InputStreamReader(stdout));

        return process.isAlive();
    }

    public String recordLirc() throws IOException {

        writer.write("((" + "sudo mode2 -m -d /dev/lirc1" + ") && echo --EOF--) || echo --EOF--\n");
        writer.flush();

        StringBuilder sb = new StringBuilder();
        String line = reader.readLine();
        while (true) {
            sb.append(line + "\n");
            if (line.contains("pulse")){
                writer = null;
                reader = null;
                process.destroy();
                return sb.toString();
            }
            line = reader.readLine();
        }
    }

    private boolean isWindows() {
        return System.getProperty(OS_NAME_KEY).toLowerCase().startsWith(WINDOWS_OS_PREFIX);
    }

}
