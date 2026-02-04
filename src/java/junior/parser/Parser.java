package junior.parser;

import java.util.ArrayList;
import java.util.List;
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
import junior.lexer.Token;
import junior.lexer.TokenType;

public final class Parser {
  private final TokenStream tokens;

  public Parser(List<Token> tokens) {
    this.tokens = new TokenStream(tokens);
  }

  public Program parseProgram() {
    List<Statement> statements = new ArrayList<>();
    while (!tokens.isAtEnd()) {
      statements.add(parseStatement());
    }
    return new Program(statements, 1, 1);
  }

  private Statement parseStatement() {
    Token current = tokens.peek();
    if (tokens.match(TokenType.LET)) {
      return parseLet(current);
    }
    if (tokens.match(TokenType.PRINT)) {
      return parsePrint(current);
    }
    return parseExpressionStatement();
  }

  private Statement parseLet(Token keyword) {
    Token name = tokens.consume(TokenType.IDENTIFIER, "Ожидался идентификатор после let");
    tokens.consume(TokenType.EQ, "Ожидался '=' после имени переменной");
    Expression initializer = parseExpression();
    tokens.consume(TokenType.SEMICOLON, "Ожидалась ';' после выражения");
    return new LetStatement(name.getLexeme(), initializer, keyword.getLine(), keyword.getColumn());
  }

  private Statement parsePrint(Token keyword) {
    tokens.consume(TokenType.L_PAREN, "Ожидалась '(' после print");
    Expression value = parseExpression();
    tokens.consume(TokenType.R_PAREN, "Ожидалась ')' после выражения");
    tokens.consume(TokenType.SEMICOLON, "Ожидалась ';' после print");
    return new PrintStatement(value, keyword.getLine(), keyword.getColumn());
  }

  private Statement parseExpressionStatement() {
    Token start = tokens.peek();
    Expression value = parseExpression();
    tokens.consume(TokenType.SEMICOLON, "Ожидалась ';' после выражения");
    return new ExpressionStatement(value, start.getLine(), start.getColumn());
  }

  private Expression parseExpression() {
    return parseEquality();
  }

  private Expression parseEquality() {
    Expression expression = parseComparison();
    while (tokens.match(TokenType.EQ_EQ) || tokens.match(TokenType.BANG_EQ)) {
      Token operator = tokens.previous();
      Expression right = parseComparison();
      expression = new BinaryExpression(expression, operator.getType(), right,
          operator.getLine(), operator.getColumn());
    }
    return expression;
  }

  private Expression parseComparison() {
    Expression expression = parseTerm();
    while (tokens.match(TokenType.LT) || tokens.match(TokenType.LTE)
        || tokens.match(TokenType.GT) || tokens.match(TokenType.GTE)) {
      Token operator = tokens.previous();
      Expression right = parseTerm();
      expression = new BinaryExpression(expression, operator.getType(), right,
          operator.getLine(), operator.getColumn());
    }
    return expression;
  }

  private Expression parseTerm() {
    Expression expression = parseFactor();
    while (tokens.match(TokenType.PLUS) || tokens.match(TokenType.MINUS)) {
      Token operator = tokens.previous();
      Expression right = parseFactor();
      expression = new BinaryExpression(expression, operator.getType(), right,
          operator.getLine(), operator.getColumn());
    }
    return expression;
  }

  private Expression parseFactor() {
    Expression expression = parseUnary();
    while (tokens.match(TokenType.STAR) || tokens.match(TokenType.SLASH)) {
      Token operator = tokens.previous();
      Expression right = parseUnary();
      expression = new BinaryExpression(expression, operator.getType(), right,
          operator.getLine(), operator.getColumn());
    }
    return expression;
  }

  private Expression parseUnary() {
    if (tokens.match(TokenType.BANG) || tokens.match(TokenType.MINUS)) {
      Token operator = tokens.previous();
      Expression right = parseUnary();
      return new UnaryExpression(operator.getType(), right, operator.getLine(), operator.getColumn());
    }
    return parsePrimary();
  }

  private Expression parsePrimary() {
    if (tokens.match(TokenType.NUMBER) || tokens.match(TokenType.STRING)) {
      Token literal = tokens.previous();
      return new LiteralExpression(literal.getLexeme(), literal.getLine(), literal.getColumn());
    }
    if (tokens.match(TokenType.IDENTIFIER)) {
      Token name = tokens.previous();
      return new IdentifierExpression(name.getLexeme(), name.getLine(), name.getColumn());
    }
    if (tokens.match(TokenType.L_PAREN)) {
      Expression expression = parseExpression();
      tokens.consume(TokenType.R_PAREN, "Ожидалась ')' после выражения");
      Token closing = tokens.previous();
      return new GroupExpression(expression, closing.getLine(), closing.getColumn());
    }
    Token current = tokens.peek();
    throw new ParserException("Ожидалось выражение", current.getLine(), current.getColumn());
  }
}
