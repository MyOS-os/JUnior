package junior.ast;

public final class LiteralExpression implements Expression {
  private final Object value;
  private final int line;
  private final int column;

  public LiteralExpression(Object value, int line, int column) {
    this.value = value;
    this.line = line;
    this.column = column;
  }

  public Object getValue() {
    return value;
  }

  @Override
  public int getLine() {
    return line;
  }

  @Override
  public int getColumn() {
    return column;
  }
}
