 <?php

$router->get('', 'AdminController@dashboard');
$router->get('login', 'AdminController@showLogin');
$router->post('login', 'AdminController@login');
$router->get('register', 'AdminController@showRegister');
$router->post('register', 'AdminController@register');
$router->get('logout', 'AdminController@logout');

$router->get('administrator', 'AdministratorController@index');
$router->post('administrator/activate', 'AdministratorController@activate');

$router->resource('employee', 'EmployeeController');
$router->resource('airport', 'AirportController');
$router->resource('airplane', 'AirplaneController');
$router->resource('pilot', 'PilotController');
$router->resource('flight', 'FlightController');
$router->resource('customer', 'CustomerController');
$router->resource('reservation', 'ReservationController');
$router->get('reservation/flight', 'ReservationController@flights');

$router->get('database', 'DatabaseController@index');
$router->get('database/table/create', 'DatabaseController@createTables');
$router->get('database/table/delete', 'DatabaseController@deleteTables');
$router->get('database/procedure/create', 'DatabaseController@createProcedures');
$router->get('database/procedure/delete', 'DatabaseController@deleteProcedures');
$router->get('database/trigger/create', 'DatabaseController@createTriggers');
$router->get('database/trigger/delete', 'DatabaseController@deleteTriggers');
