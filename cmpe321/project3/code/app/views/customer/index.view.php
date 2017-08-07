<?php partial_view('header') ?>

<div class="row">
  <div class="col-md-12">
    <div class="panel panel-default">
      <div class="panel-heading">
        <h3 class="panel-title">Customers</h3>
      </div>
      <div class="panel-body">
        <div class="row">
          <div class="col-md-12">
            <div class="table-responsive">
              <table id="customers-table" class="table table-bordered table-striped table-hover">
                <thead>
                  <tr>
                    <th>#</th>
                    <th>Name</th>
                    <th># of Resevations</th>
                    <th class="two-button"></th>
                  </tr>
                </thead>
                <tbody>
                  <?php foreach ($customers as $customer) : ?>
                    <tr id='customer-<?= $customer->id ?>'>
                      <td><?= $customer->id ?></td>
                      <td><?= $customer->name ?></td>
                      <td><?= $customer->reservation_count ?></td>
                      <td>
                        <div class="btn-group" role="group">
                          <a class="btn btn-warning btn-xs" href="\customer\edit\<?= $customer->id ?>"><i class="fa fa-edit"></i></a>
                          <button class="delete btn btn-danger btn-xs" type="button"
                          customer-id="<?= $customer->id ?>"
                          customer-name="<?= $customer->name ?>"><i class="fa fa-trash"></i>
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
  deleteItem("customer", "customer-id", "customer-name", "")
</script>
<?php partial_view('footer') ?>
