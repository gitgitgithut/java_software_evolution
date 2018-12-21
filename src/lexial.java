import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class lexial {
    private static String version;
    private static File rFolder;

    /*public static void lexial(File[] pList){
        int i,j;
        for (i = 0; i < (pList.length - 1); i++){
            ArrayList<String> release1 = tools.findFile(pList[i], new ArrayList<String>());
            for (j = i + 1; j < pList.length; j++){
                ArrayList<String> release2 = tools.findFile(pList[j], new ArrayList<String>());
                version = tools.genVersion(pList[i], pList[j]);
                diff(release1, release2);
            }
        }
    }*/



    public static void lexial(File[] pList){
        int i;
        for (i = 0; i < (pList.length - 1); i++){
            ArrayList<String> release1 = tools.findFile(pList[i], new ArrayList<String>());
            ArrayList<String> release2 = tools.findFile(pList[i+1], new ArrayList<String>());
            version = tools.genVersion(pList[i], pList[i+1]);
            rFolder = new File("D:\\Workshop\\876\\lexial_Result\\" + version);
            rFolder.mkdirs();
            Runnable r = ()-> diff(release1, release2);
            new Thread(r).start();
        }
    }

    public static void diff(ArrayList<String> v1, ArrayList<String> v2){
        for (String pathf1: v1) {
            File f1 = new File(pathf1);
            boolean cond = f1.isFile();
            for (String pathf2 : v2) {
                File f2 = new File(pathf2);
                if (f1.getName().equals(f2.getName()) && (cond == f2.isFile())){
                    if (!cond)
                        diff(tools.findFile(f1), tools.findFile(f2));
                    else
                        diff(f1, f2);
                }
            }
        }
    }

    public static void diff(File f1, File f2){
        String f1Name = f1.getName();
        String f2Name = f2.getName();
        String[] f1typel = f1Name.split("\\.+");
        if (f1typel.length == 0)
            return;
        String f1type = f1typel[f1typel.length-1];
        if (f1type.equals("md") || f1type.contains("txt"))
            return;
        List<Double> xData = new LinkedList<Double>();
        List<Double> yData = new LinkedList<Double>();
        if (f1Name.equals(f2Name)){
            try {
                BufferedReader br1 = new BufferedReader(new FileReader(f1));
                String l1, l2;
                double count1 = 0;
                double count2;
                while ((l1 = br1.readLine()) != null){
                    count1++;
                    count2 = 1;
                    BufferedReader br2 = new BufferedReader(new FileReader(f2));
                    while ((l2 = br2.readLine()) != null){
                        if (l1.equals(l2)){
                            xData.add(count1);
                            yData.add(count2);
                        }
                        count2++;
                    }
                }
                //System.out.print("Constructing Img for " + f1.getAbsolutePath() + "\n");
                tools.genChart(xData, yData, f1, version, rFolder.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /*public static void diff(File f1, File f2){
        String f1Name = f1.getName();
        String f2Name = f2.getName();
        if (f1Name.equals(f2Name)){
            //f1Name = tools.removeSuffix(f1Name);
            //f2Name = tools.removeSuffix(f2Name);
            try {
                BufferedReader br1 = new BufferedReader(new FileReader(f1));
                File newFile = new File("D:\\Workshop\\876\\" + version + "\\" + f1Name + ".txt");
                newFile.getParentFile().mkdirs();
                PrintWriter pw = new PrintWriter(new FileWriter(newFile));
                String l1,l2;
                int count1 = 0;
                int count2;
                while ((l1 = br1.readLine()) != null){
                    count1++;
                    count2 = 1;
                    BufferedReader br2 = new BufferedReader(new FileReader(f2));
                    while ((l2 = br2.readLine()) != null){
                        if (l1.equals(l2))
                            pw.printf(";" + count1 + "," + count2);
                        count2++;
                    }
                }
                pw.close();
                tools.genChart(newFile, version);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/

}
