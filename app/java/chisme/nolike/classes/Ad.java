package chisme.nolike.classes;

/**
 *
 * @author root
 */
public final class Ad {
    private final HashMap<String, Object> map;
    
    public Ad(JSONObject json){
        this.map = (HashMap<String, Object>) json.toMap();
    }

    public Ad(File image, String title, String text, String href) {
        this.map = new HashMap<>();
        this.setFile(image);
        this.setTitle(title);
        this.setText(text);
        this.setHref(href);
    }
    
    public Response upload() throws IOException{
        HttpPost post = new HttpPost(serverApi + "/api/token/anuncios/nuevo");
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        FileBody fileBody = new FileBody(this.getFile());
        builder.addPart("my_file", fileBody);
        JSONObject obj = new JSONObject()
                .put("titulo", this.getTitle())
                .put("href", this.getHref())
                .put("texto", this.getText());
        builder.addTextBody("json", obj.toString(), ContentType.APPLICATION_JSON);
        HttpEntity multiPartEntity = builder.build();
        post.setEntity(multiPartEntity);
        return httpRequest(post, Main.authToken);
    }
    
    public Response update() throws IOException, ParseException{
        HttpPut put = new HttpPut(serverApi + "/api/token/anuncios/"+this.getId());
        JSONObject json = new JSONObject()
                .put("id", this.getId())
                .put("titulo", this.getTitle())
                .put("href", this.getHref())
                .put("texto", this.getText())
                .put("imagen", this.getImage());
        put.addHeader("Content-Type", "application/json");
        put.setEntity(new StringEntity(json.toString(), "UTF-8"));
        return httpRequest(put, Main.authToken);
    }
    
    public Response delete() throws IOException{
        if(showConfirmation("Borrar el anuncio "+this.getTitle())){
            HttpDelete delete = new HttpDelete(serverApi+"/api/token/anuncios/"+this.getId());
            return httpRequest(delete, Main.authToken);
        } else {
            JSONObject json = new JSONObject()
                    .put("cancelado", "ok");
            return new Response(json, 304);
        }
    }

    public Integer getId() {
        return (Integer) this.map.getOrDefault("id", 0);
    }

    public void setId(Integer id) {
        this.map.put("id", id);
    }

    public String getImage() {
        return this.map.getOrDefault("imagen", "").toString();
    }

    public void setImage(String image) {
        this.map.put("imagen", image);
    }

    public String getText() {
        return this.map.getOrDefault("texto", "").toString();
    }

    public void setText(String text) {
        this.map.put("texto", text);
    }

    public String getHref() {
        return this.map.getOrDefault("href", "").toString();
    }

    public void setHref(String href) {
        this.map.put("href", href);
    }

    public String getTitle() {
        return this.map.getOrDefault("titulo", "").toString();
    }

    public void setTitle(String title) {
        this.map.put("titulo", title);
    }

    public File getFile() {
        return (File) this.map.getOrDefault("archivo", null);
    }

    public void setFile(File file) {
        this.map.put("archivo", file);
    }
    
    @Override
    public String toString(){
        return this.getTitle() + " " + this.getHref();
    }
    
}
