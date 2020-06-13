/*
 * The purpose of this grammar is to be able to express the operation of
 * an instruction using the ASM fields as operators
 */

/**
 * Copyright 2020 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */


grammar Instruction;

@header {
    package pt.up.fe.specs.binarytranslation.lex;
}
 
/*
 * Parsing
 */
pseudoinstruction : statement*;

statement : expression EQ expression STATEMENTEND;

expression
   :  expression operator expression
   |  LPAREN expression RPAREN 
   | symbol ;

/*
 * Lexing
 */

/* Generic Tokens */
LPAREN	: '(';
RPAREN	: ')';
LBRACK	: '[' ;
RBRACK	: ']' ;
STATEMENTEND : ';' ;

/* Operators */
PLUS	: '+';
MINUS	: '-';
TIMES	: '*';
DIV		: '/';
GT		: '>';
LT		: '<';
EQ		: '=';
EQUALS	: '==';
RSHIFT	: '>>';
LSHIFT	: '<<';
RASHIFT	: '>>>';

operator : PLUS | MINUS | TIMES | DIV | GT | LT | EQUALS | RSHIFT | LSHIFT | RASHIFT ;

/* Any possible field in the ASM field list of any instruction */
ASMFIELD	: [A-Za-z0-9]+ ;
STACKPTR : 'sp';

/* Literal Numbers */
NUMBER: ('0' .. '9') + ('.' ('0' .. '9') +)?;
UNSIGNED_INTEGER: ('0' .. '9')+;
FNUMBER		: ('0'..'9')+('.' ('0'..'9')+)?;

/* Any symbol that can be defined (anything but an operator) */
symbol: ASMFIELD | NUMBER | UNSIGNED_INTEGER | FNUMBER | STACKPTR;     
    
/* We're going to ignore all white space characters */
WS  : [ \t\r\n]+ -> skip ;
