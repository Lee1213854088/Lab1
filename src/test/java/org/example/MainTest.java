package org.example;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MainTest {
    private Main main;

    @Before
    public void setUp() {
        main = new Main();
        main.readFileAndBuildGraph("Input/1.txt");
    }

    @Test
    public void queryBridgeWords_test1() {
        String inputWord1 = "worlds";
        String inputWord2 = "seek";
        String expectedOutput1 = "life";
        String expectedOutput2 = "to";
        String actualOutput = main.queryBridgeWords(inputWord1, inputWord2);
        assertTrue(actualOutput.equals(expectedOutput1) || actualOutput.equals(expectedOutput2));
    }

    @Test
    public void queryBridgeWords_test2() {
        String inputWord1 = "to";
        String inputWord2 = "strange";
        String expectedOutput1 = "seek";
        String expectedOutput2 = "explore";
        String actualOutput = main.queryBridgeWords(inputWord1, inputWord2);
        assertTrue(actualOutput.equals(expectedOutput1) || actualOutput.equals(expectedOutput2));
    }

    @Test
    public void queryBridgeWords_test3() {
        String inputWord1 = "explore";
        String inputWord2 = "civilizations";
        String expectedOutput = "strange";
        String actualOutput = main.queryBridgeWords(inputWord1, inputWord2);
        assertTrue(actualOutput.equals(expectedOutput));
    }

    @Test
    public void queryBridgeWords_test4() {
        String inputWord1 = "life";
        String inputWord2 = "seek";
        String expectedOutput = "None";
        String actualOutput = main.queryBridgeWords(inputWord1, inputWord2);
        assertTrue(actualOutput.equals(expectedOutput));
    }

    @Test
    public void queryBridgeWords_test5() {
        String inputWord1 = "life";
        String inputWord2 = "new";
        String expectedOutput = "None";
        String actualOutput = main.queryBridgeWords(inputWord1, inputWord2);
        assertTrue(actualOutput.equals(expectedOutput));
    }

    @Test
    public void randomWalk_test1() {
        main.randomWalk();
    }

    @Test
    public void randomWalk_test2() {
        main.randomWalk();
    }

    @Test
    public void randomWalk_test3() {
        main.randomWalk();
    }

    @Test
    public void test4() {
        String Source_Word = "new";
        String Target_Word = "out";
        main.getShortestPath(Source_Word, Target_Word);
    }
}
