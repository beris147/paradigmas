/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chisme.nolike.classes;

/**
 *
 * @author beris
 */
public class Dialog {

    public static void showError(Response response) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error en la solicitud");
            alert.setHeaderText("Error: " + response.getStatus());
            HashMap<String, Object> map = (HashMap<String, Object>) response
                    .getJsonResponse()
                    .getJSONArray("items")
                    .getJSONObject(0)
                    .toMap();
            String message = "";
            message = map
                    .entrySet()
                    .stream()
                    .map((entry) -> entry.getKey() + " : " + entry.getValue() + "\n")
                    .reduce(message, String::concat);

            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    public static void showError(String error) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error en la solicitud");
            alert.setContentText(error);
            alert.showAndWait();
        });
    }
    
    public static void showSuccess(String header, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Petición correcta");
            alert.setHeaderText(header);
            alert.setContentText(content);
            alert.show();
        });
    }

    public static void showUnexpectedError(Exception ex) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Exception Dialog");
            alert.setHeaderText("Look, an Exception Dialog");
            alert.setContentText("Could not find file blabla.txt!");
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String exceptionText = sw.toString();
            Label label = new Label("The exception stacktrace was:");
            TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(true);
            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            textArea.setWrapText(true);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);
            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);
            alert.getDialogPane().setExpandableContent(expContent);
            alert.showAndWait();
        });
    }

    public static boolean showConfirmation(String action) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmación requerida");
        alert.setHeaderText(action);
        alert.setContentText("¿Desea proseguir?");
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }
}
