package junior.runtime;

import java.util.HashMap;
import java.util.Map;
import junior.ast.BinaryExpression;
import junior.ast.Expression;
import junior.ast.ExpressionStatement;
import junior.ast.GroupExpression;
import junior.ast.IdentifierExpression;
import junior.ast.LetStatement;
import junior.ast.LiteralExpression;
import junior.ast.PrintStatement;
import junior.ast.Program;
import junior.ast.Statement;
import junior.ast.UnaryExpression;

public final class Interpreter {
  private final Map<String, Object> globals = new HashMap<>();
  private final Library library;

  public Interpreter(Library library) {
    this.library = library;
  }

  public void execute(Program program) {
    for (Statement statement : program.getStatements()) {
      executeStatement(statement);
    }
  }

  private void executeStatement(Statement statement) {
    if (statement instanceof PrintStatement) {
      PrintStatement printStatement = (PrintStatement) statement;
      Object value = evaluate(printStatement.getExpression());
      library.print(value == null ? "null" : value.toString());
      return;
    }
    if (statement instanceof LetStatement) {
      LetStatement letStatement = (LetStatement) statement;
      Object value = evaluate(letStatement.getInitializer());
      globals.put(letStatement.getName(), value);
      return;
    }
    if (statement instanceof ExpressionStatement) {
      ExpressionStatement expr = (ExpressionStatement) statement;
      evaluate(expr.getExpression());
    }
  }

  private Object evaluate(Expression expression) {
    if (expression instanceof LiteralExpression) {
      return ((LiteralExpression) expression).getValue();
    }
    if (expression instanceof IdentifierExpression) {
      String name = ((IdentifierExpression) expression).getName();
      return globals.get(name);
    }
    if (expression instanceof GroupExpression) {
      return evaluate(((GroupExpression) expression).getInner());
    }
    if (expression instanceof UnaryExpression) {
      return evaluateUnary((UnaryExpression) expression);
    }
    if (expression instanceof BinaryExpression) {
      return evaluateBinary((BinaryExpression) expression);
    }
    return null;
  }

  private Object evaluateUnary(UnaryExpression expression) {
    Object value = evaluate(expression.getOperand());
    switch (expression.getOperator()) {
      case BANG:
        return !toBoolean(value);
      case MINUS:
        if (value instanceof Number) {
          return -((Number) value).doubleValue();
        }
        return 0;
      default:
        return null;
    }
  }

  private Object evaluateBinary(BinaryExpression expression) {
    Object left = evaluate(expression.getLeft());
    Object right = evaluate(expression.getRight());
    switch (expression.getOperator()) {
      case PLUS:
        if (left instanceof String || right instanceof String) {
          return String.valueOf(left) + String.valueOf(right);
        }
        return toNumber(left) + toNumber(right);
      case MINUS:
        return toNumber(left) - toNumber(right);
      case STAR:
        return toNumber(left) * toNumber(right);
      case SLASH:
        return toNumber(left) / toNumber(right);
      case EQ_EQ:
        return left == null ? right == null : left.equals(right);
      case BANG_EQ:
        return left == null ? right != null : !left.equals(right);
      case LT:
        return toNumber(left) < toNumber(right);
      case LTE:
        return toNumber(left) <= toNumber(right);
      case GT:
        return toNumber(left) > toNumber(right);
      case GTE:
        return toNumber(left) >= toNumber(right);
      case AND_AND:
        return toBoolean(left) && toBoolean(right);
      case OR_OR:
        return toBoolean(left) || toBoolean(right);
      default:
        return null;
    }
  }

  private boolean toBoolean(Object value) {
    if (value instanceof Boolean) {
      return (Boolean) value;
    }
    if (value instanceof Number) {
      return ((Number) value).doubleValue() != 0;
    }
    return value != null;
  }

  private double toNumber(Object value) {
    if (value instanceof Number) {
      return ((Number) value).doubleValue();
    }
    try {
      return Double.parseDouble(String.valueOf(value));
    } catch (NumberFormatException ex) {
      return 0;
    }
  }

  public Object getGlobal(String name) {
    return globals.get(name);
  }

  public Map<String, Object> snapshot() {
    return new HashMap<>(globals);
  }

  public interface Library {
    void print(String value);
  }
}
