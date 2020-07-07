package chisme.nolike.classes;

import java.io.IOException;
import java.util.HashMap;
import org.json.JSONObject;

/**
 *
 * @author beris
 */
public abstract class Attached {
    protected HashMap<String,Object> map;
    
    public Attached(JSONObject json){
        map = (HashMap<String, Object>) json.toMap();
    }
    
    public Attached(Integer id) throws IOException{
        map.put("id", id);
    }

    final public Integer getId() { return (Integer) map.get("id"); }
    final public void setId(Integer id) { map.put("id", id); }
    
    @Override
    final public String toString(){
        return this.getName();
    }

    abstract public Response upload() throws IOException;
    abstract public Response delete() throws IOException;
    abstract public String getFinalCode();
    abstract public String getPreviewCode();
    abstract public String getName();
}
