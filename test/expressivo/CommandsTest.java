/* Copyright (c) 2015-2017 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Tests for the static methods of Commands.
 */
public class CommandsTest {

    // Testing strategy
    /*
     * Partitions for Commands.differentiate are the same as for Expression.differentiate;
     * refer to ExpressionTest to view the partitions.
     * 
     * Partitions for Commands.simplify are the same as for Expression.simplify;
     * refer to ExpressionTest to view the partitions there. 
     * 
     * 
     */
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    // TODO tests for Commands.differentiate() and Commands.simplify()
       
    // Tests for simplifying
    
    static Map <String, Double> env ;
    static String var2 = "abc";
    static double num12 = 2.5;
    static {
        env = new HashMap <> ();
        env.put(var2 , num12 );
    }

    // Test for products of variables
    
    //static  Sum sumVarNum = new Sum(var2, number12);
    @Test
    public void testSimplifyProductVarInEnv() {
        String prod = "  abc * abc + (2 + 3*abc)";
        
        String result = Commands.simplify(prod, env);
        Double expected = env.get(var2);
        expected = expected * expected + (2 + 3*expected);
 
        //System.out.println(result + "  " + expected);
        assertEquals(result,expected.toString());
    }
    
    // // var10*number10 * number10*var10 = productOfProducts
    @Test public void testSimplifyProductVarNotInEnv() {
        String prod = "  kjg * zz * (ab + cd + 32)";
        
        String result = Commands.simplify(prod, env);
        String expected = "kjg*zz*(ab+cd+32)";
        
        assertEquals(result,expected);
    }
    
    // Test simplifying variables
    @Test 
    public void testSimplifyVarNotInEnv() {
        String var = "  a * b"; 
        String result = Commands.simplify(var, env);
        String expected = "a*b";
        assertEquals(expected,result);
    }
    
    // static  Sum sumOfSumsDiff = new Sum(sumVarNumRev, sumVarNum); // number12, var2, var2, number12
    @Test
    public void testSimplifySumVarInEnv() {
        String sum = "  ( abc + abc) +(( abc)) + 10" ;
        
        String result = Commands.simplify(sum, env);
        String expected = "17.5" ;
        
        assertEquals(result,expected);
    }
    
    static public void main (String args[]) {
        CommandsTest test = new CommandsTest();
        test.testSimplifySumVarInEnv();
    }
    
    
    // Test simplifying numbers
    @Test
    public void testSimplifyOneNum() {
        String number = ".56781";
        String result = Commands.simplify(number, env);
    
        assertEquals(result,"0" + number);
    }
    
    //Test simplifying sum/product of #'s
    @Test
    public void testSimplifyTwoNums() {
        
        String simplify = "123.200000+ 123.5"; 
        String result = Commands.simplify(simplify, env);
        String expected = "246.7";
        
        assertEquals(expected, result);
    }
    
    
    
    
    //Differentiation tests
    // Test differentiation of a number
    @Test
    public void testDifferentiateNum() {  
        String toDiff = "  .234324    ";
        String var = "zAzA";
        
        assertEquals (Commands.differentiate(toDiff ,var ), "0");
    }
    
    //Test differentiation of a variable
    
    @Test
    public void testDifferentiateVarWRTDiffVar () {
        String toDiff = "  abcDe +  abcdE + Abcde";
        String var = "abcde";
        assertEquals(Commands.differentiate(toDiff, var), "0+0+0");
    }
    
    @Test
    public void testDifferentiateWRTSameVar () {
        String toDiff = "    asdFE    ";
        String var = "asdFE";
        
        assertEquals (Commands.differentiate(toDiff, var), "1" ) ;
    }
    
    //Test differentiation of a sum
    //Sum sumOfSums = new Sum(sumVarNum, sumVarNumRev); // var2, number12, number12, var2
    
    @Test 
    public void testDifferentiateSums () {
        String toDiff = "g +  a + y +  a + ((( 45.0000 + 32.000000*a )) + b + c )";
        String var = "a";
        String result = "0+1+0+1+0+32*1+0*a+0+0";
        
        assertEquals(Commands.differentiate(toDiff, var), result )  ;
    }
    
    //Test product differentiation
    
    @Test
    public void testDifferentiateProductTwoVars() {
        String toDiff = " (( z*234*abc )  + (  x*x + y*y  )  )  ";
        String var = "x";
        String result = "z*234*0+(z*0+0*234)*abc+x*1+1*x+y*0+0*y";
        assertEquals(Commands.differentiate(toDiff, var), result );
    }
    
    @Test
    public void testDifferentiateProductMultVars() {
        String toDiff = "  d*e+  32*ZZ*ZZ   ";
        String var = "ZZ";
        String result = "d*0+0*e+32*ZZ*1+(32*1+0*ZZ)*ZZ";
        assertEquals(Commands.differentiate(toDiff, var), result );
    }
}
