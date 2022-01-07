package expressivo;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * String-based commands provided by the expression system.
 * 
 * <p>PS1 instructions: this is a required class.
 * You MUST NOT change its name or package or the names or type signatures of existing methods.
 * You MUST NOT add fields, constructors, or instance methods.
 * You may, however, add additional static methods, or strengthen the specs of existing methods.
 */
public class Commands {
    
    /**
     * Differentiate an expression with respect to a variable.
     * @param expression the expression to differentiate
     * @param variable the variable to differentiate by, a case-sensitive nonempty string of letters.
     * @return expression's derivative with respect to variable.  Must be a valid expression equal
     *         to the derivative, but doesn't need to be in simplest or canonical form.
     * @throws IllegalArgumentException if the expression or variable is invalid
     * 
     * New strengthened postcondition:
     * The expression's derivative returned equals Expression.parse(expression)
     * .differentiate(new Variable(variable)).toString()  ,
     * so it is fully deterministic actually. See the specs for Expression.parse, differentiate, and toString()
     * to see more details.
     * 
     */
    public static String differentiate(String expression, String variable) {
        return Expression.parse(expression).differentiate(new Variable(variable)).toString();
    }
    
    /**
     * Simplify an expression.
     * @param expression the expression to simplify
     * @param environment maps variables to values.  Variables are required to be case-sensitive nonempty 
     *         strings of letters.  The set of variables in environment is allowed to be different than the 
     *         set of variables actually found in expression.  Values must be nonnegative numbers.
     * @return an expression equal to the input, but after substituting every variable v that appears in both
     *         the expression and the environment with its value, environment.get(v).  If there are no
     *         variables left in this expression after substitution, it must be evaluated to a single number.
     *         Additional simplifications to the expression may be done at the implementor's discretion.
     * @throws IllegalArgumentException if the expression is invalid
     * 
     * Strengthened postcondition:
     * The returned String is equal to Expression.parse(expression).simplify(environment).toString(), so the
     * return value is fully deterministic actually. 0's and 1's are not simplified any further if they belong
     * as terms of sums or products. 
     * 
     */
    public static String simplify(String expression, Map<String,Double> environment) {
        Expression root = Expression.parse(expression);
        Map <Variable, NonNegativeNum> env = new HashMap<>();
        
        for (Map.Entry <String,Double> entry: environment.entrySet() ) {
            env.put(new Variable(entry.getKey()), new NonNegativeNum(new BigDecimal(entry.getValue()))) ;
        }
        
        String result = root.simplify(env).toString() ;
        return result; 
    }
    
    /* Copyright (c) 2015-2017 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires permission of course staff.
     */
}
