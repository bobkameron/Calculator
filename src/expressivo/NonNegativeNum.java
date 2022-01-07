package expressivo;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class NonNegativeNum implements Expression {

    private final BigDecimal number;
    private final List<Expression> terms;
    private final int hashCode;
    private final String stringRep;
    
    static final NonNegativeNum zero = new NonNegativeNum(  new BigDecimal("0"));
    static final NonNegativeNum one = new NonNegativeNum( new BigDecimal("1"));
    
    /*
     * Rep invariant: 
     * number is a BigDecimal that has a value >= 0, with all
     * trailing zeros stripped. 
     * terms is a list with only one item: the current
     * NonNegativeNum instance this. 
     * stringRep is a nonempty string equal to
     * number.toPlainString().
     * 
     * 
     * Abstraction Function: The number representation directly represents a
     * mathematical numerical value.
     * 
     * Safety from rep exposure argument: The number field points to an immutable
     * datatype BigDecimal, and the pointer number itself is final and cannot be
     * modified, so this is an immutable representation. terms is constructed as an
     * immutable list, and the reference is immutable. hashCode and stringRep are
     * immutable pointers to immutable objects.
     * 
     */

    @Override
    public List<Expression> getSummands() {
        return terms;
    }

    @Override
    public List<Expression> getProdTerms() {
        return terms;
    }

    BigDecimal numericValue() {
        return number;
    }
    
    private void checkRep() {
        assert number.compareTo(new BigDecimal(0)) != -1;
        // assert number.toString().equals(number.toPlainString());
        assert terms.size() == 1;
        assert terms.get(0) == this;

        assert stringRep.equals(number.toPlainString());

    }

    /**
     * Returns a NonNegativeNum instance representing the number passed in as input.
     * 
     * @param number, a BigDecimal number greater than or equal to 0
     * @return a NonNegativeNum that exactly represents the number.
     */
    public NonNegativeNum(BigDecimal number) {
        this.number = number.stripTrailingZeros();
        terms = Collections.unmodifiableList(Arrays.asList(this));
        hashCode = this.number.hashCode();
        stringRep = this.number.toPlainString();

        checkRep();
    }

    /**
     * Returns a string representation of the NonNegative numeric expression.
     * 
     * @return a string representation that has the same mathematical value as the
     *         number passed in to originally construct NonNegativeNumber instance,
     *         with all trailing zeros stripped. A string value without an exponent
     *         field is returned, with no space characters in the string (only #'s
     *         and '.' chars in return value).
     */
    @Override
    public String toString() {
        checkRep();
        return stringRep;
    }

    /**
     * Returns a boolean indicating whether this NonNegativeNum expression is equal
     * to another object.
     * 
     * @param thatObject any Java object
     * @return True if and only if thatObject is an instance of NonNegativeNum, and
     *         NonNegativeNum represents a number equal to thatObject's. 2 numbers
     *         are equal if they have the same mathematical value, even if they have
     *         different decimal representations. eg, 2 and 2.0000000000 are both
     *         equal.
     * 
     */
    @Override
    public boolean equals(Object thatObject) {
        if (!(thatObject instanceof NonNegativeNum))
            return false;
        NonNegativeNum that = (NonNegativeNum) thatObject;

        checkRep();
        return number.compareTo(that.number) == 0;
    }

    /**
     * Returns the hashcode of the NonNegativeNum number.
     * 
     * @return an integer hashcode. Two NonNegativeNum instances that are equal()
     *         have the same hashcode.
     */
    @Override
    public int hashCode() {
        checkRep();
        return hashCode;
    }

    static public void main(String args[]) {
        // number.stripTrailingZeros().toPlainString()
        BigDecimal big = new BigDecimal(".00000000000000");
        String rep = big.toPlainString();
        System.out.println(rep);
        rep = big.stripTrailingZeros().toPlainString();
        System.out.println(rep);

        big = big.stripTrailingZeros();
        System.out.println(big + " " + big.scale());
        BigDecimal sample = new BigDecimal("0.000008");
        System.out.println(sample.scale() + "  " + sample);
    }

    @Override
    public Expression differentiate(Variable var) {
        // TODO Auto-generated method stub
        return NonNegativeNum.zero;
    }

    @Override
    public Expression simplify(Map<Variable, NonNegativeNum> environment) {
        // TODO Auto-generated method stub
        return this;
    }

    @Override
    public boolean isNumeric() {
        // TODO Auto-generated method stub
        return true;
    }

    
}
