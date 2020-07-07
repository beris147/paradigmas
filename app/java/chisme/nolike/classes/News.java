package chisme.nolike.classes;


/**
 *
 * @author beris
 */
public class News {
    private final HashMap<String,Object> map;

    public News(String title, Category category) {
        map = new HashMap<>();
        map.put("titulo", title);
        map.put("categoria", category.getId());
    }
    
    public News(JSONObject json) throws ParseException, IOException{
        map = (HashMap<String, Object>) json.toMap();
        map.put("nota", decodeString((String) map.get("nota")));
        Object thumbnailID = map.get("miniatura");
    }
    
    public Response upload() throws IOException{
        HttpPost post = new HttpPost(serverApi + "/api/token/noticias/nueva");
        JSONObject json = new JSONObject()
                .put("titulo", map.get("titulo"))
                .put("categoria", map.get("categoria"));
        post.addHeader("Content-Type", "application/json");
        post.setEntity(new StringEntity(json.toString(), "UTF-8"));
        return httpRequest(post, Main.authToken);
    }
    
    public Response update() throws IOException, ParseException{
        HttpPut put = new HttpPut(serverApi + "/api/token/noticias/"+ map.get("id"));
        Date date = this.getDate();
        String fecha = dateFormat.format(date);
        JSONObject json = new JSONObject()
                .put("id", this.getId())
                .put("titulo", this.getTitle())
                .put("categoria", this.getCategory())
                .put("descripcion", this.getDescription())
                .put("nota", encodeString(this.getNote()))
                .put("principal", this.getPrimary())
                .put("fecha", fecha)
                .put("firma", this.getAutor())
                .put("mostrar", this.getShow())
                .put("miniatura", (this.getThumbnail() != 0) ? this.getThumbnail() : null)
                .put("clicks", this.getClicks());
        put.addHeader("Content-Type", "application/json");
        put.setEntity(new StringEntity(json.toString(), "UTF-8"));
        return httpRequest(put, Main.authToken);
    }
    
    public Response delete() throws IOException{
        if(showConfirmation("Borrar la noticia "+this.getTitle())){
            HttpDelete delete = new HttpDelete(serverApi + "/api/token/noticias/"+this.getId());
            return httpRequest(delete, Main.authToken);
        } else {
            JSONObject json = new JSONObject()
                    .put("cancelado", "ok");
            return new Response(json, 304);
        }
    }

    public Integer getId() {
        return (Integer) map.getOrDefault("id", 0);
    }

    public void setId(Integer id) {
        map.put("id", id);
    }

    public Integer getCategory() {
        return (Integer) map.getOrDefault("categoria", 0);
    }

    public void setCategory(Category category) {
        map.put("categoria", category.getId());
    }

    public String getTitle() {
        return map.getOrDefault("titulo", "").toString();
    }

    public void setTitle(String title) {
        map.put("titulo", title);
    }

    public String getDescription() {
        return map.getOrDefault("descripcion", "").toString();
    }

    public void setDescription(String description) {
        map.put("descripcion", description);
    }

    public String getNote() {
        return map.getOrDefault("nota", "").toString();
    }

    public void setNote(String note) {
        map.put("nota", note);
    }

    public Boolean getPrimary() {
        return ((Integer)map.getOrDefault("principal", 0)) == 1;
    }

    public void setPrimary(Boolean primary) {
        Integer value = (primary)? 1 : 0;
        map.put("principal", value);
    }

    public Date getDate() throws ParseException {
        Object obj = map.getOrDefault("fecha", new Date());
        if(obj == null) return new Date();
        if(obj instanceof Date) return (Date) obj;
        String sdate = obj.toString();
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(sdate);  
        return date;
    }

    public void setDate(Date date) {
        map.put("fecha", date);
    }

    public String getAutor() {
        return map.getOrDefault("firma", "").toString();
    }

    public void setAutor(String autor) {
        map.put("firma", autor);
    }

    public Boolean getShow() {
        return ((Integer) map.getOrDefault("mostrar", 0)) == 1;
    }

    public void setShow(Boolean show) {
        Integer value = (show)? 1 : 0;
        map.put("mostrar", value);
    }
    
    public Integer getThumbnail() {
        try {
            Object obj = this.map.getOrDefault("miniatura", null);
            if(obj == null) return 0;
            if (obj instanceof Integer) return (Integer) obj;
            com.fasterxml.jackson.databind.ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(obj);
            JSONObject miniatura = new JSONObject(json);
            return (miniatura.has("id")) ? miniatura.getInt("id") : 0;
        } catch (JsonProcessingException ex) {
            showUnexpectedError(ex);
        }
        return 0;
    }

    public void setThumbnail(Image thumbnail) {
        Integer thumbnailId = (thumbnail != null)? thumbnail.getId() : null;
        map.put("miniatura", thumbnailId);
    }

    public Integer getClicks() {
        return (Integer) map.getOrDefault("clicks", 0);
    }
    
    public JSONObject getJson(){
        return new JSONObject(this.map);
    }
    
    @Override
    public String toString(){
        return this.getId() + "  " + this.getTitle() + " por: " + this.getAutor();
    }

}
