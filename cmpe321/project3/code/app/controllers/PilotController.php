<?php

namespace App\Controllers;

use Core\Auth;
use App\Model\Pilot;

class PilotController
{

  public function __construct()
  {
    if (!Auth::check('administrator')) {
      return redirect('login');
    }
  }

  public function index()
  {
    $pilots = Pilot::all();
    return view('pilot/index', compact('pilots'));
  }

  public function create()
  {
    return view('pilot/create');
  }

  public function store()
  {
    Pilot::create([
      'name'      => $_POST['name'],
      'age'        => $_POST['age'],
      'created_at' => date("Y-m-d H:i:s"),
      'created_by' => Auth::user()->id,
    ]);
    set_flash_message('success', "{$_POST['name']} is created succesfully");
    return redirect('pilot');
  }

  public function show($id)
  {
    dd(Pilot::find($id));
  }

  public function edit($id)
  {
    $pilot = Pilot::find($id);
    return view('pilot/edit', compact('pilot'));
  }

  public function update($id)
  {
    $pilot = Pilot::find($id);
    $pilot->update([
      'name'      => $_POST['name'],
      'age'        => $_POST['age'],
      'updated_at' => date("Y-m-d H:i:s"),
      'updated_by' => Auth::user()->id,
    ]);
    set_flash_message('success', "{$pilot->name} is updated succesfully");
    return redirect('pilot');
  }

  public function delete($id)
  {
    $pilot = Pilot::find($id);
    $pilot->delete();
    return json_encode($pilot);
  }

}
