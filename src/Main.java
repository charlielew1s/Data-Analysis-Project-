import javax.xml.crypto.Data;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.File;  // Import the File class
import java.io.IOException;  // Import the IOException class to handle errors
import java.util.Scanner;


public class Main {
    private SqliteDB db;
    private HashMap<String, ArrayList<DataPoint>> groupToDataMap;
    public Main(){
        this.db = new SqliteDB();
        db.getData();
        groupToDataMap = db.getGroupToDataMap();
        int a = 1;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Main main = new Main();
        main.processData();
    }

    private void processData() throws FileNotFoundException {
        for (Map.Entry<String,ArrayList<DataPoint>> e : groupToDataMap.entrySet()){
            ArrayList<Double> xList = new ArrayList<>();
            ArrayList<Long> yList = new ArrayList<>();
            for (DataPoint dataPoint : e.getValue()){
                xList.add(dataPoint.getX());
                yList.add(dataPoint.getY());
            }
            double xAve = sumDouble(xList)/xList.size();
            long yAve = sumLong(yList)/yList.size();
            ArrayList<Double> XMinusXAve = new ArrayList<>();
            ArrayList<Long> YMinusYAve = new ArrayList<>();
            ArrayList<Double> XMinusXAveSquared = new ArrayList<>();
            ArrayList<Long> YMinusYAveSquared = new ArrayList<>();
            double XMinusXBar;
            long YMinusYBar;
            double sumMulTo;
            double XMinusXBarSquaredSum;
            long YMinusYBarSquaredSum;
            for (double element : xList){
                XMinusXBar = element - xAve;
                XMinusXAve.add(XMinusXBar);
                XMinusXAveSquared.add(Math.pow(XMinusXBar,2));
            }
            for (long element : yList){
                YMinusYBar = element - yAve;
                YMinusYAve.add(YMinusYBar);
                YMinusYAveSquared.add((long) Math.pow(YMinusYBar,2));
            }
            double elementX;
            long elementY;
            double elementAdd;
            ArrayList<Double> mulTo = new ArrayList<>();
            for (int element = 0; element <= XMinusXAve.size() - 1; element ++){
                elementX = XMinusXAve.get(element);
                elementY = YMinusYAve.get(element);
                elementAdd = elementX * elementY;
                mulTo.add(elementAdd);
            }
            sumMulTo = sumDouble(mulTo);
            XMinusXBarSquaredSum = sumDouble(XMinusXAveSquared);
            YMinusYBarSquaredSum  = sumLong(YMinusYAveSquared);
            double r = sumMulTo/Math.sqrt(XMinusXBarSquaredSum * YMinusYBarSquaredSum);
            String rStr = String.format("%.4f",r);
            String fileName = "data/" + e.getKey() + ".dat";
            try {
                File groupFile = new File(fileName);
                if (groupFile.createNewFile()) {
                    System.out.println("File created: " + groupFile.getName());
                } else {
                    System.out.println("File already exists.");
                }
            } catch (IOException f) {
                System.out.println("An error occurred.");
                f.printStackTrace();
            }
            double sY = Math.sqrt(YMinusYBarSquaredSum/(YMinusYAveSquared.size() - 1));
            double sX = Math.sqrt(XMinusXBarSquaredSum/(XMinusXAveSquared.size() - 1));
            // b - slope
            // a - y - intercept
            double b = r * (sY/sX);
            String bStr = String.format("%.4f",b);
            double a = yAve - (b * xAve);
            String aStr = String.format("%.4f",a);
            try {
                FileWriter myWriter = new FileWriter(fileName);
                myWriter.write("Pearson's r = " + rStr + "\n"
                + "Regression slope = " + bStr + "\n"
                + "Regression intercept = " + aStr
                + "\n" + "\n");
                for (DataPoint dataPoint : e.getValue()){
                    String dpStringX = String.format("%.4f",dataPoint.getX());
                    String dpStringY = String.valueOf(dataPoint.getY());
                    myWriter.write(dpStringX + "\t" + dpStringY + "\n");
                }
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException f) {
                System.out.println("An error occurred.");
                f.printStackTrace();
            }
            File file = new File(fileName);
            ArrayList<Object> list = new ArrayList();
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()){
                list.add(scanner.nextLine());
            }
        }
    }
    public double sumDouble(ArrayList<Double> list) {
        double sum = 0;
        for (double i : list)
            sum = sum + i;
        return sum;
    }

    public long sumLong(ArrayList<Long> list) {
        long sum = 0;
        for (long i : list)
            sum = sum + i;
        return sum;
    }
}
