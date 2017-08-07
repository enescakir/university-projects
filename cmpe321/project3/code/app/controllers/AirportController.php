<?php

namespace App\Controllers;

use Core\Auth;
use App\Model\Airport;

class AirportController
{

  public function __construct()
  {
    if (!Auth::check('administrator')) {
      return redirect('login');
    }
  }

  public function index()
  {
    $airports = Airport::all();
    return view('airport/index', compact('airports'));
  }

  public function create()
  {
    return view('airport/create');
  }

  public function store()
  {
    Airport::create([
      'name'       => $_POST['name'],
      'city'       => $_POST['city'],
      'created_at' => date("Y-m-d H:i:s"),
      'created_by' => Auth::user()->id,
    ]);
    set_flash_message('success', "{$_POST['name']} is created succesfully");
    return redirect('airport');
  }

  public function show($id)
  {
    dd(Airport::find($id));
  }

  public function edit($id)
  {
    $airport = Airport::find($id);
    return view('airport/edit', compact('airport'));
  }

  public function update($id)
  {
    $airport = Airport::find($id);
    $airport->update([
      'name'       => $_POST['name'],
      'city'       => $_POST['city'],
      'updated_at' => date("Y-m-d H:i:s"),
      'updated_by' => Auth::user()->id,
    ]);
    set_flash_message('success', "{$airport->name} is updated succesfully");
    return redirect('airport');
  }

  public function delete($id)
  {
    $airport = Airport::find($id);
    $airport->delete();
    return json_encode($airport);
  }

}
