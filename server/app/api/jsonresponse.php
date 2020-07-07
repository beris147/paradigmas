<?php
use Slim\Psr7\Response;

final class JsonResponse extends Response{
    public function __construct($data = null){
        $body = $data ? json_encode($data, JSON_UNESCAPED_UNICODE | JSON_NUMERIC_CHECK ) : null;
        parent::__construct();
        parent::getBody()->write($body); 
    }

    public static function ok($data): self{
        return self::returnJson(200, new self($data));
    }

    public static function notFound($data): self{
        return self::returnJson(404, new self($data));
    }

    public static function notAllowed($data): self{
        return self::returnJson(405, new self($data));
    }

    public static function serverError($data): self{
        return self::returnJson(500, new self($data));
    }

    public static function badRequest($data): self{
        return self::returnJson(400, new self($data));
    }

    public static function unauthorized($data): self{
        return self::returnJson(401, new self($data));
    }

    public static function returnJson($status, JsonResponse $response): self{
        return $response 
            ->withHeader('Content-Type', 'application/json')
            ->withStatus($status);
    }

    private static function setResponse($label, $data){
        $response = array();
        $response["items"] = array();
        array_push($response["items"], [$label => $data]);
        return $response;
    }

    public static function returnOk($data){
        $response = self::setResponse("value", $data);
        return JsonResponse::ok($response);
    }

    public static function returnBadRequest($error){
        $response = self::setResponse("error", $error);
        return JsonResponse::badRequest($response);
    }

    public static function returnServerError($error){
        $response = self::setResponse("error", $error);
        return JsonResponse::badRequest($response);
    }

    public static function returnNotFound($error){
        $response = self::setResponse("error", $error);
        return JsonResponse::notFound($response);
    }

    public static function returnUnauthorized($error){
        $response = self::setResponse("error", $error);
        return JsonResponse::unauthorized($response);
    }
}