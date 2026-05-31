package highlighting.regex;

import highlighting.core.HighlightRegion;
import highlighting.presets.MiniJavaColours;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RegexHighlighterTest {

   
    private final RegexHighlighter highlighter = new RegexHighlighter();

    @Test
    void testNoMatches() {
        
        assertTrue(highlighter.computeRegions("").isEmpty(), "Sollte bei leerem String nichts finden");
        assertTrue(highlighter.computeRegions("just some plain text").isEmpty(), "Sollte keine Regionen finden");
    }

    @Test
    void testSimpleNoOverlap() {
       
        List<HighlightRegion> regions = highlighter.computeRegions("public class");

        assertEquals(2, regions.size());

        assertEquals(0, regions.get(0).start());
        assertEquals(6, regions.get(0).end());
        assertEquals(MiniJavaColours.KEYWORD_COLOUR, regions.get(0).colour());

        assertEquals(7, regions.get(1).start());
        assertEquals(12, regions.get(1).end());
        assertEquals(MiniJavaColours.KEYWORD_COLOUR, regions.get(1).colour());
    }

    @Test
    void testConsecutiveRegions() {
        
        List<HighlightRegion> regions = highlighter.computeRegions("\"a\"//b");

        assertEquals(2, regions.size(), "Beide direkt aufeinanderfolgenden Regionen müssen bleiben");

       
        assertEquals(0, regions.get(0).start());
        assertEquals(3, regions.get(0).end());

        
        assertEquals(3, regions.get(1).start());
        assertEquals(6, regions.get(1).end());
    }

    @Test
    void testOverlapKeywordInComment() {
       
        List<HighlightRegion> regions = highlighter.computeRegions("// public class");

        assertEquals(1, regions.size(), "Der Kommentar sollte die Keywords verschlucken");
        assertEquals(0, regions.get(0).start());
        assertEquals(15, regions.get(0).end());
        assertEquals(MiniJavaColours.LINE_COMMENT_COLOUR, regions.get(0).colour());
    }

    @Test
    void testOverlapJavadocVsBlockComment() {
       
        List<HighlightRegion> regions = highlighter.computeRegions("/** doc */");

        assertEquals(1, regions.size());
        assertEquals(MiniJavaColours.JAVADOC_COMMENT_COLOUR, regions.get(0).colour(), "Javadoc muss Vorrang vor normalem Blockkommentar haben");
    }
}
