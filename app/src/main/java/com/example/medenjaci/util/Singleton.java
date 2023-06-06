package com.example.medenjaci.util;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.medenjaci.beans.Cart;
import com.example.medenjaci.beans.Item;
import com.example.medenjaci.beans.Order;
import com.example.medenjaci.beans.OrderItem;
import com.example.medenjaci.beans.User;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class Singleton {

    private static Singleton singleton;

    private List<User> users;
    private List<Cart> carts;
    private List<Item> items;
    private List<OrderItem> items_in_order;
    private List<Order> orders;

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Singleton() {
        //USERS
        users = new LinkedList<>();
        User u1 = new User("aleks", "akiaki123", "Aleksandar", "Paripovic", "066491668", "Milana Rakica 4/5, Beograd", "K");
        User u2 = new User("bole", "bolebole123", "Dobrivoje", "Vucetic", "0641267500", "Takovska 46, Krusevac", "K");
        User u3 = new User("neca", "necaneca123", "Nemanja", "Tijanic", "0600102250", "Pere Velimirovica 52/19, Beograd", "K");

        users.add(u1);
        users.add(u2);
        users.add(u3);
        //CARTS

        carts = new LinkedList<>();


        //ITEMS
        items = new LinkedList<>();
        Item i1 = new Item(1, 700.0, "Piti ujutru i uveče po 15g.", "Med od badema", "Bademov med je mješavina meda i badema. Konzistencija ovog sastava je tekuća, boja je svijetlo žuta. Ukus sadrži hrpu osnovnih sastojaka, a nakon upotrebe ostaje naglašen okus po okusu. Koristi se u kuhanju, tradicionalnoj medicini i kućnoj kozmetologiji.");
        Item i2 = new Item(2, 600.0, "Piti ujutru 30g.", "Med od cveta narandze", "Grčka ima više košnica po hektaru nego bilo koja druga zemlja u Evropi, a grčki med se smatra jednim od najfinijih na svetu.");
        Item i3 = new Item(3, 800.0, "Piti uveče 3 sata pre spavanja 10g.", "Med od divlje lavande", "Med od divlje lavande predstavlja mešavinu 100% prirodnog bagremovog meda sa sušenim semenom najfinije lavande. Ovaj opojni med pored savršenog ukusaima i mnoštvo prirodnih lekovitih svojstava što ga čini idealnom namirnicom za započinjanje zdravog i uspešnog dana.");
        Item i4 = new Item(4, 900.0, "Piti tri puta dnevno 5g.", "Med od eukaliptusa", "Predak ovog meda sorti je Australija, kao što je to istorijski bio dom eukaliptusa. Sa takvim daleka porijekla i pripadajućih dovoljno visoku cijenu. Eukaliptusa - zimzeleno drvo termofilnih. Postoji više od 700 vrsta na svijetu. U našoj zemlji, to je egzotičan, jer on ne može preživjeti zimu hladne i snježne klime. Povremeno naći samo u regiji Krasnodar. Često, eukaliptusa med na našim stolovima ide iz Abhazije, Španije, Izraela, pa čak i Australije.");
        Item i5 = new Item(5, 550.0, "Piti ujutru 20g.", "Hrastov med", "Zasigurno med najbogatiji lako apsorbirajućim mineralima za ljudski organizam, a pogotovo željezom radi visokog sadržaja tanina. Sadrži i veće količine antioksidansa, aminokiselina (alanin, arginin, asparaginska kiselina, cistin, glutaminska kiselina, glicin, histidin, leucin, lizin,metionin, prolin,serin, treonin, triptofan,tirozin i valin), enzimima te ostalim ljekovitih i hranjivih tvari. Osim saharoze, fruktoze, glukoze sadrži i veću količinu viših šećera kao što su rafinoza, melicitoza, turanoza, trehanoza, maltoza, koje se vrlo lako apsorbiraju u organizmu i imaju produženo djelovanje.");
        Item i6 = new Item(6, 800.0, "Piti tri puta dnevno po 20g.", "Med od kestena", "Fin med tamne boje,jakog i slatkog ukusa.Kestenov med ima veliku hranjivu vrednost jer sadrži veliki procenat polena.Bogat je taninom i saponinom sastojcima koji se bore protiv proširenih vena,reume i uopšte poboljšavaju cirkulaciju.Pomaže pri varenju,poboljšava mentalne funkcije,ima hidratantno dejstvo i preporučuje se onima koji imaju problema sa prostatom i urinarnim sistemom.");
        Item i7 = new Item(7, 450.0, "Piti dva puta dnevno. Količina varira u zavisnosti od potreba.", "Med od lavande", "Lavandin med ima snažno umirujuće dejstvo i veliki je borac protiv nervoze.Kako se lavanda u različitim aromatičnim kupkama, lekovina, receptima uvek preporučuje u borbi protiv nesanice i med od lavande takođe efikasno pomaže kod sličnih problema. Pomaže u borbi sa blažim oblicima depresije i svim  oblicima nervne napetosti kao i simptome izazvane pred-menstrualnim sindromom. Lavandin miris opušta a prilikom konzumacije ovog meda uvidećete koliko je snažan u borbi protiv stresa. Takođe je dobra za sva gljivična I bakterijska oboljenja kože.");
        Item i8 = new Item(8, 650.0, "Piti 4 puta dnevno u razmaku od po 3h.", "Sumski med", "Šumski med svoja impresivna antibakterijska svojstva duguje visokoj razini pH, niskom sadržaju vlage i vodikovom peroksidu – snažnom sredstvu za dezinfekciju koje se prirodno pojavljuje kao rezultat reakcije između glukoze, šećera i enzima koje luče pčele.");
        Item i9 = new Item(9, 520.0, "Piti svakog drugog dana ujutru i uveče po 15g.", "Med od ruzmarina", "Ružmarinov med je tvar koju pčele proizvode zbog kontakta ovih insekata s cvijetom ružmarina. Pčele izvlače nektar iz cvijeća i ta se tvar kondenzira unutar životinje, a kasnije se odlaže u košnicu gdje se konačno proizvodi med. Posebnost ove vrste meda je da se svojstva ružmarina prenose da bi se napravila s ovom biljkom.");
        Item i10 = new Item(10, 800.0, "Piti samo ujutru na prazan stomak jednu kafenu kašičicu.", "Med od vrijeska", "Ako ste pušač, iskoristite blagodati vrijeskovog meda. U kombinaciji s čajem od vrijeska odlično potiče iskašljavanje. Ima specifičan ukus no ukoliko se zakamuflira u čaju dijete bi ga trebalo zavoljeti. Osim toga, med od vrijeska može biti dobar za sve generacije jer pomaže kod reumatskih oboljenja te bolestima mokraćnih kanala i mjehura.");

        items.add(i1);
        items.add(i2);
        items.add(i3);
        items.add(i4);
        items.add(i5);
        items.add(i6);
        items.add(i7);
        items.add(i8);
        items.add(i9);
        items.add(i10);

        //ORDERS
        orders = new LinkedList<>();
/*        Order o1 = new Order(0, 1, 4300.0, "aleks", LocalDateTime.of(2021, 1, 4, 19, 51, 31), null, null, 'O');
        Order o2 = new Order(0, 2, 4200.0, "aleks", LocalDateTime.of(2021, 1, 4, 22, 45, 7), null, null, 'P');
        Order o3 = new Order(8, 3, 6010.0, "aleks", LocalDateTime.of(2021, 1, 5, 1, 30, 24), LocalDateTime.of(2021, 1, 7, 0, 2, 25), LocalDateTime.of(2021, 1, 15, 0, 2, 25), 'I');

        orders.add(o1);
        orders.add(o2);
        orders.add(o3);
*/
        //ITEMS IN ORDER
        items_in_order = new LinkedList<>();
        /*OrderItem orderItem1 = new OrderItem(1, 3, 1, 2,  1600.0);
        OrderItem orderItem2 = new OrderItem(2, 4, 1, 3, 2700.0);
        OrderItem orderItem3 = new OrderItem(3, 3, 2, 3, 2400.0);
        OrderItem orderItem4 = new OrderItem(4, 7, 2, 4, 1800.0);
        OrderItem orderItem5 = new OrderItem(5, 8, 3, 2, 1300.0);
        OrderItem orderItem6 = new OrderItem(6, 5, 3, 1, 550.0);
        OrderItem orderItem7 = new OrderItem(7, 9, 3, 8, 4160.0);


        items_in_order.add(orderItem1);
        items_in_order.add(orderItem2);
        items_in_order.add(orderItem3);
        items_in_order.add(orderItem4);
        items_in_order.add(orderItem5);
        items_in_order.add(orderItem6);
        items_in_order.add(orderItem7);
*/

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Singleton getSingleton() {
        if (singleton == null) {
            singleton = new Singleton();
        }
        return singleton;
    }


    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Cart> getCarts() {
        return carts;
    }

    public void setCarts(List<Cart> carts) {
        this.carts = carts;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<OrderItem> getItems_in_order() {
        return items_in_order;
    }

    public void setItems_in_order(List<OrderItem> items_in_order) {
        this.items_in_order = items_in_order;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    //metode

    public User find_user_by_username(String username) {
        User user = null;
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                user = u;
                return user;
            }
        }

        return user;
    }

    public void addUser(User u) {
        users.add(u);
    }

    public void addCart(Cart c) {
        carts.add(c);
    }

    public void updateCart(Cart cart) {
        for (Cart c : carts) {
            if (cart.getId_item() == c.getId_item()) {
                c.setQuantity(cart.getQuantity());
                Item i = find_item_by_id(c.getId_item());
                c.setPrice(i.getPrice() * cart.getQuantity());
                return;
            }
        }
        addCart(cart);
    }

    public Item find_item_by_id(int id) {
        Item item = null;
        for (Item i : items) {
            if (i.getId_item() == id) {
                item = i;
                break;
            }
        }
        return item;
    }

    public Cart find_item_in_cart_by_user(int id_item, String username) {
        Cart cart = null;
        for (Cart c : carts) {
            if (c.getId_item() == id_item && c.getUser().equals(username)) {
                cart = c;
                break;
            }
        }
        return cart;
    }

    public List<Cart> find_cart_for_user(String username) {
        List<Cart> carts_ = new LinkedList<>();
        for (Cart c : carts) {
            if (c.getUser().equals(username)) {
                carts_.add(c);
            }
        }
        return carts_;
    }

    public List<Order> find_orders_for_user(String username) {
        List<Order> orders_ = new LinkedList<>();
        for (Order o : orders) {
            if (o.getUsername().equals(username)) {
                orders_.add(o);
            }
        }
        return orders_;
    }

    /*public Order find_order_by_id(int id_order) {
        Order order = null;
        for (Order o : orders) {
            if (o.getId_order() == id_order) {
                order = o;
                break;
            }
        }
        return order;
    }*/

    public List<OrderItem> find_items_for_order(int id_order) {
        List<OrderItem> orderItemList = new LinkedList<>();
        for (OrderItem orderItem : items_in_order) {
            if (orderItem.getId_order().equals(id_order)) {
                orderItemList.add(orderItem);
            }
        }
        return orderItemList;
    }

}
