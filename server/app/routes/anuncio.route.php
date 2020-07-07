<?php
use Psr\Http\Message\ResponseInterface as Response;
use Psr\Http\Message\ServerRequestInterface as Request;
use Slim\Routing\RouteCollectorProxy;

$app->group('/api', function(RouteCollectorProxy $app){

    $app->get('/anuncios', function (Request $request, Response $response, $args) {
        return ApiAnuncio::getAll();
    });

    $app->get('/anuncios/{id}', function (Request $request, Response $response, $args) {
        $id = $args['id'];
        return ApiAnuncio::get($id);
    });

    $app->group('/token', function(RouteCollectorProxy $app){
        $app->post('/anuncios/nuevo', function(Request $request, Response $response){
	    $data = json_decode($request->getParsedBody()["json"], true);
            $directory = $this->get('upload_directory');
            $uploadedFile = $request->getUploadedFiles()['my_file'];
            return ApiAnuncio::add($data, $uploadedFile, $directory);
        });

        $app->put('/anuncios/{id:\d+}', function(Request $request, Response $response, $args){
            $id = $args['id'];
            $data = $request->getParsedBody();
            return ApiAnuncio::update($data, $id);
        });

        $app->delete('/anuncios/{id:\d+}', function(Request $request, Response $response, $args){
            $id = $args['id'];
	    $directory = $this->get('upload_directory');
            return ApiAnuncio::delete($id, $directory);
        });
    });
});
