import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;


class KolichestvoException extends Exception{
    public KolichestvoException(String message){
        super(message);
    }
}


class PolosaRazgonaException extends Exception{
    public PolosaRazgonaException(String message){
        super(message);
    }
}


class Airport implements Serializable{
    private String name;
    private ArrayList<F_a> f_as = new ArrayList<>();
    
    public Airport(String new_name){
        this.name = new_name;
    }
    
    public ArrayList<F_a> getF_as(){
        return this.f_as;
    }
    
    public void addF_a(F_a f_a){
        this.f_as.add(f_a);
    }
    
    public String getName(){
        return this.name;
    }
    
    public void setName(String new_name){
        this.name = new_name;
    }
}


class F_a implements Serializable{
    private String name;
    private int sits;
    public String type;
    private Airport airport;
    private ArrayList<Passeger> passegers = new ArrayList<>();
    
    public F_a(String new_name, int sits, String type){
        this.name = new_name;
        this.sits = sits;
        this.type = type;
    }
    
    public void set_airport(Airport airport){
        this.airport = airport;
        this.airport.addF_a(this);
    }
    
    public ArrayList<Passeger> get_passegers(){
        return passegers;
    }
    
    public void add_passeger(Passeger passeger){
        try{
            if (this.passegers.size() == this.sits)
                throw new KolichestvoException("Нет мест!");
            else
                this.passegers.add(passeger);
        }
        catch(KolichestvoException ex){
            System.out.println(ex.getMessage());
        }
    }
    
    public void setName(String new_name){
        this.name = new_name;
    }
    
    public String getName(){
        return this.name;
    }
    
    public void setSits(int new_sits){
        this.sits = new_sits;
    }
    
    public int getSits(){
        return this.sits;
    }
    
    public String upp(){ //Исключение: Если нет имени
        return ("Летательный аппарат "+ this.name+ " взлетел успешно!");
    }
    
    public int get_num_sits(){
        return passegers.size();
    }
}


class Plane extends F_a{
    private int len;
    
    public Plane(String name, int new_sits, int new_len) throws PolosaRazgonaException{
        super(name,new_sits, "Самолет");
        try{
            if (new_len<1)
                throw new PolosaRazgonaException("Невозможно создать самолет – указана некорректная длина полосы разгона: "+new_len);
            else
                this.len = new_len;
        }
        catch(PolosaRazgonaException ex){
            System.out.println(ex.getMessage());
            throw ex;
        }
    }
    
    public void setLen(int new_len){
        this.len = new_len;
    }
    
    public int getLen(){
        return this.len;
    }
    
    @Override
    public String upp(){
        return ("Самолет "+ this.getName()+ " взлетел успешно!");
    }
}


class Helicopter extends F_a{

    public Helicopter(String name, int max_count){
        super(name, max_count, "Вертолет");
    }
    @Override
    public String upp(){
        return ("Вертолет "+ this.getName()+ " взлетел успешно!");
    }
}


class Passeger implements Serializable{
    private String full_name;
    private int seating_position;
    public String get_name(){
        return this.full_name;
    }
    
    public Passeger(String new_name, int new_seat){
        this.full_name = new_name;
        this.seating_position = new_seat;
    }

    public int get_position(){
        return this.seating_position;
    }

    public void set_name(String new_name){
        this.full_name = new_name;
    }

    public void set_position(int new_position){
        this.seating_position = new_position;
    }

    public F_a f_a;
    public void set_f_a(F_a f_a){
        this.f_a = f_a;
        this.f_a.add_passeger(this);
    }
}


public class Main {
    public static void main( String[] args) throws IOException, PolosaRazgonaException, ClassNotFoundException{
        Airport Vnukovo = new Airport("Внуково"); 
        ArrayList<Plane> planes = new ArrayList();
        ArrayList<Helicopter> helicopters = new ArrayList();
        Plane mk100 = new Plane("Mk-100", 50, 2);
        planes.add(mk100);
        Helicopter Vit = new Helicopter("Витязь", 4); 
        helicopters.add(Vit);
        mk100.set_airport(Vnukovo);
        Vit.set_airport(Vnukovo);
        Passeger Ivan = new Passeger("Иван Фомин Николаевич", 2);
        Passeger Vova = new Passeger("Владимир Бубнин Александрович", 4);
        Passeger Kola = new Passeger("Николай Ляпин Гордеевич", 22);
        Passeger Anna = new Passeger("Анна Демидова Денисова", 34);
        Ivan.set_f_a(Vit);
        Vova.set_f_a(Vit);
        Kola.set_f_a(mk100);
        Anna.set_f_a(mk100);
        new Example(Vnukovo, planes, helicopters);
    }
}



class Example
{
    public Example(Airport airport, ArrayList<Plane> planes, ArrayList<Helicopter> helicopters)
    {
        JFrame f = new JFrame("Аэрапорт Внуково");
        f.setDefaultCloseOperation(EXIT_ON_CLOSE);
        JTabbedPane tab = new JTabbedPane(JTabbedPane.BOTTOM,
                                               JTabbedPane.SCROLL_TAB_LAYOUT);
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        ArrayList<String> items = new ArrayList<>();
        for(F_a f_a : airport.getF_as())
        {
            items.add(f_a.getName());
        }
        String[] list_items = new String[items.size()];
        for (int i = 0; i < items.size(); i++) {
            list_items[i] = items.get(i);
        }
        JComboBox comboBox = new JComboBox(list_items);
        new Pas(panel2, (String)comboBox.getSelectedItem(), airport);
        comboBox.addActionListener (new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                    new Pas(panel2, (String)comboBox.getSelectedItem(), airport);
                }
        });
        panel1.add(comboBox);
        // Создание кнопки
        JButton button = new JButton("Выбрать л/а");
        button.addActionListener(new ActionListener() {

           @Override
           public void actionPerformed(ActionEvent arg0) {
               // Делаем видимым второе окно
               String item = (String)comboBox.getSelectedItem();
               new MyDialog(airport, planes, helicopters, item, f);
           }
        });
        String item = (String)comboBox.getSelectedItem();
        
        panel1.add(button);
        tab.addTab("Информация о л/а", panel1);
        tab.addTab("Пассажиры", panel2);
        f.add(tab);
        // Вывод окна на экран
        f.setSize(600, 250);
        f.setVisible(true);
    }
}


class Pas{
    public Pas(JPanel pan, String item, Airport airport){
        pan.removeAll();
        JPanel pan2 = new JPanel();
        for(F_a f_a : airport.getF_as())
        {
            if(item == f_a.getName())
            {
                ArrayList<Passeger> passegers = f_a.get_passegers();
                GridLayout layout = new GridLayout(passegers.size(), 2, 5, 12);
                pan2.setLayout(layout);
                for(Passeger passeger : passegers)
                {
                    JLabel name = new JLabel("ФИО: " + passeger.get_name() + "|");
                    JLabel sit = new JLabel("Место: " + Integer.toString(passeger.get_position()));
                    pan2.add(name);
                    pan2.add(sit);
                }
                break;
            }
        }
        pan.add(pan2);
    }
}


class MyDialog extends JDialog{
    public MyDialog(Airport airport, ArrayList<Plane> planes, ArrayList<Helicopter> helicopters, String item, JFrame f){
        for(F_a f_a : airport.getF_as())
        {
            if(item == f_a.getName())
            {
                JLabel name = null;
                JLabel type_f_a = null;
                JLabel info = null;
                JButton button = new JButton("Взлететь");
                if (f_a.type == "Самолет")
                {
                    for (Plane plane : planes)
                    {
                        if(plane.getName() == item)
                        {
                            button.addActionListener(new ActionListener() {

                                @Override
                                public void actionPerformed(ActionEvent arg0) {
                                    // Делаем видимым второе окно
                                    JOptionPane.showMessageDialog(null, plane.upp());
                                }
                             });
                            name = new JLabel("Название л/а: " + plane.getName() + "\n");
                            type_f_a = new JLabel("Тип л/а: " + plane.type + "\n");
                            info = new JLabel("Длина полосы разгона: " + plane.getLen());
                            break;
                        }
                    }
                }
                else
                {
                    for (Helicopter helicopter : helicopters)
                    {
                        if(helicopter.getName() == item)
                        {
                            button.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent arg0) {
                                JOptionPane.showMessageDialog(null, helicopter.upp());
                            }
                         });
                            name = new JLabel("Название л/а: " + helicopter.getName() + "\n");
                            type_f_a = new JLabel("Тип л/а: " + helicopter.type + "\n");
                            info = new JLabel("Максимальное кол-во посадочных мест: " + helicopter.getSits());
                            break;
                        }
                    }
                }
                JDialog d = new JDialog(f, "Л/а", true);
                JPanel pan = new JPanel();
                JPanel pan2 = new JPanel();
                GridLayout layout2 = new GridLayout(0, 2, 5, 12);
                pan2.add(button);
                JButton button2 = new JButton("Закрыть");
                button2.addActionListener(new ActionListener() {
                   @Override
                   public void actionPerformed(ActionEvent arg0) {
                       d.dispose();
                   }
                });
                pan2.add(button2);
                GridLayout layout = new GridLayout(4, 0, 5, 12);
                pan.setLayout(layout);
                d.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                pan.add(name);
                pan.add(type_f_a);
                pan.add(info);
                pan.add(pan2);
                d.add(pan);
                d.pack();
                d.setVisible(true);
                break;
            }
        }
    }
}
