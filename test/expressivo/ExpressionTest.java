/* Copyright (c) 2015-2017 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * Tests for the Expression abstract data type.
 */
public class ExpressionTest {

    // Testing strategy
    /* 
     * Testing strategy for each implementation class and producer methods in
     * Expression: We test each method at least once, not the full Cartesian
     * product.
     * 
     * Tests for Variable:
     * 
     * toString(): partition input and make comparisons between 1) Variable objects
     * constructed with different case strings (otherwise same String) 2) the same
     * case and same sequence of letters, and 3) different sequence of letters
     * 
     * equals(Object): partitions same as for toString()
     * 
     * hashCode(): partitions same as for toString()
     * 
     * Call both constructor (from Variable class) and producer method (make(String)
     * from Expression) to create Variables to test above methods.
     * 
     * 
     * Tests for NonNegativeNum:
     * 
     * toString(): partition input and make comparisons between 1) NonNegativeNum
     * objects constructed w/ numbers w/ same value and different # of decimal
     * places (eg .2 and .2000 or 12.0 and 12.0000). 2) same value and same # of
     * decimal places, 3) different mathematical values.
     * 
     * equals(Object): same partitions as for toString()
     * 
     * hashCode(): same partitions as for toString()
     * 
     * Call both the constructor and producer method(from Expression) to create
     * NonNegativeNums to test above methods
     * 
     * 
     * Tests for Product: toString(): partition input and make comparisons between
     * 1) Products constructed as the product of products 2) Product instances that
     * are the product of a sum and any other Expression, 3) Products that are the
     * product of a Variable or NonNegativeNum instance. 4) For each of the above
     * cases, also test the cases where the left and right arguments to the
     * constructor of the Product instance are simply swapped.
     * 
     * equals(Object): same partitions as for toString()
     * 
     * hashCode(): same partitions as above
     * 
     * Call both constructor and producer method to create Product objects to test.
     * 
     * 
     * Tests for Sum: toString(): partition input and make comparisons between 1)
     * Sum constructed as the Sum of Sums 2) Sums constructed as sum of any
     * Expression and a Product, 3) Sum of a Variable or NonNegativeNum instance. 4)
     * For each of above cases, also test cases where left and right arguments to
     * constructor of Sum instance are swapped
     * 
     * equals(Object): same partitions as for toString()
     * 
     * hashCode(): same partitions as above
     * 
     * Call both constructor and producer method above to create Product objects to
     * test.
     */

    /*
     * Testing strategy for Expression.parse(String input):
     * 
     * Partition the input string as follows:
     * 
     * Schema: 
     * primitive- num,var 
     * 
     * sum: 
     * sum of 2 primitives
     * sum of sum and sum
     * sum of sum and prod
     * sum of sum and primitive
     * 
     * product:
     * product of 2 primitives
     * prod of prod and prod
     * prod of prod and sum
     * prod of prod and primitive
     * 
     * 1) Input string is composed of only a primitive (a num or var)
     * 
     * 2) Input string's highest order expression is a sum, which can be a
     * i) sum of 2 or more sums,
     * ii) sum of 1 or more sums with 1 or more products,
     * iii) sum of a sum and a primitive
     * 
     * 3) Input string's highest order expression is a product, which can be a
     * i) product of 2 or more products, 
     * ii) product of 1 or more products and 1 or more sums
     * iii) product of a product and a primitive 
     * 
     * 4) Input string is illegal, in that we have (this is not an exhaustive list of all possible invalid inputs) : 
     * a) letters other than alphanumeric, '(', ')', '*', '+', or '.' 
     * b) two primitives w/out a '+' or '*' between them 
     * c) input string is the empty string
     * d) Parentheses are not matched
     * e) the symbols '(', ')', '*', '+', or '.' occurring with no primitives in the string 
     *  
     * 
     * We also have to cover corner cases: where there are more extra parentheses or extra spaces than the minimal input
     * string that represents the same expression. eg Expression returned from parse("3+4") should equal pars(" 3 + ((4))")  
     * 
     * Also, in the above cases we should test cases where we swap the order of the terms in the sum and product.
     * 
     * We will test each case at least once. 
     * 
     */
    
    /*
     * Test cases for the input to differentiate(Variable var) :
     * 
     * 1) for Case of a NonNegativeNum, simply test that the derivative equals 0
     * 
     * 2) for Variable, simply test that the derivative equals 0 when Variable != var passed in as parameter.
     * Also test that dvar/dvar = 1
     * 
     * 3) For sum, test that the differentiation of multiple expressions satisfies structural equality of the sum. 
     * 
     * 4) For product, test that the expression returned satisfies the spec. Test with 2 product terms in the
     * current expression, and test with more than 2 to see that they obey the spec.
     * 
     * 
     */
    
    /*
     * Test cases for the input to simplify (Map<Variable,NonNegativeNum> environment):
     * 
     * 1) Test case where the expression is simply a i) number or ii) sum or product of numbers, 
     * and environment could have any # of variables. The returned expression must be a single #.
     * 
     * 2) Expression just involves a variable, where the variable i) is mapped in environment, or ii) is not mapped.
     * 
     * 3) Expression involves a sum of variables, where i)no variable is mapped in environment, or ii) at least 1 is 
     * mapped 
     * 
     * 4) Expression involves a product of variables, where i)no variable is mapped in environment, or 
     * ii) at least 1 is mapped. 
     * 
     * 
     */
    
    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    } 
    
    
    static String name10 = "abcd"; 
    static String name11 = "abcd";
    static String name12 = "abcD"; 
    static String name2 = "abcde";

    
    static Variable var10 = new Variable(name10);
    static Expression var11 = Expression.make(name11);
    static Variable var12 = new Variable(name12);
    static Expression var2 = Expression.make(name2);

    static BigDecimal num10 = new BigDecimal("123");
    static BigDecimal num11 = new BigDecimal(123);
    static BigDecimal num12 = new BigDecimal("123.00000000000000000000");
    static BigDecimal num2 = new BigDecimal(123.0001);

    static  Expression number10 = Expression.make(num10);
    static  NonNegativeNum number11 = new NonNegativeNum(num11);
    static  Expression number12 = Expression.make(num12);
    static  NonNegativeNum number2 = new NonNegativeNum(num2);

    static  Product productNumVar = new Product(var10, number10);

    static  Product productNumVarRev = new Product(number10, var10);

    static  Sum sumVarNum = new Sum(var2, number12);
    static   Sum sumVarNumDupl = new Sum(new Variable(name2), number11);
    static  Sum sumVarNumRev = new Sum(number12, var2);

    // (var2 + number12) * prod(number10*var10)
    static  Product productOfSum = new Product(sumVarNum, productNumVarRev);

    // var10*number10 * number10*var10 = productOfProducts
    // number10*var10 * var10*number10 = product of products diff
    static   Product productOfProducts = new Product(productNumVar, productNumVarRev);
    static    Product productOfProductsDupl = new Product(new Product(new Product(var10, number10), number10), var10);
    static   Product productOfProductsDiff = new Product(productNumVarRev, productNumVar);

    static   Sum sumOfSums = new Sum(sumVarNum, sumVarNumRev); // var2, number12, number12, var2
    static   Sum sumOfSumsDupl = new Sum(new Sum(var2, new Sum(number12, number12)), var2);
    static  Sum sumOfSumsDiff = new Sum(sumVarNumRev, sumVarNum); // number12, var2, var2, number12

    static  Sum sumOfProduct = new Sum(sumVarNum, productNumVar); // var2 + number12 + var10 * number10

    
  
  
  
    // Product products, product sum * expression, product num & var
    // Sum of sums, sum of product * expression, Sum of num & var

    static private String stripWhiteSpace(String string) {
        return string.replaceAll(" ", "");
    }

    static private void checkContainsAll(String string, List<String> checkContains) {
        for (String val : checkContains) {
            assert string.contains(val);
        }
    }

    static private String getSumString(List<String> symbols) {
        String s = "";
        for (String p : symbols) {
            s += p;
            s += "+";
        }
        s = s.substring(0, s.length() - 1);
        return s;
    }

    // TODO tests for Expression
   
    static NonNegativeNum zero = new  NonNegativeNum( new BigDecimal("0"));
    static NonNegativeNum one = new  NonNegativeNum( new BigDecimal("1"));
    
    /*
     * Test cases for the input to simplify (Map<Variable,NonNegativeNum> environment):
     * 
     * 1) Test case where the expression is simply a i) number or ii) sum or product of numbers, 
     * and environment could have any # of variables. The returned expression must be a single #.
     * 
     * 2) Expression just involves a variable, where the variable i) is mapped in environment, or ii) is not mapped.
     * 
     * 3) Expression involves a sum of variables, where i)no variable is mapped in environment, or ii) at least 1 is 
     * mapped 
     * 
     * 4) Expression involves a product of variables, where i)no variable is mapped in environment, or 
     * ii) at least 1 is mapped. 
     * 
     * 
     */
    
    static Map<Variable,NonNegativeNum> env;
    
    static {
        env = new HashMap<>();
        env.put((Variable)var2, number2);
    }
    
    // Tests for simplify (Map<Variable,NonNegativeNum> environment)
    
    // Test for products of variables
    
    //static  Sum sumVarNum = new Sum(var2, number12);
    @Test
    public void testSimplifyProductVarInEnv() {
        Product prod = new Product(sumVarNum, var2  );
        Expression result = prod.simplify( env);
        Expression expected = new NonNegativeNum( num2.multiply(  num2.add(num12)) );
        assertEquals(result,expected);
    }
    
    // // var10*number10 * number10*var10 = productOfProducts
    @Test public void testSimplifyProductVarNotInEnv() {
        Expression result = productOfProducts.simplify(env);
        assertEquals(result,productOfProducts);
    }
    
    // Test simplifying variables
    @Test 
    public void testSimplifyVarNotInEnv() {
        Expression result = var11.simplify(env);
        assertEquals(result, var11);
    }
    
    // static  Sum sumOfSumsDiff = new Sum(sumVarNumRev, sumVarNum); // number12, var2, var2, number12
    @Test
    public void testSimplifySumVarInEnv() {
        Expression result = sumOfSumsDiff.simplify(env);
        Expression expected = new NonNegativeNum(num12.add(num12).add(num2).add(num2));
        assertEquals(result,expected);
    }
    
    // Test simplifying numbers
    @Test
    public void testSimplifyOneNum() {
        assertEquals(number11.simplify(env), number12 );
        assertFalse(number2.simplify(env).equals(number10));
    }
    
    //Test simplifying sum/product of #'s
    @Test
    public void testSimplifyTwoNums() {
        Expression sum = new Sum(number10, number2 );
        Expression result = sum.simplify(env);
        
        Expression expected= new NonNegativeNum(  num10.add(num2));
        //System.out.println(number10.toString() + "   "+ number2.toString() ); 
        // System.out.println(result.toString() + "  " + expected.toString() );
        
        assertEquals(expected, result);
    }
    
    
    // Tests for differentiate(Variable var)
    
    // Test differentiation of a number
    @Test
    public void testDifferentiateNum() {
        assertEquals( number10.differentiate((Variable) var2), zero  );
        assertEquals ( number11.differentiate(  var10), zero  );
        assertEquals ( number2.differentiate((Variable) var11),zero );
    }
    
    //Test differentiation of a variable
    
    @Test
    public void testDifferentiateVarWRTDiffVar () {
        assertEquals ( var10.differentiate( new Variable( name12 ))  , zero);
    }
    
    @Test
    public void testDifferentiateWRTSameVar () {
        assertEquals ( var11.differentiate( new Variable( name10 ))  , one);
    }
    
    //Test differentiation of a sum
    //Sum sumOfSums = new Sum(sumVarNum, sumVarNumRev); // var2, number12, number12, var2
    
    @Test 
    public void testDifferentiateSums () {
        Variable wrt = new Variable("a");
        Sum thesum = (Sum) Expression.parse("g +  a + y +  a + ((( 45 + 32*a )) + b + c )");
        Expression diff = thesum.differentiate(wrt);
        
        Sum mustEqual = (Sum) Expression.parse("  0 +  1 + 0 +  1 + 0 + ((( 32*1 + 0*a ))) + 0 + 0  ");
        
        //System.out.println("Diff: " +   diff.toString());
        // System.out.println("mustEqual: " + mustEqual.toString());
        assert diff.equals( mustEqual);
    }
    
    //Test product differentiation
    
    @Test
    public void testDifferentiateProductTwoVars() {
        String toDiff = " (( z*234*abc )  + (  x*x + y*y  )  )  ";
        String expected = " ( z*234*0 + (z*0 + 0*234  )*abc +    (   x*1 + 1*x)) + y*0 + 0*y   ";
        Expression diff =   Expression.parse(toDiff).differentiate(new Variable("x"))    ;
        Expression needed = Expression.parse(expected);
        
        //System.out.println("diff equals " +  diff.toString());
        //System.out.println("needed equals " + needed.toString() );
        
        assertEquals(diff,needed);
    }
    
    @Test
    public void testDifferentiateProductMultVars() {
        String toDiff = "  d*e+  32*ZZ*ZZ   ";
        String expected = " d*0 + 0*e +    32*ZZ*1 +  ( 32*1 + 0*ZZ )*ZZ  ";
        Expression express = Expression.parse(toDiff);
        Variable diffWrt = new Variable("ZZ");

        Expression diff = express.differentiate(diffWrt);   
        Expression needed = Expression.parse(expected);
        
        // System.out.println(diff.toString());
        // System.out.println(needed.toString());
        
        assertEquals(needed,diff);
    }
    
    
    // Tests for Expression.parse
    /*
    Schema: 
        * primitive- num,var 
        * 
        * sum: 
        * sum of 2 primitives
        * sum of sum and sum
        * sum of sum and prod
        * sum of sum and primitive
        * 
        * product:
        * product of 2 primitives
        * prod of prod and prod
        * prod of prod and sum
        * prod of prod and primitive
    */
    
    //Test input strings w/ a sum
    
    // Sum sumVarNum = new Sum(var2, number12); reversed is reverse
    @Test
    public void testParseSumOfPrimitives () {
        String sum =  "(  (  ( " + var2.toString() + " )  )   +  ((( " + number12.toString() + ")))   ) "  ;
        Expression esum = Expression.parse(sum);
        assertEquals(sumVarNum, esum);
    }
    
    @Test
    public void testParseSumOfSumAndPrimitive() {
        String sum = name10 + " + " + sumVarNumRev.toString();
        Expression esum = Expression.parse(sum);
        assertEquals( new Sum(var10, sumVarNumRev), esum     );
    }
    
    @Test
    public void testParseSumofSums() {
        String sum =  number12.toString() + "  + (( " + var2.toString() + " +  "+ 
                var2.toString() + " ) +" + number12.toString() + ")"  ;
        Expression esum = Expression.parse(sum);
        assertEquals(esum, sumOfSumsDiff  );
    }
    
    //Sum sumOfProduct = new Sum(sumVarNum, productNumVar); // var2 + number12 + var10 * number10
    // Product productNumVarRev = new Product(number10, var10);
    @Test
    public void testParseSumofSumAndProd() {
        String sum =  var2.toString() + " + " + number12.toString() + " + " + var10.toString() + "*" + number10.toString(); 
        Expression esum = Expression.parse(sum);
        assertEquals(esum, sumOfProduct );
    }
    
    // Test input strings w/ a product
    
    // Product productNumVarRev = new Product(number10, var10);
    @Test
    public void testParseProductOfPrimitives () {
        String prod = "( ( " + number10.toString() + ")  * ((  " + var10.toString() + " ))   )" ;
        Expression eprod = Expression.parse(prod);
        assertEquals(eprod,productNumVarRev);
    }
    
    // var10*number10 * number10*var10 = product of products
    // number10*var10 * var10*number10 = product of products diff
    @Test 
    public void testParseProductOfPrimAndProd() {
        String prod =  var2.toString() + " * (  (( " + var10.toString() + " ))  *  "  + number10.toString() + " )"   ;  
        Expression eprod = Expression.parse(prod);
        assertEquals(eprod, new Product(var2, productNumVar));
    }
    
  //Sum sumOfProduct = new Sum(sumVarNum, productNumVar); // var2 + number12 + var10 * number10
    @Test
    public void testParseProductOfSumAndProd() {
        String prod = " ( " + var2.toString() + "  + " + number12.toString() + " + " + var10.toString() + 
                " * " + number10.toString() + " ) *" + productNumVarRev.toString() ;
        Expression eprod = Expression.parse(prod);
        assertEquals(eprod, new Product( sumOfProduct   ,productNumVarRev ) );
    }
    
    @Test
    public void testParseProductOfProducts () {
        String prod = productNumVar.toString() + "   *  ((  " + productNumVarRev.toString() + "  )    ) "   ;
        Expression eprod = Expression.parse(prod);
        assertEquals(eprod, new Product ( productNumVar, productNumVarRev) );
        
    }
    
    //Test input string w/ only primitives 
    
    @Test
    public void testParsePrimitives() {
        Expression v10 =  Expression.parse(name10);
        assertEquals( v10, var10  );
        
        Expression n2 = Expression.parse(num2.toString());
        assertEquals(n2, number2 );
        
        Expression v10_ = Expression.parse("     (  ((       " + name10 + "    )))       ");
        assertEquals(var10,v10_);
        
        Expression n2_ = Expression.parse(" ( ( ((    " + num2.toString() + ")))    )       ");
        assertEquals(n2_, number2);
        
    }
    
    // Test invalid / illegal input string 
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyString() {
        Expression.parse(""); 
    }
    
    
    private void testParseInvalidString(String s) {
        boolean out = true;
        
        try {
        Expression.parse(s); 
        } catch ( IllegalArgumentException e) {
          out = false; 
          throw e;  
          
        } finally {
          if (out)  
            assert false;
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidStrings() {
        List <String> invalids = Arrays.asList(" ((    . ) )", " *   ", " (  +    )", " ((()))");
        for (String s: invalids) {
            testParseInvalidString(s);
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidChars() {
        // our implementation does not support negative numbers 
        Expression.parse(" a * b * c * d * e * f * g * -5 "); 
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseOperatorMissing() {
        Expression.parse("        a        9.000000"); 
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseParensUnmatched() {
        Expression.parse("     (  9 * 34234 +  ( 23234 * (( a + b   ) ) )   "); 
    }

    // Tests for Sum Implementation

    @Test
    public void testSumNumAndVar() {
        // abcde + 123 expected for sumVarNum string
        assertTrue(sumVarNum.equals(sumVarNumDupl));

        String expected = "abcde+123";
        String sumVarNumString = sumVarNum.toString();
        assertTrue(stripWhiteSpace(sumVarNumString).equals(expected));
        assertTrue(sumVarNumString.contains(name2) && sumVarNumString.contains("123"));
        assertTrue(sumVarNum.hashCode() == sumVarNumDupl.hashCode());

        assertFalse(sumVarNum.equals(sumVarNumRev));
        assertFalse(sumVarNum.toString().equals(sumVarNumRev.toString()));
        String expectedRev = "123+abcde";
        String revString = sumVarNumRev.toString();
        assertTrue(stripWhiteSpace(revString).equals(expectedRev));
        assertTrue(revString.contains("123") && revString.contains("abcde"));
    }

    @Test
    public void testSumOfSums() {
        // sumOfSums and duplicate var2, number12, number12, var2
        // sumOfSumsRev number12, var2, var2, number12

        List<String> listStrings = Arrays.asList(var2.toString(), number12.toString());

        assertEquals(sumOfSums, sumOfSumsDupl);
        assertEquals(sumOfSums.hashCode(), sumOfSumsDupl.hashCode());
        String sumString = sumOfSums.toString();
        String sumStringDupl = sumOfSumsDupl.toString();
        String sumSpaceStripped = getSumString(
                Arrays.asList(var2.toString(), number12.toString(), number12.toString(), var2.toString()));

        assertEquals(stripWhiteSpace(sumString), stripWhiteSpace(sumStringDupl));
        assertEquals(stripWhiteSpace(sumString), sumSpaceStripped);
        checkContainsAll(sumString, listStrings);

        assertFalse(sumOfSums.equals(sumOfSumsDiff));
        assertFalse(sumOfSums.toString().equals(sumOfSumsDiff.toString()));

        String sumRevSpaceStripped = getSumString(
                Arrays.asList(number12.toString(), var2.toString(), var2.toString(), number12.toString()));
        assertEquals(stripWhiteSpace(sumOfSumsDiff.toString()), sumRevSpaceStripped);
    }

    @Test
    public void testSumOfProduct() {
        // var2 + number12 + var10 * number10
        String result = var2.toString() + "+" + number12.toString() + "+" + new Product(var10, number10).toString();
        result = stripWhiteSpace(result);
        assert result.equals(stripWhiteSpace(sumOfProduct.toString()));
    }

    // Tests for Product Implementation

    @Test
    public void testProductNumAndVar() {
        // var10, number10 = productnumvar, reverse is rev

        assertFalse(productNumVar.equals(productNumVarRev));
        assertFalse(productNumVar.toString().equals(productNumVarRev.toString()));

        String stringNoSpace = stripWhiteSpace(productNumVar.toString());

        String expected = stripWhiteSpace(var10.toString() + "*" + number10.toString());

        assertEquals(stringNoSpace, expected);

        checkContainsAll(stringNoSpace, Arrays.asList(var10.toString(), number10.toString()));

    }

    @Test
    public void testProductOfProducts() {
        // product of products and duplicate = var10*number10 * number10*var10

        // product of products reverse = number10*var10 * var10*number10
        
        // error 
        //System.out.println(productOfProducts.toString());
        // System.out.println(productOfProductsDupl.toString()); 
        
        assertTrue(productOfProducts.equals(productOfProductsDupl));
        assertTrue(productOfProducts.hashCode() == productOfProductsDupl.hashCode());
        
        String productNoSpace = stripWhiteSpace(productOfProducts.toString());
        String productDuplNoSpace = stripWhiteSpace(productOfProductsDupl.toString());
        
        assertEquals(productNoSpace, productDuplNoSpace);
        String expected = stripWhiteSpace(
                var10.toString() + "*" + number10.toString() + "*" + number10.toString() + "*" + var10.toString());
        assertEquals(expected, productNoSpace);
        checkContainsAll(productNoSpace, Arrays.asList(var10.toString(), number10.toString()));

        assertFalse(productOfProducts.equals(productOfProductsDiff));
        assertFalse(productOfProducts.toString().equals(productOfProductsDiff.toString()));

        String productRevNoSpace = stripWhiteSpace(productOfProductsDiff.toString());
        String expectedRev = stripWhiteSpace(
                number10.toString() + "*" + var10.toString() + "*" + var10.toString() + "*" + number10.toString());
        assertTrue(expectedRev.equals(productRevNoSpace));
    }

    @Test
    public void testProductOfSum() {
        // (var2 + number12) * prod(number10*var10)
        Expression equivalent = new Product(new Sum(var2, number10), new Product(number12, var11));

        assert productOfSum.equals(equivalent);
        assert productOfSum.hashCode() == equivalent.hashCode();

        String productNoSpace = stripWhiteSpace(productOfSum.toString());
        String equivalentNoSpace = stripWhiteSpace(equivalent.toString());
        assertEquals(productNoSpace, equivalentNoSpace);

        String expectedNoSpace = stripWhiteSpace("(" + var2.toString() + "+" + number12.toString() + ")" + "*"
                + number10.toString() + "*" + var10.toString());
        // error 
        assertEquals(productNoSpace, expectedNoSpace);

        checkContainsAll(productNoSpace,
                Arrays.asList(var2.toString(), number12.toString(), number10.toString(), var10.toString()));
    }

    // Tests for NonNegativeNum Implementation

    @Test
    public void testSameTwoNumsSameDecimals() {
        assertTrue(new BigDecimal(number10.toString()).compareTo(new BigDecimal(number11.toString())) == 0);
        assertEquals(number10.toString(), number11.toString());
        assertTrue(number10.equals(number11));
        assertTrue(number10.hashCode() == number11.hashCode());
    }

    @Test
    public void testSameTwoNumsDiffDecimals() {
        assertTrue(new BigDecimal(number11.toString()).compareTo(new BigDecimal(number12.toString())) == 0);
        assertEquals(number10.toString(), number12.toString());
        assertEquals(number11,number12);
        assertTrue(number10.hashCode() == number12.hashCode());
    }

    @Test
    public void testTwoDiffNums() {
        assertFalse(new BigDecimal(number11.toString()).compareTo(new BigDecimal(number2.toString())) == 0);
        assertFalse(number10.toString().equals(number2.toString()));
        assertFalse(number12.equals(number2));
    }

    // Tests for Variable Implementation

    @Test
    public void testSameTwoVariables() {
        assert var10.toString().equals(var11.toString());
        assert var10.equals(var11);
        assert var10.hashCode() == var11.hashCode();
    }

    @Test
    public void testTwoVariablesDiffCases() {
        assertFalse(var11.toString().equals(var12.toString()));
        assertFalse(var10.equals(var12));
    }

    @Test
    public void testTwoVariablesDiff() {
        assertFalse(var11.toString().equals(var2.toString()));
        assertFalse(var10.equals(var2));
    }

}
