package junior.ast;

public final class IdentifierExpression implements Expression {
  private final String name;
  private final int line;
  private final int column;

  public IdentifierExpression(String name, int line, int column) {
    this.name = name;
    this.line = line;
    this.column = column;
  }

  public String getName() {
    return name;
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
