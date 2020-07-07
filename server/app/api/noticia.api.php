<?php
class ApiNoticia{
    public static function getTotal(){
        $noticia = new Noticia();
        return $noticia->getTotal();
    }

    public static function getAll($start, $limit){
        $noticia = new Noticia();
        return ApiNoticia::getInfo($noticia->getAll($start, $limit));
    }

    public static function getFull($start, $limit){
        $noticia = new Noticia();
        return ApiNoticia::getInfo($noticia->getFull($start, $limit));
    }

    public static function get($id){
        $noticia = new Noticia();
        if($id instanceof Exception){
            return JsonResponse::returnBadRequest($id->getMessage());
        }
        return ApiNoticia::getInfo($noticia->get($id));
    }

    public static function getRelated($id){
        $noticia = new Noticia();
        if($id instanceof Exception){
            return JsonResponse::returnBadRequest($id->getMessage());
        }
        return ApiNoticia::getInfo($noticia->getRelated($id));
    }

    public static function getHidden($id){
        $noticia = new Noticia();
        if($id instanceof Exception){
            return JsonResponse::returnBadRequest($id->getMessage());
        }
        return ApiNoticia::getInfo($noticia->getHidden($id));
    }

    public static function getByCategoria($categoria, $start, $limit){
        $noticia = new Noticia();
        return ApiNoticia::getInfo($noticia->getByCategoria($categoria, $start, $limit));
    }

    private static function getInfo($stmt){
        if($stmt instanceof PDOStatement){
            $num = $stmt->rowCount();
            $response = array();
            $response["items"] = array();
            if($num>0){
                while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
                    extract($row);
		    $min = json_decode(ApiImagen::get($miniatura)->getBody(),true)["items"][0];
                    $item=array(
                        "id" => $id,
                        "categoria" => $categoria,
                        "titulo" => html_entity_decode($titulo),
                        "descripcion" => html_entity_decode($descripcion),
                        "nota" => base64_decode($nota),
                        "principal" => $principal,
                        "fecha" => $fecha,
                        "firma" => html_entity_decode($firma),
                        "mostrar" => $mostrar,
                        "miniatura" => $min,
                        "clicks" => $clicks
                    );
                    array_push($response["items"], $item);
                }
                return JsonResponse::ok($response);
            }else{
                return JsonResponse::returnNotFound("no se encontró ninguna noticia");
            }
        }else{
            return JsonResponse::returnServerError($stmt->getMessage());
        }
    }

    public static function add($data){
        $noticia = new Noticia();
        $id = $noticia->add($data);
        return ApiNoticia::getHidden($id);
    }

    public static function update($data, $id){
        $noticia = new Noticia();
        if($id == $data['id']){
            $stmt = $noticia->update($data);
            if($stmt instanceof PDOStatement){
                return ApiNoticia::getHidden($id);
            }else{
                return JsonResponse::returnServerError($stmt->getMessage());
            }
        } else{
            return JsonResponse::returnBadRequest("id no compatible");
        }
    }

    public static function delete($id, $uploads, $resources){
        $noticia = new Noticia();
        $images = json_decode(ApiImagen::getByNoticia($id)->getBody(), true)["items"];
        foreach($images as $image){
            ApiImagen::delete($image['id'], $uploads);
        }
        $codes = json_decode(ApiAdjunto::getByNoticia($id)->getBody(), true)["items"];
        foreach($codes as $code){
            ApiAdjunto::delete($code['id'], $resources);
        }
        $res = $noticia->delete($id);
        if($res instanceof Exception){
            return JsonResponse::serverError($res->getMessage());
        } else{
            return JsonResponse::returnOk("noticia eliminada");
        }
    } 
}
