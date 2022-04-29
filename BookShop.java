import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class BookShop {
    int bal;
    HashMap<String, MyPair> info;
    HashMap<String, Integer> bought;
    String rub = new String("руб.".getBytes(StandardCharsets.UTF_8)), sht = new String("шт.".getBytes(StandardCharsets.UTF_8));
    PrintWriter pw;
    void books_divide(String str){
        int last_open = -1;
        for (int i = 0; i < str.length(); i++){
            if (str.charAt(i) == ']') break;
            if (str.charAt(i) == '(') last_open = i;
            if (str.charAt(i) == ')'){
                String book_name = "";
                int cnt_beg = 0;
                for (int x = last_open + 2; x < i; x++) {
                    // pw.println(x);
                    if (str.charAt(x) == '"'){
                        cnt_beg = x;
                        break;
                    }
                    book_name += str.charAt(x);
                }

                int cnt = 0;
                int idx = cnt_beg;
                while (! ('0' <= str.charAt(idx) && str.charAt(idx) <= '9')) idx++;
                while (('0' <= str.charAt(idx) && str.charAt(idx) <= '9')) {
                    cnt = cnt * 10 + str.charAt(idx) - '0';
                    idx++;
                }
                int cost = 0;
                while (! ('0' <= str.charAt(idx) && str.charAt(idx) <= '9')) idx++;
                while (('0' <= str.charAt(idx) && str.charAt(idx) <= '9')) {
                    cost = cost * 10 + str.charAt(idx) - '0';
                    idx++;
                }
                info.put(book_name, new MyPair(cnt, cost));
            }
        }
    }
    BookShop(String str) throws UnsupportedEncodingException {
        pw = new PrintWriter(new OutputStreamWriter(System.out, "UTF-8"), true);
        info = new HashMap<>();
        bought = new HashMap<>();
        bal = 0;
        int pos_money = 0;
        for (; pos_money < str.length(); pos_money++){
            if ('0' <= str.charAt(pos_money) && str.charAt(pos_money) <= '9') break;
        }
        if (pos_money == 0){
            pw.println("breakdown");
            System.exit(0);
        }
        while ('0' <= str.charAt(pos_money) && str.charAt(pos_money) <= '9')
            bal = bal * 10 + str.charAt(pos_money++) - '0';
        int books_start = pos_money;
        for (; books_start < str.length(); books_start++){
            if (str.charAt(books_start) == ':') break;
        }
        books_divide(str.substring(books_start + 1));
    }
    public void print_balance(){
        pw.print("balance: " + bal + " ");
        pw.println(rub);
    }
    void show_stock() {
        for (String key: info.keySet()){
            if (info.get(key).cnt != 0)
                pw.println('"' + key + "\", " + info.get(key).cnt + " " + sht + ", " + info.get(key).cost + " " + rub);
        }
    }
    public void buy(String book){
        int q_cnt = 0;
        int name_breakes = 0;
        for (int i = 0; i < book.length(); i++){
            if (book.charAt(i) == '"') q_cnt++;
            if (q_cnt == 2) {
                name_breakes = i;
                break;
            }
        }
        String name = book.substring(1, name_breakes);
        int cnt = 0;
        for (int i = name_breakes + 2; i < book.length(); i++){
            char c = book.charAt(i);
            if ('0' <= c && c <= '9')
                cnt = cnt * 10 + c - '0';
        }
        if (!info.containsKey(name) || info.get(name).cnt < cnt || info.get(name).cost * cnt > bal) {
            pw.println("no deal");
            return;
        }
        MyPair adding = info.get(name);
        adding.cnt -= cnt;
        bal -= cnt * info.get(name).cost;
        if (adding.cnt != 0)
            info.put(name, adding);
        if (bought.containsKey(name)){
            bought.put(name, bought.get(name) + cnt);
        }
        else{
            bought.put(name, cnt);
        }
        pw.println("deal");
    }
    void show_bought(){
        for (String key: bought.keySet()){
            pw.println('"' + key + "\", " + bought.get(key) + " " + sht);
        }
    }
}
