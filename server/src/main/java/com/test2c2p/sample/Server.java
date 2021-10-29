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

import java.util.HashMap;
import org.json.simple.JSONObject;
import com.auth0.jwt.*;
import com.auth0.jwt.algorithms.*;
import com.auth0.jwt.interfaces.*;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;


import io.github.cdimascio.dotenv.Dotenv;

public class Server {
    private static Gson gson = new Gson();

    public static void main(String[] args) {
        port(4242);

        //To manage 환경변수 easily
        Dotenv dotenv = Dotenv.load();
        staticFiles.externalLocation(Paths.get(Paths.get("").toAbsolutePath().toString(), dotenv.get("STATIC_DIR")).normalize().toString());

        
        //Generate JWT Token
        post("/generateJWTtoken", (request, response) -> {
            //1.Prepare Payload Data & Secret Key
            String token="";
            String secretKey = "ECC4E54DBA738857B84A7EBC6B5DC7187B8DA68750E88AB53AAA41F548D6F2D9";

            HashMap<String, Object> payload = new HashMap<>();
            payload.put("merchantID","JT01");
            payload.put("invoiceNo","1523953661");
            payload.put("description","item 1");
            payload.put("amount",1000.00);
            payload.put("cuencyCode","SGD");
            
            //2.Generate Token
            try {
                Algorithm algorithm = Algorithm.HMAC256(secretKey);
            
                token = JWT.create().withPayload(payload).sign(algorithm);           
                
            } catch (JWTCreationException | IllegalArgumentException e){
            //Invalid Signing configuration / Couldn't convert Claims.
            e.printStackTrace();
            }

            //3.Construct API Request Data
            JSONObject requestData = new JSONObject();
            requestData.put("payload", token);

            //4.Send to API
            try{
                String endpoint = "https://sandbox-pgw.2c2p.com/payment/4.1/PaymentToken";
                URL obj = new URL(endpoint);
                HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/*+json");
                con.setRequestProperty("Accept", "text/plain");

                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(requestData.toString());
                wr.flush();
                wr.close();


                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response2 = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response2.append(inputLine);
                }
                in.close();
            }catch(Exception e){
                e.printStackTrace();
            }

        return gson.toJson(requestData);
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
