<?php
class Connection{
    public static function createConnection(){
        try {
            $server = $_ENV['SERVER'];
            $dbname = $_ENV['DBNAME'];
            $username = $_ENV['DBUSERNAME'];
            $password = $_ENV['DBPASSWORD'];

            $db=new PDO("mysql:host=localhost; dbname=$dbname", $username,$password);
            $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $db->exec("SET CHARACTER SET utf8");
            return $db;
        } catch (Exception $e) {
            echo $e;
        }        
    }
}
