package highlighting.presets;

import static org.junit.jupiter.api.Assertions.*;

import highlighting.core.HighlightRegion;
import highlighting.regex.Token;
import java.util.List;
import org.junit.jupiter.api.Test;

class MiniJavaTokensTest {

  // Wir holen uns die Liste aus unserer Implementierung
  private final List<Token> tokens = MiniJavaTokens.defaultTokens();

  // Zur besseren Lesbarkeit weisen wir die Token anhand ihres Index zu
  // (Achtung: Wenn du die Reihenfolge in MiniJavaTokens änderst, musst du die Indizes hier
  // anpassen!)
  private final Token javadocToken = tokens.get(0);
  private final Token blockCommentToken = tokens.get(1);
  private final Token lineCommentToken = tokens.get(2);
  private final Token stringToken = tokens.get(3);
  private final Token annotationToken = tokens.get(5);
  private final Token keywordToken = tokens.get(6);

  @Test
  void testKeywordsAndBoundaries() {
    // Prüft: Treffer am Anfang, in der Mitte, am Ende und KEIN Treffer bei Teilwörtern (newVar)
    String text = "public class Test { private int newVar = 0; return; }";
    List<HighlightRegion> matches = keywordToken.test(text);

    assertEquals(4, matches.size(), "Sollte genau 4 Keywords finden (newVar wird ignoriert)");

    // 1. "public" (Am Anfang)
    assertEquals(0, matches.get(0).start());
    assertEquals(6, matches.get(0).end());

    // 2. "class"
    assertEquals(7, matches.get(1).start());
    assertEquals(12, matches.get(1).end());

    // 3. "private" (In der Mitte)
    assertEquals(20, matches.get(2).start());
    assertEquals(27, matches.get(2).end());

    // 4. "return" (Am Ende des relevanten Bereichs)
    assertEquals(44, matches.get(3).start());
    assertEquals(50, matches.get(3).end());
  }

  @Test
  void testStringsWithCommentFakes() {
    // Prüft Grenzfall: String enthält Zeichen, die wie ein Kommentar aussehen
    String text = "String s = \"/* fake */ // comment\";";
    List<HighlightRegion> matches = stringToken.test(text);

    assertEquals(1, matches.size(), "Sollte den gesamten String als einen Treffer erkennen");
    assertEquals(11, matches.get(0).start());
    assertEquals(
        34, matches.get(0).end()); // Index 11 bis 34 umfasst genau "\"/* fake */ // comment\""
  }

  @Test
  void testNoMatch() {
    // Prüft das Verhalten, wenn nichts gefunden werden soll
    String text = "int a = 5; // just a variable";

    assertTrue(keywordToken.test(text).isEmpty(), "Darf hier keine Keywords finden");
    assertTrue(stringToken.test(text).isEmpty(), "Darf hier keine Strings finden");
    assertTrue(javadocToken.test(text).isEmpty(), "Darf hier kein Javadoc finden");
  }

  @Test
  void testAnnotations() {
    // Prüft: Annotation am Textanfang und mit vorangestelltem Leerzeichen/Zeilenumbruch
    String text = "@Override\n  @Nullable Object o;";
    List<HighlightRegion> matches = annotationToken.test(text);

    assertEquals(2, matches.size());

    // "@Override"
    assertEquals(0, matches.get(0).start());
    assertEquals(9, matches.get(0).end());

    // "@Nullable"
    assertEquals(12, matches.get(1).start());
    assertEquals(21, matches.get(1).end());
  }

  @Test
  void testCommentsAreDifferentiated() {
    // Prüft, ob ein Javadoc-Kommentar richtig erkannt wird und NICHT vom normalen Blockkommentar
    // geschluckt wird
    String text = "/** doc */ /* block */";

    List<HighlightRegion> javadocMatches = javadocToken.test(text);
    assertEquals(1, javadocMatches.size(), "Sollte nur den Javadoc-Teil finden");
    assertEquals(0, javadocMatches.get(0).start());
    assertEquals(10, javadocMatches.get(0).end());

    List<HighlightRegion> blockMatches = blockCommentToken.test(text);
    // WICHTIG: Der blockCommentToken isoliert betrachtet würde theoretisch BEIDE finden.
    // Dass Javadoc priorisiert wird, klären wir später im RegexHighlighter.
    // Für den puren Token-Test ist es okay, wenn das Token an sich funktioniert.
    assertFalse(blockMatches.isEmpty(), "Blockkommentar-Token muss greifen");
  }
}
