package expressivo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Product implements Expression {

    /*
     * Rep invariant:
     * terms is a List with at least two items. 
     * summands is a list with only one item, the current Product instance this. 
     * stringRep is a String of at least 3 letters consisting of only letters, numbers, spaces, and '.','*','+','(',')' chars. 
     * It must have the '*' char. 
     * 
     * 
     * Abstraction Function: The sequential list of the terms represents the mathematical product of 
     * each Expression term in terms, in order obeying structural equality. 
     * 
     * Safety from rep exposure argument: 
     * terms is an immutable reference and constructed as an immutable List
     * summands arg is same as terms
     * stringRep is immutable reference and immutable object
     * hashCode is immutable object and reference.
     * 
     */
    
    List<Expression> terms;
    List<Expression> summands;
    private final String stringRep;
    private final int hashCode;

    private void checkRep() {
        assert terms.size() >1;
        assert summands.size() == 1;
        assert summands.get(0) == (this);
        Sum.assertValidString(stringRep, "*");
    }
    
    
    @Override
    public List<Expression> getSummands() {
        return summands;
    }

    @Override
    public List<Expression> getProdTerms() {
        return terms;
    }

    private String initStringRep() {
        List<String> result = new ArrayList<>();

        for (Expression e : terms) {
            String toAdd = e.toString();
            if (e.getSummands().size() > 1) {
                toAdd = "(" + toAdd + ")";
            }
            result.add(toAdd);
            result.add("*");
        }
        result.remove(result.size() - 1);

        return String.join("", result);
    }

    private static void addProductTerms(Expression e, List<Expression> addTo ) {
        for (Expression exp : e.getProdTerms()) {
            addTo.add(exp);
        }
    }
    
    /**
     * Returns a product expression representing the product of the left and right
     * expressions.
     * 
     * @param left  an expression
     * @param right another expression
     * @return An expression representing the product of the left and right
     *         expressions in order, where the left expression comes before the
     *         right expression in sequential order.
     */
    public Product(Expression left, Expression right) {
        List<Expression> sequence = new ArrayList<>();
        addProductTerms(left, sequence);
        addProductTerms(right, sequence);
        
        terms = Collections.unmodifiableList(sequence);

        summands = Collections.unmodifiableList(Arrays.asList(this));
        hashCode = terms.hashCode();
        stringRep = initStringRep();
    }

    private Product(List<Expression> sequence ) {
        terms = Collections.unmodifiableList(sequence);
        summands = Collections.unmodifiableList(Arrays.asList(this));
        hashCode = terms.hashCode();
        stringRep = initStringRep();
    }
    
    /**
     * Return a string representation of the product expression
     * 
     * @return a string representation of the product expression, where 1) every
     *         term in the product is listed in the string and separated by a '*'
     *         symbol from the previous and next term in the returned string (if
     *         they exist), and 2) each of the terms in the left expression comes
     *         before the right, where left and right were the original arguments to
     *         the constructor of the Product instance. The string representation
     *         maintains structural equality of each of the terms, as mentioned in
     *         the handout.
     * 
     *         Also, any term of a product that is a sum has one set of parentheses
     *         wrapped around it, while any other term has no extra parentheses
     *         surrounding it.
     * 
     *         Any terms of the product that are variables, numbers, or sums follow
     *         the specifications of Variable, NonNegativeNum, and Sum respectively.
     */
    @Override
    public String toString() {
        return stringRep;
    }

    /**
     * Returns a boolean indicating if thatObject equals the current Product
     * instance.
     * 
     * @return true if and only if thatObject is an instance of Product, and the
     *         terms of the product match the terms of the current product, in order
     *         by structural equality mentioned in handout. Examples: Say p1,p2 are
     *         Product instances representing p1 = x * y * z, and p2 = y * x * z. p1
     *         does not equal() p2, as p1 and p2's terms are in different order. p3
     *         = (x * y * z) * a equals p4 = x * (y * z * a) though, because even
     *         though they have different left/right groupings (from the arguments
     *         left and right originally used to construct each product), the terms
     *         are in the same order and have the same mathematical value.
     * 
     */
    @Override
    public boolean equals(Object thatObject) {
        if (!(thatObject instanceof Product)) return false;
        Product that = (Product) thatObject;
        
        checkRep();
        return terms.equals(that.terms);
    }

    /**
     * Returns a hashcode for this Product instance.
     * 
     * @return a hashcode, where if two Product instances are equal, they have the
     *         same hashcode.
     * 
     */
    @Override
    public int hashCode() {
        return hashCode;
    }


    static private Expression differentiateTwoProdTerms (Expression left, Expression right, Variable var) {
        Expression result; 
        
        Expression first = new Product (left, right.differentiate(var));
        Expression second = new Product (left.differentiate(var), right );
        result = new Sum(first,second);
        return result;
    }
    
    @Override
    public Expression differentiate(Variable var) {
        // TODO Auto-generated method stub
        int length = terms.size();
        if (length == 2) {
            return Product.differentiateTwoProdTerms(terms.get(0), terms.get(1), var);
        }   
        else {
            Expression result = Product.differentiateTwoProdTerms(new Product(terms.subList(0, length-1))
                , terms.get(length-1),var);
            return result;
        }
    }


    @Override
    public Expression simplify(Map<Variable, NonNegativeNum> environment) {
        // TODO Auto-generated method stub
        boolean numeric = true;
        
        List <Expression> prodTerms = new ArrayList<>();
        
        for (Expression e: terms) {
            Expression simple = e.simplify(environment);
            if (!simple.isNumeric()) numeric = false;
            prodTerms.add(simple);
        }
        
        if (!numeric) return new Product(prodTerms);
        else {
            BigDecimal result = new BigDecimal(1);
            for (Expression term : prodTerms) {
                NonNegativeNum number = (NonNegativeNum) term;
                result = result.multiply(number.numericValue());
            }
            return new NonNegativeNum(result);
        }
    }


    @Override
    public boolean isNumeric() {
        // TODO Auto-generated method stub
        return false;
    }
}
