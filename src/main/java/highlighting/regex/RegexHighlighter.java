package highlighting.regex;

import highlighting.core.HighlightRegion;
import highlighting.core.SyntaxHighlighter;
import highlighting.presets.MiniJavaTokens;

import java.util.ArrayList;
import java.util.List;

public class RegexHighlighter extends SyntaxHighlighter {


  @Override
  public List<HighlightRegion> collectMatches(String text) {
      List<HighlightRegion> allMatches = new ArrayList<>();

      // Alle Tokens durchgehen und unabhängig voneinander auf den Text anwenden
      for (Token token : MiniJavaTokens.defaultTokens()) {
          allMatches.addAll(token.test(text));
      }

      return allMatches;

  }


  @Override
  public List<HighlightRegion> resolveConflicts(List<HighlightRegion> regions) {
      List<HighlightRegion> resolved = new ArrayList<>();

      // Da die Liste bereits durch normalize() nach Startposition sortiert wurde,
      // reicht es, sich das Ende der zuletzt eingefügten Region zu merken.
      int maxEnd = -1;

      for (HighlightRegion region : regions) {
          // Zwei Intervalle [R.start, R.end) und [S.start, S.end) überlappen nicht,
          // wenn die neue Region erst am oder nach dem Ende der alten beginnt.
          if (region.start() >= maxEnd) {
              resolved.add(region);
              maxEnd = region.end(); // Neues "Ende" setzen
          }
      }

      return resolved;
  }
}
