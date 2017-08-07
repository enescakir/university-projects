<?php partial_view('header') ?>

<div class="row">
  <div class="col-md-12">
    <div class="panel panel-default">
      <div class="panel-heading">
        <h3 class="panel-title pull-left">Reservations</h3>
        <div class="btn-group pull-right" role="group">
          <a class="btn btn-success" href="\reservation\flight"><i class="fa fa-plus"></i> Create</a>
        </div>
        <div class="clearfix"></div>
      </div>
      <div class="panel-body">
        <div class="row">
          <div class="col-md-12">
            <div class="table-responsive">
              <table id="reservations-table" class="table table-bordered table-striped table-hover">
                <thead>
                  <tr>
                    <th>#</th>
                    <th>PNR</th>
                    <th>Seat</th>
                    <th>Customer</th>
                    <th>Flight</th>
                    <th>Employee</th>
                    <th>Created at</th>
                    <th class="two-button"></th>
                  </tr>
                </thead>
                <tbody>
                  <?php foreach ($reservations as $reservation) : ?>
                    <tr id='reservation-<?= $reservation->id ?>'>
                      <td><?= $reservation->id ?></td>
                      <td><?= $reservation->pnr ?></td>
                      <td><?= $reservation->seat ?></td>
                      <td><?= $reservation->c_name ?></td>
                      <td><?= $reservation->f_number ?></td>
                      <td><?= $reservation->e_name ?></td>
                      <td><?= date("d-m-Y H:i", strtotime($reservation->created_at)) ?></td>
                      <td>
                        <div class="btn-group" role="group">
                          <a class="btn btn-warning btn-xs" href="\reservation\edit\<?= $reservation->id ?>"><i class="fa fa-edit"></i></a>
                          <button class="delete btn btn-danger btn-xs" type="button"
                          reservation-id="<?= $reservation->id ?>"
                          reservation-name="<?= $reservation->c_name ?>"><i class="fa fa-trash"></i>
                        </button>
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
<script type="text/javascript">
  deleteItem("reservation", "reservation-id", "reservation-name", "")
</script>
<?php partial_view('footer') ?>
