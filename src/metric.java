import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.io.File;

public class metric {

    private class Class {
        private String name;
        private ArrayList<Metrics> history;

        public Class(String name, ArrayList<Metrics> history) {
            this.name = name;
            this.history = history;
        }
    };

    private class Metrics {
        private int method;
        private int attributes;

        public Metrics(int method, int attributes) {
            this.method = method;
            this.attributes = attributes;
        }
    }

    public static void main(String[] pList){
        File folder;
        ArrayList<File> dirList = new ArrayList<>();
        ArrayList<Class> classList = new ArrayList<>();
        for (String release : pList){
            folder = new File(release);
            //tools.findFile(folder, dirList);
            for (File file : dirList){
                analysis(file, classList);
            }
        }
    }

    public static void analysis(File file, ArrayList<Class> classList){
        String fileName = file.getName();

        for (Class c : classList){
            if (c.name.equals(fileName)){

            }

        }


    }


}
