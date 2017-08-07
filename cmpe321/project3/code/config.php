<?php

return [
  'path'   => '/',
  'public_path' => 'PHP/cmpe321/project3/public',
  'token'   => 'PHP/cmpe321/project3',
  'database' => [
    'driver'   => 'mysql',
    'host'     => '127.0.0.1',
    'name'     => 'enescakir_cmpe321',
    'username' => 'root',
    'password' => '',
    'options'  => [
      PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION
    ],
  ],
];
