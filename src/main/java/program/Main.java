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
            long outputs = 1L;
            long[] hidden = {4L};

            float learnRate = 0.25f;

            Data data = new XORData();

            Network network = new MultiLayerPerceptron(inputs, outputs, hidden);
            Trainer trainer = new MLPTrainer((MultiLayerPerceptron) network, learnRate, data, 100);
            trainer.start();

            trainer.join();

            for (double[] d : data.getData()){
                System.out.println(d[0]+","+d[1]);
                System.out.println("Network eval: " + network.evaluate(d)[0]);
            }

            return "Ok";
        });

        MnistData data = new MnistData(10);

        get("/mnist", (request, response) -> {
            long inputs = data.numberOfPixels;
            long outputs = 10;
            long[] hidden = {15L};
            float learnRate = 0.25f;

            Network network = new MultiLayerPerceptron(inputs, outputs, hidden);
            Trainer trainer = new MLPTrainer((MultiLayerPerceptron) network, learnRate, data, 1);

            trainer.start();
            trainer.join();

            return "Ok";
        });
    }
}

//PER COMPARAR TOPOLOGIES, ELS PESOS ENTRE AQUESTES AN DE SER LO PUTO IGUAL
