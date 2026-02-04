package junior.ast;

public final class PrintStatement implements Statement {
  private final Expression expression;
  private final int line;
  private final int column;

  public PrintStatement(Expression expression, int line, int column) {
    this.expression = expression;
    this.line = line;
    this.column = column;
  }

  public Expression getExpression() {
    return expression;
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
