package chisme.nolike.classes;

import chisme.nolike.App;
import chisme.nolike.Main;
import static chisme.nolike.Main.serverApi;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import java.util.Base64;
import java.util.Date;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.color;
import javafx.scene.web.HTMLEditor;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.client.methods.HttpGet;

/**
 *
 * @author beris
 */
public class Functions {

    public static Response httpRequest(HttpUriRequest request) throws IOException {
        return httpRequestAux(request);
    }
    
    public static Response httpRequest(HttpUriRequest request, String token) throws IOException {
        try {  //Check if token expired 
            DecodedJWT jwt = JWT.decode(token);
            if(tokenIsExpired(jwt)){
                if(Main.authToken.equals(token)){ //Lets try to refresh the token
                    HttpGet get = new HttpGet(serverApi + "/api/refresh");
                    Response response = httpRequest(get, Main.refreshToken);
                    if (response.responseOk()) {
                        JSONObject json = response
                                .getJsonResponse()
                                .getJSONArray("items")
                                .getJSONObject(0);
                        Main.authToken = json.getString("jwt"); //Assing the token for future use
                        token = Main.authToken; //current token is the authToken
                    } else {
                        App.setRoot("login", 300, 600);//needs login, this should never happend
                    }
                } else {
                    App.setRoot("login", 300, 600);//needs login, this should never happend
                }
            }
        } catch (JWTDecodeException exception){
            Dialog.showUnexpectedError(exception); //Invalid token this should never happen
        }
        //here the token should not be expired 
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        return httpRequestAux(request);
    }
    
    public static void setLoginToken() {
        try {
            Algorithm algorithm = Algorithm.HMAC256("Mkx1bXZZWnVZQ011TFVKa2JPSW0wYlY5U0pBMU5ERnFEekNLY2lnaVNmN2xIajdUam5DbTlNOHY5MWtkQzVa");
            Main.loginToken = JWT.create()
                    .withIssuer("auth0")
                    .withExpiresAt(DateUtils.addMinutes(new Date(), 15))
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
        }
    }

    public static Response httpRequestAux(HttpUriRequest request){
        try ( CloseableHttpClient httpClient = HttpClients.createDefault();  CloseableHttpResponse response = httpClient.execute(request)) {
            String jsonString = EntityUtils.toString(response.getEntity());
            JSONObject json = new JSONObject(jsonString);
            return new Response(json, response.getStatusLine());
        } catch (Exception e) {
            JSONObject json = new JSONObject().put("error", e);
            return new Response(json, 500);
        }
    }
    
    public static Date getDateFromDatePicker(DatePicker dateField){
        LocalDate localDate = dateField.getValue();
        if(localDate == null) return null;
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        Date date = Date.from(instant);
        return date;
    }
    
    public static Boolean tokenIsExpired(DecodedJWT jwt){
        return jwt.getExpiresAt().compareTo(new Date()) <= 0;
    }
    
    public static String getCodeForProduction(String mycode){
        return mycode
                .replace("font-family: &quot;&quot;;","")
                .replace("<p>", "<p class=\"texto px-sm-5 py-sm-2 px-3 py-1\">")
                .replace("\n", "")
                .replace("</font>", "")
                .replace("<font face=\"Lucida Sans\">", "")
                .replaceAll(" style=\"\\s{0,}\"", "")
                .replaceAll(">\\s{0,}", ">");
    }
    
    public static LocalDate dateToLocalDate(Date dateToConvert) {
        return dateToConvert.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate();
    }

    public static String encodeString(String s) throws UnsupportedEncodingException {
        return Base64.getEncoder().encodeToString(s.getBytes("UTF-8"));
    }

    public static String decodeString(String s) {
        try{
            return new String(Base64.getDecoder().decode(s.getBytes("UTF-8")), "UTF-8");
        } catch (Exception e){
            return s;
        } 
    }

    public static String getDifference(String before, String after) {
        int a = 0, b = 0, lastPossible = 0;
        while (after.charAt(a) == before.charAt(b)) {
            if (after.charAt(a) == '<') {
                lastPossible = a;
            }
            a++;
            b++;
        }
        String diff = after.substring(lastPossible);
        a = diff.length() - 1;
        b = before.length() - 1;
        while (a >= 0 && b >= 0 && diff.charAt(a) == before.charAt(b)) {
            if (diff.charAt(a) == '<') {
                lastPossible = a;
            }
            a--;
            b--;
        }
        return diff.substring(0, lastPossible);
    }
    
    public static String hex(int i) {
        return Integer.toHexString(i);
    }

    public static String escapeJavaStyleString(String str,
            boolean escapeSingleQuote, boolean escapeForwardSlash) {
        StringBuilder out = new StringBuilder("");
        if (str == null) {
            return null;
        }
        int sz;
        sz = str.length();
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);
            if (ch > 0xfff) {
                out.append("\\u").append(hex(ch));
            } else if (ch > 0xff) {
                out.append("\\u0").append(hex(ch));
            } else if (ch > 0x7f) {
                out.append("\\u00").append(hex(ch));
            } else if (ch < 32) {
                switch (ch) {
                    case '\b':
                        out.append('\\');
                        out.append('b');
                        break;
                    case '\n':
                        out.append('\\');
                        out.append('n');
                        break;
                    case '\t':
                        out.append('\\');
                        out.append('t');
                        break;
                    case '\f':
                        out.append('\\');
                        out.append('f');
                        break;
                    case '\r':
                        out.append('\\');
                        out.append('r');
                        break;
                    default:
                        if (ch > 0xf) {
                            out.append("\\u00").append(hex(ch));
                        } else {
                            out.append("\\u000").append(hex(ch));
                        }
                        break;
                }
            } else {
                switch (ch) {
                    case '\'':
                        if (escapeSingleQuote) {
                            out.append('\\');
                        }
                        out.append('\'');
                        break;
                    case '"':
                        out.append('\\');
                        out.append('"');
                        break;
                    case '\\':
                        out.append('\\');
                        out.append('\\');
                        break;
                    case '/':
                        if (escapeForwardSlash) {
                            out.append('\\');
                        }
                        out.append('/');
                        break;
                    default:
                        out.append(ch);
                        break;
                }
            }
        }
        return out.toString();
    }
    
    public static String getStringBewteen(String source, String key, String startsAfter, String endsBefore){
        int srcStart = source.indexOf(key);
        int srcEnd = source.indexOf(startsAfter, srcStart);
        int linkStart = srcEnd + 1;
        String linkAux = source.substring(linkStart);
        int linkEnd = linkAux.indexOf(endsBefore);
        String link = linkAux.substring(0,linkEnd);
        return link;
    }
    
    public static void printChildren(HTMLEditor he, int MAXDEPTH)
      {
          System.out.println("Print Children ==========>>>>");
          String[] hieraArray = new String[MAXDEPTH]; 
          int maxDepth = 0;
          int lastDepth = 0;
          Node parent;

            /* List all elements of the HTMLeditor */
            for (Node element: (he.lookupAll("*")))
            {
                parent = element.getParent();
                if (maxDepth == 0) 
                {
                    hieraArray[0] = element.getClass().getSimpleName().toString();
                    System.out.print(hieraArray[0]);
                    System.out.println("");
                    maxDepth = 1;                   
                }
                else 
                {
                    int i = 0, i2 = 0;
                    boolean found = false;
                    for(i=maxDepth; i>=0; i--)
                    {
                        if (hieraArray[i] == null || parent.getClass().getSimpleName() == null) continue;
                        if (hieraArray[i].equals(parent.getClass().getSimpleName()))
                        {
                            for (i2 = 0; i2 <= i; i2++)
                            {
                                System.out.print("|");
                            }   
                            if ((Math.abs(lastDepth-i2)) > 2) System.out.print("->" + element.getClass().getSimpleName() + " {p: " + parent.getClass().getSimpleName() + "}");
                            else System.out.print("->" + element.getClass().getSimpleName());
                            //if (element.getClass().getSimpleName().equals("PopupButton"))  System.out.print(" ??: " + element + " ::: " + element.getClass());
                            lastDepth = i2;

                            hieraArray[(i+1)] = element.getClass().getSimpleName();
                            if (maxDepth < (i+1)) maxDepth = (i+1);
                            found = true;
                            System.out.println("");
                            break;
                        }
                    }
                    if (found == false) 
                    {
                        hieraArray[(i+1)] = parent.getClass().getSimpleName();
                        if (maxDepth < (i+1)) maxDepth = (i+1);
                    }
                    if ((maxDepth+1) >= MAXDEPTH) 
                    {
                        System.out.println("MAXDEPTH reached! increase ArraySize!");
                        return;
                    }
                }
            }
      }

    
    public static void modifyClipboard() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        String plainText = clipboard.getString();
        ClipboardContent content = new ClipboardContent();
        content.putString(plainText);
        clipboard.setContent(content);
    }
    
    public static String getHexColor(Color c){
        int green = (int) (c.getGreen() * 255);
        String greenString = Integer.toHexString(green);
        int red = (int) (c.getRed() * 255);
        String redString = Integer.toHexString(red);
        int blue = (int) (c.getBlue() * 255);
        String blueString = Integer.toHexString(blue);
        
        redString = (redString.length() > 1)? redString : "0"+redString;
        greenString = (greenString.length() > 1)? greenString : "0"+greenString;
        blueString = (blueString.length() > 1)? blueString : "0"+blueString;
        
        String hexColor = redString + greenString + blueString;
        
        return hexColor;
    }
    
    public static Color hex2Rgb(String colorStr) {
        return color(
                (Integer.valueOf( colorStr.substring( 1-1, 3-1 ), 16)/255.0 ),
                (Integer.valueOf( colorStr.substring( 3-1, 5-1 ), 16)/255.0 ),
                (Integer.valueOf( colorStr.substring( 5-1, 7-1 ), 16)/255.0 ) 
        );
    }
    
    public static void moveFromTo(HTMLEditor he, String t, int c, String t2, int c2){
        Node nCb = new Button();
        int i = 0;
        switch (t) {
             case "Separator":
                 for (Node candidate : (he.lookupAll("Separator"))) {
                     if (candidate instanceof Separator) {
                         Separator cb = (Separator) candidate;
                         if (i == c) {
                            nCb = cb;
                            break;
                        }
                    }
                    i++;
                  }
                  break;
              case "ComboBox":
                  for (Node candidate: (he.lookupAll("ComboBox"))) 
                  {
                    if (candidate instanceof ComboBox)
                    {
                        ComboBox cb = (ComboBox) candidate;
                        if (i == c)
                        {
                            nCb = cb;
                            break;
                        }
                    }
                    i++;
                  }
                  break;    
              case "ToggleButton":
                  for (Node candidate: (he.lookupAll("ToggleButton"))) 
                  {
                    if (candidate instanceof ToggleButton)
                    {
                        ToggleButton cb = (ToggleButton) candidate;
                        if (i == c)
                        {
                            nCb = cb;
                            break;
                        }
                    }
                    i++;
                }
                break;
            case "Button":
                for (Node candidate : (he.lookupAll("Button"))) {
                    if (candidate instanceof Button) {
                        Button cb = (Button) candidate;
                        if (i == c) {
                            nCb = cb;
                            break;
                        }
                    }
                    i++;
                }
                break;    
          }
          //Copy To:
          i = 0;
          switch(t2)
          {
              case "ToolBar":
                  for (Node candidate: (he.lookupAll("ToolBar"))) 
                  {
                    if (candidate instanceof ToolBar)
                    {
                        ToolBar cb2 = (ToolBar) candidate;
                        if (i == c2)
                        {
                            cb2.getItems().add(nCb);
                            break;
                        }
                    }
                    i++;
                  }
                  break;    
          }
      }

      public static void removeFrom(HTMLEditor he, String t, int c)
      {
          int i = 0;

          switch(t)
          {
          case "ToolBar":
              for (Node candidate: (he.lookupAll("ToolBar"))) 
              {
                if (candidate instanceof ToolBar)
                {
                    ToolBar cb = (ToolBar) candidate;
                    if (i == c)
                    {
                        cb.setVisible(false);
                        cb.setManaged(false);
                        //((Pane)cb.getParent()).getChildren().remove(cb);
                        //Node nCb = cb;
                        //((Pane) nCb.getParent()).getChildren().remove(nCb);
                        break;
                    }
                }
                i++;
              }
              break;
          case "PopupButton":
              for (Node candidate: (he.lookupAll("PopupButton"))) 
              {
                    if (i == c)
                    {
                        Node nCb = candidate;
                        nCb.setVisible(false); nCb.setManaged(false);
                        break;
                    }
                    i++;
              }
              break;
          case "ToggleButton":
              for (Node candidate: (he.lookupAll("ToggleButton"))) 
              {
                if (candidate instanceof ToggleButton)
                {
                    ToggleButton cb = (ToggleButton) candidate;
                    if (i == c)
                    {
                        Node nCb = cb;
                        nCb.setVisible(false); nCb.setManaged(false);
                        break;
                    }
                }
                i++;
              }
              break;
          case "Separator":
              for (Node candidate: (he.lookupAll("Separator"))) 
              {
                if (candidate instanceof Separator)
                {
                    Separator cb = (Separator) candidate;
                    if (i == c)
                    {
                        Node nCb = cb;
                        nCb.setVisible(false); nCb.setManaged(false);
                        break;
                    }
                }
                i++;
              }
              break;
          case "Button":
              for (Node candidate: (he.lookupAll("Button"))) 
              {
                if (candidate instanceof Button)
                {
                    Button cb = (Button) candidate;
                    if (i == c)
                    {
                        Node nCb = cb;
                        nCb.setVisible(false); nCb.setManaged(false);
                        break;
                    }
                }
                i++;
              }
              break;
          case "ComboBox":
              for (Node candidate: (he.lookupAll("ComboBox"))) 
              {
                if (candidate instanceof ComboBox)
                {
                    ComboBox cb = (ComboBox) candidate;
                    if (i == c)
                    {
                        Node nCb = cb;
                        nCb.setVisible(false); nCb.setManaged(false);
                        break;
                    }
                }
                i++;
              }
              break;
          }   
      }
      
}
