package com.craftinginterpreters.lox;

import java.util.List;

interface LoxCallable {
   int arity(); //how many arguments the function expects; 

    Object call(Interpreter interpreter, List<Object> arguments);
}
