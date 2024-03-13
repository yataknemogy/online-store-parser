package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.File;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        Scanner scanner;
        scanner = new Scanner(System.in);
        HashMap<Element, Element> parsJava  = new HashMap<>();

        while (true) {
            System.out.println("-----------------------------------");
            System.out.println("Welcome! Choose an action and enter in the console:");
            System.out.println("1. Parse the store (only one product)");
            System.out.println("2. Parse the store (by category products)");
            System.out.println("3. Exit");
            int chooseInput = scanner.nextInt();
            scanner.nextLine();
            switch (chooseInput) {
                case 1:
                    System.out.println("Enter the website URL");
                    System.out.println("Example: https://spigen.su/apple-spigen.html");
                    String webUrl = scanner.next();
                    System.out.println("Enter the class for the product name");
                    System.out.println("Example: a.product-item-link");
                    String nameProduct = scanner.next();
                    System.out.println("Enter the class containing the product price");
                    System.out.println("Example: span.price");
                    String priceProduct = scanner.next();
                    System.out.println("Enter the class containing the product image link");
                    System.out.println("Example: img.product-image-photo");
                    String imgUrl = scanner.next();
                    System.out.println("Processing, please wait!");
                    try {
                        var document = Jsoup.connect(webUrl).get();
                        var nameElement = document.selectFirst(nameProduct);
                        var priceElement = document.selectFirst(priceProduct);
                        var imgElement = document.selectFirst(imgUrl);
                        var imgSrc = imgElement.attr("src");
                        if (nameElement != null && priceElement != null && imgElement != null){
                            System.out.println("-----------------------------------");
                            System.out.println("Name of the product: " + nameElement.text());
                            System.out.println("-----------------------------------");
                            System.out.println("Price of the product: " + priceElement.text());
                            System.out.println("-----------------------------------");
                            System.out.println("Image URL: " + imgSrc);
                            System.out.println("-----------------------------------");
                            System.out.println("All information written to output.json, please wait.");
                        }
                        if (nameElement != null && priceElement != null && imgElement != null) {
                            ObjectMapper objectMapper = new ObjectMapper();
                            ObjectNode jsonNode = objectMapper.createObjectNode();
                            jsonNode.put("name", nameElement.text());
                            jsonNode.put("price", priceElement.text());
                            jsonNode.put("image_url", imgElement.attr("src"));
                            objectMapper.writeValue(new File("output.json"), jsonNode);
                            System.out.println("All information was successfully completed to output.json!");
                        }

                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    System.out.println("Enter the website URL");
                    System.out.println("Example: https://spigen.su/apple-spigen.html");
                    String urlWeb = scanner.next();
                    System.out.println("Enter the class for the product name");
                    System.out.println("Example: a.product-item-link");
                    String className = scanner.next();
                    System.out.println("Enter the class containing the product price");
                    System.out.println("Example: span.price");
                    String classPrice = scanner.next();
                    System.out.println("Enter the class containing the product image link");
                    System.out.println("Example: img.product-image-photo");
                    String urlImg = scanner.next();
                    System.out.println("Processing, please wait!");
                    try {
                        var document = Jsoup.connect(urlWeb).get();
                        var nameElements = document.select(className);
                        var priceElements = document.select(classPrice);
                        var imgElements = document.select(urlImg);
                        Map<String, Map<String, String>> products = new HashMap<>();
                        int elementsCount = Math.min(Math.min(nameElements.size(), priceElements.size()), imgElements.size());

                        for(int i = 0; i < elementsCount; i++) {
                            var nameElement = nameElements.get(i);
                            var priceElement = priceElements.get(i);
                            var imgElement = imgElements.get(i);

                            if(nameElement != null && priceElement != null && imgElement != null) {
                                Map<String, String> productDetails = new HashMap<>();
                                productDetails.put("name", nameElement.text());
                                productDetails.put("price", priceElement.text());
                                productDetails.put("image_url", imgElement.attr("src"));
                                products.put("product_" + i, productDetails);
                            }
                        }

                        if(!products.isEmpty()) {
                            ObjectMapper objectMapper = new ObjectMapper();
                            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
                            objectMapper.writeValue(new File("output.json"), products);
                            System.out.println("All information was successfully completed to output.json!");
                        }
                    } catch(Exception e) {
                        e.printStackTrace();
                    }

                case 3:
                    System.exit(0);
                default:
                    System.err.println("Invalid input, please try again!");
            }
        }
    }
}