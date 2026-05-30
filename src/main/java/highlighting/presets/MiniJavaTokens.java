package highlighting.presets;

import highlighting.regex.Token;

import java.util.List;
import java.util.regex.Pattern;

public final class MiniJavaTokens {

    public static List<Token> defaultTokens() {
        return List.of(
            // Index 0: Javadoc
            Token.of(Pattern.compile("(?s)/\\*\\*.*?\\*/"), MiniJavaColours.JAVADOC_COMMENT_COLOUR),

            // Index 1: Block Comment
            Token.of(Pattern.compile("(?s)/\\*.*?\\*/"), MiniJavaColours.BLOCK_COMMENT_COLOUR),

            // Index 2: Line Comment
            Token.of(Pattern.compile("//.*"), MiniJavaColours.LINE_COMMENT_COLOUR),

            // Index 3: String (Das war vorher verrutscht!)
            Token.of(Pattern.compile("\"([^\"\\\\]|\\\\.)*\""), MiniJavaColours.STRING_LITERAL_COLOUR),

            // Index 4: Char
            Token.of(Pattern.compile("'([^'\\\\]|\\\\.)'"), MiniJavaColours.CHAR_LITERAL_COLOUR),

            // Index 5: Annotation
            Token.of(Pattern.compile("@[a-zA-Z\\-]+"), MiniJavaColours.ANNOTATION_COLOUR),

            // Index 6: Keyword
            Token.of(Pattern.compile("\\b(package|import|class|public|private|final|return|null|new)\\b"), MiniJavaColours.KEYWORD_COLOUR)
        );
    }
}
