package junior.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Program implements Node {
  private final List<Statement> statements;
  private final int line;
  private final int column;

  public Program(List<Statement> statements, int line, int column) {
    this.statements = statements == null ? new ArrayList<>() : new ArrayList<>(statements);
    this.line = line;
    this.column = column;
  }

  public List<Statement> getStatements() {
    return Collections.unmodifiableList(statements);
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
