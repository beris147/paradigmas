<?php
class ApiCategoria{
    public static function getAll(){
        $categoria = new Categoria();
        return ApiCategoria::getInfo($categoria->getAll());
    }

    public static function get($id){
        $categoria = new Categoria();
        return ApiCategoria::getInfo($categoria->getCategoria($id));
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
                        "nombre" => html_entity_decode($nombre),
                        "color" => html_entity_decode($color)
                    );
                    array_push($response["items"], $item);
                }
                return JsonResponse::ok($response);
            }else{
                return JsonResponse::returnNotFound("no se encontró ninguna categoría");
            }
        }else{
            return JsonResponse::returnServerError($stmt->getMessage());
        }
    }

    public static function add($data){
        $categoria = new Categoria();
        $id = $categoria->add($data);
        if($id instanceof PDOException){
            return JsonResponse::returnServerError($id->getMessage());
        } else{
            return ApiCategoria::get($id);
        }
    }

    public static function update($data, $id){
        $categoria = new Categoria();
        if($id == $data['id']){
            $stmt = $categoria->update($data);
            if($stmt instanceof PDOStatement){
                return ApiCategoria::get($id);
            }else{
                return JsonResponse::returnServerError($stmt->getMessage());
            }
        } else{
            return JsonResponse::returnBadRequest("id no compatible");
        }
    }

    public static function delete($id){
        $categoria = new Categoria();
        $res = $categoria->delete($id);
        if($res instanceof Exception){
            return JsonResponse::serverError($res->getMessage());
        } else{
            return JsonResponse::returnOk("categoria eliminada");
        }
    }
}
