package expressivo;

import java.math.BigDecimal;
import java.util.List;

import lib6005.parser.ParseTree;


public class Ast {

    // ROOT, SUM, PRODUCT, PRIMITIVE, NUMBER, VARIABLE, WHITESPACE 
    
    static Expression buildAST(ParseTree<MathExpression> tree) {
        switch (tree.getName()) {
        
        case NUMBER:
            // System.out.println(tree.getContents() );
            return new NonNegativeNum ( new BigDecimal( tree.getContents()));   //  new Number(Integer.parseInt(tree.getContents()));
        case VARIABLE:
            return new Variable (tree.getContents());
        case PRIMITIVE:
            
           List< ParseTree<MathExpression>> sum =  tree.childrenByName(MathExpression.SUM);
            
           if (!sum.isEmpty()) 
               return buildAST(sum.get(0));
           
           List< ParseTree<MathExpression>> num = tree.childrenByName(MathExpression.NUMBER);
           
           if (!num.isEmpty())
               return buildAST(num.get(0));
           
           List< ParseTree<MathExpression>> var = tree.childrenByName(MathExpression.VARIABLE);
           
           if (!var.isEmpty())
               return buildAST(var.get(0));
           
        case SUM:
            boolean first = true;
            Expression result = null;
            for (ParseTree<MathExpression> child : tree.childrenByName(MathExpression.PRODUCT)) {
                if (first) {
                    result = buildAST(child);
                    first = false;
                } else {
                    result = new Sum(result, buildAST(child));
                }
            }
            if (first)
                throw new RuntimeException();
            return result;
        case ROOT:
            return buildAST(tree.childrenByName(MathExpression.SUM).get(0));
            
        case WHITESPACE:
            throw new RuntimeException();
        case PRODUCT:
            first = true; 
            result = null;
            for (ParseTree <MathExpression> child : tree.childrenByName(MathExpression.PRIMITIVE)) {
                if (first) {
                    result = buildAST(child);
                    first = false;
                }
                else {
                    result = new Product(result, buildAST(child) );
                }
            }
            if (first)
                throw new RuntimeException();
            return result; 
        }
        
        throw new RuntimeException();
    }
}
