package ReadFile;

import java.io.IOException;

/**
 * interface for READFILE package - each strategy that implements readfile need to implement splitToDocs method
 */

public interface ReadFileMethods {

    public String splitToDocs() throws IOException, InterruptedException;
}
