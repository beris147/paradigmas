package chisme.nolike.controllers;

public class LoginController {
    
    @FXML
    TextField userField;
    @FXML
    PasswordField passwordField;
    
    @FXML
    void initialize(){
        setLoginToken();
    }

    @FXML
    private void login() {
        new Thread(() -> {
            App.scene.setCursor(Cursor.WAIT);
            try {
                Functions.setLoginToken();
                HttpPost post = new HttpPost(serverApi + "/api/usuario/login");
                JSONObject body = new JSONObject()
                        .put("usuario", this.userField.getText())
                        .put("pass", this.passwordField.getText());
                post.addHeader("Content-Type", "application/json");
                post.setEntity(new StringEntity(body.toString(), "UTF-8"));
                Response response = httpRequest(post, Main.loginToken);
                if (response.responseOk()) {
                    JSONObject json = response
                            .getJsonResponse()
                            .getJSONArray("items")
                            .getJSONObject(0);
                    Main.authToken = json.getString("jwt");
                    Main.refreshToken = json.getString("refresh");
                    App.setRoot("index", 800, 600);
                } else {
                    Dialog.showError(response);
                }

            } catch (IOException ex) {
                showUnexpectedError(ex);
            }
            App.scene.setCursor(Cursor.DEFAULT);
        }).start();
    }
}
