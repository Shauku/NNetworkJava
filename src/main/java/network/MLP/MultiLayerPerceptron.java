package network.MLP;

import network.Network;
import java.util.ArrayList;

public class MultiLayerPerceptron extends Network {

    private Neuron[] inputList;
    private Neuron[] outputList;
    private ArrayList<Neuron[]> hiddenLayers;
    private float momentum = 1;

    public MultiLayerPerceptron(int inputs, int outputs, int[] hidden){
        super(Type.MLP);
        log("Assembling network...");
        log("Type of network: " + this.getType());

        int id = 0;
        int sumNeurons = 0;

        inputList = new Neuron[inputs];
        hiddenLayers = new ArrayList<>();
        outputList = new Neuron[outputs];


        log("Creating input layer...");

        for (int i = 0; i < inputs; i++){
            id++;
            Neuron n = new Neuron(0, id, new Neuron[0]);
            inputList[i] = n;
        }
        log("Input layer created!");

        log("Creating hidden layers...");
        log("Number of hidden layers: " + hidden.length);
        for (int x = 0; x<hidden.length; x++){
            int l = hidden[x];
            Neuron[] prevLayer;
            Neuron[] layer = new Neuron[l];
            for (int i = 0; i<l; i++){
                id++;
                if (x==0){
                    prevLayer = inputList;
                } else {
                    prevLayer = hiddenLayers.get(x-1);
                }
                Neuron n = new Neuron((float)Math.random(), id, prevLayer);
                layer[i] = n;
            }
            hiddenLayers.add(layer);
        }
        log("Hidden layers created!");

        log("Creating output layer...");
        for (int i = 0; i < outputs; i++){
            id++;
            Neuron n = new Neuron(0, id, hiddenLayers.get(hiddenLayers.size()-1));
            n.isOutputUnit = true;
            outputList[i] = n;
        }
        log("Output layer created!");

        setNumNeurons(sumNeurons);

        log("Network online!");
    }

    public float[] evaluate(float[] data){
        float[] results = new float[outputList.length];
        if (data.length == inputList.length){
            for (int x = 0; x<data.length; x++) {
                Neuron n = inputList[x];
                n.feed(data[x]);
            }
            for (int x = 0; x<outputList.length; x++){
                Neuron n = outputList[x];
                results[x] = n.fire();
            }
        } else {
            log("ERROR: Data length is different from input neurons");
        }
        float[] softmaxed = softmax(results);
        for (int x = 0; x<outputList.length; x++){
            Neuron n = outputList[x];
            n.setOutput(softmaxed[x]);
        }

        resetNeurons();
        return softmaxed;
    }

    private float[] softmax (float[] outputs) {
        // determine max output sum
        float max = outputs[0];
        for (int i = 0; i < outputs.length; ++i)
            if (outputs[i] > max) max = outputs[i];

        // determine scaling factor -- sum of exp(each val - max)
        double scale = 0.0;
        for (int i = 0; i < outputs.length; ++i)
            scale += Math.exp(outputs[i] - max);

        float[] result = new float[outputs.length];
        for (int i = 0; i < outputs.length; ++i)
            result[i] = (float) (Math.exp(outputs[i] - max) / scale);

        return result; // now scaled so that xi sum to 1.0
    }

    private void resetNeurons() {
        for(Neuron n : inputList) {
            n.fired = false;
        }

        for(Neuron n : outputList) {
            n.fired = false;
        }

        for(Neuron[] neurons : hiddenLayers) {
            for (Neuron n : neurons) {
                n.fired = false;
            }
        }
    }

    Neuron[] getInputList() {
        return inputList;
    }

    Neuron[] getOutputList() {
        return outputList;
    }

    ArrayList<Neuron[]> getHiddenLayers() {
        return hiddenLayers;
    }

}
