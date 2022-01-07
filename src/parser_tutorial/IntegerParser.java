package parser_tutorial;

import java.io.File;
import java.io.IOException;
import java.util.List;

import lib6005.parser.*;

public class IntegerParser {

    // Datatype definition of integer expression:
    // IntegerExpression = Number(n:int) + Plus(left:IntegerExpression,
    // right:IntegerExpression)

    static void visitAll(ParseTree<IntegerGrammar> node) {

        if (node.isTerminal())
            System.out.println(node.getName() + ":" + node.getContents());
        else {
            System.out.println(node.getName());
            for (ParseTree<IntegerGrammar> child : node)
                visitAll(child);
        }
    }

    static IntegerExpression buildAST(ParseTree<IntegerGrammar> p) {

        switch (p.getName()) {
        case NUMBER:
            return new Number(Integer.parseInt(p.getContents()));
        case PRIMITIVE:
            if (p.childrenByName(IntegerGrammar.NUMBER).isEmpty()) {
                return buildAST(p.childrenByName(IntegerGrammar.SUM).get(0));
            } else {
                return buildAST(p.childrenByName(IntegerGrammar.NUMBER).get(0));
            }
        case SUM:
            boolean first = true;
            IntegerExpression result = null;
            for (ParseTree<IntegerGrammar> child : p.childrenByName(IntegerGrammar.PRIMITIVE)) {
                if (first) {
                    result = buildAST(child);
                    first = false;
                } else {
                    result = new Plus(result, buildAST(child));
                }
            }
            if (first)
                throw new RuntimeException();
            return result;
        case ROOT:
            return buildAST(p.childrenByName(IntegerGrammar.SUM).get(0));
        case WHITESPACE:
            throw new RuntimeException();

        }
        throw new RuntimeException();
    }

    static void printNodes(ParseTree<IntegerGrammar> node, String indent){
        
        System.out.println(indent + node.getName() + ":" + node.toString());
        for (ParseTree<IntegerGrammar> child: node.children()){
            printNodes(child, indent + "  ");
        }
    }
    
    public static void main(String args[]) {

        Parser<IntegerGrammar> parser = null;
        try {
            parser = GrammarCompiler.compile(new File("src\\parser_tutorial\\IntegerExpression.g"),
                    IntegerGrammar.ROOT);
        } catch (UnableToParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String expression = " (( 34 ))  +  (       ( (  23  + 2 )  ) )     +   4";
        ParseTree<IntegerGrammar> tree = null;
        try {
            tree = parser.parse(expression);
        } catch (UnableToParseException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        System.out.println("tree string" +   tree.toString());
        // tree.display();
        
        printNodes(tree, "   ");
        
        IntegerExpression e = buildAST(tree);
        
        System.out.println("here is the tree" + e);
        
        
        
        // returns substring of original string corresponding to parse tree node
        String originalString = tree.getContents();

        assert expression.equals(originalString);
        System.out.println(originalString);

        // returns all children of the node, ordered by position in input
        List<ParseTree<IntegerGrammar>> children = tree.children();
        System.out.println(children);

        // gets children of a parsetree node.
        for (ParseTree<IntegerGrammar> child : children) {
            System.out.println(child + " terminal? :" + child.isTerminal());

            for (ParseTree<IntegerGrammar> grandchild : child) {
                System.out.println(grandchild + "  terminal?" + grandchild.isTerminal());
            }
        }

        // gets the enum type/symbole corresponding to the parse tree.
        IntegerGrammar symbol = tree.getName();
        System.out.println(symbol);

        visitAll(tree);

    }
}

enum IntegerGrammar {
    ROOT, SUM, PRIMITIVE, NUMBER, WHITESPACE
};