package highlighting.presets;

import highlighting.regex.Token;

import java.util.List;
import java.util.regex.Pattern;

public final class MiniJavaTokens {

    public static List<Token> defaultTokens() {
        return List.of(
            
            Token.of(Pattern.compile("(?s)/\\*\\*.*?\\*/"), MiniJavaColours.JAVADOC_COMMENT_COLOUR),

            
            Token.of(Pattern.compile("(?s)/\\*.*?\\*/"), MiniJavaColours.BLOCK_COMMENT_COLOUR),

            
            Token.of(Pattern.compile("//.*"), MiniJavaColours.LINE_COMMENT_COLOUR),

            
            Token.of(Pattern.compile("\"([^\"\\\\]|\\\\.)*\""), MiniJavaColours.STRING_LITERAL_COLOUR),

            
            Token.of(Pattern.compile("'([^'\\\\]|\\\\.)'"), MiniJavaColours.CHAR_LITERAL_COLOUR),

            
            Token.of(Pattern.compile("@[a-zA-Z\\-]+"), MiniJavaColours.ANNOTATION_COLOUR),

            
            Token.of(Pattern.compile("\\b(package|import|class|public|private|final|return|null|new)\\b"), MiniJavaColours.KEYWORD_COLOUR)
        );
    }
}
