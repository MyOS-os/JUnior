package junior.ast;

public final class LetStatement implements Statement {
  private final String name;
  private final Expression initializer;
  private final int line;
  private final int column;

  public LetStatement(String name, Expression initializer, int line, int column) {
    this.name = name;
    this.initializer = initializer;
    this.line = line;
    this.column = column;
  }

  public String getName() {
    return name;
  }

  public Expression getInitializer() {
    return initializer;
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
