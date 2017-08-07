<?php partial_view('header') ?>

<div class="row">
  <div class="col-md-12">
    <div class="panel panel-default">
      <div class="panel-heading">
        <h3 class="panel-title pull-left">Airports</h3>
        <div class="btn-group pull-right" role="group">
          <a class="btn btn-success" href="\airport\create"><i class="fa fa-plus"></i> Create</a>
        </div>
        <div class="clearfix"></div>
      </div>
      <div class="panel-body">
        <div class="row">
          <div class="col-md-12">
            <div class="table-responsive">
              <table id="airports-table" class="table table-bordered table-striped table-hover">
                <thead>
                  <tr>
                    <th>#</th>
                    <th>Name</th>
                    <th>City</th>
                    <th class="two-button"></th>
                  </tr>
                </thead>
                <tbody>
                  <?php foreach ($airports as $airport) : ?>
                    <tr id='airport-<?= $airport->id ?>'>
                      <td><?= $airport->id ?></td>
                      <td><?= $airport->name ?></td>
                      <td><?= $airport->city ?></td>
                      <td>
                        <div class="btn-group" role="group">
                          <a class="btn btn-warning btn-xs" href="\airport\edit\<?= $airport->id ?>"><i class="fa fa-edit"></i></a>
                          <button class="delete btn btn-danger btn-xs" type="button"
                          airport-id="<?= $airport->id ?>"
                          airport-name="<?= $airport->name ?>"><i class="fa fa-trash"></i>
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
  deleteItem("airport", "airport-id", "airport-name", "")
</script>
<?php partial_view('footer') ?>
