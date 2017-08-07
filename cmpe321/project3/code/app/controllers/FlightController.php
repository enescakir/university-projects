<?php

namespace App\Controllers;

use Core\Auth;
use App\Model\Pilot;
use App\Model\Airport;
use App\Model\Airplane;
use App\Model\Flight;

class FlightController
{

  public function __construct()
  {
    if (!Auth::check('administrator')) {
      return redirect('login');
    }
  }

  public function index()
  {
    $flights = Flight::allWithRelations();
    return view('flight/index', compact('flights'));
  }

  public function create()
  {
    $pilots    = Pilot::all();
    $airports  = Airport::all();
    $airplanes = Airplane::all();
    return view('flight/create', compact(['pilots', 'airports', 'airplanes']));
  }

  public function store()
  {
    Flight::create([
      'number'         => $_POST['number'],
      'departured_at'  => date_format(date_create_from_format('d/m/Y H:i', $_POST['departured_at']), "Y-m-d H:i:s"),
      'departure_id'   => $_POST['departure_id'],
      'destination_id' => $_POST['destination_id'],
      'airplane_id'    => $_POST['airplane_id'],
      'pilot_id'       => $_POST['pilot_id'],
      'created_at'     => date("Y-m-d H:i:s"),
      'created_by'     => Auth::user()->id,
    ]);
    set_flash_message('success', "{$_POST['number']} is created succesfully");
    return redirect('flight');
  }

  public function show($id)
  {
    dd(Flight::find($id));
  }

  public function edit($id)
  {
    $flight    = Flight::find($id);
    $pilots    = Pilot::all();
    $airports  = Airport::all();
    $airplanes = Airplane::all();

    return view('flight/edit', compact(['flight', 'pilots', 'airports', 'airplanes']));
  }

  public function update($id)
  {
    $flight = Flight::find($id);
    $flight->update([
      'number'         => $_POST['number'],
      'departured_at'  => date_format(date_create_from_format('d/m/Y H:i', $_POST['departured_at']), "Y-m-d H:i:s"),
      'departure_id'   => $_POST['departure_id'],
      'destination_id' => $_POST['destination_id'],
      'airplane_id'    => $_POST['airplane_id'],
      'pilot_id'       => $_POST['pilot_id'],
      'updated_at'     => date("Y-m-d H:i:s"),
      'updated_by'     => Auth::user()->id,
    ]);
    set_flash_message('success', "{$flight->number} is updated succesfully");
    return redirect('flight');
  }

  public function delete($id)
  {
    $flight = Flight::find($id);
    $flight->delete();
    return json_encode($flight);
  }

}
