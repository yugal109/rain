package com.craftinginterpreters.lox;
import java.util.List;

public class LoxFunction implements LoxCallable {

    // the parsed function declaration (name, params, body)
    private final Stmt.Function declaration;
    private final Environment closure;
    private final boolean isInitializer;

    LoxFunction(Stmt.Function declaration,Environment closure,boolean isInitializer)
    {
        this.declaration=declaration;
        this.closure=closure;
        this.isInitializer=isInitializer;
    }

    LoxFunction bind(LoxInstance instance)
    {
        Environment environment=new Environment(closure);
        environment.define("this",instance);
        return new LoxFunction(declaration, environment,isInitializer);
    }

    // how many parameters this function expects
    @Override
    public int arity(){
        return declaration.params.size();
    }

    // execute the function 
    @Override
    public Object call(Interpreter interpreter,List<Object> arguments)
    {
        // create NEW environment for this function call
        // parent = globals ( not current environment!)
        Environment environment=new Environment(closure);

        // bind each parameter to its argument value
        for(int i=0;i<declaration.params.size();i++)
        {
            environment.define(declaration.params.get(i).lexeme,arguments.get(i));
        }

        try{
            interpreter.executeBlock(declaration.body, environment);
        }catch(Return returnValue)
        {
            // if initializer → always return this!
            if(isInitializer) return closure.getAt(0,"this");
            return returnValue.value;
        }

        if (isInitializer) return closure.getAt(0, "this");
        return null;
    }


    @Override
    public String toString() {
        return "<fn " + declaration.name.lexeme + ">";
    }

    
}
