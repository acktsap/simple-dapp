package io.blocko.simpledapp;

public abstract class LuaCodeUtils {

  /**
   * Make a execution code with function name and arguments.
   * 
   * @param funcName function name
   * @param args arguments
   * @return generated code
   */
  public static String makeExecutionCode(final String funcName, final Object... args) {
    final StringBuffer buffer = new StringBuffer();
    buffer.append("ret, ok = call(");
    buffer.append('\"');
    buffer.append(funcName);
    buffer.append('\"');
    for (final Object arg : args) {
      buffer.append(", ");
      if (arg instanceof Integer) {
        buffer.append(arg);
      } else { // String
        buffer.append('\"');
        buffer.append((String) arg);
        buffer.append('\"');
      }
    }
    buffer.append("); assert(ok, ret)");
    return buffer.toString();
  }

  /**
   * Make a query code with function name and arguments.
   * 
   * @param funcName function name
   * @param args arguments
   * @return generated code
   */
  public static String makeQueryCode(final String funcName, final Object... args) {
    final StringBuffer buffer = new StringBuffer();
    buffer.append("ret, ok = call(");
    buffer.append('\"');
    buffer.append(funcName);
    buffer.append('\"');
    for (final Object arg : args) {
      buffer.append(", ");
      if (arg instanceof Integer) {
        buffer.append(arg);
      } else { // String
        buffer.append('\"');
        buffer.append((String) arg);
        buffer.append('\"');
      }
    }
    buffer.append("); return ret");
    return buffer.toString();
  }

}
