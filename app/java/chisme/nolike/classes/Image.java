package chisme.nolike.classes;


/**
 *
 * @author beris
 */
public class Image extends Attached{

    public Image(JSONObject json) {
        super(json);
    }

    public Image(Integer id) throws IOException {
        super(id);
        HttpGet get = new HttpGet(serverApi + "/api/imagenes/"+id);
        Response response = httpRequest(get);
        if(response.responseOk()){
             map = (HashMap<String, Object>) response
                    .getJsonResponse()
                    .getJSONArray("items")
                    .getJSONObject(0)
                    .toMap();
        } else {
            showError(response);
            this.map.clear();
        }
    }
    
    @Override
    public Response upload() throws IOException{
        HttpPost post = new HttpPost(serverApi + "/api/token/imagenes/nueva");
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        FileBody fileBody = new FileBody((File) map.get("archivo"));
        builder.addPart("my_file", fileBody);
        JSONObject obj = new JSONObject()
                .put("alt", map.get("nombre"))
                .put("noticia", map.get("noticia"));
        builder.addTextBody("json", obj.toString(), ContentType.APPLICATION_JSON);
        HttpEntity multiPartEntity = builder.build();
        post.setEntity(multiPartEntity);
        return httpRequest(post, Main.authToken);
    }
    
    @Override
    public String getFinalCode() {
        return "<img src=\""+uploads+map.get("src").toString()+"\" alt=\""+this.getName()+"\" class=\"my-md-5 my-2 py-0 px-lg-5 px-sm-3 px-2 img-fluid col-12\">";
    }
    
    @Override
    public String getPreviewCode(){
        return "<img src=\""+uploads+map.get("src").toString()+"\" style=\"display: block; margin-left: auto; margin-right: auto;\" alt=\""+this.getName()+"\" width=\"560\">";
    }

    @Override
    public Response delete() throws IOException {
        HttpDelete request = new HttpDelete(serverApi + "/api/token/imagenes/" + this.getId());
        return httpRequest(request, Main.authToken);
    }

    @Override
    public String getName() {
        return map.get("alt").toString();
    }
}
