<?php

namespace Core;

class Router
{
  /**
  * All registered routes.
  *
  * @var array
  */
  public $routes = [
    'GET' => [],
    'POST' => [],
    'PUT' => [],
    'DELETE' => [],
  ];

  /**
  * Load a user's routes file.
  *
  * @param string $file
  */
  public static function load($file)
  {
    $router = new static;

    require $file;

    return $router;
  }

  /**
  * Register a GET route.
  *
  * @param string $uri
  * @param string $controller
  */
  public function get($uri, $controller)
  {
    $this->routes['GET'][$uri] = $controller;
  }

  /**
  * Register a POST route.
  *
  * @param string $uri
  * @param string $controller
  */
  public function post($uri, $controller)
  {
    $this->routes['POST'][$uri] = $controller;
  }

  /**
  * Register a PUT route.
  *
  * @param string $uri
  * @param string $controller
  */
  public function put($uri, $controller)
  {
    $this->routes['PUT'][$uri] = $controller;
  }

  /**
  * Register a DELETE route.
  *
  * @param string $uri
  * @param string $controller
  */
  public function delete($uri, $controller)
  {
    $this->routes['DELETE'][$uri] = $controller;
  }

  /**
  * Register a Resources route.
  *
  * @param string $uri
  * @param string $controller
  */
  public function resource($uri, $controller)
  {
    $this->get($uri, "{$controller}@index");
    $this->get("{$uri}/create", "{$controller}@create");
    $this->post($uri, "{$controller}@store");
    $this->get("{$uri}/show", "{$controller}@show");
    $this->get("{$uri}/edit", "{$controller}@edit");
    $this->post("{$uri}/update", "{$controller}@update");
    $this->delete("{$uri}/delete", "{$controller}@delete");
  }

  /**
  * Load the requested URI's associated controller method.
  *
  * @param string $uri
  * @param string $requestType
  */
  public function direct($uri, $requestType, $parameter)
  {
    if (array_key_exists($uri, $this->routes[$requestType])) {
      return $this->callAction(
        explode('@', $this->routes[$requestType][$uri])[0],
        explode('@', $this->routes[$requestType][$uri])[1],
        $parameter
      );
    }

    throw new \Exception('No route defined for this URI.');
  }

  /**
  * Load and call the relevant controller action.
  *
  * @param string $controller
  * @param string $action
  */
  protected function callAction($controller, $action, $parameter)
  {
    $controller = "App\\Controllers\\{$controller}";
    $controller = new $controller;

    if (! method_exists($controller, $action)) {
      throw new \Exception(
        "{$controller} does not respond to the {$action} action."
      );
    }

    return $controller->$action($parameter);
  }
}
