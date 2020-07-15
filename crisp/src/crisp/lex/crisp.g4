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

// Based on the Java grammar at : 
// https://github.com/antlr/grammars-v4/blob/master/java/java9/Java9.g4

grammar crisp;

@header {
    package crisp.lex;
}

/* ************************************************************************* */
/* LEXER */

// fragments //////////////////////////////////////////////////////////////////

// literals
fragment ALPHA : [a-zA-Z_] ;
fragment DIGIT : [0-9] ;

// syntax
fragment LBRACK : '[' ;
fragment RBRACK : ']' ;
fragment LCURL : '{' ;
fragment RCURL : '}' ;
fragment LPAREN : '(' ;
fragment RPAREN : ')' ;
fragment SEMICOLON : ';' ;
fragment COMMA : ',' ;
fragment LINECOMMENT : '//' ;
fragment BLOCKCOMMENTSTART : '/*' ;
fragment BLOCKCOMMENTEND : '*/' ;

// keywords ///////////////////////////////////////////////////////////////////

// generic modifiers
PRIVATE : 'private' ;
PUBLIC : 'public' ;
PROTECTED : 'protected' ;

// top level words
CLASS : 'class' ;
END : 'end' ;
EXTENDS : 'extends' ;
IMPLEMENTS : 'implements' ;
NEW : 'new' ;

// constructs
IF : 'if';
ELSE : 'else';
FOR : 'for';

// variable types
LOGIC : 'logic';
INTEGER : 'integer';
LONG : 'long';

// blocks

// Taken from : https://github.com/antlr/grammars-v4/blob/master/java/java9/Java9.g4
Identifier : Letter LetterOrDigit* ;
fragment Letter : [a-zA-Z$_] ; // these are the "java letters" below 0x7F 
fragment LetterOrDigit : [a-zA-Z0-9$_]  ; // these are the "java letters or digits" below 0x7F

// discard ///////////////////////////////////////////////////////////////////

CARRIAGE : '\r' -> channel(HIDDEN);
NEWLINE : '\n' -> channel(HIDDEN);
WHITE_SPACE : [ \t\r\n]+ -> channel(HIDDEN) ;

/* ************************************************************************* */
/* PARSER */

/* Expressions */
variableType : LOGIC | INTEGER | LONG ;



/* Declarations */

// e.g. any name
identifier : Identifier ;

// e.g. varA = <expr>
variableInitializer : expression ;

// e.g. varA = a
variableDeclarator : identifier ('=' variableInitializer)? ;

// e.g. varA = a, varB = b, varC, etc
variableDeclaratorList : variableDeclarator (COMMA variableDeclarator)* ;

// declare a class name
classType : identifier ;

// e.g. "extends classA"
superclass : EXTENDS classType ;

interfaceType : classType; 

// e.g. "classA, classB" 
interfaceTypeList :	interfaceType (',' interfaceType)* ;

// e.g. "implements classA, classB"
superinterfaces : IMPLEMENTS interfaceTypeList ;

/* Modifier Prefixes */
classModifier : PUBLIC | PRIVATE;
constructorModifier : PUBLIC |PROTECTED | PRIVATE ;
fieldModifier : PUBLIC |PROTECTED | PRIVATE ;
methodModifier : PUBLIC |PROTECTED | PRIVATE ;

// e.g. "private ClassA(...)"
constructorDeclaration : constructorModifier* constructorDeclarator constructorBody ;

fieldDeclaration : fieldModifier* variableType variableDeclaratorList SEMICOLON ;

classMemberDeclaration
	:	fieldDeclaration
	|	methodDeclaration
	|	SEMICOLON
	;

classBodyDeclaration
	:	classMemberDeclaration
	|	instanceInitializer
	|	staticInitializer
	|	constructorDeclaration
	;

classBody :	LCURL classBodyDeclaration* RCURL ;

classDeclaration : classModifier* CLASS identifier superclass? superinterfaces? classBody

