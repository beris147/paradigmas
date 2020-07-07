<?php
class ApiAdjunto{
    public static function getAll(){
        $adjunto = new Adjunto();
        return ApiAdjunto::getInfo($adjunto->getAll());
    }

    public static function get($id){
        $adjunto = new Adjunto();
        return ApiAdjunto::getInfo($adjunto->get($id));
    }

    public static function getByNoticia($noticia){
        $adjunto = new Adjunto();
        return ApiAdjunto::getInfo($adjunto->getAdjuntoByNoticia($noticia));
    }

    private static function getInfo($stmt){
        if($stmt instanceof PDOStatement){
            $num = $stmt->rowCount();
            $response = array();
            $response["items"] = array();
            if($num>0){
                while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
                    extract($row);
                    $item=array(
                        "nombre" => html_entity_decode($nombre),
                        "id" => $id,
                        "html" => base64_decode($html),
                        "vue" => base64_decode($vue),
                        "noticia" => $noticia
                    );
                    array_push($response["items"], $item);  
                }
                return JsonResponse::ok($response);
            }else{
                return JsonResponse::returnNotFound("no se encontró ningun link");
            }
        }else{
            return JsonResponse::returnServerError($stmt->getMessage());
        }
    }

    public static function add($data, $resources){
        $adjunto = new Adjunto();
        $id = $adjunto->add($data);
        if($id instanceof Exception){
            return JsonResponse::returnServerError($id->getMessage());
        } else{
            UploadImageController::uploadCodePreview($data, $id, $resources);
            return ApiAdjunto::get($id);          
        }
        
    }

    public static function update($data, $id){
        $adjunto = new Adjunto();
        if($id == $data['id']){
            $stmt = $adjunto->update($data);
            if($stmt instanceof PDOStatement){
                return ApiAdjunto::get($id);
            }else{
                return JsonResponse::returnServerError($stmt->getMessage());
            }
        } else{
            return JsonResponse::returnBadRequest("id no compatible");
        }
    }

    public static function delete($id, $resources){
        $adjunto = new Adjunto();
        $res = $adjunto->delete($id);
        if($res instanceof Exception){
            return JsonResponse::serverError($res->getMessage());
        } else{
            UploadImageController::delete($id.'.jpg', $resources.'/generated');
            return JsonResponse::returnOk("adjunto eliminado");
        }
    }
}
