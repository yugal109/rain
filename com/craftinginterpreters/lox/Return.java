package com.craftinginterpreters.lox;

public class Return extends RuntimeException{
    final Object value;

    Return(Object value)
    {
        // disable Java's exception overhead
        // we're using this as control flow not error handling
        super(null,null,false,false);
        this.value=value;
    }
    
}
