package junior.lexer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum TokenType {
  IDENTIFIER("identifier", null, Category.IDENTIFIER, "Идентификатор", 0, Associativity.NONE),
  NUMBER("number", null, Category.LITERAL, "Числовой литерал", 0, Associativity.NONE),
  STRING("string", null, Category.LITERAL, "Строковый литерал", 0, Associativity.NONE),

  LET("let", "let", Category.KEYWORD, "Объявление переменной", 0, Associativity.NONE),
  PRINT("print", "print", Category.KEYWORD, "Печать", 0, Associativity.NONE),
  IF("if", "if", Category.KEYWORD, "Условие", 0, Associativity.NONE),
  ELSE("else", "else", Category.KEYWORD, "Иначе", 0, Associativity.NONE),
  WHILE("while", "while", Category.KEYWORD, "Цикл while", 0, Associativity.NONE),
  CLASS("class", "class", Category.KEYWORD, "Объявление класса", 0, Associativity.NONE),
  RETURN("return", "return", Category.KEYWORD, "Возврат значения", 0, Associativity.NONE),
  NAMESPACE("namespace", "namespace", Category.KEYWORD, "Пространство имён", 0, Associativity.NONE),
  WORKSPACE("workspace", "workspace", Category.KEYWORD, "Рабочая область", 0, Associativity.NONE),
  DATASPACE("dataspace", "dataspace", Category.KEYWORD, "Область данных", 0, Associativity.NONE),
  DISKSPACE("diskspace", "diskspace", Category.KEYWORD, "Область диска", 0, Associativity.NONE),
  CODESPACE("codespace", "codespace", Category.KEYWORD, "Область кода", 0, Associativity.NONE),
  LIB("lib", "lib", Category.KEYWORD, "Библиотека", 0, Associativity.NONE),
  EXIT("exit", "exit", Category.KEYWORD, "Выход", 0, Associativity.NONE),
  USING("using", "using", Category.KEYWORD, "Использование", 0, Associativity.NONE),
  FOR("for", "for", Category.KEYWORD, "Цикл for", 0, Associativity.NONE),
  NEW("new", "new", Category.KEYWORD, "Создание", 0, Associativity.NONE),
  TRUE("true", "true", Category.KEYWORD, "Логическая истина", 0, Associativity.NONE),
  FALSE("false", "false", Category.KEYWORD, "Логическая ложь", 0, Associativity.NONE),

  PLUS("+", "+", Category.OPERATOR, "Сложение", 10, Associativity.LEFT),
  MINUS("-", "-", Category.OPERATOR, "Вычитание", 10, Associativity.LEFT),
  STAR("*", "*", Category.OPERATOR, "Умножение", 20, Associativity.LEFT),
  SLASH("/", "/", Category.OPERATOR, "Деление", 20, Associativity.LEFT),
  EQ("=", "=", Category.OPERATOR, "Присваивание", 1, Associativity.RIGHT),
  EQ_EQ("==", "==", Category.OPERATOR, "Равно", 7, Associativity.LEFT),
  BANG("!", "!", Category.OPERATOR, "Логическое НЕ", 30, Associativity.RIGHT),
  BANG_EQ("!=", "!=", Category.OPERATOR, "Не равно", 7, Associativity.LEFT),
  LT("<", "<", Category.OPERATOR, "Меньше", 8, Associativity.LEFT),
  LTE("<=", "<=", Category.OPERATOR, "Меньше или равно", 8, Associativity.LEFT),
  GT(">", ">", Category.OPERATOR, "Больше", 8, Associativity.LEFT),
  GTE(">=", ">=", Category.OPERATOR, "Больше или равно", 8, Associativity.LEFT),
  AND_AND("&&", "&&", Category.OPERATOR, "Логическое И", 4, Associativity.LEFT),
  OR_OR("||", "||", Category.OPERATOR, "Логическое ИЛИ", 3, Associativity.LEFT),
  COLON_COLON("::", "::", Category.OPERATOR, "Область", 25, Associativity.LEFT),
  ARROW("->", "->", Category.OPERATOR, "Стрелка", 2, Associativity.RIGHT),
  FAT_ARROW("=>", "=>", Category.OPERATOR, "Толстая стрелка", 2, Associativity.RIGHT),
  SHIFT_LEFT("<<<", "<<<", Category.OPERATOR, "Сдвиг влево", 9, Associativity.LEFT),
  SHIFT_RIGHT(">>>", ">>>", Category.OPERATOR, "Сдвиг вправо", 9, Associativity.LEFT),

  L_PAREN("(", "(", Category.DELIMITER, "Левая скобка", 0, Associativity.NONE),
  R_PAREN(")", ")", Category.DELIMITER, "Правая скобка", 0, Associativity.NONE),
  L_BRACE("{", "{", Category.DELIMITER, "Левая фигурная скобка", 0, Associativity.NONE),
  R_BRACE("}", "}", Category.DELIMITER, "Правая фигурная скобка", 0, Associativity.NONE),
  L_BRACKET("[", "[", Category.DELIMITER, "Левая квадратная скобка", 0, Associativity.NONE),
  R_BRACKET("]", "]", Category.DELIMITER, "Правая квадратная скобка", 0, Associativity.NONE),
  COMMA(",", ",", Category.DELIMITER, "Запятая", 0, Associativity.NONE),
  DOT(".", ".", Category.DELIMITER, "Точка", 0, Associativity.NONE),
  SEMICOLON(";", ";", Category.DELIMITER, "Точка с запятой", 0, Associativity.NONE),
  COLON(":", ":", Category.DELIMITER, "Двоеточие", 0, Associativity.NONE),

  EOF("EOF", null, Category.SPECIAL, "Конец файла", 0, Associativity.NONE);

  public enum Category {
    IDENTIFIER,
    LITERAL,
    KEYWORD,
    OPERATOR,
    DELIMITER,
    SPECIAL
  }

  public enum Associativity {
    LEFT,
    RIGHT,
    NONE
  }

  private static final Map<String, TokenType> LEXEME_MAP = new HashMap<>();
  private static final Map<Category, List<TokenType>> CATEGORY_CACHE = new HashMap<>();

  static {
    for (TokenType type : values()) {
      if (type.lexeme != null) {
        LEXEME_MAP.put(type.lexeme, type);
      }
      CATEGORY_CACHE.computeIfAbsent(type.category, key -> new ArrayList<>()).add(type);
    }
  }

  private final String name;
  private final String lexeme;
  private final Category category;
  private final String description;
  private final int precedence;
  private final Associativity associativity;

  TokenType(String name, String lexeme, Category category, String description, int precedence,
            Associativity associativity) {
    this.name = name;
    this.lexeme = lexeme;
    this.category = category;
    this.description = description;
    this.precedence = precedence;
    this.associativity = associativity;
  }

  public String getName() {
    return name;
  }

  public String getLexeme() {
    return lexeme;
  }

  public Category getCategory() {
    return category;
  }

  public String getDescription() {
    return description;
  }

  public int getPrecedence() {
    return precedence;
  }

  public Associativity getAssociativity() {
    return associativity;
  }

  public boolean isKeyword() {
    return category == Category.KEYWORD;
  }

  public boolean isLiteral() {
    return category == Category.LITERAL;
  }

  public boolean isOperator() {
    return category == Category.OPERATOR;
  }

  public boolean isAssignment() {
    return this == EQ || this == ARROW || this == FAT_ARROW;
  }

  public boolean isComparison() {
    return this == EQ_EQ || this == BANG_EQ || this == LT || this == LTE || this == GT || this == GTE;
  }

  public boolean isLogical() {
    return this == AND_AND || this == OR_OR || this == BANG;
  }

  public boolean isDelimiter() {
    return category == Category.DELIMITER;
  }

  public boolean isSpecial() {
    return category == Category.SPECIAL;
  }

  public boolean hasLexeme() {
    return lexeme != null;
  }

  public static TokenType fromLexeme(String lexeme) {
    return LEXEME_MAP.get(lexeme);
  }

  public static List<TokenType> allByCategory(Category category) {
    List<TokenType> list = CATEGORY_CACHE.get(category);
    if (list == null) {
      return Collections.emptyList();
    }
    return Collections.unmodifiableList(list);
  }

  public static List<TokenType> keywords() {
    return allByCategory(Category.KEYWORD);
  }

  public static List<TokenType> operators() {
    return allByCategory(Category.OPERATOR);
  }

  public static List<TokenType> delimiters() {
    return allByCategory(Category.DELIMITER);
  }

  public static List<TokenType> literals() {
    return allByCategory(Category.LITERAL);
  }

  public String describe() {
    if (lexeme == null) {
      return name + " (" + description + ")";
    }
    return name + " '" + lexeme + "' (" + description + ")";
  }

  @Override
  public String toString() {
    return name + (lexeme == null ? "" : "(" + lexeme + ")");
  }
}
