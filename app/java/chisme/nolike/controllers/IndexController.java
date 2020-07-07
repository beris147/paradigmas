package chisme.nolike.controllers;
public class IndexController {
    
    private int limit, pages, total;
    
    @FXML
    private ListView newsList, categoriesList, adsList;
    @FXML
    private Spinner<Integer> pagesSpinner, limitSpinner;

    @FXML
    public void initialize() {
        try {
            this.iniatilizePagination();
            this.initializeCategories();
            this.initializeNewsList();
            this.initializeAds();
        } catch (IOException ex) {
            showUnexpectedError(ex);
        }
    }
    
    @FXML
    private void logout(){
        try {
            App.setRoot("login", 300, 600);
            Main.authToken = "";
            Main.refreshToken = "";
            setLoginToken();
        } catch (IOException ex) {
        }
    }
    
    @FXML
    private void createAd(){
        Dialog<Ad> dialog = new Dialog<>();
        dialog.setTitle("Nuevo anuncio");
        dialog.setHeaderText("Añadir nuevo anuncio");
        dialog.initStyle(StageStyle.UTILITY);
        ButtonType okButtonType = new ButtonType("Ok", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        TextField title = new TextField();
        title.setPromptText("Titulo");
        
        TextArea text = new TextArea();
        text.setPromptText("Texto"); text.setWrapText(true);
        
        Button fileButton = new Button("...");
        TextField path = new TextField();
        path.setEditable(false); path.setPromptText("/archivo/direccion");
        
        TextField href = new TextField();
        href.setPromptText("https://pagina.web");  
        
        fileButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override public void handle(ActionEvent e) {
                File file = new FileChooser().showOpenDialog(null);
                if(file != null){
                    path.setText(file.getAbsolutePath());
                } else { 
                    path.setText("");
                }
            }
        });
       
        grid.add(new Label("Titulo:"), 0, 0); grid.add(title, 1, 0);
        grid.add(new Label("Texto:"), 0, 1); grid.add(text, 1, 1);
        grid.add(new Label("Imagen:"), 0, 2); grid.add(path, 1, 2); grid.add(fileButton, 2, 2);
        grid.add(new Label("Pagina ref:"), 0, 4); grid.add(href, 1, 4);
        
        Node okButton = dialog.getDialogPane().lookupButton(okButtonType);
        okButton.setDisable(true);
        title.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue.trim().isEmpty());
        });
        dialog.getDialogPane().setContent(grid);
        Platform.runLater(() -> title.requestFocus());
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return new Ad(new File(path.getText()), title.getText(), text.getText(), href.getText());
            }
            return null;
        });
        Optional<Ad> result = dialog.showAndWait();
        result.ifPresent((Ad res) -> {
            new Thread(() -> {
                App.scene.setCursor(Cursor.WAIT);
                try {
                    Response response = res.upload();
                    if (response.responseOk()) {
                        showSuccess("Completado", "Anuncio generado");
                        Platform.runLater(() -> {
                            try {
                                this.initializeAds();
                            } catch (IOException ex) {
                                showUnexpectedError(ex);
                            }
                        });
                    } else {
                        showError(response);
                    }
                } catch (Exception ex) {
                    showUnexpectedError(ex);
                }
                App.scene.setCursor(Cursor.DEFAULT);
            }).start();
        });
    }
    
    @FXML
    private void createCategory(){
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Nueva categoría");
        dialog.setHeaderText("Añadir nueva categoría");
        dialog.initStyle(StageStyle.UTILITY);
        ButtonType okButtonType = new ButtonType("Ok", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        TextField title = new TextField();
        title.setPromptText("Nombre");
        ColorPicker color = new ColorPicker();
        grid.add(new Label("Nombre:"), 0, 0); grid.add(title, 1, 0);
        grid.add(new Label("Color:"), 0, 1); grid.add(color, 1, 1);
        Node okButton = dialog.getDialogPane().lookupButton(okButtonType);
        okButton.setDisable(true);
        title.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue.trim().isEmpty());
        });
        dialog.getDialogPane().setContent(grid);
        Platform.runLater(() -> title.requestFocus());
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return new Pair<>(title.getText(), getHexColor(color.getValue()));
            }
            return null;
        });
        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent((Pair<String, String> res) -> {
            new Thread(() -> {
                App.scene.setCursor(Cursor.WAIT);
                try {
                    Category category = new Category(0, res.getKey(), res.getValue());
                    Response response = category.upload();
                    if (response.responseOk()) {
                        Platform.runLater(() -> {
                            try {
                                this.initializeCategories();
                            } catch (IOException ex) {
                                showUnexpectedError(ex);
                            }
                        });
                        
                    } else {
                        showError(response);
                    }
                } catch (IOException ex) {
                    showUnexpectedError(ex);
                }
                App.scene.setCursor(Cursor.DEFAULT);
            }).start();
        });
    }

    @FXML
    private void createNews(){
        Dialog<Pair<String, Category>> dialog = new Dialog<>();
        dialog.setTitle("Nueva noticia");
        dialog.setHeaderText("Añadir nueva noticia");
        dialog.initStyle(StageStyle.UTILITY);
        ButtonType okButtonType = new ButtonType("Ok", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        TextField title = new TextField();
        title.setPromptText("Título");
        ChoiceBox categoryBox = new ChoiceBox();
        categoryBox.setItems(Main.categoryItems);
        categoryBox.getSelectionModel().selectFirst();
        grid.add(new Label("Título:"), 0, 0); grid.add(title, 1, 0);
        grid.add(new Label("Categoría:"), 0, 1); grid.add(categoryBox, 1, 1);
        Node okButton = dialog.getDialogPane().lookupButton(okButtonType);
        okButton.setDisable(true);
        title.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue.trim().isEmpty());
        });
        dialog.getDialogPane().setContent(grid);
        Platform.runLater(() -> title.requestFocus());
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return new Pair<>(title.getText(), (Category) categoryBox.getSelectionModel().getSelectedItem());
            }
            return null;
        });
        Optional<Pair<String, Category>> result = dialog.showAndWait();
        result.ifPresent((Pair<String, Category> res) -> {
            new Thread(() -> {
                App.scene.setCursor(Cursor.WAIT);
                try {
                    News news = new News(res.getKey(), res.getValue());
                    Response response = news.upload();
                    if (response.responseOk()) {
                        App.setRoot("news", 800, 600, response
                                .getJsonResponse()
                                .getJSONArray("items")
                                .getJSONObject(0));
                    } else {
                        showError(response);
                    }
                } catch (IOException ex) {
                    showUnexpectedError(ex);
                }
                App.scene.setCursor(Cursor.DEFAULT);
            }).start();
        });
    }

    @FXML
    private void newLimit(){
        try {
            int currentPage = this.pagesSpinner.getValue();
            this.limit = this.limitSpinner.getValue();
            this.pages = (int) Math.ceil((float) this.total / (float) this.limit);
            currentPage = (currentPage <= pages) ? currentPage : pages;
            this.pages = (this.pages >= 1)? this.pages : 1;
            SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, this.pages, currentPage);
            this.pagesSpinner.setValueFactory(valueFactory);
            this.initializeNewsList();
        } catch (IOException ex) {
            showUnexpectedError(ex);
        }
    }
    
    private void editCategory(Category category){
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Editar categoría");
        dialog.setHeaderText("Editar " + category.getName());
        dialog.initStyle(StageStyle.UTILITY);
        ButtonType okButtonType = new ButtonType("Ok", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        TextField title = new TextField(category.getName());
        title.setPromptText("Nombre");
        ColorPicker color = new ColorPicker(hex2Rgb(category.getColor()));
        grid.add(new Label("Nombre:"), 0, 0); grid.add(title, 1, 0);
        grid.add(new Label("Color:"), 0, 1); grid.add(color, 1, 1);
        Node okButton = dialog.getDialogPane().lookupButton(okButtonType);
        okButton.setDisable(true);
        title.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue.trim().isEmpty());
        });
        dialog.getDialogPane().setContent(grid);
        Platform.runLater(() -> title.requestFocus());
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return new Pair<>(title.getText(), getHexColor(color.getValue()));
            }
            return null;
        });
        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent((Pair<String, String> res) -> {
            new Thread(() -> {
                App.scene.setCursor(Cursor.WAIT);
                try {
                    category.setName(res.getKey());
                    category.setColor(res.getValue());
                    Response response = category.update();
                    if (response.responseOk()) {
                        Platform.runLater(() -> {
                            try {
                                this.initializeCategories();
                            } catch (IOException ex) {
                                showUnexpectedError(ex);
                            }
                        });
                    } else {
                        showError(response);
                    }
                } catch (IOException | ParseException ex) {
                    showUnexpectedError(ex);
                }
                App.scene.setCursor(Cursor.DEFAULT);
            }).start();
        });
    }
    
    private void editAd(Ad ad){
        Dialog<Ad> dialog = new Dialog<>();
        dialog.setTitle("Editar anuncio");
        dialog.setHeaderText("Editando anuncio "+ ad.getTitle());
        dialog.initStyle(StageStyle.UTILITY);
        ButtonType okButtonType = new ButtonType("Ok", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        TextField title = new TextField(ad.getTitle());
        title.setPromptText("Titulo");
        
        TextArea text = new TextArea(ad.getText());
        text.setPromptText("Texto"); text.setWrapText(true);
        
        TextField href = new TextField(ad.getHref());
        href.setPromptText("https://pagina.web");  
       
        grid.add(new Label("Titulo:"), 0, 0); grid.add(title, 1, 0);
        grid.add(new Label("Texto:"), 0, 1); grid.add(text, 1, 1);
        grid.add(new Label("Pagina ref:"), 0, 4); grid.add(href, 1, 4);
        
        Node okButton = dialog.getDialogPane().lookupButton(okButtonType);

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(() -> title.requestFocus());
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return new Ad(null, title.getText(), text.getText(), href.getText());
            }
            return null;
        });
        Optional<Ad> result = dialog.showAndWait();
        result.ifPresent((Ad res) -> {
            new Thread(() -> {
                App.scene.setCursor(Cursor.WAIT);
                try {
                    ad.setTitle(res.getTitle());
                    ad.setHref(res.getHref());
                    ad.setFile(res.getFile());
                    ad.setText(res.getText());
                    Response response = ad.update();
                    if (response.responseOk()) {
                        showSuccess("Completado", "Anuncio actualizado");
                        Platform.runLater(() -> {
                            try {
                                this.initializeAds();
                            } catch (IOException ex) {
                                showUnexpectedError(ex);
                            }
                        });
                    } else {
                        showError(response);
                    }
                } catch (IOException | ParseException ex) {
                    showUnexpectedError(ex);
                }
                App.scene.setCursor(Cursor.DEFAULT);
            }).start();
        });
    }
    
    @FXML
    private void initializeNewsList() throws IOException{
        ObservableList<News> newsItems = FXCollections.observableArrayList ();
        HttpGet get = new HttpGet(serverApi+"/api/token/noticias/pagina="+this.pagesSpinner.getValue()+"/limite="+this.limitSpinner.getValue());
        Response response = httpRequest(get, Main.authToken);
        if (response.responseOk()) {
            JSONArray news = response.getJsonResponse().getJSONArray("items");
            for (Object o : news) {
                JSONObject json = (JSONObject) o;
                try {
                    newsItems.add(new News(json));
                } catch (JSONException | ParseException ex) {
                    showUnexpectedError(ex);
                }
            }
            
        }
        this.newsList.setItems(newsItems);
        this.newsList.setCellFactory(param ->  {
            ListCell<News> cell = new ListCell<News>(){
                @Override
                protected void updateItem(News item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null || item.getTitle() == null) {
                        setText(null);
                    } else {
                        setText(item.toString());
                    }
                }
            };
            ContextMenu contextMenu = new ContextMenu();
            MenuItem editItem = new MenuItem();
            editItem.textProperty().bind(Bindings.format("Editar", cell.itemProperty()));
            editItem.setOnAction(event -> {
                new Thread(() -> {
                    App.scene.setCursor(Cursor.WAIT);
                    News item = cell.getItem();
                    try {
                        Thread.sleep(500);
                        App.setRoot("news", 800, 600, item.getJson());
                    } catch (IOException | InterruptedException ex) {
                        showUnexpectedError(ex);
                    }
                    App.scene.setCursor(Cursor.DEFAULT);
                }).start();

            });
            MenuItem deleteItem = new MenuItem();
            deleteItem.textProperty().bind(Bindings.format("Borrar", cell.itemProperty()));

            deleteItem.setOnAction(event -> {
                try {
                    Response responseDelete = cell.getItem().delete();
                    if (responseDelete.responseOk()) { 
                        new Thread(() -> {
                            App.scene.setCursor(Cursor.WAIT);
                            Platform.runLater(() -> {
                                 this.newsList.getItems().remove(cell.getItem());
                            });
                            showSuccess("Éxito", "Noticia borrada correctamente");
                            App.scene.setCursor(Cursor.DEFAULT);
                        }).start();
                    } else {
                        showError("Noticia no borrada");
                    }
                } catch (IOException ex) {
                    showUnexpectedError(ex);
                }
                App.scene.setCursor(Cursor.DEFAULT);
            });
            contextMenu.getItems().addAll(editItem, deleteItem);
            //cell.textProperty().bind(cell.itemProperty().asString());
            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(contextMenu);
                }
            });
            return cell;
        });
    }
    
    private void initializeCategories() throws IOException {
        Main.categoryItems = FXCollections.observableArrayList();
        HttpGet get = new HttpGet(serverApi + "/api/categorias");
        Response response = httpRequest(get);
        if (response.responseOk()) {
            JSONArray links = response.getJsonResponse().getJSONArray("items");
            for (Object o : links) {
                JSONObject json = (JSONObject) o;
                try {
                    Main.categoryItems.add(new Category(json));
                } catch (JSONException ex) {
                    showUnexpectedError(ex);
                }
            }
        } else {
            showError(response);
        }
        this.categoriesList.setItems(Main.categoryItems);
        this.categoriesList.setCellFactory(param ->  {
            ListCell<Category> cell = new ListCell<Category>(){
                @Override
                protected void updateItem(Category item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null || item.getName()== null) {
                        setText(null);
                    } else {
                        setText(item.toString());
                    }
                }
            };
            ContextMenu contextMenu = new ContextMenu();
            MenuItem editItem = new MenuItem();
            editItem.textProperty().bind(Bindings.format("Editar", cell.itemProperty()));
            editItem.setOnAction(event -> {
                Category item = cell.getItem();
                this.editCategory(item);
            });
            MenuItem deleteItem = new MenuItem();
            deleteItem.textProperty().bind(Bindings.format("Borrar", cell.itemProperty()));
            
            deleteItem.setOnAction((event) -> {
                try {
                    Response responseDelete = cell.getItem().delete();
                    if (responseDelete.responseOk()) {
                        new Thread(() -> {
                            App.scene.setCursor(Cursor.WAIT);
                            Platform.runLater(() -> {
                                this.categoriesList.getItems().remove(cell.getItem());
                            });
                            showSuccess("Éxito", "Categoría borrada correctamente");
                            App.scene.setCursor(Cursor.DEFAULT);
                        }).start();
                    } else {
                        showError("Categoría no borrada");
                    }
                } catch (IOException ex) {
                    showUnexpectedError(ex);
                }
            });
            contextMenu.getItems().addAll(editItem, deleteItem);
            //cell.textProperty().bind(cell.itemProperty().asString());
            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(contextMenu);
                }
            });
            return cell;
        });
    }
    
    private void initializeAds() throws IOException {
        ObservableList<Ad> adsItems  = FXCollections.observableArrayList();
        HttpGet get = new HttpGet(serverApi + "/api/anuncios");
        Response response = httpRequest(get);
        if (response.responseOk()) {
            JSONArray ads = response.getJsonResponse().getJSONArray("items");
            for (Object o : ads) {
                JSONObject json = (JSONObject) o;
                try {
                    adsItems.add(new Ad(json));
                } catch (JSONException ex) {
                    showUnexpectedError(ex);
                }
            }
        }
        this.adsList.setItems(adsItems);
        this.adsList.setCellFactory(param ->  {
            ListCell<Ad> cell = new ListCell<Ad>(){
                @Override
                protected void updateItem(Ad item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null || item.getTitle()== null) {
                        setText(null);
                    } else {
                        setText(item.toString());
                    }
                }
            };
            ContextMenu contextMenu = new ContextMenu();
            MenuItem editItem = new MenuItem();
            editItem.textProperty().bind(Bindings.format("Editar", cell.itemProperty()));
            editItem.setOnAction(event -> {
                Ad item = cell.getItem();
                this.editAd(item);
            });
            MenuItem deleteItem = new MenuItem();
            deleteItem.textProperty().bind(Bindings.format("Borrar", cell.itemProperty()));

            deleteItem.setOnAction(event -> {
                try {
                    Response responseDelete = cell.getItem().delete();
                    if (responseDelete.responseOk()) {
                        new Thread(() -> {
                            App.scene.setCursor(Cursor.WAIT);
                            Platform.runLater(() -> {
                                this.adsList.getItems().remove(cell.getItem());
                            });
                            showSuccess("Éxito", "Anuncio eliminado");
                            App.scene.setCursor(Cursor.DEFAULT);
                        }).start();
                    } else {
                        showError(responseDelete);
                    }
                } catch (IOException ex) {
                    showUnexpectedError(ex);
                }
            });
            contextMenu.getItems().addAll(editItem, deleteItem);
            //cell.textProperty().bind(cell.itemProperty().asString());
            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(contextMenu);
                }
            });
            return cell;
        });
    }

    private void iniatilizePagination() throws IOException {
        this.limit = 20;
        HttpGet get = new HttpGet(serverApi + "/api/noticias/total");
        Response response = httpRequest(get);
        if (response.responseOk()) {
            JSONObject json = response.getJsonResponse().getJSONArray("items").getJSONObject(0);
            this.total = json.getInt("value");
            this.pages = (this.total > 0)? (int) Math.ceil((float) this.total / (float) this.limit) : 1;
            loadPagination();
        } else {
            showError(response);
        }
    }
    
    private void loadPagination(){
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, this.limit);
        this.limitSpinner.setValueFactory(valueFactory);
        
        valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, this.pages, 1);
        this.pagesSpinner.setValueFactory(valueFactory);
    }
}
