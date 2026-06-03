package com.craftinginterpreters.lox;


enum TokenType{

    // Single character tokens
    LEFT_PAREN,RIGHT_PAREN, // ( )
    LEFT_BRACE, RIGHT_BRACE, // { }
    COMMA, // , 
    DOT, // .
    MINUS, // -
    PLUS, // +
    MOD, // %
    SEMICOLON, // ;
    STAR, // *
    SLASH, // /

    // one or two character tokens
    BANG, BANG_EQUAL, // ! !=
    EQUAL, EQUAL_EQUAL, // = ==
    GREATER,GREATER_EQUAL, // > >= 
    LESS, LESS_EQUAL, // < <=

    // Literals
    IDENTIFIER, // variable names, function names
    STRING, // "hello"
    NUMBER, // 1234, 12.34

    // Keywords
    AND, CLASS, ELSE, FALSE, // and class else false
    FUN,FOR,IF,NIL,OR, // fun for if nil or
    PRINT,PRINTIN, RETURN, SUPER, THIS, // print return super this
    TRUE, VAR, WHILE, // true var while

    // End of file
    EOF // signals parser that input is done

}
