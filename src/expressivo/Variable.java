package expressivo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Variable implements Expression {
    
    private final String name; 
    private final List <Expression> terms; 
    private final int hashCode; 
    
    
    /*
     * Rep invariant:
     * name is a String that is non-empty and has only alphabetic letters.
     * terms is a list that has only one item: the current Variable instance. 
     * 
     * Abstraction Function: 
     * The name directly represents a variable with the same letters, case-dependent. 
     * 
     * Safety from rep exposure argument:
     * The field name points to an immutable datatype String, and the pointer name itself
     * cannot be modified, so this is an immutable representation.
     * Also, terms is constructed as an unmodifiable list, so safe from rep exposure.  
     * hashCode is immutable.
     * 
     */
    
    private void checkRep() {
        try {
            checkName();
        } catch (IllegalArgumentException error) {
            throw new AssertionError ("Variable's name is not valid");
        }
        assert terms.size() == 1;
        assert terms.get(0) == this;
    }
    
    private void checkName() {
        if (name.isEmpty()) 
            throw new IllegalArgumentException();
        for(Character c : name.toCharArray()) {
            if (!Character.isAlphabetic(c))
                throw new IllegalArgumentException(); 
        }
    }
    
    @Override
    public List <Expression> getSummands() {
        return terms; 
    }
    @Override
    public List <Expression> getProdTerms () {
        return terms;
    }
    
    /**
     * Returns a variable expression with the name of the name parameter.
     * @param name - a nonempty string of only alphabetic letters. 
     * @return a variable expression representing a variable with the case-sensitive name of argument name.
     */
    public Variable (String name) {
        this.name = name;
        checkName();
        
        terms = Collections.unmodifiableList(Arrays.asList(this)); 
        hashCode = name.hashCode();
        checkRep();
    }
    
    /**
     * Returns the string representation of this variable.  
     * @return a string representation exactly equal to the String variable name originally used to construct the Variable instance.
     */
    @Override
    public String toString() {
        checkRep();
        return name;
    }
    
    /**
     * Returns a boolean indicating whether this variable expression is equivalent to another.
     * @param thatObject any Java object 
     * @return true if and only if thatObject is a Variable object, and the strings used to construct the Variables thatObject 
     * and the current instance are equal.
     */
    @Override
    public boolean equals(Object thatObject) {
        
        if (!(thatObject instanceof Variable)) return false;
        Variable that = (Variable) thatObject;
        
        checkRep();
        return name.equals(that.name);
    }
    
    /** 
     * Returns the hashcode of the variable.  
     * @return an integer hash code. If two Variable instances are equal(), they have the same hashcode. 
     */
    @Override
    public int hashCode() {
        checkRep();
        return hashCode;
    }
    
    static public void main (String args[]) {
        String s =  " asdfasdf s s s      f  ";
        s = s.replaceAll(" ", "");
        System.out.println(s);
        
        
    }

    @Override
    public Expression differentiate(Variable var) {
        // TODO Auto-generated method stub
        if (var.equals(this)) {
            return NonNegativeNum.one;
        }
        else return NonNegativeNum.zero;
    }

    @Override
    public Expression simplify(Map<Variable, NonNegativeNum> environment) {
        // TODO Auto-generated method stub
        if (environment.containsKey(this)) {
            return environment.get(this);
        }
        else return this;
    }

    @Override
    public boolean isNumeric() {
        // TODO Auto-generated method stub
        return false;
    }
}






