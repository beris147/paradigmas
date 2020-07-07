<?php
class Categoria{
    private $db;

    public function __construct(){
        $this->db = Connection::createConnection();
    }

    public function getAll(){
        try{
            $query = "SELECT * FROM categoria";
			$stmt = $this->db->prepare($query);
			$stmt->execute();
			return $stmt;
		}catch(Exception $e){
			return $e;
		}
    }

    public function getCategoria($id){
        try{
            $query = "SELECT * FROM categoria WHERE id = :id";
			$stmt = $this->db->prepare($query);
			$stmt->execute(array(
                ':id' => $id
            ));
			return $stmt;
		}catch(Exception $e){
			return $e;
		}
    }

}
