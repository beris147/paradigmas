<?php
use Psr\Http\Message\ResponseInterface as Response;
use Psr\Http\Message\ServerRequestInterface as Request;
use Slim\Routing\RouteCollectorProxy;

$app->group('/api', function(RouteCollectorProxy $app){
    $app->get('/noticias/total', function (Request $request, Response $response, $args) {
        return ApiNoticia::getTotal();
    });

    $app->get('/noticias[/pagina={page:\d+}[/limite={limit:\d+}]]', function (Request $request, Response $response, $args) {
        $page = ($args['page'] && $args['page'] >= 1) ? $args['page'] : 1;
        $limit = ($args['limit']) ? $args['limit'] : 10;
        $start = ($page > 1)? $page*$limit - $limit : 0;
        return ApiNoticia::getAll($start, $limit);
    });

    $app->get('/noticia/{id:\d+}', function (Request $request, Response $response, $args) {
        $id = $args['id'];
        return ApiNoticia::get($id);
    });

    $app->get('/noticia/related/{id:\d+}', function (Request $request, Response $response, $args) {
        $id = $args['id'];
        return ApiNoticia::getRelated($id);
    });

    $app->get('/noticias/categoria/{id:\d+}[/pagina={page:\d+}[/limite={limit:\d+}]]', function (Request $request, Response $response, $args) {
        $id = $args['id'];
        $page = ($args['page'] && $args['page'] >= 1) ? $args['page'] : 1;
        $limit = ($args['limit']) ? $args['limit'] : 10;
        $start = ($page > 1)? $page*$limit - $limit : 0;
        return ApiNoticia::getByCategoria($id, $start, $limit);
    });

    $app->group('/token', function(RouteCollectorProxy $app){
        $app->get('/noticias[/pagina={page:\d+}[/limite={limit:\d+}]]', function (Request $request, Response $response, $args) {
            $page = ($args['page'] && $args['page'] >= 1) ? $args['page'] : 1;
            $limit = ($args['limit']) ? $args['limit'] : 10;
            $start = ($page > 1)? $page*$limit - $limit : 0;
            return ApiNoticia::getFull($start, $limit);
        });

        $app->get('/noticia/{id:\d+}', function (Request $request, Response $response, $args) {
            $id = $args['id'];
            return ApiNoticia::getHidden($id);
        });

        $app->post('/noticias/nueva', function(Request $request, Response $response){
            $data = $request->getParsedBody();
            return ApiNoticia::add($data);
        });

        $app->put('/noticias/{id:\d+}', function(Request $request, Response $response, $args){
            $data = $request->getParsedBody();  
            $id = $args['id'];
            //return ApiNoticia::getHidden($id);
            return ApiNoticia::update($data, $id);
        });

        $app->delete('/noticias/{id:\d+}', function(Request $request, Response $response, $args){
            $id = $args['id'];
            $uploads = $this->get('upload_directory');
            $resources = $this->get('resources_directory');
            return ApiNoticia::delete($id, $uploads, $resources);
        });
    });
});

