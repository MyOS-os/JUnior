package junior.ast;

import junior.lexer.TokenType;

public final class UnaryExpression implements Expression {
  private final TokenType operator;
  private final Expression operand;
  private final int line;
  private final int column;

  public UnaryExpression(TokenType operator, Expression operand, int line, int column) {
    this.operator = operator;
    this.operand = operand;
    this.line = line;
    this.column = column;
  }

  public TokenType getOperator() {
    return operator;
  }

  public Expression getOperand() {
    return operand;
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
