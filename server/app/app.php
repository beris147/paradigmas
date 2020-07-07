<?php
use DI\Container;
use Psr\Http\Message\ServerRequestInterface;
use Slim\Factory\AppFactory;
use Slim\Psr7\Response;

require $location.'vendor/autoload.php';

//ENVIRONMENT
$dotenv = Dotenv\Dotenv::createImmutable(__DIR__);
$dotenv->load();

//URL PATH
$PATH = implode('/', array_slice(explode('/', $_SERVER['SCRIPT_NAME']), 0, -1));

AppFactory::setContainer($container);
$app = AppFactory::create();
$app->setBasePath($PATH);

//ROUTES
require_once $location.'app/routes/anuncio.route.php';
require_once $location.'app/routes/categoria.route.php';
require_once $location.'app/routes/noticia.route.php';
require_once $location.'app/routes/imagen.route.php';
require_once $location.'app/routes/adjunto.route.php';
require_once $location.'app/routes/usuario.route.php';
require_once $location.'app/routes/refresh.route.php';

$app->run();
