package junior.lexer;

public class LexerException extends RuntimeException {
  public enum Kind {
    UNKNOWN_SYMBOL,
    UNTERMINATED_STRING,
    INVALID_NUMBER,
    INVALID_ESCAPE,
    UNEXPECTED_EOF,
    INTERNAL
  }

  private final Kind kind;
  private final int line;
  private final int column;
  private final String lexeme;
  private final String context;

  public LexerException(String message) {
    this(Kind.INTERNAL, message, -1, -1, null, null, null);
  }

  public LexerException(Kind kind, String message, int line, int column, String lexeme) {
    this(kind, message, line, column, lexeme, null, null);
  }

  public LexerException(Kind kind, String message, int line, int column, String lexeme, String context) {
    this(kind, message, line, column, lexeme, context, null);
  }

  public LexerException(Kind kind, String message, int line, int column, String lexeme, String context,
                        Throwable cause) {
    super(message, cause);
    this.kind = kind == null ? Kind.INTERNAL : kind;
    this.line = line;
    this.column = column;
    this.lexeme = lexeme;
    this.context = context;
  }

  public Kind getKind() {
    return kind;
  }

  public int getLine() {
    return line;
  }

  public int getColumn() {
    return column;
  }

  public String getLexeme() {
    return lexeme;
  }

  public String getContext() {
    return context;
  }

  public boolean hasPosition() {
    return line > 0 && column > 0;
  }

  public boolean hasLexeme() {
    return lexeme != null && !lexeme.isEmpty();
  }

  public boolean hasContext() {
    return context != null && !context.isEmpty();
  }

  public String format() {
    StringBuilder builder = new StringBuilder();
    builder.append(kind).append(": ").append(getMessage());
    if (hasPosition()) {
      builder.append(" (строка ").append(line).append(", колонка ").append(column).append(")");
    }
    if (hasLexeme()) {
      builder.append(" -> '").append(lexeme).append("'");
    }
    if (hasContext()) {
      builder.append(" | ").append(context);
    }
    return builder.toString();
  }

  public String formatMultiline() {
    StringBuilder builder = new StringBuilder();
    builder.append(kind).append(':').append('\n');
    builder.append("  message: ").append(getMessage()).append('\n');
    if (hasPosition()) {
      builder.append("  line: ").append(line).append('\n');
      builder.append("  column: ").append(column).append('\n');
    }
    if (hasLexeme()) {
      builder.append("  lexeme: ").append(lexeme).append('\n');
    }
    if (hasContext()) {
      builder.append("  context: ").append(context).append('\n');
    }
    return builder.toString();
  }

  public LexerException withContext(String context) {
    return new LexerException(kind, getMessage(), line, column, lexeme, context, getCause());
  }

  public LexerException withLexeme(String lexeme) {
    return new LexerException(kind, getMessage(), line, column, lexeme, context, getCause());
  }

  public LexerException withPosition(int line, int column) {
    return new LexerException(kind, getMessage(), line, column, lexeme, context, getCause());
  }

  public static LexerException unknownSymbol(String symbol, int line, int column) {
    String message = "Неизвестный символ";
    return new LexerException(Kind.UNKNOWN_SYMBOL, message, line, column, symbol);
  }

  public static LexerException unterminatedString(int line, int column) {
    String message = "Незакрытая строка";
    return new LexerException(Kind.UNTERMINATED_STRING, message, line, column, null);
  }

  public static LexerException invalidNumber(String lexeme, int line, int column) {
    String message = "Некорректное число";
    return new LexerException(Kind.INVALID_NUMBER, message, line, column, lexeme);
  }

  public static LexerException invalidEscape(String escape, int line, int column) {
    String message = "Некорректная escape-последовательность";
    return new LexerException(Kind.INVALID_ESCAPE, message, line, column, escape);
  }

  public static LexerException unexpectedEof(int line, int column) {
    String message = "Неожиданный конец файла";
    return new LexerException(Kind.UNEXPECTED_EOF, message, line, column, null);
  }

  public static LexerException internal(String message, Throwable cause) {
    return new LexerException(Kind.INTERNAL, message, -1, -1, null, null, cause);
  }

  public static Builder builder(Kind kind, String message) {
    return new Builder(kind, message);
  }

  @Override
  public String toString() {
    return format();
  }

  public static final class Builder {
    private final Kind kind;
    private final String message;
    private int line = -1;
    private int column = -1;
    private String lexeme;
    private String context;
    private Throwable cause;

    private Builder(Kind kind, String message) {
      this.kind = kind == null ? Kind.INTERNAL : kind;
      this.message = message;
    }

    public Builder line(int line) {
      this.line = line;
      return this;
    }

    public Builder column(int column) {
      this.column = column;
      return this;
    }

    public Builder position(int line, int column) {
      this.line = line;
      this.column = column;
      return this;
    }

    public Builder lexeme(String lexeme) {
      this.lexeme = lexeme;
      return this;
    }

    public Builder context(String context) {
      this.context = context;
      return this;
    }

    public Builder cause(Throwable cause) {
      this.cause = cause;
      return this;
    }

    public LexerException build() {
      return new LexerException(kind, message, line, column, lexeme, context, cause);
    }
  }
}
