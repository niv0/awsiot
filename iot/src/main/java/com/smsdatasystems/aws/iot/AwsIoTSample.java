package com.smsdatasystems.aws.iot;

import com.amazonaws.services.iot.client.AWSIotDevice;
import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.sample.sampleUtil.SampleUtil;

/**
 * Created by u83K7P1 on 15.01.2017.
 */
public class AwsIoTSample {

    private AWSIotMqttClient client;

    private void connect() throws AWSIotException {

        String clientEndpoint = "<prefix>.iot.<region>.amazonaws.com";       // replace <prefix> and <region> with your own
        String clientId = "<unique client id>";                              // replace with your own client ID. Use unique client IDs for concurrent connections.
        String certificateFile = "465c833f6f-certificate.pem.crt";                       // X.509 based certificate file
        String privateKeyFile = "465c833f6f-private.pem.key";                        // PKCS#1 or PKCS#8 PEM encoded private key file

        // SampleUtil.java and its dependency PrivateKeyReader.java can be copied from the sample source code.
        // Alternatively, you could load key store directly from a file - see the example included in this README.
        SampleUtil.KeyStorePasswordPair pair = SampleUtil.getKeyStorePasswordPair(certificateFile, privateKeyFile);
        client = new AWSIotMqttClient(clientEndpoint, clientId, pair.keyStore, pair.keyPassword);

        // optional parameters can be set before connect()
        client.connect();

    }

    private void publicMessageBlocking() throws AWSIotException {
        String topic = "my/own/topic";
        String payload = "any payload";

        client.publish(topic, AWSIotQos.QOS0, payload);

    }

    private void shadowBlocking() throws AWSIotException {

        String thingName = "<thing name>";                    // replace with your AWS IoT Thing name

        AWSIotDevice device = new AWSIotDevice(thingName);

        client.attach(device);
        client.connect();

        // Delete existing shadow document
        device.delete();

        // Update shadow document
        String stateString = "{\"state\":{\"reported\":{\"sensor\":3.0}}}";
        device.update(stateString);

        // Get the entire shadow document
        final String state = device.get();
    }
}
