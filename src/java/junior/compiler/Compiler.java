package junior.compiler;

import junior.ast.Program;

public final class Compiler {
  public CompiledUnit compile(Program program) {
    return new CompiledUnit(program);
  }

  public static final class CompiledUnit {
    private final Program program;

    private CompiledUnit(Program program) {
      this.program = program;
    }

    public Program getProgram() {
      return program;
    }
  }
}
