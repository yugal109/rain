package com.craftinginterpreters.lox;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

final class NativeFunctions {
    private static final String ALPHANUM =
        "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final String PASSWORD_CHARS =
        ALPHANUM + "!@#$%^&*()-_=+[]{}|;:,.<>?";

    private NativeFunctions() {}

    static void install(Environment globals) {
        globals.define("clock", clock());
        globals.define("sqrt", sqrt());
        globals.define("delay", delay());
        globals.define("sin", sin());
        globals.define("cos", cos());
        globals.define("floor", floor());
        globals.define("ceil", ceil());
        globals.define("rand", rand());
        globals.define("randInt", randInt());
        globals.define("len", len());
        globals.define("subStr", substring());
        globals.define("chr", chr());
        globals.define("abs", abs());
        globals.define("min", min());
        globals.define("max", max());
        globals.define("round", round());
        globals.define("pow", pow());
        globals.define("randStr", randStr());
        globals.define("password", password());
        globals.define("hashPassword", hashPassword());
        globals.define("securePassword", securePassword());
    }

    private static int lengthArg(List<Object> arguments) {
        int length = (int) (double) arguments.get(0);
        if (length < 0) {
            return 0;
        }
        return length;
    }

    private static String randomFromCharset(int length, String charset) {
        if (length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * charset.length());
            sb.append(charset.charAt(index));
        }
        return sb.toString();
    }

    private static String sha256Hex(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available.");
        }
    }

    private static LoxCallable clock() {
        return new LoxCallable() {
            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                return (double) System.currentTimeMillis() / 1000.0;
            }

            @Override
            public String toString() {
                return "<native fn>";
            }
        };
    }

    private static LoxCallable sqrt() {
        return new LoxCallable() {
            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                double num = (double) arguments.get(0);

                if (num < 0) {
                    double imaginary = Math.sqrt(-num);
                    if (imaginary == (int) imaginary) {
                        return (int) imaginary + "i";
                    }
                    return imaginary + "i";
                }

                double result = Math.sqrt(num);
                if (result == (int) result) {
                    return (double) (int) result;
                }
                return result;
            }

            @Override
            public String toString() {
                return "<native fn>";
            }
        };
    }

    private static LoxCallable delay() {
        return new LoxCallable() {
            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                double milliseconds = (double) arguments.get(0);

                try {
                    Thread.sleep((long) milliseconds);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                return null;
            }

            @Override
            public String toString() {
                return "<native fn>";
            }
        };
    }

    private static LoxCallable sin() {
        return new LoxCallable() {
            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                return Math.sin((double) arguments.get(0));
            }

            @Override
            public String toString() {
                return "<native fn>";
            }
        };
    }

    private static LoxCallable cos() {
        return new LoxCallable() {
            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                return Math.cos((double) arguments.get(0));
            }

            @Override
            public String toString() {
                return "<native fn>";
            }
        };
    }

    private static LoxCallable floor() {
        return new LoxCallable() {
            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                return Math.floor((double) arguments.get(0));
            }

            @Override
            public String toString() {
                return "<native fn>";
            }
        };
    }

      private static LoxCallable ceil() {
        return new LoxCallable() {
            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                return Math.ceil((double) arguments.get(0));
            }

            @Override
            public String toString() {
                return "<native fn>";
            }
        };
    }

    private static LoxCallable rand() {
        return new LoxCallable() {
            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                return Math.random(); // [0.0, 1.0)
            }

            @Override
            public String toString() {
                return "<native fn>";
            }
        };
    }

    private static LoxCallable randInt() {
        return new LoxCallable() {
            @Override
            public int arity() {
                return 2;
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                int start = (int) (double) arguments.get(0);
                int end = (int) (double) arguments.get(1);

                if (end <= start) {
                    return (double) start;
                }

                return (double) (start + (int) (Math.random() * (end - start)));
            }

            @Override
            public String toString() {
                return "<native fn>";
            }
        };
    }

    private static LoxCallable len() {
        return new LoxCallable() {
            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                return (double) ((String) arguments.get(0)).length();
            }

            @Override
            public String toString() {
                return "<native fn>";
            }
        };
    }

    private static LoxCallable substring() {
        return new LoxCallable() {
            @Override
            public int arity() {
                return 3;
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                String text = (String) arguments.get(0);
                int start = (int) (double) arguments.get(1);
                int end = (int) (double) arguments.get(2);
                return text.substring(start, end);
            }

            @Override
            public String toString() {
                return "<native fn>";
            }
        };
    }

    private static LoxCallable chr() {
        return new LoxCallable() {
            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                int code = (int) (double) arguments.get(0);
                return String.valueOf((char) code);
            }

            @Override
            public String toString() {
                return "<native fn>";
            }
        };
    }

    private static LoxCallable abs() {
        return new LoxCallable() {
            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                return Math.abs((double) arguments.get(0));
            }

            @Override
            public String toString() {
                return "<native fn>";
            }
        };
    }

    private static LoxCallable min() {
        return new LoxCallable() {
            @Override
            public int arity() {
                return 2;
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                double a = (double) arguments.get(0);
                double b = (double) arguments.get(1);
                return Math.min(a, b);
            }

            @Override
            public String toString() {
                return "<native fn>";
            }
        };
    }

    private static LoxCallable max() {
        return new LoxCallable() {
            @Override
            public int arity() {
                return 2;
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                double a = (double) arguments.get(0);
                double b = (double) arguments.get(1);
                return Math.max(a, b);
            }

            @Override
            public String toString() {
                return "<native fn>";
            }
        };
    }

    private static LoxCallable round() {
        return new LoxCallable() {
            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                return (double) Math.round((double) arguments.get(0));
            }

            @Override
            public String toString() {
                return "<native fn>";
            }
        };
    }

    private static LoxCallable pow() {
        return new LoxCallable() {
            @Override
            public int arity() {
                return 2;
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                double base = (double) arguments.get(0);
                double exponent = (double) arguments.get(1);
                return Math.pow(base, exponent);
            }

            @Override
            public String toString() {
                return "<native fn>";
            }
        };
    }

    private static LoxCallable randStr() {
        return new LoxCallable() {
            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                return randomFromCharset(lengthArg(arguments), ALPHANUM);
            }

            @Override
            public String toString() {
                return "<native fn>";
            }
        };
    }

    private static LoxCallable password() {
        return new LoxCallable() {
            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                return randomFromCharset(lengthArg(arguments), PASSWORD_CHARS);
            }

            @Override
            public String toString() {
                return "<native fn>";
            }
        };
    }

    private static LoxCallable hashPassword() {
        return new LoxCallable() {
            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                return sha256Hex((String) arguments.get(0));
            }

            @Override
            public String toString() {
                return "<native fn>";
            }
        };
    }

    private static LoxCallable securePassword() {
        return new LoxCallable() {
            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                String plain = randomFromCharset(lengthArg(arguments), PASSWORD_CHARS);
                return sha256Hex(plain);
            }

            @Override
            public String toString() {
                return "<native fn>";
            }
        };
    }
}
