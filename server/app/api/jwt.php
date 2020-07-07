<?php

final class JWT{
    private $jwt;

    public function __construct($exp, $secret){
	$header = json_encode(['typ' => 'JWT', 'alg' => 'HS256']);
        $timestamp = strtotime($exp);
        $payload = json_encode(['exp' => $timestamp]);
        $base64UrlHeader = str_replace(['+', '/', '='], ['-', '_', ''], base64_encode($header));
        $base64UrlPayload = str_replace(['+', '/', '='], ['-', '_', ''], base64_encode($payload));
	$signature = hash_hmac('sha256', $base64UrlHeader . "." . $base64UrlPayload, $secret, true);
        $base64UrlSignature = str_replace(['+', '/', '='], ['-', '_', ''], base64_encode($signature));
        $this->jwt = $base64UrlHeader . "." . $base64UrlPayload . "." . $base64UrlSignature;
    }

    public function getToken() {
        return $this->jwt;
    }
}
