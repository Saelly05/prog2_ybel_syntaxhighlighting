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

      
      for (Token token : MiniJavaTokens.defaultTokens()) {
          allMatches.addAll(token.test(text));
      }

      return allMatches;

  }


  @Override
  public List<HighlightRegion> resolveConflicts(List<HighlightRegion> regions) {
      List<HighlightRegion> resolved = new ArrayList<>();

      
      int maxEnd = -1;

      for (HighlightRegion region : regions) {
         
          if (region.start() >= maxEnd) {
              resolved.add(region);
              maxEnd = region.end(); 
          }
      }

      return resolved;
  }
}
