package chisme.nolike;

/**
 *
 * @author beris
 */
public class Main {
    //Direction
    public static final String url = "server/api";
    
    //date aux
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    public static void main(String[] args) {
        try {
            App.main(args);
        } catch (Exception ex) {
        }
    }
}
