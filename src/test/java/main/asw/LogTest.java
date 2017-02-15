package main.asw;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author nokutu
 * @since 10/02/2017.
 */
public class LogTest {

    @Before
    public void removeFile() {
        File f = new File("./test.log");
        if (f.exists()) {
            f.delete();
        }
    }

    @Test
    public void writeTest() {

        Logger log = LoggerFactory.getLogger(getClass());
        String message = String.valueOf(Math.random());
        log.debug("aaaaaa");
        log.debug(message);
        log.error("bnbbbb");

        BufferedReader br = null;
        try {
            File f = new File("./test.log");
            br = new BufferedReader(new FileReader(f));
            boolean contained = false;
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(message)) {
                    contained = true;
                    break;
                }
            }
            assert contained;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
