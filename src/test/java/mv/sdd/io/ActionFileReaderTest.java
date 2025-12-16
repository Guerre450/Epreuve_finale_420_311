package mv.sdd.io;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ActionFileReaderTest {


    @Test
    void readActions() {
        assertDoesNotThrow(() -> {ActionFileReader.readActions("data/scenario_1.txt");});
        try{
            ActionFileReader.readActions("data/scenario_1.txt").forEach(System.out::println);
        }
         catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}