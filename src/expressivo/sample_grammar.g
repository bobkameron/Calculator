/*
root ::= sum;
@skip whitespace{
	sum ::= primitive ('+' primitive)*;
	primitive ::= number | '(' sum ')';
}
number ::= [0-9]+;

whitespace ::= [ ]+;
*/



root ::= sum | product; 

// root ::= sum;
@skip whitespace{
	sum ::= primitive ('+' primitive)*;
	
	product ::= primitive ('*' primitive)*;
	
	primitive ::= number | '(' sum ')' | variable | '(' product ')' ;
	
	//primitive ::= number | '(' sum ')';
}
//number ::= [0-9]+;

whitespace ::= [ ]+;

number ::= [0-9]+ ('.')? [0-9]* | [0-9]* ('.')? [0-9]+  ;
variable ::= [a-z]+;

/*
sum = var, num, prod, or sum + another datatype.

prod = var,num , prod * var,num, or prod ; could also be var,num, or prod *   (sum)  

primitive is a num or variable, or a num,var,prod,sum wrapped in '(' ')'
*/

/*
@skip whitespace {
	root ::= expression;
	expression ::= sum | product | primitive;
	product ::= primitive ('*' primitive)+ ;
	sum ::= primitive ('+' primitive)+;
	
	primitive ::= number | variable | '(' sum ')' | '(' product ')' | '(' primitive ')'  ;
}



//whitespace ::= [\t\r\n ];
whitespace ::= [ ]+;

number ::= [0-9]+ ('.')? [0-9]* | [0-9]* ('.')? [0-9]+  ;
variable ::= [a-z]+; 
*/

root ::= sum | product; 

@skip whitespace{
	sum ::= sumprimitive ('+' sumprimitive)*;
	
	product ::= prodprimitive ('*' prodprimitive)*;
	
	sumprimitive ::= number | variable  |  product   | '(' sum ')';
	
	prodprimitive ::= number | variable | '(' sum ')' | '(' product ')' ;
}

whitespace ::= [ ]+;
number ::= [0-9]+ ('.')? [0-9]* | [0-9]* ('.')? [0-9]+ ;
variable ::= [a-z]+;






