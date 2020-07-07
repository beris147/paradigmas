<?php
class ApiImagen{
    public static function getAll(){
        $imagen = new Imagen();
        return ApiImagen::getInfo($imagen->getAll());
    }

    public static function getByNoticia($noticia){
        $imagen = new Imagen();
        return ApiImagen::getInfo($imagen->getByNoticia($noticia));
    }

    public static function get($id){
        $imagen = new Imagen();
        return ApiImagen::getInfo($imagen->get($id));
    }

    private static function getInfo($stmt){
        if($stmt instanceof PDOStatement){
            $response = array();
            $response["items"] = array();
            $num = $stmt->rowCount();
            if($num>0){
                while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
                    extract($row);
                    $item=array(
                        "id" => $id,
                        "src" => html_entity_decode($src),
                        "alt" => html_entity_decode($alt),
                        "noticia" => $noticia
                    );
                    array_push($response["items"], $item);
                }
                return JsonResponse::ok($response);
            }else{
                return JsonResponse::returnNotFound("no se encontró ninguna imagen");
            }
        }else{
            return JsonResponse::returnServerError($stmt->getMessage());
        }
    }

    public static function add($data, $uploadedFile, $directory){
        $imagen = new Imagen();
        $src = UploadImageController::upload($uploadedFile, $directory); //intenta subir la imagen al server
        if($src instanceof JsonResponse){ //Si se regresa una respuesta json es que hubo algún error
            return $src;
        }
        $data['src'] = $src; 
        $id = $imagen->add($data); //se añade la imagen a la base de datos
        if($id instanceof PDOException){
            return JsonResponse::returnServerError($id->getMessage());
        } else{
            return ApiImagen::get($id); //error        
        }
    }

    public static function update($data, $id){
        $imagen = new Imagen();
        if($id == $data['id']){
            $stmt = $imagen->update($data);
            if($stmt instanceof PDOStatement){
                return ApiImagen::get($id);
            }else{
                return JsonResponse::returnServerError($stmt->getMessage());
            }
        } else{
            return JsonResponse::returnBadRequest("id no compatible");
        }
    }

    public static function delete($id, $directory){
        $imagen = new Imagen();
        $stmt = $imagen->delete($id);
        if($stmt instanceof Exception){
            return JsonResponse::serverError($stmt->getMessage());
        } else{
            if($stmt instanceof PDOStatement){
                $num = $stmt->rowCount();
                if($num>0){
                    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
                        extract($row);
                        return UploadImageController::delete($src, $directory);
                    }
                }else{
                    return JsonResponse::returnNotFound("no se encontró ninguna imagen");
                }
            }else{
                return JsonResponse::returnServerError($stmt->getMessage());
            }
        }
    }
}
