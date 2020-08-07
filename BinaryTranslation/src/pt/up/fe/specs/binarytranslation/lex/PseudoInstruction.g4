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

/*
 * The purpose of this grammar is to be able to express the operation of
 * an instruction using the ASM fields as operators
 */

grammar PseudoInstruction;

@header {
    package pt.up.fe.specs.binarytranslation.lex.generated;
}
 
/************************************************************
 * Parsing
 */ 
pseudoInstruction : statement*;

statement 
	: function STATEMENTEND 					# unaryStatement 
	| operand rlop expression STATEMENTEND 		# assignmentStatement;

expression
   : left=expression operator right=expression 	# binaryOperation
   | operator right=expression 					# unaryOperation
   | LPAREN expression RPAREN					# parenExpression
   | operand									# variable
   | function									# functionExpression
   | operand subscriptvalue						# scalarsubscript
   | operand rangesubscriptvalue				# rangesubscript;   
 
subscriptvalue: LBRACK unsignednumber RBRACK;

rangesubscriptvalue: LBRACK unsignednumber SEMI unsignednumber RBRACK; 
 
rlop: EQ; 

/* Built ins */
MSB : 'msb';
LSB : 'lsb';
MSW : 'msw';
LSW : 'lsw';
SETCARRY: 'setCarry';
GETCARRY: 'getCarry';
SEXTEND: 'sext';
UCAST: 'unsigned';
SCAST: 'signed';

builtin : MSB | LSB | MSW | LSW | SETCARRY | GETCARRY | SEXTEND | UCAST | SCAST;

arguments: expression ( ',' expression )*;

function: builtin LPAREN arguments? RPAREN ;

/* Operators and opeands  */
operator : PLUS | MINUS | TIMES | DIV | GT | LT | EQUALS | RSHIFT | LSHIFT | RASHIFT | LNOT | LOR | LAND | LXOR;

/* Any symbol that can be defined (anything but an operator) */
unsignednumber: (INT | DOUBLE);
signednumber: MINUS (INT | DOUBLE);
number: unsignednumber | signednumber;
 
operand:
	(ASMFIELD | STACKPTR) 	# AsmField
   | (number)		# Literal;

/************************************************************
 * Lexing
 */

/* Generic Tokens */
LPAREN	: '(';
RPAREN	: ')';
LBRACK	: '[' ;
RBRACK	: ']' ;
SEMI	: ':' ;
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
LNOT	: '~';
LOR		: '|';
LAND	: '&';
LXOR	: '^';
RSHIFT	: '>>';
LSHIFT	: '<<';
RASHIFT	: '>>>';

/* Any possible field in the ASM field list of any instruction */
ASMFIELD : [A-Za-z]+;
STACKPTR : 'sp';

/* Literal Numbers */
INT    : [0-9]+;
DOUBLE : [0-9]+'.'[0-9]+;
    
/* We're going to ignore all white space characters */
WS  : [ \t\r\n]+ -> skip ;
