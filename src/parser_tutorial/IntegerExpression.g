
//IntegerExpression Grammar


root ::= sum;
@skip whitespace {
	
	sum ::= primitive ( '+' primitive)*;
	primitive ::= number | '(' sum ')'  ; 


}

whitespace ::= [ \t\r\n]+ ;

number ::= [0-9]+; 


 