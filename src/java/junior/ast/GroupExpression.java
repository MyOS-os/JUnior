package junior.ast;

public final class GroupExpression implements Expression {
  private final Expression inner;
  private final int line;
  private final int column;

  public GroupExpression(Expression inner, int line, int column) {
    this.inner = inner;
    this.line = line;
    this.column = column;
  }

  public Expression getInner() {
    return inner;
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
