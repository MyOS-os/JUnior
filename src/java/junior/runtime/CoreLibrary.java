package junior.runtime;

public final class CoreLibrary implements Interpreter.Library {
  @Override
  public void print(String value) {
    System.out.println(value);
  }
}
