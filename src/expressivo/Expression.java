package expressivo;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import lib6005.parser.*;

/**
 * An immutable data type representing a polynomial expression of: + and *
 * nonnegative integers and floating-point numbers variables (case-sensitive
 * nonempty strings of letters)
 * 
 * <p>
 * PS1 instructions: this is a required ADT interface. You MUST NOT change its
 * name or package or the names or type signatures of existing methods. You may,
 * however, add additional methods, or strengthen the specs of existing methods.
 * Declare concrete variants of Expression in their own Java source files.
 */

public interface Expression {

    // Datatype definition
    // Expression = NonNegativeNum:BigDecimal + Variable:String +
    // Sum(left:Expression,right:Expression) +
    // Product(left:Expression,right:Expression)
    
    /**
     * Parse an expression.
     * 
     * @param input expression to parse, as defined in the PS1 handout.
     * @return expression AST for the input
     * @throws IllegalArgumentException if the expression is invalid
     * 
     * Added by me: note that the expression returned respects structural equality
     * as defined by the PS1 handout, and also treats mathematical expressions
     * w/ different groupings but same meaning and sequential order in the String input
     * as the same. 
     * eg the Expression returned by parsing "(2 + 5) + 6 + 7" is equal to the 
     * Expression returned by parsing "2 + 5 + (6 + 7)"
     * 
     * The Expression returned is case-sensitive, but not sensitive to the # of trailing
     * zeroes for a number. So a variable "abcd" in the input string != "AbCd", but
     * "3434.23" equals "3434.2300000" 
     * 
     */
    public static Expression parse(String input) {
        lib6005.parser.Parser<MathExpression> parser = null; 
        
        try {
            parser = GrammarCompiler.compile(new File (
                    "src\\expressivo\\Expression.g" ), MathExpression.ROOT   );
        } catch (UnableToParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new IllegalArgumentException();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
        ParseTree<MathExpression> tree = null;
        try {
            tree = parser.parse(input);
        } catch (UnableToParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
        
        //System.out.println("String input  " + input);
        //System.out.println("Parsed Tree     " + tree.toString()) ;
        
        
        Expression root = Ast.buildAST(tree);
        
        // System.out.println(root.toString());
        
        
        return root;
    }
    
    
    public boolean isNumeric();
    
    
    public static void main(String args[]) {
        String toParse = " abcd + ((23423.234234)) * (abcf + dsf)";
        String simpler = "  ( (  (  abcd  ) )  +      (  (  111.0000  )    )  )";
        Expression simple = Expression.parse(simpler);
        Expression result =  Expression.parse(toParse);
        Expression another = Expression.parse("   3+ a *  b  " );
        System.out.println(simple.toString() + "   " + result.toString() + "  " + another.toString());
        
        
    }
    
    /**
     * @param var the Variable to differentiate with respect to. 
     * @return the current expression differentiated with respect to var. 
     * 
     * The differentiation respects the structural equality of the Expression, as mentioned in the handout.
     * 
     * When differentiating constants, a NonNegativeNum 0 is always returned.
     * 
     * When differentiating dvar/dvar, a NonNegativeNum 1 is always returned.
     * 
     * When differentiating summands of a Sum, d(y1 + y2 + y3 + ... + yn)/dx, the differentiated expression maintains
     * the order of the terms, so d(y1 + y2 + y3 + ... + yn)/dx = dy1/dx + dy2/dx + ... + dyn/dx. 
     * 
     * When differentiating products, the returned expression of d(y1*y2*...*yn)/dx follows the product rule applied successively
     * to terms y2 to yn, so for example d(x*x)/dx = x*1 + 1*x, and d(x*x*x)/dx =  x*x*1 + (x*1+1*x)*x.
     * 
     * We never simplify 1's or 0's if they are terms in a summand or product. 
     * 
     */
    public Expression differentiate (Variable var);
    

    
    /*
     * Simplifies the expression given a mapping of variables to NonNegativeNum's. 
     * 
     * @param environment A mapping of Variable names to NonNegativeNum number instances.
     * @returns A simplified expression that preserves the structural equality of the expression, as described in the handout.
     * 
     * If all terms of an n-ary grouping of simplified terms are numbers, they must be simplified down to a single NonNegativeNum
     * instance. eg if a simplified expression e = 5+4+3, then the function returns e = NonNegativeNum(12). 
     * Also, if (3+3)*g is a simplified expression e, then the function returns e = Product(6,g), as (3+3) is a grouping
     * that consists of only numbers and no variables. 
     * 
     * Also, the addition or product of an expression that is an n-ary grouping of only #'s with another n-ary
     * grouping of #'s yields just a #. So, 
     * (3+3)*(6+5) can be simplified to just 66. 
     * 
     * All non simplified variables in the expression are simply left as the same variables.
     * 
     * No other simplifications are allowed actually, and 
     * we do not simplify cases where we are adding 0 in a sum or multiplying by 1, so this function is completely 
     * deterministic. 
     * 
     */
    public Expression simplify (Map<Variable,NonNegativeNum> environment);
    
    /**
     * Returns an expression representing a variable (defined above in class spec)
     * 
     * @param variableName the String name of the expression to be created, requires
     *                     that all of its characters be letters and the String is
     *                     non-empty.
     * @return the case-sensitive expression representing the variable with the name
     *         variableName.
     */
    public static Expression make(String variableName) {
        return new Variable(variableName);
    }

    /**
     * Returns an expression representing the BigInteger number
     * 
     * @param number, must be nonnegative
     * @return an expression representing the argument number.
     */
    public static Expression make(BigDecimal number) {
        return new NonNegativeNum(number);
    }

    /**
     * Returns an expression representing the sum of left and right arguments.
     * 
     * @param left  an Expression
     * @param right an Expression
     * @return an expression representing the sum of left and right, where the terms
     *         belonging to left come before the terms belonging to right in
     *         sequential order.
     */
    public static Expression makeSum(Expression left, Expression right) {
        return new Sum(left, right);
    }

    /**
     * Returns an expression representing the product of the arguments left and
     * right.
     * 
     * @param left  an Expression
     * @param right an Expression
     * @return an expression representing the product of left and right, where the
     *         terms belonging to left come before the terms belonging to right in
     *         sequential order.
     */
    public static Expression makeProduct(Expression left, Expression right) {
        return new Product(left, right);
    }

    /**
     * @return the summands of the expression, with the order of the return list of 
     * summands obeying structural equality as defined in the handout. 
     */
    public List<Expression> getSummands();

    /**
     * 
     * @return the terms of the product of the expression, with the order of the 
     * returned list of products obeying structural equality defined in the handout.
     */
    public List<Expression> getProdTerms();

    /**
     * @return a parsable representation of this expression, such that for all
     *         e:Expression, e.equals(Expression.parse(e.toString())).
     * 
     */
    @Override
    public String toString();

    /**
     * @param thatObject any object
     * @return true if and only if this intance and thatObject are
     *         structurally-equal Expressions, as defined in the PS1 handout.
     *
     */
    @Override
    public boolean equals(Object thatObject);

    /**
     * @return hash code value consistent with the equals() definition of structural
     *         equality, such that for all e1,e2:Expression, e1.equals(e2) implies
     *         e1.hashCode() == e2.hashCode()
     */
    @Override
    public int hashCode();

    // TODO more instance methods

    /*
     * Copyright (c) 2015-2017 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires permission of course
     * staff.
     */
}

enum MathExpression {
    // ROOT, SUM, PRODUCT, PRIMITIVE, NUMBER, VARIABLE, WHITESPACE //  , EXPRESSION
    //ROOT, SUM, PRODUCT, PRODPRIMITIVE, SUMPRIMITIVE, NUMBER, VARIABLE, WHITESPACE, PRIMITIVE
    ROOT, SUM, PRODUCT, PRIMITIVE, NUMBER, VARIABLE, WHITESPACE 

};
/*
 * @skip whitespace {
    root ::= expression;
    expression ::= sum | product | primitive;
    sum ::= primitive ('+' primitive)+;
    product ::= primitive ('*' primitive)+;
    primitive ::= number | variable | '(' sum ')' | '(' product ')' | '(' primitive ')'
}

whitespace ::= [ ]+;
number::= [0-9]+ ('.')? [0-9]* | [0-9]* ('.')? [0-9]+  ;
variable ::= [a-z]+
 * 
 * 
 * 
 * 
 * 
 */










