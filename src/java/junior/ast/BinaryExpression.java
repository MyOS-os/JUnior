package junior.ast;

import junior.lexer.TokenType;

public final class BinaryExpression implements Expression {
  private final Expression left;
  private final TokenType operator;
  private final Expression right;
  private final int line;
  private final int column;

  public BinaryExpression(Expression left, TokenType operator, Expression right, int line, int column) {
    this.left = left;
    this.operator = operator;
    this.right = right;
    this.line = line;
    this.column = column;
  }

  public Expression getLeft() {
    return left;
  }

  public TokenType getOperator() {
    return operator;
  }

  public Expression getRight() {
    return right;
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
