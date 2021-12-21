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
pseudoInstruction : statement+;

/* Question mark stands for: zero or one
 * Plus stands for: one or more
 * Star stands for: zero or more 
 */

statementlist
	: statement
	| LBRACE statement* RBRACE;

statement 
	: expression STATEMENTEND 																										# plainStmt
	| 'if' LPAREN condition=expression RPAREN ifsats=statementlist 									# ifStatement
	| 'if' LPAREN condition=expression RPAREN ifsats=statementlist ('else' elsestats=statementlist)	# ifElseStatement;	

expression 
	: operand 																# variableExpr 
	| LPAREN expression RPAREN 												# parenExpr
	| functionName LPAREN arguments? RPAREN									# functionExpr
	| operator right=expression 											# unaryExpr
	| left=expression operator right=expression 							# binaryExpr
	| left=expression rlop right=expression 			 					# assignmentExpr; 
	
rlop: EQ; 

/* Built ins */
ADDR : 'addr'; // gets address of own instruction, called as "addr()"
BYTE : 'byte';
CLZ : 'clz';
MSB : 'msb';
LSB : 'lsb';
MSW : 'msw';
LSW : 'lsw';
SQRT: 'sqrt';
SETCARRY: 'setCarry';
GETCARRY: 'getCarry';
SEXTEND: 'sext';
UCAST: 'unsigned';
SCAST: 'signed';
FLOAT: 'float';

functionName : BYTE | CLZ | MSB | LSB | MSW | LSW | SETCARRY | GETCARRY | SEXTEND | UCAST | SCAST | FLOAT | SQRT;

arguments: expression (',' expression)*;

/* Operators and operands  */
operator : PLUS | MINUS | TIMES | DIV | GT | GET | LT | LET | EQUALS | NEQUALS | RSHIFT | LSHIFT | RASHIFT | LNOT | LOR | LAND | BXOR | BOR | BAND;

/* Any symbol that can be defined (anything but an operator) */
integerval: INT;
doubleval: DOUBLE;
unsignednumber: (integerval | doubleval);
signednumber: MINUS(integerval | doubleval);
number: unsignednumber | signednumber;
encodedfield: ASMFIELD;
metafield: METASYMBOL processorRegister=ASMFIELD;
asmfield: encodedfield | metafield;

rangesubscript: asmfield LBRACK loidx=integerval SEMI hiidx=integerval RBRACK;
scalarsubscript : asmfield LBRACK idx=integerval RBRACK;
 
operand:
	asmfield 			# AsmFieldOperand
   | number				# LiteralOperand
   | scalarsubscript 	# ScalarsubscriptOperand
   | rangesubscript 	# RangesubscriptOperand; 

/************************************************************
 * Lexing
 */

/* Generic Tokens */
LPAREN	: '(';
RPAREN	: ')';
LBRACK	: '[' ;
RBRACK	: ']' ;
LBRACE	: '{' ;
RBRACE	: '}' ;
SEMI	: ':' ;
STATEMENTEND : ';' ;

/* Operators */
PLUS	: '+';
MINUS	: '-';
TIMES	: '*';
DIV		: '/';
GT		: '>';
GET		: '>=';
LT		: '<';
LET		: '<=';
EQ		: '=';
EQUALS	: '==';
NEQUALS	: '!=';
LNOT	: '~';
LOR		: '||';
LAND	: '&&';
BOR		: '|';
BAND	: '&';
BXOR	: '^';
RSHIFT	: '>>';
LSHIFT	: '<<';
RASHIFT	: '>>>';

fragment DIGIT: [0-9];
fragment CHARACTER: [A-Za-z];

/* Any possible field in the ASM field list of any instruction */
/* New ASMFIELD regex for RISC-V: [A-Za-z][A-Za-z0-9]+ */
METASYMBOL : '$';
ASMFIELD : CHARACTER+;

/* Literal Numbers */
INT    : DIGIT+;
DOUBLE : DIGIT+'.'DIGIT+;
    
/* We're going to ignore all white space characters */
WS  : [ \t\r\n]+ -> skip ;
