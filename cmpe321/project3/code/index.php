<?php

require 'vendor/autoload.php';
require 'core/bootstrap.php';

use Core\{Router, Request};

if (!session_id()) @session_start();

Router::load('app/routes.php')
  ->direct(Request::uri(), Request::method(), Request::parameter());


// TODO: Add Admin Profile page
// TODO: Check unique email
