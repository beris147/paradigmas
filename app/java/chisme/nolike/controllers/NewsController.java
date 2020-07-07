package chisme.nolike.controllers;

public class NewsController {

    private News news;
    private boolean changes;
    
    @FXML
    void initialize(){
        this.changes = false;
        this.showHtml.textProperty().addListener((observable,oldValue,newValue)->{
            this.changes = true;
        });
    }
    
    private String getPreviewCode(String mycode){
        ObservableList<Code> codes = this.linksList.getItems();
        for(Code code : codes){
            mycode = mycode.replace(getCodeForProduction(code.getFinalCode()), code.getPreviewCode());
        }
        ObservableList<Image> images = this.imagesList.getItems();
        for(Image image : images){
            mycode = mycode.replace(image.getFinalCode(), image.getPreviewCode());
        }
        mycode = mycode
                .replace("class=\"texto px-sm-5 py-sm-2 px-3 py-1\"", "")
                .replace("\n", "");
        return mycode;
    }
    
    private String getFinalCode(){
        String mycode = getStringBewteen(this.htmlEditor.getHtmlText(), "<body contenteditable=\"true\">", ">", "</body>");
        ObservableList<Code> codes = this.linksList.getItems();
        for(Code code : codes){
            mycode = mycode.replace(code.getPreviewCode(), code.getFinalCode());
        }
        ObservableList<Image> images = this.imagesList.getItems();
        for(Image image : images){
            mycode = mycode.replace(image.getPreviewCode(), image.getFinalCode());
        }
        return getCodeForProduction(mycode);
    }
    
    @FXML
    private void goIndex() {
        if (showConfirmation("regresar, cambios no guardados se perderán")) {
            new Thread(() -> {
                App.scene.setCursor(Cursor.WAIT);
                try {
                    Thread.sleep(100);
                    App.setRoot("index", 800, 600);
                } catch (IOException | InterruptedException ex) {
                    showUnexpectedError(ex);
                } 
                App.scene.setCursor(Cursor.DEFAULT);
            }).start();
        }
    }

    @FXML
    private void postImage() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        TextInputDialog dialog = new TextInputDialog("Imagen");
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setTitle("Nombre de imágen");
        dialog.setHeaderText("Introduce el nombre de la imágen");
        dialog.setContentText("Nombre:");
        Optional<String> imageName = (file != null) ? dialog.showAndWait() : null;
        if (file != null && imageName.isPresent()) {
            try {
                JSONObject json = new JSONObject()
                    .put("archivo", file)
                    .put("nombre", imageName.get())
                    .put("noticia", this.news.getId());
                Image newImage = new Image(json);
                Response response = newImage.upload();
                if (response.responseOk()) {
                    initializeImageList();
                } else {
                    showError(response);
                }
            } catch (IOException ex) {
                showUnexpectedError(ex);
            }
        }
    }
    
    @FXML
    private void deleteImage() {
        Image oldImage = (Image) this.imagesList.getSelectionModel().getSelectedItem();
        if (oldImage == null) {
            showError("Seleccione la imagen a borrar");
        } else if (showConfirmation("Eliminar imagen " + oldImage.getName())) {
            try {
                Response response = oldImage.delete();
                if (response.responseOk()) {
                    initializeImageList();
                    this.htmlEditor.setHtmlText(this.htmlEditor.getHtmlText().replace(oldImage.getPreviewCode(), ""));
                } else {
                    showError(response);
                }
            } catch (IOException ex) {
                showUnexpectedError(ex);
            }
        }
    }

    @FXML
    private void postLink() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Nuevo link");
        dialog.setHeaderText("Inserta el nombre y el código a insertar");
        dialog.initStyle(StageStyle.UTILITY);
        ButtonType okButtonType = new ButtonType("OK", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPadding(new Insets(20, 150, 10, 10));
        TextField linkName = new TextField("Nombre");
        linkName.setPromptText("Nombre");
        TextArea htmlCode = new TextArea();
        htmlCode.setWrapText(true);
        htmlCode.setPromptText("</Código>");
        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(linkName, 1, 0);
        grid.add(new Label("Código html:"), 0, 1);
        grid.add(htmlCode, 1, 1);
        Node okButton = dialog.getDialogPane().lookupButton(okButtonType);
        okButton.setDisable(true);

        htmlCode.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(() -> linkName.requestFocus());
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return new Pair<>(linkName.getText(), htmlCode.getText());
            }
            return null;
        });
        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent((Pair<String, String> nombre_html) -> {
            String name = nombre_html.getKey(), code = nombre_html.getValue();
            try {
                JSONObject json = new JSONObject()
                        .put("nombre", name)
                        .put("html", encodeString(code))
                        .put("noticia", this.news.getId());
                Code newLink = new Code(json);
                if (newLink.getType() != null) {
                    Response response = newLink.upload();
                    if (response.responseOk()) {
                        initializeLinkList();
                    } else {
                        showError(response);
                    }
                } else {
                    showError("Error en el formato del código");
                }
            } catch (IOException ex) {
                showUnexpectedError(ex);
            }
        });
    }

    @FXML
    private void deleteLink() {
        Code oldLink = (Code) this.linksList.getSelectionModel().getSelectedItem();
        if (oldLink == null) {
            showError("Seleccione el enlace a borrar");
        } else if (showConfirmation("Eliminar enlace " + oldLink.getName())) {
            try {
                Response response = oldLink.delete();
                if (response.responseOk()) {
                    initializeLinkList();
                    this.htmlEditor.setHtmlText(this.htmlEditor.getHtmlText().replace(oldLink.getPreviewCode(), ""));
                } else {
                    showError(response);
                }
            } catch (IOException ex) {
                showUnexpectedError(ex);
            }
        }
    }
    
    @FXML
    private void enableHtmlArea(){
        if (!this.showHtml.isEditable() && showConfirmation(
            this.showHtml.setDisable(false);
            this.showHtml.setEditable(true);
            this.showHtml.setText(getFinalCode());
        }
    }

    @FXML
    private void disableHtmlArea() {
        if (this.showHtml != null) {
            if (this.changes) {
                this.htmlEditor.setHtmlText(this.getPreviewCode(this.showHtml.getText()));
                this.changes = false;
            }
            this.showHtml.setDisable(true);
            this.showHtml.setEditable(false);
        }
    }

    @FXML
    private void save(){
        if(this.showHtml.isEditable()){
            this.news.setNote(getCodeForProduction(this.showHtml.getText()));
            this.htmlEditor.setHtmlText(this.getPreviewCode(this.news.getNote()));
        } else {
            this.news.setNote(this.getFinalCode());
        }
        new Thread(() -> {
            App.scene.setCursor(Cursor.WAIT);
            try {
                Response response = this.news.update();
                if (response.responseOk()) {
                    showSuccess("Éxito", "Noticia guardada");
                } else {
                    showError(response);
                }
            } catch (IOException | ParseException ex) {
                showUnexpectedError(ex);
            }
            App.scene.setCursor(Cursor.DEFAULT);
        }).start();
    }

    public void init(JSONObject json) {
        try {
            PreviewCode(this.news.getNote()));
        } catch (IOException | ParseException ex) {
            showUnexpectedError(ex);
        }
    }

    public void customizeHTMLEditor() {
        Node webNode = this.htmlEditor.lookup(".web-view");
        WebView webView = (WebView) webNode;
        WebEngine engine = webView.getEngine();
        engine.setCreatePopupHandler((PopupFeatures config) -> null);
        engine.setUserStyleSheetLocation(getClass().getResource("/chisme/nolike/styles/webview.css").toExternalForm());
        Button addLink = new Button(), addImage = new Button(), addHyperlink = new Button();
        });
        editHtmlEditor(addLink, addImage, addHyperlink);
        this.htmlEditor.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if( e.isControlDown() && e.getCode() == KeyCode.V) {
                modifyClipboard();
            }
        });
        Button button = (Button) this.htmlEditor.lookup(".html-editor-paste");
        button.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            modifyClipboard();
        });
    }
    
    //<a href="https://www.thesitewizard.com/" target="_blank">thesitewizard.com</a>
    private void addHyperLink(WebEngine engine){
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Hipervínculo ");
        Platform.runLater(() -> linkName.requestFocus());
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return new Pair<>(linkName.getText(), direction.getText());
            }
            return null;
        });
        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent((Pair<String, String> nombre_html) -> {
            String text = nombre_html.getKey(), dir = nombre_html.getValue();
            String href = "<a href=\""+dir+"\" target=\"_blank\">"+text+"</a>";
            this.attachedItem(engine, href);
        });
    }

    private ObservableList<Attached> getListItems(String uri) throws IOException{
        ObservableList<Attached> items = FXCollections.observableArrayList();
        HttpGet get = new HttpGet(serverApi + uri + this.news.getId());
        Response response = httpRequest(get);
        if (response.responseOk()) {
            JSONArray links = response.getJsonResponse().getJSONArray("items");
            for (Object o : links) {
                JSONObject json = (JSONObject) o;
                try {
                    if(uri.contains("adjuntos")){
                        items.add(new Code(json));
                    } else {
                        items.add(new Image(json));
                    }
                } catch (JSONException ex) {
                    showUnexpectedError(ex);
                }
            }
        }
        return items;
    }

    private void initializeLinkList() throws IOException {
        this.linksList.setItems(getListItems("/api/adjuntos/noticia/"));
        this.linksList.setCellFactory(param -> new ListCell<Code>() {
            @Override
            protected void updateItem(Code item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getName() == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
    }

    private void initializeImageList() throws IOException {
        ObservableList<Attached> items = this.getListItems("/api/imagenes/noticia/");
        this.imagesList.setItems(items);
        this.selectThumbnail(items);
        this.imagesList.setCellFactory(param -> new ListCell<Image>() {
            @Override
            protected void updateItem(Image item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getName() == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
    }
    
    private void selectThumbnail(ObservableList<Attached> items){
        Image thumbnail = (Image) this.thumbnailBox.getSelectionModel().getSelectedItem();
        this.thumbnailBox.setItems(items);
        if(thumbnail != null){
            for (Iterator it = this.thumbnailBox.getItems().iterator(); it.hasNext();) {
                Image img = (Image) it.next();
                if(Objects.equals(img.getId(), thumbnail.getId())){
                    this.thumbnailBox.getSelectionModel().select(img);
                    break;
                }
            }
        }
    }
    
    private void selectThumbnail(Integer id){
        for (Iterator it = this.thumbnailBox.getItems().iterator(); it.hasNext();) {
            Image img = (Image) it.next();
            if(Objects.equals(img.getId(), id)){
                this.thumbnailBox.getSelectionModel().select(img);
                break;
            }
        }
    }
}
