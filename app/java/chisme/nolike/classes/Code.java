package chisme.nolike.classes;

/**
 *
 * @author beris
 */
public final class Code extends Attached{
 
    public Code(JSONObject json) {
        super(json);
        this.map.put("html", decodeString(this.map.get("html").toString()));
        this.init();
        this.map.put("vue", this.setFinalCode());
    }
    
    public Code(Integer id) throws IOException {
        super(id);
        HttpGet get = new HttpGet(serverApi + "/api/adjuntos/"+id);
        Response response = httpRequest(get);
        if(response.responseOk()){
            this.map = (HashMap<String, Object>) response
                    .getJsonResponse()
                    .getJSONArray("items")
                    .getJSONObject(0)
                    .toMap();
        } else {
            showError(response);
            this.map.clear();
        }
    }
    

    public void init() {
        String html = this.map.get("html").toString(), type = null;
        if (html.contains("https://www.youtube") && html.contains("/embed/")) {
            type = "youtube";
        } else if (html.contains("class=\"twitter-tweet\"")) {
           type = "twitter";
        } else if (html.contains("class=\"fb-video\"") || html.contains("class=\"fb-post\"")) {
            type = "facebook";
        } else if (html.contains("class=\"instagram-media\"") || html.contains("www.instagram.com/embed")) {
            type = "instagram";
        }
        this.map.put("tipo", type);
    }
    
    @Override
    public String getFinalCode() {
        return this.map.getOrDefault("vue", "<!-- error -->").toString();
    }
    
    public String setFinalCode() {
        String type = this.map.get("tipo").toString(), code = "", html = this.map.get("html").toString();
        switch(type){
            case "youtube":
                String youtubeLink = getStringBewteen(html, "src=", "\"", "\"");
                code =  "<b-embed" +
                        " type=\"iframe\"" +
                        " aspect=\"16by9\"" +
                        " src=\""+youtubeLink+"\"" +
                        " allowfullscreen></b-embed>";
                break;
            case "twitter":
                String idTweet = getStringBewteen(html, "status/", "/", "?");
                code = "<div class=\"redesPubli\"><Tweet id=\""+idTweet+"\"  class=\"mx-auto\" style=\"display: flex; align-items: center; justify-content: center;\"></Tweet></div>";
                break;
            case "facebook":
                code =  "<div class=\"row col-12 m-0 p-0\">"+html
                        .replace("class=\"fb-video\"", "class=\"fb-video mx-auto\" style=\"overflow-x: auto; max-width: 100%;\"")
                        .replace("class=\"fb-post\"", "class=\"fb-post mx-auto\" style=\"overflow-x: auto; max-width: 100%;\"")
                        .replaceAll("\\s{1,}\\n", "\n")
                        .replaceAll("\\n{1,}", "\n")+"</div>";
                break;
            case "instagram":
                String instagramLink = getStringBewteen(html, "href=", "\"", "?");
                code = "<div class=\"redesPubli\"><instagram-embed class=\"mx-auto\" style=\"display: flex; align-items: center; justify-content: center;\"" +
                        " :url=\"\'"+instagramLink+"\'\"" +
                        "/></div>";
                break;
        }
        return code;
    }
    
    @Override
    public String getPreviewCode() {
        return "<img src=\""+resources+"generated/"+this.getId()+".jpg\" style=\"display: block; margin-left: auto; margin-right: auto;\" id=\""+this.getId()+"\" alt=\""+this.getType()+this.getName()+"\">";
    }

    @Override
    public Response upload() throws IOException {
        HttpPost post = new HttpPost(serverApi + "/api/token/adjuntos/nuevo");
        JSONObject json = new JSONObject()
                .put("nombre", this.map.get("nombre"))
                .put("html", encodeString(this.map.get("html").toString()))
                .put("vue", encodeString(this.map.get("vue").toString()))
                .put("tipo", this.map.get("tipo"))
                .put("noticia", this.map.get("noticia"));
        post.addHeader("Content-Type", "application/json");
        post.setEntity(new StringEntity(json.toString(), "UTF-8"));
        return httpRequest(post, Main.authToken);
    }

    @Override
    public Response delete() throws IOException {
        HttpDelete request = new HttpDelete(serverApi + "/api/token/adjuntos/" + this.getId());
        return httpRequest(request, Main.authToken);
    }

    @Override
    public String getName() {
        return this.map.get("nombre").toString();
    }
    
    public String getType(){
        return this.map.get("tipo").toString();
    }

    public void setCode(String code) {
        this.map.put("html", code);
    }
}
