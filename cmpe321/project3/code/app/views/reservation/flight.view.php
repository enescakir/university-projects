<?php partial_view('header') ?>

<div class="row">
  <div class="col-md-12">
    <div class="panel panel-default">
      <div class="panel-heading">
        <h3 class="panel-title pull-left">Flights</h3>
      </div>
      <div class="panel-body">
        <div class="row">
          <div class="col-md-12">
            <div class="table-responsive">
              <table id="flights-table" class="table table-bordered table-striped table-hover">
                <thead>
                  <tr>
                    <th>#</th>
                    <th>Number</th>
                    <th>Departure</th>
                    <th>Destination</th>
                    <th>Pilot</th>
                    <th>Airplane</th>
                    <th>Full Seat #</th>
                    <th>Total Seat #</th>
                    <th>Departure Time</th>
                    <th class="one-button"></th>
                  </tr>
                </thead>
                <tbody>
                  <?php foreach ($flights as $flight) : ?>
                    <tr id='flight-<?= $flight->id ?>' class="<?= $flight->departured_at > date("Y-m-d H:i:s") ? "success" : "warning" ?>">
                      <td><?= $flight->id ?></td>
                      <td><?= $flight->number ?></td>
                      <td><?= $flight->dep_name ?> (<?= $flight->dep_city ?>)</td>
                      <td><?= $flight->des_name ?> (<?= $flight->des_city ?>)</td>
                      <td><?= $flight->p_name ?></td>
                      <td><?= $flight->ap_model ?></td>
                      <td><?= count($flight->full_seats()) ?></td>
                      <td><?= $flight->airplane()->vertical * $flight->airplane()->horizantal ?></td>
                      <td><?= date("d-m-Y H:i", strtotime($flight->departured_at)) ?></td>
                      <td>
                        <div class="btn-group" role="group">
                          <a class="btn btn-success btn-xs" href="\reservation\create?flight_id=<?= $flight->id ?>"><i class="fa fa-ticket"></i></a>
                      </div>
                    </td>
                  </tr>
                <?php endforeach; ?>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
</div>
<?php partial_view('scripts') ?>
<?php partial_view('footer') ?>
