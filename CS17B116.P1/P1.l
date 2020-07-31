%{  
	#include <stdio.h>
	#include "P1.tab.h"
	#include <string.h>
	#include <stdlib.h>
%}
%%
";"  {yylval.str=malloc(strlen(yytext)+1);strcpy(yylval.str,yytext);return SCOL;}
"."  {yylval.str=malloc(strlen(yytext)+1);strcpy(yylval.str,yytext);return DOT;}
","  {yylval.str=malloc(strlen(yytext)+1);strcpy(yylval.str,yytext);return COMMA;}

"&&" {yylval.str=malloc(strlen(yytext)+1);strcpy(yylval.str,yytext);return AND;}
"||" {yylval.str=malloc(strlen(yytext)+1);strcpy(yylval.str,yytext);return OR;}
"<="  {yylval.str=malloc(strlen(yytext)+1);strcpy(yylval.str,yytext);return LEQ;}
"!="  {yylval.str=malloc(strlen(yytext)+1);strcpy(yylval.str,yytext);return NEQ;}
"+"  {yylval.str=malloc(strlen(yytext)+1);strcpy(yylval.str,yytext);return PLUS;}
"-"  {yylval.str=malloc(strlen(yytext)+1);strcpy(yylval.str,yytext);return MINUS;}
"*"  {yylval.str=malloc(strlen(yytext)+1);strcpy(yylval.str,yytext);return MUL;}
"/"  {yylval.str=malloc(strlen(yytext)+1);strcpy(yylval.str,yytext);return DIV;}
"!"  {yylval.str=malloc(strlen(yytext)+1);strcpy(yylval.str,yytext);return NOT;}

"{"  {yylval.str=malloc(strlen(yytext)+1);strcpy(yylval.str,yytext);return LCURL;}
"}"  {yylval.str=malloc(strlen(yytext)+1);strcpy(yylval.str,yytext);return RCURL;}
"("  {yylval.str=malloc(strlen(yytext)+1);strcpy(yylval.str,yytext);return LBRACK;}
")"  {yylval.str=malloc(strlen(yytext)+1);strcpy(yylval.str,yytext);return RBRACK;}
"["  {yylval.str=malloc(strlen(yytext)+1);strcpy(yylval.str,yytext);return LBLOCK;}
"]"  {yylval.str=malloc(strlen(yytext)+1);strcpy(yylval.str,yytext);return RBLOCK;}
"="  {yylval.str=malloc(strlen(yytext)+1);strcpy(yylval.str,yytext);return EQ;}

"#define" {yylval.str=malloc(strlen(yytext)+1);strcpy(yylval.str,yytext);return DEF;}

"class"  {yylval.str=malloc(strlen(yytext)+1);strcpy(yylval.str,yytext);return CLASS;}
"public"  {yylval.str=malloc(strlen(yytext)+1);strcpy(yylval.str,yytext);return PUBLIC;}
"static"  {yylval.str=malloc(strlen(yytext)+1);strcpy(yylval.str,yytext);return STATIC;}
"void"  {yylval.str=malloc(strlen(yytext)+1);strcpy(yylval.str,yytext);return VOIDS;}
"main"  {yylval.str=malloc(strlen(yytext)+1);strcpy(yylval.str,yytext);return MAIN;}
"String"  {yylval.str=malloc(strlen(yytext)+1);strcpy(yylval.str,yytext);return STRING;}
"return"  {yylval.str=malloc(strlen(yytext)+1);strcpy(yylval.str,yytext);return RETURN;}
"if"  {yylval.str=malloc(strlen(yytext)+1);strcpy(yylval.str,yytext);return IF;}
"else"  {yylval.str=malloc(strlen(yytext)+1);strcpy(yylval.str,yytext);return ELSE;}
"while"  {yylval.str=malloc(strlen(yytext)+1);strcpy(yylval.str,yytext);return WHILE;}
"System.out.println" {yylval.str=malloc(strlen(yytext)+1);strcpy(yylval.str,yytext);return SYSOUT;}
"true"  {yylval.str=malloc(strlen(yytext)+1);strcpy(yylval.str,yytext);return TRUEB;}
"false"  {yylval.str=malloc(strlen(yytext)+1);strcpy(yylval.str,yytext);return FALSEB;}
"this"  {yylval.str=malloc(strlen(yytext)+1);strcpy(yylval.str,yytext);return THIS;}
"int" {yylval.str=malloc(strlen(yytext)+1);strcpy(yylval.str,yytext);return INT_TYPE;}
"boolean" {yylval.str=malloc(strlen(yytext)+1);strcpy(yylval.str,yytext);return BOOLEAN_TYPE;}
"length" {yylval.str=malloc(strlen(yytext)+1);strcpy(yylval.str,yytext);return LENGTH;}
"extends" {yylval.str=malloc(strlen(yytext)+1);strcpy(yylval.str,yytext);return EXTENDS;}
"new" {yylval.str=malloc(strlen(yytext)+1);strcpy(yylval.str,yytext);return NEW;}
0|([1-9][0-9]*)	{yylval.str=malloc(strlen(yytext)+1);strcpy(yylval.str,yytext);return INTEGER;}
[a-z|A-Z|_][a-z|A-Z|0-9|_]*  {yylval.str=malloc(strlen(yytext)+1);strcpy(yylval.str,yytext);return IDENTIFIER;}
"//".* {;}
"\n" {;}
"\t" {;}
" " {;}
%%