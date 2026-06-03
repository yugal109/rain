package com.craftinginterpreters.lox;

import java.util.HashMap;
import java.util.Map;

public class Environment {

    final Environment enclosing; // parent environment

    private final Map<String,Object> values=new HashMap<>();


    // constructor for GLOBAL environment ( no parent ) 
    Environment() {
        enclosing = null;
    }


    // constructor for LOCAL environment ( has parent )
    Environment(Environment enclosing)
    {
        this.enclosing = enclosing;
    }


    // define a NEW variable
    // always succeeds — can redefine existing variables
    void define(String name,Object value)
    {
        values.put(name,value);
    }

    // get a variable's value
    // throws RuntimeError if variable doesn't exist
    Object get(Token name)
    {
        if(values.containsKey(name.lexeme))
        {
            return values.get(name.lexeme);
        }

        if(enclosing!=null)
        {
            return enclosing.get(name);
        }

        throw new RuntimeError(name,"Undefined variable '"+name.lexeme+"'.");
    }

    // update an existing variable's value
    // throws RuntimeError if variable doesn't exist
    void assign(Token name,Object value)
    {
        if(values.containsKey(name.lexeme))
        {
            values.put(name.lexeme,value);
            return;
        }

        if(enclosing!=null)
        {
            enclosing.assign(name,value);
            return;
        }

        throw new RuntimeError(name,"Undefined variable '"+name.lexeme+"'.");
    }

    // get variable exactly 'distance' hops up
    Object getAt(int distance,String name)
    {
        return ancestor(distance).values.get(name);
    }

    // assign variable exactly 'distance' hops up
    void assignAt(int distance, Token name, Object value)
    {
        ancestor(distance).values.put(name.lexeme,value);
    }

    // walk exactly 'distance' hops up the chain
    private Environment ancestor(int distance )
    {
        Environment environment = this;
        for( int i=0;i<distance;i++)
        {
            environment=environment.enclosing;
        }
        return environment;
    }
    
}
