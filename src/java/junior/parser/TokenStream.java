package junior.parser;

import java.util.ArrayList;
import java.util.List;
import junior.lexer.Token;
import junior.lexer.TokenType;

public final class TokenStream {
  private final List<Token> tokens;
  private int index;

  public TokenStream(List<Token> tokens) {
    this.tokens = tokens == null ? new ArrayList<>() : new ArrayList<>(tokens);
    this.index = 0;
  }

  public boolean isAtEnd() {
    return peek().getType() == TokenType.EOF;
  }

  public Token peek() {
    if (index >= tokens.size()) {
      return tokens.get(tokens.size() - 1);
    }
    return tokens.get(index);
  }

  public Token previous() {
    int previousIndex = Math.max(0, index - 1);
    return tokens.get(previousIndex);
  }

  public Token advance() {
    if (!isAtEnd()) {
      index++;
    }
    return previous();
  }

  public boolean match(TokenType type) {
    if (check(type)) {
      advance();
      return true;
    }
    return false;
  }

  public boolean check(TokenType type) {
    if (isAtEnd()) {
      return false;
    }
    return peek().getType() == type;
  }

  public Token consume(TokenType type, String message) {
    if (check(type)) {
      return advance();
    }
    Token current = peek();
    throw new ParserException(message, current.getLine(), current.getColumn());
  }
}
