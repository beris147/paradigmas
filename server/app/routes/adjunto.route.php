<?php
use Psr\Http\Message\ResponseInterface as Response;
use Psr\Http\Message\ServerRequestInterface as Request;
use Slim\Routing\RouteCollectorProxy;

$app->group('/api', function(RouteCollectorProxy $app){

    $app->get('/adjuntos', function (Request $request, Response $response, $args) {
        return ApiAdjunto::getAll();
    });

    $app->get('/adjuntos/noticia/{noticia:\d+}', function (Request $request, Response $response, $args) {
        $noticia = $args['noticia'];
        return ApiAdjunto::getByNoticia($noticia);
    });

    $app->get('/adjuntos/{id:\d+}', function (Request $request, Response $response, $args) {
        $id = $args['id'];
        return ApiAdjunto::get($id);
    });

    $app->group('/token', function(RouteCollectorProxy $app){
        $app->post('/adjuntos/nuevo', function(Request $request, Response $response){
            $data = $request->getParsedBody();
            $resources = $this->get('resources_directory');
            return ApiAdjunto::add($data,$resources);
        });

        $app->put('/adjuntos/{id:\d+}', function(Request $request, Response $response, $args){
            $data = $request->getParsedBody();  
            $id = $args['id'];
            return ApiAdjunto::update($data, $id);
        });

        $app->delete('/adjuntos/{id:\d+}', function(Request $request, Response $response, $args){
            $id = $args['id'];
            $resources = $this->get('resources_directory');
            return ApiAdjunto::delete($id,$resources);
        });
    });
});