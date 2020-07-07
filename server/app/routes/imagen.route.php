<?php
use Psr\Http\Message\ResponseInterface as Response;
use Psr\Http\Message\ServerRequestInterface as Request;
use Slim\Routing\RouteCollectorProxy;

$app->group('/api', function(RouteCollectorProxy $app){
    $app->get('/imagenes', function (Request $request, Response $response, $args) {
        return ApiImagen::getAll();
    });

    $app->get('/imagenes/noticia/{id}', function (Request $request, Response $response, $args) {
        $id = $args['id'];
        return ApiImagen::getByNoticia($id);
    });

    $app->get('/imagenes/{id}', function (Request $request, Response $response, $args) {
        $id = $args['id'];
        return ApiImagen::get($id);
    });

    $app->group('/token', function(RouteCollectorProxy $app){
        $app->post('/imagenes/nueva', function(Request $request, Response $response){
            $data = json_decode($request->getParsedBody()["json"], true);
            $directory = $this->get('upload_directory');
            $uploadedFile = $request->getUploadedFiles()['my_file'];
            return ApiImagen::add($data, $uploadedFile, $directory);
        });

        $app->put('/imagenes/{id:\d+}', function(Request $request, Response $response, $args){
            $data = $request->getParsedBody();  
            $id = $args['id'];
            return ApiImagen::update($data, $id);
        });

        $app->delete('/imagenes/{id:\d+}', function(Request $request, Response $response, $args){
            $id = $args['id'];
            $directory = $this->get('upload_directory');
            return ApiImagen::delete($id, $directory);
        });
    });
});
