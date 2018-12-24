//https://github.com/satnam-sandhu/ASTGenerator/blob/master/src/main/com/kitcode/ASTGenerator.java

import gramma.*;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.DOTTreeGenerator;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;


public class ast {

    static ArrayList<String> LineNum = new ArrayList<String>();
    static ArrayList<String> Type = new ArrayList<String>();
    static ArrayList<String> Content = new ArrayList<String>();

    public static void main(File[] pList){
        int i;
        for (i = 0; i < pList.length; i++){
            ArrayList<String> list = tools.findFile(pList[i]);
            String version = pList[i].getName().split("-")[1];
            File rFolder = new File("D:\\Workshop\\876\\ast_result\\" + pList[i].getName());
            rFolder.mkdirs();
            Runnable r = ()-> {
                try {
                    ast(list, rFolder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };
            new Thread(r).start();
        }
    }

    private static void ast(ArrayList<String> list, File target) throws IOException{
        for (String fName : list){
            File f = new File(fName);
            if (f.isFile())
                ast(f, target);
            else
                ast(tools.findFile(f), target);
        }
    }

    private static void ast(File f, File target) throws IOException{
        String fName = f.getName();
        String fType = fName.substring(fName.indexOf(".")+1);
        String inputString = readFile(f);
        ANTLRInputStream input = new ANTLRInputStream(inputString);
        CommonTokenStream tokens;
        ParserRuleContext ctx;
        if (fType.equals("cc")){
            CPP14Lexer lexer = new CPP14Lexer(input);
            tokens = new CommonTokenStream(lexer);
            CPP14Parser parser = new CPP14Parser(tokens);
            ctx = parser.translationunit();
        }
        else if (fType.equals("c")){
            CLexer lexer = new CLexer(input);
            tokens = new CommonTokenStream(lexer);
            CParser parser = new CParser(tokens);
            ctx = parser.compilationUnit();
        }
        else if (fType.equals("py")){
            AltPython3Lexer lexer = new AltPython3Lexer(input);
            tokens = new CommonTokenStream(lexer);
            AltPython3Parser parser = new AltPython3Parser(tokens);
            ctx = parser.file_input();
        }
        else
            return;


        generateAST(ctx, false, 0, fType);
//
//        String fpath = f.getAbsolutePath();
//        String temp = target.getName().split("-")[1];
//        int a = fpath.indexOf(temp);
//        int b = fpath.indexOf(fName);
//        File newf = new File(target + "\\" + fpath.substring(a + temp.length()+1, b));
//        newf.mkdirs();
//        PrintWriter pw = new PrintWriter(new FileWriter(newf + "\\" + fName + ".DOT"));
//        pw.println("digraph G {");
//        printDOT(pw);
//        pw.println("}");
//        pw.close();
    }

    private static void generateAST(RuleContext ctx, boolean verbose, int indentation, String fType) {
        boolean toBeIgnored = !verbose && ctx.getChildCount() == 1 && ctx.getChild(0) instanceof ParserRuleContext;

        if (!toBeIgnored) {
            String ruleName;
            if (fType.equals("cc"))
                ruleName = CPP14Parser.ruleNames[ctx.getRuleIndex()]; //change here
            else if (fType.equals("c"))
                ruleName = CParser.ruleNames[ctx.getRuleIndex()];
            else
                ruleName = AltPython3Parser.ruleNames[ctx.getRuleIndex()];
            LineNum.add(Integer.toString(indentation));
            Type.add(ruleName);
            Content.add(ctx.getText());
        }
        for (int i = 0; i < ctx.getChildCount(); i++) {
            ParseTree element = ctx.getChild(i);
            if (element instanceof RuleContext) {
                generateAST((RuleContext) element, verbose, indentation + (toBeIgnored ? 0 : 1), fType);
            }
        }
    }

    private static void printDOT(PrintWriter pw){
        printLabel(pw);
        int pos = 0;
        for(int i=1; i<LineNum.size();i++){
            pos=getPos(Integer.parseInt(LineNum.get(i))-1, i);
            pw.println((Integer.parseInt(LineNum.get(i))-1)+Integer.toString(pos)+"->"+LineNum.get(i)+i);
        }
    }

    private static void printLabel(PrintWriter pw){
        for(int i =0; i<LineNum.size(); i++){
            pw.println(LineNum.get(i)+i+"[label=\""+Type.get(i)+"\\n "+Content.get(i)+" \"]");
        }
    }

    private static int getPos(int n, int limit){
        int pos = 0;
        for(int i=0; i<limit;i++){
            if(Integer.parseInt(LineNum.get(i))==n){
                pos = i;
            }
        }
        return pos;
    }

    private static String readFile(File f) throws IOException {
        byte[] encoded = Files.readAllBytes(f.toPath());
        return new String(encoded, Charset.forName("UTF-8"));
    }
}
