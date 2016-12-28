package program;

import data.Data;
import data.MnistData;
import data.XORData;
import network.MLP.MLPTrainer;
import network.MLP.MultiLayerPerceptron;
import network.Trainer;
import network.Network;
import static spark.Spark.*;

public class Main {

    //localhost:4567/
    //http://www.nnwj.de/forwardpropagation.html
    //http://www.nnwj.de/backpropagation.html

    public static void main(String[] args) {


        get("/", (request, response) -> {
            long inputs = 2L;
            long outputs = 2L;
            long[] hidden = {4L};

            float learnRate = 3f;

            Data data = new XORData();

            Network network = new MultiLayerPerceptron(inputs, outputs, hidden);
            Trainer trainer = new MLPTrainer((MultiLayerPerceptron) network, learnRate, data, 10000);
            trainer.start();

            trainer.join();

            for (double[] d : data.getData()){
                System.out.println(d[0]+","+d[1]);
                double[] res = network.evaluate(d);
                System.out.println("Network eval: " + res[0] + " - " + res[1]);
            }

            return "Ok";
        });

        MnistData data = new MnistData(10);

        get("/mnist", (request, response) -> {
            System.out.println("Number of inputs: " + data.numberOfPixels);
            long inputs = data.numberOfPixels;
            long outputs = 10;
            long[] hidden = {300L};
            float learnRate = 3f;

            Network network = new MultiLayerPerceptron(inputs, outputs, hidden);
            Trainer trainer = new MLPTrainer((MultiLayerPerceptron) network, learnRate, data, 1);

            trainer.start();
            trainer.join();

            return "Number of inputs: " + data.numberOfPixels;
        });
    }
}

//PER COMPARAR TOPOLOGIES, ELS PESOS ENTRE AQUESTES AN DE SER LO PUTO IGUAL
