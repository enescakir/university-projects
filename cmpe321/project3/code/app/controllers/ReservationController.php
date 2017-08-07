<?php

namespace App\Controllers;

use Core\Auth;
use App\Model\Reservation;
use App\Model\Flight;
use App\Model\Airplane;

class ReservationController
{

  public function __construct()
  {
    if (!Auth::check('employee')) {
      return redirect('login');
    }
  }

  public function index()
  {
    $reservations = Reservation::allWithRelations();
    return view('reservation/index', compact('reservations'));
  }

  public function create()
  {
    $flight = Flight::find($_GET['flight_id']);
    $full_seats = $flight->full_seats();
    $airplane = Airplane::find($flight->airplane_id);
    return view('reservation/create', compact(['flight', 'airplane', 'full_seats']));
  }

  public function flights()
  {
    $flights = Flight::future();
    return view('reservation/flight', compact(['flights']));
  }

  public function store()
  {
    $pnr = strtoupper(str_random(10));
    Reservation::create([
      'pnr'           => $pnr,
      'seat'          => $_POST['seat'],
      'flight_id'     => $_POST['flight_id'],
      'customer_name' => $_POST['name'],
      'created_at'    => date("Y-m-d H:i:s"),
      'employee_id'   => Auth::user()->id,
      'created_by'    => Auth::user()->id,
    ]);
    set_flash_message('success', "{$pnr} is created succesfully");
    return redirect('reservation');
  }

  public function show($id)
  {
    dd(Reservation::find($id));
  }

  public function edit($id)
  {
    $reservation = Reservation::findWithRelations($id);
    $flight = Flight::find($reservation->flight_id);
    $full_seats = $flight->full_seats();
    $airplane = Airplane::find($flight->airplane_id);
    return view('reservation/edit', compact('reservation', 'flight', 'airplane', 'full_seats'));
  }

  public function update($id)
  {
    $reservation = Reservation::find($id);
    $reservation->update([
      'seat'        => $_POST['seat'],
      'updated_at' => date("Y-m-d H:i:s"),
      'updated_by' => Auth::user()->id,
    ]);
    set_flash_message('success', "{$reservation->pnr} is updated succesfully");
    return redirect('reservation');
  }

  public function delete($id)
  {
    $reservation = Reservation::find($id);
    $reservation->delete();
    return json_encode($reservation);
  }

}
