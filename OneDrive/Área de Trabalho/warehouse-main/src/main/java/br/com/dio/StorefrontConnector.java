package br.com.dio;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class StorefrontConnector {
    private static final int PORT = 8080;
    private static final ObjectMapper mapper = new ObjectMapper();
    
    public static void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        
        // Endpoints para integração com storefront
        server.createContext("/api/products", new ProductsHandler());
        server.createContext("/api/stock", new StockHandler());
        server.createContext("/api/sell", new SellHandler());
        server.createContext("/api/health", new HealthHandler());
        
        server.start();
        System.out.println("Storefront Connector iniciado na porta " + PORT);
        System.out.println("Endpoints disponíveis:");
        System.out.println("  GET  /api/products - Lista produtos");
        System.out.println("  GET  /api/stock - Verifica estoque");
        System.out.println("  POST /api/sell - Realiza venda");
        System.out.println("  GET  /api/health - Health check");
    }
    
    static class ProductsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = """
                {
                    "products": [
                        {"id": 1, "name": "Cesta Básica", "price": 100.00, "stock": 50}
                    ]
                }
                """;
            
            exchange.sendResponseHeaders(200, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes(StandardCharsets.UTF_8));
            }
        }
    }
    
    static class StockHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = """
                {
                    "stock": {
                        "total": %d,
                        "outOfDate": %d
                    }
                }
                """.formatted(
                    Main.getStockSize(),
                    Main.getOutOfDateCount()
                );
            
            exchange.sendResponseHeaders(200, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes(StandardCharsets.UTF_8));
            }
        }
    }
    
    static class SellHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = """
                {
                    "status": "success",
                    "message": "Venda realizada com sucesso"
                }
                """;
            
            exchange.sendResponseHeaders(200, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes(StandardCharsets.UTF_8));
            }
        }
    }
    
    static class HealthHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "{\"status\": \"ok\"}";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes(StandardCharsets.UTF_8));
            }
        }
    }
}
