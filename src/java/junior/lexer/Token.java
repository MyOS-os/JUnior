package junior.lexer;

import java.util.Objects;

public final class Token {
  private final TokenType type;
  private final String lexeme;
  private final int line;
  private final int column;
  private final int length;
  private final Object literal;
  private final int startIndex;
  private final int endIndex;

  public Token(TokenType type, String lexeme, int line, int column) {
    this(type, lexeme, line, column, lexeme == null ? 0 : lexeme.length(), null, -1, -1);
  }

  public Token(TokenType type, String lexeme, int line, int column, int length,
               Object literal, int startIndex, int endIndex) {
    this.type = Objects.requireNonNull(type, "type");
    this.lexeme = lexeme == null ? "" : lexeme;
    this.line = line;
    this.column = column;
    this.length = Math.max(0, length);
    this.literal = literal;
    this.startIndex = startIndex;
    this.endIndex = endIndex;
  }

  public TokenType getType() {
    return type;
  }

  public String getLexeme() {
    return lexeme;
  }

  public int getLine() {
    return line;
  }

  public int getColumn() {
    return column;
  }

  public int getLength() {
    return length;
  }

  public Object getLiteral() {
    return literal;
  }

  public int getStartIndex() {
    return startIndex;
  }

  public int getEndIndex() {
    return endIndex;
  }

  public boolean hasLiteral() {
    return literal != null;
  }

  public boolean isType(TokenType expected) {
    return type == expected;
  }

  public boolean isKeyword() {
    return type.isKeyword();
  }

  public boolean isOperator() {
    return type.isOperator();
  }

  public boolean isDelimiter() {
    return type.isDelimiter();
  }

  public boolean isLiteralToken() {
    return type.isLiteral();
  }

  public boolean matchesLexeme(String expected) {
    return lexeme.equals(expected);
  }

  public Token withLiteral(Object nextLiteral) {
    return new Token(type, lexeme, line, column, length, nextLiteral, startIndex, endIndex);
  }

  public Token withSpan(int nextStartIndex, int nextEndIndex) {
    return new Token(type, lexeme, line, column, length, literal, nextStartIndex, nextEndIndex);
  }

  public Token withType(TokenType nextType) {
    return new Token(nextType, lexeme, line, column, length, literal, startIndex, endIndex);
  }

  public TokenSpan span() {
    return new TokenSpan(line, column, length, startIndex, endIndex);
  }

  public String debugString() {
    StringBuilder builder = new StringBuilder();
    builder.append(type.getName()).append("('").append(lexeme).append("')");
    builder.append(" @ ").append(line).append(":").append(column);
    if (hasLiteral()) {
      builder.append(" literal=").append(literal);
    }
    return builder.toString();
  }

  @Override
  public String toString() {
    return "Token{" +
        "type=" + type +
        ", lexeme='" + lexeme + '\'' +
        ", line=" + line +
        ", column=" + column +
        ", length=" + length +
        ", literal=" + literal +
        ", startIndex=" + startIndex +
        ", endIndex=" + endIndex +
        '}';
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof Token)) {
      return false;
    }
    Token token = (Token) other;
    return line == token.line
        && column == token.column
        && length == token.length
        && startIndex == token.startIndex
        && endIndex == token.endIndex
        && type == token.type
        && Objects.equals(lexeme, token.lexeme)
        && Objects.equals(literal, token.literal);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, lexeme, line, column, length, literal, startIndex, endIndex);
  }

  public static Builder builder(TokenType type, String lexeme, int line, int column) {
    return new Builder(type, lexeme, line, column);
  }

  public static final class Builder {
    private final TokenType type;
    private final String lexeme;
    private final int line;
    private final int column;
    private int length;
    private Object literal;
    private int startIndex = -1;
    private int endIndex = -1;

    private Builder(TokenType type, String lexeme, int line, int column) {
      this.type = Objects.requireNonNull(type, "type");
      this.lexeme = lexeme == null ? "" : lexeme;
      this.line = line;
      this.column = column;
      this.length = this.lexeme.length();
    }

    public Builder length(int length) {
      this.length = Math.max(0, length);
      return this;
    }

    public Builder literal(Object literal) {
      this.literal = literal;
      return this;
    }

    public Builder startIndex(int startIndex) {
      this.startIndex = startIndex;
      return this;
    }

    public Builder endIndex(int endIndex) {
      this.endIndex = endIndex;
      return this;
    }

    public Builder span(int startIndex, int endIndex) {
      this.startIndex = startIndex;
      this.endIndex = endIndex;
      return this;
    }

    public Token build() {
      return new Token(type, lexeme, line, column, length, literal, startIndex, endIndex);
    }
  }
}

final class TokenSpan {
  private final int line;
  private final int column;
  private final int length;
  private final int startIndex;
  private final int endIndex;

  TokenSpan(int line, int column, int length, int startIndex, int endIndex) {
    this.line = line;
    this.column = column;
    this.length = length;
    this.startIndex = startIndex;
    this.endIndex = endIndex;
  }

  public int getLine() {
    return line;
  }

  public int getColumn() {
    return column;
  }

  public int getLength() {
    return length;
  }

  public int getStartIndex() {
    return startIndex;
  }

  public int getEndIndex() {
    return endIndex;
  }

  public String asRange() {
    return line + ":" + column + "+" + length;
  }

  @Override
  public String toString() {
    return "TokenSpan{" +
        "line=" + line +
        ", column=" + column +
        ", length=" + length +
        ", startIndex=" + startIndex +
        ", endIndex=" + endIndex +
        '}';
  }
}
