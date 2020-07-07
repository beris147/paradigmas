<?php
class Noticia{
    private $db;

    public function __construct(){
        $this->db = Connection::createConnection();
    }

    public function getTotal(){
        try{
            $query = "SELECT COUNT(id) AS total FROM noticia";
            $stmt = $this->db->prepare($query);
            $stmt->execute();
            return JsonResponse::returnOk($stmt->fetch()['total']);
        }catch(Exception $e){
            return JsonResponse::returnServerError($e->getMessage());
        }
    }

    public function getAll($start, $limit){
        try{
            $query = "SELECT * FROM noticia WHERE mostrar = 1  ORDER BY fecha DESC LIMIT :start,:limit";
            $stmt = $this->db->prepare($query);
            $stmt->bindValue(':start', intval($start), PDO::PARAM_INT);
            $stmt->bindValue(':limit', intval($limit), PDO::PARAM_INT);
			$stmt->execute();
			return $stmt;
		}catch(Exception $e){
			return $e;
		}
    }

    public function getFull($start, $limit){
        try{
            $query = "SELECT * FROM noticia ORDER BY fecha DESC LIMIT :start,:limit";
            $stmt = $this->db->prepare($query);
            $stmt->bindValue(':start', intval($start), PDO::PARAM_INT);
            $stmt->bindValue(':limit', intval($limit), PDO::PARAM_INT);
			$stmt->execute();
			return $stmt;
		}catch(Exception $e){
			return $e;
		}
    }

    public function get($id){
        try{
            $query = "SELECT * FROM noticia WHERE id = :id AND mostrar = 1";
            $stmt = $this->db->prepare($query);
            $stmt->execute(array(
                ':id' => $id
            ));
            return $stmt;
        }catch(Exception $e){
            return $e;
        }
    }

    public function getRelated($id){
        try{
            $query = "SELECT id,categoria, titulo, descripcion, nota, principal, fecha, firma, mostrar, miniatura, clicks FROM
(SELECT *, MATCH(titulo) AGAINST((SELECT titulo FROM noticia WHERE id = :id)) AS score
 FROM noticia
 WHERE id != :id
 AND categoria != 1
 ANd mostrar = 1
 AND MATCH(titulo) AGAINST((SELECT titulo FROM noticia WHERE id = :id))
 ORDER BY score DESC) AS titulos
 
 UNION
 
 SELECT id,categoria, titulo, descripcion, nota, principal, fecha, firma, mostrar, miniatura, clicks FROM
(SELECT *, MATCH(descripcion) AGAINST((SELECT descripcion FROM noticia WHERE id = :id)) AS score
 FROM noticia
 WHERE id != :id
 AND categoria != 1
 AND mostrar = 1
 AND MATCH(descripcion) AGAINST((SELECT descripcion FROM noticia WHERE id = :id))
 ORDER BY score DESC) AS descripciones
 
 LIMIT 15";
            $stmt = $this->db->prepare($query);
            $stmt->execute(array(
                ':id' => $id
            ));
            return $stmt;
        }catch(Exception $e){
            return $e;
        }
    }

    public function getHidden($id){
        try{
            $query = "SELECT * FROM noticia WHERE id = :id";
            $stmt = $this->db->prepare($query);
            $stmt->execute(array(
                ':id' => $id
            ));
            return $stmt;
        }catch(Exception $e){
            return $e;
        }
    }

    public function delete($id){
        try{
            $query = "DELETE FROM noticia WHERE id = :id";
            $stmt = $this->db->prepare($query);
            $stmt->execute(array(
                ':id' => $id
            ));
            return true;
        }catch(Exception $e){
            return $e;
        }
    }
}
