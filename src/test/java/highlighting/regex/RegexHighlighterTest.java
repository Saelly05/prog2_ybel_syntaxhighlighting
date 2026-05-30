package highlighting.regex;

import highlighting.core.HighlightRegion;
import highlighting.presets.MiniJavaColours;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RegexHighlighterTest {

    // Unser Highlighter, den wir für alle Tests verwenden
    private final RegexHighlighter highlighter = new RegexHighlighter();

    @Test
    void testNoMatches() {
        // Testet Leerstring und Text ohne gültige Token
        assertTrue(highlighter.computeRegions("").isEmpty(), "Sollte bei leerem String nichts finden");
        assertTrue(highlighter.computeRegions("just some plain text").isEmpty(), "Sollte keine Regionen finden");
    }

    @Test
    void testSimpleNoOverlap() {
        // "public" [0,6), "class" [7,12) -> Keine Überlappung
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
        // Zwei Regionen, die direkt aneinandergrenzen: [0,3) und [3,6)
        // "\"a\"" ist ein String, "//b" ist ein Zeilenkommentar
        List<HighlightRegion> regions = highlighter.computeRegions("\"a\"//b");

        assertEquals(2, regions.size(), "Beide direkt aufeinanderfolgenden Regionen müssen bleiben");

        // String
        assertEquals(0, regions.get(0).start());
        assertEquals(3, regions.get(0).end());

        // Kommentar (darf nicht wegen Überlappung gelöscht werden, da start >= maxEnd)
        assertEquals(3, regions.get(1).start());
        assertEquals(6, regions.get(1).end());
    }

    @Test
    void testOverlapKeywordInComment() {
        // "public" fängt bei 3 an, aber der Kommentar fängt bei 0 an und verdeckt es komplett.
        List<HighlightRegion> regions = highlighter.computeRegions("// public class");

        assertEquals(1, regions.size(), "Der Kommentar sollte die Keywords verschlucken");
        assertEquals(0, regions.get(0).start());
        assertEquals(15, regions.get(0).end());
        assertEquals(MiniJavaColours.LINE_COMMENT_COLOUR, regions.get(0).colour());
    }

    @Test
    void testOverlapJavadocVsBlockComment() {
        // Javadoc (/** ... */) und normaler Blockkommentar (/* ... */) würden hier beide matchen.
        // Da Javadoc in unserer Liste weiter oben steht, sammelt collectMatches ihn zuerst.
        // Bei der Konfliktauflösung gewinnt dann der Javadoc-Match, weil er früher an die Reihe kommt.
        List<HighlightRegion> regions = highlighter.computeRegions("/** doc */");

        assertEquals(1, regions.size());
        assertEquals(MiniJavaColours.JAVADOC_COMMENT_COLOUR, regions.get(0).colour(), "Javadoc muss Vorrang vor normalem Blockkommentar haben");
    }
}
