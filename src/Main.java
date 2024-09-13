import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    static String[] variants = new String[2];
    static double bias;
    static double[] weights;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter name to trainig data file");
        String trainingdatapath = scanner.nextLine();
        System.out.println("Enter name to data file");
        String datapath = scanner.nextLine();

        ArrayList<PObject> trainingdata = loadData(trainingdatapath);
        ArrayList<PObject> testdata = loadData(datapath);

        weights = new double[trainingdata.get(0).coordinates.size()];
        bias = Math.random()*0.1;
        for(int i = 0;i< weights.length;i++) weights[i] = Math.random()*0.1;

        System.out.println("Enter number of iterations to train");
        int numofIter = scanner.nextInt();
        System.out.println("Enter learning rate");
        double learningrate = scanner.nextDouble();

        for(int i = 0;i<numofIter;i++) {
            Collections.shuffle(trainingdata);
            trainPerceptron(trainingdata,learningrate);
            System.out.println(1+i + " Accuracy rate: " +getAccuracy(testdata));
        }

        boolean exit = false;
        while (!exit) {
            System.out.print("Enter your choice: ");
            System.out.println("1. Classify one object");
            System.out.println("2. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Enter coordinates");
                    String coord = scanner.nextLine();
                    int prediction = prediction(stringToArrayList(coord));

                    if(prediction==0) System.out.println(variants[0]);
                    else System.out.println(variants[1]);

                    break;
                case 2:
                    exit = true;
                    break;
                }
        }
    }
    static void trainPerceptron(List<PObject> trainingData,double learningRate) {
        for (PObject pObject : trainingData) {
            int correctType = pObject.getType(variants[0]);
            int predictedType = prediction( pObject.coordinates);

            if (correctType != predictedType) {
                for (int i = 0; i < pObject.coordinates.size(); i++) {
                    weights[i] += learningRate * (correctType - predictedType) * pObject.coordinates.get(i);
                }
                bias -= learningRate * (correctType - predictedType);
            }
        }
    }
    private static double getAccuracy(ArrayList<PObject> testData) {
        int correctPredictions = 0;
        for (PObject pObject : testData) {

            int correctType = pObject.getType(variants[0]);
            int predictedType = prediction( pObject.coordinates);

            if (correctType==predictedType) {
                correctPredictions++;
            }
        }
        return (double) correctPredictions / testData.size();
    }


    public static ArrayList<Double> stringToArrayList(String inputString) {
        String[] parts = inputString.split("\\s+");
        ArrayList<Double> doublesList = new ArrayList<>();
        for (String part : parts) {
            doublesList.add(Double.parseDouble(part));
        }
        return doublesList;
    }
     static int prediction(ArrayList<Double> coordinates) {
        double activation = 0;
        for (int i = 0; i < coordinates.size(); i++) {
            activation += weights[i] * coordinates.get(i);
        }
        activation -= bias;
        return activation >= 0 ? 1 : 0;
    }
    public static ArrayList<PObject> loadData(String filePath) {
        ArrayList<PObject> pObjects = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null&&line.length()>0) {
                String[] parts = line.split(",");
                ArrayList<Double> coordinates = new ArrayList<>();
                for (int i = 0; i < parts.length-1; i++) {
                    coordinates.add(Double.parseDouble(parts[i]));
                }
                String type = parts[parts.length - 1];
                if(variants[0]==null){
                    variants[0]=type;
                } else if (!variants[0].equals(type)) {
                    variants[1]=type;
                }
                pObjects.add(new PObject(coordinates, type));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pObjects;
    }
}