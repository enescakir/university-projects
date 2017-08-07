<?php

namespace App\Controllers;
use Core\App;

class DatabaseController
{

  public function index()
  {
    return view('database');
  }

  public function createTables()
  {
    // Administrator table
    App::get('database')->createTable("administrators", [
        "id"           => "INT(6) UNSIGNED AUTO_INCREMENT",
        "name"         => "VARCHAR(255) NOT NULL",
        "email"        => "VARCHAR(255) NOT NULL",
        "password"     => "VARCHAR(255) NOT NULL",
        "activated_at" => "DATETIME",
      ],[
        "id"         => ["primary"],
      ]
    );

    // Employee table
    App::get('database')->createTable("employees", [
        "id"         => "INT(6) UNSIGNED AUTO_INCREMENT",
        "name"       => "VARCHAR(255) NOT NULL",
        "email"      => "VARCHAR(255) NOT NULL",
        "password"   => "VARCHAR(255) NOT NULL",
        "created_by" => "INT(6) UNSIGNED",
        "created_at" => "DATETIME",
        "updated_by" => "INT(6) UNSIGNED",
        "updated_at" => "DATETIME",
      ],[
        "id"         => ["primary"],
        "created_by" => ["foreing", "administrators", "id", "SET NULL"],
        "updated_by" => ["foreing", "administrators", "id", "SET NULL"],
      ]
    );

    // Pilot table
    App::get('database')->createTable("pilots", [
        "id"         => "INT(6) UNSIGNED AUTO_INCREMENT",
        "name"       => "VARCHAR(255) NOT NULL",
        "age"        => "INT(6) UNSIGNED",
        "created_by" => "INT(6) UNSIGNED",
        "created_at" => "DATETIME",
        "updated_by" => "INT(6) UNSIGNED",
        "updated_at" => "DATETIME",
      ],[
        "id"         => ["primary"],
        "created_by" => ["foreing", "administrators", "id", "SET NULL"],
        "updated_by" => ["foreing", "administrators", "id", "SET NULL"],
      ]
    );

    // Airport table
    App::get('database')->createTable("airports", [
        "id"         => "INT(6) UNSIGNED AUTO_INCREMENT",
        "name"       => "VARCHAR(255) NOT NULL",
        "city"       => "VARCHAR(255) NOT NULL",
        "created_by" => "INT(6) UNSIGNED",
        "created_at" => "DATETIME",
        "updated_by" => "INT(6) UNSIGNED",
        "updated_at" => "DATETIME",
      ],[
        "id"         => ["primary"],
        "created_by" => ["foreing", "administrators", "id", "SET NULL"],
        "updated_by" => ["foreing", "administrators", "id", "SET NULL"],
      ]
    );

    // Airplane table
    App::get('database')->createTable("airplanes", [
        "id"         => "INT(6) UNSIGNED AUTO_INCREMENT",
        "model"      => "VARCHAR(255) NOT NULL",
        "age"        => "INT(6) UNSIGNED",
        "vertical"   => "INT(6) UNSIGNED",
        "horizantal" => "INT(6) UNSIGNED",
        "created_by" => "INT(6) UNSIGNED",
        "created_at" => "DATETIME",
        "updated_by" => "INT(6) UNSIGNED",
        "updated_at" => "DATETIME",
      ],[
        "id"         => ["primary"],
        "created_by" => ["foreing", "administrators", "id", "SET NULL"],
        "updated_by" => ["foreing", "administrators", "id", "SET NULL"],
      ]
    );

    // Flight table
    App::get('database')->createTable("flights", [
        "id"             => "INT(6) UNSIGNED AUTO_INCREMENT",
        "number"         => "VARCHAR(30) NOT NULL",
        "departured_at"  => "DATETIME",
        "pilot_id"       => "INT(6) UNSIGNED",
        "departure_id"   => "INT(6) UNSIGNED",
        "destination_id" => "INT(6) UNSIGNED",
        "airplane_id"    => "INT(6) UNSIGNED",
        "created_by"     => "INT(6) UNSIGNED",
        "created_at"     => "DATETIME",
        "updated_by"     => "INT(6) UNSIGNED",
        "updated_at"     => "DATETIME",
      ],[
        "id"             => ["primary"],
        "pilot_id"       => ["foreing", "pilots", "id", "SET NULL"],
        "departure_id"   => ["foreing", "airports", "id", "CASCADE"],
        "destination_id" => ["foreing", "airports", "id", "CASCADE"],
        "airplane_id"    => ["foreing", "airplanes", "id", "SET NULL"],
        "created_by"     => ["foreing", "administrators", "id", "SET NULL"],
        "updated_by"     => ["foreing", "administrators", "id", "SET NULL"],
      ]
    );

    // Customer table
    App::get('database')->createTable("customers", [
        "id"          => "INT(6) UNSIGNED AUTO_INCREMENT",
        "name"        => "VARCHAR(255) NOT NULL",
        "created_by"  => "INT(6) UNSIGNED",
        "created_at"  => "DATETIME",
        "updated_by"  => "INT(6) UNSIGNED",
        "updated_at"  => "DATETIME",
      ],[
        "id"         => ["primary"],
        "created_by" => ["foreing", "employees", "id", "SET NULL"],
        "updated_by" => ["foreing", "employees", "id", "SET NULL"],
      ]
    );

    // Reservation table
    App::get('database')->createTable("reservations", [
        "id"            => "INT(6) UNSIGNED AUTO_INCREMENT",
        "pnr"           => "VARCHAR(15) NOT NULL",
        "seat"          => "VARCHAR(10) NOT NULL",
        "flight_id"     => "INT(6) UNSIGNED",
        "employee_id"   => "INT(6) UNSIGNED",
        "customer_id"   => "INT(6) UNSIGNED",
        "customer_name" => "VARCHAR(255)",
        "created_by"    => "INT(6) UNSIGNED",
        "created_at"    => "DATETIME",
        "updated_by"    => "INT(6) UNSIGNED",
        "updated_at"    => "DATETIME",
      ],[
        "id"          => ["primary"],
        "flight_id"   => ["foreing", "flights", "id", "CASCADE"],
        "customer_id" => ["foreing", "customers", "id", "CASCADE"],
        "created_by"  => ["foreing", "employees", "id", "SET NULL"],
        "employee_id" => ["foreing", "employees", "id", "SET NULL"],
        "updated_by"  => ["foreing", "employees", "id", "SET NULL"],
      ]
    );

    return redirect('database');
  }

  public function deleteTables()
  {
    App::get('database')->deleteTable("administrators", true);
    App::get('database')->deleteTable("employees", true);
    App::get('database')->deleteTable("pilots", true);
    App::get('database')->deleteTable("airports", true);
    App::get('database')->deleteTable("airplanes", true);
    App::get('database')->deleteTable("flights", true);
    App::get('database')->deleteTable("customers", true);
    App::get('database')->deleteTable("reservations", true);

    return redirect('database');
  }

  public function createProcedures()
  {
    App::get('database')->createProcedure("past_flights", "IN pid VARCHAR(10)",
      "
        IF pid = \"ALL\" THEN
          SELECT f.*, des.name as des_name, des.city as des_city, dep.name as dep_name, dep.city as dep_city, p.name as p_name, ap.model as ap_model
          FROM flights as f
          JOIN airports as des ON des.id = f.destination_id
          JOIN airports as dep ON dep.id = f.departure_id
          JOIN pilots as p ON p.id = f.pilot_id
          JOIN airplanes as ap ON ap.id = f.airplane_id
      		WHERE f.departured_at < NOW();
      	ELSE
          SELECT f.*, des.name as des_name, des.city as des_city, dep.name as dep_name, dep.city as dep_city, p.name as p_name, ap.model as ap_model
          FROM flights as f
          JOIN airports as des ON des.id = f.destination_id
          JOIN airports as dep ON dep.id = f.departure_id
          JOIN pilots as p ON p.id = f.pilot_id
          JOIN airplanes as ap ON ap.id = f.airplane_id
          WHERE f.departured_at < NOW() AND f.pilot_id = pid;
      	END IF;
      "
    );

    App::get('database')->createProcedure("future_flights", "IN pid VARCHAR(10)",
      "
        IF pid = \"ALL\" THEN
          SELECT f.*, des.name as des_name, des.city as des_city, dep.name as dep_name, dep.city as dep_city, p.name as p_name, ap.model as ap_model
          FROM flights as f
          JOIN airports as des ON des.id = f.destination_id
          JOIN airports as dep ON dep.id = f.departure_id
          JOIN pilots as p ON p.id = f.pilot_id
          JOIN airplanes as ap ON ap.id = f.airplane_id
          WHERE f.departured_at > NOW();
      	ELSE
          SELECT f.*, des.name as des_name, des.city as des_city, dep.name as dep_name, dep.city as dep_city, p.name as p_name, ap.model as ap_model
          FROM flights as f
          JOIN airports as des ON des.id = f.destination_id
          JOIN airports as dep ON dep.id = f.departure_id
          JOIN pilots as p ON p.id = f.pilot_id
          JOIN airplanes as ap ON ap.id = f.airplane_id
          WHERE f.departured_at > NOW() AND f.pilot_id = pid;
      	END IF;
      "
    );

    App::get('database')->createProcedure("full_seats", "IN f_id VARCHAR(10)",
      "
        SELECT seat FROM reservations WHERE flight_id = f_id;
      "
    );

    return redirect('database');
  }

  public function deleteProcedures()
  {
    App::get('database')->deleteProcedure("past_flights");
    App::get('database')->deleteProcedure("future_flights");
    App::get('database')->deleteProcedure("full_seats");

    return redirect('database');
  }

  public function createTriggers()
  {
    App::get('database')->createTrigger("check_customer", "BEFORE INSERT ON reservations",
      "
      IF EXISTS (SELECT * FROM customers WHERE name LIKE NEW.customer_name) THEN
  			SET NEW.customer_id = (SELECT id FROM customers WHERE name LIKE NEW.customer_name LIMIT 1);
        SET NEW.customer_name = NULL;
  		ELSE
  			INSERT INTO customers(name) VALUES (NEW.customer_name);
  			SET NEW.customer_id = (SELECT LAST_INSERT_ID());
  			SET NEW.customer_name = NULL;
  		END IF;
      "
    );

    return redirect('database');
  }

  public function deleteTriggers()
  {
    App::get('database')->deleteTrigger("check_customer");

    return redirect('database');
  }
}
