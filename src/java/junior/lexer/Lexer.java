package junior.lexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Lexer {
  private static final Map<String, TokenType> KEYWORDS = new HashMap<>();
  private static final Map<String, TokenType> SYMBOLS = new HashMap<>();
  private static final List<String> MULTI_CHAR_SYMBOLS = new ArrayList<>();

  static {
    for (TokenType type : TokenType.keywords()) {
      if (type.getLexeme() != null) {
        KEYWORDS.put(type.getLexeme(), type);
      }
    }
    for (TokenType type : TokenType.operators()) {
      if (type.getLexeme() != null) {
        SYMBOLS.put(type.getLexeme(), type);
      }
    }
    for (TokenType type : TokenType.delimiters()) {
      if (type.getLexeme() != null) {
        SYMBOLS.put(type.getLexeme(), type);
      }
    }
    for (TokenType type : TokenType.operators()) {
      if (type.getLexeme() != null && type.getLexeme().length() > 1) {
        MULTI_CHAR_SYMBOLS.add(type.getLexeme());
      }
    }
    MULTI_CHAR_SYMBOLS.sort((left, right) -> Integer.compare(right.length(), left.length()));
  }

  private final String source;
  private int index;
  private int line;
  private int column;

  public Lexer(String source) {
    this.source = source == null ? "" : source;
    this.index = 0;
    this.line = 1;
    this.column = 1;
  }

  public List<Token> tokenize() {
    List<Token> tokens = new ArrayList<>();
    while (!isAtEnd()) {
      skipWhitespaceAndComments();
      if (isAtEnd()) {
        break;
      }
      int startLine = line;
      int startColumn = column;
      char current = peek();
      if (isIdentifierStart(current)) {
        tokens.add(readIdentifierOrKeyword(startLine, startColumn));
      } else if (Character.isDigit(current)) {
        tokens.add(readNumber(startLine, startColumn));
      } else if (current == '"' || current == '\'') {
        tokens.add(readString(startLine, startColumn));
      } else {
        tokens.add(readSymbol(startLine, startColumn));
      }
    }
    tokens.add(new Token(TokenType.EOF, "", line, column));
    return tokens;
  }

  private Token readIdentifierOrKeyword(int startLine, int startColumn) {
    StringBuilder builder = new StringBuilder();
    while (!isAtEnd() && isIdentifierPart(peek())) {
      builder.append(advance());
    }
    String lexeme = builder.toString();
    TokenType type = KEYWORDS.get(lexeme);
    return new Token(type == null ? TokenType.IDENTIFIER : type, lexeme, startLine, startColumn);
  }

  private Token readNumber(int startLine, int startColumn) {
    StringBuilder builder = new StringBuilder();
    if (peek() == '0' && (peekNext() == 'x' || peekNext() == 'X')) {
      builder.append(advance());
      builder.append(advance());
      readWhile(builder, this::isHexDigit);
      return new Token(TokenType.NUMBER, builder.toString(), startLine, startColumn);
    }
    if (peek() == '0' && (peekNext() == 'b' || peekNext() == 'B')) {
      builder.append(advance());
      builder.append(advance());
      readWhile(builder, this::isBinaryDigit);
      return new Token(TokenType.NUMBER, builder.toString(), startLine, startColumn);
    }
    while (!isAtEnd() && Character.isDigit(peek())) {
      builder.append(advance());
    }
    if (!isAtEnd() && peek() == '_') {
      readWhile(builder, this::isNumberPart);
    }
    return new Token(TokenType.NUMBER, builder.toString(), startLine, startColumn);
  }

  private Token readString(int startLine, int startColumn) {
    char quote = advance();
    StringBuilder builder = new StringBuilder();
    while (!isAtEnd() && peek() != quote) {
      char current = advance();
      if (current == '\\' && !isAtEnd()) {
        char escaped = advance();
        builder.append(readEscape(escaped));
      } else {
        builder.append(current);
      }
    }
    if (isAtEnd()) {
      throw LexerException.unterminatedString(startLine, startColumn);
    }
    advance();
    return new Token(TokenType.STRING, builder.toString(), startLine, startColumn);
  }

  private char readEscape(char escaped) {
    switch (escaped) {
      case 'n':
        return '\n';
      case 't':
        return '\t';
      case 'r':
        return '\r';
      case '"':
        return '"';
      case '\'':
        return '\'';
      case '\\':
        return '\\';
      default:
        return escaped;
    }
  }

  private Token readSymbol(int startLine, int startColumn) {
    String matched = matchMultiCharSymbol();
    if (matched != null) {
      TokenType type = SYMBOLS.get(matched);
      if (type == null) {
        throw LexerException.unknownSymbol(matched, startLine, startColumn);
      }
      return new Token(type, matched, startLine, startColumn);
    }
    char symbol = advance();
    String lexeme = String.valueOf(symbol);
    TokenType type = SYMBOLS.get(lexeme);
    if (type == null) {
      throw LexerException.unknownSymbol(lexeme, startLine, startColumn);
    }
    return new Token(type, lexeme, startLine, startColumn);
  }

  private String matchMultiCharSymbol() {
    for (String symbol : MULTI_CHAR_SYMBOLS) {
      if (matches(symbol)) {
        for (int i = 0; i < symbol.length(); i++) {
          advance();
        }
        return symbol;
      }
    }
    return null;
  }

  private void skipWhitespaceAndComments() {
    while (!isAtEnd()) {
      char current = peek();
      if (current == ' ' || current == '\t' || current == '\r' || current == '\n') {
        advance();
        continue;
      }
      if (current == '/' && peekNext() == '/') {
        while (!isAtEnd() && peek() != '\n') {
          advance();
        }
        continue;
      }
      break;
    }
  }

  private void readWhile(StringBuilder builder, CharPredicate predicate) {
    while (!isAtEnd() && predicate.test(peek())) {
      builder.append(advance());
    }
  }

  private boolean isHexDigit(char value) {
    return Character.isDigit(value) || (value >= 'a' && value <= 'f') || (value >= 'A' && value <= 'F');
  }

  private boolean isBinaryDigit(char value) {
    return value == '0' || value == '1';
  }

  private boolean isNumberPart(char value) {
    return Character.isDigit(value) || value == '_';
  }

  private boolean matches(String text) {
    if (index + text.length() > source.length()) {
      return false;
    }
    return source.startsWith(text, index);
  }

  private char advance() {
    char current = source.charAt(index);
    index++;
    if (current == '\n') {
      line++;
      column = 1;
    } else {
      column++;
    }
    return current;
  }

  private char peek() {
    if (isAtEnd()) {
      return '\0';
    }
    return source.charAt(index);
  }

  private char peekNext() {
    if (index + 1 >= source.length()) {
      return '\0';
    }
    return source.charAt(index + 1);
  }

  private boolean isAtEnd() {
    return index >= source.length();
  }

  private boolean isIdentifierStart(char value) {
    return Character.isLetter(value) || value == '_';
  }

  private boolean isIdentifierPart(char value) {
    return isIdentifierStart(value) || Character.isDigit(value);
  }

  private interface CharPredicate {
    boolean test(char value);
  }
}
