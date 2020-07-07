package chisme.nolike;

/**
 * JavaFX App
 */
public class App extends Application {

    public static Scene scene;
    private static Stage stage;

    @Override
    public void start(Stage s) throws IOException {
        stage = s;
        scene = new Scene(loadFXML("login"), 300, 500);
        show();
    }

    
    public static void setRoot(String fxml, double w, double h) throws IOException {
        Platform.runLater(() -> {
            try {
                stage.close();
                scene = new Scene(loadFXML(fxml), w, h);
                show();
            } catch (IOException ex) {
                showUnexpectedError(ex);
            }
        });
    }
    
    public static void setRoot(String fxml, double w, double h, JSONObject json) throws IOException {
        Platform.runLater(() -> {
            try {
                stage.close();
                FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
                Parent root = (Parent) fxmlLoader.load();
                scene = new Scene(root, w, h);
                show();
                switch (fxml) {
                    case "news":
                        NewsController controller = fxmlLoader.<NewsController>getController();
                        controller.init(json);
                        controller.customizeHTMLEditor();
                        break;
                    default:
                        break;
                }
            } catch (IOException ex) {
               showUnexpectedError(ex);
            }
        });
    }
    
    static void setSize(double w, double h){
        stage.setWidth(w);
        stage.setHeight(h);
    }
    
    static void show(){
        stage.setScene(scene);
        stage.setOnCloseRequest((WindowEvent event) -> {
            Platform.exit();
            Thread start = new Thread(() -> {
                System.exit(0);
            });
            start.start();
        });
        stage.show();
    }
    
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml")); 
        return fxmlLoader.load();
    }
    
    private static Parent loadFXML(String fxml, JSONObject json) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        Parent root = (Parent)fxmlLoader.load();  
        switch(fxml){
            case "news":
                NewsController controller = fxmlLoader.<NewsController>getController();
                controller.init(json);
                break;
            default:
                break;
        }
        return root;
    }

    public static void main(String[] args) {
        launch();
    }
    
    public static void cursorWait(){
        Runnable wait = () -> { 
            scene.setCursor(Cursor.WAIT); 
            System.out.println("wait is running"); 
            for(int i=0; i < 1000000000; i++ )
                System.out.println("waiting " + i);
        };
        new Thread(wait).start();
    }
}