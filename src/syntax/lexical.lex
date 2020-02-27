import java_cup.runtime.*;

%%

%class LexicoAST
%public
%unicode
%cup
%line
%column

%{
	StringBuffer string = new StringBuffer();

  private Symbol token(int type) {
    System.out.println(sym.terminalNames[type]+">>("+yyline+","+yycolumn+")");
    return new Symbol(type, yyline, yycolumn);
  }
  private Symbol token(int type, Object value) {
    System.out.println(sym.terminalNames[type]+">>("+yyline+","+yycolumn+")");
    return new Symbol(type, yyline, yycolumn, value);
  }
%}

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f]

/* comments */
Comment = {TraditionalComment} | {EndOfLineComment} | {DocumentationComment}

TraditionalComment   = "#*" [^*] ~"*#" | "#*" "*"+ "#"
// Comment can be the last line of the file, without line terminator.
EndOfLineComment     = "#" {InputCharacter}* {LineTerminator}?

DocumentationComment = "#**" {CommentContent} "*"+ "/"

CommentContent       = ( [^*] | #*+ [^#*] )*

/* literals */
Identifier = [:jletter:] [:jletterdigit:]*

DecIntegerLiteral = 0 | [1-9][0-9]*

numero = {DecIntegerLiteral}("." {DecIntegerLiteral})?

%state STRING

%%

<YYINITIAL>{

    /* reserved words */

		/* GUI */

    "pie"               {   return  token(sym.PIE);		}

    "barplot"			{   return  token(sym.BAR_PLOT);}

    "plot"              {   return  token(sym.PLOT);	}

    "hist"              {   return  token(sym.HIST);	}

		/* Nativas */
    
	"c"					{   return  token(sym.C);		}

    "list"				{   return  token(sym.LIST);	}

    "matrix"			{   return  token(sym.MATRIX);	}

    "typeof"			{   return  token(sym.TYPEOF);	}

    "length"			{   return  token(sym.LENGTH);	}

    "ncol"				{   return  token(sym.N_COL);	}

    "nrow"				{   return  token(sym.N_ROW);	}

    "remove"			{   return  token(sym.REMOVE);	}

    "tolowercase"		{   return  token(sym.TO_LOW);	}

    "touppercase"		{   return  token(sym.TO_UP);	}

    "trunk"				{   return  token(sym.TRUNK);	}

	"round"				{   return  token(sym.ROUND);	}

    "mean"        		{   return  token(sym.MEAN);   	}

	"median"        	{   return  token(sym.MEDIAN);	}

	"mode"				{   return  token(sym.MODE);   	}

	"array"       	 	{   return  token(sym.ARRAY);	}

	"stringlength"      {   return  token(sym.S_LENGTH);}

	"print"	        	{   return  token(sym.PRINT);	}
                         
    "break"				{   return  token(sym.BREAK);	} 
                          
    "switch"	   		{   return  token(sym.SWITCH);	} 
                          
    "case"        		{   return  token(sym.CASE);	}
                        
    "default"      		{   return  token(sym.DEFAULT);	}
                            
    "return"     		{   return  token(sym.RETURN);	} 
                                                
    "if"         		{   return  token(sym.IF);		} 

	"for"         		{   return  token(sym.FOR);		}  

	"while"         	{   return  token(sym.WHILE);	}  

	"do"       			{   return  token(sym.DO);		}

    "else"        		{   return  token(sym.ELSE);	}

    "true"	        	{   return  token(sym.TRUE_VAL);}
                            
    "false"         	{   return  token(sym.FALSE_VAL);}
                            
    "null"				{   return  token(sym.NULL_VAL);}                      
	
	/* literals */
                            
  	{numero}        	{	return token(sym.NUM, yytext());		}

	{tchar}  	     	{	return token(sym.TCHAR, yytext());		}

	{Identifier}    	{	return token(sym.IDENTIFIER, yytext());	}

	/* symbols */

	"?"         		{   return  token(sym.QUESTION);}

	"="         		{   return  token(sym.ASSIGN);	}

	","        			{   return  token(sym.COMA);	}
                            
	"."        			{   return  token(sym.DOT);		}

	":"        			{   return  token(sym.COLON);	}

	";"        			{   return  token(sym.SCOLON);	}

	"("        			{   return  token(sym.OPAR);	}

	")"        			{   return  token(sym.CPAR);	}

	"{"        			{   return  token(sym.OCUR);	}

	"}"        			{   return  token(sym.CCUR);	}

	"["        			{   return  token(sym.OBRA);	}

	"]"        			{   return  token(sym.CBRA);	}
                                
    /* operators */

        /** arithmetic **/

	"+"         		{   return  token(sym.ADD);		}

	"-"         		{   return  token(sym.SUB);		}

	"*"         		{   return  token(sym.MUL);		}
	
	"/"					{	return  token(sym.DIV);		}

	"%"					{	return  token(sym.MOD);		}

	"^"					{	return  token(sym.POT);		}

	"++"        		{   return  token(sym.INC_OP);	}

	"--"        		{   return  token(sym.DEC_OP);	}
                            
        /** relational **/

	"|"         		{   return  token(sym.OR_OP);	}
                            
	"&"         		{   return  token(sym.AND_OP);  }
                         
    "!"         		{   return  token(sym.INVERT);  }

	">"         		{   return  token(sym.GTHAN);   }

	"<"         		{   return  token(sym.LTHAN);   }

	"=="        		{   return  token(sym.EQ_OP);	}

	"!>"        		{   return  token(sym.NE_OP);	}

	">>="   		  	{   return  token(sym.GE_OP);	}

	"<<="				{   return  token(sym.LE_OP);	}

	\"              	{	string.setLength(0);
							yybegin(STRING); 			}

  /* comments */
  {Comment}                      { /* ignore */ }

  /* whitespace */
  {WhiteSpace}                   { /* ignore */ }

}

<STRING> {
  \"					{	yybegin(YYINITIAL); 
							return token(sym.TSTRING,
											string.toString());	}
							
  [^\n\r\"\\]+			{	string.append( yytext() );			}
  
  \\t					{		string.append('\t'); 			}
  
  \\n					{		string.append('\n');			}

  \\r					{		string.append('\r');			}
  
  \\\"					{		string.append('\"');			}
  
  \\					{		string.append('\\');			}
}

/* error fallback */
[^]                     { throw new Error("Illegal character <"+ yytext()+">"); }