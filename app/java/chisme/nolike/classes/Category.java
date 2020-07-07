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
public class Category {
    private HashMap<String, Object> map;

    public Category(Integer id, String name, String color) {
        this.map = new HashMap<>();
        this.map.put("id", id);
        this.map.put("nombre", name);
        this.map.put("color", color);
    }
    
    public Category(JSONObject json){
        this.map = (HashMap<String, Object>) json.toMap();
    }
    
    public Category(Integer id) throws IOException{
        HttpGet get = new HttpGet(serverApi + "/api/categorias/"+id);
        Response response = httpRequest(get);
        if(response.responseOk()){
            JSONObject json = response
                    .getJsonResponse()
                    .getJSONArray("items")
                    .getJSONObject(0);
            this.map = (HashMap<String, Object>) json.toMap();
        } else {
            showError(response);
            this.map.clear();
        }
    }
    
    public Response upload() throws IOException{
        HttpPost post = new HttpPost(serverApi + "/api/token/categorias/nueva");
        JSONObject json = new JSONObject()
                .put("nombre", this.getName())
                .put("color", this.getColor());
        post.addHeader("Content-Type", "application/json");
        post.setEntity(new StringEntity(json.toString(), "UTF-8"));
        return httpRequest(post, Main.authToken);
    }
    
    public Response update() throws IOException, ParseException{
        HttpPut put = new HttpPut(serverApi + "/api/token/categorias/"+ this.getId());
        JSONObject json = new JSONObject()
                .put("id", this.getId())
                .put("nombre", this.getName())
                .put("color", this.getColor());
        put.addHeader("Content-Type", "application/json");
        put.setEntity(new StringEntity(json.toString(), "UTF-8"));
        return httpRequest(put, Main.authToken);
    }
    
    public Response delete() throws IOException{
        if(showConfirmation("Borrar la categoría "+this.getName())){
            HttpDelete delete = new HttpDelete(serverApi + "/api/token/categorias/"+this.getId());
            return httpRequest(delete, Main.authToken);
        } else {
            JSONObject json = new JSONObject()
                    .put("cancelado", "ok");
            return new Response(json, 304);
        }
    }

    public Integer getId() {
        return (Integer) this.map.get("id");
    }

    public void setId(Integer id) {
        this.map.put("id", id);
    }

    public String getName() {
        return (String) this.map.get("nombre");
    }

    public void setName(String name) {
        this.map.put("nombre", name);
    }

    public String getColor() {
        return (String) this.map.get("color");
    }

    public void setColor(String color) {
        this.map.put("color", color);
    }
    
    @Override
    public String toString() {
        return this.getName();
    }
    
}
