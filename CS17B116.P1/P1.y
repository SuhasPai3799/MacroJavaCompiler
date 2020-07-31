%{
	#include <stdio.h>
	#include <string.h>
	#include <stdlib.h>
	#include<stdbool.h>
	int yyerror (char* h);
	int yylex(void);
	int val=0;
	int found=0;
	char *getMacroName(char* macro)
	{
	    char *res=malloc(1000);
	    int i=0;
	    int n=strlen(macro);
	    while(i<n)
	    {
	        if(macro[i]==' ')
	            i++;
	        else
	            break;
	    }
	    while(macro[i]!='n')
	    {
	        i++;
	    }
	    i+=2;
	    while(macro[i]==' ')
	        i++;
	    int cur=0;
	    while(macro[i]!='(' && macro[i]!=' ')
	    {
	        *(res+cur)=*(macro+i);
	        macro++;
	        cur++;
	    }
	    return res;

	}
	char* substitute(char* expr,char * rep,char* newrep,bool vis[])
    {
    int tot,remlen,i,j,extra,startindex=-1,limit,cur_pos;
 	tot=strlen(expr);
	char *replacedString=(char*)malloc(sizeof(char)*(tot+200));
  
   
    remlen=strlen(rep);
    extra=strlen(newrep);
    
    cur_pos=0;
    for(i=0;i<tot;i++)
        {
        bool flag=false;
        if(tot-i>=remlen&&*(expr+i)==*(rep))
            {
            startindex=i;
            j=1;
            while(j<remlen)
            {
                if(*(expr+i+j)!=*(rep+j))
                    {
                    startindex=-1;
                    break;
                    }
                 j++;
            }
            if(startindex!=-1 && vis[cur_pos]==0)
                {
                if(startindex==0)
                {
                vis[cur_pos]=1;
                j=0;
                while(j<extra)
                    {
                    *(replacedString+cur_pos)=*(newrep+j);
                    j++;
                    cur_pos++;
                    }
                i=i+remlen-1;
                flag=true;
                }
                else if((expr[startindex-1]-'a'>=0 && expr[startindex-1]-'a'<=25)|| (expr[startindex-1]-'A'>=0 && expr[startindex-1]-'A'<=25))
                {
                    ;
                }
                else
                {
                    vis[cur_pos]=1;
                    j=0;
                    while(j<extra)
                    {
                    *(replacedString+cur_pos)=*(newrep+j);
                    j++;
                    cur_pos++;
                    }
                    i=i+remlen-1;
                    flag=true;
                }
            }
            else
                {
                ;
                }
            }
        if(flag==false)
            {
            *(replacedString+cur_pos)=*(expr+i);
            cur_pos++;
            }
        }
    *(replacedString+cur_pos)='\0';

    return replacedString;
    }

	char * clean(char *str)
	{
	    int i=0;
	    int n=strlen(str);
	    int st,end=n;

	    while(str[i]==' ' && i<n)
	    {
	        i++;
	    }
	    st=i;
	    i=n-1;
	    while(1)
	    {
	    	if(str[i]!=' ')
	    	{
	    		end=i;
	    		break;
	    	}
	    	i--;
	    }
	    if(end-st+1 <0)
	    	return str;
	    char *res=malloc(end-st+1);
	    memcpy(res,&str[st],end-st+1);
	    return res;
	}
	char * makeMacro(char *str,char *call,char* ret,char ch,char ch1)
	{
		
	    int i=0;
		int n=strlen(str);
		int lb=0;
	    int comm1=0,comm2=0;
	    while(i<n)
	    {
	        if(str[i]==',')
	            comm1++;
	        i++;
	    }
	    i=0;
	    int n1=strlen(call);
	    while(i<n1)
	    {
	        if(call[i]==',')
	            comm2++;
	        i++;
	    }
	    if(comm2!=comm1)
	        return ret;

	    i=0;
		while(i<n)
		{
			if(str[i]=='(')
				{
					lb=i;
					break;
				}
			i++;
		}
		while(str[lb-1]==' ')
		{
			lb--;
		}
		lb--;
		int st=lb;
		while(str[st]!=' ')
		{
			st--;
		}
		st++;
		int name_st=st;
		int name_end=lb;
		int args_st;
		int args_end;
		int expr_st,expr_end;
		char *name=malloc(lb-st+2);
		memcpy(name,&str[st],lb-st+1);
		i=0;
		while(i<n)
		{
			if(str[i]=='(')
			{
				args_st=i+1;
				break;
			}
			i++;

		}
		i=0;
		while(i<n)
		{
			if(str[i]==')')
			{
				args_end=i-1;
				break;
			}
			i++;
		}
		while(i<n)
		{
			if(str[i]==ch)
			{
				expr_st=i+1;
				break;
			}
			i++;
		}
	    i=n-1;
		while(i>=0)
		{
			if(str[i]==ch1)
			{
				expr_end=i-1;
	            break;
			}
			i--;
		}
		
		char *args=malloc(args_end-args_st+2);
		memcpy(args,&str[args_st],args_end-args_st+1);
		char *expr=malloc(expr_end-expr_st+2);
		memcpy(expr,&str[expr_st],expr_end-expr_st+1);

	    ret=malloc(100000);
	    strcpy(ret,expr);


	    ////// Preprocessing of macrostring done
	    ////// Preprocessing of call string begins
	    i=0;
	    int call_st,call_end;
	    n=strlen(call);
	    while(i<n)
	    {
	        if(call[i]==' ')
	            i++;
	        else
	        {
	            call_st=i;
	            i++;
	            break;
	        }
	        
	    }
	    while(i<n)
	    {
	        if(call[i]==' ' || call[i]=='(')
	        {
	            call_end=i-1;
	            break;
	        }
	        else
	            i++;
	    }
	    char *call_name=malloc(call_end-call_st+1);
	    memcpy(call_name,&call[call_st],call_end-call_st+1);
	   
	    if(strcmp(clean(call_name),clean(name))!=0)
	        return ret;

	    int call_arg_st,call_arg_end;
	    i=0;
	    while(i<n)
	    {
	        if(call[i]=='(')
	        {
	            call_arg_st=i+1;
	            break;
	        }
	        i++;

	    }
	    i=n-1;
	    while(i>=0)
	    {
	        if(call[i]==')')
	        {
	            call_arg_end=i-1;
	            break;
	        }
	        i++;
	    }
	    char *call_args=malloc(call_arg_end-call_arg_st+2);
	    memcpy(call_args,&call[call_arg_st],call_arg_end-call_arg_st+1);
	  	bool b1=false,b2=false;
	  	n=strlen(args);
	  	for(i=0;i<n;i++)
	  		if(args[i]!=' ')
	  			b1=true;
	  	n=strlen(call_args);
	  	for(i=0;i<n;i++)
	  		if(args[i]!=' ')
	  			b2=true;
	  	if(b1==true && b2!=true)
	  		return ret;
	  	if(b2==true && b1!=true)
	  		return ret;
	    ////////// Preprocessing done
	    char *save1;
	    char *save2;
	    char *token1=strtok_r(args,",",&save1);
	    char *token2=strtok_r(call_args,",",&save2);
	    bool vis[10000];
        for(i=0;i<strlen(expr);i++)
            vis[i]=0;
		while (token1 != NULL) { 
	        if(token1!=NULL)
	        {
	            if(token2==NULL)
	                return ret;
	            ret=substitute(ret,clean(token1),clean(token2),vis);
	            
	           
	            
	        }
	        token1 = strtok_r(NULL, ",",&save1); 
	        token2 = strtok_r(NULL, ",",&save2);
	        
	        
	        //break;
	    }
	    val=1;

	    return ret;

	}
	void combSpace(char *p1,char*p2)
	{
		p1=strcat(p1," ");
		p1=strcat(p1,p2);
	}
	void combNew(char *p1,char*p2)
	{
		p1=strcat(p1," \n");
		p1=strcat(p1,p2);
	}
	void set(char *val,char* p1,char* p2)
	{
		int n1=strlen(p1);
		int n2=strlen(p2);
		val=malloc(n1+n2+2);
	}
	void setMax(char* val)
	{
		val=malloc(1024);
	}
	struct Node{
	char *mac;
	struct Node* next;
	};
	struct list{
	struct Node* head;
	struct Node* tail;
	};
	struct list *mac_list;

	void addToList(struct Node* t,struct list* cur_list)
	{
		if(cur_list->head==NULL)
		{
			cur_list->head=t;
			cur_list->tail=t;
		}
		else
		{
			cur_list->tail->next=t;
			cur_list->tail=t;
		}
	}
	bool checkMacroList(char *str)
    {
    	char *a=getMacroName(str);
    	bool flag=true;
    	struct Node* temp=malloc(sizeof(temp));
		temp=mac_list->head;
		while(temp!=NULL)
		{
			if(strcmp(a,getMacroName(temp->mac))==0)
				return false;
			temp=temp->next;
		}
		return true;

    }
	void createNode(char *s,struct list* cur_list)
	{
		struct Node *temp=malloc(sizeof(struct Node));
		temp->mac=malloc(strlen(s)+1);
		strcpy(temp->mac,s);
		addToList(temp,cur_list);
	}
	
	void printMacs(struct list* cur_list)
	{
		struct Node* temp=malloc(sizeof(temp));
		temp=cur_list->head;
		while(temp!=NULL)
		{
			printf("%s \n",temp->mac);
			temp=temp->next;
		}
	}
	char *replMacros(char *str,char ch,char ch1)
	{
		found=0;
		char *res;
		struct Node* temp=malloc(sizeof(temp));
		temp=mac_list->head;
		while(temp!=NULL)
		{
			val=0;
			res=makeMacro(temp->mac,str,res,ch,ch1);
			if(val==1)
			{
				found=1;
				return res;
			}
			temp=temp->next;
		}

		return res;

	}
%}
%union
{
    int integer;
	char ch;
	char* str;
	

}
%start Goal ;

%type <str> SCOL DOT COMMA AND OR LEQ NEQ PLUS MINUS MUL DIV NOT LCURL RCURL LBRACK RBRACK LBLOCK RBLOCK EQ DEF CLASS PUBLIC STATIC VOIDS MAIN STRING RETURN IF ELSE WHILE
%type <str> SYSOUT TRUEB FALSEB THIS INT_TYPE BOOLEAN_TYPE LENGTH Identifier INTEGER EXTENDS NEW IDENTIFIER MainClass
%type <str> Goal MacroDefStar TypeDecStar Expression TypeDec Type MethodDec MethodDecStar TypeIdStar StatementStar TypeIdList IdList MacroDefinition PrimExpr
%type <str> MacroDefExpression Statement MacroDefStatement NeedIdList NeedTypeIdList NeedExprList ExpressionList
%token SCOL DOT COMMA AND OR LEQ NEQ PLUS MINUS MUL DIV NOT LCURL RCURL LBRACK RBRACK LBLOCK RBLOCK EQ DEF CLASS PUBLIC STATIC VOIDS MAIN STRING RETURN IF ELSE WHILE
%token INTEGER EXTENDS NEW IDENTIFIER SYSOUT TRUEB FALSEB THIS INT_TYPE BOOLEAN_TYPE LENGTH EOF_1

%%
Goal : MacroDefStar MainClass TypeDecStar 
	{
		//printf("Successfuly parsed\n");
		//printMacs(mac_list);
		printf("%s\n",$2);
		printf("%s\n",$3);
	};
MacroDefStar : MacroDefStar MacroDefinition
	{
		set($$,$1,$2);
		combNew($1,$2);
		strcpy($$,$1);
	}
	| 
	{
		$$=malloc(100000);
	};
MainClass : CLASS Identifier LCURL PUBLIC STATIC VOIDS MAIN LBRACK STRING LBLOCK RBLOCK Identifier RBRACK LCURL SYSOUT LBRACK Expression RBRACK SCOL RCURL RCURL
	{
		$$=malloc(100000);
		combSpace($$,$1);
		combSpace($$,$2);
		combSpace($$,$3);
		combSpace($$,$4);
		combSpace($$,$5);
		combSpace($$,$6);
		combSpace($$,$7);
		combSpace($$,$8);
		combSpace($$,$9);
		combSpace($$,$10);
		combSpace($$,$11);
		combSpace($$,$12);
		combSpace($$,$13);
		combSpace($$,$14);
		combSpace($$,$15);
		combSpace($$,$16);
		combSpace($$,$17);
		combSpace($$,$18);
		combSpace($$,$19);
		combSpace($$,$20);
		combSpace($$,$21);;
		
	};
TypeDecStar : TypeDec TypeDecStar 
	{
		$$=malloc((strlen($1)+strlen($2)+100));
		strcpy($$,$1);
		combNew($$,$2);
	}| 
	{
		$$=malloc(10);
	};
TypeIdStar : TypeIdStar Type Identifier SCOL
	{
		$$=malloc((strlen($1)+strlen($2)+ strlen($3)+strlen($4))+100);
		strcpy($$,$1);
		combNew($$,$2);
		combSpace($$,$3);
		combSpace($$,$4);
	}
	|
	{
		$$=malloc(10);
	};
StatementStar : Statement StatementStar
	{
		$$=malloc((strlen($1)+strlen($2)+100));
		strcpy($$,$1);
		combNew($$,$2);
	}|
	{
		$$=malloc(10);
	};
Statement: LCURL StatementStar RCURL
	{
		$$=malloc((strlen($1)+strlen($2)+strlen($3)+100));
		strcpy($$,$1);
		combSpace($$,$2);
		combSpace($$,$3);
	}
	| SYSOUT LBRACK Expression RBRACK SCOL
	{
		$$=malloc((strlen($1)+strlen($2)+ strlen($3)+strlen($4)+strlen($5))+100);
		strcpy($$,$1);
		combSpace($$,$2);
		combSpace($$,$3);
		combSpace($$,$4);
		combSpace($$,$5);
	}
	| Identifier EQ Expression SCOL
	{
		$$=malloc((strlen($1)+strlen($2)+ strlen($3)+strlen($4))+100);
		strcpy($$,$1);
		combSpace($$,$2);
		combSpace($$,$3);
		combSpace($$,$4);
	}
	| Identifier LBLOCK Expression RBLOCK EQ Expression SCOL
	{
		$$=malloc((strlen($1)+strlen($2)+ strlen($3)+strlen($4)+strlen($5)+strlen($6)+strlen($7))+100);
		strcpy($$,$1);
		combSpace($$,$2);
		combSpace($$,$3);
		combSpace($$,$4);
		combSpace($$,$5);
		combSpace($$,$6);
		combSpace($$,$7);	
	}
	| IF LBRACK Expression RBRACK Statement
	{
		$$=malloc((strlen($1)+strlen($2)+ strlen($3)+strlen($4)+strlen($5))+100);
		strcpy($$,$1);
		combSpace($$,$2);
		combSpace($$,$3);
		combSpace($$,$4);
		combSpace($$,$5);
	}
	| IF LBRACK Expression RBRACK Statement ELSE Statement
	{
		$$=malloc((strlen($1)+strlen($2)+ strlen($3)+strlen($4)+strlen($5)+strlen($6)+strlen($7))+100);
		strcpy($$,$1);
		combSpace($$,$2);
		combSpace($$,$3);
		combSpace($$,$4);
		combSpace($$,$5);
		combNew($$,$6);
		combSpace($$,$7);	
	}
	| WHILE LBRACK Expression RBRACK Statement
	{
		$$=malloc((strlen($1)+strlen($2)+ strlen($3)+strlen($4)+strlen($5))+100);
		strcpy($$,$1);
		combSpace($$,$2);
		combSpace($$,$3);
		combSpace($$,$4);
		combNew($$,$5);
	}| Identifier LBRACK NeedExprList RBRACK SCOL    //////////Macro Call
	{
		$$=malloc(100000);
		strcpy($$,$1);
		combSpace($$,$2);
		combSpace($$,$3);
		combSpace($$,$4);
		val=0;
		found=0;
		char *replaced=replMacros($$,'{','}');
		if(found==1)
		{
			//strcpy($$,"{");
			strcpy($$,replaced);
			//strcat($$,"}");
			//strcat($$,";");
		}
		else
		{
			yyerror("Done");
			exit(0);
		}
	};
TypeDec: CLASS Identifier LCURL TypeIdStar MethodDecStar RCURL
	{
		$$=malloc((strlen($1)+strlen($2)+ strlen($3)+strlen($4)+strlen($5)+strlen($6))+100);
		strcpy($$,$1);
		combSpace($$,$2);
		combSpace($$,$3);
		combNew($$,$4);
		combNew($$,$5);
		combSpace($$,$6);
	}
	| CLASS Identifier EXTENDS Identifier LCURL TypeIdStar MethodDecStar RCURL 
	{
		$$=malloc((strlen($1)+strlen($2)+ strlen($3)+strlen($4)+strlen($5)+strlen($6)+strlen($7)+strlen($8))+100);
		strcpy($$,$1);
		combSpace($$,$2);
		combSpace($$,$3);
		combSpace($$,$4);
		combSpace($$,$5);
		combNew($$,$6);
		combNew($$,$7);
		combSpace($$,$8);	
	};
TypeIdList: COMMA Type Identifier TypeIdList
	{
		$$=malloc((strlen($1)+strlen($2)+ strlen($3)+strlen($4))+100);
		strcpy($$,$1);
		combSpace($$,$2);
		combSpace($$,$3);
		combSpace($$,$4);
	}
	| 
	{
		$$=malloc(10);
	};
NeedTypeIdList : Type Identifier TypeIdList
	{
		$$=malloc((strlen($1)+strlen($2)+ strlen($3))+100);;
		strcpy($$,$1);
		combSpace($$,$2);
		combSpace($$,$3);
	} | 
	{
		$$=malloc(10);
	};
ExpressionList: COMMA Expression ExpressionList
	{
		$$=malloc((strlen($1)+strlen($2)+ strlen($3))+100);
		strcpy($$,$1);
		combSpace($$,$2);
		combSpace($$,$3);
	}
	| 
	{
		$$=malloc(10);
	};
NeedExprList : Expression ExpressionList
	{
		$$=malloc((strlen($1)+strlen($2))+100);
		strcpy($$,$1);
		combSpace($$,$2);
	} | 
	{
		$$=malloc(100000);
	};

MethodDec: PUBLIC Type Identifier LBRACK NeedTypeIdList RBRACK LCURL TypeIdStar StatementStar RETURN Expression SCOL RCURL
	{
		$$=malloc(100000);
		strcpy($$,$1);
		combSpace($$,$2);
		combSpace($$,$3);
		combSpace($$,$4);
		combSpace($$,$5);
		combSpace($$,$6);
		combSpace($$,$7);
		combNew($$,$8);
		combNew($$,$9);
		combNew($$,$10);
		combSpace($$,$11);
		combSpace($$,$12);
		combSpace($$,$13);


	};
MethodDecStar: MethodDec MethodDecStar 
	{
		$$=malloc((strlen($1)+strlen($2))+100);
		strcpy($$,$1);
		combNew($$,$2);
	}
	|
	{
		$$=malloc(100000);
	};
Type: INT_TYPE LBLOCK RBLOCK
	{
		$$=malloc((strlen($1)+strlen($2)+ strlen($3))+100);
		strcpy($$,$1);
		combSpace($$,$2);
		combSpace($$,$3);
	}
	| BOOLEAN_TYPE
	{
		$$=malloc(10);
		strcpy($$,$1);
	}
	| INT_TYPE
	{
		$$=malloc(10);
		strcpy($$,$1);
	}
	|	Identifier
	{
		$$=malloc(10);
		strcpy($$,$1);
	};
Expression : PrimExpr AND PrimExpr
	{
		$$=malloc((strlen($1)+strlen($2)+ strlen($3))+100);
		strcpy($$,$1);
		combSpace($$,$2);
		combSpace($$,$3);
	}
	| PrimExpr OR PrimExpr
	{
		$$=malloc((strlen($1)+strlen($2)+ strlen($3))+100);
		strcpy($$,$1);
		combSpace($$,$2);
		combSpace($$,$3);
	}
	| PrimExpr NEQ PrimExpr
	{
		$$=malloc((strlen($1)+strlen($2)+ strlen($3))+100);
		strcpy($$,$1);
		combSpace($$,$2);
		combSpace($$,$3);
	}
	| PrimExpr LEQ PrimExpr
	{
		$$=malloc((strlen($1)+strlen($2)+ strlen($3))+100);
		strcpy($$,$1);
		combSpace($$,$2);
		combSpace($$,$3);
	}
	| PrimExpr PLUS PrimExpr
	{
		$$=malloc((strlen($1)+strlen($2)+ strlen($3))+100);
		strcpy($$,$1);
		combSpace($$,$2);
		combSpace($$,$3);
	}
	| PrimExpr MINUS PrimExpr
	{
		$$=malloc((strlen($1)+strlen($2)+ strlen($3))+100);
		strcpy($$,$1);
		combSpace($$,$2);
		combSpace($$,$3);
	}
	| PrimExpr MUL PrimExpr
	{
		$$=malloc((strlen($1)+strlen($2)+ strlen($3))+100);
		strcpy($$,$1);
		combSpace($$,$2);
		combSpace($$,$3);
	}
	| PrimExpr DIV PrimExpr
	{
		$$=malloc((strlen($1)+strlen($2)+ strlen($3))+100);
		strcpy($$,$1);
		combSpace($$,$2);
		combSpace($$,$3);
	}
	| PrimExpr LBLOCK PrimExpr RBLOCK
	{
		$$=malloc((strlen($1)+strlen($2)+ strlen($3)+strlen($4))+100);
		strcpy($$,$1);
		combSpace($$,$2);
		combSpace($$,$3);
		combSpace($$,$4);
	}
	| PrimExpr DOT LENGTH
	{
		$$=malloc((strlen($1)+strlen($2)+ strlen($3))+100);
		strcpy($$,$1);
		combSpace($$,$2);
		combSpace($$,$3);
	}
	| PrimExpr
	{
		$$=malloc(strlen($1)+1);
		strcpy($$,$1);
	}
	| PrimExpr DOT Identifier LBRACK NeedExprList RBRACK
	{
		$$=malloc((strlen($1)+strlen($2)+ strlen($3)+strlen($4)+strlen($5)+strlen($6))+100);
		strcpy($$,$1);
		combSpace($$,$2);
		combSpace($$,$3);
		combSpace($$,$4);
		combSpace($$,$5);
		combSpace($$,$6);
	}
	| Identifier LBRACK NeedExprList RBRACK      //////////Macro Call
	{
		$$=malloc(100000);
		strcpy($$,$1);
		combSpace($$,$2);
		combSpace($$,$3);
		combSpace($$,$4);
		val=0;
		found=0;
		char *replaced=replMacros($$,'(',')');
		if(found==1)
		{
			//strcpy($$,"(");

			strcpy($$,replaced);
			//strcat($$,")");
		}
		else
		{
			yyerror("Done");
			exit(0);
		}
	};


PrimExpr: INTEGER
	{
		$$=malloc(strlen($1)+5);
		strcpy($$,$1);
	}
	| TRUEB
	{
		$$=malloc(strlen($1)+5);
		strcpy($$,$1);
	}
	| FALSEB
	{
		$$=malloc(strlen($1)+5);
		strcpy($$,$1);
	}
	| Identifier
	{
		$$=malloc(strlen($1)+5);
		strcpy($$,$1);
	}
	| THIS
	{
		$$=malloc(strlen($1)+5);
		strcpy($$,$1);
	}
	| NEW INT_TYPE LBLOCK Expression RBLOCK
	{
		$$=malloc(100000);
		strcpy($$,$1);
		combSpace($$,$2);
		combSpace($$,$3);
		combSpace($$,$4);
		combSpace($$,$5);
		
	}
	| NEW Identifier LBRACK RBRACK
	{
		$$=malloc((strlen($1)+strlen($2)+ strlen($3)+strlen($4))+100);
		strcpy($$,$1);
		combSpace($$,$2);
		combSpace($$,$3);
		combSpace($$,$4);
	}
	| NOT Expression
	{
		$$=malloc(100000);
		strcpy($$,$1);
		combSpace($$,$2);
	}
	|	LBRACK Expression RBRACK
	{
		$$=malloc(100000);
		strcpy($$,$1);
		combSpace($$,$2);
		combSpace($$,$3);
	
	};
Identifier: IDENTIFIER
	{
		$$=malloc(strlen($1)+2);
		strcpy($$,$1);
	};
IdList: COMMA Identifier IdList
	{
		$$=malloc(100000);
		strcpy($$,$1);
		combSpace($$,$2);
		combSpace($$,$3);
	}
	| 
	{
		$$=malloc(100000);

	};
NeedIdList : Identifier IdList
	{
		$$=malloc(100000);
		strcpy($$,$1);
		combSpace($$,$2);

	}|
	{
		$$=malloc(100000);
	}
MacroDefinition: MacroDefExpression
	{
		$$=malloc(100000);
		strcpy($$,$1);
	}
	|	MacroDefStatement
	{
		$$=malloc(100000);
		strcpy($$,$1);
	}
MacroDefStatement: DEF Identifier LBRACK NeedIdList RBRACK LCURL StatementStar RCURL
	{
		$$=malloc(100000);
		combSpace($$,$1);
		combSpace($$,$2);
		combSpace($$,$3);
		combSpace($$,$4);
		combSpace($$,$5);
		combSpace($$,$6);
		combSpace($$,$7);
		combSpace($$,$8);
		if(true)
			createNode($$,mac_list);
		else
			{
				yyerror("Wrong");
				exit(0);
			}
	};
MacroDefExpression: DEF Identifier LBRACK NeedIdList RBRACK LBRACK Expression RBRACK
	{
		$$=malloc(100000);
		combSpace($$,$1);
		combSpace($$,$2);
		combSpace($$,$3);
		combSpace($$,$4);
		combSpace($$,$5);
		combSpace($$,$6);
		combSpace($$,$7);
		combSpace($$,$8);
		if(true)
			createNode($$,mac_list);
		else
			{
				yyerror("Wrong");
				exit(0);
			}
	};

%%

int yyerror(char *s)
{
	printf ("// Failed to parse macrojava code." );
	return 0;
  
  
}
int main ()
{
	mac_list=malloc(sizeof(struct list));
	yyparse();
	return 0;
}
