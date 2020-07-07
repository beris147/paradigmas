<?php
class ApiAnuncio{
    public static function getAll(){
        $anuncio = new Anuncio();
        return ApiAnuncio::getInfo($anuncio->getAll());
    }

    public static function get($id){
        $anuncio = new Anuncio();
        return ApiAnuncio::getInfo($anuncio->get($id));
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
                        "id" => $id,
                        "imagen" => $imagen,
                        "titulo" => $titulo,
                        "texto" => $texto,
                        "href" => $href
                    );
                    array_push($response["items"], $item);
                }
                return JsonResponse::ok($response);
            }else{
                return JsonResponse::returnNotFound("no se encontró ningun anuncio");
            }
        }else{
            return JsonResponse::returnServerError($stmt->getMessage());
        }
    }

    public static function add($data, $uploadedFile, $directory){
        $anuncio = new Anuncio();
        $imagen = UploadImageController::upload($uploadedFile, $directory); //intenta subir la imagen al server
        if($imagen instanceof JsonResponse){ //Si se regresa una respuesta json es que hubo algún error al intentar subir la imagen
            return $imagen;
        }
        $data['imagen'] = $imagen;
        $id = $anuncio->add($data); //se añade el anuncio a la base de datos
        if($id instanceof PDOException){ //error en la peticion sql
            return JsonResponse::returnServerError($id->getMessage());
        } else {
            return ApiAnuncio::get($id);//se regresa toda la informacion del anuncio, exito al subir
        }

    }

    public static function update($data, $id){
        $anuncio = new Anuncio();
        if($id == $data['id']){
            /*if($uploadedFile){ //Si viene una imagen nueva, hay que borrar la anterior
                $stmt = $anuncio->get($id); //obtengo el anucio a borrar para quitarle la imagen
                if($stmt instanceof Exception){
                    return JsonResponse::serverError($stmt->getMessage()); //internal error
                } else {
                //se borra la imagen del anuncio
                    if($stmt instanceof PDOStatement){
                        $num = $stmt->rowCount();
                        if($num>0){
                            while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
                                extract($row);
                                UploadImageController::delete($imagen, $directory);
                            }
                        }
                    }
                }
                //apartir de aqui se actualiza, despues de haber borrado la imagen
                $imagen = UploadImageController::upload($uploadedFile, $directory); //intenta subir la imagen>
                if($imagen instanceof JsonResponse){ //Si se regresa una respuesta json es que hubo algún err>
                    return $imagen; //error
                }
                $data['imagen'] = $imagen; //la nueva imagen
            }*/
            //Actualizar
            $stmt = $anuncio->update($data);
            if($stmt instanceof PDOStatement){
                return ApiAnuncio::get($id); //exito, se regresa la noticia actualizada
            }else{
                return JsonResponse::returnServerError($stmt->getMessage());
            }
        } else{
            return JsonResponse::returnBadRequest("id no compatible, ".$id ."vs".$data);
        }
    }

    public static function delete($id, $directory){
        $anuncio = new Anuncio();
        $stmt = $anuncio->delete($id);
        if($stmt instanceof Exception){
            return JsonResponse::serverError($stmt->getMessage());
        } else{
            if($stmt instanceof PDOStatement){
                $num = $stmt->rowCount();
                if($num>0){
                    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
                        extract($row);
                        $response = UploadImageController::delete($imagen, $directory);
                    }
                    return JsonResponse::returnOk("anuncio eliminado.");
                } else {
                    return JsonResponse::returnNotFound("no se encontró ningun anuncio");
                }
            } else {
                return JsonResponse::returnServerError($stmt->getMessage());
            }
        }
    }
}
