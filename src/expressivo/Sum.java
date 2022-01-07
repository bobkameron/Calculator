package expressivo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Sum implements Expression {

    private final List<Expression> terms;
    private final List<Expression> prodTerms;
    private final String stringRep;
    private final int hashCode;

    static final List<Character> validChars = Collections.unmodifiableList(Arrays.asList('*','+','.','(',')',' '));
    
    /*
     * Rep invariant:
     * terms is a List with at least two items. 
     * prodTerms is a list with only one item, the current Sum instance this. 
     * stringRep is a String of at least 3 letters consisting of only letters, numbers, spaces, and '.','*','+','(',')' chars. 
     * It must have the '+' char. 
     * 
     * 
     * Abstraction Function: The sequential list of the terms represents the mathematical addition of 
     * each Expression term in terms, in order obeying structural equality. 
     * 
     * Safety from rep exposure argument: 
     * terms is an immutable reference and constructed as an immutable List
     * prodTerms arg is same as terms
     * stringRep is immutable reference and immutable object
     * hashCode is immutable object and reference.
     * 
     */

    static void assertValidString(String stringRep, String mustHave) {
        assert stringRep.length() > 2;
        assert stringRep.contains(mustHave);
        int length = stringRep.length();
        for (int i = 0; i < length; i ++ ) {
            Character c = stringRep.charAt(i);
            assert Character.isLetterOrDigit(c) || validChars.contains(c) ;
        }
  
    }
    
    private void checkRep() {
        assert terms.size() >1;
        assert prodTerms.size() == 1;
        assert prodTerms.get(0) == this;
        assertValidString(stringRep, "+");
        
    }
    
    @Override
    public List<Expression> getSummands() {
        return terms;
    }

    @Override
    public List<Expression> getProdTerms() {
        return prodTerms;
    }

    private static void addSummands(Expression e, List<Expression> addTo) {
        for (Expression that : e.getSummands()) {
            addTo.add(that);
        }
    }

    private String initStringRep() {
        List<String> result = new ArrayList<>();
        for (Expression e : this.terms) {
            result.add(e.toString());
            result.add("+");
        }
        result.remove(result.size() - 1);
        return String.join("", result);

    }

    /**
     * Returns a sum expression representing the product of the left and right
     * expressions.
     * 
     * @param left  an expression
     * @param right another expression
     * @return An expression representing the sum of the left and right expressions
     *         in order, where the left expression comes before the right expression
     *         in sequential order.
     */
    public Sum(Expression left, Expression right) {
        List<Expression> sequence = new ArrayList<>();
        addSummands(left, sequence);
        addSummands(right, sequence);
        terms = Collections.unmodifiableList(sequence);
        
        prodTerms = Collections.unmodifiableList(Arrays.asList(this));
        hashCode = terms.hashCode();
        stringRep = initStringRep();

    }
    
    private Sum( List<Expression> sequence ) {
        terms = Collections.unmodifiableList(sequence);
        prodTerms = Collections.unmodifiableList(Arrays.asList(this));
        hashCode = terms.hashCode();
        stringRep = initStringRep();
    }

    /**
     * Return a string representation of the Sum expression.
     * 
     * @return a string representation of the sum expression, where 1) every term in
     *         the sum is listed in the string and separated by a '+' symbol from
     *         the previous and next term in the returned string (if they exist),
     *         and 2) each of the terms in the left expression comes before the
     *         right, where left and right were the original arguments to the
     *         constructor of the Sum instance.
     * 
     *         The order of the summands in the string representation maintains
     *         structural equality as mentioned in the handout. No term of the Sum
     *         that is simply an addend of some Sum will have parentheses
     *         surrounding it. Only Sums that are terms in products within the Sum
     *         will have parentheses surrounding it. No other terms will have any
     *         parentheses.
     * 
     *         Any terms of the product that are variables, numbers, or Products
     *         follow the specifications of Variable, NonNegativeNum, and Sum
     *         respectively.
     */
    @Override
    public String toString() {
        return stringRep;
    }

    /**
     * Returns a boolean indicating if thatObject equals the current Sum instance.
     * 
     * @return true if and only if thatObject is an instance of Sum, and the terms
     *         of the Sum match the terms of the current Sum, in order by structural
     *         equality mentioned in the handout. Examples: Say p1,p2 are Sum
     *         instances representing p1 = x + y + z, and p2 = y + x + z. p1 does
     *         not equal() p2, as p1 and p2's terms are in different order. p3 = (x
     *         + y + z) + a equals p4 = x + (y + z + a) though, because even though
     *         they have different left/right groupings (referring to the arguments
     *         used to construct p3 and p4 instances), the terms are in the same
     *         order and have the same mathematical value.
     * 
     */
    @Override
    public boolean equals(Object thatObject) {
        if (!(thatObject instanceof Sum)) return false;
        Sum that = (Sum) thatObject;
        
        checkRep();
        return terms.equals(that.terms);
    }

    /**
     * Returns a hashcode for this Sum instance.
     * 
     * @return a hashcode, where if two Sum instances are equal, they have the same
     *         hashcode.
     * 
     */
    @Override
    public int hashCode() {
        return hashCode;
    }

    public static void main(String args[]) {
        System.out.println("here we are  " + 234.23423423334234234234234233423434 + "  " + (int) (34.9999));
        System.out.println((float) 1 == 1.00000001);

        BigDecimal big = new BigDecimal("234234.000000000000000000000000000000000000000000");
        BigDecimal big1 = new BigDecimal(23423);
        boolean result = big.equals(big1);

        System.out.println(big + "  " + big1 + "  " + result);

        int res = big.compareTo(big1);
        System.out.println(res);

        System.out.println(big);
        big = big.stripTrailingZeros();
        System.out.println(big.toString());

        List<String> arr = Arrays.asList("aaaa", "BBBBBB", "ZYZYZY");
        String joined = String.join("", arr);
        System.out.println(joined);
    }

    @Override
    public Expression differentiate(Variable var) {
        // TODO Auto-generated method stub
        /*
        List<Expression> diffSummands = new ArrayList<>() ;
        
        for (Expression e: this.terms) {
            Expression diff = e.differentiate(var);
            diffSummands.add(diff);
        }
        
        Expression result = new Sum (diffSummands.get(0), diffSummands.get(1) );
        int length = diffSummands.size();
        for (int i = 2; i < length; i ++) {
            result = new Sum (result, diffSummands.get(i));
        }
        
        return result;
        */
        //recursive method below:
        int length = this.terms.size();
        if (length == 2) {
            return new Sum(this.terms.get(0).differentiate(var),this.terms.get(1).differentiate(var));
        }
        else
            return new Sum ( new Sum (this.terms.subList(0, length-1)  ).differentiate(var),
                this.terms.get(length-1).differentiate(var)  );
    }

    @Override
    public Expression simplify(Map<Variable, NonNegativeNum> environment) {
        // TODO Auto-generated method stub
        
        boolean numeric = true;
        
        List <Expression> summands = new ArrayList<>();
        
        for (Expression e: terms) {
            Expression simple = e.simplify(environment);
            if (!simple.isNumeric()) numeric = false;
            summands.add(simple);
        }
        
        // System.out.println(summands);
        
        if (!numeric) return new Sum(summands);
        else {
            BigDecimal result = new BigDecimal(0);
            for (Expression term :summands) {
                NonNegativeNum number = (NonNegativeNum) term;
                
                //System.out.println(number);
                result = result.add(number.numericValue());
              
            }
            //System.out.println(result);
            return new NonNegativeNum(result);
        }
    }

    @Override
    public boolean isNumeric() {
        // TODO Auto-generated method stub
        return false;
    }
}
