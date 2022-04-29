import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.*;
import java.io.*;



public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String str = br.readLine();
        BookShop bs = new BookShop(str);
        while (true){
            String command = br.readLine();
            if (command.equals("exit")) break;
            if (command.equals("print balance")){
                bs.print_balance();
            }
            else if (command.equals("show books in stock")) {
                bs.show_stock();
            }
            else if (command.startsWith("buy")){
                bs.buy(command.substring(4));
            }
            else if (command.equals("show bought books")){
                bs.show_bought();
            }
            else {
                System.out.println("I don't understand");
            }
        }
    }
}
