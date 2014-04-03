/*
 * Copyright 2014 Jin Kwon.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.github.jinahya.rfc868;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


/**
 *
 * @author Jin Kwon
 */
public class Rfc868Times_UDP_IT implements Runnable {


    private static final Logger logger
            = LoggerFactory.getLogger(Rfc868Times_UDP_IT.class);


    private static final int PORT = Rfc868Constants.PORT_UDP + 1024;


    private static Thread thread;


    private static DatagramSocket server;


    private static DatagramSocket client;


    @Override
    public void run() {

        final byte[] data = new byte[4];
        final DatagramPacket packet = new DatagramPacket(data, data.length);
        while (!Thread.currentThread().isInterrupted()) {
            try {
                server.receive(packet);
                logger.debug("server received");
            } catch (SocketException se) {
                break;
            } catch (IOException ioe) {
                logger.debug("ioe", ioe);
            }
            final long time = Rfc868Times.put(
                    System.currentTimeMillis(), packet);
            logger.debug("server time: {}", time);
            try {
                server.send(packet);
                logger.debug("server sent");
            } catch (IOException ioe) {
                logger.debug("ioe", ioe);
            }
        }
    }


    @BeforeClass
    public void startUp() throws SocketException {

        server = new DatagramSocket(PORT);
        logger.debug("server created");

        thread = new Thread(this);
        thread.start();
        logger.debug("thread started");

        client = new DatagramSocket();
        logger.debug("client created");
    }


    @AfterClass
    public void finishDown() throws InterruptedException {

        server.close();
        logger.debug("server closed");

        thread.interrupt();
        thread.join();
        logger.debug("thread joined");

        client.close();
        logger.debug("client closed");
    }


    @Test(invocationCount = 128)
    public void request() throws UnknownHostException, IOException {

        final byte[] data = new byte[4];

        final DatagramPacket packet = new DatagramPacket(
                data, 0, data.length, InetAddress.getLocalHost(), PORT);

        client.send(packet);
        logger.debug("client sent");

        client.receive(packet);
        logger.debug("client received");

        final long time = Rfc868Times.get(packet);
        logger.debug("client time: {}", time);
    }


}

