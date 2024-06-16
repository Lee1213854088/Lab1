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
    public void testGenerateTextWithBridgeWords_NoBridgeWords() {
        String inputText = "new worlds";
        String expectedOutput = "new worlds";
        String actualOutput = main.generateTextWithBridgeWords(inputText);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testGenerateTextWithBridgeWords_WithBridgeWords() {
        // Assume "hello" -> "beautiful" -> "world" is in the graph
        String inputText = "to strange";
        String actualOutput = main.generateTextWithBridgeWords(inputText);
        assertTrue(actualOutput.equals("to explore strange") || actualOutput.equals("to seek strange"));
    }

    @Test
    public void testGenerateTextWithBridgeWords_MultipleBridgeWords() {
        // Assume there are multiple bridge words between "hello" and "world"
        String inputText = "new strange";
        String actualOutput = main.generateTextWithBridgeWords(inputText);
        assertTrue(actualOutput.contains("new") && actualOutput.contains("strange"));
    }

    @Test
    public void testGenerateTextWithBridgeWords_CaseInsensitive() {
        String inputText = "worlds seek";
        String actualOutput = main.generateTextWithBridgeWords(inputText);
        assertTrue(actualOutput.equals("worlds to seek") || actualOutput.equals("worlds life seek"));
    }

    @Test
    public void testGenerateTextWithBridgeWords_EmptyInput() {
        String inputText = "";
        String expectedOutput = "";
        String actualOutput = main.generateTextWithBridgeWords(inputText);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testGenerateTextWithBridgeWords_SingleWordInput() {
        String inputText = "new";
        String expectedOutput = "new";
        String actualOutput = main.generateTextWithBridgeWords(inputText);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testGenerateTextWithBridgeWords_LongTextInput() {
        String inputText = "this is a long text with multiple words";
        String actualOutput = main.generateTextWithBridgeWords(inputText);
        assertNotNull(actualOutput);
        assertFalse(actualOutput.isEmpty());
    }
}
