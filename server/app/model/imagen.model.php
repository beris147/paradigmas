<?php
class Imagen{
    private $db;

    public function __construct(){
        $this->db = Connection::createConnection();
    }

    public function getAll(){
        try{
            $query = "SELECT * FROM imagen";
			$stmt = $this->db->prepare($query);
			$stmt->execute();
			return $stmt;
		}catch(Exception $e){
			return $e;
		}
    }

    public function get($id){
        try{
            $query = "SELECT * FROM imagen WHERE id = :id";
            $stmt = $this->db->prepare($query);
            $stmt->execute(array(
                ':id' => $id		
            ));
            return $stmt;
        }catch (Exception $e) {
			return $e;
		}
    }

    public function getByNoticia($noticia){
        try{
            $query = "SELECT * FROM imagen WHERE noticia = :noticia";
            $stmt = $this->db->prepare($query);
            $stmt->execute(array(
                ':noticia' => $noticia		
            ));
            return $stmt;
        }catch (Exception $e) {
			return $e;
		}
    }
}
