import org.knowm.xchart.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class tools {

    public static ArrayList<String> findFile(File f){
        ArrayList<String> dirList = new ArrayList<>();
        return findFile(f, dirList);
    }

    public static ArrayList<String> findFile(File f, ArrayList<String> dirList){
        if (!f.isFile()){
            File[] subfolder = f.listFiles();
            for (File file : subfolder) {
                dirList = findFile(file, dirList);
            }
        }
        else{
            dirList.add(f.getAbsolutePath());
        }
        return dirList;
    }

    public static int matchFile(ArrayList<File> flist, File f){
        String fname = f.getName();
        for (File file : flist){
            if (fname.equals(file.getName()))
                return flist.indexOf(file);
        }
        return -1;
    }

    public static String removeSuffix(String s){
        int x = s.indexOf(".");
        if (x == 0)
            return s.substring(1);
        else if (x == -1)
            return s;
        else
            return s.substring(0, s.indexOf("."));
    }

    public static String genVersion(File a, File b){
        String aName = a.getName();
        String bName = b.getName();
        int x = aName.indexOf("-")+1;
        int y = bName.indexOf("-")+1;
        return aName.substring(x) + " vs " + bName.substring(y);
    }

    public static void genChart( List<Double> xData, List<Double> yData, File dataSrc, String version, String target) throws IOException {
        if (xData.size() == 0 || yData.size() == 0)
            return;
        String fName = dataSrc.getName();
        String path = dataSrc.getAbsolutePath();
        XYChart chart = new XYChartBuilder().build();
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        chart.getStyler().setChartTitleVisible(true);
        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setMarkerSize(3);
        chart.getStyler().setAxisTitlesVisible(true);
        String[] temp = version.split(" vs ");
        chart.setXAxisTitle(temp[0]);
        chart.setYAxisTitle(temp[1]);
        int a = path.indexOf(temp[0]);
        path = target + "\\" + path.substring(a + temp[0].length()+1);
        File f = new File(path.substring(0, path.indexOf(fName)));
        f.mkdirs();
        chart.setTitle(fName);
        chart.addSeries(fName, xData, yData);
        BitmapEncoder.saveBitmap(chart, path, BitmapEncoder.BitmapFormat.PNG);
    }

    public static void sortVer(File[] list){
        File temp;
        String fName1, fName2;
        String[] f1, f2;
        int i,j, n = list.length;
        for (i = 0; i < n-1; i++){
            for (j = 0; j < n-i-1; j++){
                fName1 = list[j].getName();
                fName2 = list[j+1].getName();
                f1 = fName1.substring(fName1.indexOf("-")+1).split("\\.+");
                f2 = fName2.substring(fName2.indexOf("-")+1).split("\\.+");
                if (verComparator(f1, f2)){
                    temp = list[j];
                    list[j] = list[j+1];
                    list[j+1] = temp;
                }
            }
        }
    }
    //return true if f1 > f2
    public static boolean verComparator(String[] f1, String[] f2){
       return verComparator(f1, f2, 0);
    }

    public static boolean verComparator(String[] f1, String[] f2, int i){
        int a = Integer.parseInt(f1[i]);
        int b = Integer.parseInt(f2[i]);
        if (a > b)
            return true;
        else if (a < b)
            return false;
        else
            return verComparator(f1, f2, i+1);
    }

    /*public static void genChart(File dataSrc, String version){
        List<Double> xData = new LinkedList<Double>();
        List<Double> yData = new LinkedList<Double>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(dataSrc));
            String line;
            int count = 0;
            while ((line = br.readLine()) != null){
                String[] ptList = line.split(";");
                for (String pt : ptList){
                    if (count != 0){
                        String[] coord = pt.split(",");
                        xData.add(Double.parseDouble(coord[0]));
                        yData.add(Double.parseDouble(coord[1]));
                    }
                    count++;
                }
             }
             XYChart chart = new XYChartBuilder().build();
            if (count == 0)
                return;
            chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
            chart.getStyler().setChartTitleVisible(true);
            chart.getStyler().setLegendVisible(false);
            chart.getStyler().setMarkerSize(5);
            chart.getStyler().setAxisTitlesVisible(true);
            chart.setXAxisTitle(version.split(" vs ")[0]);
            chart.setYAxisTitle(version.split(" vs ")[1]);
            chart.setTitle(dataSrc.getName().substring(0, dataSrc.getName().length()-4));
            chart.addSeries(dataSrc.getName(), xData, yData);
            BitmapEncoder.saveBitmap(chart, dataSrc.getAbsolutePath().substring(0,dataSrc.getAbsolutePath().length()-4), BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}

