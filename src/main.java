import java.io.*;
import java.util.Arrays;


public class main {

    public static void main(String[] args){
        File[] pList = (new File(args[0])).listFiles();
        tools.sortVer(pList);
        //lexial.lexial(pList);
        ast_test.main(pList);

    }
}

