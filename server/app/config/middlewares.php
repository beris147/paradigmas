<?php
$app->addBodyParsingMiddleware();
$app->addRoutingMiddleware();

//SECRET

//TOKEN AUTH
$authMiddleware = new Tuupola\Middleware\JwtAuthentication([
    "secret" =>$_ENV['SECRET'],
    "path" => $PATH."/api/token", 
    "error" => function ($response, $arguments) {
        return JsonResponse::returnUnauthorized($arguments["message"]);
    }
]);


//TOKEN refresh
$refreshMiddleware = new Tuupola\Middleware\JwtAuthentication([
    "secret" => $_ENV['REFRESHSECRET'],
    "path" => $PATH."/api/refresh",
    "error" => function ($response, $arguments) {
        return JsonResponse::returnUnauthorized($arguments["message"]);
    }
]);

//TOKEN LOGIN
$userMiddleware = new Tuupola\Middleware\JwtAuthentication([
    "secret" => $_ENV['SECRET2'],
    "path" => $PATH."/api/usuario",
    "error" => function ($response, $arguments) {
        return JsonResponse::returnUnauthorized($arguments["message"]);
    }
]);


$app->add($authMiddleware);
$app->add($userMiddleware);
$app->add($refreshMiddleware);
