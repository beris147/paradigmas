<?php
use Psr\Http\Message\ResponseInterface as Response;
use Psr\Http\Message\ServerRequestInterface as Request;
use Slim\Routing\RouteCollectorProxy;

$app->group('/api', function(RouteCollectorProxy $app){

    $app->get('/categorias', function (Request $request, Response $response, $args) {
        return ApiCategoria::getAll();
    });

    $app->get('/categorias/{id:\d+}', function (Request $request, Response $response, $args) {
        $id = $args["id"];
        return ApiCategoria::get($id);
    });

    $app->group('/token', function(RouteCollectorProxy $app){
        $app->post('/categorias/nueva', function(Request $request, Response $response){
            $data = $request->getParsedBody();
            return ApiCategoria::add($data);
        });

        $app->put('/categorias/{id:\d+}', function(Request $request, Response $response, $args){
            $data = $request->getParsedBody();  
            $id = $args['id'];
            return ApiCategoria::update($data, $id);
        });

        $app->delete('/categorias/{id:\d+}', function(Request $request, Response $response, $args){
            $id = $args['id'];
            return ApiCategoria::delete($id);
        });
    });
});