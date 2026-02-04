package junior.parser;

public class ParserException extends RuntimeException {
  private final int line;
  private final int column;

  public ParserException(String message, int line, int column) {
    super(message);
    this.line = line;
    this.column = column;
  }

  public int getLine() {
    return line;
  }

  public int getColumn() {
    return column;
  }

  @Override
  public String toString() {
    return getMessage() + " (строка " + line + ", колонка " + column + ")";
  }
}
