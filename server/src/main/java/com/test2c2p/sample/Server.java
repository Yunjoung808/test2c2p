package com.test2c2p.sample;

import java.nio.file.Paths;
import java.nio.charset.Charset;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Stream;
import java.util.stream.Collectors;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.port;
import static spark.Spark.staticFiles;

import com.google.gson.Gson;


import io.github.cdimascio.dotenv.Dotenv;

public class Server {
    private static Gson gson = new Gson();

    public static void main(String[] args) {
        port(4242);

        //To manage 환경변수 easily
        Dotenv dotenv = Dotenv.load();


        staticFiles.externalLocation(Paths.get(Paths.get("").toAbsolutePath().toString(), dotenv.get("STATIC_DIR")).normalize().toString());

        get("/config", (request, response) -> {
            System.out.println("=============START /CONFIG==============");

            response.type("application/json");
            String price = dotenv.get("PRICE");
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("publicKey", dotenv.get("STRIPE_PUBLISHABLE_KEY"));
    
            System.out.println("=============FINISH /CONFIG==============");

            return gson.toJson(responseData);
        });
    }




    public static void checkEnv() {
        Dotenv dotenv = Dotenv.load();
        String price = dotenv.get("PRICE");
        if(price == "price_12345" || price == "" || price == null) {
          System.out.println("You must set a Price ID in the .env file. Please see the README.");
          System.exit(0);
        }
    }
}
