package billy.ui;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Thin wrapper around standard input/output for the terminal UI.
 *
 * <p>The no-arg constructor binds to {@link System#in}/{@link System#out}; the
 * stream constructor allows tests to inject scripted input and capture output.</p>
 */
public class Console {

    private final Scanner scanner;
    private final PrintStream out;

    public Console() {
        this(System.in, System.out);
    }

    public Console(InputStream in, PrintStream out) {
        this.scanner = new Scanner(in);
        this.out = out;
    }

    public void print(String text) {
        out.print(text);
    }

    public void println(String text) {
        out.println(text);
    }

    public void println() {
        out.println();
    }

    /** Returns true if more input is available (false on EOF, e.g. piped input). */
    public boolean hasInput() {
        return scanner.hasNextLine();
    }

    /** Reads a line, or null at end of input. */
    public String readLine() {
        return scanner.hasNextLine() ? scanner.nextLine() : null;
    }

    public String prompt(String label) {
        print(label);
        return readLine();
    }
}

